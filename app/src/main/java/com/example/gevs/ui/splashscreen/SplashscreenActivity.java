package com.example.gevs.ui.splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.gevs.R;
import com.example.gevs.data.BaseRepository;
import com.example.gevs.data.pojo.Voter;
import com.example.gevs.ui.auth.loginvoter.VoterLoginActivity;
import com.example.gevs.ui.user.admin.AdminMainActivity;
import com.example.gevs.ui.user.voter.VoterMainActivity;
import com.example.gevs.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashscreenActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authListener;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Logic for switching between group of users
                if (firebaseUser != null) {
//                    // testing
//                    Intent i = new Intent(SplashscreenActivity.this, AdminMainActivity.class);
//                    i.putExtra(Constants.KEY_SESSION_TYPE, Constants.SESSION_TYPE_DATA_MANAGER);
//                    startActivity(i);
//                    finish();
                    //determine user category first
                    BaseRepository baseRepository = new BaseRepository();
                    baseRepository.getVoter(firebaseUser.getUid()).observe(SplashscreenActivity.this, new Observer<Voter>() {
                        @Override
                        public void onChanged(Voter voter) {
                            Intent i;
                            if (voter.getRole().equals(Constants.ROLE_ADMIN)) {
                                i = new Intent(SplashscreenActivity.this, AdminMainActivity.class);
                            } else {
                                i = new Intent(SplashscreenActivity.this, VoterMainActivity.class);
                            }
                            startActivity(i);
                            finish();
                            //SplashScreenActivity.this.overridePendingTransition(0, 0);
                        }
                    });
                } else {
                    Intent i = new Intent(SplashscreenActivity.this, VoterLoginActivity.class);
                    startActivity(i);
                    SplashscreenActivity.this.overridePendingTransition(0, 0);
                    finish();
                }
            }
        }, 3000);
    }

}