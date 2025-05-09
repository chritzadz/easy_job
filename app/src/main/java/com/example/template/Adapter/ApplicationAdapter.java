package com.example.template.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.template.R;
import com.example.template.model.Application;

import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder>{
    private List<Application> apps;
    private ApplicationAdapter.ApplicationClickListener listener;
    public ApplicationAdapter(List<Application> apps){
        this.apps = apps;
    }

    @NonNull
    @Override
    public ApplicationAdapter.ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_application_item, parent, false);
        return new ApplicationAdapter.ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationAdapter.ApplicationViewHolder holder, int position) {
        Application application = apps.get(position);
        holder.appKeyLabel.setText(application.getAppKey());
        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onApplicationClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public void setApplicationClickListener(ApplicationAdapter.ApplicationClickListener listener) {
        this.listener = listener;
    }

    public interface ApplicationClickListener {
        void onApplicationClick(View view, int position);
    }

    public static class ApplicationViewHolder extends RecyclerView.ViewHolder {
        TextView appKeyLabel;
        public ApplicationViewHolder(View itemView) {
            super(itemView);
            appKeyLabel = itemView.findViewById(R.id.AppKeyLabelTextViewApplicationItem);
        }
    }
}
