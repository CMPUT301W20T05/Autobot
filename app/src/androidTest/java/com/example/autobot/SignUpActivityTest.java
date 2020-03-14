package com.example.autobot;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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
        solo.enterText((EditText) solo.getView(R.id.accountPhoneNumber), "1234567890");
        solo.enterText((EditText) solo.getView(R.id.accountUserName), "TestSignUp");
        solo.clickOnRadioButton(1);
        solo.clickOnCheckBox(0);
    }

    @Test
    public void checkActivityChange() {
        // Asserts that the current activity is the signupactivity. Otherwise, show wrong message
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
    }

    /**
     * Check to see if the back button works or not
     */
    @Test
    public void checkContinueButton() {
        solo.clickOnButton("CONTINUE");
        solo.assertCurrentActivity("Wrong Activity", SetPasswordActivity.class);
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
