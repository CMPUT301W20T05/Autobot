package com.example.autobot;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RateDriverTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        //This method used to create the solo object with instrumentation and activity as arguments
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());

        solo.enterText((EditText) solo.getView(R.id.comment), "Good");
    }

    @Test
    public void checkActivityChange() {
        // Asserts that the current activity is the signupactivity. Otherwise, show wrong message
        solo.assertCurrentActivity("Wrong Activity", RateDriver.class);
    }

    @Test
    public void checkConfirmButton() {
        solo.clickOnButton("Confirm");
        solo.assertCurrentActivity("Wrong Activity", HomePageActivity.class);
    }

    @Test
    public void checkSkipButton() {
        solo.clickOnButton("Skip");
        solo.assertCurrentActivity("Wrong Activity", HomePageActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
