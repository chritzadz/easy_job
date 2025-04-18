    package com.example.template.view;

    import android.content.Intent;
    import android.os.Bundle;

    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Spinner;
    import android.widget.TextView;


    import androidx.appcompat.app.AppCompatActivity;

    import com.example.template.Firebase.FirebaseUseCase;
    import com.example.template.R;
    import com.example.template.controller.CredentialCheckUseCase;

    import com.example.template.factory.UserRoleFactory;
    import com.example.template.model.User;
    import com.example.template.status.Status;
    import com.example.template.status.SuccessStatus;


    /**
     * @file RegisterActivity.java
     * @author: -
     * @description: Activity that prompts user to create an account.
     * system will require to input email and password for the account, and will
     * redirected to login page.
     */

    public class RegisterActivity extends AppCompatActivity {
        private EditText emailEditText;
        private EditText passwordEditText;
        private Button submitButton;
        private Spinner roleSelectionSpinner;
        private TextView statusLabel;
        private TextView logInLink;
        private EditText firstNameEditText;
        private EditText lastNameEditText;
        private final String[] roleOptions = {"Employer", "Employee"};

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

            FirebaseUseCase.set(this);

            setContents();
            setEventListeners();
        }

        private void setEventListeners() {
            submitButton.setOnClickListener(v -> checkCredentials());
            logInLink.setOnClickListener(v -> move2LogInActivity());
        }

        private void checkCredentials() {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String role = roleSelectionSpinner.getSelectedItem().toString();

            Status status = CredentialCheckUseCase.validateSignUp(email, password);
            if(status.equals(new SuccessStatus())){
                User user = UserRoleFactory.createUser(firstName, lastName, email, password, role);
                addUser(user);
                move2LogInActivity();
            }
            else{
                displayErrorMessage(status);
            }
        }

        public void addUser(User user){
            FirebaseUseCase.addUser(user);
        }

        private void move2LogInActivity() {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        private void setContents() {
            emailEditText = findViewById(R.id.editTextTextEmailAddress);
            passwordEditText = findViewById(R.id.editTextTextPassword);
            firstNameEditText = findViewById(R.id.editTextTextFirstName);
            lastNameEditText = findViewById(R.id.editTextTextLastName);
            submitButton = findViewById(R.id.submit);
            statusLabel = findViewById(R.id.statusLabelRegister);
            roleSelectionSpinner = findViewById(R.id.userRoleSelectionSpinner);
            logInLink = findViewById(R.id.textViewForLoginLink);
            initializeSpinner();
        }

        private void initializeSpinner() {
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roleOptions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            roleSelectionSpinner.setAdapter(adapter);
        }

        private void displayErrorMessage(Status status) {
            statusLabel.setText(status.message());
        }
    }