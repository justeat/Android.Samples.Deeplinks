package com.justeat.app.deeplinks.links;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.PatternMatcher;
import android.text.TextUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.ActivityData;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.manifest.IntentFilterData;
import org.robolectric.res.builder.RobolectricPackageManager;
import org.robolectric.shadows.ShadowApplication;

import com.justeat.app.deeplinks.BuildConfig;
import com.justeat.app.deeplinks.activities.LinkDispatcherActivity;
import com.justeat.app.deeplinks.config.RobolectricRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertThat;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricRunner.class)
public class IntentFilterTest {
    @Test
    public void activity_a_app_link_resolves() {
        assertIntentResolves(DeepLinks.getActivityAUri());
    }

    @Test
    public void activity_b_app_link_resolves() {
        assertIntentResolves(DeepLinks.getActivityBUri("bogoQuery"));
    }

    @Test
    public void activity_c_app_link_resolves() {
        assertIntentResolves(DeepLinks.getActivityCUri("bogoQuery", 3));
    }

    @Test
    public void activity_a_web_link_resolves_http() {
        assertIntentResolves(DeepLinks.getActivityAWebUrl(false, false));
    }

    @Test
    public void activity_a_web_link_resolves_http_www() {
        assertIntentResolves(DeepLinks.getActivityAWebUrl(false, true));
    }

    @Test
    public void activity_a_web_link_resolves_https() {
        assertIntentResolves(DeepLinks.getActivityAWebUrl(true, false));
    }

    @Test
    public void activity_a_web_link_resolves_https_www() {
        assertIntentResolves(DeepLinks.getActivityAWebUrl(true, true));
    }

    @Test
    public void activity_c_web_link_resolves_http() {
        assertIntentResolves(DeepLinks.getActivityCWebUrl(false, false, "bogoQuery", 3));
    }

    @Test
    public void activity_c_web_link_resolves_http_www() {
        assertIntentResolves(DeepLinks.getActivityCWebUrl(false, true, "bogoQuery", 3));
    }

    @Test
    public void activity_c_web_link_resolves_https() {
        assertIntentResolves(DeepLinks.getActivityCWebUrl(true, false, "bogoQuery", 3));
    }

    @Test
    public void activity_c_web_link_resolves_https_www() {
        assertIntentResolves(DeepLinks.getActivityCWebUrl(true, true, "bogoQuery", 3));
    }


    private void assertIntentResolves(String uriString) {
        //Arrange
        ShadowApplication.getInstance().getAppManifest();
        RobolectricPackageManager packageManager = (RobolectricPackageManager) RuntimeEnvironment.application.getPackageManager();
        packageManager.setQueryIntentImplicitly(true);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));

        //Act
        List<ResolveInfo> resolveInfo = queryImplicitIntent(intent, -1);
        ArrayList<String> activityNames = new ArrayList<>();
        for (ResolveInfo info : resolveInfo) {
            activityNames.add(info.activityInfo.targetActivity);
        }

        //Assert
        assertThat(LinkDispatcherActivity.class.getName(), isOneOf(activityNames.toArray()));
    }

    private List<ResolveInfo> queryImplicitIntent(Intent intent, int flags) {
        List<ResolveInfo> resolveInfoList = new ArrayList<ResolveInfo>();
        AndroidManifest appManifest = ShadowApplication.getInstance().getAppManifest();
        String packageName = appManifest.getPackageName();

        for (Map.Entry<String, ActivityData> activity : appManifest.getActivityDatas().entrySet()) {
            String activityName = activity.getKey();
            ActivityData activityData = activity.getValue();
            if (activityData.getTargetActivity() != null) {
                activityName = activityData.getTargetActivityName();
            }

            if (matchIntentFilter(activityData, intent)) {
                ResolveInfo resolveInfo = new ResolveInfo();
                resolveInfo.resolvePackageName = packageName;
                resolveInfo.activityInfo = new ActivityInfo();
                resolveInfo.activityInfo.targetActivity = activityName;

                resolveInfoList.add(resolveInfo);
            }
        }

        return resolveInfoList;
    }

    private boolean matchIntentFilter(ActivityData activityData, Intent intent) {
        System.out.println("Searching for "+ intent.getDataString());
        for (IntentFilterData intentFilterData : activityData.getIntentFilters()) {
            List<String> actionList = intentFilterData.getActions();
            List<String> categoryList = intentFilterData.getCategories();
            IntentFilter intentFilter = new IntentFilter();

            for (String action : actionList) {
                intentFilter.addAction(action);
            }

            for (String category : categoryList) {
                intentFilter.addCategory(category);
            }

            for (String scheme : intentFilterData.getSchemes()) {
                intentFilter.addDataScheme(scheme);
            }

            for (String mimeType : intentFilterData.getMimeTypes()) {
                try {
                    intentFilter.addDataType(mimeType);
                } catch (IntentFilter.MalformedMimeTypeException ex) {
                    throw new RuntimeException(ex);
                }
            }

            for (String path : intentFilterData.getPaths()) {
                intentFilter.addDataPath(path, PatternMatcher.PATTERN_LITERAL);
            }

            for (String pathPattern : intentFilterData.getPathPatterns()) {
                intentFilter.addDataPath(pathPattern, PatternMatcher.PATTERN_SIMPLE_GLOB);
            }

            for (String pathPrefix : intentFilterData.getPathPrefixes()) {
                intentFilter.addDataPath(pathPrefix, PatternMatcher.PATTERN_PREFIX);
            }

            for (IntentFilterData.DataAuthority authority : intentFilterData.getAuthorities()) {
                intentFilter.addDataAuthority(authority.getHost(), authority.getPort());
            }

            // match action
            boolean matchActionResult = intentFilter.matchAction(intent.getAction());
            // match category
            String matchCategoriesResult = intentFilter.matchCategories(intent.getCategories());
            // match data
            int matchResult = intentFilter.matchData(intent.getType(), intent.getScheme(),
                    intent.getData());

            if ((matchResult != IntentFilter.NO_MATCH_DATA && matchResult != IntentFilter.NO_MATCH_TYPE)) {
                System.out.println("Matcher result for " + intent.getType() + " " + intent.getScheme() + " " +
                        intent.getDataString() +": " + Integer.toHexString(matchResult));
                System.out.println(intentFilterData.getSchemes());
                for (IntentFilterData.DataAuthority authority : intentFilterData.getAuthorities()) {
                    System.out.println(authority.getHost() + ":" + authority.getPort());
                }
                System.out.println(intentFilterData.getPaths());
                System.out.println(intentFilterData.getPathPatterns());
                System.out.println(intentFilterData.getPathPrefixes());
                boolean pathMatchesExactly = false;
                for (int i = 0; i < intentFilter.countDataPaths(); i++) {
                    PatternMatcher pm = intentFilter.getDataPath(i);
                    if (pm.match(intent.getData().getPath())) {
                        System.out.println("Pattern match on path: " + pm.getPath());
                    }
                }
            }

            // Check if it's a match at all
            boolean result = matchActionResult && (matchCategoriesResult == null) &&
                    (matchResult != IntentFilter.NO_MATCH_DATA && matchResult != IntentFilter.NO_MATCH_TYPE);

            // No match, discard activity
            if (!result) continue;

            // We have a partial match. The matchResult doesn't seem to correspond to reality
            // as far as path matching goes - no idea why. Fortunately we can check if the path
            // matches exactly manually.
            if (!TextUtils.isEmpty(intent.getData().getPath())) {
                //If the path is not empty, we need to make sure it's an exact match

                boolean pathMatch = false;
                for (int i = 0; i < intentFilter.countDataPaths(); i++) {
                    PatternMatcher pm = intentFilter.getDataPath(i);
                    if (pm.match(intent.getData().getPath())) {
                        // Exact match found, return
                        pathMatch = true;
                    }
                }
                // No exact match found - we only have a partial match on the intent filter.
                // While this filter may work for general cases,
                // for links like "android-app://com.justeat.app.uk/just-eat.co.uk/account"
                // to work, the path must be an exact match. Hence we enforce this for all intents
                // This way we can catch all errors.
                result = pathMatch;
            }
            if (result) return true;
        }
        return false;
    }
}