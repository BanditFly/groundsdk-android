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

package com.parrot.drone.groundsdk;

import android.location.Address;
import android.support.annotation.NonNull;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import static org.hamcrest.core.IsEqual.equalTo;

public final class AddressMatcher {

    public static Matcher<Address> addressIs(@NonNull String addressLine, @NonNull String postalCode,
                                             @NonNull String locality, @NonNull String countryCode,
                                             @NonNull String countryName) {
        return Matchers.both(Matchers.notNullValue(Address.class))
                       .and(nonNullAddressIs(addressLine, postalCode, locality, countryCode, countryName));
    }

    private static Matcher<Address> nonNullAddressIs(@NonNull String addressLine, @NonNull String postalCode,
                                                     @NonNull String locality, @NonNull String countryCode,
                                                     @NonNull String countryName) {
        return Matchers.allOf(
                new FeatureMatcher<Address, String>(equalTo(addressLine), "address line is", "address line") {

                    @Override
                    protected String featureValueOf(Address actual) {
                        return actual.getAddressLine(0);
                    }
                },
                new FeatureMatcher<Address, String>(equalTo(postalCode), "postal code is", "postal code") {

                    @Override
                    protected String featureValueOf(Address actual) {
                        return actual.getPostalCode();
                    }
                },
                new FeatureMatcher<Address, String>(equalTo(locality), "locality is", "locality") {

                    @Override
                    protected String featureValueOf(Address actual) {
                        return actual.getLocality();
                    }
                },
                new FeatureMatcher<Address, String>(equalTo(countryCode), "country code is", "country code") {

                    @Override
                    protected String featureValueOf(Address actual) {
                        return actual.getCountryCode();
                    }
                },
                new FeatureMatcher<Address, String>(equalTo(countryName), "country name is", "country name") {

                    @Override
                    protected String featureValueOf(Address actual) {
                        return actual.getCountryName();
                    }
                }
        );
    }

    private AddressMatcher() {
    }
}
