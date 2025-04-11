package com.example.template.Espresso;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.template.R;
import com.example.template.model.CurrentUser;
import com.example.template.model.Employee;
import com.example.template.model.Employer;
import com.example.template.model.User;
import com.example.template.util.FirebaseCRUD;
import org.mockito.MockedStatic;
import com.example.template.view.ResetPasswordActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class ResetPasswordTest {
    private CurrentUser currUser;

    @Rule
    public ActivityScenarioRule<ResetPasswordActivity> activityScenarioRule = new ActivityScenarioRule<>(ResetPasswordActivity.class);

    @Before
    public void setUp(){
        currUser = CurrentUser.getInstance();
        currUser.setUser(new Employer("chris", "anthony","test@dal.ca", "Pass123!"));
    }

    @Test
    public void testEmptyEmail(){
        onView(withId(R.id.editEmailAddressText)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.validationButton)).perform(click());
        onView(withId(R.id.warningTextBox))
                .check(matches(withText(R.string.NO_EMAIL)));
    }

    @Test
    public void testNonExistEmail(){
        User mockUser = currUser.getUser();

        onView(withId(R.id.editEmailAddressText)).perform(typeText("test1@dal.ca"), closeSoftKeyboard());
        onView(withId(R.id.validationButton)).perform(click());
        onView(withId(R.id.warningTextBox))
                .check(matches(withText(R.string.UNKNOWN_EMAIL)));
    }

    @Test
    public void testExistedEmail(){
        User mockUser = currUser.getUser();

        onView(withId(R.id.editEmailAddressText)).perform(typeText("test@dal.ca"), closeSoftKeyboard());
        onView(withId(R.id.validationButton)).perform(click());
        onView(withId(R.id.warningTextBox))
                .check(matches(withText("")));

    }

    @Test
    public void move2ResetPasswordFormActivity(){
        User mockUser = currUser.getUser();

        onView(withId(R.id.editEmailAddressText)).perform(typeText("test@dal.ca"), closeSoftKeyboard());
        onView(withId(R.id.validationButton)).perform(click());
        onView(withId(R.id.resetPasswordLabel2)).check(matches(withText("Reset Password")));
    }
}
