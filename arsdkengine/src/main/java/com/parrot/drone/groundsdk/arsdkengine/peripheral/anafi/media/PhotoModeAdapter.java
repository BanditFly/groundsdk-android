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

package com.parrot.drone.groundsdk.arsdkengine.peripheral.anafi.media;

import android.support.annotation.NonNull;

import com.parrot.drone.groundsdk.arsdkengine.http.HttpMediaItem;
import com.parrot.drone.groundsdk.device.peripheral.media.MediaItem;

/**
 * Utility class to adapt {@link HttpMediaItem.PhotoMode drone http} to {@link MediaItem.PhotoMode groundsdk} photo
 * modes.
 */
final class PhotoModeAdapter {

    /**
     * Converts an {@code HttpMediaItem.PhotoMode} to its {@code MediaItem.PhotoMode} equivalent.
     *
     * @param mode http photo mode to convert
     *
     * @return the groundsdk media item photo mode equivalent
     */
    @NonNull
    static MediaItem.PhotoMode from(@NonNull HttpMediaItem.PhotoMode mode) {
        switch (mode) {
            case SINGLE:
                return MediaItem.PhotoMode.SINGLE;
            case BRACKETING:
                return MediaItem.PhotoMode.BRACKETING;
            case BURST:
                return MediaItem.PhotoMode.BURST;
            case PANORAMA:
                return MediaItem.PhotoMode.PANORAMA;
            case TIMELAPSE:
                return MediaItem.PhotoMode.TIME_LAPSE;
            case GPSLAPSE:
                return MediaItem.PhotoMode.GPS_LAPSE;
        }
        return null;
    }

    /**
     * Private constructor for static utility class.
     */
    private PhotoModeAdapter() {
    }
}
