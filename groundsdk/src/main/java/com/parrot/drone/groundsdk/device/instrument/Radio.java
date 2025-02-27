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

package com.parrot.drone.groundsdk.device.instrument;

import android.support.annotation.IntRange;

import com.parrot.drone.groundsdk.Ref;
import com.parrot.drone.groundsdk.device.Drone;

/**
 * Instrument that informs about the radio.
 * <p>
 * This instrument can be obtained from a {@link Drone drone} using:
 * <pre>{@code drone.getInstrument(Radio.class)}</pre>
 *
 * @see Drone#getInstrument(Class)
 * @see Drone#getInstrument(Class, Ref.Observer)
 */
public interface Radio extends Instrument {

    /**
     * Retrieves the current received signal strength indication (RSSI), expressed in dBm.
     *
     * @return current RSSI
     */
    @IntRange(to = 0)
    int getRssi();

    /**
     * Gets the current link signal quality.
     * <p>
     * The quality varies from 0 to 4.
     * <p>
     * {@code 0} means that disconnection is highly probable, {@code 4} means that the link signal quality is very good.
     *
     * @return current link signal quality from 0 to 4 or {@code -1} if unknown.
     */
    @IntRange(from = -1, to = 4)
    int getLinkSignalQuality();

    /**
     * Tells whether the radio link is perturbed by external elements.
     * <p>
     * The returned value is {@code true} if the link signal quality is low although the radio RSSI is good. This
     * indicates that the radio link is perturbed by external elements.
     *
     * @return {@code true} if the radio link is perturbed by external elements, otherwise {@code false}
     */
    boolean isLinkPerturbed();

    /**
     * Tells whether there is a probable 4G interference coming from the user's phone.
     * <p>
     * In that case, it is advised to disable 4G on the user's phone.
     *
     * @return {@code true} if a 4G interference is currently detected, otherwise {@code false}
     */
    boolean is4GInterfering();
}
