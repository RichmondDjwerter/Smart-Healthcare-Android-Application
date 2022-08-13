package com.example.modernizedshapp.doctor.Diagnosis.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONObject;

import java.util.Locale;

public class MakeTranslatorRequest {

    public MakeTranslatorRequest(Context context, String text, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        String apiKey = TranslatorApiClass.getInstance(context).getKey();
        String apiLangSource = Locale.getDefault().getLanguage();
        String apiLangTarget = "en";
        String googleApiUrl = TranslatorApiClass.getInstance(context).getUrl() + "?key=" + apiKey + "&source=" + apiLangSource
                + "&target=" + apiLangTarget + "&q=" + text;
        ApiRequestQueue.getInstance(context).addToRequestQueue(new JSONObjectRequestWithHeaders(Request.Method.GET,
                googleApiUrl, null, null, successListener, errorListener));
    }
}
