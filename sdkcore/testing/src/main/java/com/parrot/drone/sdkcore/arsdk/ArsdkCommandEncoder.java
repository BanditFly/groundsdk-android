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

package com.parrot.drone.sdkcore.arsdk;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.parrot.drone.sdkcore.arsdk.command.ArsdkCommand;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.List;

public class ArsdkCommandEncoder {

    @NonNull
    private final ByteBuffer mBuf;

    ArsdkCommandEncoder() {
        mBuf = ByteBuffer.allocateDirect(4096);
        mBuf.order(ByteOrder.LITTLE_ENDIAN);
    }

    ArsdkCommandEncoder writeByte(byte b) {
        mBuf.put(b);
        return this;
    }

    ArsdkCommandEncoder writeShort(short s) {
        mBuf.putShort(s);
        return this;
    }

    ArsdkCommandEncoder writeInt(int i) {
        mBuf.putInt(i);
        return this;
    }

    ArsdkCommandEncoder writeLong(long l) {
        mBuf.putLong(l);
        return this;
    }

    ArsdkCommandEncoder writeFloat(float f) {
        mBuf.putFloat(f);
        return this;
    }

    ArsdkCommandEncoder writeDouble(double d) {
        mBuf.putDouble(d);
        return this;
    }

    ArsdkCommandEncoder writeString(String string) {
        for (byte b : string.getBytes(Charset.defaultCharset())) {
            mBuf.put(b);
        }
        mBuf.put((byte) 0);
        return this;
    }

    ArsdkCommandEncoder writeBuffer(byte[] buff) {
        mBuf.put(buff);
        return this;
    }

    ArsdkCommandEncoder writeMultiset(List<ArsdkCommand> commands) {
        ByteBuffer commandsBuffer = ByteBuffer.allocateDirect(4096);

        for (ArsdkCommand cmd : commands) {
            @SuppressLint("RestrictedApi")
            ByteBuffer cmdBuff = cmd.getData();
            commandsBuffer.putShort((short) cmdBuff.limit());
            commandsBuffer.put(cmdBuff);
        }

        mBuf.putShort((short) commandsBuffer.position());
        commandsBuffer.flip();
        mBuf.put(commandsBuffer);

        return this;
    }

    public ArsdkCommandEncoder reset() {
        mBuf.clear();
        return this;
    }

    @SuppressLint("RestrictedApi")
    ArsdkCommand encode() {
        ArsdkCommand cmd = ArsdkCommand.Pool.DEFAULT.obtain();
        mBuf.flip();
        cmd.setData(mBuf.slice());
        return cmd;
    }
}
