package com.example.template.Espresso;

import static androidx.test.core.app.ActivityScenario.launch;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import com.example.template.R;
import com.example.template.model.Job;
import com.example.template.model.User;
import com.example.template.view.JobActivity;
import com.example.template.view.JobApplicationActivity;
import com.example.template.view.JobDetailsActivity;

import org.junit.Test;

public class JobApplicationPageTest {

    @Test
    public void testDisplayAllJobDetail(){
        // Launch activity with job details
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), JobApplicationActivity.class);
        Job mockJob = new Job("Teacher", "Teach elementary school students.", "Halifax", "A", "user@dal.ca", "5", "5","", "");
        intent.putExtra("jobObj", mockJob);

        ActivityScenario.launch(intent);

        // Verify that the job details are displayed correctly
        onView(withId(R.id.jobApplicationTitle)).check(matches(withText("Apply")));
        onView(withId(R.id.jobApplicationJobTitle)).check(matches(withText("Teacher")));
        onView(withId(R.id.jobApplicationHeading)).check(matches(withText("Job Title:")));
        onView(withId(R.id.jobApplicationCoverLetterTitle)).check(matches(withText("Cover Letter")));
    }

    @Test
    public void submitWithEmptyCoverLetter(){
        // Launch activity with job details
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), JobApplicationActivity.class);
        Job mockJob = new Job("Teacher", "Teach elementary school students.", "Halifax", "A", "user@dal.ca", "5", "5", "", "");
        User mockUser = new User("test", "test", "employee@dal.ca", "Pass!123");
        intent.putExtra("jobObj", mockJob);
        intent.putExtra("userObj", mockUser);

        ActivityScenario.launch(intent);

        // Click submit button without entering cover letter
        onView(withId(R.id.jobApplicationSubmitButton)).perform(click());

        // Verify that an error message is displayed
        onView(withId(R.id.jobApplicationTitle))
                .check(matches(withText("Apply")));
    }
}