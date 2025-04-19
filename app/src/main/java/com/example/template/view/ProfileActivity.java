package com.example.template.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.template.Adapter.SettingAdapter;
import com.example.template.Firebase.FirebaseUseCase;
import com.example.template.R;
import com.example.template.model.CurrentUser;
import com.example.template.model.User;
import com.google.android.gms.maps.model.Dash;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;

public class ProfileActivity extends AppCompatActivity implements SettingAdapter.SettingFunctionClickListener {
    TextView nameLabel;
    TextView roleLabel;

    private final User currentUser = CurrentUser.getInstance().getUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseUseCase.set(this);

        setContents();
    }


    private void setContents() {
        nameLabel = findViewById(R.id.nameTextViewProfile);
        roleLabel = findViewById(R.id.roleTextViewProfile);

        nameLabel.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        roleLabel.setText(currentUser.getRole());
        showNavigation();

        RecyclerView resultView = findViewById(R.id.settingsFunctionRecyclerViewProfile);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        resultView.setLayoutManager(manager);

        DividerItemDecoration decoration = new DividerItemDecoration(resultView.getContext(), manager.getOrientation());
        resultView.addItemDecoration(decoration);

        SettingAdapter adapter = new SettingAdapter();
        adapter.setSettingFunctionClickListener(this);
        resultView.setAdapter(adapter);
    }

    private void showNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setPadding(0,0,0,0);
        bottomNavigationView.setSelectedItemId(R.id.nav_settings);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_settings) {
                return true;
            } else if (item.getItemId() == R.id.nav_home) {
                move2DashboardPage();
                return true;
            } else if (item.getItemId() == R.id.nav_job) {
                move2JobPage();
                return true;
            }
            return false;
        });
    }

    private void move2JobPage() {
        Intent intent = new Intent(this, JobActivity.class);
        startActivity(intent);
    }

    private void move2DashboardPage() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    private void move2LoginPage(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSettingFunctionClick(View view, int position) {
        switch (position) {
            case 0: //reset
                move2ProfileManagementPage();
                break;
            case 1: //switch
                alertSwitch();
                break;
            case 2: //logout
                alertLogout();
                break;
            default:
                break;
        }

    }

    private void move2ProfileManagementPage() {

    }

    private void alertSwitch() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Switch?")
                .setMessage("Are you sure you want to switch role?")
                .setPositiveButton("Yes", (dialog, which) -> processRoleSwitch(this))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void processRoleSwitch(Context context) {
        String email = currentUser.getEmail();

        FirebaseUseCase.switchRole(email, () -> { //keren jg pakelambda
            User updatedUser = FirebaseUseCase.findUserByEmail(email);
            CurrentUser.getInstance().setUser(updatedUser);

            Intent intent = new Intent(context, DashboardActivity.class);
            startActivity(intent);
        });
    }

    public void alertLogout(){
        new AlertDialog.Builder(this)
                .setTitle("Confirm Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> move2LoginPage())
                .setNegativeButton("Cancel", null)
                .show();
    }
}
