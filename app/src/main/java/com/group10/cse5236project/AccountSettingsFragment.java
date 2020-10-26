package com.group10.cse5236project;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountSettingsFragment extends Fragment implements View.OnClickListener{

    private final String TAG = "AccountSettingsFragment";

    private Button mUpdatePasswordButton, mDeleteAccountButton, mExitButton;
    private EditText mCurrentPasswordEditText, mNewPasswordEditText;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.d(TAG, "onCreateView() called");
        View v = inflater.inflate(R.layout.fragment_account_settings, container, false);

        mUpdatePasswordButton = v.findViewById(R.id.update_password_button);
        mDeleteAccountButton = v.findViewById(R.id.delete_account_button);
        mExitButton = v.findViewById(R.id.account_settings_exit_button);

        mCurrentPasswordEditText = v.findViewById(R.id.current_password_edittext);
        mNewPasswordEditText = v.findViewById(R.id.new_password_edittext);

        if (mUpdatePasswordButton != null) {
            mUpdatePasswordButton.setOnClickListener(this);
        }
        if (mDeleteAccountButton != null) {
            mDeleteAccountButton.setOnClickListener(this);
        }
        if (mExitButton != null) {
            mExitButton.setOnClickListener(this);
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
        if (activity != null) {
            switch (v.getId()) {
                case R.id.update_password_button:
                    updatePassword();
                    break;
                case R.id.delete_account_button:
                    deleteAccount();
                    break;
                case R.id.account_settings_exit_button:
                    activity.getSupportFragmentManager().popBackStack();
                    break;
            }
        }
    }

    private void updatePassword() {
        final String currentPassword = mCurrentPasswordEditText.getText().toString().trim();
        final String newPassword = mNewPasswordEditText.getText().toString().trim();
        if (!currentPassword.equals("") && !newPassword.equals("")) {
            final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Accounts").child(Account.getInstance().getUsername());
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (currentPassword.equals(snapshot.getValue().toString())) {
                        dbRef.setValue(newPassword);
                        Toast.makeText(getActivity(), R.string.password_changed_toast, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), R.string.incorrect_password_toast, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), R.string.password_change_error_toast, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), R.string.no_current_new_password_toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteAccount() {
        final String password = mCurrentPasswordEditText.getText().toString().trim();
        if (!password.equals("")) {
            final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Accounts").child(Account.getInstance().getUsername());
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (password.equals(snapshot.getValue().toString())) {
                        dbRef.removeValue();
                        Toast.makeText(getActivity(), R.string.account_deleted_toast, Toast.LENGTH_SHORT).show();
                        Account.getInstance().clear();
                        getActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getActivity(), R.string.incorrect_password_toast, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), R.string.account_delete_error_toast, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), R.string.no_current_password_toast, Toast.LENGTH_SHORT).show();
        }
    }
}
