package com.shenkar.android.ots.dao;

import android.provider.BaseColumns;

public class TasksDbContract {
    public static final class TaskEntry implements BaseColumns {

        public static final String TABLE_NAME = "tasks";

        public static final String COLUMN_TASK_CREATED = "task_created";

        public static final String COLUMN_TASK_CATEGORY = "task_category";

        public static final String COLUMN_TASK_PRIORITY = "task_priority";

        public static final String COLUMN_TASK_LOCATION = "task_location";

        public static final String COLUMN_TASK_DUETIME = "task_dueTime";

        public static final String COLUMN_TASK_ASSIGNEE = "task_assignee";

        public static final String COLUMN_TASK_STATUS = "task_status";

        public static final String COLUMN_TASK_IMAGEURL = "task_imageUrl";

        public static final String COLUMN_TASK_NAME = "task_name";

        public static final String COLUMN_TASK_ACCEPT = "task_accept";

    }
}
