package com.example.template.Espresso;

import static androidx.test.core.app.ActivityScenario.launch;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import com.example.template.R;
import com.example.template.factory.UserRoleFactory;
import com.example.template.model.Application;
import com.example.template.model.CurrentUser;
import com.example.template.model.Job;
import com.example.template.view.ApplicationDetailsActivity;
import com.example.template.view.JobDetailsActivity;

import org.junit.Before;
import org.junit.Test;

public class ApplicationDetailsTest {
    @Before
    public void setup() {
        CurrentUser.getInstance().setUser(UserRoleFactory.createUser("chris", "dum", "chris@dal.ca", "123123", "employer"));
    }

    @Test
    public void testDisplayAllApplicationDetail() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ApplicationDetailsActivity.class);

        Job job = new Job("Teacher", "Teach elementary school students.", "Halifax", "A", "user@dal.ca", "5", "5", "19283801", "pending");
        Application clickedApp = new Application("user2@dal.ca", "user@dal.ca" ,"i want this job" ,"19283801");

        intent.putExtra("appObj", clickedApp);
        intent.putExtra("appId", clickedApp.getKey());
        intent.putExtra("corrJob", job);
        intent.putExtra("prevPage", "ApplicationListActivity");

        ActivityScenario.launch(intent);

        onView(withId(R.id.acceptButton)).check(matches(withText("Accept")));
        onView(withId(R.id.declineButton)).check(matches(withText("Decline")));
    }

    @Test
    public void testBackButtonClick() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ApplicationDetailsActivity.class);

        Job job = new Job("Teacher", "Teach elementary school students.", "Halifax", "A", "user@dal.ca", "5", "5", "19283801", "pending");
        Application clickedApp = new Application("user2@dal.ca", "user@dal.ca" ,"i want this job" ,"19283801");
        CurrentUser.getInstance().setUser(UserRoleFactory.createUser("chris", "dum", "chris@dal.ca", "123123", "employer"));

        intent.putExtra("appObj", clickedApp);
        intent.putExtra("appId", clickedApp.getKey());
        intent.putExtra("corrJob", job);
        intent.putExtra("prevPage", "EmployerApplicationListActivity");

        ActivityScenario.launch(intent);

        onView(withId(R.id.backButtonAppDetails))
                .check(matches(isClickable()))
                .perform(click());
        onView(withId(R.id.allAppRecyler)).check(matches(isDisplayed()));
    }
}
