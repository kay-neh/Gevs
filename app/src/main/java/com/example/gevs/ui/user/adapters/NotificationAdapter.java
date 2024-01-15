package com.example.gevs.ui.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gevs.R;
import com.example.gevs.data.pojo.Notification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    List<Notification> notificationList;

    public void setList(List<Notification> notificationList) {
        this.notificationList = notificationList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_notifications, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        if (notificationList != null) {
            Notification notification = notificationList.get(position);
            holder.notificationTitle.setText(notification.getNotificationTitle());
            holder.notificationMessage.setText(notification.getNotificationMessage());

            DateFormat df = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            String notificationTime = df.format(notification.getNotificationTime());
            holder.notificationTime.setText(notificationTime);
        }
    }

    @Override
    public int getItemCount() {
        if (notificationList != null) {
            return notificationList.size();
        } else {
            return 0;
        }
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView notificationTitle, notificationMessage, notificationTime;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationTitle = itemView.findViewById(R.id.notification_title);
            notificationMessage = itemView.findViewById(R.id.notification_message);
            notificationTime = itemView.findViewById(R.id.notification_time);
        }
    }
}
