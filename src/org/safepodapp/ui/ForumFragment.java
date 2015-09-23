package org.safepodapp.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.safepodapp.R;
import org.safepodapp.adapters.ForumViewPagerAdapter;
import org.safepodapp.beans.Experience;
import org.safepodapp.util.NetworkServices;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ForumFragment extends Fragment {
	
	View view;
	Button postExperienceButton;
	ArrayList<Experience> experiences = new ArrayList<>();
 
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
 
        view = inflater.inflate(R.layout.fragment_forum, container, false);
        
        postExperienceButton = (Button) view.findViewById(R.id.post_experience_button);
        
        postExperienceButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("here");
				PostExperienceFragment postExperienceFragment = new PostExperienceFragment();
				FragmentTransaction transaction = getFragmentManager().beginTransaction();

				transaction.replace(container.getId(), postExperienceFragment);
				transaction.addToBackStack(null);

				transaction.commit();
			}
		});
        
        new GetExperiences().execute();
 
        return view;
    }
    
    class GetExperiences extends AsyncTask<Void, Integer, String>
    {
        protected void onPreExecute (){
            System.out.println("On pre Exceute......");
        }

        protected String doInBackground(Void...arg0) {
        	System.out.println("On doInBackground...");
        	
        	try {
				String result = NetworkServices.sendGet("http://safepodapp.org/forum?");
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
            ViewPager myPager = (ViewPager) view.findViewById(R.id.myfivepanelpager);
            myPager.setAdapter(adapter);
            myPager.setCurrentItem(0);
        }
    }
 
}
