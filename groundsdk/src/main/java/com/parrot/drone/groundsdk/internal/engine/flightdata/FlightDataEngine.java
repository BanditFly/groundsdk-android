/*
 *     Copyright (C) 2019 Parrot Drones SAS
 *
 *     Redistribution and use in source and binary forms, with or without
 *     modification, are permitted provided that the following conditions
 *     are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in
 *       the documentation and/or other materials provided with the
 *       distribution.
 *     * Neither the name of the Parrot Company nor the names
 *       of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written
 *       permission.
 *
 *     THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *     "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *     LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 *     FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 *     PARROT COMPANY BE LIABLE FOR ANY DIRECT, INDIRECT,
 *     INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *     BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 *     OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 *     AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *     OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *     OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *     SUCH DAMAGE.
 *
 */

package com.parrot.drone.groundsdk.internal.engine.flightdata;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.parrot.drone.groundsdk.internal.ApplicationStorageProvider;
import com.parrot.drone.groundsdk.internal.engine.EngineBase;
import com.parrot.drone.groundsdk.internal.facility.FlightDataManagerCore;
import com.parrot.drone.groundsdk.internal.tasks.Task;
import com.parrot.drone.groundsdk.internal.utility.FlightDataStorage;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Monitors flight data repository on the user device's local file system and allows the application to list and delete
 * them.
 */
public class FlightDataEngine extends EngineBase {

    /** Flight data manager facility for which this engine is the backend. */
    @NonNull
    private final FlightDataManagerCore mManager;

    /**
     * Root directory where flight data files are stored on the user device's local file system.
     * <p>
     * This directory may contain <ul>
     * <li>the current {@link #mWorkDir work directory}, which may itself contain temporary flight data files (being
     * currently downloaded from remote devices) and successfully downloaded flight data files,</li>
     * <li>previous work directories, that may themselves contain successfully downloaded flight data files, or
     * temporary files that failed to be downloaded completely. </li>
     * </ul>
     * When the engine starts, all successfully downloaded flight data files from all work directories are listed.
     * Temporary files in previous work directories (other than {@code mWorkDir}) are deleted. Temporary files in
     * {@code mWorkDir} are left untouched.
     */
    @NonNull
    private final File mEngineDir;

    /** Current work directory where flight data files downloaded from remote devices get stored. */
    @Nullable
    private File mWorkDir;

    /** Keeps track of all locally downloaded flight data files. */
    @NonNull
    private final Set<File> mLocalFiles;

    /** Currently running local file collection task, {@code null} if not running. */
    @Nullable
    private Task mCollectTask;

    /**
     * Constructor.
     *
     * @param controller provides access to the engine's controller; opaque to subclasses, which should forward it
     *                   directly through {@code super(controller)}
     */
    public FlightDataEngine(@NonNull Controller controller) {
        super(controller);
        mLocalFiles = new HashSet<>();
        mManager = new FlightDataManagerCore(getFacilityPublisher(), mBackend);
        mEngineDir = new File(ApplicationStorageProvider.getInstance().getInternalAppFileCache(), "flightdata");
        publishUtility(FlightDataStorage.class, new FlightDataStorageCore(this));
    }

    @Override
    public void onStart() {
        mCollectTask = launchCollectFilesJob();
        mManager.publish();
    }

    @Override
    protected void onStopRequested() {
        acknowledgeStopRequest();
        mManager.unpublish();
        if (mCollectTask != null) {
            mCollectTask.cancel();
        }
        mWorkDir = null;
        mLocalFiles.clear();
    }

    /** Backend of FlightDataManagerCore implementation. */
    @SuppressWarnings("FieldCanBeLocal")
    private final FlightDataManagerCore.Backend mBackend = flightDataFile -> {
        if (flightDataFile.isFile() && flightDataFile.delete()) {
            removeLocalFile(flightDataFile);
            return true;
        }
        return false;
    };

    /**
     * Retrieves the root directory where flight data engine stores files on the device's file system.
     *
     * @return flight data engine root directory
     */
    @NonNull
    File getEngineDirectory() {
        return mEngineDir;
    }

    /**
     * Retrieves the directory where new flight data files may be downloaded on the device's file system.
     *
     * @return work directory
     */
    @NonNull
    File getWorkDirectory() {
        if (mWorkDir == null) {
            mWorkDir = new File(mEngineDir, UUID.randomUUID().toString());
        }
        return mWorkDir;
    }

    /**
     * Adds several locally downloaded flight data files.
     *
     * @param files downloaded flight data files
     */
    void addLocalFiles(@NonNull Collection<File> files) {
        if (mLocalFiles.addAll(files)) {
            mManager.updateFiles(mLocalFiles).notifyUpdated();
        }
    }

    /**
     * Removes a locally downloaded flight data files.
     *
     * @param file downloaded flight data file to remove
     */
    private void removeLocalFile(@NonNull File file) {
        if (mLocalFiles.remove(file)) {
            mManager.updateFiles(mLocalFiles).notifyUpdated();
        }
    }

    /**
     * Launches the collect flight data files background task.
     * <p>
     * Only used by tests to mock the collect task.
     *
     * @return the running collect background task
     */
    @VisibleForTesting
    Task<Collection<File>> launchCollectFilesJob() {
        return new CollectJob(this).launch();
    }
}
