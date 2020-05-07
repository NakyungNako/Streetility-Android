package com.example.streetlity_android.Firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class StreetlityFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "[Firebase]";
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        Log.println(Log.INFO,TAG,"Token: " + token );
    }
}
