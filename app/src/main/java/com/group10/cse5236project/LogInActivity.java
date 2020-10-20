package com.group10.cse5236project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class LogInActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new LogInFragment();
    }

}