package com.example.gevs.ui.splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;

import android.app.UiModeManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

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

    int themeMode;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        // get defPreference and themeMode
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        themeMode = preferences.getInt("ThemeMode", AppCompatDelegate.MODE_NIGHT_NO);
        applyThemeMode(themeMode);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Logic for switching between group of users
                if (firebaseUser != null) {
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

    protected void applyThemeMode(int themeMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            UiModeManager manager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
            manager.setApplicationNightMode(themeMode);
        } else {
            AppCompatDelegate.setDefaultNightMode(themeMode);
        }
    }

}