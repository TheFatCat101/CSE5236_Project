package com.group10.cse5236project;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class MainMenuFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "MainMenuFragment";

    private Button mJoinOrCreateButton, mAccountSettingsButton, mLogOutButton;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "OnAttach() called");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate() called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() called");
        View v = inflater.inflate(R.layout.fragment_main_menu, container, false);

        mJoinOrCreateButton = v.findViewById(R.id.join_create_button);
        mAccountSettingsButton = v.findViewById(R.id.account_settings_button);
        mLogOutButton = v.findViewById(R.id.log_out_button);

        if (mJoinOrCreateButton != null) {
            mJoinOrCreateButton.setOnClickListener(this);
        }
        if (mAccountSettingsButton != null) {
            mAccountSettingsButton.setOnClickListener(this);
        }
        if (mLogOutButton != null) {
            mLogOutButton.setOnClickListener(this);
        }

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "OnActivityCreated() called");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "OnStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "OnPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "OnStop() called");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "OnDestroyView() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "OnDestroy() called");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "OnDetach() called");
    }

    @Override
    public void onClick(View v) {
        FragmentActivity activity = getActivity();
        FragmentManager fm = getFragmentManager();
        if (activity != null) {
            switch (v.getId()) {
                case R.id.join_create_button:
                    // To Do
                    break;
                case R.id.account_settings_button:
                    Fragment fragment = new AccountSettingsFragment();
                    fm.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("account_settings_fragment").commit();
                    break;
                case R.id.log_out_button:
                    Account.getInstance().clear();
                    activity.finish();
                    break;
            }
        }
    }

}
