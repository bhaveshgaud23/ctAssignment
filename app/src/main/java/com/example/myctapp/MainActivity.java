package com.example.myctapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.clevertap.android.sdk.CleverTapAPI;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private CleverTapAPI cleverTap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cleverTap = CleverTapAPI.getDefaultInstance(getApplicationContext());


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String fcmRegId = task.getResult();
                        cleverTap.pushFcmRegistrationId(fcmRegId, true);
                    } else {
                        Log.e("FCMToken", "Failed to get FCM token: " + task.getException());
                    }
                });


        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG);


        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnTestEvent = findViewById(R.id.btnTestEvent);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();

                // Calling onUserLogin to pass user information to CleverTap
                HashMap<String, Object> profileUpdate = new HashMap<>();
                profileUpdate.put("Name", name);
                profileUpdate.put("Email", email);
                cleverTap.onUserLogin(profileUpdate);

                cleverTap.pushEvent("Product Viewed");
            }
        });

        btnTestEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cleverTap.pushEvent("TEST");
            }
        });
    }
}
