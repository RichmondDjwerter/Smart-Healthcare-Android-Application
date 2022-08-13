package com.example.modernizedshapp.common.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.modernizedshapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class Forget_Password extends AppCompatActivity {
    ImageView bigImage;
    TextView title, description;
    CountryCodePicker countryCodePicker;
    TextInputLayout phoneNumber_textInput;
    Button nextBtn;
    RelativeLayout progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget__password);

        //Hooks
        bigImage = findViewById(R.id.forgetP_imageView);
        title = findViewById(R.id.forgetP_title);
        description = findViewById(R.id.forgetP_description);
        countryCodePicker = findViewById(R.id.forgetP_countryCodePicker);
        phoneNumber_textInput = findViewById(R.id.ForgetP_mobile_number);
        nextBtn = findViewById(R.id.forgetP_nextBtn);
        progressbar = findViewById(R.id.forgetP_progressbar);
    }

    public void VerifyPhoneNumber(View view) {
        //check connection
        if (!isConnected(this)) {
            showCustomDialog();
            return;
        }

        if (!validateFields()) {
            return;
        }

        progressbar.setVisibility(View.VISIBLE);

        String _phoneNumberEnteredByUser = phoneNumber_textInput.getEditText().getText().toString().trim();

        if (_phoneNumberEnteredByUser.charAt(0) == '0')
            _phoneNumberEnteredByUser = _phoneNumberEnteredByUser.substring(1);

        final String _completePhoneNumber = "+" + countryCodePicker.getFullNumber() + _phoneNumberEnteredByUser;

        Query checkUser = FirebaseDatabase.getInstance().getReference("Registration").orderByChild("phoneNo").equalTo(_completePhoneNumber);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    phoneNumber_textInput.setError(null);
                    phoneNumber_textInput.setErrorEnabled(false);

                    Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                    intent.putExtra("phoneNo", _completePhoneNumber);
                    intent.putExtra("whatToDo", "updateData");
                    startActivity(intent);
                    finish();

                    progressbar.setVisibility(View.GONE);

                } else {
                    progressbar.setVisibility(View.GONE);
                    phoneNumber_textInput.setError("No such user exist.\nPlease create a new Account.");
                    phoneNumber_textInput.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Forget_Password.this);
        builder.setMessage("Please Check your internet connectivity")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), StartUpScreen.class));
                        finish();
                    }
                });
    }

    private boolean isConnected(Forget_Password forgetPassword) {
        ConnectivityManager connectivityManager = (ConnectivityManager) forgetPassword.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }

    }

    private boolean validateFields() {
        String _phoneNumber = phoneNumber_textInput.getEditText().getText().toString().trim();

        if (_phoneNumber.isEmpty()) {
            phoneNumber_textInput.setError("Phone Number cannot be empty");
            phoneNumber_textInput.requestFocus();
            return false;
        } else {
            phoneNumber_textInput.setError(null);
            phoneNumber_textInput.setErrorEnabled(false);
            return true;
        }
    }
}