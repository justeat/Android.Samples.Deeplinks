package com.justeat.app.deeplinks.links;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.justeat.app.deeplinks.intents.IntentHelper;

public class UriToIntentMapper {
    private Context mContext;
    private IntentHelper mIntents;

    public UriToIntentMapper(Context context, IntentHelper intentHelper) {
        mContext = context;
        mIntents = intentHelper;
    }

    public void dispatchIntent(Intent intent) {
        final Uri uri = intent.getData();
        Intent dispatchIntent = null;

        if (uri == null) throw new IllegalArgumentException("Uri cannot be null");

        final String scheme = uri.getScheme().toLowerCase();
        final String host = uri.getHost().toLowerCase();

        if ("example-scheme".equals(scheme)) {
            dispatchIntent = mapAppLink(uri);
        } else if (("http".equals(scheme) || "https".equals(scheme)) &&
                ("www.example.co.uk".equals(host) || "example.co.uk".equals(host))) {
            dispatchIntent = mapWebLink(uri);
        }

        if (dispatchIntent != null) {
            mContext.startActivity(dispatchIntent);
        }
    }

    private Intent mapAppLink(Uri uri) {
        final String host = uri.getHost().toLowerCase();

        switch (host) {
            case "activitya":
                return mIntents.newAActivityIntent(mContext);
            case "activityb":
                String bQuery = uri.getQueryParameter("query");
                return mIntents.newBActivityIntent(mContext, bQuery);
            case "activityc":
                String cQuery = uri.getQueryParameter("query");
                int choice = Integer.parseInt(uri.getQueryParameter("choice"));
                return mIntents.newCActivityIntent(mContext, cQuery, choice);
        }
        return null;
    }

    private Intent mapWebLink(Uri uri) {
        final String path = uri.getPath();

        switch (path) {
            case "/a":
                return mIntents.newAActivityIntent(mContext);
            case "/c":
                String cQuery = uri.getQueryParameter("query");
                int choice = Integer.parseInt(uri.getQueryParameter("choice"));
                return mIntents.newCActivityIntent(mContext, cQuery, choice);
        }
        return null;
    }
}
