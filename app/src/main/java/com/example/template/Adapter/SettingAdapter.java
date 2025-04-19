/**
 *  File: ApplicationAdapter.java
 *  Author:
 *
 *  Description: This class implements an Adapter design pattern that allows
 *  to fetch application data from firebase realtime database to interact and shown
 *  in RecyclerView format.
 */

package com.example.template.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.template.R;
import com.example.template.model.Application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingFunctionViewHolder> {
    private final List<String> settingList = new ArrayList<>(Arrays.asList("Reset password", "Switch role", "Logout"));
    private SettingFunctionClickListener listener;

    @NonNull
    @Override
    public SettingFunctionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_profile_item, parent, false);
        return new SettingFunctionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingFunctionViewHolder holder, int position) {

        holder.functionName.setText(settingList.get(position));

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onSettingFunctionClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return settingList.size();
    }

    public void setSettingFunctionClickListener(SettingFunctionClickListener listener) {
        this.listener = listener;
    }

    public interface SettingFunctionClickListener {
        void onSettingFunctionClick(View view, int position);
    }

    public static class SettingFunctionViewHolder extends RecyclerView.ViewHolder {
        TextView functionName;
        public SettingFunctionViewHolder(View itemView) {
            super(itemView);
            functionName = itemView.findViewById(R.id.settingFunctionTextView);
        }
    }
}
