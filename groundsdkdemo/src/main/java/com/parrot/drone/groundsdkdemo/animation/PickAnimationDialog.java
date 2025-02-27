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

package com.parrot.drone.groundsdkdemo.animation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;

import com.parrot.drone.groundsdk.GroundSdk;
import com.parrot.drone.groundsdk.ManagedGroundSdk;
import com.parrot.drone.groundsdk.device.Drone;
import com.parrot.drone.groundsdk.device.pilotingitf.AnimationItf;
import com.parrot.drone.groundsdk.device.pilotingitf.animation.Animation;
import com.parrot.drone.groundsdkdemo.Extras;
import com.parrot.drone.groundsdkdemo.R;

import java.util.EnumSet;
import java.util.Set;

public class PickAnimationDialog extends DialogFragment {

    @NonNull
    public static PickAnimationDialog newInstance(@NonNull String deviceUid) {
        PickAnimationDialog fragment = new PickAnimationDialog();
        Bundle args = new Bundle();
        args.putString(Extras.EXTRA_DEVICE_UID, deviceUid);
        fragment.setArguments(args);
        return fragment;
    }

    public interface Listener {

        void onAnimationTypeAcquired(@NonNull Animation.Type type);
    }

    private Listener mListener;

    private ArrayAdapter<Animation.Type> mAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_dialog_animation_choice)
                .setAdapter(mAdapter, mClickListener)
                .create();
    }

    private final DialogInterface.OnClickListener mClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Animation.Type animationType = mAdapter.getItem(which);
            if (animationType != null) {
                mListener.onAnimationTypeAcquired(animationType);
            }
            dismiss();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (Listener) context;
        mAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);

        assert getArguments() != null;
        String deviceUid = getArguments().getString(Extras.EXTRA_DEVICE_UID);
        GroundSdk sdk = ManagedGroundSdk.obtainSession((Activity) context);
        Drone drone = deviceUid == null ? null : sdk.getDrone(deviceUid);
        if (drone != null) {
            drone.getPilotingItf(AnimationItf.class, animationItf -> {
                Set<Animation.Type> animations = animationItf == null ? EnumSet.noneOf(Animation.Type.class)
                        : animationItf.getAvailableAnimations();
                if (animations.isEmpty()) {
                    dismiss();
                } else {
                    mAdapter.setNotifyOnChange(false);
                    mAdapter.clear();
                    mAdapter.setNotifyOnChange(true);
                    mAdapter.addAll(animations);
                }
            });
        }
    }
}
