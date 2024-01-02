package com.example.gevs.ui.auth.forgotpassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gevs.R;
import com.example.gevs.databinding.ActivityForgotPasswordBinding;
import com.example.gevs.ui.auth.loginadmin.AdminLoginActivity;
import com.example.gevs.ui.auth.loginvoter.VoterLoginActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);

        binding.forgotPasswordSigninTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, VoterLoginActivity.class));
                finish();
            }
        });


    }
}