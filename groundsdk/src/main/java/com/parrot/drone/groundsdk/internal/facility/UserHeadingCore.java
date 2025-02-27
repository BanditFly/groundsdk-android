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

package com.parrot.drone.groundsdk.internal.facility;

import android.support.annotation.NonNull;

import com.parrot.drone.groundsdk.facility.Facility;
import com.parrot.drone.groundsdk.facility.UserHeading;
import com.parrot.drone.groundsdk.internal.component.ComponentDescriptor;
import com.parrot.drone.groundsdk.internal.component.ComponentStore;
import com.parrot.drone.groundsdk.internal.component.SingletonComponentCore;

/** Core class for the {@link UserHeading} facility. */
public class UserHeadingCore extends SingletonComponentCore implements UserHeading {

    /** Description of UserHeading. */
    private static final ComponentDescriptor<Facility, UserHeading> DESC =
            ComponentDescriptor.of(UserHeading.class);

    /** Backend of a UserHeadingCore which handles the messages. */
    public interface Backend {

        /** Starts monitoring heading. */
        void startMonitoringHeading();

        /** Stops monitoring heading. */
        void stopMonitoringHeading();
    }

    /** Backend of this facility. */
    @NonNull
    private final Backend mBackend;

    /** Current user heading. */
    private double mHeading;

    /**
     * Constructor.
     *
     * @param facilityStore store where this component provider belongs
     * @param backend       backend used to forward actions to the engine
     */
    public UserHeadingCore(@NonNull ComponentStore<Facility> facilityStore, @NonNull Backend backend) {
        super(DESC, facilityStore);
        mBackend = backend;
    }

    @Override
    public double getHeading() {
        return mHeading;
    }

    @Override
    protected void onObserved() {
        mBackend.startMonitoringHeading();
    }

    @Override
    protected void onNoMoreObserved() {
        mBackend.stopMonitoringHeading();
    }

    /**
     * Updates current heading.
     *
     * @param heading new heading
     *
     * @return {@code this}, to allow call chaining
     */
    @NonNull
    public UserHeadingCore updateHeading(double heading) {
        if (Double.compare(mHeading, heading) != 0) {
            mHeading = heading;
            mChanged = true;
        }
        return this;
    }
}
