package com.example.modernizedshapp.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modernizedshapp.R;

public class Bmi_calculator extends AppCompatActivity {
    android.widget.Button calculated;
    ImageView backBtn, addweight, addage, decweight, decage;
    TextView textView, textView2, textView3;
    SeekBar seekBar;
    RelativeLayout male, female;

    int intweight = 55;
    int intage = 22;
    int currenprogress;
    String mintprogress = "170";
    String typeofuser = "0";
    String weight2 = "55";
    String age2 = "22";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bmi_calculator);

        //Hooks
        calculated = findViewById(R.id.calculateBmi);
        addage = findViewById(R.id.incrementAge);
        decage = findViewById(R.id.decrementAge);
        addweight = findViewById(R.id.incrementWeight);
        decweight = findViewById(R.id.decrementWeight);
        textView = findViewById(R.id.currentAge);
        textView2 = findViewById(R.id.currentWeight);
        textView3 = findViewById(R.id.currentHeight);
        seekBar = findViewById(R.id.seekBarForHeight);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        backBtn = findViewById(R.id.bmi_back);

        backBtn.setOnClickListener(v -> Bmi_calculator.super.onBackPressed());

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.male_female_drawable));
                female.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.male_female_drawable));
                typeofuser = "Male";
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.male_female_drawable));
                male.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.male_female_drawable));
                typeofuser = "Female";
            }
        });


        seekBar.setMax(300);
        seekBar.setProgress(170);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currenprogress = progress;
                mintprogress = String.valueOf(currenprogress);
                textView3.setText(mintprogress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        addage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intage = intage + 1;
                age2 = String.valueOf(intage);
                textView.setText(age2);
            }
        });

        addweight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intweight = intweight + 1;
                weight2 = String.valueOf(intweight);
                textView.setText(weight2);
            }
        });

        decweight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intweight = intweight - 1;
                weight2 = String.valueOf(intweight);
                textView.setText(weight2);
            }
        });

        decage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intage = intage - 1;
                age2 = String.valueOf(intage);
                textView.setText(age2);
            }
        });

        calculated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeofuser.equals("0")) {
                    Toast.makeText(getApplicationContext(), "Select Your Gender", Toast.LENGTH_LONG).show();
                } else if (mintprogress.equals("0")) {
                    Toast.makeText(getApplicationContext(), "Select Your Height", Toast.LENGTH_LONG).show();
                } else if (intage == 0 || intage < 0) {
                    Toast.makeText(getApplicationContext(), "Age cannot be 0 nor less than 0", Toast.LENGTH_LONG).show();
                } else if (intweight == 0 || intweight < 0) {
                    Toast.makeText(getApplicationContext(), "weight cannot be 0 nor less than 0", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(Bmi_calculator.this, BMI_calculated.class);
                    intent.putExtra("gender", typeofuser);
                    intent.putExtra("height", mintprogress);
                    intent.putExtra("age", age2);
                    intent.putExtra("weight", weight2);
                    startActivity(intent);
                }
            }
        });
    }
}