package com.example.modernizedshapp.doctor.foodtip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.modernizedshapp.R;

public class FMainScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    float height = (float) 120 / 100, weight, cal;
    float active_level = 1.2f;
    TextView height_txt, age;
    int gender = -1;
    int count_weight = 50, count_age = 30;
    RelativeLayout weight_plus, weight_minus, age_plus, age_minus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_f_main_screen);

        height_txt = findViewById(R.id.height_txt);

        final TextView female_text = findViewById(R.id.female);
        final TextView male_text = findViewById(R.id.male);

        CardView card_female = findViewById(R.id.cardView_female);
        CardView card_male = findViewById(R.id.cardView_male);

        age_minus = findViewById(R.id.age_minus);
        age_plus = findViewById(R.id.age_plus);


        weight_minus = findViewById(R.id.weight_minus);
        weight_plus = findViewById(R.id.weight_plus);

        card_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male_text.setTextColor(Color.parseColor("#FFFFFF"));
                male_text.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.male_white, 0, 0);

                female_text.setTextColor(Color.parseColor("#8D8E99"));
                female_text.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.female_dark, 0, 0);

                gender = 1;
            }
        });

        card_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female_text.setTextColor(Color.parseColor("#FFFFFF"));
                female_text.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.female_white, 0, 0);

                male_text.setTextColor(Color.parseColor("#8D8E99"));
                male_text.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.male_dark, 0, 0);

                gender = 0;
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("Sedentary (little or no exercise)");
        categories.add("Lightly active (1-3 days/week)");
        categories.add("Moderately active (3-5 days/week)");
        categories.add("Very active (6-7 days/week)");
        categories.add("Super active (twice/day)");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, categories);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);


        CheckSeekbarStatus();

        CheckWeight();

        CheckAge();

        Button calculate = findViewById(R.id.calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculateBMI();
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        int index = parent.getSelectedItemPosition();
        if (index == 0) active_level = (float) 1.2;
        else if (index == 1) active_level = (float) 1.375;
        else if (index == 2) active_level = (float) 1.55;
        else if (index == 3) active_level = (float) 1.725;
        else if (index == 4) active_level = (float) 1.9;

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void CheckAge() {

        age = findViewById(R.id.age);

        age_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count_age++;
                age.setText(String.valueOf(count_age));
            }
        });

        age_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count_age--;
                age.setText(String.valueOf(count_age));
            }
        });
    }

    private void CheckWeight() {

        final TextView weight_txt = findViewById(R.id.weight);

        weight_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count_weight++;
                weight_txt.setText(String.valueOf(count_weight));
            }
        });

        weight_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count_weight--;
                weight_txt.setText(String.valueOf(count_weight));
            }
        });

        weight = Float.parseFloat(weight_txt.getText().toString());

    }
    private void CheckSeekbarStatus() {

        SeekBar Seekbar = findViewById(R.id.Seekbar);
        Seekbar.setMax(220);
        Seekbar.setProgress(120);
        Seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String ht = progress + getResources().getString(R.string.cm);
                height_txt.setText(ht);
                height = (float) (progress) / 100;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void CalculateBMI() {
        if (gender == -1) {
            Toast.makeText(FMainScreen.this, "Select gender", Toast.LENGTH_SHORT).show();
            return;
        }

        float BMI = count_weight / (height * height);

        String weightGroup = null;
        if (BMI >= 25) {
            weightGroup = "Overweight";
        } else if (BMI > 18.5) {
            weightGroup = "Normal";
        } else {
            weightGroup = "Underweight";
        }

        float BMR;
        if (gender == 1) {
            cal = (float) (88.362 + (13.397 * count_weight) + (4.799 * height * 100) - (5.677 * count_age));
        } else if (gender == 0) {
            cal = (float) (447.593 + (9.247 * count_weight) + (3.098 * height * 100) - (4.3 * count_age));
        }
        BMR = cal;

        String ageGroup = null;
        if (count_age >= 20 && count_age <= 39) {
            ageGroup = "AgeGroup - 1 ( 20 - 39 )";
        } else if (count_age >= 40 && count_age <= 59) {
            ageGroup = "AgeGroup - 2  ( 40 - 59 )";
        } else if (count_age >= 60) {
            ageGroup = "AgeGroup - 3  ( 60 - more )";
        }

        Intent intent = new Intent(FMainScreen.this, ResultActivity.class);
        intent.putExtra("BMI", BMI);
        intent.putExtra("BMR", BMR);
        intent.putExtra("weightGroup", weightGroup);
        intent.putExtra("ageGroup", ageGroup);
        intent.putExtra("age", age.getText().toString());
        intent.putExtra("active", active_level);
        startActivity(intent);
    }
}