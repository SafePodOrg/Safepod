package org.safepodapp.util;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.safepodapp.SafepodAppApplication;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

public class GeoCoding extends AsyncTask<Void, Void, Void> {
	private String address;

	private JSONObject jsonObj;

	private String URL;

	private String Address1 = "";

	private String Address2 = "";

	private String City = "";

	private String State = "";

	private String Country = "";

	private String County = "";

	private String PIN = "";

	private String Area="";

	private  double latitude, longitude;

	private HttpURLConnection connection;

	private BufferedReader br;

	private StringBuilder sb ;

	public GeoCoding(String address){
        this.address = address;
    }

	@Override
    protected Void doInBackground(Void... params)  {
        try {
            StringBuilder urlStringBuilder = new StringBuilder("http://maps.google.com/maps/api/geocode/json");
            urlStringBuilder.append("?address=" + URLEncoder.encode(address, "utf8"));
            urlStringBuilder.append("&sensor=false");
            URL = urlStringBuilder.toString();
            Log.d(SafepodAppApplication.getDebugTag(), "URL: " + URL);

            URL url = new URL(URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb = sb.append(line + "\n");
            }
        }catch (Exception e){e.printStackTrace(); }
        return null;
    }

	public void getAddress() {
        Address1 = "";
        Address2 = "";
        City = "";
        State = "";
        Country = "";
        County = "";
        PIN = "";
        Area ="";

        try {

            String Status = jsonObj.getString("status");
            if (Status.equalsIgnoreCase("OK")) {
                JSONArray Results = jsonObj.getJSONArray("results");
                JSONObject zero = Results.getJSONObject(0);
                JSONArray address_components = zero.getJSONArray("address_components");

                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject zero2 = address_components.getJSONObject(i);
                    String long_name = zero2.getString("long_name");
                    JSONArray mtypes = zero2.getJSONArray("types");
                    String Type = mtypes.getString(0);

                    if (! TextUtils.isEmpty(long_name) || !long_name.equals(null) || long_name.length() > 0 || !long_name.equals("")) {
                        if (Type.equalsIgnoreCase("street_number")) {
                            Address1 = long_name + " ";
                        } else if (Type.equalsIgnoreCase("route")) {
                            Address1 = Address1 + long_name;
                        } else if (Type.equalsIgnoreCase("sublocality")) {
                            Address2 = long_name;
                        } else if (Type.equalsIgnoreCase("locality")) {
                            City = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                            County = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                            State = long_name;
                        } else if (Type.equalsIgnoreCase("country")) {
                            Country = long_name;
                        } else if (Type.equalsIgnoreCase("postal_code")) {
                            PIN = long_name;
                        }else if( Type.equalsIgnoreCase("neighborhood")){
                            Area = long_name;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

	public String getAddress1() {
		return Address1;
	}

	public String getAddress2() {
		return Address2;
	}

	public String getArea(){
        return Area;
    }

	public BufferedReader getBr() {
		return br;
	}

	public String getCity() {
		return City;
	}

	public HttpURLConnection getConnection() {
		return connection;
	}

	public String getCountry() {
		return Country;
	}

	public String getCounty() {
		return County;
	}

	public void getGeoPoint(){
        try{
             longitude = ((JSONArray)jsonObj.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");
            latitude = ((JSONArray)jsonObj.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

	public JSONObject getJsonObj() {
		return jsonObj;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
    public String getPIN() {
		return PIN;
	}
    public StringBuilder getSb() {
		return sb;
	}
    public String getState() {
		return State;
	}
    public String getURL() {
		return URL;
	}
    @Override
    protected void onPostExecute(Void aVoid) {
        try {
            Log.d(SafepodAppApplication.getDebugTag(), "response code: " + connection.getResponseCode());
            jsonObj = new JSONObject(sb.toString());
            Log.d(SafepodAppApplication.getDebugTag(), "JSON obj: " + jsonObj);
            getAddress();
            Log.d(SafepodAppApplication.getDebugTag(), "area is: " + getArea());
            getGeoPoint();
            Log.d("latitude", "" + latitude);
            Log.d("longitude", "" + longitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPostExecute(aVoid);
    }
    public void setAddress(String address) {
		this.address = address;
	}
    public void setAddress1(String address1) {
		Address1 = address1;
	}
    public void setAddress2(String address2) {
		Address2 = address2;
	}
    public void setArea(String area) {
		Area = area;
	}
    public void setBr(BufferedReader br) {
		this.br = br;
	}
    public void setCity(String city) {
		City = city;
	}
    public void setConnection(HttpURLConnection connection) {
		this.connection = connection;
	}
    public void setCountry(String country) {
		Country = country;
	}
    public void setCounty(String county) {
		County = county;
	}
    public void setJsonObj(JSONObject jsonObj) {
		this.jsonObj = jsonObj;
	}

    public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

    public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

    public void setPIN(String pIN) {
		PIN = pIN;
	}

    public void setSb(StringBuilder sb) {
		this.sb = sb;
	}


    public void setState(String state) {
		State = state;
	}

    public void setURL(String uRL) {
		URL = uRL;
	}
}
