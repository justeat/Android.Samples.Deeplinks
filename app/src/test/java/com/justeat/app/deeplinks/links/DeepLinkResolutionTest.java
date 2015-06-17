package com.justeat.app.deeplinks.links;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.justeat.app.deeplinks.BuildConfig;
import com.justeat.app.deeplinks.activities.LinkDispatcherActivity;
import com.justeat.app.deeplinks.config.RobolectricRunner;
import com.justeat.app.deeplinks.intents.IntentHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricRunner.class)
public class DeepLinkResolutionTest {

    private IntentHelper mMockIntents;
    private Activity mMockActivity;
    private UriToIntentMapper mMapper;

    @Before
    public void setUp() {
        mMockIntents = mock(IntentHelper.class);
        mMockActivity = mock(LinkDispatcherActivity.class);
        mMapper = new UriToIntentMapper(mMockActivity, mMockIntents);
    }

    @Test
    public void activity_a_link_dispatches() {
        //Arrange
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DeepLinks.getActivityAUri()));
        Intent mockDispatchIntent = mock(Intent.class);
        when(mMockIntents.newAActivityIntent(mMockActivity)).thenReturn(mockDispatchIntent);

        //Act
        mMapper.dispatchIntent(intent);

        //Assert
        verify(mMockActivity).startActivity(eq(mockDispatchIntent));
    }

    @Test
    public void activity_b_link_dispatches() {
        //Arrange
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DeepLinks.getActivityBUri("bogo")));
        Intent mockDispatchIntent = mock(Intent.class);
        when(mMockIntents.newBActivityIntent(eq(mMockActivity), eq("bogo"))).thenReturn(mockDispatchIntent);

        //Act
        mMapper.dispatchIntent(intent);

        //Assert
        verify(mMockIntents).newBActivityIntent(any(Context.class), eq("bogo"));
        verify(mMockActivity).startActivity(eq(mockDispatchIntent));
    }

    @Test
    public void activity_c_link_dispatches() {
        //Arrange
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DeepLinks.getActivityCUri("bogo", 1234)));
        Intent mockDispatchIntent = mock(Intent.class);
        when(mMockIntents.newCActivityIntent(eq(mMockActivity), eq("bogo"), eq(1234))).thenReturn(mockDispatchIntent);

        //Act
        mMapper.dispatchIntent(intent);

        //Assert
        verify(mMockIntents).newCActivityIntent(any(Context.class), eq("bogo"), eq(1234));
        verify(mMockActivity).startActivity(eq(mockDispatchIntent));
    }

    @Test
    public void activity_a_web_link_http_dispatches() {
        //Arrange
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DeepLinks.getActivityAWebUrl(false, false)));
        Intent mockDispatchIntent = mock(Intent.class);
        when(mMockIntents.newAActivityIntent(mMockActivity)).thenReturn(mockDispatchIntent);

        //Act
        mMapper.dispatchIntent(intent);

        //Assert
        verify(mMockActivity).startActivity(eq(mockDispatchIntent));
    }

    @Test
    public void activity_a_web_link_http_www_dispatches() {
        //Arrange
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DeepLinks.getActivityAWebUrl(false, true)));
        Intent mockDispatchIntent = mock(Intent.class);
        when(mMockIntents.newAActivityIntent(mMockActivity)).thenReturn(mockDispatchIntent);

        //Act
        mMapper.dispatchIntent(intent);

        //Assert
        verify(mMockActivity).startActivity(eq(mockDispatchIntent));
    }

    @Test
    public void activity_a_web_link_https_dispatches() {
        //Arrange
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DeepLinks.getActivityAWebUrl(true, false)));
        Intent mockDispatchIntent = mock(Intent.class);
        when(mMockIntents.newAActivityIntent(mMockActivity)).thenReturn(mockDispatchIntent);

        //Act
        mMapper.dispatchIntent(intent);

        //Assert
        verify(mMockActivity).startActivity(eq(mockDispatchIntent));
    }

    @Test
    public void activity_a_web_link_https_www_dispatches() {
        //Arrange
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DeepLinks.getActivityAWebUrl(true, true)));
        Intent mockDispatchIntent = mock(Intent.class);
        when(mMockIntents.newAActivityIntent(mMockActivity)).thenReturn(mockDispatchIntent);

        //Act
        mMapper.dispatchIntent(intent);

        //Assert
        verify(mMockActivity).startActivity(eq(mockDispatchIntent));
    }

    @Test
    public void activity_c_web_link_http_dispatches() {
        //Arrange
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DeepLinks.getActivityCWebUrl(false, false, "bogo", 1234)));
        Intent mockDispatchIntent = mock(Intent.class);
        when(mMockIntents.newCActivityIntent(eq(mMockActivity), eq("bogo"), eq(1234))).thenReturn(mockDispatchIntent);

        //Act
        mMapper.dispatchIntent(intent);

        //Assert
        verify(mMockIntents).newCActivityIntent(any(Context.class), eq("bogo"), eq(1234));
        verify(mMockActivity).startActivity(eq(mockDispatchIntent));
    }

    @Test
    public void activity_c_web_link_http_www_dispatches() {
        //Arrange
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DeepLinks.getActivityCWebUrl(false, true, "bogo", 1234)));
        Intent mockDispatchIntent = mock(Intent.class);
        when(mMockIntents.newCActivityIntent(eq(mMockActivity), eq("bogo"), eq(1234))).thenReturn(mockDispatchIntent);

        //Act
        mMapper.dispatchIntent(intent);

        //Assert
        verify(mMockIntents).newCActivityIntent(any(Context.class), eq("bogo"), eq(1234));
        verify(mMockActivity).startActivity(eq(mockDispatchIntent));
    }

    @Test
    public void activity_c_web_link_https_dispatches() {
        //Arrange
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DeepLinks.getActivityCWebUrl(true, false, "bogo", 1234)));
        Intent mockDispatchIntent = mock(Intent.class);
        when(mMockIntents.newCActivityIntent(eq(mMockActivity), eq("bogo"), eq(1234))).thenReturn(mockDispatchIntent);

        //Act
        mMapper.dispatchIntent(intent);

        //Assert
        verify(mMockIntents).newCActivityIntent(any(Context.class), eq("bogo"), eq(1234));
        verify(mMockActivity).startActivity(eq(mockDispatchIntent));
    }

    @Test
    public void activity_c_web_link_https_www_dispatches() {
        //Arrange
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DeepLinks.getActivityCWebUrl(true, true, "bogo", 1234)));
        Intent mockDispatchIntent = mock(Intent.class);
        when(mMockIntents.newCActivityIntent(eq(mMockActivity), eq("bogo"), eq(1234))).thenReturn(mockDispatchIntent);

        //Act
        mMapper.dispatchIntent(intent);

        //Assert
        verify(mMockIntents).newCActivityIntent(any(Context.class), eq("bogo"), eq(1234));
        verify(mMockActivity).startActivity(eq(mockDispatchIntent));
    }
}
