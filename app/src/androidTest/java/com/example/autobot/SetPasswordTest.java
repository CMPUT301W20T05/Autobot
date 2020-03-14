package com.example.autobot;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SetPasswordTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        //This method used to create the solo object with instrumentation and activity as arguments
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        //LoginActivity Step
        solo.clickOnText("No account?");
        //SignUpActivity Steps
        solo.enterText((EditText) solo.getView(R.id.accountPhoneNumber), "1234567890");
        solo.enterText((EditText) solo.getView(R.id.accountUserName), "WoZuiShuai");
        solo.clickOnRadioButton(1);
        solo.clickOnCheckBox(0);
        solo.clickOnView(solo.getView(R.id.ContinueButton));
        //SetPasswordTest
        solo.enterText((EditText) solo.getView(R.id.editTextSetPassword), "2zZ.");
        solo.enterText((EditText) solo.getView(R.id.editTextConfirmPassword), "2zZ.");
    }

    @Test
    public void checkActivityChange() {
        // Asserts that the current activity is the SetPassword. Otherwise, show wrong message
        solo.assertCurrentActivity("Wrong Activity", SetPasswordActivity.class);
    }

    @Test
    public void checkConfirmButton(){
        solo.clickOnButton("Confirm");
        solo.assertCurrentActivity("Wrong Activity", HomePageActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
