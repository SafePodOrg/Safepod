package org.safepodapp;

import android.app.Application;

public class SafepodAppApplication extends Application {
	private static final String CONST_DEBUG_TAG = "SAFEPOD_APP_DEBUG_TAG";
	private static final String CONST_WEBSERVICE_URI = "http://www.example.com";

	private static final String CONST_SHARED_PREFERENCE = "SAFEPOD_APPS_SAHRED_PREFERENCE";
	private static final String YEAR = "year";
	private static final String MONTH = "month";
	private static final String DAY_OF_MONTH = "dayOfMonth";
	private static final String HOUR_OF_DAY = "hourOfDay";
	private static final String MINUTE = "minute";
	
	private static final String HOME_LAT = "homeLat";
	private static final String HOME_LONG = "homeLong";
	private static final String HOME_ADDRESS = "homeAddress";

	private static final String USER_PIN = "userPin";

	public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME = 
        "com.google.android.gms.location.sample.locationaddress"; 
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
        ".RESULT_DATA_KEY"; 
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
        ".LOCATION_DATA_EXTRA"; 
    
	public static String getDebugTag() {
		return CONST_DEBUG_TAG;
	}
	public static String getWebserviceUri() {
		return CONST_WEBSERVICE_URI;
	}
	public static String getSharedPreference() {
		return CONST_SHARED_PREFERENCE;
	}
	public static String getYear() {
		return YEAR;
	}
	public static String getMonth() {
		return MONTH;
	}
	public static String getDayOfMonth() {
		return DAY_OF_MONTH;
	}
	public static String getHourOfDay() {
		return HOUR_OF_DAY;
	}
	public static String getMinute() {
		return MINUTE;
	}
	public static String getHomeLat() {
		return HOME_LAT;
	}
	public static String getHomeLong() {
		return HOME_LONG;
	}
	public static String getUserPin() {
		return USER_PIN;
	}
	public static String getUserHomeAddress() {
		return HOME_ADDRESS;
	}
}