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

package com.parrot.drone.groundsdk.arsdkengine.http;

import android.support.annotation.NonNull;

import com.parrot.drone.groundsdk.internal.http.HttpClient;
import com.parrot.drone.groundsdk.internal.http.HttpRequest;
import com.parrot.drone.groundsdk.internal.http.HttpSession;
import com.parrot.drone.sdkcore.ulog.ULog;

import java.io.File;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.PUT;

import static com.parrot.drone.groundsdk.arsdkengine.Logging.TAG_HTTP;

/**
 * Client of flight plan device webservice.
 * <p>
 * Provides method {@link #uploadFlightPlan} to upload a flight plan over HTTP.
 */
public class HttpFlightPlanClient extends HttpClient {

    /** Implementation of flight plan REST API. */
    @NonNull
    private final Service mService;

    /**
     * Constructor.
     *
     * @param httpSession HTTP session
     */
    @SuppressWarnings("WeakerAccess") // Acceded by introspection
    public HttpFlightPlanClient(@NonNull HttpSession httpSession) {
        mService = httpSession.create(new Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create()),
                Service.class);
    }

    /**
     * Uploads a local flight plan to the device.
     *
     * @param flightPlan flight plan file to upload
     * @param callback   callback notified of upload completion status
     *
     * @return an HTTP request, that can be canceled
     */
    @NonNull
    public HttpRequest uploadFlightPlan(@NonNull File flightPlan,
                                        @NonNull HttpRequest.ResultCallback<String> callback) {
        Call<String> uploadCall = mService.upload(RequestBody.create(null, flightPlan));
        uploadCall.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                int code = response.code();
                if (response.isSuccessful()) {
                    String flightPlanUid = response.body();
                    if (flightPlanUid != null) {
                        // trim beginning and ending quotes
                        flightPlanUid = flightPlanUid.replaceAll("^\"|\"$", "");
                    }
                    callback.onRequestComplete(HttpRequest.Status.SUCCESS, code, flightPlanUid);
                } else {
                    if (ULog.e(TAG_HTTP)) {
                        ULog.e(TAG_HTTP, "Failed to upload flight plan [file: " + flightPlan + ", code: " + code + "]");
                    }
                    callback.onRequestComplete(HttpRequest.Status.FAILED, code, null);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable error) {
                if (call.isCanceled()) {
                    callback.onRequestComplete(HttpRequest.Status.CANCELED, HttpRequest.STATUS_CODE_UNKNOWN, null);
                } else {
                    if (ULog.e(TAG_HTTP)) {
                        ULog.e(TAG_HTTP, "Failed to upload flight plan [file: " + flightPlan + "]", error);
                    }
                    callback.onRequestComplete(HttpRequest.Status.FAILED, HttpRequest.STATUS_CODE_UNKNOWN, null);
                }
            }
        });
        return bookRequest(uploadCall::cancel);
    }

    /** REST API. */
    private interface Service {

        /**
         * Uploads a flight plan.
         *
         * @param file request body wrapping the flight plan file to upload
         *
         * @return a retrofit call for sending the request out
         */
        @PUT("api/v1/upload/flightplan")
        @NonNull
        Call<String> upload(@NonNull @Body RequestBody file);
    }
}
