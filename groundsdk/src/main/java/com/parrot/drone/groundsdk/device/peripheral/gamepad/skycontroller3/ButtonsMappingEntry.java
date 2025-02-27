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

package com.parrot.drone.groundsdk.device.peripheral.gamepad.skycontroller3;

import android.support.annotation.NonNull;

import com.parrot.drone.groundsdk.device.Drone;
import com.parrot.drone.groundsdk.device.peripheral.gamepad.ButtonsMappableAction;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * A mapping entry that defines a {@link ButtonsMappableAction} to be triggered when the gamepad inputs produce
 * a set of {@link ButtonEvent button events} in the {@link ButtonEvent.State#PRESSED pressed} state.
 */
public final class ButtonsMappingEntry extends MappingEntry {

    /** Action to be triggered. */
    @NonNull
    private final ButtonsMappableAction mAction;

    /** Set of button events that triggers the action when in the {@link ButtonEvent.State#PRESSED pressed} state. */
    @NonNull
    private final Set<ButtonEvent> mButtonEvents;

    /**
     * Constructor.
     *
     * @param droneModel   drone model onto which the entry should apply
     * @param action       action to be triggered
     * @param buttonEvents event set that triggers the action
     */
    public ButtonsMappingEntry(@NonNull Drone.Model droneModel, @NonNull ButtonsMappableAction action,
                               @NonNull Set<ButtonEvent> buttonEvents) {
        super(Type.BUTTONS_MAPPING, droneModel);
        mAction = action;
        mButtonEvents = buttonEvents.isEmpty() ? Collections.emptySet()
                : Collections.unmodifiableSet(EnumSet.copyOf(buttonEvents));
    }

    /**
     * Gets the action to be triggered.
     *
     * @return action to be triggered
     */
    @NonNull
    public ButtonsMappableAction getAction() {
        return mAction;
    }

    /**
     * Gets the set of button events that triggers the action when pressed.
     * <p>
     * The application gets a read only view of those events, which cannot be modified.
     *
     * @return set of button event that triggers the action
     */
    @NonNull
    public Set<ButtonEvent> getButtonEvents() {
        return mButtonEvents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        ButtonsMappingEntry entry = (ButtonsMappingEntry) o;

        return mAction == entry.mAction;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + mAction.hashCode();
        return result;
    }
}
