package com.example.template.Espresso;

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
import com.example.template.util.FirebaseCRUD;
import com.example.template.view.DashboardActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;


import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;

@RunWith(AndroidJUnit4.class)
public class EmployeeDashboardPageTest {
    private ActivityScenario<DashboardActivity> scenario;
    private Intent intent;

    @Before
    public void setUp() {
        User testUserEmployee = new Employee("test", "test", "test@dal.ca", "123!@#Qwe");
        CurrentUser.getInstance().setUser(testUserEmployee);
        intent = new Intent(ApplicationProvider.getApplicationContext(), DashboardActivity.class);
    }

    @After
    public void tearDown() {
        CurrentUser.getInstance().setUser(null);
    }

    @Test
    public void testRecyclerViewExists() {
        scenario = ActivityScenario.launch(intent);
        Espresso.onView(ViewMatchers.withId(R.id.jobRecycleView)).check(matches(isDisplayed()));
    }

    @Test
    public void LocationBoxExist() {
        scenario = ActivityScenario.launch(intent);
        Espresso.onView(ViewMatchers.withId(R.id.locationTextBox)).check(matches(isDisplayed()));
    }
}
