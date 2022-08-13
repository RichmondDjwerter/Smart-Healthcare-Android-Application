package com.example.modernizedshapp.doctor.Diagnosis.api;

import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.volley.Response;
import com.example.modernizedshapp.doctor.Diagnosis.ChatActivity;
import com.example.modernizedshapp.doctor.Diagnosis.helpers.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Map;


public abstract class DiagnoseRequest {
    protected final RequestUtil.ChatRequestListener listener;
    private final Response.Listener<JSONObject> successListener;
    private final Response.ErrorListener errorListener;
    private final Map<String, String> headers;
    private final JSONObject requestBody;

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    public DiagnoseRequest(ChatActivity chatActivity) {
        this(chatActivity, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    public DiagnoseRequest(ChatActivity chatActivity, @Nullable String userAnswer) {
        this.successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                boolean shouldStop;
                try {
                    shouldStop = response.getBoolean("should_stop");
                    RequestUtil.getInstance().setConditionsArray(response.getJSONArray("conditions"));
                    if (shouldStop) {
                        if (!listener.finishDiagnose()) {
                            shouldStop = false;
                        }
                    }
                    if (!shouldStop) {
                        JSONObject jsonObjectQuestion = response.getJSONObject("question");
                        listener.onDoctorMessage(jsonObjectQuestion.getString("text"));
                        listener.onDoctorQuestionReceived(jsonObjectQuestion.getJSONArray("items").getJSONObject(0).getString("id"),
                                jsonObjectQuestion.getJSONArray("items").getJSONObject(0).getJSONArray("choices"),
                                jsonObjectQuestion.getJSONArray("items").getJSONObject(0).getString("name"));
                    }
                } catch (JSONException e) {
                    chatActivity.onRequestFailure();
                }
            }
        };
        this.errorListener = error -> {
            System.out.println("HEADERS " + error.networkResponse.headers + "\r\n" + "BODY " + Arrays.toString(error.networkResponse.data));
            chatActivity.onRequestFailure();
        };
        listener = chatActivity;
        GlobalVariables globalVariables = GlobalVariables.getInstance();
        if (!globalVariables.getCurrentUser().isPresent()) {
            System.out.println("User not found!");
        }
        this.headers = RequestUtil.getDefaultHeaders(chatActivity);

        RequestUtil.addLanguageToInfermedicaHeaders(headers);

        this.requestBody = new JSONObject();

        try {
            JSONArray jsonArray = new JSONArray(RequestUtil.getInstance().getEvidenceArray().toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonArray.getJSONObject(i).remove("name");
            }
            RequestUtil.addUserDataToJsonObject(requestBody);
            requestBody.put("evidence", jsonArray);
            JSONObject jsonObjectExtras = new JSONObject();
            jsonObjectExtras.put("disable_groups", "true");
            requestBody.put("extras", jsonObjectExtras);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (userAnswer != null) {
            listener.addUserMessage(userAnswer);
        }
    }

    protected Response.Listener<JSONObject> getSuccessListener() {
        return successListener;
    }

    protected Response.ErrorListener getErrorListener() {
        return errorListener;
    }

    protected Map<String, String> getHeaders() {
        return headers;
    }

    protected JSONObject getRequestBody() {
        return requestBody;
    }

    protected void addAgeToRequestBody(Callable callable) throws JSONException {
        callable.call(this.requestBody);
    }

    protected interface Callable {
        void call(JSONObject jsonObject) throws JSONException;
    }
}
