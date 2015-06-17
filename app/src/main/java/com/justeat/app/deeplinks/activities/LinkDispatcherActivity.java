package com.justeat.app.deeplinks.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.justeat.app.deeplinks.BuildConfig;
import com.justeat.app.deeplinks.intents.IntentHelper;
import com.justeat.app.deeplinks.links.UriToIntentMapper;

public class LinkDispatcherActivity extends Activity {
    private final UriToIntentMapper mMapper = new UriToIntentMapper(this, new IntentHelper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mMapper.dispatchIntent(getIntent());

        } catch (IllegalArgumentException iae) {
            // Malformed URL
            if (BuildConfig.DEBUG) {
                Log.e("Deep links", "Invalid URI", iae);
            }
        } finally {
            // Always finish the Activity so that it doesn't stay in our history
            finish();
        }
    }
}
