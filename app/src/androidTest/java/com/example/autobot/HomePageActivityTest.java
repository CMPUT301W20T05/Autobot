package com.example.autobot;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HomePageActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
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
    }
    @Test
    public void checkActivityChange() {
        // Asserts that the current activity is the HomePage. Otherwise, show wrong message
        solo.assertCurrentActivity("Wrong Activity", HomePageActivity.class);
    }

    @Test
    public void ConfirmRequestButton(){
        solo.clickOnButton("Confirm Request");
        solo.assertCurrentActivity("Wrong Activity", UCurRequest.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
