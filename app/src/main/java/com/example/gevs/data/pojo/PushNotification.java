package com.example.gevs.data.pojo;

import java.util.HashMap;

public class PushNotification {

    private HashMap<String, Notification> notificationHashmap;

    public PushNotification() {
    }

    public PushNotification(HashMap<String, Notification> notificationHashmap) {
        this.notificationHashmap = notificationHashmap;
    }

    public HashMap<String, Notification> getNotificationHashmap() {
        return notificationHashmap;
    }

    public void setNotificationHashmap(HashMap<String, Notification> notificationHashmap) {
        this.notificationHashmap = notificationHashmap;
    }
}
