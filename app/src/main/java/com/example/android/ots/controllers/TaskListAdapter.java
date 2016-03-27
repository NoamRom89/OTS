package com.example.android.ots.controllers;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.android.ots.R;
import com.example.android.ots.activities.EditTaskActivity;
import com.example.android.ots.activities.ReportTaskActivity;
import com.example.android.ots.general.Constants;
import com.example.android.ots.general.DoitException;
import com.example.android.ots.general.ItemClickListener;
import com.example.android.ots.general.LoginController;
import com.example.android.ots.general.TaskItem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * TaskListAdapter
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {
    private ITaskController controller;
    private List<TaskItem> taskItems;
    private Context context;
    private LoginController loginController;

    public TaskListAdapter(Context context) {
        this.taskItems = new ArrayList<>();
        this.context = context;
        this.loginController = new LoginController(context);
    }

    public void setController(ITaskController controller) {
        this.controller = controller;
    }

    @Override
    public TaskListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(context)
                .inflate(R.layout.task_list_view_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TaskListAdapter.ViewHolder holder, int position) {
        TaskItem item = taskItems.get(position);
        holder.mTvTaskName.setText(item.getTaskName());
        holder.mTvAssignee.setText(item.getAssignee());
        holder.mTvCategory.setText(item.getCategory());

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                TaskItem task = taskItems.get(position);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US);

                if (loginController.isAdmin()) {
                    Intent intent = new Intent(context, EditTaskActivity.class);
                    intent.putExtra(Constants.EDIT_TASK_ID, task.getId());
                    intent.putExtra(Constants.NEW_TASK_CATEGORY, task.getCategory());
                    intent.putExtra(Constants.NEW_TASK_PRIORITY, task.getPriority());
                    intent.putExtra(Constants.NEW_TASK_ASSIGNEE, task.getAssignee());
                    intent.putExtra(Constants.NEW_TASK_LOCATION, task.getLocation());
                    intent.putExtra(Constants.NEW_TASK_STATUS, task.getStatus());
                    intent.putExtra(Constants.NEW_TASK_ACCEPT, task.getAccept());
                    intent.putExtra(Constants.NEW_TASK_NAME, task.getTaskName());
                    intent.putExtra(Constants.NEW_TASK_DUE_DATE, task.getDueTime());
                    ((Activity) context).startActivityForResult(intent, Constants.REQUEST_CODE_UPDATE_TASK);
                } else {
                    Intent intent = new Intent(context, ReportTaskActivity.class);
                    intent.putExtra(Constants.EDIT_TASK_ID, task.getId());
                    intent.putExtra(Constants.NEW_TASK_CATEGORY, task.getCategory());
                    intent.putExtra(Constants.NEW_TASK_PRIORITY, task.getPriority());
                    intent.putExtra(Constants.NEW_TASK_ASSIGNEE, task.getAssignee());
                    intent.putExtra(Constants.NEW_TASK_LOCATION, task.getLocation());
                    intent.putExtra(Constants.NEW_TASK_STATUS, task.getStatus());
                    intent.putExtra(Constants.NEW_TASK_ACCEPT, task.getAccept());
                    intent.putExtra(Constants.NEW_TASK_NAME, task.getTaskName());

                    intent.putExtra(Constants.NEW_TASK_DUE_DATE, sdf.format(task.getDueTime()));

                    intent.putExtra(Constants.NEW_TASK_DUE_DATE, sdf.format(task.getDueTime().getTime()));

                    ((Activity) context).startActivityForResult(intent, Constants.REQUEST_CODE_REPORT_TASK);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskItems.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        //Each item is a view in the card.
        private TextView mTvTaskName;
        private TextView mTvAssignee;
        private TextView mTvCategory;
        private ItemClickListener clickListener;
        public ViewHolder(View parentView) {
            super(parentView);
            mTvTaskName = (TextView) parentView.findViewById(R.id.taskTitle);
            mTvAssignee = (TextView) parentView.findViewById(R.id.assignToTask);
            mTvCategory = (TextView) parentView.findViewById(R.id.taskCategory);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition(), false);

        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }
    }

    public void add(TaskItem task) throws DoitException{
        if (task == null) {
            throw new DoitException("error");
        }
        else {
            this.taskItems.add(task);
            notifyDataSetChanged();
        }
    }

    public void remove(TaskItem task) throws  DoitException {
        if (task == null) {
            throw new DoitException("error");
        }
        else {
            for (int i = 0; i < this.taskItems.size() ; i++ ) {
                if (this.taskItems.get(i).getId().equals(task.getId())) {
                    this.taskItems.remove(i);
                    break;
                }
            }
            notifyDataSetChanged();
        }
    }

    public void updateTask(TaskItem task) throws  DoitException {
        if (task == null) {
            throw new DoitException("error");
        }
        else {
            for (int i = 0; i < this.taskItems.size() ; i++ ) {
                if (this.taskItems.get(i).getId().equals(task.getId())) {
                    this.taskItems.set(i, task);
                    break;
                }
            }
            notifyDataSetChanged();
        }
    }

    public void updateList(List<TaskItem> data) {
        taskItems = data;
        notifyDataSetChanged();
    }

    public TaskItem getTask(String id) {
        for (int i = 0; i < this.taskItems.size() ; i++ ) {
            if (this.taskItems.get(i).getId().equals(id)) {
                return this.taskItems.get(i);
            }
        }
        return null;
    }


}