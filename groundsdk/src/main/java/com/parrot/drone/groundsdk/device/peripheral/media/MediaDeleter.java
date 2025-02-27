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

package com.parrot.drone.groundsdk.device.peripheral.media;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

/**
 * Provides progress and status of a media/resource deletion task.
 */
public interface MediaDeleter {

    /**
     * Retrieves the current status of the deletion task.
     *
     * @return task status
     */
    @NonNull
    MediaTaskStatus getStatus();

    /**
     * Retrieves the amount of media containing resource(s) that the task will delete.
     * <p>
     * This value is fixed for the whole duration of the task.
     *
     * @return total amount of media containing resource(s) to delete
     */
    @IntRange(from = 0)
    int getTotalMediaCount();

    /**
     * Retrieves the index of the media whose resources are currently being deleted.
     * <p>
     * This index is in range [0, {@link #getTotalMediaCount() total media count}] and is incremented each time a set
     * of resources from a given media starts being deleted.
     *
     * @return index of the media whose resources are currently being deleted
     */
    @IntRange(from = 0)
    int getCurrentMediaIndex();
}
