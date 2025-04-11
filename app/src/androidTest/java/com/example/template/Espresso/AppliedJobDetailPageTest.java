package com.example.template.Espresso;

import static androidx.test.core.app.ActivityScenario.launch;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import com.example.template.R;
import com.example.template.factory.UserRoleFactory;
import com.example.template.model.CurrentUser;
import com.example.template.model.Job;
import com.example.template.view.AppliedJobDetailsActivity;
import com.example.template.view.JobDetailsActivity;

import org.junit.Before;
import org.junit.Test;

public class AppliedJobDetailPageTest {
    @Before
    public void setup() {
        CurrentUser.getInstance().setUser(UserRoleFactory.createUser("chris", "dum", "chris@dal.ca", "123123", "employer"));
    }

    @Test
    public void testDisplayAllJobDetail() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AppliedJobDetailsActivity.class);
        Job mockJob = new Job("Teacher", "Teach elementary school students.", "Halifax", "A", "user@dal.ca", "5", "5", "19283801", "pending");
        intent.putExtra("jobObj", mockJob);

        ActivityScenario.launch(intent);

        onView(withId(R.id.appliedJobDetailStatus)).check(matches(withText("Status: pending")));
        onView(withId(R.id.appliedJobDetailHours)).check(matches(withText("Hours: 5")));
        onView(withId(R.id.appliedJobDetailSalary)).check(matches(withText("$5")));
        onView(withId(R.id.appliedJobDetailLocation)).check(matches(withText("Halifax")));
    }

    @Test
    public void testBackButton() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AppliedJobDetailsActivity.class);
        Job mockJob = new Job("Teacher", "Teach elementary school students.", "Halifax", "A", "user@dal.ca", "5", "5", "19283801", "pending");
        intent.putExtra("jobObj", mockJob);

        ActivityScenario.launch(intent);
        onView(withId(R.id.appliedJobDetailBackButton)).perform(click());
        onView(withId(R.id.welcomeTextView)).check(matches(isDisplayed()));
    }
}
