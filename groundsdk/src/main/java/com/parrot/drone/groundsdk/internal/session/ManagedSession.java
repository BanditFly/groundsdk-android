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

package com.parrot.drone.groundsdk.internal.session;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.parrot.drone.groundsdk.ManagedGroundSdk;

/**
 * A Session that is automatically managed by {@link SessionManager} based on the attached activity lifecycle.
 */
final class ManagedSession extends Session {

    /**
     * Observer behaviour for this session, defines whether observers resume/suspend must occur at activity
     * onStart/onStop or at activity onResume/onPause.
     */
    @NonNull
    private final ManagedGroundSdk.ObserverBehavior mObserverBehavior;

    /**
     * Constructor.
     *
     * @param observerBehavior observer behaviour this session should comply to
     */
    ManagedSession(@NonNull ManagedGroundSdk.ObserverBehavior observerBehavior) {
        mObserverBehavior = observerBehavior;
    }

    /**
     * Called back by {@link SessionManager} when the attached activity calls {@link Activity#onStart()}.
     */
    void onActivityStarted() {
        if (mObserverBehavior == ManagedGroundSdk.ObserverBehavior.NOTIFY_ON_START) {
            resumeObservers();
        }
    }

    /**
     * Called back by {@link SessionManager} when the attached activity calls {@link Activity#onResume()}.
     */
    void onActivityResumed() {
        if (mObserverBehavior == ManagedGroundSdk.ObserverBehavior.NOTIFY_ON_RESUME) {
            resumeObservers();
        }
    }

    /**
     * Called back by {@link SessionManager} when the attached activity calls {@link Activity#onPause()}.
     */
    void onActivityPaused() {
        if (mObserverBehavior == ManagedGroundSdk.ObserverBehavior.NOTIFY_ON_RESUME) {
            suspendObservers();
        }
    }


    /**
     * Called back by {@link SessionManager} when the attached activity calls {@link Activity#onStop()}.
     */
    void onActivityStopped() {
        if (mObserverBehavior == ManagedGroundSdk.ObserverBehavior.NOTIFY_ON_START) {
            suspendObservers();
        }
    }
}
