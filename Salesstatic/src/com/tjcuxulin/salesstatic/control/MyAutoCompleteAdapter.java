package com.tjcuxulin.salesstatic.control;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

public class MyAutoCompleteAdapter extends ArrayAdapter<String>{
	private ArrayList<String> items;

	public MyAutoCompleteAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		items = new ArrayList<String>();
	}
	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		super.clear();
		items.clear();
	}
	
	@Override
	public void add(String object) {
		// TODO Auto-generated method stub
		super.add(object);
		items.add(object);
	}
	
	//TODO
	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return new Filter() {
			
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				// TODO Auto-generated method stub
				FilterResults results = new FilterResults();
				results.count = items.size();
				results.values = items;
				return results;
			}
		};
	}

}
