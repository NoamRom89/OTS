package com.example.android.ots.activities;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.android.ots.R;
import com.example.android.ots.general.Constants;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ReportTaskActivity extends AppCompatActivity implements OnItemSelectedListener {

    private String acceptStatus = "waiting";
    private String status = "waiting";
    private Intent intent;
    private String taskId = "";
    private Bitmap bitmap;
    private ImageView imageView;
    private byte[] bytes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_task);

        imageView  = (ImageView) findViewById(R.id.viewImageFromButton);

        intent = getIntent();

        /*
        status spinner initialization
         */

        Spinner acceptStatusSpinner = (Spinner) findViewById(R.id.acceptStatus);

        // Spinner click listener
        acceptStatusSpinner.setOnItemSelectedListener(this);

        //add status options
        List<String> acceptStatusOption = new ArrayList<String>();
        acceptStatusOption.add("waiting");
        acceptStatusOption.add("accept");
        acceptStatusOption.add("reject");

        // Creating adapter for spinner
        ArrayAdapter<String> acceptStatusDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, acceptStatusOption);

        // Drop down layout style - list view with radio button
        acceptStatusDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        acceptStatusSpinner.setAdapter(acceptStatusDataAdapter);

        //set selection according to task
        String acceptValue = intent.getStringExtra(Constants.NEW_TASK_ACCEPT);

        int spinnerPosition = acceptStatusDataAdapter.getPosition(acceptValue);
        acceptStatusSpinner.setSelection(spinnerPosition);

        showStatus(acceptValue);

        /*
        status spinner initialization
         */

        Spinner statusSpinner = (Spinner) findViewById(R.id.acceptStatusAnswear);

        // Spinner click listener
        statusSpinner.setOnItemSelectedListener(this);

        //add status options
        List<String> statusOption = new ArrayList<String>();
        statusOption.add("waiting");
        statusOption.add("in progress");
        statusOption.add("done");

        // Creating adapter for spinner
        ArrayAdapter<String> statusDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusOption);

        // Drop down layout style - list view with radio button
        statusDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        statusSpinner.setAdapter(statusDataAdapter);

        //set selection according to task
        String statusValue = intent.getStringExtra(Constants.NEW_TASK_STATUS);


        Log.e("status",statusValue);


        showAddPhoto(statusValue);

        /**
         * add the task info
         */

        TextView categoryTV = (TextView) findViewById(R.id.taskCategoryInfoVal);
        TextView priorityTV = (TextView) findViewById(R.id.taskPrioVal);
        TextView locationTV = (TextView) findViewById(R.id.taskLocationVal);
        TextView dueDateTV = (TextView) findViewById(R.id.taskDueTimeVal);

        categoryTV.setText(intent.getStringExtra(Constants.NEW_TASK_CATEGORY));
        priorityTV.setText(priorityToString(intent.getIntExtra(Constants.NEW_TASK_PRIORITY, 1)));
        locationTV.setText(intent.getStringExtra(Constants.NEW_TASK_LOCATION));
        dueDateTV.setText(intent.getStringExtra(Constants.NEW_TASK_DUE_DATE));

        taskId = intent.getStringExtra(Constants.EDIT_TASK_ID);



        /*
        update image
         */
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.getInBackground(taskId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    ParseFile image = (ParseFile) object.get("image");
                    if (null != image) {
                        image.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                if (e == null) {
                                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    imageView.setImageBitmap(bitmap);
                                }
                            }
                        });
                    }
                } else {
                    // something went wrong
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        String item = parent.getItemAtPosition(position).toString();

        //if spinner is accept status spinner
        if (spinner.getId() == R.id.acceptStatus) {
            acceptStatus = item;
            showStatus(item);
        }

        //if spinner is status spinner
        if (spinner.getId() == R.id.acceptStatusAnswear) {
            status = item;
            showAddPhoto(item);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showStatus(String status) {
        LinearLayout statusLayout = (LinearLayout) findViewById(R.id.acceptStatusVal);
        if (status.equals("accept")) {
            statusLayout.setVisibility(View.VISIBLE);
        }
        else {
            statusLayout.setVisibility(View.GONE);
        }
    }

    public void showAddPhoto(String status) {
        LinearLayout photoLayout = (LinearLayout) findViewById(R.id.addPhoto);
        if (status.equals("done")) {
            photoLayout.setVisibility(View.VISIBLE);
        }
        else {
            photoLayout.setVisibility(View.GONE);
        }
    }

//    public void addImageClicked(View view) {
//        // create intent with ACTION_IMAGE_CAPTURE action
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        // start camera activity
//        startActivityForResult(intent, Constants.REQUEST_CODE_ADD_IMAGE);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_CODE_ADD_IMAGE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();

            // get bitmap
            bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            bytes = stream.toByteArray();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void saveClicked(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.NEW_TASK_ACCEPT,acceptStatus);
        returnIntent.putExtra(Constants.NEW_TASK_STATUS,status);
        returnIntent.putExtra(Constants.EDIT_TASK_ID,taskId);
        returnIntent.putExtra(Constants.NEW_TASK_IMAGE_BYTES,bytes);
        setResult(RESULT_OK, returnIntent);
        finish();

    }

    public String priorityToString(int priority) {
        switch (priority) {
            case 1:
                return "low";
            case 2:
                return "normal";
            case 3:
                return "urgent";
            default:
                return "normal";
        }
    }
}
