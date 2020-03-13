package com.example.autobot;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class OrderInfoTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        //This method used to create the solo object with instrumentation and activity as arguments
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void checkActivityChange() {
        // Asserts that the current activity is the signupactivity. Otherwise, show wrong message
        solo.assertCurrentActivity("Wrong Activity", OrderInfo.class);
    }

    @Test
    public void checkImageBackButton() {
        solo.clickOnImageButton(2);
        solo.assertCurrentActivity("Wrong Activity", DriverIsOnTheWayActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
