package com.example.modernizedshapp.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.modernizedshapp.R;
import com.example.modernizedshapp.doctor.HealthNews.HealthMainActivity;
import com.example.modernizedshapp.doctor.foodtip.SplashActivity;
import com.example.modernizedshapp.doctor.mapbox.MapBoxMainActivity;
import com.example.modernizedshapp.doctor.therapy.TheTherapist;

public class AllCategories extends AppCompatActivity {
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_all_categories);

        //Hooks
        backBtn = findViewById(R.id.back_pressed);

        backBtn.setOnClickListener(v -> AllCategories.super.onBackPressed());
    }

    public void callTherapyC(View view) {
        startActivity(new Intent(getApplicationContext(), TheTherapist.class));
    }

    public void callDoctorC(View view) {
        startActivity(new Intent(getApplicationContext(), com.example.modernizedshapp.doctor.Diagnosis.SplashActivity.class));
    }

    public void callLocationC(View view) {
        startActivity(new Intent(getApplicationContext(), MapBoxMainActivity.class));
    }

    public void callFoodC(View view) {
        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
    }

    public void callNewsstand(View view) {
        startActivity(new Intent(getApplicationContext(), HealthMainActivity.class));
    }
}