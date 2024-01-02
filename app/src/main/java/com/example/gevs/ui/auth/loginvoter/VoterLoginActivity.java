package com.example.gevs.ui.auth.loginvoter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.gevs.R;
import com.example.gevs.data.BaseRepository;
import com.example.gevs.databinding.ActivityVoterLoginBinding;
import com.example.gevs.ui.auth.forgotpassword.ForgotPasswordActivity;
import com.example.gevs.ui.auth.loginadmin.AdminLoginActivity;
import com.example.gevs.ui.auth.registervoter.VoterRegisterOneActivity;
import com.example.gevs.ui.user.voter.VoterMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class VoterLoginActivity extends AppCompatActivity {

    ActivityVoterLoginBinding binding;
    BaseRepository baseRepository;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_voter_login);

        baseRepository = new BaseRepository();
        mAuth = FirebaseAuth.getInstance();

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

        binding.loginUserSignupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VoterLoginActivity.this, VoterRegisterOneActivity.class));
                finish();
            }
        });

        binding.loginUserLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCredentials();
            }
        });

    }

    public void validateCredentials() {
        String email = binding.loginUserEmailEditText.getText().toString().trim();
        String password = binding.loginUserPasswordEditText.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            signInUser(email, password);
        } else {
            Toast.makeText(VoterLoginActivity.this, "Please fill in all details", Toast.LENGTH_SHORT).show();
        }
    }

    public void signInUser(String email, String password) {
        showProgressIndicator(true);
        baseRepository.isAdmin(email).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    signInEmailAndPassword(email, password);
                } else {
                    showProgressIndicator(false);
                    Toast.makeText(VoterLoginActivity.this, "User is not a registered voter", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signInEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //User has logged in
                            if (mAuth.getCurrentUser() != null) {
                                startActivity(new Intent(VoterLoginActivity.this, VoterMainActivity.class));
                                finish();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            showProgressIndicator(false);
                            Log.w("SIGN IN", "signInWithEmail:failure", task.getException());
                            Toast.makeText(VoterLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void showProgressIndicator(Boolean showIndicator) {
        if (showIndicator) {
            binding.loadingIndiator.setVisibility(View.VISIBLE);
        } else {
            binding.loadingIndiator.setVisibility(View.GONE);
        }
    }


}