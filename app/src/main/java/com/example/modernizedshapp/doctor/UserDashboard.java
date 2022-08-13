package com.example.modernizedshapp.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.modernizedshapp.Databases.SessionManager;
import com.example.modernizedshapp.R;
import com.example.modernizedshapp.User.Dashboard;
import com.example.modernizedshapp.User.Emergencies;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class UserDashboard extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_dashboard);

        TextView textView = findViewById(R.id.TextView);
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDataFromSession();

        String fullName = userDetails.get(SessionManager.KEY_FULLNAME);
        String username = userDetails.get(SessionManager.KEY_USERNAME);
        String email = userDetails.get(SessionManager.KEY_EMAIL);
        String password = userDetails.get(SessionManager.KEY_PASSWORD);
        String phoneNumber = userDetails.get(SessionManager.KEY_PHONENUMBER);
        String date = userDetails.get(SessionManager.KEY_DATE);
        String gender = userDetails.get(SessionManager.KEY_GENDER);


        textView.setText(
                "FULL NAME: " + fullName +
                        "\nUSERNAME: " + username +
                        "\nEMAIL: " + email +
                        "\nPASSWORD: " + password +
                        "\nPhone Number: " + phoneNumber +
                        "\nDATE OF BIRTH: " + date +
                        "\nGENDER: " + gender
        );

        bottomNavigationView = findViewById(R.id.buttom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.buttom_nav_dashboard);

        bottomNavigationView.setOnNavigationItemReselectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.buttom_nav_dashboard:
                case R.id.buttom_nav_home:
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    overridePendingTransition(0, 0);
                    return;
                case R.id.buttom_nav_profle:
                    startActivity(new Intent(getApplicationContext(), Emergencies.class));
                    overridePendingTransition(0, 0);
            }
        });

    }
}