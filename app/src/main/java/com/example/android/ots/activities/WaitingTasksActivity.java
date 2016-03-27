package com.example.android.ots.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.widget.Spinner;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import com.example.android.ots.controllers.TaskController;
import com.example.android.ots.controllers.ITaskController;
import com.example.android.ots.controllers.LoginListener;
import com.example.android.ots.controllers.TaskListAdapter;
import com.example.android.ots.controllers.TaskUpdateListener;
import com.example.android.ots.general.Constants;
import com.example.android.ots.R;
import com.example.android.ots.general.DoitException;
import java.util.Date;
import com.example.android.ots.general.LoginController;
import com.example.android.ots.general.TaskItem;
import com.example.android.ots.general.Toolbar;
import com.parse.SaveCallback;
import com.example.android.ots.dialogs.NewTasksDialogFragment;
import com.parse.ParseException;
import com.parse.ParseFile;
import java.util.List;

public class WaitingTasksActivity extends Toolbar implements LoginListener ,TaskUpdateListener ,SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener, NewTasksDialogFragment.NewTaskDialogListener
{

    private static final String TAG = WaitingTasksActivity.class.getName();
    private ITaskController controller;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TaskListAdapter mAdapter;
    private LoginController loginController;
    private String currentTab = "waiting";
    private int currentSortByPosition = 0;
    private String newTaskDialogReturned;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_waiting_task);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_waiting_list);
        swipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.task_list_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new TaskListAdapter(this);

        controller = new TaskController(this);
        loginController = new LoginController(this);

        if (loginController.isAdmin()) {
            controller.getWaitingTasks();
        } else {
            controller.getWaitingTasksByAssignee(loginController.getUserName());
            //hide add new task button
            Button add_task_button = (Button) findViewById(R.id.add_task_button);
            add_task_button.setVisibility(View.INVISIBLE);
        }

        mRecyclerView.setAdapter(mAdapter);

        updateTasksCount();

        Spinner spinner = (Spinner) findViewById(R.id.taskSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.arrange_by, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        LinearLayout spinner_layout = (LinearLayout) findViewById(R.id.tasksFilter);
        spinner_layout.setVisibility(View.GONE);
    }


    /**
     * when click on all task this function will start
     * @param view
     */
    public void moveToAllTasks(View view) {
        if (currentTab.equals("waiting")) {
            currentTab = "all";
            if (loginController.isAdmin()) {
                controller.getTasks(0);
            } else {
                controller.getTasksByAssignee(loginController.getUserName(), 0);
            }
            Button button = (Button) findViewById(R.id.allTasksTab);
            button.setTextColor(Color.WHITE);
            Button button2 = (Button) findViewById(R.id.waitListTab);
            button2.setTextColor(Color.BLACK);
            updateTasksCount();
            LinearLayout spinner_layout = (LinearLayout) findViewById(R.id.tasksFilter);
            spinner_layout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * when click on waiting task tab this function will start
     * @param view
     */
    public void moveToWaitingTasks(View view) {
        if (currentTab.equals("all")) {
            currentTab = "waiting";
            if (loginController.isAdmin()) {
                controller.getWaitingTasks();
            } else {
                controller.getWaitingTasksByAssignee(loginController.getUserName());
            }
            Button button = (Button) findViewById(R.id.allTasksTab);
            button.setTextColor(Color.BLACK);
            Button button2 = (Button) findViewById(R.id.waitListTab);
            button2.setTextColor(Color.WHITE);

            LinearLayout spinner_layout = (LinearLayout) findViewById(R.id.tasksFilter);
            spinner_layout.setVisibility(View.GONE);
        }
    }

    /**
     * update the task counter text
     */
    public void updateTasksCount() {
        //set the number of tasks
        int tasksCount = mAdapter.getItemCount();
        TextView taskCount = (TextView) findViewById(R.id.waitListCounter);
        taskCount.setText(Integer.toString(tasksCount));
    }

    /**
     * start the activity for add new task when the add task button is clicked
     * @param view
     */
    public void addNewTaskButtonClicked(View view) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_ADD_NEW_TASK);
    }

    /**
     * when activity is returned with data
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //if returning from adding new task
        if (requestCode == Constants.REQUEST_CODE_ADD_NEW_TASK && resultCode == Activity.RESULT_OK) {
            String category = data.getStringExtra(Constants.NEW_TASK_CATEGORY);
            int priority = data.getIntExtra(Constants.NEW_TASK_PRIORITY, 1);
            String assignee = data.getStringExtra(Constants.NEW_TASK_ASSIGNEE);
            String location = data.getStringExtra(Constants.NEW_TASK_LOCATION);
            String status = "waiting";
            String accept = "waiting";
            String name = data.getStringExtra(Constants.NEW_TASK_NAME);
            Date dueDate = (Date) data.getSerializableExtra(Constants.NEW_TASK_DUE_DATE);

            TaskItem task = new TaskItem(category, priority, location, dueDate, assignee, status, accept, name);
            controller.addTask(task);
            try {
               // mAdapter.add(task);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        else if (requestCode == Constants.REQUEST_CODE_UPDATE_TASK  && resultCode == Activity.RESULT_OK) {
            String category = data.getStringExtra(Constants.NEW_TASK_CATEGORY);
            int priority = data.getIntExtra(Constants.NEW_TASK_PRIORITY, 1);
            String assignee = data.getStringExtra(Constants.NEW_TASK_ASSIGNEE);
            String location = data.getStringExtra(Constants.NEW_TASK_LOCATION);
            String status = "waiting";
            String accept = "waiting";
            String name = data.getStringExtra(Constants.NEW_TASK_NAME);
            String TaskId = data.getStringExtra(Constants.EDIT_TASK_ID);
            Date dueDate = (Date) data.getSerializableExtra(Constants.NEW_TASK_DUE_DATE);

            TaskItem task = new TaskItem(category, priority, location, dueDate, assignee, status, accept, name);
            task.setId(TaskId);
            controller.updateTask(task);
        }

        else if (requestCode == Constants.REQUEST_CODE_REPORT_TASK && resultCode == Activity.RESULT_OK) {
            String status = data.getStringExtra(Constants.NEW_TASK_STATUS);
            String accept = data.getStringExtra(Constants.NEW_TASK_ACCEPT);
            String TaskId = data.getStringExtra(Constants.EDIT_TASK_ID);
            byte[] imageBytes = data.getByteArrayExtra(Constants.NEW_TASK_IMAGE_BYTES);

            final TaskItem task = mAdapter.getTask(TaskId);
            task.setStatus(status);
            task.setAccept(accept);

            if (imageBytes != null) {

                final ParseFile file = new ParseFile(task.getTaskName() + ".jpg" , imageBytes);

                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            controller.updateTask(task,file);
                        }
                    }
                });

            }

            controller.updateTask(task);

            if (accept.equals("accept")) {

                Toast.makeText(this,"Task accepted and task is " + status ,Toast.LENGTH_LONG).show();
            }
            if (accept.equals("reject")) {
                Toast.makeText(this,"Task rejected",Toast.LENGTH_LONG).show();

            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    public void onUpdate(int code) {

    }

    /**
     * when an item from the spinner is selected
     * @param parent
     * @param view
     * @param pos
     * @param id
     */
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        if (currentSortByPosition != pos) {
            if (loginController.isAdmin()) {
                controller.getTasks(pos);
            } else {
                controller.getTasksByAssignee(loginController.getUserName(),pos);
            }
            currentSortByPosition = pos;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        if(currentTab.equals("all")){
            if (loginController.isAdmin()) {
                controller.getTasks(0);
            } else {
                controller.getTasksByAssignee(loginController.getUserName(), 0);
            }
        }else{
            if (loginController.isAdmin()) {
                controller.getWaitingTasks();
            } else {
                controller.getWaitingTasksByAssignee(loginController.getUserName());
            }
        }
        Toast.makeText(this,"The list has been updated!", Toast.LENGTH_LONG).show();
    }

    public void refreshListButtonClicked(View view) {
        refreshList();
    }

    /**
     * This function handle asynchronous calls from the server - in every update
     */
    @Override
    public void onUpdate(List<TaskItem> tasks,int code) {
        if(tasks.size()==0)
            ((TextView)findViewById(R.id.emptyTask)).setVisibility(View.VISIBLE);
        else
            ((TextView)findViewById(R.id.emptyTask)).setVisibility(View.GONE);


        if (code == Constants.TASK_UPDATE_LISTENER_CODE_ALL_ITEMS) {
            mAdapter.updateList(tasks);
            updateTasksCount();
        } else if (code == Constants.TASK_UPDATE_LISTENER_CODE_ALL_UPDATE) {
            try {
                mAdapter.updateTask(tasks.get(0));

            } catch (DoitException e) {
                e.printStackTrace();
            }
        } else if (code == Constants.TASK_UPDATE_LISTENER_CODE_ALL_DELETE) {
            try {
                mAdapter.remove(tasks.get(0));
            } catch (DoitException e) {
                e.printStackTrace();
            }
        } else if (code == Constants.TASK_UPDATE_LISTENER_CODE_ALL_ADD) {
            try {
                mAdapter.add(tasks.get(0));
            } catch (DoitException e) {
                e.printStackTrace();
            }
        }

        refreshList();

    }

    /**
     * updates the tasks list
     */
    public void refreshList() {
        swipeRefreshLayout.setRefreshing(true);

        if (currentTab.equals("all")) {
            moveToAllTasks(null);
        }
        else {
            moveToWaitingTasks(null);
        }



        swipeRefreshLayout.setRefreshing(false);
    }
}



