package com.example.android.ots.activities;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;
import com.example.android.ots.R;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, year, month, day);
        return dpd;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        EditText d = (EditText) getActivity().findViewById(R.id.date_text);
        d.setText(String.valueOf(day) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year));
    }
}
