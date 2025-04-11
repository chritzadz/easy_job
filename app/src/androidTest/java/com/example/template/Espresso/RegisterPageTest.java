package com.example.template.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.template.R;
import com.example.template.view.RegisterActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;

@RunWith(AndroidJUnit4.class)
public class RegisterPageTest {

    @Rule
    public ActivityScenarioRule<RegisterActivity> activityScenarioRule = new ActivityScenarioRule<>(RegisterActivity.class);

    @Test
    public void testEmptyEmail() {
        onView(ViewMatchers.withId(R.id.submit)).perform(click());
        onView(withId(R.id.statusLabelRegister))
                .check(matches(withText("Please enter an email address")));
    }

    @Test
    public void testEmptyPassword() {
        onView(withId(R.id.editTextTextEmailAddress)).perform(typeText("test@example.com"), closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.submit)).perform(click());
        onView(withId(R.id.statusLabelRegister))
                .check(matches(withText("Please enter a password")));
    }

    @Test
    public void testInvalidPassword() {
        onView(withId(R.id.editTextTextEmailAddress)).perform(typeText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.editTextTextPassword)).perform(typeText("pass"), closeSoftKeyboard());

        onView(withId(R.id.submit)).perform(click());

        onView(withId(R.id.statusLabelRegister))
                .check(matches(withText("Password must be at least 8 characters long, contain uppercase, lowercase, digit, and special character")));
    }

    @Test
    public void testInvalidEmail() {
        onView(withId(R.id.editTextTextEmailAddress)).perform(typeText("example.com"), closeSoftKeyboard());
        onView(withId(R.id.submit)).perform(click());
        onView(withId(R.id.statusLabelRegister))
                .check(matches(withText("Enter an email address of the form user@example.com")));
    }
}
