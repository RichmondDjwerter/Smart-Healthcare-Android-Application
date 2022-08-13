package com.example.modernizedshapp.doctor.Diagnosis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.modernizedshapp.R;
import com.example.modernizedshapp.User.Dashboard;
import com.example.modernizedshapp.databinding.ActivityMenuBinding;
import com.example.modernizedshapp.doctor.Diagnosis.api.RequestUtil;
import com.example.modernizedshapp.doctor.Diagnosis.db.Chat;
import com.example.modernizedshapp.doctor.Diagnosis.db.ChatSQLiteDBHelper;
import com.example.modernizedshapp.doctor.Diagnosis.helpers.ChatAdapter;
import com.example.modernizedshapp.doctor.Diagnosis.helpers.GlobalVariables;
import com.example.modernizedshapp.doctor.Diagnosis.helpers.SharedPreferencesHelper;

import java.util.ArrayList;


public class Menu extends AppCompatActivity implements AddProfileFragment.AddProfileFragmentListener {

    ActivityMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());
        setPicture();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadChats();
    }

    public void setPicture() {
        if (!GlobalVariables.getInstance().getCurrentUser().isPresent()) {
            SharedPreferencesHelper.loadUser(this);
        }

        if (GlobalVariables.getInstance().getCurrentUser().isPresent()) {
            binding.menuTop1Bar.profileImage.setImageBitmap(GlobalVariables.getInstance().getCurrentUser().get().getPicture());
        }
    }

    public void onProfilePictureClick(View v) {
        runAddProfileFragment(false);
    }

    public void onCreateNewProfileClick(View v) {
        runAddProfileFragment(true);
    }

    private void runAddProfileFragment(boolean isNewUser) {
        Fragment fragment = new AddProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(getString(R.string.is_new_user), isNewUser);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!isNewUser) {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        }
        transaction.replace(R.id.layoutToBeReplacedWithFragmentInMenu, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public void openCovid(View v) {
        GlobalVariables.getInstance().setCurrentChat(null);
        RequestUtil.getInstance().resetEvidenceArray();
        RequestUtil.getInstance().resetConditionsArray();
        Intent intent = new Intent(Menu.this, ChatActivity.class);
        intent.putExtra(getString(R.string.is_covid), true);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    public void onChangeProfileClick(View v) {
        startActivity(new Intent(getApplicationContext(), Dashboard.class));
    }

    private void loadChats() {
        ArrayList<Chat> chatArrayList = (ArrayList<Chat>) ChatSQLiteDBHelper.getAllChatsForUserFromDB(this,
                GlobalVariables.getInstance().getCurrentUser().get().getId());
        ChatAdapter chatAdapter = new ChatAdapter(this, chatArrayList);
        binding.chatList.setAdapter(chatAdapter);

    }

    public void goToChatActivity() {
        Intent intent = new Intent(Menu.this, ChatActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    public void newChatButtonOnClick(View v) {
        GlobalVariables.getInstance().setCurrentChat(null);
        RequestUtil.getInstance().resetEvidenceArray();
        RequestUtil.getInstance().resetConditionsArray();
        goToChatActivity();
    }

    @Override
    public void callback(String result) {
        if (result.equals(getString(R.string.reload))) {
            setPicture();
            loadChats();
        } else if (result.equals(getString(R.string.openChat))) {
            goToChatActivity();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent3 = new Intent(this, Dashboard.class);
        startActivity(intent3);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}