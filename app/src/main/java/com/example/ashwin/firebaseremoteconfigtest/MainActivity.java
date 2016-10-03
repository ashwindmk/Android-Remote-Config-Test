package com.example.ashwin.firebaseremoteconfigtest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private TextView mHelloWorldTextView;
    private String mHelloWorldText = "Hello world";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelloWorldTextView = (TextView) findViewById(R.id.HelloWorldTextView);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);

        mHelloWorldText = mFirebaseRemoteConfig.getString("hello_world_text");

        mHelloWorldTextView.setText(mHelloWorldText);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");

        mFirebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if( !task.isSuccessful() ) {
                            Log.i(TAG, "fetch failed");
                        } else {
                            mFirebaseRemoteConfig.activateFetched();
                            mHelloWorldText = mFirebaseRemoteConfig.getString("hello_world_text");
                            mHelloWorldTextView.setText(mHelloWorldText);
                            Log.i(TAG, "fetch success : " + mHelloWorldText);
                        }
                    }
                });

    }
}
