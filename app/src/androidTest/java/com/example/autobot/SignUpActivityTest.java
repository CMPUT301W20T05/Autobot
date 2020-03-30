package com.example.autobot;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SignUpActivityTest {
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
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        //LoginActivity Step
        solo.clickOnText("No account?");

        //SignUpActivity Steps
        solo.enterText((EditText) solo.getView(R.id.accountPhoneNumber), "123456");
        solo.enterText((EditText) solo.getView(R.id.accountUserName), "TestSignUp");
        solo.clickOnRadioButton(1);
        solo.clickOnCheckBox(0);
    }

    /**
     * Check to see if the back button works or not
     */
    @Test
    public void checkButton() {
        solo.clickOnButton("Continue");
        solo.clickOnButton("Continue");
        //solo.clickOnView(solo.getView(R.id.ContinueButton));
        //solo.clickOnView(solo.getView(R.id.ContinueButton));
        solo.assertCurrentActivity("Wrong Activity", SetPasswordActivity.class);
    }

    @Test
    public void checkActivityChange() {
        // Asserts that the current activity is the signupactivity. Otherwise, show wrong message
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
