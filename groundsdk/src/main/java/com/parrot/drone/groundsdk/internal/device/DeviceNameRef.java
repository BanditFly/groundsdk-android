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

package com.parrot.drone.groundsdk.internal.device;

import android.support.annotation.NonNull;

import com.parrot.drone.groundsdk.internal.session.Session;

/**
 * A reference to a device name instance.
 */
final class DeviceNameRef extends Session.RefBase<String> {

    /** Holder holding the device name. */
    @NonNull
    private final DeviceNameHolder mHolder;

    /**
     * Constructor.
     *
     * @param session  session that will manage this ref
     * @param observer observer notified when the device name changes
     * @param holder   holder holding referenced device name
     */
    DeviceNameRef(@NonNull Session session, @NonNull Observer<String> observer, @NonNull DeviceNameHolder holder) {
        super(session, observer);
        mHolder = holder;
        mHolder.registerObserver(mHolderObserver);
        init(mHolder.get());
    }

    /**
     * Releases the reference.
     */
    @Override
    protected void release() {
        mHolder.unregisterObserver(mHolderObserver);
        super.release();
    }

    /** Holder observer. */
    private final DeviceNameHolder.Observer mHolderObserver = this::update;

}
