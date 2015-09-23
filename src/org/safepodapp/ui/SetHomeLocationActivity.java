package org.safepodapp.ui;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.safepodapp.R;
import org.safepodapp.SafepodAppApplication;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SetHomeLocationActivity extends Activity {
    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    private EditText mHomeAddressEditText;
    private Button submitButton;
    private Button clearButton;
    private SharedPreferences sharedPreferences;
    private TextView mSetAddressTextView;
    
    private LatLng homeLatLng;
    private String address;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_home_location);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        sharedPreferences = getSharedPreferences(SafepodAppApplication.getSharedPreference(), Context.MODE_PRIVATE);

        String textHeader = getIntent().getStringExtra("addressSetActivityHeader");
        mSetAddressTextView = (TextView) findViewById(R.id.setHomeAddressTextView);
        mSetAddressTextView.setText(textHeader);

        setTitle(textHeader);
        
        mHomeAddressEditText = (EditText) findViewById(R.id.enterAddressEditText);
        
        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				address = mHomeAddressEditText.getEditableText().toString();
				addAddressToSharedPreference();
			}
		});
        
        // Set up the 'clear text' button that clears the text in the autocomplete view
        clearButton = (Button) findViewById(R.id.clear_button);
        clearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHomeAddressEditText.setText("");
            }
        });
	}
	
    protected void addAddressToSharedPreference() {
		getLatLongFromAddress(address);

		SharedPreferences.Editor editor = sharedPreferences.edit();
        
        Log.d("latitude", "" + homeLatLng.latitude);
        Log.d("longitude", "" + homeLatLng.longitude);
        editor.putFloat(SafepodAppApplication.getHomeLat(), (float) homeLatLng.latitude);
        editor.putFloat(SafepodAppApplication.getHomeLong(), (float) homeLatLng.longitude);
        editor.putString(SafepodAppApplication.getUserHomeAddress(), address);
        editor.commit();
        finish();
	}

   private void getLatLongFromAddress(String youraddress) {
    	URL url;
		HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
			url = new URL(
					"http://maps.google.com/maps/api/geocode/json?address=" + 
					URLEncoder.encode(youraddress, 
							"UTF_8"
//							Charset.availableCharsets().get("UTF_8").toString()
							).toString() 
					+ "&sensor=false");
			 
        	conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			Log.e(SafepodAppApplication.getDebugTag(), "Error processing Places API URL", e);
		} catch (IOException e) {
			Log.e(SafepodAppApplication.getDebugTag(), "Error connecting to Places API", e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(jsonResults.toString());

            double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lng");

            double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lat");

            Log.d("latitude", "" + lat);
            Log.d("longitude", "" + lng);
            homeLatLng = new LatLng(lat, lat);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}