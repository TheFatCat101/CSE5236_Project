package com.group10.cse5236project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class NewAccountFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_account, container, false);

        Button createButton = v.findViewById(R.id.create_button);
        Button cancelButton = v.findViewById(R.id.cancel_button);
        if (createButton != null) {
            createButton.setOnClickListener(this);
        }
        if (cancelButton != null) {
            cancelButton.setOnClickListener(this);
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            switch (v.getId()) {
                case R.id.create_button:
                    break;
                case R.id.cancel_button:
                    activity.getSupportFragmentManager().popBackStack();
                    break;
            }
        }
    }

}
