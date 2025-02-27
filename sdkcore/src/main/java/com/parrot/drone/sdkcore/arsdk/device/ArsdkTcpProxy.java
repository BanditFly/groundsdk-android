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

package com.parrot.drone.sdkcore.arsdk.device;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.parrot.drone.sdkcore.arsdk.ArsdkCore;

import javax.net.SocketFactory;

/**
 * TCP Proxy to the device.
 */
public final class ArsdkTcpProxy {

    /**
     * Listener notified when TCP proxy creation completes.
     */
    public interface Listener {

        /**
         * Called back when TCP proxy creation completes.
         * <p>
         * Called back on <strong>MAIN</strong> thread.
         * <p>
         *
         * @param address       proxy IP address on success, otherwise {@code null}
         * @param port          proxy IP port on success, otherwise {@code 0}
         * @param socketFactory factory for creating sockets bound to the network through which this proxy communicates,
         *                      may be {@code null} in case using this proxy does not require binding sockets to a
         *                      specific network
         */
        void onComplete(@Nullable String address, int port, @Nullable SocketFactory socketFactory);
    }

    /**
     * Creates a TCP proxy with the device.
     * <p>
     * Note that {@code deviceHandle} is the handle of the device that will handle the proxy.
     * On the other hand, {@code deviceType} is the type of the device to access with the proxy.
     *
     * @param arsdkCore     ArsdkCore instance
     * @param deviceHandle  handle of the device that will handle the proxy
     * @param deviceType    type of the device to access
     * @param port          port to access
     * @param listener      listener to be notified when TCP proxy creation completes
     * @param socketFactory factory for creating sockets bound to the network through which this proxy communicates,
     *                      may be {@code null} in case using this proxy does not require binding sockets to a
     *                      specific network
     *
     * @return a new ArsdkTcpProxy instance
     */
    @NonNull
    public static ArsdkTcpProxy create(@NonNull ArsdkCore arsdkCore, short deviceHandle,
                                       @ArsdkDevice.Type int deviceType, int port,
                                       @NonNull Listener listener,
                                       @Nullable SocketFactory socketFactory) {
        return new ArsdkTcpProxy(arsdkCore, deviceHandle, deviceType, port, listener, socketFactory);
    }

    /** ArsdkCore instance. */
    @NonNull
    private final ArsdkCore mArsdkCore;

    /** {@code true} when the TCP proxy has been closed. */
    private boolean mClosed;

    /** Native request pointer. */
    private long mNativePtr;

    /**
     * Constructor.
     *
     * @param arsdkCore    ArsdkCore instance
     * @param deviceHandle handle of the device that will handle the proxy
     * @param deviceType   type of the device to access
     * @param port         port to access
     * @param listener     listener to be notified when TCP proxy creation completes
     */
    private ArsdkTcpProxy(@NonNull ArsdkCore arsdkCore, short deviceHandle, @ArsdkDevice.Type int deviceType,
                          int port, @NonNull Listener listener, @Nullable SocketFactory socketFactory) {
        mArsdkCore = arsdkCore;
        arsdkCore.dispatchToPomp(() -> {
            if (!mClosed) {
                mNativePtr = nativeCreate(arsdkCore.getNativePtr(), deviceHandle, deviceType, port);
                int proxyPort = mNativePtr == 0 ? 0 : nativeGetPort(mNativePtr);
                String proxyAddress = mNativePtr == 0 ? null : nativeGetAddr(mNativePtr);
                mArsdkCore.dispatchToMain(() -> listener.onComplete(proxyAddress, proxyPort, socketFactory));
            }
        });
    }

    /**
     * Closes the proxy.
     */
    public void close() {
        if (!mClosed) {
            mClosed = true;
            mArsdkCore.dispatchToPomp(() -> {
                if (mNativePtr != 0) {
                    nativeClose(mNativePtr);
                    mNativePtr = 0;
                }
            });
        }
    }

    /* JNI declarations and setup */
    private native long nativeCreate(long arsdkNativePtr, short deviceHandle, @ArsdkDevice.Type int deviceType,
                                     int port);

    private static native void nativeClose(long nativePtr);

    private static native int nativeGetPort(long nativePtr);

    private static native String nativeGetAddr(long nativePtr);
}
