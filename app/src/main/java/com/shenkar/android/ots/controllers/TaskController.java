package com.shenkar.android.ots.controllers;
import android.content.Context;
import com.shenkar.android.ots.general.TaskItem;
import com.shenkar.android.ots.dao.DAO;
import com.shenkar.android.ots.dao.IDataAccess;
import com.parse.ParseFile;
import java.util.ArrayList;
import java.util.List;
public class TaskController implements ITaskController{

    private IDataAccess dao;
    private Context context;
    public TaskController(Context context)
    {
        dao = DAO.getInstance(context.getApplicationContext());
        dao.setTaskUpdateListener((TaskUpdateListener) context);
        this.context = context;
    }

    @Override
    public void getTasks(int orderby) {
        dao.getTasks(orderby);
    }

    @Override
    public void getTaskById(String id) {
        dao.getTaskById(id);
    }

    @Override
    public void getWaitingTasks() {
         dao.getWaitingTasks();
    }

    @Override
    public void getTasksByAssignee(String assignee,int orderby) {
         dao.getTasksForMember(assignee, orderby);
    }

    @Override
    public void getWaitingTasksByAssignee(String assignee) {
         dao.getWaitingTasksForMember(assignee);
    }

    @Override
    public void updateTask(TaskItem task) {
        dao.updateTask(task);
    }
    public void updateTask(TaskItem task,ParseFile file) {
        dao.updateTask(task,file);
    }

    @Override
    public void removeTask(TaskItem task) {
         dao.removeTask(task);
    }

    @Override
    public void addTask(TaskItem task) {
         dao.addTask(task);
    }

    @Override
    public List<TaskItem> checkForNewTasks() {
        List<TaskItem> list = new ArrayList<TaskItem>();
        return list;

    }
}
