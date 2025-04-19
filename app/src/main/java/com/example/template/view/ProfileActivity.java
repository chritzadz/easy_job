package com.example.template.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.template.Adapter.SettingAdapter;
import com.example.template.R;
import com.example.template.model.CurrentUser;
import com.example.template.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity implements SettingAdapter.SettingFunctionClickListener {
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

    @Override
    public void onSettingFunctionClick(View view, int position) {

    }
}
