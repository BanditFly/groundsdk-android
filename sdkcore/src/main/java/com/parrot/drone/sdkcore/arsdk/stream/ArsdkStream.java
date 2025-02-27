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

package com.parrot.drone.sdkcore.arsdk.stream;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.parrot.drone.sdkcore.stream.SdkCoreStream;

/**
 * SdkCore stream implementation for ArsdkDevice streams.
 */
class ArsdkStream extends SdkCoreStream {

    /** Stream controller. */
    @NonNull
    private final ArsdkDeviceStreamController mController;

    /** Stream URL. */
    @NonNull
    private final String mUrl;

    /** Stream track. {@code null} for default track. */
    @Nullable
    private final String mTrack;

    /**
     * Constructor.
     * <p>
     * Must be called on main thread.
     *
     * @param controller stream controller
     * @param client     stream client
     * @param url        stream URL
     * @param track      stream track to select, {@code null} to select default track, if any
     */
    ArsdkStream(@NonNull ArsdkDeviceStreamController controller, @NonNull Client client, @NonNull String url,
                @Nullable String track) {
        super(controller.mArsdkCore.pomp(), (stream) -> controller.onStreamClosed((ArsdkStream) stream), client);
        mController = controller;
        mUrl = url;
        mTrack = track;
    }

    /**
     * Opens the stream.
     */
    void open() {
        internalOpen(() -> nativeOpen(mController.mArsdkCore.getNativePtr(), mController.mDeviceHandle, mUrl, mTrack));
    }

    /* JNI declarations and setup */
    private native long nativeOpen(long arsdkNativePtr, short deviceHandle, @NonNull String url,
                                   @Nullable String track);
}
