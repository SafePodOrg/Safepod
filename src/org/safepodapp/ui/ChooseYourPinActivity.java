package org.safepodapp.ui;

import org.safepodapp.R;
import org.safepodapp.SafepodAppApplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChooseYourPinActivity extends Activity {
	private SharedPreferences sharedpreferences;
	private EditText mPinEditText;
	private EditText mConfirmPinEditText;
	private Button mSetPinButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_your_pin);
		sharedpreferences = getSharedPreferences(SafepodAppApplication.getSharedPreference(), Context.MODE_PRIVATE);

		mPinEditText = (EditText) findViewById(R.id.confirm_pin);
		mConfirmPinEditText = (EditText) findViewById(R.id.pin);
		mSetPinButton = (Button) findViewById(R.id.set_pin_button);
		mPinEditText.requestFocus();
		mPinEditText.setEnabled(true);
		mSetPinButton.setEnabled(true);
		
		mConfirmPinEditText.setVisibility(View.GONE);
		
		setOnClickListeners();
	}

	private void setOnClickListeners() {
		mPinEditText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isPinValid(mPinEditText.getText().toString())) {
//					mConfirmPinEditText.setEnabled(true);
//					mConfirmPinEditText.requestFocus();
//					mSetPinButton.requestFocus();
					Toast.makeText(v.getContext(), "Pin was set!", Toast.LENGTH_LONG).show();
				}
			}
		});
		
//		mConfirmPinEditText.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(mPinEditText.getText().toString() == pin) {
//					Toast.makeText(v.getContext(), "Pin has been successfully set!", Toast.LENGTH_LONG).show();
//					mSetPinButton.setEnabled(true);
////					mSetPinButton.requestFocus();
//				}
//			}
//		});
		
		mSetPinButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String pin = mPinEditText.getText().toString();
		        if(isPinValid(pin)) {
					SharedPreferences.Editor editor = sharedpreferences.edit();
			        editor.putString(SafepodAppApplication.getUserPin(), pin);
			        editor.commit();
			        finish();
		        }
			}
		});
	}

	protected boolean isPinValid(String pin) {
		try{
			Integer.parseInt(pin);
		} catch(NumberFormatException e) {
			Log.e(SafepodAppApplication.getDebugTag(), e.getMessage());
		}
		if(pin.length() != 5 || pin.equals("90210")) {
			Toast.makeText(this, "Pin length must me 5 digits!", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
}