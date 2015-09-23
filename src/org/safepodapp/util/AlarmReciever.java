package org.safepodapp.util;

import org.safepodapp.R;
import org.safepodapp.SafepodAppApplication;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class AlarmReciever extends BroadcastReceiver implements ConnectionCallbacks, OnConnectionFailedListener {
	/** 
	 * Provides the entry point to Google Play services. 
	 */ 
	protected GoogleApiClient mGoogleApiClient;

	/** 
	 * Represents a geographical location. 
	 */ 
	protected Location mLastLocation;   
	/**
	 * Location stuff above this
	 * -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 */

	private Context context;
	private SharedPreferences sharedpreferences;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(SafepodAppApplication.getDebugTag(), "Received the alarm");
		sharedpreferences = context.getSharedPreferences(SafepodAppApplication.getSharedPreference(), Context.MODE_PRIVATE);
		this.context = context;
		/**
		 * -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		 * Location stuff below this
		 */
        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        if(computeDistanceFromHome() > 0.1)
        	raiseAlarm();
		Log.d(SafepodAppApplication.getDebugTag(), "Distance is: "+Double.toString(computeDistanceFromHome()));
	}
	
	private void raiseAlarm() {
		// here you can start an activity or service depending on your need
		// for ex you can start an activity to vibrate phone or to ring the phone   
		String phoneNumberReciver="4435357524";// phone number to which SMS to be send
		String message="Hi Abhay! Hopefully, this will be a susccesful effort! -Prajit";// message to send
		SmsManager sms = SmsManager.getDefault(); 
		sms.sendTextMessage(phoneNumberReciver, null, message, null, null);
		// Show the toast  like in above screen shot
		Toast.makeText(context, "Alarm Triggered and SMS Sent", Toast.LENGTH_LONG).show();
	}

	/** 
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API. 
     */ 
    protected synchronized void buildGoogleApiClient() { 
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API) 
                .build(); 
    }

//    @Override 
//    protected void onStart() { 
//        super.onStart(); 
//        mGoogleApiClient.connect();
//    } 
// 
// 
//    @Override 
//    protected void onStop() { 
//        super.onStop(); 
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        } 
//    } 
 
    /** 
     * Runs when a GoogleApiClient object successfully connects. 
     */ 
    @Override 
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for 
        // applications that do not require a fine-grained location and that do not need location 
        // updates. Gets the best and most recent location currently available, which may be null 
        // in rare cases when a location is not available. 
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.d(SafepodAppApplication.getDebugTag(), Double.toString(mLastLocation.getLatitude())+Double.toString(mLastLocation.getLongitude()));
        if (mLastLocation != null) {
        	computeDistanceFromHome();
        } else { 
            Toast.makeText(context, R.string.no_address_found, Toast.LENGTH_LONG).show();
        } 
    }
 
    private double computeDistanceFromHome() {
		Log.d(SafepodAppApplication.getDebugTag(), "computing distance");
    	DistanceCalculator d = new DistanceCalculator();
    	Double result = d.distance(mLastLocation.getLatitude(), mLastLocation.getLongitude(), sharedpreferences.getFloat(SafepodAppApplication.getHomeLat(), 0), sharedpreferences.getFloat(SafepodAppApplication.getHomeLong(), 0));
		Log.d(SafepodAppApplication.getDebugTag(), "computing distance"+Double.toString(result));
    	return result;
    }

	@Override 
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in 
        // onConnectionFailed. 
        Log.i(SafepodAppApplication.getDebugTag(), "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    } 
 
    @Override 
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to 
        // attempt to re-establish the connection. 
        Log.i(SafepodAppApplication.getDebugTag(), "Connection suspended");
        mGoogleApiClient.connect();
    }
/**
 * Location stuff above this
 * -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 */
}