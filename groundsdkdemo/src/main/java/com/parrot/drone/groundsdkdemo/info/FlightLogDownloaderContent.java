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

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.parrot.drone.groundsdk.device.Drone;
import com.parrot.drone.groundsdk.device.RemoteControl;
import com.parrot.drone.groundsdk.device.peripheral.FlightLogDownloader;
import com.parrot.drone.groundsdk.device.peripheral.Peripheral;
import com.parrot.drone.groundsdkdemo.R;

class FlightLogDownloaderContent extends PeripheralContent<Peripheral.Provider, FlightLogDownloader> {

    FlightLogDownloaderContent(@NonNull Drone drone) {
        super(R.layout.flightlog_downloader_info, drone, FlightLogDownloader.class);
    }

    FlightLogDownloaderContent(@NonNull RemoteControl rc) {
        super(R.layout.flightlog_downloader_info, rc, FlightLogDownloader.class);
    }

    @Override
    ViewHolder onCreateViewHolder(@NonNull View rootView) {
        return new ViewHolder(rootView);
    }

    static class ViewHolder extends PeripheralContent.ViewHolder<FlightLogDownloaderContent, FlightLogDownloader> {

        @NonNull
        private final TextView mDownloadText;

        @NonNull
        private final TextView mDownloadCountText;

        @NonNull
        private final TextView mDownloadStatusText;

        ViewHolder(@NonNull View rootView) {
            super(rootView);
            mDownloadText = findViewById(R.id.downloading);
            mDownloadCountText = findViewById(R.id.download_count);
            mDownloadStatusText = findViewById(R.id.status);
        }

        @Override
        void onBind(@NonNull FlightLogDownloaderContent content, @NonNull FlightLogDownloader flightLogDownloader) {
            mDownloadText.setText(Boolean.toString(flightLogDownloader.isDownloading()));
            mDownloadCountText.setText(mContext.getString(R.string.int_value_format,
                    flightLogDownloader.getLatestDownloadCount()));
            mDownloadStatusText.setText(flightLogDownloader.getCompletionStatus().toString());
        }
    }
}
