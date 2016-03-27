package com.example.android.ots.controllers;


import com.example.android.ots.general.TaskItem;

import java.util.List;

public interface TaskUpdateListener {
    void onUpdate(List<TaskItem> list, int code);
}
