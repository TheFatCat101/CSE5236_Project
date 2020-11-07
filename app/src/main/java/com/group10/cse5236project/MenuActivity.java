package com.group10.cse5236project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

public class MenuActivity extends SingleFragmentActivity {

    private final String TAG = "MenuActivity";

    @Override
    protected Fragment createFragment() {
        return new MainMenuFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate() called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "OnStart() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "OnStop() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "OnPause() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "OnDestroy() called");
    }

}