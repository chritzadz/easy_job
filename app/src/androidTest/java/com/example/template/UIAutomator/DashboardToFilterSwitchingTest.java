package com.example.template.UIAutomator;

import androidx.test.uiautomator.UiDevice;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;

public class DashboardToFilterSwitchingTest {
    private static final String APP_PACKAGE = "com.example.template";

    private  static final int LAUNCH_TIMEOUT = 5000;

    private UiDevice device;

    @Before
    public void setUp(){
        device = UiDevice.getInstance(getInstrumentation());
        device.wait(Until.hasObject(By.pkg(APP_PACKAGE).depth(0)), LAUNCH_TIMEOUT);

        Context context = getInstrumentation().getContext();
        final Intent intent = new Intent();
        intent.setClassName("com.example.template", "com.example.template.view.DashboardActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("role", "Employee");
        context.startActivity(intent);
        device.wait(Until.hasObject(By.pkg(APP_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void checkEmployeeDashboardIsDisplayed() {
        UiObject filterButton = device.findObject(new UiSelector().text("Filter Jobs"));
    }

}
