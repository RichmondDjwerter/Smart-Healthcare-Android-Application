package com.example.modernizedshapp.doctor.Diagnosis.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ApiRequestQueue {

    private static ApiRequestQueue INSTANCE;
    private static Context ctx;
    private RequestQueue requestQueue;

    private ApiRequestQueue(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized ApiRequestQueue getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ApiRequestQueue(context);
        }
        return INSTANCE;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
