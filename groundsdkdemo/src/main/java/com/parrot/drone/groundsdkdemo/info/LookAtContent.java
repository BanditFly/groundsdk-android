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
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.parrot.drone.groundsdk.device.Drone;
import com.parrot.drone.groundsdk.device.pilotingitf.Activable;
import com.parrot.drone.groundsdk.device.pilotingitf.LookAtPilotingItf;
import com.parrot.drone.groundsdk.device.pilotingitf.tracking.TrackingIssue;
import com.parrot.drone.groundsdkdemo.R;

import java.util.EnumSet;

class LookAtContent extends ActivablePilotingItfContent<Drone, LookAtPilotingItf> {

    LookAtContent(@NonNull Drone drone) {
        super(R.layout.look_at_info, drone, LookAtPilotingItf.class);
    }

    @Override
    ViewHolder onCreateViewHolder(@NonNull View rootView) {
        return new ViewHolder(rootView);
    }

    private static final class ViewHolder
            extends ActivablePilotingItfContent.ViewHolder<LookAtContent, LookAtPilotingItf> {

        @NonNull
        private final TextView mTrackingIssuesText;

        ViewHolder(@NonNull View rootView) {
            super(rootView);
            mTrackingIssuesText = findViewById(R.id.issues);
            mActivateBtn.setOnClickListener(mClickListener);
        }

        @Override
        void onBind(@NonNull LookAtContent content, @NonNull LookAtPilotingItf lookAt) {
            super.onBind(content, lookAt);
            EnumSet<TrackingIssue> issues = lookAt.getState() == Activable.State.UNAVAILABLE ?
                    lookAt.getAvailabilityIssues() : lookAt.getQualityIssues();
            mTrackingIssuesText.setText(TextUtils.join("\n", issues));
        }

        private final OnClickListener mClickListener = new OnClickListener() {

            @Override
            void onClick(View v, @NonNull LookAtContent content, @NonNull LookAtPilotingItf lookAt) {
                Activable.State state = lookAt.getState();
                if (state == Activable.State.IDLE) {
                    lookAt.activate();
                } else if (state == Activable.State.ACTIVE) {
                    lookAt.deactivate();
                }
            }
        };
    }
}
