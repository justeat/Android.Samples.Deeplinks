package com.justeat.app.deeplinks.intents;

import android.content.Context;
import android.content.Intent;

import com.justeat.app.deeplinks.activities.AActivity;
import com.justeat.app.deeplinks.activities.BActivity;
import com.justeat.app.deeplinks.activities.CActivity;

public class IntentHelper {

    public static String EXTRA_B_QUERY = "com.justeat.app.deeplinks.intents.Intents.EXTRA_B_QUERY";
    public static String EXTRA_C_QUERY = "com.justeat.app.deeplinks.intents.Intents.EXTRA_C_QUERY";
    public static String EXTRA_C_CHOICE = "com.justeat.app.deeplinks.intents.Intents.EXTRA_C_CHOICE";

    public Intent newAActivityIntent(Context context) {
        Intent i = new Intent(context, AActivity.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return i;
    }

    public Intent newBActivityIntent(Context context, String query) {
        Intent i = new Intent(context, BActivity.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.putExtra(EXTRA_B_QUERY, query);
        return i;
    }

    public Intent newCActivityIntent(Context context, String query, int choice) {
        Intent i = new Intent(context, CActivity.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.putExtra(EXTRA_C_QUERY, query);
        i.putExtra(EXTRA_C_CHOICE, choice);
        return i;
    }
}
