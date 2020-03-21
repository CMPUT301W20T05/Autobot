package com.example.autobot;

import android.widget.Button;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class  LoginTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setup() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        solo.enterText((EditText) solo.getView(R.id.editAccount), "1");
        solo.enterText((EditText) solo.getView(R.id.editTextInputPassword), "1");
    }

    @Test
    public void checkActivityChange() {
        // Asserts that the current activity is the signupactivity. Otherwise, show wrong message
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }


    @Test
    public void checkLoginButton(){
        solo.clickOnButton("Log in");
        solo.assertCurrentActivity("Wrong Activity", HomePageActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
