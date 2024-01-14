package com.example.gevs.ui.user.voter.notifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.gevs.R;
import com.example.gevs.data.BaseRepository;
import com.example.gevs.data.pojo.Notification;
import com.example.gevs.data.pojo.PushNotification;
import com.example.gevs.data.pojo.VoteCount;
import com.example.gevs.databinding.ActivityVoterNotificationBinding;
import com.example.gevs.ui.user.adapters.NotificationAdapter;
import com.example.gevs.ui.user.adapters.VoteAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VoterNotificationActivity extends AppCompatActivity {

    ActivityVoterNotificationBinding binding;
    BaseRepository baseRepository;
    NotificationAdapter notificationAdapter;
    FirebaseAuth firebaseAuth;

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
            baseRepository.getNotificationsById(firebaseAuth.getCurrentUser().getUid()).observe(this, new Observer<List<Notification>>() {
                @Override
                public void onChanged(List<Notification> notificationList) {
                    if (notificationList != null) {
                        notificationAdapter.setList(notificationList);
                        if (notificationList.size() == 0) {
                            binding.adminNotificationEmptyState.setVisibility(View.VISIBLE);
                        } else {
                            binding.adminNotificationEmptyState.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }

    }

    public void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        binding.voterNotificationRecyclerview.setLayoutManager(llm);
        binding.voterNotificationRecyclerview.setHasFixedSize(true);
        notificationAdapter = new NotificationAdapter();
        binding.voterNotificationRecyclerview.setAdapter(notificationAdapter);
    }

    public List<Notification> convertHashmapToList(HashMap<String, Notification> notificationHashMap) {
        if (notificationHashMap != null) {
            return new ArrayList<>(notificationHashMap.values());
        } else {
            return new ArrayList<>();
        }
    }


}