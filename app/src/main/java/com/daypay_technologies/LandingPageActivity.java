package com.daypay_technologies;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

public class LandingPageActivity extends BaseAppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_landing_page);
        fragmentManager = getSupportFragmentManager();
    }
}
