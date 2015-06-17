package com.justeat.app.deeplinks.links;

public class DeepLinks {

    public static String getActivityAUri() {
        return "example-scheme://activitya";
    }

    public static String getActivityBUri(String query) {
        return String.format("example-scheme://activityb?query=%s", query);
    }

    public static String getActivityCUri(String query, int choice) {
        return String.format("example-scheme://activityc?query=%s&choice=%d", query, choice);
    }

    public static String getActivityAWebUrl(boolean https, boolean www) {
        StringBuilder sb = new StringBuilder(https ? "https" : "http");
        sb.append("://");
        if (www) sb.append("www.");
        sb.append("example.co.uk");
        sb.append("/a");
        return sb.toString();
    }

    public static String getActivityCWebUrl(boolean https, boolean www, String query, int choice) {
        StringBuilder sb = new StringBuilder(https ? "https" : "http");
        sb.append("://");
        if (www) sb.append("www.");
        sb.append("example.co.uk");
        sb.append(String.format("/c?query=%s&choice=%d", query, choice));
        return sb.toString();
    }
}
