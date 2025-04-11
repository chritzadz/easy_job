package com.example.template.Espresso;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.core.app.ActivityScenario.launch; // Correct import for `launch`

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.template.R;
import com.example.template.model.Job;
import com.example.template.view.JobDetailsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JobDetailsPageTest {

    @Before
    public void setup() {
        launch(JobDetailsActivity.class);
    }

    @Test
    public void move2JobApplicationActivity() {
        onView(withId(R.id.jobDetailApplyButton)).perform(click());
        onView(withId(R.id.jobApplicationTitle)).check(matches(withText("Apply")));
    }

    @Test
    public void testDisplayAllJobDetail() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), JobDetailsActivity.class);
        Job mockJob = new Job("Teacher", "Teach elementary school students.", "Halifax", "A", "user@dal.ca", "5", "", "", "");
        intent.putExtra("jobObj", mockJob);

        ActivityScenario.launch(intent);

        onView(withId(R.id.jobDetailJobTitle)).check(matches(withText("Teacher")));
        onView(withId(R.id.jobDetailLocation)).check(matches(withText("Halifax")));
        onView(withId(R.id.jobDetailDescription)).check(matches(withText("Teach elementary school students.")));
        onView(withId(R.id.jobDetailCategory)).check(matches(withText("Category: A")));
    }

}
