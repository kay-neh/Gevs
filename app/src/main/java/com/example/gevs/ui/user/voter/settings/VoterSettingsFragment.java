package com.example.gevs.ui.user.voter.settings;

import static android.content.Context.UI_MODE_SERVICE;

import android.app.UiModeManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.example.gevs.R;
import com.example.gevs.data.BaseRepository;
import com.example.gevs.data.pojo.Candidate;
import com.example.gevs.data.pojo.Voter;
import com.example.gevs.databinding.FragmentVoterSettingsBinding;
import com.example.gevs.ui.user.admin.AdminMainActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class VoterSettingsFragment extends Fragment {

    FragmentVoterSettingsBinding binding;
    private FirebaseAuth mAuth;
    BaseRepository baseRepository;

    int themeMode;
    SharedPreferences preferences;

    public VoterSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voter_settings, container, false);

        // get defPreference and themeMode
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        themeMode = preferences.getInt("ThemeMode", AppCompatDelegate.MODE_NIGHT_NO);
        setDarkModeSummary(themeMode);
        applyThemeMode(themeMode);

        mAuth = FirebaseAuth.getInstance();
        baseRepository = new BaseRepository();

        if (mAuth.getCurrentUser() != null) {
            baseRepository.getVoter(mAuth.getCurrentUser().getUid()).observe(getViewLifecycleOwner(), new Observer<Voter>() {
                @Override
                public void onChanged(Voter voter) {
                    if (voter != null) {
                        binding.voterName.setText(voter.getFullName());
                        binding.voterConstituency.setText(voter.getConstituency() + " Constituency");
                    }
                }
            });
        }

        binding.nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setTheme(isChecked);
            }
        });

        binding.aboutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialog();
            }
        });

        binding.logOutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogOutDialog();
            }
        });


        return binding.getRoot();
    }

    public void showLogOutDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
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
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
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

    public void setTheme(Boolean isChecked) {
        if (isChecked) {
            themeMode = AppCompatDelegate.MODE_NIGHT_YES;
        } else {
            themeMode = AppCompatDelegate.MODE_NIGHT_NO;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("ThemeMode", themeMode);
        editor.apply();

        applyThemeMode(themeMode);
    }

    protected void applyThemeMode(int themeMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            UiModeManager manager = (UiModeManager) getActivity().getSystemService(UI_MODE_SERVICE);
            manager.setApplicationNightMode(themeMode);
        } else {
            AppCompatDelegate.setDefaultNightMode(themeMode);
        }
    }

    protected void setDarkModeSummary(int themeMode) {
        if (themeMode == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.darkmodeSummary.setText("On");
            binding.nightModeSwitch.setChecked(true);
        } else if (themeMode == AppCompatDelegate.MODE_NIGHT_NO) {
            binding.darkmodeSummary.setText("Off");
            binding.nightModeSwitch.setChecked(false);
        }
    }

}