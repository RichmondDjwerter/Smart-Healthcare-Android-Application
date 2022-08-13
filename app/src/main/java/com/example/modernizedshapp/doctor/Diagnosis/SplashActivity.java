package com.example.modernizedshapp.doctor.Diagnosis;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.modernizedshapp.R;
import com.example.modernizedshapp.databinding.ActivitySplashScreenBinding;
import com.example.modernizedshapp.doctor.Diagnosis.db.User;
import com.example.modernizedshapp.doctor.Diagnosis.helpers.GlobalVariables;
import com.example.modernizedshapp.doctor.Diagnosis.helpers.NotificationHelper;
import com.example.modernizedshapp.doctor.Diagnosis.helpers.Receiver;
import com.example.modernizedshapp.doctor.Diagnosis.helpers.SharedPreferencesHelper;

import static com.example.modernizedshapp.doctor.Diagnosis.helpers.NotificationHelper.REQUEST_CODE;


public class SplashActivity extends AppCompatActivity implements AddProfileFragment.AddProfileFragmentListener {

    private final static int SPLASH_TIME_OUT = 1500;
    private final Runnable loadRunnable = () -> {
        SharedPreferencesHelper.loadUser(SplashActivity.this);
        User.loadBitmaps(SplashActivity.this);
    };

    ActivitySplashScreenBinding binding;
    private Thread loadThread;
    private final Runnable waifForLoadRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                loadThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                runOnUiThread(() -> new Handler().postDelayed(() -> {
                    if (GlobalVariables.getInstance().getCurrentUser().isPresent()) {
                        startChatActivity();
                    } else {
                        runFragment();
                    }
                }, SPLASH_TIME_OUT));
            }
        }
    };

    private void startChatActivity() {
        Intent intent = new Intent(SplashActivity.this, ChatActivity.class);
        startActivity(intent);
        finish();
    }

    private void runFragment() {
        Fragment fragment = new AddProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(getString(R.string.is_new_user), true);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layoutToBeReplacedWithFragmentInMenu, fragment)
                .addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivLogo.setAnimation(AnimationUtils.loadAnimation(this, R.anim.from_top_animation));
        binding.tvName.setAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_for_title));
        binding.tvDesc.setAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_for_description));

        Intent intent = new Intent(SplashActivity.this, Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(SplashActivity.this, REQUEST_CODE, intent, 0);
        NotificationHelper.setNotification(pendingIntent, getApplicationContext());

        this.loadThread = new Thread(this.loadRunnable);
        this.loadThread.start();
        new Thread(this.waifForLoadRunnable).start();
    }

    @Override
    public void onBackPressed() {
        if (GlobalVariables.getInstance().getCurrentUser().isPresent()) {
            startChatActivity();
        }
    }

    @Override
    public void callback(String result) {
    }
}
