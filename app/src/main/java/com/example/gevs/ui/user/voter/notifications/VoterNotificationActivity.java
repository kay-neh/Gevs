package com.example.gevs.ui.user.voter.notifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.gevs.R;
import com.example.gevs.data.BaseRepository;
import com.example.gevs.data.pojo.Notification;
import com.example.gevs.databinding.ActivityVoterNotificationBinding;
import com.example.gevs.ui.user.adapters.NotificationAdapter;
import com.example.gevs.ui.user.voter.VoterMainActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VoterNotificationActivity extends AppCompatActivity {

    ActivityVoterNotificationBinding binding;
    BaseRepository baseRepository;
    NotificationAdapter notificationAdapter;
    FirebaseAuth firebaseAuth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_voter_notification);

        baseRepository = new BaseRepository();
        firebaseAuth = FirebaseAuth.getInstance();
        initAdapter();
        binding.voterNotificationToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (firebaseAuth.getCurrentUser() != null) {
            userId = firebaseAuth.getCurrentUser().getUid();
            baseRepository.getNotificationsById(userId).observe(this, new Observer<List<Notification>>() {
                @Override
                public void onChanged(List<Notification> notificationList) {
                    if (notificationList != null) {
                        notificationAdapter.setList(notificationList);
                        if (notificationList.size() == 0) {
                            binding.adminNotificationEmptyState.setVisibility(View.VISIBLE);
                            binding.voterNotificationToolbar.getMenu().clear();
                        } else {
                            binding.adminNotificationEmptyState.setVisibility(View.GONE);
                            binding.voterNotificationToolbar.inflateMenu(R.menu.menu_voter_notification);
                        }
                    }
                }
            });
        }

        binding.voterNotificationToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.notification_clear) {
                    // do nothing for now
                    showNotificationClearDialog();
                }
                return false;
            }
        });

    }

    public void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        binding.voterNotificationRecyclerview.setLayoutManager(llm);
        binding.voterNotificationRecyclerview.setHasFixedSize(true);
        notificationAdapter = new NotificationAdapter();
        binding.voterNotificationRecyclerview.setAdapter(notificationAdapter);
    }

    public void showNotificationClearDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Clear all notifications?");
        builder.setMessage("You are about to clear all notifications!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                baseRepository.clearAllNotifications(userId);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }


}