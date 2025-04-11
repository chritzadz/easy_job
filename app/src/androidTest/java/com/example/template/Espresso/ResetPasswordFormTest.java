package com.example.template.Espresso;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.mockito.Mockito.mock;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.template.R;
import com.example.template.util.FirebaseCRUD;
import com.example.template.view.ResetPasswordFormActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(AndroidJUnit4.class)
public class ResetPasswordFormTest {
    //passed all test
    @Mock
    private FirebaseCRUD mockFirebase;

    @Rule
    public ActivityScenarioRule<ResetPasswordFormActivity> activityScenarioRule = new ActivityScenarioRule<>(ResetPasswordFormActivity.class);

    @Before
    public void setUp(){
        mockFirebase = mock(FirebaseCRUD.class);
        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.setCrud(mockFirebase);
        });
    }

    @Test
    public void testPasswordValidation(){
        onView(withId(R.id.editPasswordText)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.editPasswordText2)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.validationButton)).perform(click());
        onView(withId(R.id.warningTextBox))
                .check(matches(withText(R.string.PASSWORD_ERROR)));
    }

    @Test
    public void testPasswordNonMatching(){


        onView(withId(R.id.editPasswordText)).perform(typeText("benBe23@#"), closeSoftKeyboard());
        onView(withId(R.id.editPasswordText2)).perform(typeText("benBe23@"), closeSoftKeyboard());
        onView(withId(R.id.validationButton)).perform(click());
        onView(withId(R.id.warningTextBox))
                .check(matches(withText(R.string.PASSWORD_NOT_MATCH)));
    }

    @Test
    public void testNewPasswordSuccessful(){

        onView(withId(R.id.editPasswordText)).perform(typeText("benBe23@#"), closeSoftKeyboard());
        onView(withId(R.id.editPasswordText2)).perform(typeText("benBe23@#"), closeSoftKeyboard());
        onView(withId(R.id.validationButton)).perform(click());

        onView(withId(R.id.LoginTitle))
                .check(matches(withText("Log In")));
    }
}
