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
import com.parrot.drone.groundsdk.device.instrument.PhotoProgressIndicator;
import com.parrot.drone.groundsdk.value.OptionalDouble;
import com.parrot.drone.groundsdkdemo.R;

class PhotoProgressContent extends InstrumentContent<Drone, PhotoProgressIndicator> {

    PhotoProgressContent(@NonNull Drone drone) {
        super(R.layout.photo_progress_info, drone, PhotoProgressIndicator.class);
    }

    @Override
    ViewHolder onCreateViewHolder(@NonNull View rootView) {
        return new ViewHolder(rootView);
    }

    private static final class ViewHolder
            extends InstrumentContent.ViewHolder<PhotoProgressContent, PhotoProgressIndicator> {

        @NonNull
        private final TextView mRemainingTimeText;

        @NonNull
        private final TextView mRemainingDistanceText;

        ViewHolder(@NonNull View rootView) {
            super(rootView);
            mRemainingTimeText = findViewById(R.id.remaining_time);
            mRemainingDistanceText = findViewById(R.id.remaining_distance);
        }

        @Override
        void onBind(@NonNull PhotoProgressContent content, @NonNull PhotoProgressIndicator photoProgressIndicator) {
            OptionalDouble remainingTime = photoProgressIndicator.getRemainingTime();
            mRemainingTimeText.setText(mContext.getString(
                    remainingTime.isAvailable() ? R.string.time_s_format : R.string.no_value,
                    remainingTime.getValue()));

            OptionalDouble remainingDistance = photoProgressIndicator.getRemainingDistance();
            mRemainingDistanceText.setText(mContext.getString(
                    remainingDistance.isAvailable() ? R.string.distance_format : R.string.no_value,
                    remainingDistance.getValue()));
        }
    }
}
