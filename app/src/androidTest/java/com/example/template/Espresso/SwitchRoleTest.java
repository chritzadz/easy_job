package com.example.template.Espresso;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.runner.RunWith;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.template.R;
import com.example.template.model.CurrentUser;
import com.example.template.model.Employee;
import com.example.template.model.Employer;
import com.example.template.model.User;
import com.example.template.util.FirebaseCRUD;
import com.example.template.view.SwitchRoleActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

@RunWith(AndroidJUnit4.class)
public class SwitchRoleTest {
    private CurrentUser currUser;

    @Rule
    public ActivityScenarioRule<SwitchRoleActivity> activityScenarioRule =
            new ActivityScenarioRule<>(SwitchRoleActivity.class);

    @Before
    public void setUp(){
        currUser = CurrentUser.getInstance();
        currUser.setUser(new Employee("test", "test", "test@dal.ca", "Pass123!"));
    }

    @Test
    public void noEmail() {
        onView(withId(R.id.enterEmailAddress)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.confirmEmailButton)).perform(click());
        onView(withId(R.id.roleSwitchWarningText)).check(matches(withText(R.string.NO_EMAIL)));
    }

    @Test
    public void notValidEmail() {
        onView(withId(R.id.enterEmailAddress)).perform(typeText("test.test.com"), closeSoftKeyboard());
        onView(withId(R.id.confirmEmailButton)).perform(click());
        onView(withId(R.id.roleSwitchWarningText)).check(matches(withText(R.string.EMAIL_ERROR)));
    }

    @Test
    public void emailNotMatching() {
        onView(withId(R.id.enterEmailAddress)).perform(typeText("test@test.com"), closeSoftKeyboard());
        onView(withId(R.id.confirmEmailButton)).perform(click());
        onView(withId(R.id.roleSwitchWarningText)).check(matches(withText(R.string.UNKNOWN_EMAIL)));
    }

    @Test
    public void emailMatching() {
        onView(withId(R.id.enterEmailAddress)).perform(typeText("test@dal.ca"), closeSoftKeyboard());
        onView(withId(R.id.confirmEmailButton)).perform(click());

        String currRole = currUser.getUser().getRole();
    }
}
