package com.shenkar.android.ots.activities;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

import com.shenkar.android.ots.R;

import java.util.Calendar;

/**
 * Created by tamiir on 17/01/2016.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it

        TimePickerDialog tpd = new TimePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK,this, hour, minute,DateFormat.is24HourFormat(getActivity()));

        return tpd;

    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        EditText t = (EditText) getActivity().findViewById(R.id.time_text);
        t.setText(String.valueOf(hourOfDay)+":"+String.valueOf(minute));
    }
}