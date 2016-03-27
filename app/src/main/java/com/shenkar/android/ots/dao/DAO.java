package com.shenkar.android.ots.dao;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.shenkar.android.ots.controllers.LoginListener;
import com.shenkar.android.ots.controllers.TaskUpdateListener;
import com.shenkar.android.ots.general.Constants;
import com.shenkar.android.ots.general.TaskItem;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Class DAO
 */
public class DAO implements IDataAccess {


    private static DAO instance;
    private Context context;
    private ArrayList<TaskUpdateListener> listeners = new ArrayList<TaskUpdateListener>();
    private LoginListener loginListener;

    private DAO(Context context) {
        this.context = context;
    }

    public static DAO getInstance(Context applicationContext) {
        if (instance == null)
            instance = new DAO(applicationContext);
        return instance;
    }

    @Override
    public void updateTask(final TaskItem task) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.getInBackground(task.getId(), new GetCallback<ParseObject>() {
            public void done(final ParseObject po, ParseException e) {
                if (e == null) {
                    taskToParseObject(po, task);
                    po.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            task.setId(po.getObjectId());
                            List<TaskItem> list = new ArrayList<TaskItem>();
                            list.add(task);
                            updateListeners(list, Constants.TASK_UPDATE_LISTENER_CODE_ALL_UPDATE);
                        }
                    });
                }
            }
        });

    }
    @Override
    public void updateTask(final TaskItem task,final ParseFile file) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.getInBackground(task.getId(), new GetCallback<ParseObject>() {
            public void done(final ParseObject po, ParseException e) {
                if (e == null) {
                    taskToParseObject(po, task);
                    po.put("image", file);
                    po.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            task.setId(po.getObjectId());
                            List<TaskItem> list = new ArrayList<TaskItem>();
                            list.add(task);
                            updateListeners(list, Constants.TASK_UPDATE_LISTENER_CODE_ALL_UPDATE);
                        }
                    });
                }
            }
        });

    }

    @Override
    public void removeTask(TaskItem task) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.getInBackground(task.getId(), new GetCallback<ParseObject>() {
            public void done(final ParseObject po, ParseException e) {
                if (e == null) {
                    po.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            TaskItem t = new TaskItem();
                            t.setId(po.getObjectId());
                            List<TaskItem> list = new ArrayList<TaskItem>();
                            list.add(t);
                            updateListeners(list, Constants.TASK_UPDATE_LISTENER_CODE_ALL_DELETE);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void addTask(final TaskItem task) {
        Log.i("addtask", "got to add Task");
        final ParseObject po = new ParseObject("Task");
        taskToParseObject(po, task);
        po.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i("addtask", "done");
                task.setId(po.getObjectId());
                List<TaskItem> list = new ArrayList<TaskItem>();
                list.add(task);
                updateListeners(list, Constants.TASK_UPDATE_LISTENER_CODE_ALL_ADD);
            }
        });
    }

    @Override
    public void getTasks(int orderbyColumn) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.orderByDescending(getOrderByColumnString(orderbyColumn));
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> taskList, ParseException e) {
                if (e == null) {

                    List<TaskItem> tasks = new ArrayList<>();

                    for (int i = 0; i < taskList.size(); i++) {
                        TaskItem task = parseObjectToTask(taskList.get(i));
                        tasks.add(task);
                    }
                    updateListeners(tasks, Constants.TASK_UPDATE_LISTENER_CODE_ALL_ITEMS);

                } else {

                }
            }
        });
    }

    @Override
    public void getWaitingTasks() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.orderByDescending(getOrderByColumnString(0));
        query.whereEqualTo("status", "waiting");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> taskList, ParseException e) {
                if (e == null) {

                    List<TaskItem> tasks = new ArrayList<>();

                    for (int i = 0; i < taskList.size(); i++) {
                        TaskItem task = parseObjectToTask(taskList.get(i));
                        tasks.add(task);
                    }
                    updateListeners(tasks, Constants.TASK_UPDATE_LISTENER_CODE_ALL_ITEMS);

                } else {

                }
            }
        });
    }

    @Override
    public void getTasksForMember(String member, int orderbyColumn) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.orderByDescending(getOrderByColumnString(orderbyColumn));
        query.whereEqualTo("assignee", member);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> taskList, ParseException e) {
                if (e == null) {

                    List<TaskItem> tasks = new ArrayList<>();

                    for (int i = 0; i < taskList.size(); i++) {
                        TaskItem task = parseObjectToTask(taskList.get(i));
                        tasks.add(task);
                    }
                    updateListeners(tasks, Constants.TASK_UPDATE_LISTENER_CODE_ALL_ITEMS);

                } else {

                }
            }
        });
    }

    @Override
    public void getWaitingTasksForMember(String member) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.orderByDescending(getOrderByColumnString(0));
        query.whereEqualTo("status", "waiting");
        query.whereEqualTo("assignee", member);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> taskList, ParseException e) {
                if (e == null) {

                    List<TaskItem> tasks = new ArrayList<>();

                    for (int i = 0; i < taskList.size(); i++) {
                        TaskItem task = parseObjectToTask(taskList.get(i));
                        tasks.add(task);
                    }
                    updateListeners(tasks, Constants.TASK_UPDATE_LISTENER_CODE_ALL_ITEMS);

                } else {

                }
            }
        });
    }


    private TaskItem cursorToTask(Cursor cursor) {
        return new TaskItem();
    }

    private TaskItem parseObjectToTask(ParseObject object) {
        TaskItem t = new TaskItem();
        t.setId(object.getObjectId());
        t.setAccept(object.getString("accept"));
        t.setCategory(object.getString("category"));
        t.setAssignee(object.getString("assignee"));
        t.setDueTime(object.getDate("dueTime"));
        t.setLocation(object.getString("location"));
        t.setPriority(object.getInt("priority"));
        t.setStatus(object.getString("status"));
        t.setTaskName(object.getString("name"));

        return t;
    }

    private void taskToParseObject(ParseObject po, TaskItem task) {
        Log.e("taks object", task.toString());
        if (task.getAccept() != null ) {
            po.put("accept", task.getAccept());
        }
        if (task.getAssignee() != null) {
            po.put("assignee", task.getAssignee());
        }
        if (task.getCategory() != null) {

            po.put("category", task.getCategory());
        }
        if (task.getDueTime() != null) {

            po.put("dueTime", task.getDueTime());
        }
        if (task.getLocation() != null ) {

            po.put("location", task.getLocation());
        }
        if (task.getStatus() != null) {
            po.put("status", task.getStatus());
        }

        po.put("priority", task.getPriority());

        if (task.getTaskName() != null) {

            po.put("name", task.getTaskName());
        }

    }

    private String getOrderByColumnString(int orderbyCode) {
        switch (orderbyCode) {
            case 1:
                return "priority";
            case 2:
                return "status";
            default:
                return "dueTime";
        }
    }

    public void setTaskUpdateListener(TaskUpdateListener listener) {
        this.listeners.add(listener);
    }

    public void updateListeners(List<TaskItem> taskItems, int code) {
        for (TaskUpdateListener listener : this.listeners) {
            listener.onUpdate(taskItems, code);
        }
    }

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }

    public void updateLoginListener(int code) {
        loginListener.onUpdate(code);
    }

    public void login(String username, String password) {

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Log.e("user test", "user logged in");
                    updateLoginListener(Constants.USER_LOGGED_IN);
                } else {
                    Log.e("user test", "user logged in failed");
                    updateLoginListener(Constants.USER_LOGGED_IN_FAILED);
                }
            }
        });
    }

    @Override
    public void getTaskById(String id) {

    }

}
