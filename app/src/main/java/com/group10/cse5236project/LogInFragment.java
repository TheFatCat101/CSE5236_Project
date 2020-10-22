package com.group10.cse5236project;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class LogInFragment extends Fragment implements View.OnClickListener {
    private Button logInButton, newAccountButton, exitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance) {
        View v = inflater.inflate(R.layout.fragment_log_in, container, false);

        logInButton = v.findViewById(R.id.log_in_button);
        newAccountButton = v.findViewById(R.id.new_account_button);
        exitButton = v.findViewById(R.id.login_exit_button);
        if (logInButton != null) {
            logInButton.setOnClickListener(this);
        }
        if (newAccountButton != null) {
            newAccountButton.setOnClickListener(this);
        }
        if (exitButton != null) {
            exitButton.setOnClickListener(this);
        }
        return v;
    }

    @Override
    public void onClick(View v) {
        Activity activity = getActivity();
        if (activity != null) {
            switch (v.getId()) {
                case R.id.log_in_button:
                    // To Do
                    break;
                case R.id.new_account_button:
                    FragmentManager fm = getFragmentManager();
                    Fragment fragment = new NewAccountFragment();
                    fm.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("new_account_fragment").commit();
                    break;
                case R.id.login_exit_button:
                    activity.finish();
                    break;
            }
        }
    }

}
