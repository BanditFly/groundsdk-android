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

package com.parrot.drone.sdkcore.arsdk.backend.net;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.parrot.drone.sdkcore.arsdk.ArsdkCore;

/**
 * Wrapper on native arsdk backend net.
 */
final class ArsdkNetBackend {

    /** Notifies when a socket is about to be created. */
    interface Listener {

        /**
         * Called back when a socket is about to be created.
         *
         * @param socketFd the socket native file descriptor
         */
        void onSocketCreated(int socketFd);
    }

    /** Pointer to native backend. */
    private long mNativePtr;

    /** Listener notified about socket creation. */
    @NonNull
    private final Listener mListener;

    /**
     * Constructor.
     *
     * @param controller backend controller
     * @param arsdkCore  arsdk ctrl instance owning this backend
     * @param listener   listener notified when sockets are about to be created
     */
    ArsdkNetBackend(@NonNull ArsdkWifiBackendController controller, @NonNull ArsdkCore arsdkCore,
                    @NonNull Listener listener) {
        mNativePtr = nativeInit(arsdkCore.getNativePtr(), controller);
        if (mNativePtr == 0) {
            throw new RuntimeException("nativeInit Fail");
        }
        mListener = listener;
    }

    /**
     * Destructor.
     */
    void destroy() {
        nativeRelease(mNativePtr);
        mNativePtr = 0;
    }

    /**
     * Gets parent native backend pointer.
     *
     * @return parent native backend pointer
     */
    long getParentNativePtr() {
        if (mNativePtr == 0) {
            throw new RuntimeException("Destroyed");
        }
        return nativeGetParent(mNativePtr);
    }

    @SuppressWarnings("unused") /* native-cb */
    private void onSocketCreated(int socketFd) {
        mListener.onSocketCreated(socketFd);
    }

    /* JNI declarations and setup */
    private native long nativeInit(long arsdkNativePtr, @Nullable ArsdkWifiBackendController controller);

    private native void nativeRelease(long nativePtr);

    private native long nativeGetParent(long nativePtr);

    private static native void nativeClassInit();

    static {
        nativeClassInit();
    }
}
