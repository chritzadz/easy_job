package com.example.template.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.template.Firebase.FirebaseUseCase;
import com.example.template.R;
import com.example.template.model.CurrentUser;
import com.example.template.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileManagementActivity extends AppCompatActivity {
    ImageView backButton;
    TextInputEditText firstName;
    TextInputEditText lastName;
    TextInputEditText role;
    Button saveButton;
    private User currUser = CurrentUser.getInstance().getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);

        FirebaseUseCase.set(this);

        setContents();
        setEventListeners();
    }

    private void setEventListeners() {
        backButton.setOnClickListener(v -> move2ProfilePage());
        saveButton.setOnClickListener(v -> updateProfile(this));
    }

    private void updateProfile(Context context) {
        String first = firstName.getText().toString().trim();
        String second = lastName.getText().toString().trim();

        FirebaseUseCase.updateProfile(currUser.getEmail(), first, second, () -> {
            currUser.setFirstName(first);
            currUser.setLastName(second);
            Intent intent = new Intent(context, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void move2ProfilePage() {
        Log.d("CLICK", "BACK BUTTON");
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void setContents() {
        backButton = findViewById(R.id.backButtonImageViewProfileManagement);
        firstName = findViewById(R.id.firstNameTextInputProfileManagement);
        lastName = findViewById(R.id.lastNameTextInputProfileManagement);
        role = findViewById(R.id.roleTextInputProfileManagement);
        saveButton = findViewById(R.id.saveButtonProfileManagement);

        firstName.setText(currUser.getFirstName());
        lastName.setText(currUser.getLastName());
        role.setText(currUser.getRole());
        backButton.bringToFront();
    }
}
