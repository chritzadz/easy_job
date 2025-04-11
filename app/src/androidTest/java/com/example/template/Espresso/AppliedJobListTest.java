package com.example.template.Espresso;

import static androidx.test.core.app.ActivityScenario.launch;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.template.R;
import com.example.template.model.CurrentUser;
import com.example.template.model.Employee;
import com.example.template.model.User;
import com.example.template.view.AppliedJobListActivity;
import com.example.template.view.DashboardActivity;
import com.example.template.view.JobActivity;
import com.example.template.view.JobDetailsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AppliedJobListTest {
    private ActivityScenario<JobActivity> scenario;
    private Intent intent;

    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), AppliedJobListActivity.class);
        CurrentUser.getInstance().setUser(new Employee("chris", "dumanauw","chris@dal.ca","123!@#Qwe"));
    }

    @Test
    public void testRecyclerViewExists() {
        scenario = ActivityScenario.launch(intent);
        Espresso.onView(withId(R.id.appliedJobsRecyclerView)).check(matches(isDisplayed()));
    }

    @Test
    public void BackButtonExist() {
        scenario = ActivityScenario.launch(intent);
        Espresso.onView(withId(R.id.appliedJobListBackButton)).check(matches(isDisplayed()));
    }

    public ActivityScenario<JobActivity> getScenario() {
        return scenario;
    }

    public void setScenario(ActivityScenario<JobActivity> scenario) {
        this.scenario = scenario;
    }
}
