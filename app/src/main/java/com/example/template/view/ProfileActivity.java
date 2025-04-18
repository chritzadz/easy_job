package com.example.template.view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.template.Firebase.FirebaseUseCase;
import com.example.template.R;
import com.example.template.model.CurrentUser;
import com.example.template.model.User;

public class ProfileActivity extends AppCompatActivity {
    TextView nameLabel;
    TextView roleLabel;

    private User currentUser = CurrentUser.getInstance().getUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setContents();
    }

    private void setEventListeners() {
    }

    private void setContents() {
        nameLabel = findViewById(R.id.nameTextViewProfile);
        roleLabel = findViewById(R.id.roleTextViewProfile);

        nameLabel.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        roleLabel.setText(currentUser.getRole());
    }
}
