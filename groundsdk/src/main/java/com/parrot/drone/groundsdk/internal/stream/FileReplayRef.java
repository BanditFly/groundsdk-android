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

package com.parrot.drone.groundsdk.internal.stream;

import android.support.annotation.NonNull;

import com.parrot.drone.groundsdk.internal.session.Session;
import com.parrot.drone.groundsdk.stream.FileReplay;

/**
 * A reference on a {@link FileReplay} stream.
 */
public final class FileReplayRef extends Session.RefBase<FileReplay> {

    /** Referenced stream. */
    @NonNull
    private final FileReplayCore mStream;

    /**
     * Constructor.
     *
     * @param session  session that will manage this ref
     * @param observer observer that will be notified when the referenced object is updated
     * @param source   identifies the local file to stream
     */
    public FileReplayRef(@NonNull Session session, @NonNull Observer<? super FileReplay> observer,
                         @NonNull FileSourceCore source) {
        super(session, observer);
        mStream = new FileReplayCore(source);
        mStream.registerObserver(() -> update(get()));
        init(mStream);
    }

    @Override
    protected void release() {
        mStream.release();
        super.release();
    }
}
