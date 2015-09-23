package org.safepodapp.ui;

import java.util.Calendar;

import org.safepodapp.SafepodAppApplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	private SharedPreferences sharedpreferences;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		sharedpreferences = getActivity().getSharedPreferences(SafepodAppApplication.getSharedPreference(), Context.MODE_PRIVATE);
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		// Create a new instance of DatePickerDialog and return it
		DatePickerDialog d = new DatePickerDialog(getActivity(), this, year, month, day);
		d.getDatePicker().setMinDate(c.getTimeInMillis());
		return d;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		SharedPreferences.Editor editor = sharedpreferences.edit();
        
        editor.putInt(SafepodAppApplication.getYear(), year);
        editor.putInt(SafepodAppApplication.getMonth(), month);
        editor.putInt(SafepodAppApplication.getDayOfMonth(), day);
        editor.commit();
	}

    public static DatePickerFragment newInstance() {
    	return new DatePickerFragment();
    }
}