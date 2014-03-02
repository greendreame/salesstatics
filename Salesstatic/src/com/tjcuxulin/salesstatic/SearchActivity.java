package com.tjcuxulin.salesstatic;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;

import com.tjcuxulin.salesstatic.control.MyAutoCompleteAdapter;
import com.tjcuxulin.salesstatic.model.Customer;
import com.tjcuxulin.salesstatic.model.Merchandise;
import com.tjcuxulin.salesstatic.model.Sales;
import com.tjcuxulin.salesstatic.db.MySqliteOpenHelper;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends BaseActivity {
	private long merchandiseId = -1;
	private long customerId = -1;
	// private String startTimeStr;
	// private String endTimeStr;
	private TableLayout parent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		init();
	}

	private void init() {
		parent = (TableLayout) findViewById(R.id.search_parent);
		final ArrayList<SimpleEntry<Integer, String>> merchandiselist = new ArrayList<SimpleEntry<Integer, String>>();
		final ArrayList<SimpleEntry<Integer, String>> customerslist = new ArrayList<SimpleEntry<Integer, String>>();
		final MyAutoCompleteAdapter customerAdapter = new MyAutoCompleteAdapter(
				getApplicationContext(), R.layout.autocomplete_items);
		final MyAutoCompleteAdapter merchandiseAdapter = new MyAutoCompleteAdapter(
				getApplicationContext(), R.layout.autocomplete_items);
		AutoCompleteTextView merchandiseView = (AutoCompleteTextView) findViewById(R.id.search_merchandise);
		AutoCompleteTextView customerView = (AutoCompleteTextView) findViewById(R.id.search_customer);
		final EditText startTimeView = (EditText) findViewById(R.id.search_starttime);
		final EditText endTimeView = (EditText) findViewById(R.id.search_endtime);
		
		startTimeView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Calendar c = Calendar.getInstance();
		        int year = c.get(Calendar.YEAR);
		        int month = c.get(Calendar.MONTH);
		        int day = c.get(Calendar.DAY_OF_MONTH);
				DatePickerDialog dialog = new DatePickerDialog(SearchActivity.this, new OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						// TODO Auto-generated method stub
						monthOfYear += 1;
						String month = null;
						if (monthOfYear < 10) {
							month = "0" + monthOfYear;
						} else {
							month = String.valueOf(monthOfYear);
						}
						
						String day = null;
						if (dayOfMonth < 10) {
							day = "0" + dayOfMonth;
						} else {
							day = String.valueOf(dayOfMonth);
						}
						
						startTimeView.setText(year + "-" + month + "-" + day);
					}
				}, year, month, day);
				dialog.show();
			}
		});
		
		endTimeView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Calendar c = Calendar.getInstance();
		        int year = c.get(Calendar.YEAR);
		        int month = c.get(Calendar.MONTH);
		        int day = c.get(Calendar.DAY_OF_MONTH);
				DatePickerDialog dialog = new DatePickerDialog(SearchActivity.this, new OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						// TODO Auto-generated method stub
						monthOfYear += 1;
						String month = null;
						if (monthOfYear < 10) {
							month = "0" + monthOfYear;
						} else {
							month = String.valueOf(monthOfYear);
						}
						
						String day = null;
						if (dayOfMonth < 10) {
							day = "0" + dayOfMonth;
						} else {
							day = String.valueOf(dayOfMonth);
						}
						endTimeView.setText(year + "-" + month + "-" + day);
					}
				}, year, month, day);
				dialog.show();
			}
		});

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
				
				Cursor cursor = db.query(true, Merchandise.TABLENAME,
						new String[] { MySqliteOpenHelper._ID, Merchandise.NAME },
						Merchandise.NAME + " like '%" + keyWord + "%' or "
								+ Merchandise.NAME_FIRST_CHARS + " like '"
								+ keyWord + "%' or " + Merchandise.NAME_PINYIN
								+ " like '" + keyWord + "%'", null, null, null,
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

		Button ok = (Button) findViewById(R.id.search_ok);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				View headerView = parent.getChildAt(0);
				parent.removeAllViews();
				parent.addView(headerView);
				
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

				String startTimeStr = startTimeView.getText().toString();
				String endTimeStr = endTimeView.getText().toString();
				if (!isStringEmpty(startTimeStr)) {
					if (!whereClause.equals("")) {
						whereClause += " and ";
					}

					whereClause += Sales.DATE + " >= '" + startTimeStr + "'";
				}

				if (!isStringEmpty(endTimeStr)) {
					if (!whereClause.equals("")) {
						whereClause += " and ";
					}

					whereClause += Sales.DATE + " <= '" + endTimeStr + "'";
				}
				
				if (whereClause.equals("")) {
					whereClause = null;
				}

//				String limit = page * MAXLOAD + ", " + MAXLOAD;
				Cursor cursor = db.query(true, Sales.TABLENAME, null,
						whereClause, null, null, null, null, null);
				Log.d("Test", "cursor : " + cursor.getCount());
				Log.d("Test", "whereClause : " + whereClause);
				while (cursor.moveToNext()) {
					addRow(getColumnString(Merchandise.NAME, cursor
							.getLong(cursor
									.getColumnIndex(Sales.MERCHANDISE_ID)),
							Merchandise.TABLENAME),
							getColumnString(
									Customer.NAME,
									cursor.getLong(cursor
											.getColumnIndex(Sales.CUSTOMER_ID)),
									Customer.TABLENAME), cursor.getFloat(cursor
									.getColumnIndex(Sales.SALES_NUMS)),
							cursor.getFloat(cursor
									.getColumnIndex(Sales.SELLING_PRICE)),
							cursor.getString(cursor.getColumnIndex(Sales.DATE)));
				}

				cursor.close();
				cursor = null;
			}
		});
		
		if(isVisiable == false) {
			TextView priceView = (TextView) findViewById(R.id.search_price);
			priceView.setVisibility(View.GONE);
		}

	}

	private void addRow(String merchandiseString, String customerString,
			float salesNums, float price, String timeStr) {
		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.search_item, null);
		TextView merchandiseView = (TextView) view
				.findViewById(R.id.search_item_merchandise_name);
		merchandiseView.setText(merchandiseString);
		TextView customerView = (TextView) view
				.findViewById(R.id.search_item_customer_name);
		customerView.setText(customerString);
		TextView salesView = (TextView) view
				.findViewById(R.id.search_item_sales_nums);
		salesView.setText(salesNums + "");
		TextView priceView = (TextView) view
				.findViewById(R.id.search_item_sales_price);
		priceView.setText(price + "");
		if(isVisiable == false) {
			priceView.setVisibility(View.GONE);
		}
		TextView timeView = (TextView) view
				.findViewById(R.id.search_item_sales_time);
		timeView.setText(timeStr);
		parent.addView(view);
	}
}
