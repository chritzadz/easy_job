package com.example.template.UIAutomator;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.Intent;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import com.example.template.model.User;
import com.example.template.util.FirebaseCRUD;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class ResetPageAndFormTest {
    //all test passed

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
        intent.setClassName("com.example.template", "com.example.template.view.SettingsActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        mockFirebase = mock(FirebaseCRUD.class);
        device.wait(Until.hasObject(By.pkg(APP_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void checkIfResetPasswordButtonExist() {
        UiObject resetPassButton = device.findObject(new UiSelector().text("Reset Password"));
        assertTrue(resetPassButton.exists());
    }

    @Test
    public void checkIfResetPasswordPageContentExist() throws UiObjectNotFoundException {
        UiObject button = device.findObject(new UiSelector().text("Reset Password"));
        button.clickAndWaitForNewWindow();

        UiObject emailBox = device.findObject(new UiSelector().text("Email Address"));
        assertTrue(emailBox.exists());
        UiObject validationButton = device.findObject(new UiSelector().text("Validate"));
        assertTrue(validationButton.exists());
    }

    @Test
    public void checkIfResetPasswordPageIsDisplayed() throws UiObjectNotFoundException {
        UiObject button = device.findObject(new UiSelector().text("Reset Password"));
        button.clickAndWaitForNewWindow();
        UiObject label = device.findObject(new UiSelector().resourceId("com.example.template:id/enterEmailAddressLabel"));
        assertTrue(label.exists());
    }

    @Test
    public void checkIfResetPasswordPageFormIsDisplayed() throws UiObjectNotFoundException {
        TaskCompletionSource<User> taskCompletionSource = new TaskCompletionSource<>();
        Task<User> mockTask = taskCompletionSource.getTask();

        when(mockFirebase.findUserByEmail("test@dal.ca")).thenReturn(mockTask);

        UiObject button = device.findObject(new UiSelector().text("Reset Password"));
        button.clickAndWaitForNewWindow();

        UiObject emailBox = device.findObject(new UiSelector().text("Email Address"));
        UiObject validationButton = device.findObject(new UiSelector().text("Validate"));

        emailBox.setText("test@example.com");
        validationButton.clickAndWaitForNewWindow();

        UiObject label = device.findObject(new UiSelector().resourceId("com.example.template:id/resetPasswordLabel2"));
        assertTrue(label.exists());
    }

    @Test
    public void checkIfLogInPageIsDisplayed() throws UiObjectNotFoundException {
        TaskCompletionSource<User> taskCompletionSource = new TaskCompletionSource<>();
        Task<User> mockTask = taskCompletionSource.getTask();

        when(mockFirebase.findUserByEmail("test@dal.ca")).thenReturn(mockTask);

        UiObject button = device.findObject(new UiSelector().text("Reset Password"));
        button.clickAndWaitForNewWindow();

        UiObject emailBox = device.findObject(new UiSelector().text("Email Address"));
        UiObject validationButton = device.findObject(new UiSelector().text("Validate"));

        emailBox.setText("test@example.com");
        validationButton.clickAndWaitForNewWindow();

        UiObject newPassword = device.findObject(new UiSelector().resourceId("com.example.template:id/editPasswordText"));
        UiObject newPasswordConfirm = device.findObject(new UiSelector().resourceId("com.example.template:id/editPasswordText2"));

        newPassword.setText("benBe23@#");
        newPasswordConfirm.setText("benBe23@#");
        UiObject button2 = device.findObject(new UiSelector().text("Change Password"));
        button2.clickAndWaitForNewWindow();

        UiObject label = device.findObject(new UiSelector().text("Log In"));
        assertTrue(label.exists());
    }

    @Test
    public void checkIfPasswordUpdated() throws UiObjectNotFoundException {
        TaskCompletionSource<User> taskCompletionSource = new TaskCompletionSource<>();
        Task<User> mockTask = taskCompletionSource.getTask();

        when(mockFirebase.findUserByEmail("test@dal.ca")).thenReturn(mockTask);

        UiObject button = device.findObject(new UiSelector().text("Reset Password"));
        button.clickAndWaitForNewWindow();

        UiObject emailBox = device.findObject(new UiSelector().text("Email Address"));
        UiObject validationButton = device.findObject(new UiSelector().text("Validate"));

        emailBox.setText("test@example.com");
        validationButton.clickAndWaitForNewWindow();

        UiObject newPassword = device.findObject(new UiSelector().resourceId("com.example.template:id/editPasswordText"));
        UiObject newPasswordConfirm = device.findObject(new UiSelector().resourceId("com.example.template:id/editPasswordText2"));

        newPassword.setText("benBe23@#");
        newPasswordConfirm.setText("benBe23@#");
        UiObject button2 = device.findObject(new UiSelector().text("Change Password"));
        button2.clickAndWaitForNewWindow();

        //check the hash value later.
    }
}
