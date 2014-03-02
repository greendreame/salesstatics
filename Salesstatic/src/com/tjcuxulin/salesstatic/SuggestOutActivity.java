package com.tjcuxulin.salesstatic;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;

import com.tjcuxulin.salesstatic.control.MyAutoCompleteAdapter;
import com.tjcuxulin.salesstatic.db.MySqliteOpenHelper;
import com.tjcuxulin.salesstatic.model.Customer;
import com.tjcuxulin.salesstatic.model.Merchandise;
import com.tjcuxulin.salesstatic.model.PriceSuggest;
import com.tjcuxulin.salesstatic.model.Sales;
import com.tjcuxulin.salesstatic.util.SalesUtil;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class SuggestOutActivity extends BaseActivity {
	private long merchandiseId = -1;
	private long customerId = -1;
	private ArrayList<HashMap<String, String>> list;
	private MyAdapter adapter;
	private int[] widths;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggestout);
		init();
	}

	private void init() {
		widths = new int[7];
		for (int i = 0; i < widths.length; i++) {
			widths[i] = 1;
		}
		ListView resultListView = (ListView) findViewById(R.id.suggest_out_result_list);
		list = new ArrayList<HashMap<String, String>>();
		adapter = new MyAdapter();
		resultListView.setAdapter(adapter);
		final ArrayList<SimpleEntry<Integer, String>> merchandiselist = new ArrayList<SimpleEntry<Integer, String>>();
		final ArrayList<SimpleEntry<Integer, String>> customerslist = new ArrayList<SimpleEntry<Integer, String>>();
		final MyAutoCompleteAdapter customerAdapter = new MyAutoCompleteAdapter(
				getApplicationContext(), R.layout.autocomplete_items);
		final MyAutoCompleteAdapter merchandiseAdapter = new MyAutoCompleteAdapter(
				getApplicationContext(), R.layout.autocomplete_items);
		AutoCompleteTextView merchandiseView = (AutoCompleteTextView) findViewById(R.id.suggest_out_merchandise);
		AutoCompleteTextView customerView = (AutoCompleteTextView) findViewById(R.id.suggest_out_customer);
		customerView.setAdapter(customerAdapter);
		customerView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String keyWord = s.toString();
				
				if (customerId != -1) {
					customerId = -1;
				}

				Cursor cursor = db.query(true, Customer.TABLENAME,
						new String[] { MySqliteOpenHelper._ID, Customer.NAME },
						Customer.NAME + " like '%" + keyWord + "%' or "
								+ Customer.NAME_FIRST_CHARS + " like '"
								+ keyWord + "%' or " + Customer.NAME_PINYIN
								+ " like '" + keyWord + "%'", null, null, null,
						null, null);

				customerAdapter.clear();
				customerslist.clear();
				if (cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						SimpleEntry<Integer, String> entry = new SimpleEntry<Integer, String>(
								cursor.getInt(0), cursor.getString(1));
						customerslist.add(entry);
						customerAdapter.add(cursor.getString(1));
					}
				}

				customerAdapter.notifyDataSetChanged();

				cursor.close();
				cursor = null;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
		customerView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				customerId = customerslist.get(0).getKey();
			}
		});
		merchandiseView.setAdapter(merchandiseAdapter);
		merchandiseView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String keyWord = s.toString();
				
				if (merchandiseId != -1) {
					merchandiseId = -1;
				}
				
				Cursor cursor = db
						.query(true, Merchandise.TABLENAME, new String[] {
								MySqliteOpenHelper._ID, Merchandise.NAME },
								Merchandise.NAME + " like '%" + keyWord
										+ "%' or "
										+ Merchandise.NAME_FIRST_CHARS
										+ " like '" + keyWord + "%' or "
										+ Merchandise.NAME_PINYIN + " like '"
										+ keyWord + "%'", null, null, null,
								null, null);

				merchandiseAdapter.clear();
				merchandiselist.clear();
				if (cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						SimpleEntry<Integer, String> entry = new SimpleEntry<Integer, String>(
								cursor.getInt(0), cursor.getString(1));
						merchandiselist.add(entry);
						merchandiseAdapter.add(cursor.getString(1));
					}
				}

				merchandiseAdapter.notifyDataSetChanged();

				cursor.close();
				cursor = null;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		merchandiseView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				merchandiseId = merchandiselist.get(0).getKey();
			}
		});

		Button ok = (Button) findViewById(R.id.suggest_out_ok);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String whereClause = "";
				if (merchandiseId != -1) {
					whereClause += Sales.MERCHANDISE_ID + " = " + merchandiseId;
				}

				if (customerId != -1) {
					if (merchandiseId != -1) {
						whereClause += " and ";
					}

					whereClause += Sales.CUSTOMER_ID + " = " + customerId;
				}

				if (whereClause.equals("")) {
					initListData();
					return;
				}
				
				list.clear();
				
				for (int i = 0; i < widths.length; i++) {
					widths[i] = 20;
				}

				Cursor cursor = db.query(true, PriceSuggest.TABLENAME,
						new String[] { PriceSuggest.CUSTOMER_ID,
								PriceSuggest.MERCHANDISE_ID,
								PriceSuggest.CUSTOMER_PRICE }, whereClause,
						null, null, null, null, null);
				while (cursor.moveToNext()) {
					String mName = null;
					String price = null;
					Cursor c = db.query(true, Merchandise.TABLENAME, new String[]{Merchandise.NAME, Merchandise.SELL_PRICE}, MySqliteOpenHelper._ID + " = " + cursor.getLong(1), null, null, null, null, null);
					if (c.moveToNext()) {
						mName = c.getString(0);
						price = c.getString(1);
						
						int w0 = SalesUtil.GetPixelByText(20, mName);
						if (widths[0] < w0) {
							widths[0] = w0;
						}
						
						int w1 = SalesUtil.GetPixelByText(20, price);
						if (widths[1] < w1) {
							widths[1] = w1;
						}
					}
					c.close();
					c = null;
					if (mName == null || price == null) {
						initListData();
						return ;
					}
					
					String cName = null;
					String tel = null;
					String phone = null;
					String demand = null;
					c = db.query(true, Customer.TABLENAME, new String[] {Customer.NAME, Customer.TELEPHONE, Customer.CELLPHONE, Customer.DEMAND}, MySqliteOpenHelper._ID + " = " + cursor.getLong(0), null, null, null, null, null);
					if (c.moveToNext()) {
						cName = c.getString(0);
						tel = c.getString(1);
						phone = c.getString(2);
						demand = c.getString(3);
						
						int w2 = SalesUtil.GetPixelByText(20, cName);
						if (widths[2] < w2) {
							widths[2] = w2;
						}
						
						int w4 = SalesUtil.GetPixelByText(20, demand);
						if (widths[4] < w4) {
							widths[4] = w4;
						}
						
						int w5 = SalesUtil.GetPixelByText(20, tel);
						if (widths[5] < w5) {
							widths[5] = w5;
						}
						
						int w6 = SalesUtil.GetPixelByText(20, phone);
						if (widths[6] < w6) {
							widths[6] = w6;
						}
						
					}
					c.close();
					c = null;
					if (cName == null) {
						return ;
					}
					
					String cPrice = String.valueOf(cursor.getFloat(2));
					int w3 = SalesUtil.GetPixelByText(20, cPrice);
					if (widths[3] < w3) {
						widths[3] = w3;
					}
					
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("mName", mName);
					map.put("price", price);
					map.put("cName", cName);
					map.put("cPrice", cPrice);
					map.put("tel", tel);
					map.put("demand", demand);
					map.put("phone", phone);
					list.add(map);
				}
				
				cursor.close();
				cursor = null;
				
				if (list.size() != 0) {
					if (isVisiable) {
						setAllWidth();
					} else {
						setWidth();
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
		
		if (isVisiable == false) {
			merchandiseView.setVisibility(View.GONE);
			customerView.setVisibility(View.GONE);
			ok.setVisibility(View.GONE);
		}

		setInVisiable();
		initListData();
		
	}

	private void setInVisiable() {
		if (isVisiable == false) {
			TextView customer = (TextView) findViewById(R.id.suggest_out_customer_name);
			customer.setVisibility(View.GONE);
			TextView customerPrice = (TextView) findViewById(R.id.suggest_out_customer_price);
			customerPrice.setVisibility(View.GONE);
			TextView customerDemand = (TextView) findViewById(R.id.suggest_out_demond);
			customerDemand.setVisibility(View.GONE);
			TextView customerTel = (TextView) findViewById(R.id.suggest_out_tel);
			customerTel.setVisibility(View.GONE);
			TextView customerPhone = (TextView) findViewById(R.id.suggest_out_phone);
			customerPhone.setVisibility(View.GONE);
		}
	}

	private void initListData() {
		Cursor cursor = db.query(true, Merchandise.TABLENAME, new String[] {
				Merchandise.NAME, Merchandise.SELL_PRICE }, null, null, null,
				null, null, null);
		while (cursor.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("mName", cursor.getString(0));
			map.put("price", cursor.getString(1));
			int w0 = SalesUtil.GetPixelByText(20, cursor.getString(0));
			if (widths[0] < w0) {
				widths[0] = w0;
			}
			
			int w1 = SalesUtil.GetPixelByText(20, cursor.getString(1));
			if (widths[1] < w1) {
				widths[1] = w1;
			}
			
			list.add(map);
		}
		cursor.close();
		cursor = null;

		setWidth();
		
		adapter.notifyDataSetChanged();
	}
	
	private void setWidth() {
		TextView merchandise = (TextView) findViewById(R.id.suggest_out_merchandise_name);
		merchandise.setWidth(widths[0]);
		TextView price = (TextView) findViewById(R.id.suggest_out_normal_price);
		price.setWidth(widths[1]);
	}
	
	private void setAllWidth() {
		TextView merchandise = (TextView) findViewById(R.id.suggest_out_merchandise_name);
		merchandise.setWidth(widths[0]);
		TextView price = (TextView) findViewById(R.id.suggest_out_normal_price);
		price.setWidth(widths[1]);
		TextView customer = (TextView) findViewById(R.id.suggest_out_customer_name);
		customer.setWidth(widths[2]);
		TextView customerPrice = (TextView) findViewById(R.id.suggest_out_customer_price);
		customerPrice.setWidth(widths[3]);
		TextView customerDemand = (TextView) findViewById(R.id.suggest_out_demond);
		customerDemand.setWidth(widths[4]);
		TextView customerTel = (TextView) findViewById(R.id.suggest_out_tel);
		customerTel.setWidth(widths[5]);
		TextView customerPhone = (TextView) findViewById(R.id.suggest_out_phone);
		customerPhone.setWidth(widths[6]);
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = null;
			if (convertView == null) {
				view = LayoutInflater.from(SuggestOutActivity.this).inflate(
						R.layout.suggest_out_list_item, null);
			} else {
				view = convertView;
			}

			TextView mName = (TextView) view
					.findViewById(R.id.suggest_out_item_merchandise_name);
			mName.setText(list.get(position).get("mName"));
			TextView price = (TextView) view
					.findViewById(R.id.suggest_out_item_normal_price);
			price.setText(list.get(position).get("price"));
			TextView cName = (TextView) view
					.findViewById(R.id.suggest_out_item_customer_name);
			cName.setText(list.get(position).get("cName"));
			TextView cPrice = (TextView) view
					.findViewById(R.id.suggest_out_item_customer_price);
			cPrice.setText(list.get(position).get("cPrice"));
			TextView tel = (TextView) view
					.findViewById(R.id.suggest_out_item_tel);
			tel.setText(list.get(position).get("tel"));
			TextView demand = (TextView) view
					.findViewById(R.id.suggest_out_item_demond);
			demand.setText(list.get(position).get("demand"));
			TextView phone = (TextView) view
					.findViewById(R.id.suggest_out_item_phone);
			phone.setText(list.get(position).get("phone"));
			mName.setWidth(widths[0]);
			price.setWidth(widths[1]);
			cName.setWidth(widths[2]);
			cPrice.setWidth(widths[3]);
			demand.setWidth(widths[4]);
			tel.setWidth(widths[5]);
			phone.setWidth(widths[6]);
			if (isVisiable == false) {
				cName.setVisibility(View.GONE);
				cPrice.setVisibility(View.GONE);
				tel.setVisibility(View.GONE);
				demand.setVisibility(View.GONE);
				phone.setVisibility(View.GONE);
			}
			return view;
		}

	}
}
