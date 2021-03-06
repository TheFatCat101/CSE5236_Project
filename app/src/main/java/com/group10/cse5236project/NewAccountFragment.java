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

public class NewAccountFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "NewAccountFragment";

    private Button mCreateButton, mCancelButton;
    private EditText mUsernameEditText, mPasswordEditText, mConfirmPasswordEditText;

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
        Log.d(TAG, "OnCreateView() called");
        View v = inflater.inflate(R.layout.fragment_new_account, container, false);

        mCreateButton = v.findViewById(R.id.create_button);
        mCancelButton = v.findViewById(R.id.cancel_button);

        mUsernameEditText = v.findViewById(R.id.username);
        mPasswordEditText = v.findViewById(R.id.password);
        mConfirmPasswordEditText = v.findViewById(R.id.confirm_password);

        if (mCreateButton != null) {
            mCreateButton.setOnClickListener(this);
        }
        if (mCancelButton != null) {
            mCancelButton.setOnClickListener(this);
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
                case R.id.create_button:
                    String username = mUsernameEditText.getText().toString().trim();
                    String password = mPasswordEditText.getText().toString().trim();
                    String passwordConfirm = mConfirmPasswordEditText.getText().toString().trim();
                    if (username.equals("") || password.equals("")) {
                        Toast.makeText(getActivity(), R.string.no_username_password_toast, Toast.LENGTH_SHORT).show();
                        break;
                    }

                    //validate password and confirm_password
                    //strong enough (no req)
                    //password matches confirm_password
                    if(!password.equals(passwordConfirm)){
                        Toast.makeText(getActivity(), R.string.passwords_no_match_toast,Toast.LENGTH_SHORT).show();
                        break;
                    }

                    //validate account username
                    //does not yet exist in db
                    addUserInfo(username, password);
                    break;
                case R.id.cancel_button:
                    activity.getSupportFragmentManager().popBackStack();
                    break;
            }
        }
    }
    //method return false if username already exists
    //return true when username does not exist and has been added by our method below
    private void addUserInfo(String username, final String password) {
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Accounts").child(username);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Toast.makeText(getActivity(), R.string.username_taken_toast, Toast.LENGTH_SHORT).show();
                } else {
                    //push to dbRef
                    //new (username, password) pair add to db
                    dbRef.setValue(password);
                    Toast.makeText(getActivity(), R.string.account_created_toast, Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), R.string.new_account_error_toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
//accilorometer possibly
//step counter and vibration sender API