package com.justeat.app.deeplinks.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.justeat.app.deeplinks.R;
import com.justeat.app.deeplinks.intents.IntentHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AActivity extends Activity {

    @InjectView(R.id.query) EditText mQueryEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.button_go)
    public void onClick() {
        startActivity(IntentHelper.newBActivityIntent(this, mQueryEditText.getText().toString()));
    }

}
