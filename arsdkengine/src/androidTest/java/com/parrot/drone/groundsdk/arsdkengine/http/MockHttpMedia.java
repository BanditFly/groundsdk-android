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

package com.parrot.drone.groundsdk.arsdkengine.http;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;

public final class MockHttpMedia {

    public static HttpMediaItem item(@Nullable String id, @Nullable HttpMediaItem.Type type, @Nullable Date date,
                                     @IntRange(from = 0) long size, @Nullable String runId,
                                     @IntRange(from = 0) int expectedCount, @Nullable String thumbnailUrl,
                                     @Nullable String streamUrl, @Nullable HttpMediaItem.Location location,
                                     @Nullable HttpMediaItem.PhotoMode photoMode,
                                     @Nullable HttpMediaItem.PanoramaType panoramaType,
                                     boolean hasThermalMetaData,
                                     @NonNull List<HttpMediaItem.Resource> resources) {
        return new HttpMediaItem(id, type, date, size, runId, expectedCount, thumbnailUrl, streamUrl, location,
                photoMode,
                panoramaType, hasThermalMetaData, resources);
    }

    public static HttpMediaItem.Location location(double latitude, double longitude, double altitude) {
        return new HttpMediaItem.Location(latitude, longitude, altitude);
    }

    public static HttpMediaItem.Resource resource(@Nullable String mediaId, @Nullable String resourceId,
                                                  @Nullable HttpMediaItem.Resource.Type type,
                                                  @Nullable HttpMediaItem.Resource.Format format, @Nullable Date date,
                                                  @IntRange(from = 0) long size, @IntRange(from = 0) int duration,
                                                  @Nullable String url, @Nullable String thumbnailUrl,
                                                  @Nullable String streamUrl,
                                                  @Nullable HttpMediaItem.Location location,
                                                  @IntRange(from = 0) int width, @IntRange(from = 0) int height,
                                                  boolean hasThermalMetaData) {
        return new HttpMediaItem.Resource(mediaId, resourceId, type, format, date, size, duration, url, thumbnailUrl,
                streamUrl, location, width, height, hasThermalMetaData);
    }

    private MockHttpMedia() {
    }
}
