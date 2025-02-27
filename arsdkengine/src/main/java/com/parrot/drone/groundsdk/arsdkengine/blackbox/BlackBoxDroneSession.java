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

package com.parrot.drone.groundsdk.arsdkengine.blackbox;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.parrot.drone.groundsdk.arsdkengine.blackbox.data.Event;
import com.parrot.drone.groundsdk.arsdkengine.blackbox.data.LocationInfo;
import com.parrot.drone.groundsdk.arsdkengine.pilotingitf.PilotingCommand;
import com.parrot.drone.groundsdk.internal.device.DroneCore;
import com.parrot.drone.groundsdk.internal.tasks.Executor;
import com.parrot.drone.sdkcore.arsdk.ArsdkFeatureArdrone3;
import com.parrot.drone.sdkcore.arsdk.ArsdkFeatureCommon;
import com.parrot.drone.sdkcore.arsdk.ArsdkFeatureFollowMe;
import com.parrot.drone.sdkcore.arsdk.ArsdkFeatureRc;
import com.parrot.drone.sdkcore.arsdk.command.ArsdkCommand;
import com.parrot.drone.sdkcore.ulog.ULog;

import java.util.concurrent.TimeUnit;

import static com.parrot.drone.groundsdk.arsdkengine.Logging.TAG_BLACKBOX;

/**
 * Drone black box recording session.
 */
public final class BlackBoxDroneSession extends BlackBoxSession {

    /** Black box being recorded. */
    @NonNull
    final BlackBoxImpl mBlackBox;

    /**
     * Constructor.
     *
     * @param context  context that manages the session
     * @param drone    drone to record a black box from
     * @param listener listener to notify when the session closes
     */
    BlackBoxDroneSession(@NonNull BlackBoxRecorder.Context context, @NonNull DroneCore drone,
                         @NonNull CloseListener listener) {
        super(context, listener);
        mBlackBox = new BlackBoxImpl(drone);
        // start sampling
        mEnvironmentInfoSampler.start();
        mFlightInfoSampler.start();
        if (ULog.d(TAG_BLACKBOX)) {
            ULog.d(TAG_BLACKBOX, "Opened new Drone blackbox session [drone: " + drone.getUid()
                                 + ", session: " + System.identityHashCode(this)
                                 + ", context: " + System.identityHashCode(context)
                                 + ", blackbox: " + System.identityHashCode(mBlackBox) + "]");
        }
    }

    /**
     * Dispatches a piloting command to the session for processing.
     *
     * @param command piloting command to dispatch
     */
    public void onPilotingCommandChanged(@NonNull PilotingCommand command) {
        mContext.mFlightInfo.setDronePilotingCommand(command);
    }

    /**
     * Dispatches a sent land command event to the session for processing.
     */
    public void onLandCommandSent() {
        mContext.addEvent(Event.landing());
    }

    @Override
    public void onCommandReceived(@NonNull ArsdkCommand command) {
        switch (command.getFeatureId()) {
            case ArsdkFeatureArdrone3.GPSSettingsState.UID:
                ArsdkFeatureArdrone3.GPSSettingsState.decode(command, mGpsSettingsStateCallback);
                break;
            case ArsdkFeatureArdrone3.NetworkSettingsState.UID:
                ArsdkFeatureArdrone3.NetworkSettingsState.decode(command, mNetworkSettingsStateCallback);
                break;
            case ArsdkFeatureArdrone3.PilotingState.UID:
                ArsdkFeatureArdrone3.PilotingState.decode(command, mPilotingStateCallback);
                break;
            case ArsdkFeatureArdrone3.SettingsState.UID:
                ArsdkFeatureArdrone3.SettingsState.decode(command, mArdrone3SettingsStateCallback);
                break;
            case ArsdkFeatureCommon.CommonState.UID:
                ArsdkFeatureCommon.CommonState.decode(command, mCommonStateCallback);
                break;
            case ArsdkFeatureCommon.MavlinkState.UID:
                ArsdkFeatureCommon.MavlinkState.decode(command, mMavlinkStateCallback);
                break;
            case ArsdkFeatureCommon.RunState.UID:
                ArsdkFeatureCommon.RunState.decode(command, mRunStateCallback);
                break;
            case ArsdkFeatureCommon.SettingsState.UID:
                ArsdkFeatureCommon.SettingsState.decode(command, mCommonSettingsStateCallback);
                break;
            case ArsdkFeatureFollowMe.UID:
                ArsdkFeatureFollowMe.decode(command, mFollowMeCallback);
                break;
            case ArsdkFeatureRc.UID:
                ArsdkFeatureRc.decode(command, mExternalRcCallback);
                break;
        }
    }

    @Override
    public void close() {
        // stop sampling
        mFlightInfoSampler.stop();
        mEnvironmentInfoSampler.stop();
        super.close();
    }

    /** Allows running a task at regular interval on main thread. */
    private abstract static class Sampler {

        /** Time interval between two calls to {@link #sample()} method, in milliseconds. */
        private final long mInterval;

        /**
         * Constructor.
         *
         * @param interval interval at which {@link #sample()} method should be called, in milliseconds
         */
        Sampler(long interval) {
            mInterval = interval;
        }

        /**
         * Starts executing recurrent task.
         */
        final void start() {
            postSelf();
        }

        /**
         * Stops executing recurrent task.
         */
        final void stop() {
            Executor.unschedule(mSampleTask);
        }

        /**
         * Called each time the task executes. Subclasses should override this method to implement the task logic.
         */
        abstract void sample();

        /** Runnable scheduled regularly on main thread, which calls {@link #sample()}. */
        private final Runnable mSampleTask = new Runnable() {

            @Override
            public void run() {
                sample();
                postSelf();
            }

            @Override
            public String toString() {
                return Sampler.this.toString();
            }
        };

        /**
         * Schedules next task execution.
         */
        private void postSelf() {
            Executor.schedule(mSampleTask, mInterval);
        }
    }

    /** Samples context current flight info and injects those samples in the blackbox. */
    private final Sampler mFlightInfoSampler = new Sampler(TimeUnit.SECONDS.toMillis(1) / 5) {

        @Override
        void sample() {
            mBlackBox.addFlightInfo(mContext.mFlightInfo.build());
        }

        @Override
        public String toString() {
            return "Black box flight info sampler";
        }
    };

    /** Samples context current environment info and injects those samples in the blackbox. */
    private final Sampler mEnvironmentInfoSampler = new Sampler(TimeUnit.SECONDS.toMillis(1)) {

        @Override
        void sample() {
            mBlackBox.addEnvironmentInfo(mContext.mEnvironmentInfo.build());
        }

        @Override
        public String toString() {
            return "Black box environment info sampler";
        }
    };

    /** Callbacks called when a command of the feature ArsdkFeatureArdrone3.GPSSettingsState is decoded. */
    private final ArsdkFeatureArdrone3.GPSSettingsState.Callback mGpsSettingsStateCallback =
            new ArsdkFeatureArdrone3.GPSSettingsState.Callback() {

                @Override
                public void onHomeChanged(double latitude, double longitude, double altitude) {
                    mContext.addEvent(Event.homeLocationChange(latitude, longitude, altitude));
                }

                @Override
                public void onGPSFixStateChanged(int fixed) {
                    mContext.addEvent(Event.gpsFixChange(fixed));
                }
            };

    /** Callbacks called when a command of the feature ArsdkFeatureArdrone3.NetworkSettingsState is decoded. */
    private final ArsdkFeatureArdrone3.NetworkSettingsState.Callback mNetworkSettingsStateCallback =
            new ArsdkFeatureArdrone3.NetworkSettingsState.Callback() {

                @Override
                public void onWifiSelectionChanged(
                        @Nullable ArsdkFeatureArdrone3.NetworksettingsstateWifiselectionchangedType type,
                        @Nullable ArsdkFeatureArdrone3.NetworksettingsstateWifiselectionchangedBand band,
                        int channel) {
                    mContext.addEvent(Event.wifiBandChange(band == null ? -1 : band.value))
                            .addEvent(Event.wifiChannelChange(channel));
                }
            };

    /** Callbacks called when a command of the feature ArsdkFeatureArdrone3.PilotingState is decoded. */
    private final ArsdkFeatureArdrone3.PilotingState.Callback mPilotingStateCallback =
            new ArsdkFeatureArdrone3.PilotingState.Callback() {

                /** Latest received drone location, {@code null} if not received yet. */
                private LocationInfo mLastLocation;

                @Override
                public void onAlertStateChanged(
                        @Nullable ArsdkFeatureArdrone3.PilotingstateAlertstatechangedState state) {
                    mContext.addEvent(Event.alertStateChange(state == null ? -1 : state.value));
                }

                @Override
                public void onFlyingStateChanged(
                        @Nullable ArsdkFeatureArdrone3.PilotingstateFlyingstatechangedState state) {
                    mContext.addEvent(Event.flyingStateChange(state == null ? -1 : state.value));
                    if (state == ArsdkFeatureArdrone3.PilotingstateFlyingstatechangedState.TAKINGOFF
                        && mLastLocation != null) {
                        mContext.addEvent(Event.takeOffLocation(mLastLocation));
                    }
                }

                @Override
                public void onNavigateHomeStateChanged(
                        @Nullable ArsdkFeatureArdrone3.PilotingstateNavigatehomestatechangedState state,
                        @Nullable ArsdkFeatureArdrone3.PilotingstateNavigatehomestatechangedReason reason) {
                    mContext.addEvent(Event.returnHomeStateChange(state == null ? -1 : state.value));
                }

                @Override
                public void onPositionChanged(double latitude, double longitude, double altitude) {
                    mLastLocation = new LocationInfo(latitude, longitude, altitude);
                    mContext.mEnvironmentInfo.setDroneLocation(mLastLocation);
                }

                @Override
                public void onAttitudeChanged(float roll, float pitch, float yaw) {
                    mContext.mFlightInfo.setAttitude(roll, pitch, yaw);
                }

                @Override
                public void onSpeedChanged(float speedX, float speedY, float speedZ) {
                    mContext.mFlightInfo.setSpeed(speedX, speedY, speedZ);
                }

                @Override
                public void onAltitudeChanged(double altitude) {
                    mContext.mFlightInfo.setAltitude(altitude);
                }
            };

    /** Callbacks called when a command of the feature ArsdkFeatureArdrone3.SettingsState is decoded. */
    private final ArsdkFeatureArdrone3.SettingsState.Callback mArdrone3SettingsStateCallback =
            new ArsdkFeatureArdrone3.SettingsState.Callback() {

                @Override
                public void onProductGPSVersionChanged(String software, String hardware) {
                    mBlackBox.mHeader.setGpsVersion(software);
                }

                @SuppressWarnings("deprecation")
                @Override
                public void onMotorSoftwareVersionChanged(String version) {
                    mBlackBox.mHeader.setMotorVersion(version);
                }
            };

    /** Callbacks called when a command of the feature ArsdkFeatureCommon.CommonState is decoded. */
    private final ArsdkFeatureCommon.CommonState.Callback mCommonStateCallback =
            new ArsdkFeatureCommon.CommonState.Callback() {

                @Override
                public void onBatteryStateChanged(int percent) {
                    mContext.addEvent(Event.batteryLevelChange(percent));
                }

                @Override
                public void onWifiSignalChanged(int rssi) {
                    mContext.mEnvironmentInfo.setWifiSignal(rssi);
                }
            };

    /** Callbacks called when a command of the feature ArsdkFeatureCommon.MavlinkState is decoded. */
    private final ArsdkFeatureCommon.MavlinkState.Callback mMavlinkStateCallback =
            new ArsdkFeatureCommon.MavlinkState.Callback() {

                @Override
                public void onMavlinkFilePlayingStateChanged(
                        @Nullable ArsdkFeatureCommon.MavlinkstateMavlinkfileplayingstatechangedState state,
                        String filepath,
                        @Nullable ArsdkFeatureCommon.MavlinkstateMavlinkfileplayingstatechangedType type) {
                    mContext.addEvent(Event.flightPlanStateChange(state == null ? -1 : state.value));
                }
            };

    /** Callbacks called when a command of the feature ArsdkFeatureCommon.RunState is decoded. */
    private final ArsdkFeatureCommon.RunState.Callback mRunStateCallback = new ArsdkFeatureCommon.RunState.Callback() {

        @Override
        public void onRunIdChanged(@NonNull String runid) {
            mContext.addEvent(Event.runIdChange(runid));
        }
    };

    /** Callbacks called when a command of the feature ArsdkFeatureCommon.SettingsState is decoded. */
    private final ArsdkFeatureCommon.SettingsState.Callback mCommonSettingsStateCallback =
            new ArsdkFeatureCommon.SettingsState.Callback() {

                @Override
                public void onCountryChanged(@NonNull String code) {
                    mContext.addEvent(Event.countryChange(code));
                }

                @Override
                public void onProductVersionChanged(@NonNull String software, @NonNull String hardware) {
                    mBlackBox.mHeader.setVersion(software, hardware);
                }
            };

    /** Callbacks called when a command of the feature ArsdkFeatureFollowMe is decoded. */
    private final ArsdkFeatureFollowMe.Callback mFollowMeCallback = new ArsdkFeatureFollowMe.Callback() {

        @Override
        public void onState(@Nullable ArsdkFeatureFollowMe.Mode mode, @Nullable ArsdkFeatureFollowMe.Behavior behavior,
                            @Nullable ArsdkFeatureFollowMe.Animation animation, int animationAvailableBitField) {
            mContext.addEvent(Event.followMeModeChange(mode == null ? -1 : mode.value));
        }
    };

    /** Callbacks called when a command of the feature ArsdkFeatureRc is decoded. */
    private final ArsdkFeatureRc.Callback mExternalRcCallback = new ArsdkFeatureRc.Callback() {

        /** {@code true} when an external radio command is both connected and enabled. */
        private boolean mReceiverActive;

        @Override
        public void onReceiverState(@Nullable ArsdkFeatureRc.ReceiverState state, String protocol, int enabled) {
            boolean active = state == ArsdkFeatureRc.ReceiverState.CONNECTED && enabled != 0;
            if (mReceiverActive != active) {
                mContext.addEvent(Event.externalRcStateChange(active, protocol));
                mReceiverActive = active;
            }
        }
    };
}
