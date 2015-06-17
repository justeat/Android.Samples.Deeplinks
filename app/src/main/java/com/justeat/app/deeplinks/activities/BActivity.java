package com.justeat.app.deeplinks.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.justeat.app.deeplinks.R;
import com.justeat.app.deeplinks.intents.IntentHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class BActivity extends Activity {

    @InjectView(R.id.query_label) TextView mQueryText;
    private IntentHelper mIntents = new IntentHelper();

    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

        ButterKnife.inject(this);

        // Parse intent arguments
        parseIntent();

        // Do something with the arguments received
        mQueryText.setText(mQuery);
    }

    private void parseIntent() {
        final Intent intent = getIntent();
        final Uri uri = intent.getData();
        if (uri != null) {
            if ("example-scheme".equals(uri.getScheme()) && "b".equals(uri.getHost())) {
                // Cool, we have a URI addressed to this activity!
                mQuery = uri.getQueryParameter("query");
            }
        }

        if (mQuery == null) {
            mQuery = intent.getStringExtra(IntentHelper.EXTRA_B_QUERY);
        }

    }

    @OnClick({R.id.button_choice1, R.id.button_choice2})
    public void onClick(View view) {
        int choice;
        if (view.getId() == R.id.button_choice1) {
            choice = 1;
        } else {
            choice = 2;
        }

        startActivity(mIntents.newCActivityIntent(this, mQuery, choice));
    }
}
