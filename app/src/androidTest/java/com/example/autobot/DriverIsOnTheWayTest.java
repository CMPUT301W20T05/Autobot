package com.example.autobot;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class DriverIsOnTheWayTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        solo.clickOnImageButton(0); //backButton
        solo.clickOnImageButton(1);  //EmailButton
        solo.clickOnImageButton(2);  //PhoneButton
    }

    @Test
    public void checkActivityChange(){
        solo.assertCurrentActivity("Wrong Activity", DriverIsOnTheWayActivity.class);
    }

    @Test
    public void checkContinueButton() {
        solo.clickOnButton("Cancel Order");
        solo.assertCurrentActivity("Wrong Activity", TripComplete.class);
    }

    @Test
    public void checkSeeProfileButton(){
        solo.clickOnButton("See profile");
        solo.assertCurrentActivity("Wrong Activity", OrderInfo.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
