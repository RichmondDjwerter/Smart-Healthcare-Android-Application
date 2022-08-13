package com.example.modernizedshapp.doctor.Diagnosis.helpers;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

import com.example.modernizedshapp.databinding.WarningDialogBinding;

import java.util.Objects;

public class WarningDialog extends Dialog {

    private ConfirmationListener confirmationListener;
    private WarningDialogBinding binding;

    public WarningDialog(@NonNull Context context) {
        super(context);
    }


    public void setConfirmationListener(ConfirmationListener confirmationListener) {
        this.confirmationListener = confirmationListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = WarningDialogBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCanceledOnTouchOutside(true);
        setContentView(binding.getRoot());
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.binding.confirmButton.setOnClickListener(this::confirmButtonOnClick);
        this.setOnCancelListener(this::confirmButtonOnClick);
    }

    private void confirmButtonOnClick(DialogInterface dialogInterface) {
        confirmButtonOnClick();
    }

    public void confirmButtonOnClick(View v) {
        confirmButtonOnClick();
    }

    public void confirmButtonOnClick() {
        this.confirmationListener.onUserAccept();
        this.dismiss();
    }

    public interface ConfirmationListener {
        void onUserAccept();
    }
}
