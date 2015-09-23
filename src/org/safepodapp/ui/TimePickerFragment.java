package org.safepodapp.ui;

import java.util.Calendar;

import org.safepodapp.SafepodAppApplication;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.widget.Toast;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
	private SharedPreferences sharedpreferences;
	private TimePickerDialog t;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		sharedpreferences = getActivity().getSharedPreferences(SafepodAppApplication.getSharedPreference(), Context.MODE_PRIVATE);
		// Use the current time as the default values for the picker
//		final Calendar c = Calendar.getInstance();
//		int hour = c.get(Calendar.HOUR_OF_DAY);
//		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		t = new TimePickerDialog(getActivity(), this, 
				sharedpreferences.getInt(SafepodAppApplication.getHourOfDay(), 11), 
				sharedpreferences.getInt(SafepodAppApplication.getMinute(), 0),
				DateFormat.is24HourFormat(getActivity()));
		return t;
	}
	
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		boolean validTime;
        if(hourOfDay < hour) {
            validTime = false;
        } 
        else if(hourOfDay == hour) {
            validTime = minute >= min;
        } 
        else if(hourOfDay == hour) {
            validTime = minute <= min;
        } 
        else { 
            validTime = true;
        } 
 
        if(validTime) {
			SharedPreferences.Editor editor = sharedpreferences.edit();
	        
	        editor.putInt(SafepodAppApplication.getHourOfDay(), hourOfDay);
	        editor.putInt(SafepodAppApplication.getMinute(), minute);
	        editor.commit();
        } else {
        	t.updateTime(Calendar.HOUR_OF_DAY, Calendar.MINUTE);
        	Toast.makeText(getActivity(), "Invalid time selected, please try again!", Toast.LENGTH_LONG).show();
        }
	}

    public static TimePickerFragment newInstance() {
    	return new TimePickerFragment();
    }

	//DIALOGFRAGMENT 
	//Create interface in your DialogFragment (or a new file) 
	public interface OnDismissListener { 
		void onDismiss(TimePickerFragment myDialogFragment);
	} 
	//create Pointer and setter to it 
	private OnDismissListener onDismissListener;
	public void setOnDissmissListener(OnDismissListener dissmissListener) {
		this.onDismissListener = dissmissListener; 
	} 
	//Call it on the dialogFragment onDissmiss 
	@Override 
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog); 
		if (onDismissListener != null) { 
			onDismissListener.onDismiss(this); 
		} 
	}
}