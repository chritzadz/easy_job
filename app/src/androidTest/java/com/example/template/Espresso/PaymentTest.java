package com.example.template.Espresso;

import static androidx.test.core.app.ActivityScenario.launch;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.CoreMatchers.not;

import android.content.Intent;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.uiautomator.UiObject;

import com.example.template.R;
import com.example.template.model.Job;
import com.example.template.view.PaymentEmployerActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PaymentTest {


    @Test
    public void notPaidYet() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), PaymentEmployerActivity.class);
        Job mockJob = new Job("Gardener", "Lawn the grass", "Halifax","Gardening", "user@dal.ca",
                "10", "3", "-", "Completed");

        intent.putExtra("jobObj", mockJob);

        ActivityScenario.launch(intent);

        onView(withId(R.id.paymentButton)).check(matches(isEnabled()));
        onView(withId(R.id.paymentPageDoneButton)).check(matches(not(isEnabled())));
    }


    @Test
    public void paymentSuccessful() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), PaymentEmployerActivity.class);
        Job mockJob = new Job("Gardener", "Lawn the grass", "Halifax","Gardening", "user@dal.ca",
                "10", "3", "-", "Paid");

        intent.putExtra("jobObj", mockJob);

        ActivityScenario.launch(intent);

        onView(withId(R.id.paymentButton)).check(matches(not(isEnabled())));
        onView(withId(R.id.paymentPageDoneButton)).check(matches(isEnabled()));

    }

}
