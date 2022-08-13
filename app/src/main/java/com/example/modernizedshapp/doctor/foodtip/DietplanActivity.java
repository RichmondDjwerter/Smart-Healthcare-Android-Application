package com.example.modernizedshapp.doctor.foodtip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modernizedshapp.R;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import info.hoang8f.android.segmented.SegmentedGroup;

public class DietplanActivity extends AppCompatActivity {

    String ageGroup;
    String weightGroup;
    String type = "veg";
    float BMR;

    ProgressDialog progressDialog;

    ScrollView layout;
    TextView tv_breakfastFood, tv_lunchFood, tv_snackFood, tv_dinnerFood;
    SegmentedGroup segmented;
    RadioButton btn_veg, btn_nonveg;

    public static final String BASE_URL = "https://webdietplan.000webhostapp.com/index.php/";
    //public static final String BASE_URL = "http://192.168.29.104/projectDietPlan/index.php";

    public String getItems(JSONArray array) {
        StringBuilder sb = new StringBuilder();
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject Object = array.getJSONObject(i);
                String food = Object.getString("food");
                String cal = Object.getString("cal");
                String fat = Object.getString("fat");
                String protein = Object.getString("protein");
                String qty = Object.getString("qty");
                String cat = Object.getString("cat");
                String item = food + " - " + qty + "\n" + "C:  " + cal + "\n" + "F:  " + fat + "\n" + "P:  " + protein + "\n\n";
                sb.append(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public void processDiet() {
        progressDialog = new ProgressDialog(DietplanActivity.this);
        progressDialog.setTitle("Diet Plan");
        progressDialog.setMessage("Processing ...");
        progressDialog.show();

        StringRequest obreq = new StringRequest(Request.Method.POST, BASE_URL,
                // The third parameter Listener overrides the method onResponse() and passes
                //JSONObject as a parameter
                new Response.Listener<String>() {
                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(String response) {
                        try {
                            //tv_breakfastFood.setText(response);
                            JSONObject obj = new JSONObject(response);

                            JSONArray breakfastArray = obj.getJSONArray("breakfast");
                            JSONArray lunchArray = obj.getJSONArray("lunch");
                            JSONArray snackArray = obj.getJSONArray("snack");
                            JSONArray dinnerArray = obj.getJSONArray("dinner");

                            float totalCal = BMR;
                            totalCal -= 175;

                            String bfTimings = "07:30 AM  -  10:00 AM";
                            String lTimings = "12:30 PM  -  02:00 PM";
                            String sTimings = "04:30 PM  -  06:00 PM";
                            String dTimings = "08:30 PM  -  10:00 PM";

                            String bfcalInfo = "Total Calories: \t" + Math.round(totalCal / 4) + " cal \n\n";
                            String lcalInfo = "Total Calories: \t" + Math.round(3 * totalCal / 7) + " cal \n\n";
                            String scalInfo = "Total Calories: \t" + 175 + " cal \n\n";
                            String dcalInfo = "Total Calories: \t" + Math.round(3 * totalCal / 7) + " cal \n\n";

                            tv_breakfastFood.setText(bfTimings + "\n\n\n" + bfcalInfo);
                            tv_breakfastFood.append(getItems(breakfastArray));

                            tv_lunchFood.setText(lTimings + "\n\n\n" + lcalInfo);
                            tv_lunchFood.append(getItems(lunchArray));

                            tv_snackFood.setText(sTimings + "\n\n\n" + scalInfo);
                            tv_snackFood.append(getItems(snackArray));

                            tv_dinnerFood.setText(dTimings + "\n\n\n" + dcalInfo);
                            tv_dinnerFood.append(getItems(dinnerArray));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DietplanActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ageGroup", ageGroup.replaceAll(" ", ""));
                params.put("weightGroup", weightGroup);
                params.put("BMR", "" + BMR);
                params.put("type", type);
                return params;
            }
        };

        // Adds the JSON object request "obreq" to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(DietplanActivity.this);
        requestQueue.add(obreq);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dietplan);
        layout = findViewById(R.id.layout);

        segmented = (SegmentedGroup) findViewById(R.id.segmented);
        btn_veg = findViewById(R.id.button1);
        btn_nonveg = findViewById(R.id.button2);

        tv_breakfastFood = findViewById(R.id.breakfastFoods);
        tv_lunchFood = findViewById(R.id.lunchFoods);
        tv_snackFood = findViewById(R.id.snackFoods);
        tv_dinnerFood = findViewById(R.id.dinnerFoods);

        Intent intent = getIntent();
        ageGroup = intent.getStringExtra("ageGroup");
        weightGroup = intent.getStringExtra("weightGroup");
        BMR = intent.getFloatExtra("BMR", 1200);

        vegButtonClicked(layout);
        btn_veg.setChecked(true);
    }

    public void vegButtonClicked(View view) {
        type = "veg";
        segmented.setTintColor(Color.rgb(90, 220, 101));
        processDiet();
    }

    public void nonVegButtonClicked(View view) {
        type = "nonveg";
        segmented.setTintColor(Color.rgb(220, 105, 90));
        processDiet();
    }

}