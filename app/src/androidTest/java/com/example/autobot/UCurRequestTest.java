package com.example.autobot;

import android.app.Activity;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class UCurRequestTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setup() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        solo.pressSpinnerItem(0,0);
        solo.enterText((EditText) solo.getView(R.id.editText),"$10");
        solo.clickOnButton(0);
    }

    @Test
    public void checkActivityChange() {
        // Asserts that the current activity is the signupactivity. Otherwise, show wrong message
        solo.assertCurrentActivity("Wrong Activity", UCurRequest.class);
    }

    @Test
    public void checkConfirmButton(){
        solo.clickOnButton("Confirm");
        solo.assertCurrentActivity("Wrong Activity", DriverIsOnTheWayActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}