package com.example.gevs.ui.auth.loginvoter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gevs.R;
import com.example.gevs.databinding.ActivityVoterLoginBinding;
import com.example.gevs.ui.auth.forgotpassword.ForgotPasswordActivity;
import com.example.gevs.ui.auth.loginadmin.AdminLoginActivity;

public class VoterLoginActivity extends AppCompatActivity {

    ActivityVoterLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_voter_login);

        binding.loginAdminText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VoterLoginActivity.this, AdminLoginActivity.class));
                finish();
            }
        });

        binding.loginUserForgotPasswordTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VoterLoginActivity.this, ForgotPasswordActivity.class));
                finish();
            }
        });

    }


}