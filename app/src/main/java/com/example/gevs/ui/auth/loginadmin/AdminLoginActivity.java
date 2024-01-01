package com.example.gevs.ui.auth.loginadmin;

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
import com.example.gevs.databinding.ActivityAdminLoginBinding;
import com.example.gevs.ui.auth.loginvoter.VoterLoginActivity;
import com.example.gevs.ui.user.admin.AdminMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLoginActivity extends AppCompatActivity {

    ActivityAdminLoginBinding binding;
    BaseRepository baseRepository;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_login);

        baseRepository = new BaseRepository();
        mAuth = FirebaseAuth.getInstance();

        binding.loginUserText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminLoginActivity.this, VoterLoginActivity.class));
                finish();
            }
        });

        binding.loginAdminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCredentials();
            }
        });

    }

    public void validateCredentials() {
        String email = binding.loginAdminEmailEditText.getText().toString().trim();
        String password = binding.loginAdminPasswordEditText.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            signInAdmin(email, password);
        } else {
            Toast.makeText(AdminLoginActivity.this, "Please fill in all details", Toast.LENGTH_SHORT).show();
        }
    }

    public void signInAdmin(String email, String password) {
        showProgressIndicator(true);
        baseRepository.isAdmin(email).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    signInAdminEmailAndPassword(email, password);
                } else {
                    showProgressIndicator(false);
                    Toast.makeText(AdminLoginActivity.this, "Account is not an Admin", Toast.LENGTH_SHORT).show();
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

    public void signInAdminEmailAndPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //User has logged in
                            if (mAuth.getCurrentUser() != null) {
                                startActivity(new Intent(AdminLoginActivity.this, AdminMainActivity.class));
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            showProgressIndicator(false);
                            Log.w("SIGN IN", "signInWithEmail:failure", task.getException());
                            Toast.makeText(AdminLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


}