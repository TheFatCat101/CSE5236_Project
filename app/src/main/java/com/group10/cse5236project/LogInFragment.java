package com.group10.cse5236project;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInFragment extends Fragment implements View.OnClickListener {

    private EditText mUsernameEditText, mPasswordEditText;
    private Button mLogInButton, mNewAccountButton, mExitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance) {
        View v = inflater.inflate(R.layout.fragment_log_in, container, false);

        mUsernameEditText = v.findViewById(R.id.log_in_username_field);
        mPasswordEditText = v.findViewById(R.id.log_in_password_field);

        mLogInButton = v.findViewById(R.id.log_in_button);
        mNewAccountButton = v.findViewById(R.id.new_account_button);
        mExitButton = v.findViewById(R.id.log_in_exit_button);

        if (mLogInButton != null) {
            mLogInButton.setOnClickListener(this);
        }
        if (mNewAccountButton != null) {
            mNewAccountButton.setOnClickListener(this);
        }
        if (mExitButton != null) {
            mExitButton.setOnClickListener(this);
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        Activity activity = getActivity();
        if (activity != null) {
            switch (v.getId()) {
                case R.id.log_in_button:
                    logIn();
                    break;
                case R.id.new_account_button:
                    FragmentManager fm = getFragmentManager();
                    Fragment fragment = new NewAccountFragment();
                    fm.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("new_account_fragment").commit();
                    break;
                case R.id.log_in_exit_button:
                    activity.finish();
                    break;
            }
        }
    }

    private void logIn() {
        final String username = mUsernameEditText.getText().toString().trim();
        final String password = mPasswordEditText.getText().toString().trim();
        if (!username.equals("") && !password.equals("")) {
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Accounts").child(username);
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        if (password.equals(snapshot.getValue().toString())) {
                            Account.getInstance().setUsername(username);
                            Toast.makeText(getActivity(), R.string.successful_log_in_toast, Toast.LENGTH_SHORT).show();
                            FragmentManager fm = getFragmentManager();
                            Fragment fragment = new AccountSettingsFragment();
                            fm.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("account_settings_fragment").commit();
                        } else {
                            Toast.makeText(getActivity(), R.string.incorrect_password_toast, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.username_not_found_toast, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), R.string.log_in_error_toast, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), R.string.no_username_password_toast, Toast.LENGTH_SHORT).show();
        }
    }

    // Below methods are used for testing.
    private void dbTestRead() {
        final String[] password = new String[1];
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Accounts").child("username3");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    password[0] = snapshot.getValue().toString();
                    Toast.makeText(getActivity(), password[0], Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "username DNE", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "login failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dbTestWrite() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("message");
        db.setValue("Hello, World!");
    }

    private void dbTestConnected() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Toast.makeText(getActivity(), "is connected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "is not connected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
