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

package com.parrot.drone.groundsdk.device.peripheral.gamepad.skycontroller3;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.parrot.drone.groundsdk.device.peripheral.SkyController3Gamepad;
import com.parrot.drone.groundsdk.device.peripheral.gamepad.AxisInterpolator;

/**
 * An event that may be produced by a gamepad {@link SkyController3Gamepad.Axis axis} input.
 * <p>
 * The corresponding input has an axis behavior, i.e. it has a position in some range, and, when grabbed, an event is
 * sent each time that position changes, along with the current position value in a [-100, 100] range.
 * <p>
 * Note that received event values have been transformed by the {@link AxisInterpolator interpolator} currently set on
 * the corresponding axis.
 */
public enum AxisEvent {

    /**
     * Event sent when the {@link SkyController3Gamepad.Axis#LEFT_STICK_HORIZONTAL horizontal axis on left stick}
     * is moved.
     */
    LEFT_STICK_HORIZONTAL,

    /**
     * Event sent when the {@link SkyController3Gamepad.Axis#LEFT_STICK_VERTICAL vertical axis on left stick} is moved.
     */
    LEFT_STICK_VERTICAL,

    /**
     * Event sent when the {@link SkyController3Gamepad.Axis#RIGHT_STICK_HORIZONTAL horizontal axis on right stick}
     * is moved.
     */
    RIGHT_STICK_HORIZONTAL,

    /**
     * Event sent when the {@link SkyController3Gamepad.Axis#RIGHT_STICK_VERTICAL vertical axis on right stick}
     * is moved.
     */
    RIGHT_STICK_VERTICAL,

    /**
     * Event sent when the {@link SkyController3Gamepad.Axis#LEFT_SLIDER left slider} is moved.
     */
    LEFT_SLIDER,

    /**
     * Event sent when the {@link SkyController3Gamepad.Axis#RIGHT_SLIDER right slider} is moved.
     */
    RIGHT_SLIDER;

    /**
     * Receives axis events sent from the remote control when a corresponding input is grabbed.
     */
    public interface Listener {

        /**
         * Called back when an axis event is received.
         *
         * @param event received axis event
         * @param value current axis position, in range [-100, 100]
         */
        void onAxisEvent(@NonNull AxisEvent event, @IntRange(from = -100, to = 100) int value);
    }
}
