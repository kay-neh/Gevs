package com.example.gevs.ui.auth.registervoter;

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
import com.example.gevs.data.pojo.Voter;
import com.example.gevs.databinding.ActivityVoterRegisterTwoBinding;
import com.example.gevs.ui.user.voter.VoterMainActivity;
import com.example.gevs.util.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VoterRegisterTwoActivity extends AppCompatActivity {

    ActivityVoterRegisterTwoBinding binding;
    BaseRepository baseRepository;
    String fullName, constituency;
    Long dateOfBirth;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_voter_register_two);

        fullName = getIntent().getStringExtra("FullName");
        dateOfBirth = getIntent().getLongExtra("DateOfBirth", 0);
        constituency = getIntent().getStringExtra("Constituency");

        baseRepository = new BaseRepository();
        mAuth = FirebaseAuth.getInstance();

        binding.navImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.registerUvcTextInput.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQrCode();
            }
        });

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCredentials();
            }
        });

    }

    public void scanQrCode() {

    }

    public void validateCredentials() {
        // validate inputs
        String email = binding.registerEmailEditText.getText().toString().trim();
        String password = binding.registerPasswordEditText.getText().toString().trim();
        String uvc = binding.registerUvcEditText.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(uvc)) {
            showProgressIndicator(true);
            // check constraints
            baseRepository.isExistingEmail(email).observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if (!aBoolean) {
                        // check if uvc exists
                        baseRepository.isUvcValid(uvc).observe(VoterRegisterTwoActivity.this, new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                if (aBoolean) {
                                    // check if uvc has been used
                                    baseRepository.isUvcUsed(uvc).observe(VoterRegisterTwoActivity.this, new Observer<Boolean>() {
                                        @Override
                                        public void onChanged(Boolean aBoolean) {
                                            if (!aBoolean) {
                                                // all checks passed
                                                Voter voter = new Voter(email, fullName, constituency, uvc, Constants.ROLE_VOTER, dateOfBirth);
                                                createUser(voter, password);
                                            } else {
                                                showProgressIndicator(false);
                                                Toast.makeText(VoterRegisterTwoActivity.this, "Uvc has been used", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    showProgressIndicator(false);
                                    Toast.makeText(VoterRegisterTwoActivity.this, "Uvc does not exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        showProgressIndicator(false);
                        Toast.makeText(VoterRegisterTwoActivity.this, "Email has been used", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Toast.makeText(VoterRegisterTwoActivity.this, "Please fill in all details", Toast.LENGTH_SHORT).show();
        }

    }

    public void createUser(Voter voter, String password) {
        showProgressIndicator(true);
        mAuth.createUserWithEmailAndPassword(voter.getEmail(), password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        // Save to Database
                        baseRepository.saveVoter(user.getUid(), voter);
                        startActivity(new Intent(VoterRegisterTwoActivity.this, VoterMainActivity.class));
                        finishAffinity();
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    showProgressIndicator(false);
                    Log.w("Failed Sign Up", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(VoterRegisterTwoActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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