package com.example.android.ots.activities;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.ots.R;
import com.example.android.ots.general.Constants;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditTaskActivity extends AppCompatActivity {

    private String name;
    private Date date = new Date();
    private Calendar calendar = Calendar.getInstance();
    private int priority;
    private String category;
    private String room;
    private String assign;
    private String taskId;
    private Bitmap bitmap;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Intent intent = getIntent();

        category = intent.getStringExtra(Constants.NEW_TASK_CATEGORY);
        priority = intent.getIntExtra(Constants.NEW_TASK_PRIORITY, 0);
        room = intent.getStringExtra(Constants.NEW_TASK_LOCATION);
        name = intent.getStringExtra(Constants.NEW_TASK_NAME);
        taskId = intent.getStringExtra(Constants.EDIT_TASK_ID);
        assign = intent.getStringExtra(Constants.NEW_TASK_ASSIGNEE);

        RadioGroup temp2 = (RadioGroup) findViewById(R.id.priority_radio_group);
        RadioButton radioButton = (RadioButton) temp2.getChildAt(priority);
        radioButton.setChecked(true);


        final String[] categories = {"Cleaning", "Electricity", "Computers", "General", "Other"};
        ArrayAdapter<String> stringArrayAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        Spinner spinner = (Spinner)  findViewById(R.id.task_category_spinner);
        spinner.setAdapter(stringArrayAdapter);


        final Spinner users = (Spinner) findViewById(R.id.person_name_spinner);

        ParseQuery<ParseUser> query = ParseUser.getQuery();

        //query.whereEqualTo("Boy", tf3.getText().toString());   //This is to filter things!
        //query.selectKeys(Arrays.asList("username"));
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {

                    int len = objects.size();
                    String[] list1Strings22 = new String[len];
                    // list1Strings22[0] = "test";

                    for (int i = 0; i < len; i++) {
                        list1Strings22[i] = objects.get(i).getString("username");
                    }


                    ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(EditTaskActivity.this, android.R.layout.simple_spinner_dropdown_item, list1Strings22);
                    users.setAdapter(stringArrayAdapter);
                    int spinnerPosition = stringArrayAdapter.getPosition(assign);
                    users.setSelection(spinnerPosition);

                } else {

                }
            }

        });


       date = (Date) intent.getSerializableExtra(Constants.NEW_TASK_DUE_DATE);



        EditText temp = (EditText) findViewById(R.id.task_name_text);
        temp.setText(name, TextView.BufferType.EDITABLE);

        Spinner temp1 = (Spinner) findViewById(R.id.task_category_spinner);
        ArrayAdapter myAdap = (ArrayAdapter) temp1.getAdapter();
        int spinnerPosition = myAdap.getPosition(category);
        temp1.setSelection(spinnerPosition);




        temp = (EditText) findViewById(R.id.task_room_num);
        temp.setText(room, TextView.BufferType.EDITABLE);

        if(name == null){
            date = new Date();
            LinearLayout LS = (LinearLayout) findViewById(R.id.edit_task_status_container);
            LS.setVisibility(LinearLayout.GONE);
        }



        String strCurrentDate = date.toString();
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM d k:m:s ZZZZ y");
        Date newDate = null;
        try {
            newDate = format.parse(strCurrentDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("k:m");
        String sTime = format.format(newDate);
        format = new SimpleDateFormat("d/MM/y");
        String sDate = format.format(newDate);








        temp = (EditText) findViewById(R.id.time_text);
        temp.setText(sTime);

        temp = (EditText) findViewById(R.id.date_text);
        temp.setText(sDate);


        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Task");
        query1.getInBackground(taskId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    ParseFile image = (ParseFile) object.get("image");
                    if (null != image) {
                        image.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                if (e == null) {
                                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    LinearLayout LS = (LinearLayout) findViewById(R.id.edit_task_status_container);
                                    LS.setVisibility(LinearLayout.VISIBLE);
                                    imageView = (ImageView) findViewById(R.id.edit_task_image_view);
                                    imageView.setImageBitmap(bitmap);
                                }
                            }
                        });
                    }
                    LinearLayout LS = (LinearLayout) findViewById(R.id.edit_task_status_container);
                    LS.setVisibility(LinearLayout.GONE);
                   
                } else {

                    // something went wrong
                }
            }
        });
    }






    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    public void onCancel(View v){
        finish();
    }
    public void onSave(View v) {

        EditText tmp = (EditText) findViewById(R.id.task_name_text);//task name
        name = tmp.getText().toString();
        RadioGroup rg1 = (RadioGroup) findViewById(R.id.priority_radio_group);//priority
        if (rg1.getCheckedRadioButtonId() != -1) {
            int id = rg1.getCheckedRadioButtonId();
            View radioButton = rg1.findViewById(id);
            int radioId = rg1.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) rg1.getChildAt(radioId);
            priority =  radioId;
        }
        Spinner sp = (Spinner) findViewById(R.id.task_category_spinner);
        category = sp.getSelectedItem().toString(); //category


        EditText nm = (EditText)  findViewById(R.id.task_room_num); //room
        room  = nm.getText().toString();

        EditText time = (EditText) findViewById(R.id.time_text); // time
        String[] time1 = time.getText().toString().split(":");
        this.calendar.set(calendar.HOUR_OF_DAY, Integer.parseInt(time1[0]));
        this.calendar.set(calendar.MINUTE , Integer.parseInt(time1[1]));

        time = (EditText) findViewById(R.id.date_text); //date
        String[] date1 = time.getText().toString().split("/");
        this.calendar.set(calendar.DATE, Integer.parseInt(date1[0]));
        this.calendar.set(calendar.MONTH, Integer.parseInt(date1[1])-1);
        this.calendar.set(calendar.YEAR, Integer.parseInt(date1[2]));
        date = calendar.getTime();





        Spinner spp = (Spinner) findViewById(R.id.person_name_spinner);
        assign = spp.getSelectedItem().toString();

        Intent intent = new Intent(this, WaitingTasksActivity.class);
        intent.putExtra(Constants.NEW_TASK_NAME,name);
        intent.putExtra(Constants.NEW_TASK_CATEGORY,category);
        intent.putExtra(Constants.NEW_TASK_LOCATION,room);
        intent.putExtra(Constants.NEW_TASK_PRIORITY,priority);
        intent.putExtra(Constants.NEW_TASK_DUE_DATE, date);
        intent.putExtra(Constants.NEW_TASK_ASSIGNEE, assign);
        intent.putExtra(Constants.EDIT_TASK_ID,taskId);
        setResult(RESULT_OK, intent);
        finish();
    }
}
