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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.template.R;
import com.example.template.model.Employee;
import com.example.template.model.User;
import com.example.template.util.FirebaseCRUD;
import com.example.template.view.LoginActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(AndroidJUnit4.class)
public class LoginPageTest {
    @Mock
    private FirebaseCRUD mockFirebase;

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setUp(){
        mockFirebase = mock(FirebaseCRUD.class);
        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.setCrud(mockFirebase);
        });
    }

    @Test
    public void testUnknownEmail() {
        TaskCompletionSource<User> taskCompletionSource = new TaskCompletionSource<>();
        taskCompletionSource.setResult(null);

        when(mockFirebase.findUserByEmail("test@example.com")).thenReturn(taskCompletionSource.getTask());

        onView(withId(R.id.loginEditTextEmailAddress)).perform(typeText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.logineditTextPassword)).perform(typeText("Pass@123"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.UNKNOWN_EMAIL)));
    }

    @Test
    public void testIncorrectPassword(){
        User mockUser = new Employee("test", "test", "test@example.com", "Pass@123");

        TaskCompletionSource<User> taskCompletionSource = new TaskCompletionSource<>();
        taskCompletionSource.setResult(mockUser);

        when(mockFirebase.findUserByEmail("test@example.com")).thenReturn(taskCompletionSource.getTask());

        onView(withId(R.id.loginEditTextEmailAddress)).perform(typeText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.logineditTextPassword)).perform(typeText("123!@#Qwe"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.INCORRECT_PASSWORD)));
    }
}
