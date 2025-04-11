package com.example.template.UIAutomator;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import com.example.template.util.FirebaseCRUD;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;

public class LoginToDashboardSwitchingTest {

    private static final String APP_PACKAGE = "com.example.template";
    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice device;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseCRUD firebase;

    @Before
    public void setUp() {
        device = UiDevice.getInstance(getInstrumentation());
        device.wait(Until.hasObject(By.pkg(APP_PACKAGE).depth(0)), LAUNCH_TIMEOUT);

        Context context = getInstrumentation().getContext();

        final Intent intent = new Intent();
        intent.setClassName("com.example.template", "com.example.template.view.LoginActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        device.wait(Until.hasObject(By.pkg(APP_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void checkIfLoginPageIsDisplayed() {
        UiObject emailBox = device.findObject(new UiSelector().text("Email"));
        assertTrue(emailBox.exists());
        UiObject passwordBox = device.findObject(new UiSelector().text("Password"));
        assertTrue(passwordBox.exists());
        UiObject loginButton = device.findObject(new UiSelector().text("Log In"));
        assertTrue(loginButton.exists());
    }

    @Test
    public void checkIfCredentialIsRetrieved() throws UiObjectNotFoundException, InterruptedException {
//        firebase.getEmailRef().setValue("test@dal.ca");
//        firebase.getPassRef().setValue("Pass!123");

        UiObject emailBox = device.findObject(new UiSelector().resourceId("com.example.template:id/loginEditTextEmailAddress"));
        emailBox.setText("test@dal.ca");
        UiObject passwordBox = device.findObject(new UiSelector().resourceId("com.example.template:id/logineditTextPassword"));
        passwordBox.setText("Pass!123");
        UiObject loginButton = device.findObject(new UiSelector().resourceId("com.example.template:id/loginButton"));
        loginButton.clickAndWaitForNewWindow();

        device.wait(Until.hasObject(By.res("com.example.template", "DashboardTitle")), 10000);

        UiObject dashboardView = device.findObject(new UiSelector().resourceId("com.example.template:id/DashboardTitle"));

        assertTrue("TextView not found!", dashboardView.exists());
    }
}
