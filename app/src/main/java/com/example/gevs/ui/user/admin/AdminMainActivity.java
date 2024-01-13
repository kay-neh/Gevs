package com.example.gevs.ui.user.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.UiModeManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.gevs.R;
import com.example.gevs.data.BaseRepository;
import com.example.gevs.databinding.ActivityAdminMainBinding;
import com.example.gevs.ui.auth.loginvoter.VoterLoginActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminMainActivity extends AppCompatActivity {

    ActivityAdminMainBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    BaseRepository baseRepository;

    int themeMode;
    SharedPreferences preferences;
    int checkedItemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_main);

        // get defPreference and themeMode
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        themeMode = preferences.getInt("ThemeMode", AppCompatDelegate.MODE_NIGHT_NO);
        applyThemeMode(themeMode);

        baseRepository = new BaseRepository();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        NavController controller = navHostFragment.getNavController();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.adminDashboardFragment, R.id.adminVotersFragment, R.id.adminCandidatesFragment, R.id.adminResultFragment).build();
        //Toolbar
        NavigationUI.setupWithNavController(binding.adminHomepageToolbar, controller, appBarConfiguration);
        //Bottom NavBar
        NavigationUI.setupWithNavController(binding.adminBottomNavBar, controller);


        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent i = new Intent(AdminMainActivity.this, VoterLoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };

        binding.adminHomepageToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.home_about) {
                    showAboutDialog();
                }
                if (id == R.id.home_dark_mode) {
                    showThemeSelectionDialog();
                }
                if (id == R.id.home_log_out) {
                    showLogOutDialog();
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

    public void showLogOutDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Log out?");
        builder.setMessage("You are about to sign out");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mAuth.signOut();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //
            }
        });
        builder.show();
    }

    public void showAboutDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("About the app");
        builder.setMessage(R.string.lorem_ipsum);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    public void showThemeSelectionDialog() {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(AdminMainActivity.this)
                .setTitle("Choose Theme")
                .setSingleChoiceItems(R.array.theme_mode, getCheckedItemPosition(), null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ListView lw = ((androidx.appcompat.app.AlertDialog) dialogInterface).getListView();
                        int checkedItem = lw.getCheckedItemPosition();

                        switch (checkedItem) {
                            case 0:
                                themeMode = AppCompatDelegate.MODE_NIGHT_YES;
                                break;
                            case 1:
                                themeMode = AppCompatDelegate.MODE_NIGHT_NO;
                        }

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("ThemeMode", themeMode);
                        editor.apply();

                        applyThemeMode(themeMode);
                    }
                });

        materialAlertDialogBuilder.create().show();
    }

    protected void applyThemeMode(int themeMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            UiModeManager manager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
            manager.setApplicationNightMode(themeMode);
        } else {
            AppCompatDelegate.setDefaultNightMode(themeMode);
        }
    }

    protected int getCheckedItemPosition() {
        if (themeMode == AppCompatDelegate.MODE_NIGHT_YES) {
            checkedItemPosition = 0;
        } else if (themeMode == AppCompatDelegate.MODE_NIGHT_NO) {
            checkedItemPosition = 1;
        }
        return checkedItemPosition;
    }

}