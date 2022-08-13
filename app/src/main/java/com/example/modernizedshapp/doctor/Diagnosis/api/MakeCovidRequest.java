package com.example.modernizedshapp.doctor.Diagnosis.api;

import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.volley.Response;
import com.example.modernizedshapp.doctor.Diagnosis.ChatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MakeCovidRequest extends DiagnoseRequest {

    private final Response.Listener<JSONObject> succesListener;

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    public MakeCovidRequest(ChatActivity chatActivity, @Nullable String userAnswer) {
        super(chatActivity, userAnswer);
        this.succesListener = response -> {
            boolean shouldStop = false;
            try {
                try {
                    shouldStop = response.getBoolean("should_stop");
                    RequestUtil.getInstance().setConditionsArray(response.getJSONArray("conditions"));

                    if (shouldStop) {
                        new CovidTriageRequest(chatActivity);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!shouldStop) {
                    JSONObject jsonObjectQuestion = response.getJSONObject("question");
                    listener.onDoctorMessage(jsonObjectQuestion.getJSONArray("items").getJSONObject(0).getString("name"));
                    listener.onDoctorQuestionReceived(jsonObjectQuestion.getJSONArray("items").getJSONObject(0).getString("id"),
                            jsonObjectQuestion.getJSONArray("items").getJSONObject(0).getJSONArray("choices"),
                            jsonObjectQuestion.getJSONArray("items").getJSONObject(0).getString("name"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                listener.onRequestFailure();
            }
        };
        String url = InfermedicaApiClass.getInstance(chatActivity).getUrl() + "/covid19" + "/diagnosis";
        try {
            this.addAgeToRequestBody(RequestUtil::addAgeToJsonObjectForCovid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequestQueue.getInstance(chatActivity).addToRequestQueue(new JSONObjectRequestWithHeaders(1, url, this.getHeaders(),
                this.getRequestBody(), this.getSuccessListener(), this.getErrorListener()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    public MakeCovidRequest(ChatActivity chatActivity) {
        this(chatActivity, null);
    }

    @Override
    protected Response.Listener<JSONObject> getSuccessListener() {
        return this.succesListener;
    }

    private static class CovidTriageRequest extends DiagnoseRequest {

        @RequiresApi(api = Build.VERSION_CODES.O_MR1)
        public CovidTriageRequest(ChatActivity chatActivity) {
            super(chatActivity);
            String url = InfermedicaApiClass.getInstance(chatActivity).getUrl() + "/covid19" + "/triage";
            try {
                this.addAgeToRequestBody(RequestUtil::addAgeToJsonObjectForCovid);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ApiRequestQueue.getInstance(chatActivity).addToRequestQueue(new JSONObjectRequestWithHeaders(1, url, this.getHeaders(),
                    this.getRequestBody(), this.getSuccessListener(), this.getErrorListener()));
        }

        @Override
        protected Response.Listener<JSONObject> getSuccessListener() {
            return response -> {
                RequestUtil.getInstance().setConditionsArray(new JSONArray().put(response));
                listener.finishCovidDiagnose();
            };
        }

    }

}

