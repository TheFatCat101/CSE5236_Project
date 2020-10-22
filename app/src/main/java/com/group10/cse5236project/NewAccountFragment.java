package com.group10.cse5236project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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
                    //validate account username
                        //does not yet exist in db

                    //validate password and confirm_password
                        //strong enough
                        //password matches confirm_password

                    break;
                case R.id.cancel_button:
                    activity.getSupportFragmentManager().popBackStack();
                    break;
            }
        }
    }

}
