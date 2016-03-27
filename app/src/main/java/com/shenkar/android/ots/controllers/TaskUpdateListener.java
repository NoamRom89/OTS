package com.shenkar.android.ots.controllers;


import com.shenkar.android.ots.general.TaskItem;

import java.util.List;

public interface TaskUpdateListener {
    void onUpdate(List<TaskItem> list, int code);
}
