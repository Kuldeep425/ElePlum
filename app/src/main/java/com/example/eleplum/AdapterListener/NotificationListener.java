package com.example.eleplum.AdapterListener;

import com.example.eleplum.Models.CreatedTask;

public interface NotificationListener {
    void onAcceptClick(CreatedTask task);
    void onRejectClick(CreatedTask task);
}
