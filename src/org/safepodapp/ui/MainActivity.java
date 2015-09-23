package org.safepodapp.ui;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.safepodapp.R;
import org.safepodapp.adapters.ForumViewPagerAdapter;
import org.safepodapp.beans.Experience;
import org.safepodapp.util.NetworkServices;
import org.safepodapp.SafepodAppApplication;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
	private SharedPreferences sharedPreferences;
    private EditText searchEditText;
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;
	
	private Fragment fragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sharedPreferences = getSharedPreferences(SafepodAppApplication.getSharedPreference(), Context.MODE_PRIVATE);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		setmTitle(getTitle());

        // Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
		setDefaultSharedPreferences();
	}

	private void setDefaultSharedPreferences() {
		SharedPreferences.Editor editor = sharedPreferences.edit();
        
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = 23; //Default expectation is you will reach home at 11
		int minute = 0;

		if (sharedPreferences.getInt(SafepodAppApplication.getYear(), 1970) == 1970)
			editor.putInt(SafepodAppApplication.getYear(), year);
		if (sharedPreferences.getInt(SafepodAppApplication.getMonth(), 1) == 1)
			editor.putInt(SafepodAppApplication.getMonth(), month);
		if (sharedPreferences.getInt(SafepodAppApplication.getDayOfMonth(), 1) == 1)
			editor.putInt(SafepodAppApplication.getDayOfMonth(), day);
		if (sharedPreferences.getInt(SafepodAppApplication.getHourOfDay(), 0) == 0)
			editor.putInt(SafepodAppApplication.getHourOfDay(), hour);
		if (sharedPreferences.getInt(SafepodAppApplication.getMinute(), 0) == 0)
			editor.putInt(SafepodAppApplication.getMinute(), minute);
		
//		if(sharedPreferences.getString(SafepodAppApplication.getUserHomeAddress(), "empty") == "empty")
//			editor.putString(SafepodAppApplication.getUserHomeAddress(), "Default Location");
        editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen() && fragment.getClass() == ForumFragment.class) {
            // Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
//			searchEditText = new EditText(this);
//			searchEditText.setId(R.id.edit_text_search);
//			searchEditText.setHint(getString(R.string.action_search));
//			searchEditText.setSelectAllOnFocus(true);
//			searchEditText.setTextColor(Color.WHITE);
//			searchEditText.setWidth(600);
////			searchEditText.setOnClickListener(this);
//			searchEditText.setTypeface(Typeface.createFromAsset(getAssets(),
//		            "JosefinSans-Regular.ttf"));
//			searchEditText.setTextSize(16);
//            menu.add(0, 0, 0, R.string.action_search).setActionView(searchEditText).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			getMenuInflater().inflate(R.menu.main, menu);
		    return true; 
		}
		return super.onCreateOptionsMenu(menu);
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getItemId() == R.id.action_search){
			new GetExperiences().execute();
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		
		fragment = null;
	    
	    switch (position) {
	    case 0:
	        fragment = new MainFragment();
	        break;
	    case 1:
	        fragment = new SafeZoneMapFragment();
	        break;
	    case 2:
	        fragment = new SettingsFragment();
	        
	        break;
	    case 3:
	        fragment = new ForumFragment();
	        break;
	 
	    default:
	        break;
	    }
	    
		// update the main content by replacing fragments
	    if(fragment != null){
	    	FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.container, fragment)
					.commit();
	    }
	    else{
	    	Log.e(SafepodAppApplication.getDebugTag(), "Error");
	    }
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			setmTitle(getString(R.string.title_section1));
			break;
		case 2:
			setmTitle(getString(R.string.title_section2));
			break;
		case 3:
			setmTitle(getString(R.string.title_section3));
			break;
		}
	}

//	public void restoreActionBar() {
//		ActionBar actionBar = getActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//		actionBar.setDisplayShowTitleEnabled(false);
//		actionBar.setTitle(mTitle);
//	}

	public CharSequence getmTitle() {
		return mTitle;
	}
	public void setmTitle(CharSequence mTitle) {
		this.mTitle = mTitle;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
		}
	}
	
	class GetExperiences extends AsyncTask<Void, Integer, String>
    {
		ArrayList<Experience> experiences = new ArrayList<>();
        protected void onPreExecute (){
            System.out.println("On pre Exceute......");
        }

        protected String doInBackground(Void...arg0) {
        	System.out.println("On doInBackground...");
        	
        	try {
				String result = NetworkServices.sendGet("http://safepodapp.org/forum?q=" + searchEditText.getText());
				JSONObject json = new JSONObject(result);   
				JSONArray array = json.getJSONArray("results");
				
				for(int i=0; i<array.length(); i++){   
					  JSONObject o = array.getJSONObject(i);
					  Experience experience = new Experience();
					  experience.setBody(o.getString("body"));
					  experience.setDay(o.getString("day"));
					  experience.setMonth(o.getString("month"));
					  experience.setZip(o.getString("zip"));
					  experience.setYear(o.getString("year"));
					  experience.setLatitude(o.getString("latitude"));
					  experience.setLongitude(o.getString("longitude"));
					  experience.setCity(o.getString("city"));
					  experience.setState(o.getString("state"));
					  experiences.add(experience);
					}
        	} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            for(int i=0; i<10; i++){
                publishProgress(i);
            }
            return "You are at PostExecute";
        }
        
        protected void onProgressUpdate(Integer...a){
        }

        protected void onPostExecute(String result) {
        	ForumViewPagerAdapter adapter = new ForumViewPagerAdapter(experiences);
            ViewPager myPager = (ViewPager) findViewById(R.id.myfivepanelpager);
            myPager.setAdapter(adapter);
            myPager.setCurrentItem(0);
        }
    }
}