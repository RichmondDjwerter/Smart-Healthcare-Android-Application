package com.example.modernizedshapp.doctor.Diagnosis.api;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;


public class JSONObjectRequestWithHeaders extends JsonObjectRequest {

    private final Map<String, String> headers;

    public JSONObjectRequestWithHeaders(int method, String url, Map<String, String> headers, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        this.headers = headers;
    }

    @Override
    public String toString() {
        return "JSONObjectRequestWithHeaders{" +
                "headers=" + headers +
                "} " + super.toString();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        return super.parseNetworkResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

}
