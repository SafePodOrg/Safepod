package org.safepodapp.adapters;

import java.util.List;

import org.safepodapp.R;
import org.safepodapp.util.OptionsView;
import org.safepodapp.SafepodAppApplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationListAdapter extends ArrayAdapter<OptionsView> {
	private List<OptionsView> optionsList;
	private Context context;

	public NavigationListAdapter(Context context, int resource, List<OptionsView> objects) {
		super(context, resource, objects);
		this.optionsList = objects;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return ((optionsList != null) ? optionsList.size() : 0);
	}

	@Override
	public OptionsView getItem(int position) {
		return 	((optionsList != null) ? optionsList.get(position) : null);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		if(view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			Log.d(SafepodAppApplication.getDebugTag(), "I was here!");
			view = layoutInflater.inflate(R.layout.list_item, parent, false);
		}
		
		OptionsView data = optionsList.get(position);
		
		if(data != null) {
			ImageView icon = (ImageView) view.findViewById(R.id.listviewIcon);
			TextView text = (TextView) view.findViewById(R.id.listviewTextView);
			
			icon.setImageDrawable(context.getDrawable(data.getIcon()));
			text.setText(data.getOptionText());
		}
		return view;
	}
}