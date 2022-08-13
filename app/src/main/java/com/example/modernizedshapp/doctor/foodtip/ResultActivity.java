package com.example.modernizedshapp.doctor.foodtip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

import com.example.modernizedshapp.R;

public class ResultActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://192.168.29.104/projectDietPlan/index.php";
    public String foodList = null;
    String ageGroup, weightGroup;
    float BMR, active_level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();

        float BMI = Math.round((intent.getFloatExtra("BMI", 0) * 100) / 100);
        BMR = intent.getFloatExtra("BMR", 0);
        ageGroup = intent.getStringExtra("ageGroup");
        weightGroup = intent.getStringExtra("weightGroup");
        active_level = intent.getFloatExtra("active", 1.2f);
        String age_value = intent.getStringExtra("age");

        BMR = Math.round(BMR * active_level);

        TextView your_bmi = findViewById(R.id.your_bmi);
        your_bmi.setText(String.valueOf(BMI));

        TextView age = findViewById(R.id.age);
        age.setText(ageGroup);

        final TextView category = findViewById(R.id.category);
        Category category1 = new Category();
        category.setText("" + BMR);

        TextView condition = findViewById(R.id.condition);
        Condition condition1 = new Condition();
        condition.setText(condition1.getCategory(BMI));

        final Button recalculate = findViewById(R.id.recalculate);
        recalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI();
            }
        });
    }

    private void updateUI() {
        Intent intent1 = new Intent(ResultActivity.this, DietplanActivity.class);
        //intent1.putExtra("snackFoods",foodList);
        intent1.putExtra("ageGroup", ageGroup.replaceAll(" ", ""));
        intent1.putExtra("weightGroup", weightGroup);
        intent1.putExtra("BMR", BMR);
        startActivity(intent1);
        fileList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}