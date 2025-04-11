package com.example.template.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.template.R;
import com.example.template.model.CurrentUser;
import com.example.template.model.Employee;
import com.example.template.model.User;
import com.example.template.util.FirebaseCRUD;
import com.google.firebase.database.FirebaseDatabase;

/**
 * @file FilterActivity.java
 * @author: -
 * @description: Activity that will handle in managing the filter for jobs.
 * filter includes filtering by keyword, category, and location.
 */

public class FilterActivity extends AppCompatActivity {
    Button filterButton;
    Spinner categorySpinner;
    SeekBar paySeekBar;
    TextView paySeekBarValue;
    String initialSearch;
    final String[] jobCategories = {"Gardening","Pet Care", "Cleaning", "Delivery", "Tutoring"};

    /**
     * Initializes activity and set user interface
     * @param savedInstanceState The saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        initializeViews();
        setListeners();

        getSearchInfoIntent();
    }

    private void getSearchInfoIntent() {
        Intent intent = getIntent();
        initialSearch = intent.getStringExtra("initialSearch");
    }

    private void initializeViews() {
        categorySpinner = findViewById(R.id.categorySelectionSpinner);
        initializeSpinner();

        filterButton = findViewById(R.id.buttonFilterCatAndSal);

        paySeekBarValue = findViewById(R.id.seekBarValue);
        paySeekBar = findViewById(R.id.seekBarPay);
        paySeekBar.setMax(100);
        paySeekBar.setProgress(50);

        //i will put this in anotehr function
        paySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 0){
                    paySeekBarValue.setText("No filter");
                }
                paySeekBarValue.setText("Minimal amount: $" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * Initializes each components listeners
     */
    private void setListeners() {
        filterButton.setOnClickListener(this::onFilterButtonSelected);
    }

    /**
     * Initializes each category spinners
     */
    private void initializeSpinner() {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jobCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }


    /**
     * Handles the click event for the filter by category button
     * @param view The view that was clicked.
     */
    private void onFilterButtonSelected(View view) {
        String category = categorySpinner.getSelectedItem().toString();
        int pay = paySeekBar.getProgress();

        move2JobActivity(category, pay);
    }

    /**
     * Navigate to the DashboardActivity
     */
    private void move2JobActivity(String category, int pay) {
        Intent intent = new Intent(FilterActivity.this, JobActivity.class);
        intent.putExtra("categoryFilter", category);
        intent.putExtra("payFilter", pay);
        intent.putExtra("initialSearch", initialSearch);

        startActivity(intent);
    }
}