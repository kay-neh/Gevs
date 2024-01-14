package com.example.gevs.util;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FCMSender {

    /*
     * URL where we request to send notification and the key to send notification using admin sdk
     * */
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send",
            KEY_STRING = "key=AAAA6Gw5Oco:APA91bF0FApmyU-74xgj4oqZcBqXcecHg1Z5mn0wjACh_SJOPVwXyiJhzuu0AfgbIZfu20HCysQp5VxV9URf5pBPmqLdTzA5R1-hEzn7DTscJpabqozxPC0Na7X4cPR4XU7G_T-GJ4Bo";

    OkHttpClient client = new OkHttpClient();

    /*
     * Method to send notification to the application
     * */
    public void send(String message, Callback callback) {
        RequestBody reqBody = RequestBody.create(message
                , MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(FCM_URL)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", KEY_STRING)
                .post(reqBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

}
