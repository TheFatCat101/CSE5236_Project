package com.group10.cse5236project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewAccountFragment extends Fragment implements View.OnClickListener {
    private Button mcreateButton, mcancelButton;
    EditText musername, mpassword, mconfirmPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_account, container, false);

        mcreateButton = v.findViewById(R.id.create_button);
        mcancelButton = v.findViewById(R.id.cancel_button);

        musername = v.findViewById(R.id.username);
        mpassword = v.findViewById(R.id.password);
        mconfirmPassword = v.findViewById(R.id.confirm_password);

        if (mcreateButton != null) {
            mcreateButton.setOnClickListener(this);
        }
        if (mcancelButton != null) {
            mcancelButton.setOnClickListener(this);
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            switch (v.getId()) {
                case R.id.create_button:
                    String username = musername.getText().toString().trim();
                    String password = mpassword.getText().toString().trim();
                    String passwordConfirm = mconfirmPassword.getText().toString().trim();
                    if (username.equals("") || password.equals("")) {
                        Toast.makeText(getActivity(), "Invalid Username & password", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    //validate password and confirm_password
                    //strong enough (no req)
                    //password matches confirm_password
                    if(!password.equals(passwordConfirm)){
                        Toast.makeText(getActivity(), "Passwords do NOT match", Toast.LENGTH_SHORT).show();
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
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        Toast.makeText(getActivity(), "Username already claimed", Toast.LENGTH_SHORT).show();
                    } else {
                        //push to dbRef
                        //new (username, password) pair add to db
                        dbRef.push().setValue(password);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), R.string.new_account_error_toast, Toast.LENGTH_SHORT).show();
                }
            });
    }
}
