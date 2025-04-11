    package com.example.template.view;

    import android.content.Intent;
    import android.os.Bundle;
    import android.text.TextUtils;
    import android.util.Log;
    import android.view.View;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Spinner;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;

    import com.example.template.R;
    import com.example.template.controller.CredentialValidator;
    import com.example.template.factory.UserRoleFactory;
    import com.example.template.model.User;

    import com.example.template.util.FirebaseCRUD;
    import com.google.firebase.FirebaseApp;

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
        FirebaseCRUD crud;

        /**
         * Initializes activity and set user interface
         * @param savedInstanceState The saved instance state bundle
         */
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            FirebaseApp.initializeApp(this);
            crud = FirebaseCRUD.getInstance();

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

            setContents();
            setEventListeners();
        }

        /**
         * Initializes each components listeners
         */
        private void setEventListeners() {
            submitButton.setOnClickListener(this::onSubmitButtonClick);
            logInLink.setOnClickListener(this::onLoginLinkClick);
        }

        /**
         * Handles the click event for login link
         * @param view The view that was clicked
         */
        private void onLoginLinkClick(View view) {
            move2LogInActivity();
        }

        /**
         * Handles the click event for submit button
         * @param view The view that was clicked
         */
        private void onSubmitButtonClick(View view) {
            if(credentialValidation()){
                move2LogInActivity();
            }
        }

        /**
         * Navigate to the LoginActivity
         */
        private void move2LogInActivity() {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        private boolean credentialValidation() {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String role = roleSelectionSpinner.getSelectedItem().toString();

            if (!CredentialValidator.isValidEmail(email)) {
                displayEmailError(email);
                return false;
            }
            else if (!CredentialValidator.isValidPassword(password)) {
                displayPasswordError(password);
                return false;
            }

            User user = UserRoleFactory.createUser(firstName, lastName, email, password, role);
            addUserToDatabase(user);
            return true;
        }

        /**
         * Initializes content components of the interface
         */
        private void setContents() {
            emailEditText = findViewById(R.id.editTextTextEmailAddress);
            passwordEditText = findViewById(R.id.editTextTextPassword);
            firstNameEditText = findViewById(R.id.editTextTextFirstName);
            lastNameEditText = findViewById(R.id.editTextTextLastName);
            submitButton = findViewById(R.id.submit);
            statusLabel = findViewById(R.id.statusLabelRegister);
            roleSelectionSpinner = (Spinner) findViewById(R.id.userRoleSelectionSpinner);
            logInLink = findViewById(R.id.textViewForLoginLink);
            initializeSpinner();
        }

        /**
         * Initializes the spinner for role selection with predefined role options
         */
        private void initializeSpinner() {
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roleOptions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            roleSelectionSpinner.setAdapter(adapter);
        }

        /**
         * Adds a new user to the database and displays a success message
         * @param user The User object to be added to the database
         */
        private void addUserToDatabase(User user) {
            crud.addUser(user);
            Toast.makeText(RegisterActivity.this, "Form Submitted Successfully", Toast.LENGTH_LONG).show();
        }

        /**
         * Displays an error message related to the email input, create toast if there is validation violation
         * @param email The email address entered by the user
         */
        private void displayEmailError(String email) {
            String emailErrorMessage = "";
            Toast emailError;
            if (TextUtils.isEmpty(email)) {
                emailErrorMessage = "Please enter an email address";
                statusLabel.setText(emailErrorMessage);
                emailError = Toast.makeText(this, emailErrorMessage, Toast.LENGTH_LONG);
                emailError.show();
            } else {
                emailErrorMessage = "Enter an email address of the form user@example.com";
                statusLabel.setText(emailErrorMessage);
                emailError = Toast.makeText(this, emailErrorMessage, Toast.LENGTH_LONG);
                emailError.show();
            }
        }

        /**
         * Displays an error message related to the password input, create toast if there is validation violation
         * @param password The password entered by the user
         */
        private void displayPasswordError(String password) {
            String passwordErrorMessage = "";
            Toast passwordError;
            if (TextUtils.isEmpty(password)) {
                passwordErrorMessage = "Please enter a password";
                statusLabel.setText(passwordErrorMessage);
                passwordError = Toast.makeText(this, passwordErrorMessage, Toast.LENGTH_LONG);
                passwordError.show();
            } else {
                passwordErrorMessage = "Password must be at least 8 characters long, contain uppercase, lowercase, digit, and special character";
                statusLabel.setText(passwordErrorMessage);
                passwordError = Toast.makeText(this, passwordErrorMessage, Toast.LENGTH_LONG);
                passwordError.show();
            }
        }
    }