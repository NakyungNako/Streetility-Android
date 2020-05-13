package com.example.streetlity_android.Firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public final class FirebaseManagement {
    public static String Token;
    private static String TAG = "[Firebase]";
    /**
     * RequestToken send a request to create a Registration Token.
     * The token will be stored in Token field.
     *
     */
    public static void RequestToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        Token = task.getResult().getToken();
                        Log.println(Log.INFO, TAG, Token);
                    }
                });
    }
}
