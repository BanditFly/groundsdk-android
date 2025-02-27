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

package com.parrot.drone.groundsdk.arsdkengine.blackbox;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.parrot.drone.sdkcore.arsdk.command.ArsdkCommand;
import com.parrot.drone.sdkcore.ulog.ULog;

import static com.parrot.drone.groundsdk.arsdkengine.Logging.TAG_BLACKBOX;

/**
 * Base black box recording session.
 */
public abstract class BlackBoxSession {

    /**
     * Interface for receiving blackbox session close notifications.
     */
    public interface CloseListener {

        /**
         * Called back when the blackbox session is closed.
         */
        void onBlackBoxSessionClosed();
    }

    /** Recording context the session lives in. */
    @NonNull
    final BlackBoxRecorder.Context mContext;

    /** Listener to notify when the session closes. */
    @NonNull
    private final CloseListener mCloseListener;

    /**
     * Constructor.
     *
     * @param context       recording context that manages the session
     * @param closeListener listener notified when the session gets closed
     */
    BlackBoxSession(@NonNull BlackBoxRecorder.Context context, @NonNull CloseListener closeListener) {
        mContext = context;
        mCloseListener = closeListener;
    }

    /**
     * Dispatches a received command to the session for processing.
     *
     * @param command received command to process
     */
    public abstract void onCommandReceived(@NonNull ArsdkCommand command);

    /**
     * Closes the session.
     */
    @CallSuper
    public void close() {
        mCloseListener.onBlackBoxSessionClosed();
        if (ULog.d(TAG_BLACKBOX)) {
            ULog.d(TAG_BLACKBOX, "Closed blackbox session: " + System.identityHashCode(this));
        }
    }
}
