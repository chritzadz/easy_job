package com.example.template.UIAutomator;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import android.content.Context;
import android.content.Intent;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import com.example.template.model.CurrentUser;
import com.example.template.model.Employer;
import com.example.template.util.FirebaseCRUD;
import com.example.template.view.JobPostingFormActivity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class JobPostingToDashboardTest {
    private static final String APP_PACKAGE = "com.example.template";
    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice device;
    private CurrentUser currUser;
    @Mock
    FirebaseCRUD mockFirebase;

    @Before
    public void setUp() {
        device = UiDevice.getInstance(getInstrumentation());
        device.wait(Until.hasObject(By.pkg(APP_PACKAGE).depth(0)), LAUNCH_TIMEOUT);

        Context context = getInstrumentation().getContext();

        final Intent intent = new Intent();
        intent.setClassName("com.example.template", "com.example.template.view.JobPostingFormActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        mockFirebase = mock(FirebaseCRUD.class);
        device.wait(Until.hasObject(By.pkg(APP_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    //need to add mocking so the test passes
    @Test
    public void move2DashboardWorks() throws UiObjectNotFoundException {
        UiObject jobName = device.findObject(new UiSelector().text("Job Name"));
        jobName.setText("Test");
        UiObject jobDescription = device.findObject(new UiSelector().resourceId("com.example.template:id/jobDescription"));
        jobDescription.setText("Test description");
        UiObject jobLocation = device.findObject(new UiSelector().text("Enter City"));
        jobLocation.setText("Vancouver");
        //keep job category as the default
        UiObject jobPay = device.findObject(new UiSelector().text("Pay Amount"));
        jobPay.setText("45");
        currUser = CurrentUser.getInstance();
        currUser.setUser(new Employer("chris", "anthony", "Test@dal.ca", "Pass123!"));

        UiObject jobPostButton = device.findObject(new UiSelector().text("Post Job"));
        jobPostButton.clickAndWaitForNewWindow();
    }

}
