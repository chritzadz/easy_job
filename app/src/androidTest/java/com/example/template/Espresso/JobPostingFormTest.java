package com.example.template.Espresso;

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

import com.example.template.util.FirebaseCRUD;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class JobPostingFormTest {
    private static final String APP_PACKAGE = "com.example.template";
    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice device;

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

    @Test
    public void checkAllFormPartsExist() {
        UiObject jobName = device.findObject(new UiSelector().text("Job Name"));
        assertTrue(jobName.exists());
        UiObject jobDescription = device.findObject(new UiSelector().resourceId("com.example.template:id/jobDescription"));
        assertTrue(jobDescription.exists());
        UiObject jobLocation = device.findObject(new UiSelector().text("Enter City"));
        assertTrue(jobLocation.exists());
        UiObject jobPostButton = device.findObject(new UiSelector().text("Post Job"));
        assertTrue(jobPostButton.exists());
        UiObject jobCategory = device.findObject(new UiSelector().resourceId("com.example.template:id/jobCategory"));
        assertTrue(jobCategory.exists());
    }


}
