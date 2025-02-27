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

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

import com.parrot.drone.groundsdk.Ref;
import com.parrot.drone.groundsdk.device.Drone;

/**
 * Point Of Interest piloting interface for copters.
 * <p>
 * During a piloted Point Of Interest, the drone will always look at the given Point Of Interest but can be piloted
 * normally. However, yaw value is not settable. Camera tilt and pan command is ignored by the drone.
 * <p>
 * This piloting interface can be obtained from a {@link Drone drone} using:
 * <pre>{@code drone.getPilotingItf(PointOfInterestPilotingItf.class)}</pre>
 *
 * @see Drone#getPilotingItf(Class)
 * @see Drone#getPilotingItf(Class, Ref.Observer)
 */
public interface PointOfInterestPilotingItf extends PilotingItf, Activable {

    /** A Point Of Interest to look at. */
    interface PointOfInterest {

        /**
         * Retrieves the latitude of the location (in degrees) to look at.
         *
         * @return the latitude in degrees
         */
        double getLatitude();

        /**
         * Retrieves the longitude of the location (in degrees) to look at.
         *
         * @return the longitude in degrees
         */
        double getLongitude();

        /**
         * Retrieves the altitude above take off point (in meters) to look at.
         *
         * @return the altitude in meters
         */
        double getAltitude();
    }

    /**
     * Starts a piloted Point Of Interest.
     *
     * @param latitude  latitude of the location (in degrees) to look at
     * @param longitude longitude of the location (in degrees) to look at
     * @param altitude  altitude above take off point (in meters) to look at
     */
    void start(double latitude, double longitude, double altitude);

    /**
     * Retrieves the current targeted Point Of Interest.
     *
     * @return the current target, or {@code null} if there's no piloted Point Of Interest in progress
     */
    @Nullable
    PointOfInterest getCurrentPointOfInterest();

    /**
     * Sets the current pitch value.
     * <p>
     * {@code value} is expressed as a signed percentage of the {@link ManualCopterPilotingItf#getMaxPitchRoll() max
     * pitch/roll setting},
     * in range [-100, 100]. <br>
     * -100 corresponds to a pitch angle of max pitch/roll towards ground (copter will fly forward), 100 corresponds to
     * a pitch angle of max pitch/roll towards sky (copter will fly backward).
     * <p>
     * Note: {@code value} may be clamped if necessary, in order to respect the maximum supported physical tilt of the
     * copter.
     *
     * @param value the new pitch value to set
     *
     * @see ManualCopterPilotingItf#getMaxPitchRoll()
     */
    void setPitch(@IntRange(from = -100, to = 100) int value);

    /**
     * Sets the current roll value.
     * <p>
     * {@code value} is expressed as a signed percentage of the {@link ManualCopterPilotingItf#getMaxPitchRoll() max
     * pitch/roll setting},
     * in range [-100, 100]. <br>
     * -100 corresponds to a roll angle of max pitch/roll to the left (copter will fly left), 100 corresponds to a roll
     * angle of max pitch/roll to the right (copter will fly right).
     * <p>
     * Note: {@code value} may be clamped if necessary, in order to respect the maximum supported physical tilt of the
     * copter.
     *
     * @param value the new pitch roll to set
     *
     * @see ManualCopterPilotingItf#getMaxPitchRoll()
     */
    void setRoll(@IntRange(from = -100, to = 100) int value);

    /**
     * Set the current vertical speed value.
     * <p>
     * {@code value} is expressed as as a signed percentage of the
     * {@link ManualCopterPilotingItf#getMaxVerticalSpeed() max vertical speed setting}, in range [-100, 100]. <br>
     * -100 corresponds to max vertical speed towards ground,100 corresponds to max vertical speed towards sky.
     *
     * @param value the new vertical speed value to set
     *
     * @see ManualCopterPilotingItf#getMaxVerticalSpeed()
     */
    void setVerticalSpeed(@IntRange(from = -100, to = 100) int value);
}
