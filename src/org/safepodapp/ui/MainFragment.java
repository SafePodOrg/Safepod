package org.safepodapp.ui;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.safepodapp.R;
import org.safepodapp.util.AlarmReciever;
import org.safepodapp.SafepodAppApplication;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainFragment extends Fragment implements OnClickListener {
	private SharedPreferences sharedpreferences;
	private TextView mTextClockView;
    private Button setTimeButton;
	private TextView mLocationTextView;
    private Button goingNonHomeLocButton;
//	private EditText searchAddressEditText;
    private Button okayButton;
	
    public MainFragment() {
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		sharedpreferences = getActivity().getSharedPreferences(SafepodAppApplication.getSharedPreference(), Context.MODE_PRIVATE);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        
        mTextClockView = (TextView) rootView.findViewById(R.id.textClockView);
        setClock();
        
        mLocationTextView = (TextView) rootView.findViewById(R.id.locationTextView);
        mLocationTextView.setText(sharedpreferences.getString(SafepodAppApplication.getUserHomeAddress(), "Home"));
        
        setTimeButton = (Button) rootView.findViewById(R.id.date_time_set_button);
        setTimeButton.setOnClickListener(this);
        
        okayButton = (Button) rootView.findViewById(R.id.okay_button);
        okayButton.setOnClickListener(this);
        okayButton.requestFocus();
        
        goingNonHomeLocButton = (Button) rootView.findViewById(R.id.going_non_home_loc_button);
        goingNonHomeLocButton.setOnClickListener(this);
        
//        searchAddressEditText = (EditText) rootView.findViewById(R.id.search_address_edit_text);
//        searchAddressEditText.setOnClickListener(this);
//        searchAddressEditText.setEnabled(false);
//        searchAddressEditText.setVisibility(View.GONE);
        
        return rootView;
    }

	private void setClock() {
        int min = sharedpreferences.getInt(SafepodAppApplication.getMinute(), 0);
        if(min < 10) {
        	mTextClockView.setText(
        		Integer.toString(sharedpreferences.getInt(SafepodAppApplication.getHourOfDay(), 0))
        		+":0"
        		+Integer.toString(min));
        } else {
        	mTextClockView.setText(
        		Integer.toString(sharedpreferences.getInt(SafepodAppApplication.getHourOfDay(), 0))
        		+":"
        		+Integer.toString(min));
        }

	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.date_time_set_button: {
				dateTimePick(v);
				break;
			}
			case R.id.okay_button: {
				setUpAlertSystems(v);
				if(!isPinSet())
					setPin();
				if(!isHomeSet())
					setHome();
				break;
			}
			case R.id.going_non_home_loc_button: {
				searchAddressOnMaps();
//		        searchAddressEditText.setEnabled(true);
//		        searchAddressEditText.setVisibility(View.VISIBLE);
//		        searchAddressEditText.requestFocus();
				break;
			}
			default: {
				break;
			}
		}
	}

	private void setHome() {
		Intent setHomeLocationActivity = new Intent(getActivity(), SetHomeLocationActivity.class);
		setHomeLocationActivity.putExtra("addressSetActivityHeader", "Set Home Address");
		startActivity(setHomeLocationActivity);
	}

	private boolean isHomeSet() {
		if(sharedpreferences.getString(SafepodAppApplication.getUserHomeAddress(), "empty").equals("empty"))
			return false;
		return true;
	}

	private void setPin() {
		Intent firstTimeInstallActivityIntent = new Intent(getActivity(), ChooseYourPinActivity.class);
		startActivity(firstTimeInstallActivityIntent);
	}

	private boolean isPinSet() {
		if(sharedpreferences.getString(SafepodAppApplication.getUserPin(), "90210").equals("90210"))
			return false;
		return true;
	}

	private void searchAddressOnMaps() {
		Intent setNewLocationInSetHomeLocationActivityIntent = new Intent(getActivity(), SetHomeLocationActivity.class);
		setNewLocationInSetHomeLocationActivityIntent.putExtra("addressSetActivityHeader", "Set New Address");
		startActivity(setNewLocationInSetHomeLocationActivityIntent);
	}

	private void setUpAlertSystems(View v) {
		scheduleAlarm(v);
		Intent modifyTripActivityIntent = new Intent(getActivity(), TripActivity.class);
		startActivity(modifyTripActivityIntent);
	}
	
	public void scheduleAlarm(View V) {
		int year = sharedpreferences.getInt(SafepodAppApplication.getYear(), 1970);
		int month = sharedpreferences.getInt(SafepodAppApplication.getMonth(), 1);
		int day = sharedpreferences.getInt(SafepodAppApplication.getDayOfMonth(), 1);
		int hour = sharedpreferences.getInt(SafepodAppApplication.getHourOfDay(), 0);
		int minute = sharedpreferences.getInt(SafepodAppApplication.getMinute(), 0); 

	    Calendar customCalendar = new GregorianCalendar();
	    customCalendar.set(year, month, day, hour, minute);
	 
//	    Date customDate = customCalendar.getTime();
	 
		// create an Intent and set the class which will execute when Alarm triggers, here we have
		// given AlarmReciever in the Intent, the onRecieve() method of this class will execute when
		// alarm triggers and 
		//we will write the code to send SMS inside onRecieve() method pf Alarmreciever class
		Intent intentAlarm = new Intent(getActivity(), AlarmReciever.class);
	
		// create the object
		AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
	
		//set the alarm for particular time
		alarmManager.set(AlarmManager.RTC_WAKEUP, customCalendar.getTimeInMillis(), PendingIntent.getBroadcast(getActivity(), 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
		Toast.makeText(getActivity(), "Alarm Scheduled", Toast.LENGTH_LONG).show();
	}

	private void dateTimePick(View v) {
//		FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
//
//		Fragment dateFragment = getActivity().getFragmentManager().findFragmentByTag("date_picker");
//		Fragment timeFragment = (TimePickerFragment) getActivity().getFragmentManager().findFragmentByTag("time_picker");
		
		TimePickerFragment timePickerFragment = TimePickerFragment.newInstance();
		timePickerFragment.setOnDissmissListener(new TimePickerFragment.OnDismissListener() { 
			@Override
			public void onDismiss(TimePickerFragment myDialogFragment) {
				setClock();
			} 
		}); 
		timePickerFragment.show(getFragmentManager().beginTransaction(), "time_picker");
		
		DatePickerFragment datePickerFragment = DatePickerFragment.newInstance();
		datePickerFragment.show(getFragmentManager().beginTransaction(), "date_picker");
	}
	
}