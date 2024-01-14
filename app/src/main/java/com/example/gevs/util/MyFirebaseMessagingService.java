package com.example.gevs.util;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.example.gevs.R;
import com.example.gevs.data.BaseRepository;
import com.example.gevs.data.pojo.Notification;
import com.example.gevs.data.pojo.Voter;
import com.example.gevs.ui.user.voter.VoterMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    // Notification ID.
    private final int NOTIFICATION_ID = 0;
    private final int REQUEST_CODE = 0;

    BaseRepository baseRepository;
    FirebaseAuth firebaseAuth;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        baseRepository = new BaseRepository();
        firebaseAuth = FirebaseAuth.getInstance();

        Log.e("Called", "yes");
        showNotification(message);
        if (firebaseAuth.getCurrentUser() != null) {
            Notification notification = new Notification(message.getData().get("title"), message.getData().get("message"), message.getSentTime());
            baseRepository.saveNotification(firebaseAuth.getCurrentUser().getUid(), notification);
        }

    }

    /*
     * Method to show notification when received
     * */
    private void showNotification(RemoteMessage remoteMessage) {

        Intent contentIntent = new Intent(getApplicationContext(), VoterMainActivity.class);
        contentIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentPendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            contentPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    REQUEST_CODE, contentIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        } else {
            contentPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    REQUEST_CODE, contentIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.outline_how_to_vote_24)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("message"))
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}
