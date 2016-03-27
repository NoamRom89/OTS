package com.shenkar.android.ots.dao;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class TasksDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "tasks.db";

    public TasksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE "
                + TasksDbContract.TaskEntry.TABLE_NAME
                + " (" + TasksDbContract.TaskEntry._ID + " INTEGER PRIMARY KEY,"
                + TasksDbContract.TaskEntry.COLUMN_TASK_ASSIGNEE + " TEXT NOT NULL, "
                + TasksDbContract.TaskEntry.COLUMN_TASK_ACCEPT + " TEXT NOT NULL, "
                + TasksDbContract.TaskEntry.COLUMN_TASK_CATEGORY + " TEXT NOT NULL, "
                + TasksDbContract.TaskEntry.COLUMN_TASK_CREATED + " DATETIME NOT NULL, "
                + TasksDbContract.TaskEntry.COLUMN_TASK_DUETIME + " DATETIME NOT NULL, "
                + TasksDbContract.TaskEntry.COLUMN_TASK_IMAGEURL + " TEXT NOT NULL, "
                + TasksDbContract.TaskEntry.COLUMN_TASK_LOCATION + " TEXT NOT NULL, "
                + TasksDbContract.TaskEntry.COLUMN_TASK_NAME + " TEXT NOT NULL, "
                + TasksDbContract.TaskEntry.COLUMN_TASK_PRIORITY + " TEXT NOT NULL, "
                + TasksDbContract.TaskEntry.COLUMN_TASK_STATUS + " TEXT NOT NULL "
                + ")";
        db.execSQL(SQL_CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
