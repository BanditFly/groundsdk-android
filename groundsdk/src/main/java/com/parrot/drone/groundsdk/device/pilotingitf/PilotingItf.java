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

package com.parrot.drone.groundsdk.device.pilotingitf;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.parrot.drone.groundsdk.Ref;

/**
 * Base class for an Piloting Interface.
 */
public interface PilotingItf {

    /**
     * An interface for an object capable of providing a {@link PilotingItf}.
     */
    interface Provider {

        /**
         * Gets a piloting interface.
         *
         * @param pilotingItfClass class of the piloting interface
         * @param <PI>             type of the piloting interface class
         *
         * @return requested piloting interface, or {@code null} if it's not present
         */
        @Nullable
        <PI extends PilotingItf> PI getPilotingItf(@NonNull Class<PI> pilotingItfClass);

        /**
         * Gets a piloting interface and registers an observer notified each time it changes.
         *
         * @param pilotingItfClass class of the piloting interface
         * @param observer         observer to notify when the piloting interface changes
         * @param <PI>             type of the piloting interface class
         *
         * @return reference to the requested piloting interface
         */
        @NonNull
        <PI extends PilotingItf> Ref<PI> getPilotingItf(@NonNull Class<PI> pilotingItfClass,
                                                        @NonNull Ref.Observer<PI> observer);
    }
}
