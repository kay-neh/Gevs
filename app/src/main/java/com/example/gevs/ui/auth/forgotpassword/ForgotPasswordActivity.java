package com.example.gevs.ui.auth.forgotpassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.gevs.R;
import com.example.gevs.data.BaseRepository;
import com.example.gevs.databinding.ActivityForgotPasswordBinding;
import com.example.gevs.ui.auth.loginadmin.AdminLoginActivity;
import com.example.gevs.ui.auth.loginvoter.VoterLoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;
    BaseRepository baseRepository;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        baseRepository = new BaseRepository();

        binding.forgotPasswordSigninTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, VoterLoginActivity.class));
                finish();
            }
        });

        binding.resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCredentials();
            }
        });

    }

    private void validateCredentials() {
        String email = binding.forgotPasswordEmailEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(email)) {
            showProgressIndicator(true);
            // check if email exists first
            baseRepository.isExistingEmail(email).observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (aBoolean) {
                        sendRecoveryMail(email);
                    } else {
                        showProgressIndicator(false);
                        Toast.makeText(ForgotPasswordActivity.this, "Email address not registered", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Toast.makeText(ForgotPasswordActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendRecoveryMail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        showProgressIndicator(false);
                        if (task.isSuccessful()) {
                            Log.d("Recovery Mail", "Email sent.");
                            showMailSentDialog(email);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Recovery Mail", "failure", task.getException());
                            Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showMailSentDialog(String email) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Check your email");
        builder.setMessage("Follow the instructions sent to " + email + " to recover your password.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(ForgotPasswordActivity.this, VoterLoginActivity.class));
                finish();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                startActivity(new Intent(ForgotPasswordActivity.this, VoterLoginActivity.class));
                finish();
            }
        });
        builder.show();
    }

    public void showProgressIndicator(Boolean showIndicator) {
        if (showIndicator) {
            binding.loadingIndiator.setVisibility(View.VISIBLE);
        } else {
            binding.loadingIndiator.setVisibility(View.GONE);
        }
    }

}