package com.example.modernizedshapp.doctor.Diagnosis.api;

import android.content.Context;

import com.example.modernizedshapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class TranslatorApiClass {

    private static final TranslatorApiClass INSTANCE = new TranslatorApiClass();

    private static final String url = "https://www.googleapis.com/language/translate/v2";
    private static String key;

    private TranslatorApiClass() {
    }

    public static TranslatorApiClass getInstance(Context context) {
        loadApiInfo(context);
        return INSTANCE;
    }

    private static void loadApiInfo(Context context) {
        try {
            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(loadJSONFromAsset(context)));
            key = (String) jsonObject.get("key");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static String loadJSONFromAsset(Context context) {
        String jsonString = "";
        try {
            InputStream is = context.getResources().openRawResource(R.raw.translator_api_info);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return jsonString;
    }

    public String getUrl() {
        return url;
    }

    public String getKey() {
        return key;
    }

}
