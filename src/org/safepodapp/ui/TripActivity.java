package org.safepodapp.ui;

import org.safepodapp.R;
import org.safepodapp.SafepodAppApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TripActivity extends Activity {
	private SharedPreferences sharedpreferences;
    private Button modifyTripButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_trip);
	    Toast.makeText(this, "Trip started!", Toast.LENGTH_LONG).show();
		sharedpreferences = getSharedPreferences(SafepodAppApplication.getSharedPreference(), Context.MODE_PRIVATE);

        modifyTripButton = (Button) findViewById(R.id.modify_trip);
        setOnClickListeners();
	}
	
	public void setOnClickListeners() {
		modifyTripButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	isChangeAllowed(v);
            }
        });
	}

	private boolean changeRequested(final View v) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage(R.string.dialog_change_time)
               .setPositiveButton(R.string.dialog_resp_yes, new DialogInterface.OnClickListener() {
            	   public void onClick(DialogInterface dialog, int id) {
	            		resetAlertSystems(v);
	            		dateTimePick(v);
	            		setUpAlertSystems(v);
            	   }
               })
               .setNegativeButton(R.string.dialog_resp_no, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   Toast.makeText(v.getContext(), "Data was not deleted!", Toast.LENGTH_LONG).show();
                   }
               });

		// create alert dialog
		AlertDialog alertDialog = builder.create();

		// show it
		alertDialog.show();
		return false;
	}

	private void isChangeAllowed(final View v) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        LayoutInflater inflater = LayoutInflater.from(v.getContext());
        View view = inflater.inflate(R.layout.fragment_pin_dialog, null);
        
        builder.setTitle("PIN required.");
        builder.setCancelable(false);
        builder.setView(view);
        
        final EditText userInput = (EditText) view.findViewById(R.id.pin_edit_text);
        builder.setMessage(R.string.dialog_enter_pin)
        	.setCancelable(false)
        	.setPositiveButton(R.string.dialog_done_with_pin, new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int id) {
        			if(checkPin(userInput.getText().toString()))
        				changeRequested(v);
        			else
        				Toast.makeText(v.getContext(), "Sorry you can't change the time!", Toast.LENGTH_LONG).show();
        		}

        	});
		// create alert dialog
		AlertDialog alertDialog = builder.create();

		// show it
		alertDialog.show();
	}

	private boolean checkPin(String pinEntered) {
		String pinSet = sharedpreferences.getString(SafepodAppApplication.getUserPin(), "90210");
		if(!pinSet.equals(pinEntered)) 
			return false;
		return true;
	}

	private void resetAlertSystems(View v) {
		Toast.makeText(v.getContext(), "Trip cancelled!", Toast.LENGTH_LONG).show();
	}

	private void setUpAlertSystems(View v) {
		Toast.makeText(v.getContext(), "Trip started!", Toast.LENGTH_LONG).show();
	}

	private void dateTimePick(View v) {
//		FragmentTransaction ft = getFragmentManager().beginTransaction();
//
//		Fragment dateFragment = getFragmentManager().findFragmentByTag("date_picker");
//		Fragment timeFragment = getFragmentManager().findFragmentByTag("time_picker");
		
		DialogFragment newFragment = TimePickerFragment.newInstance();
        newFragment.show(getFragmentManager().beginTransaction(), "time_picker");
		newFragment = DatePickerFragment.newInstance();
        newFragment.show(getFragmentManager().beginTransaction(), "date_picker");
	}
}