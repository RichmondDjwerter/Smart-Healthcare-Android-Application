package com.example.modernizedshapp.common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.modernizedshapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

public class SignUp3rdClass extends AppCompatActivity {
    //variables
    ImageView backBtn;
    Button next, login;
    TextView titleText;

    TextInputLayout phoneNo;
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up3rd_class);

        //Hooks
        backBtn = findViewById(R.id.signup_backBtn);
        next = findViewById(R.id.sign_up_next);
        login = findViewById(R.id.sign_up_login);
        titleText = findViewById(R.id.create_account_title);
        phoneNo = findViewById(R.id.mobile_number);
        countryCodePicker = findViewById(R.id.countryCodePicker);
    }

    public void callVerifyOTPUser(View view) {
        if (!validatePhoneNumber()) {
            return;
        }
        String _name = getIntent().getStringExtra("fullName");
        String _email = getIntent().getStringExtra("email");
        String _username = getIntent().getStringExtra("c");
        String _password = getIntent().getStringExtra("password");
        String _date = getIntent().getStringExtra("date");
        String _gender = getIntent().getStringExtra("gender");

        String _getUSerEnteredPhoneNumber = phoneNo.getEditText().getText().toString().trim();
        String _phoneNo = "+" + countryCodePicker.getFullNumber() + _getUSerEnteredPhoneNumber;


        Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);

        //pass all data to the next activity
        intent.putExtra("fullName", _name);
        intent.putExtra("email", _email);
        intent.putExtra("username", _username);
        intent.putExtra("password", _password);
        intent.putExtra("date", _date);
        intent.putExtra("gender", _gender);
        intent.putExtra("phoneNo", _phoneNo);

        //add transition
        Pair[] pair = new Pair[3];
        pair[0] = new Pair<View, String>(backBtn, "transition_back_arrow_button");
        pair[1] = new Pair<View, String>(next, "transition_next");
        pair[2] = new Pair<View, String>(titleText, "transition_title_text");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp3rdClass.this, pair);
            startActivity(intent, options.toBundle());

        } else {
            startActivity(intent);
        }
    }

    public void callLoginScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), Login.class);

        Pair[] pairs = new Pair[1];

        pairs[0] = new Pair<View, String>(findViewById(R.id.sign_up_login), "transition_login");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp3rdClass.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    private boolean validatePhoneNumber() {
        String val = phoneNo.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            phoneNo.setError("Enter valid phone number");
            return false;
        } else {
            phoneNo.setError(null);
            phoneNo.setErrorEnabled(false);
            return true;
        }
    }
}