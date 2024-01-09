package com.example.gevs.ui.user.voter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.gevs.R;
import com.example.gevs.databinding.ActivityVoterMainBinding;
import com.example.gevs.ui.auth.loginvoter.VoterLoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VoterMainActivity extends AppCompatActivity {

    ActivityVoterMainBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_voter_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.voter_nav_host_fragment);
        assert navHostFragment != null;
        NavController controller = navHostFragment.getNavController();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.voterDashboardFragment, R.id.voterVoteFragment, R.id.voterResultFragment, R.id.voterSettingsFragment).build();
        //Toolbar
        NavigationUI.setupWithNavController(binding.voterHomepageToolbar, controller, appBarConfiguration);
        //Bottom NavBar
        NavigationUI.setupWithNavController(binding.voterBottomNavBar, controller);


        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent i = new Intent(VoterMainActivity.this, VoterLoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };

        binding.voterHomepageToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.voter_menu_notification) {
                    // do nothing for now
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(authStateListener);
    }


}