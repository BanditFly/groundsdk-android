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

package com.parrot.drone.groundsdkdemo.peripheral.gamepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Button;

import com.parrot.drone.groundsdk.device.Drone;
import com.parrot.drone.groundsdk.device.RemoteControl;
import com.parrot.drone.groundsdkdemo.GroundSdkActivityBase;
import com.parrot.drone.groundsdkdemo.R;
import com.parrot.drone.groundsdkdemo.peripheral.gamepad.facade.GamepadFacade;
import com.parrot.drone.groundsdkdemo.peripheral.gamepad.facade.GamepadFacadeProvider;

import java.util.Set;

import static com.parrot.drone.groundsdkdemo.Extras.EXTRA_DEVICE_UID;
import static com.parrot.drone.groundsdkdemo.peripheral.gamepad.GamepadEditMappingActivity.EXTRA_ENTRY_MODEL;

public class GamepadMappingsActivity extends GroundSdkActivityBase {

    private String mRcUid;

    private GamepadFacade mGamepad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RemoteControl rc = groundSdk().getRemoteControl(getIntent().getStringExtra(EXTRA_DEVICE_UID));
        if (rc == null) {
            finish();
            return;
        }
        mRcUid = rc.getUid();

        setContentView(R.layout.activity_gamepad_mappings);

        setSupportActionBar(findViewById(R.id.toolbar));

        Button newMappingBtn = findViewById(R.id.btn_new);
        Button resetMappingBtn = findViewById(R.id.btn_reset);

        ViewPager pager = findViewById(R.id.pager);
        TabLayout tabs = findViewById(R.id.tabs);

        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

        GamepadFacadeProvider.of(rc).getPeripheral(GamepadFacade.class, gamepad -> {
            if (gamepad == null) {
                finish();
            } else {
                mGamepad = gamepad;
                Set<Drone.Model> models = gamepad.getSupportedDroneModels();
                boolean hasModels = !models.isEmpty();
                resetMappingBtn.setEnabled(hasModels);
                newMappingBtn.setEnabled(hasModels);
                adapter.setModels(models);
            }
        });

        newMappingBtn.setOnClickListener(v -> {
            Drone.Model model = adapter.getModel(pager.getCurrentItem());
            startActivity(new Intent(this, GamepadEditMappingActivity.class)
                    .putExtra(EXTRA_DEVICE_UID, rc.getUid())
                    .putExtra(EXTRA_ENTRY_MODEL, model.ordinal()));
        });

        resetMappingBtn.setOnClickListener(v -> {
            Drone.Model model = adapter.getModel(pager.getCurrentItem());
            mGamepad.resetDefaultMappings(model);
        });
    }

    private class PageAdapter extends FragmentStatePagerAdapter {

        private Drone.Model[] mModels;

        PageAdapter(FragmentManager fm) {
            super(fm);
        }

        void setModels(@NonNull Set<Drone.Model> models) {
            mModels = models.toArray(new Drone.Model[0]);
            notifyDataSetChanged();
        }

        Drone.Model getModel(int position) {
            return mModels[position];
        }

        @Override
        public Fragment getItem(int position) {
            return GamepadMappingFragment.newInstance(mRcUid, mModels[position]);
        }

        @Override
        public int getCount() {
            return mModels == null ? 0 : mModels.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mModels[position].toString();
        }
    }
}
