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
import com.parrot.drone.groundsdk.device.peripheral.MediaStore;
import com.parrot.drone.groundsdkdemo.R;
import com.parrot.drone.groundsdkdemo.peripheral.MediaStoreBrowserActivity;

import static com.parrot.drone.groundsdkdemo.Extras.EXTRA_DEVICE_UID;

class MediaStoreContent extends PeripheralContent<Drone, MediaStore> {

    MediaStoreContent(@NonNull Drone drone) {
        super(R.layout.media_store_info, drone, MediaStore.class);
    }

    @Override
    Content.ViewHolder<?> onCreateViewHolder(@NonNull View rootView) {
        return new ViewHolder(rootView);
    }

    private static final class ViewHolder extends PeripheralContent.ViewHolder<MediaStoreContent, MediaStore> {

        @NonNull
        private final TextView mPictureCountText;

        @NonNull
        private final TextView mVideoCountText;

        @NonNull
        private final Button mBrowseButton;

        ViewHolder(@NonNull View rootView) {
            super(rootView);
            mPictureCountText = findViewById(R.id.picture_count);
            mVideoCountText = findViewById(R.id.video_count);
            mBrowseButton = findViewById(R.id.btn_browse);
            mBrowseButton.setOnClickListener(mClickListener);
        }

        @Override
        void onBind(@NonNull MediaStoreContent content, @NonNull MediaStore mediaStore) {
            int pictures = mediaStore.getPhotoMediaCount();
            int photoResources = mediaStore.getPhotoResourceCount();
            mPictureCountText.setText(mContext.getString(R.string.media_count_format, pictures, photoResources));
            int videos = mediaStore.getVideoMediaCount();
            int videoResources = mediaStore.getVideoResourceCount();
            mVideoCountText.setText(mContext.getString(R.string.media_count_format, videos, videoResources));
            mBrowseButton.setEnabled(pictures > 0 || videos > 0);
        }

        private final OnClickListener mClickListener = new OnClickListener() {

            @Override
            void onClick(View v, @NonNull MediaStoreContent content, @NonNull MediaStore mediaStore) {
                mContext.startActivity(new Intent(mContext, MediaStoreBrowserActivity.class)
                        .putExtra(EXTRA_DEVICE_UID, content.mDevice.getUid()));
            }
        };
    }
}
