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

package com.parrot.drone.groundsdkdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.parrot.drone.groundsdk.device.peripheral.RemovableUserStorage;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class FormatDialogFragment extends DialogFragment {

    public interface Listener {

        void onFormatAccept(@NonNull RemovableUserStorage.FormattingType type, @NonNull String name);
    }

    @Nullable
    private Listener mListener;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private List<RemovableUserStorage.FormattingType> mFormattingTypes;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private EditText mNameInput;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private Spinner mFormattingTypeView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        assert activity != null;

        @SuppressLint("InflateParams")
        View content = activity.getLayoutInflater().inflate(R.layout.dialog_format, null);
        mNameInput = content.findViewById(android.R.id.edit);
        mFormattingTypeView = content.findViewById(R.id.formatting_type);
        ArrayAdapter<RemovableUserStorage.FormattingType> adapter =
                new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, mFormattingTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFormattingTypeView.setAdapter(adapter);
        return new AlertDialog.Builder(activity)
                .setTitle(R.string.title_dialog_format)
                .setView(content)
                .setPositiveButton(R.string.action_format, mClickListener)
                .setNegativeButton(R.string.action_cancel, mClickListener)
                .create();
    }

    public void setListener(@Nullable Listener listener) {
        mListener = listener;
    }

    public void setSupportedFormattedTypes(@NonNull EnumSet<RemovableUserStorage.FormattingType> types) {
        mFormattingTypes = new ArrayList<>(types);
    }

    private final DialogInterface.OnClickListener mClickListener = (dialog, which) -> {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            if (mListener != null) {
                RemovableUserStorage.FormattingType type =
                        (RemovableUserStorage.FormattingType) mFormattingTypeView.getSelectedItem();
                mListener.onFormatAccept(type, mNameInput.getText().toString());
            }
            dialog.dismiss();
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.cancel();
        }
    };
}
