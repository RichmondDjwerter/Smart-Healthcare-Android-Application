package com.example.modernizedshapp.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.modernizedshapp.R;

public class BMI_calculated extends AppCompatActivity {

    android.widget.Button recalculate;

    TextView mbmidisplay, mbmicat, mbmigender;
    Intent intent;
    ImageView mimageView;
    String mbmi;
    float intmbi;

    String height;
    String weight;
    float intweight, intheight;
    RelativeLayout mbackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_b_m_i_calculated);

        //Hooks
        intent = getIntent();
        mbmidisplay = findViewById(R.id.BmiDisplay);
        mbmigender = findViewById(R.id.genderDisplay);
        mbmicat = findViewById(R.id.catDisplay);
        mimageView = findViewById(R.id.successImage);
        mbackground = findViewById(R.id.contentLayout);
        recalculate = findViewById(R.id.recalculateBmi);

        height = intent.getStringExtra("height");
        weight = intent.getStringExtra("weight");

        intheight = Float.parseFloat(height);
        intweight = Float.parseFloat(weight);

        intheight = intheight / 100;

        intmbi = intweight / (intheight * intheight);

        mbmi = Float.toString(intmbi);

        if (intmbi < 16) {
            mbmicat.setText("You Are Underweight");
        } else if (intmbi < 16.9 && intmbi > 16) {
            mbmicat.setText("Moderately UnderWeight");
        } else if (intmbi < 18.5 && intmbi > 16.9) {
            mbmicat.setText("Mildly UnderWeight");
        } else if (intmbi < 24.9 && intmbi > 18.5) {
            mbmicat.setText("Ideal");
        } else if (intmbi < 29.9 && intmbi > 24.9) {
            mbmicat.setText("OverWeight");
        } else {
            mbmicat.setText("Obese class");
        }

        mbmigender.setText(intent.getStringExtra("gender"));
        mbmidisplay.setText(mbmi);

        recalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BMI_calculated.this, Bmi_calculator.class);
                startActivity(intent);
                finish();
            }
        });
    }
}