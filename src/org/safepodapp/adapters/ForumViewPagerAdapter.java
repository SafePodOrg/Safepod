package org.safepodapp.adapters;

import java.util.ArrayList;

import org.safepodapp.R;
import org.safepodapp.beans.Experience;

import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ForumViewPagerAdapter extends PagerAdapter {
	private ArrayList<Experience> experiences = new ArrayList<>();

	public ForumViewPagerAdapter(ArrayList<Experience> ex) {
		experiences = ex;
	}

	public int getCount() {
		return experiences.size();
	}

	public Object instantiateItem(View collection, int position) {
		
		ScrollView scroll = new ScrollView(collection.getContext());
		LinearLayout layout = new LinearLayout(collection.getContext());

	    LayoutParams layoutParameters = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
	    layout.setOrientation(LinearLayout.VERTICAL);
	    layout.setPadding(30, 30, 30, 30);
	    
	    layout.setLayoutParams(layoutParameters);
	    
	    TextView location = new TextView(collection.getContext());
	    location.setTextColor(collection.getContext().getResources().getColor(R.color.light));
	    location.setGravity(Gravity.LEFT);
	    location.setTextSize(28);
	    location.setLineSpacing(0.0f, 1.2f);
	    Typeface face = Typeface.createFromAsset(collection.getContext().getAssets(),
	            "JosefinSans-Regular.ttf");
	    location.setTypeface(face);
	    if(experiences.get(position).getCity().compareTo("") == 0 && experiences.get(position).getCity().compareTo("") == 0)
	    	location.setText("Location : Not Specified");
	    else
	    	location.setText(experiences.get(position).getCity() + ", " + experiences.get(position).getState());
		layout.addView(location);
		
		TextView when = new TextView(collection.getContext());
		when.setTextColor(collection.getContext().getResources().getColor(R.color.light));
		when.setGravity(Gravity.LEFT);
		when.setTextSize(28);
		when.setLineSpacing(0.0f, 1.2f);
		when.setTypeface(face);
	    if(experiences.get(position).getMonth().compareTo("0") == 0 && experiences.get(position).getMonth().compareTo("0") == 0 && experiences.get(position).getMonth().compareTo("0") == 0)
	    	when.setText("Date and Time : Not Specified \n\n");
	    else
	    	when.setText("Date and Time : " + experiences.get(position).getMonth() + "-" + experiences.get(position).getDay() + "-" + experiences.get(position).getDay() + "\n\n");
		layout.addView(when);
		
		TextView description = new TextView(collection.getContext());
		description.setTextColor(collection.getContext().getResources().getColor(R.color.light));
		description.setGravity(Gravity.LEFT);
		description.setTextSize(28);
		description.setTypeface(face);
		description.setLineSpacing(0.0f, 1.2f);
		description.setText(experiences.get(position).getBody());
		layout.addView(description);
		scroll.addView(layout);
		
		((ViewPager) collection).addView(scroll, 0);
		
		return scroll;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}
}
