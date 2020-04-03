package com.example.autobot;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
/**
 This is the UI test for testing UCurRequest Page
 */
public class UCurRequestTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setup() throws Exception{
        //This method used to create the solo object with instrumentation and activity as arguments
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        //LoginActivity steps
        solo.enterText((EditText) solo.getView(R.id.editAccount), "111");
        solo.enterText((EditText) solo.getView(R.id.editTextConfirmPassword), "1zZ.");
        solo.clickOnButton("LOG IN");
        //HomePageActivity steps
        //seaechbar fill in
        solo.clickOnButton("Direction");
        solo.clickOnButton("Confirm Request");
        //CurRequestActivity
        solo.pressSpinnerItem(0,0);

    }

   // @Test
    public void checkActivityChange() {
        // Asserts that the current activity is the signupactivity. Otherwise, show wrong message
        solo.assertCurrentActivity("Wrong Activity", UCurRequest.class);
    }

 //   @Test
    public void checkConfirmButton(){
        solo.clickOnButton("Confirm");
        solo.assertCurrentActivity("Wrong Activity", DriverIsOnTheWayActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
