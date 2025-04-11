package com.example.template.UIAutomator;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import com.example.template.util.FirebaseCRUD;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;

public class HomePageSwitchingTest {

    private static final String APP_PACKAGE = "com.example.template"; // Your app's package name
    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice device;

    @Before
    public void setUp() {
        device = UiDevice.getInstance(getInstrumentation());
        device.wait(Until.hasObject(By.pkg(APP_PACKAGE).depth(0)), LAUNCH_TIMEOUT);

        Context context = getInstrumentation().getContext();

        final Intent intent = new Intent();
        intent.setClassName("com.example.template", "com.example.template.view.HomePageActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        device.wait(Until.hasObject(By.pkg(APP_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void testSignInFlow() throws Exception {
        UiObject signInButton = device.findObject(new UiSelector().text("Register"));
        assertTrue("Sign In button not found", signInButton.exists());
        signInButton.click();

        UiObject emailField = device.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/editTextTextEmailAddress"));
        assertTrue("Email field not found", emailField.exists());
        emailField.setText("test@example.com");

        UiObject passwordField = device.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/editTextTextPassword"));
        assertTrue("Password field not found", passwordField.exists());
        passwordField.setText("Password123");

        UiObject submitButton = device.findObject(new UiSelector().resourceId(APP_PACKAGE + ":id/submit"));
        assertTrue("Submit button not found", submitButton.exists());
        submitButton.click();
    }
}
