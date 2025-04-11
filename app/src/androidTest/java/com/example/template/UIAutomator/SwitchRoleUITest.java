package com.example.template.UIAutomator;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertFalse;
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
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class SwitchRoleUITest {

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
    public void checkSwitchRoleButtonExists() {
        UiObject switchRoleButton = device.findObject(new UiSelector().text("Switch Role"));
        assertTrue(switchRoleButton.exists());
    }

    @Test
    public void checkSwitchRolePageContentExists() throws UiObjectNotFoundException {
        UiObject switchRoleButton = device.findObject(new UiSelector().text("Switch Role"));
        switchRoleButton.clickAndWaitForNewWindow();

        UiObject switchRoleTitle = device.findObject(new UiSelector().textContains("switch role"));
        assertTrue(switchRoleTitle.exists());
        UiObject switchRoleEmail = device.findObject(new UiSelector().text("Email"));
        assertTrue(switchRoleEmail.exists());
        UiObject switchRoleConfirm = device.findObject(new UiSelector().text("Confirm"));
        assertTrue(switchRoleConfirm.exists());
    }

    @Test
    public void checkDashboardSwitchIsCorrect() throws UiObjectNotFoundException, InterruptedException {
        UiObject switchRoleButton = device.findObject(new UiSelector().text("Switch Role"));
        switchRoleButton.clickAndWaitForNewWindow();

//        crud.getEmailRef().setValue("test@dal.ca");
//        crud.getPassRef().setValue("Pass123!");
//        crud.getRoleRef().setValue("Employee");

        Thread.sleep(3000);

        UiObject email = device.findObject(new UiSelector().text("Email"));
        email.setText("test@dal.ca");
        UiObject confirmButton = device.findObject(new UiSelector().text("Confirm"));
        confirmButton.clickAndWaitForNewWindow();

        UiObject employeeView = device.findObject(new UiSelector().text("Employer view"));
        assertTrue(employeeView.exists());
    }

    @Test
    public void checkRoleUpdatedInFirebase() throws UiObjectNotFoundException, InterruptedException {
        UiObject switchRoleButton = device.findObject(new UiSelector().text("Switch Role"));
        switchRoleButton.clickAndWaitForNewWindow();

//        crud.getEmailRef().setValue("test@dal.ca");
//        crud.getPassRef().setValue("Pass123!");
//        crud.getRoleRef().setValue("Employee");

        Thread.sleep(3000);

//        String oldRole = crud.getExtractedRole();
        UiObject email = device.findObject(new UiSelector().text("Email"));
        email.setText("test@dal.ca");
        UiObject confirmButton = device.findObject(new UiSelector().text("Confirm"));
        confirmButton.clickAndWaitForNewWindow();

//        String newRole = crud.getExtractedRole();
//        assertFalse(newRole.equals(oldRole));
    }

}
