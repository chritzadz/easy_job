package com.example.template.Espresso;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.release;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.template.R;
import com.example.template.util.FirebaseCRUD;
import com.example.template.view.LoginActivity;
import com.example.template.view.SettingsActivity;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Rule;
import org.junit.Test;



public class SettingsPageTest {

    @Rule
    public ActivityScenarioRule<SettingsActivity> activityScenarioRule = new ActivityScenarioRule<>(SettingsActivity.class);

    @Test
    public void move2ResetPasswordActivity(){
        onView(ViewMatchers.withId(R.id.ResetPasswordButton)).perform(click());
        onView(withId(R.id.resetPasswordLabel1)).check(matches(withText("Reset Password")));
    }

    @Test
    public void testLogoutConfirmationDialogAppears(){
        onView(withId(R.id.Logout)).perform(click());
        onView(withText("Confirm Logout")).check(matches(isDisplayed()));
        onView(withText("Are you sure you want to logout?")).check(matches(isDisplayed()));
    }

    @Test
    public void testLogoutRedirectsToLoginActivity() {
        init();
        onView(withId(R.id.Logout)).perform(click());
        onView(withText("Yes")).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
        release();
    }
}
