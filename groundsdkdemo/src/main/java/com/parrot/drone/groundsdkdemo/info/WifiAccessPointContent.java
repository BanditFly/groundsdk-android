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

package com.parrot.drone.groundsdkdemo.info;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parrot.drone.groundsdk.device.Drone;
import com.parrot.drone.groundsdk.device.RemoteControl;
import com.parrot.drone.groundsdk.device.peripheral.Peripheral;
import com.parrot.drone.groundsdk.device.peripheral.WifiAccessPoint;
import com.parrot.drone.groundsdkdemo.R;
import com.parrot.drone.groundsdkdemo.settings.WifiAccessPointSettingsActivity;

import java.util.Locale;

import static com.parrot.drone.groundsdkdemo.Extras.EXTRA_DEVICE_UID;

class WifiAccessPointContent extends PeripheralContent<Peripheral.Provider, WifiAccessPoint> {

    @NonNull
    private final String mDeviceUid;

    WifiAccessPointContent(@NonNull Drone drone) {
        this(drone, drone.getUid());
    }

    WifiAccessPointContent(@NonNull RemoteControl remoteControl) {
        this(remoteControl, remoteControl.getUid());
    }

    private WifiAccessPointContent(@NonNull Peripheral.Provider device, @NonNull String deviceUid) {
        super(R.layout.wifi_ap_info, device, WifiAccessPoint.class);
        mDeviceUid = deviceUid;
    }

    @Override
    ViewHolder onCreateViewHolder(@NonNull View rootView) {
        return new ViewHolder(rootView);
    }

    private static final class ViewHolder
            extends PeripheralContent.ViewHolder<WifiAccessPointContent, WifiAccessPoint> {

        @NonNull
        private final Button mEditButton;

        @NonNull
        private final TextView mEnvironmentText;

        @NonNull
        private final TextView mCountryText;

        @NonNull
        private final TextView mDefaultCountryUsedText;

        @NonNull
        private final TextView mChannelSelectionModeText;

        @NonNull
        private final TextView mChannelText;

        @NonNull
        private final TextView mSsidText;

        @NonNull
        private final TextView mSecurityText;

        ViewHolder(@NonNull View rootView) {
            super(rootView);
            mEditButton = findViewById(R.id.btn_edit);
            mEditButton.setOnClickListener(mClickListener);
            mEnvironmentText = findViewById(R.id.environment);
            mCountryText = findViewById(R.id.country);
            mDefaultCountryUsedText = findViewById(R.id.default_country_used);
            mChannelSelectionModeText = findViewById(R.id.channel_selection_mode);
            mChannelText = findViewById(R.id.channel);
            mSsidText = findViewById(R.id.ssid);
            mSecurityText = findViewById(R.id.security);
        }

        @Override
        void onBind(@NonNull WifiAccessPointContent content, @NonNull WifiAccessPoint wifiAp) {
            mEnvironmentText.setText(wifiAp.environment().getValue().toString());
            mCountryText.setText(new Locale("", wifiAp.country().getCode()).getDisplayCountry());
            mDefaultCountryUsedText.setText(Boolean.toString(wifiAp.country().isDefaultCountryUsed()));
            WifiAccessPoint.ChannelSetting channel = wifiAp.channel();
            mChannelSelectionModeText.setText(channel.getSelectionMode().toString());
            mChannelText.setText(channel.get().toString());
            mSsidText.setText(wifiAp.ssid().getValue());
            mSecurityText.setText(wifiAp.security().getMode().toString());
        }

        private final OnClickListener mClickListener = new OnClickListener() {

            @Override
            void onClick(View v, @NonNull WifiAccessPointContent content, @NonNull WifiAccessPoint wifiAp) {
                mContext.startActivity(new Intent(mContext, WifiAccessPointSettingsActivity.class)
                        .putExtra(EXTRA_DEVICE_UID, content.mDeviceUid));
            }
        };
    }
}
