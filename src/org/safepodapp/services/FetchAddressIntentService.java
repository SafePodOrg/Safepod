package org.safepodapp.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.safepodapp.R;
import org.safepodapp.SafepodAppApplication;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

public class FetchAddressIntentService extends IntentService {

	public FetchAddressIntentService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		String errorMessage = "";

		// Get the location passed to this service through an extra.
		Location location = intent.getParcelableExtra(
				SafepodAppApplication.LOCATION_DATA_EXTRA);

		List<Address> addresses = null;

		try {
			addresses = geocoder.getFromLocation(
					location.getLatitude(),
					location.getLongitude(),
					// In this sample, get just a single address.
					1);
		} catch (IOException ioException) {
			// Catch network or other I/O problems.
			errorMessage = getString(R.string.service_not_available);
			Log.e(SafepodAppApplication.getDebugTag(), errorMessage, ioException);
		} catch (IllegalArgumentException illegalArgumentException) {
			// Catch invalid latitude or longitude values.
			errorMessage = getString(R.string.invalid_lat_long_used);
			Log.e(SafepodAppApplication.getDebugTag(), errorMessage + ". " +
					"Latitude = " + location.getLatitude() +
					", Longitude = " +
					location.getLongitude(), illegalArgumentException);
		}

		// Handle case where no address was found.
		if (addresses == null || addresses.size() == 0) {
			if (errorMessage.isEmpty()) {
				errorMessage = getString(R.string.no_address_found);
				Log.e(SafepodAppApplication.getDebugTag(), errorMessage);
			}
			deliverResultToReceiver(SafepodAppApplication.FAILURE_RESULT, errorMessage);
		} else {
			Address address = addresses.get(0);
			ArrayList<String> addressFragments = new ArrayList<String>();

			// Fetch the address lines using getAddressLine,
			// join them, and send them to the thread.
			for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
				addressFragments.add(address.getAddressLine(i));
			}
			Log.i(SafepodAppApplication.getDebugTag(), getString(R.string.address_found));
			deliverResultToReceiver(SafepodAppApplication.SUCCESS_RESULT,
					TextUtils.join(System.getProperty("line.separator"),
							addressFragments));
		}
	}
	
	protected ResultReceiver mReceiver;
    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(SafepodAppApplication.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    } 

}