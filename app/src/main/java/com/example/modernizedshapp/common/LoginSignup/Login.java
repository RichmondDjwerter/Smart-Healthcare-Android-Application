package com.example.modernizedshapp.common.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.modernizedshapp.Databases.SessionManager;
import com.example.modernizedshapp.R;
import com.example.modernizedshapp.User.Dashboard;
import com.example.modernizedshapp.doctor.UserDashboard;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

public class Login extends AppCompatActivity {
    Button create_acc;
    TextInputLayout mobileNumber, password;
    RelativeLayout progressbar;
    CountryCodePicker countryCodePicker;
    CheckBox rememberMe;
    TextInputEditText PhoneNumberEditText, PasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //Hooks
        create_acc = findViewById(R.id.ca_start_up);
        mobileNumber = findViewById(R.id.login_mobile_number);
        password = findViewById(R.id.login_password);
        countryCodePicker = findViewById(R.id.login_countryCodePicker);
        progressbar = findViewById(R.id.login_progressbar);
        rememberMe = findViewById(R.id.remember_me);
        PhoneNumberEditText = findViewById(R.id.text_number);
        PasswordEditText = findViewById(R.id.text_password);


        SessionManager sessionManager =new SessionManager(Login.this, SessionManager.SESSION_REMEMBERME);
        if(sessionManager.checkRememberMe()){
            HashMap<String, String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
            PhoneNumberEditText.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPHONENUMBER));
            PasswordEditText.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPASSWORD));
        }
    }

    public void callSignUp_Screen(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(R.id.ca_start_up), "transition_sign_up");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    public void callForgetPassword(View view) {
        startActivity(new Intent(getApplicationContext(), Forget_Password.class));
    }

    public void letUserLogIn(View view) {

        if (!isConnected(this)) {
            showCustomDialog();
            return;
        }

        if (!validateFields()) {
            return;
        }

        progressbar.setVisibility(View.VISIBLE);

        String _phoneNumberEnteredByUser = mobileNumber.getEditText().getText().toString().trim();
        final String _passwordNumberEnteredByUser = password.getEditText().getText().toString().trim();

        if (_phoneNumberEnteredByUser.charAt(0) == '0') {
            _phoneNumberEnteredByUser = _phoneNumberEnteredByUser.substring(1);
        }

        String _completePhoneNumber = "+" + countryCodePicker.getFullNumber() + _phoneNumberEnteredByUser;

        if (rememberMe.isChecked()){
            SessionManager sessionManager =new SessionManager(Login.this, SessionManager.SESSION_REMEMBERME);
            sessionManager.createRememberMeSession(_phoneNumberEnteredByUser, _passwordNumberEnteredByUser);
        }

        Query checkUser = FirebaseDatabase.getInstance().getReference("Registration").orderByChild("phoneNo").equalTo(_completePhoneNumber);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mobileNumber.setError(null);
                    mobileNumber.setErrorEnabled(false);

                    String systemPassword = snapshot.child(_completePhoneNumber).child("password").getValue(String.class);
                    if (systemPassword.equals(_passwordNumberEnteredByUser)) {
                        password.setError(null);
                        password.setErrorEnabled(false);

                        String _name = snapshot.child(_completePhoneNumber).child("fullName").getValue(String.class);
                        String _email = snapshot.child(_completePhoneNumber).child("email").getValue(String.class);
                        String _phoneNo = snapshot.child(_completePhoneNumber).child("phoneNo").getValue(String.class);
                        String _date = snapshot.child(_completePhoneNumber).child("date").getValue(String.class);
                        String _gender = snapshot.child(_completePhoneNumber).child("gender").getValue(String.class);
                        String _username = snapshot.child(_completePhoneNumber).child("username").getValue(String.class);
                        String _password = snapshot.child(_completePhoneNumber).child("password").getValue(String.class);

                        SessionManager sessionManager = new SessionManager(Login.this, SessionManager.SESSION_USERSESSION);
                        sessionManager.createLoginSession(_name, _username, _email, _phoneNo, _date, _gender, _password);

                        startActivity(new Intent(getApplicationContext(), UserDashboard.class));

                        Toast.makeText(Login.this, _name + "\n" + _email + "\n" + _phoneNo + "\n" + _date, Toast.LENGTH_LONG).show();

                    } else {
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "sorry, password does not match!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "sorry, user does not exist!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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

    private boolean isConnected(Login login) {
        ConnectivityManager connectivityManager = (ConnectivityManager) login.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }

    }

    private boolean validateFields() {
        String _phoneNumber = mobileNumber.getEditText().getText().toString().trim();
        String _password = password.getEditText().getText().toString().trim();

        if (_phoneNumber.isEmpty()) {
            mobileNumber.setError("Phone Number cannot be empty");
            mobileNumber.requestFocus();
            return false;
        } else if (_password.isEmpty()) {
            password.setError("Password cannot be empty");
            password.requestFocus();
            return false;
        } else {
            mobileNumber.setError(null);
            mobileNumber.setErrorEnabled(false);
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
}