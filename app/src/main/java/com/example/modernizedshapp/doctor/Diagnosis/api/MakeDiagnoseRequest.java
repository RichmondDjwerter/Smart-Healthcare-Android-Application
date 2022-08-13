package com.example.modernizedshapp.doctor.Diagnosis.api;

import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.modernizedshapp.doctor.Diagnosis.ChatActivity;

import org.json.JSONException;


public class MakeDiagnoseRequest extends DiagnoseRequest {

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    public MakeDiagnoseRequest(ChatActivity chatActivity, @Nullable String userAnswer) {
        super(chatActivity, userAnswer);

        String url = InfermedicaApiClass.getInstance(chatActivity).getUrl() + "/v3" + "/diagnosis";
        try {
            this.addAgeToRequestBody(RequestUtil::addAgeToJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiRequestQueue.getInstance(chatActivity).addToRequestQueue(new JSONObjectRequestWithHeaders(1, url, this.getHeaders(), this.getRequestBody(), this.getSuccessListener(), this.getErrorListener()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    public MakeDiagnoseRequest(ChatActivity chatActivity) {
        this(chatActivity, null);
    }
}
