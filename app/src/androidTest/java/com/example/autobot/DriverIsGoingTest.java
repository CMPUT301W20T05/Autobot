package com.example.autobot;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
/**
 This is the UI test for testing DriverIsGoing
 */
public class DriverIsGoingTest {
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

    }

    // @Test
    public void checkActivityChange(){
        solo.assertCurrentActivity("Wrong Activity", DriveIsGoing.class);
    }

    /// @Test
    public void checkContinueButton() {
        solo.clickOnButton("Cancel Order");
        solo.assertCurrentActivity("Wrong Activity", Driver_ordercomplete.class);
    }


    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
