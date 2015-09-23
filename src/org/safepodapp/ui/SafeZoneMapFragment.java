package org.safepodapp.ui;

import org.safepodapp.R;
import org.safepodapp.SafepodAppApplication;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SafeZoneMapFragment extends Fragment implements ConnectionCallbacks, OnConnectionFailedListener {
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
GoogleMap map;
LatLng latlng;
private LocationRequest lr;
MapFragment mapFragment;
private static View view;

public SafeZoneMapFragment() {
}

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

    if (view != null) {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null)
            parent.removeView(view);
    }

    try {
        view = inflater.inflate(R.layout.fragment_safe_zone_map, container,
                false);

        mapFragment = ((MapFragment) this.getActivity()
                .getFragmentManager().findFragmentById(R.id.map));

        map = mapFragment.getMap();
        map.getUiSettings().setAllGesturesEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(false);

        MapsInitializer.initialize(this.getActivity());
    } catch (InflateException e) {
        Toast.makeText(getActivity(), "Problems inflating the view !",
                Toast.LENGTH_LONG).show();
    } catch (NullPointerException e) {
        Toast.makeText(getActivity(), "Google Play Services missing !",
                Toast.LENGTH_LONG).show();
    }

    return view;
}
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    lr = LocationRequest.create();
    lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
}

/** 
 * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API. 
 */ 
protected synchronized void buildGoogleApiClient() { 
    mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API) 
            .build(); 
}
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
}