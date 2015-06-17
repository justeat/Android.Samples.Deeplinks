package com.justeat.app.deeplinks.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.justeat.app.deeplinks.R;
import com.justeat.app.deeplinks.intents.IntentHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CActivity extends Activity {

    @InjectView(R.id.query_label) TextView mQueryTextView;
    @InjectView(R.id.choice_label) TextView mChoiceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);
        ButterKnife.inject(this);

        parseIntent();
    }

    private void parseIntent() {
        Intent intent = getIntent();
        mQueryTextView.setText(intent.getStringExtra(IntentHelper.EXTRA_C_QUERY));
        mChoiceTextView.setText(Integer.toString(intent.getIntExtra(IntentHelper.EXTRA_C_CHOICE, 0)));
    }

}
