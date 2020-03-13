package com.example.autobot;

import android.widget.EditText;

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
        //This method used to create the solo object with instrumentation and activity as arguments
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        //LoginActivity steps
        solo.enterText((EditText) solo.getView(R.id.editAccount), "111");
        solo.enterText((EditText) solo.getView(R.id.editTextConfirmPassword), "1zZ.");
        solo.clickOnButton("Log in");
        //HomePageActivity steps
        //seaechbar fill in
        solo.clickOnButton("Direction");
        solo.clickOnButton("Confirm Request");
        //CurRequestActivity
        solo.pressSpinnerItem(0,0);
        solo.clickOnButton("Confirm");

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
