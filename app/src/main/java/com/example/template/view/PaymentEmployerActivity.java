package com.example.template.view;

import static java.lang.String.valueOf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.template.R;
import com.example.template.model.Job;
import com.example.template.util.FirebaseCRUD;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class PaymentEmployerActivity extends AppCompatActivity {

    /**
     *      credit card number: 4214027353485201
     *      expiry date: 03/2030
     *      cvc code: any 3 digits
     * */
    FirebaseCRUD crud = FirebaseCRUD.getInstance(this);
    private static final String TAG = PaymentEmployerActivity.class.getName();
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private PayPalConfiguration payPalConfig;
    private TextView jobTitle;
    private TextView jobStatus;
    private TextView jobHours;
    private TextView jobPay;
    private TextView jobLocation;
    private TextView jobDescription;
    private Button paymentButton;
    private Button backButton;
    private Button doneButton;
    private Job job;
    private TextView paymentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_employer);

        getJobIntent();
        initializeViews();
        initializeListeners();
        initializeButtons();
        showDetails();

        initActivityLauncher();
        configPayPal();

    }

    private void configPayPal() {
        String clientID = valueOf(R.string.paypal_key);
        payPalConfig = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(clientID);
    }

    private void initializeViews() {
        paymentButton = findViewById(R.id.paymentButton);
        paymentStatus = findViewById(R.id.successErrorMessage);
        doneButton = findViewById(R.id.paymentPageDoneButton);
        backButton = findViewById(R.id.paymentBackButton);
        jobTitle = findViewById(R.id.paymentJobTitle);
        jobStatus = findViewById(R.id.paymentStatus);
        jobHours = findViewById(R.id.paymentHours);
        jobPay = findViewById(R.id.paymentSalary);
        jobLocation = findViewById(R.id.paymentLocation);
        jobDescription = findViewById(R.id.paymentDescription);
    }

    private void initializeListeners() {
        paymentButton.setOnClickListener(v -> processPayment());
        backButton.setOnClickListener(v -> move2PrevPage());
        doneButton.setOnClickListener(v -> move2Dashboard());
    }

    private void showDetails() {
        jobTitle.setText(job.getJobName());
        jobStatus.setText(String.format("Status: %s", job.getJobStatus()));
        jobHours.setText(String.format("Hours: %s", job.getJobHours()));
        jobPay.setText(String.format("$%s", job.getJobPay()));
        jobLocation.setText(job.getJobLocation());
        jobDescription.setText(job.getJobDescription());
    }

    private void getJobIntent() {
        Intent intent = getIntent();
        job = (Job) intent.getSerializableExtra("jobObj");
    }

    private void initializeButtons() {
        paymentButton.setEnabled(true);
        doneButton.setEnabled(false);
    }

    private void updateButtons() {
        String jobKey = job.getJobKey();

        /*The two if statements do the same thing but for the tests the one without the
        * firebase is needed and the one with the firebase access is needed for the
        * live application.
        * */

        //this is here for test purposes
        if(job.getJobStatus().equals("Paid")){
            paymentButton.setEnabled(false);
            doneButton.setEnabled(true);
        }
        //this is here for non test purposes
        crud.getStatusByJob(jobKey).addOnSuccessListener(status -> {
            if(status != null) {
                if(status.equals("Paid")){
                    paymentButton.setEnabled(false);
                    doneButton.setEnabled(true);
                }
            }
        }).addOnFailureListener(e -> {
            System.out.println("Error retrieving status: " + e.getMessage());
        });
    }

    //from tutorial code base
    private void initActivityLauncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        final PaymentConfirmation confirmation = result.getData().getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                        if (confirmation != null) {
                            try {
                                // Get the payment details
                                String paymentDetails = confirmation.toJSONObject().toString(4);
                                Log.i(TAG, paymentDetails);
                                // Extract json response and display it in a text view.
                                JSONObject payObj = new JSONObject(paymentDetails);
                                String payID = payObj.getJSONObject("response").getString("id");
                                String state = payObj.getJSONObject("response").getString("state");
                                paymentStatus.setText(String.format("Payment %s%n with payment id is %s", state, payID));
                                crud.updateJobStatus(job.getJobKey(), "Paid");
                                updateButtons();
                            } catch (JSONException e) {
                                Log.e("Error", "an extremely unlikely failure occurred: ", e);
                            }
                        }
                    } else if (result.getResultCode() == PaymentActivity.RESULT_EXTRAS_INVALID) {
                        Log.d(TAG, "Launcher Result Invalid");
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        Log.d(TAG, "Launcher Result Cancelled");
                    }
                });
    }


    //from tutorial code base
    private void processPayment() {
        final String amount = job.getJobPay();
        final PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount),
                "CAD", "Pay for Service", PayPalPayment.PAYMENT_INTENT_SALE);

        // Create Paypal Payment activity intent
        final Intent intent = new Intent(this, PaymentActivity.class);
        // Adding paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
        // Adding paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        // Starting Activity Request launcher
        activityResultLauncher.launch(intent);

    }

    private void move2Dashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    private void move2PrevPage() {
        Intent intent = new Intent(this, AppliedJobDetailsActivity.class);
        intent.putExtra("jobObj", job);
        startActivity(intent);
    }

}
