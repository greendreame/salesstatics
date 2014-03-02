package com.tjcuxulin.salesstatic;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

import com.tjcuxulin.salesstatic.control.MyAutoCompleteAdapter;
import com.tjcuxulin.salesstatic.db.MySqliteOpenHelper;
import com.tjcuxulin.salesstatic.model.Customer;
import com.tjcuxulin.salesstatic.model.Merchandise;
import com.tjcuxulin.salesstatic.model.PriceSuggest;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;

public class SuggestInActvity extends BaseActivity {
	private long merchandiseId = -1;
	private long customerId = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggestin);
		init();
	}
	
	private void init() {
		final AutoCompleteTextView merchandiseView = (AutoCompleteTextView) findViewById(R.id.suggest_in_merchandise);
		final EditText normalView = (EditText) findViewById(R.id.suggest_in_normal);
		final AutoCompleteTextView customerView = (AutoCompleteTextView) findViewById(R.id.suggest_in_customer);
		final EditText spicalView = (EditText) findViewById(R.id.suggest_in_spical);
		Button ok = (Button) findViewById(R.id.suggest_in_ok);
		final ArrayList<SimpleEntry<Integer, String>> merchandiselist = new ArrayList<SimpleEntry<Integer, String>>();
		final ArrayList<SimpleEntry<Integer, String>> customerslist = new ArrayList<SimpleEntry<Integer, String>>();
		final MyAutoCompleteAdapter customerAdapter = new MyAutoCompleteAdapter(
				getApplicationContext(), R.layout.autocomplete_items);
		final MyAutoCompleteAdapter nameAdapter = new MyAutoCompleteAdapter(
				getApplicationContext(), R.layout.autocomplete_items);
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
		
		merchandiseView.setAdapter(nameAdapter);
		merchandiseView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String keyWord = s.toString();
				
				if (merchandiseId != -1) {
					merchandiseId = -1;
					normalView.setText(null);
				}
				
				Cursor cursor = db.query(true, Merchandise.TABLENAME,
						new String[] { MySqliteOpenHelper._ID, Merchandise.NAME },
						Merchandise.NAME + " like '%" + keyWord + "%' or "
								+ Merchandise.NAME_FIRST_CHARS + " like '"
								+ keyWord + "%' or " + Merchandise.NAME_PINYIN
								+ " like '" + keyWord + "%'", null, null, null,
						null, null);

				nameAdapter.clear();
				merchandiselist.clear();
				if (cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						SimpleEntry<Integer, String> entry = new SimpleEntry<Integer, String>(
								cursor.getInt(0), cursor.getString(1));
						merchandiselist.add(entry);
						nameAdapter.add(cursor.getString(1));
					}
				}
				
				nameAdapter.notifyDataSetChanged();

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
				normalView.setText(getColumnString(Merchandise.SELL_PRICE, merchandiseId, Merchandise.TABLENAME));
			}
		});
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (merchandiseId == -1) {
					showToast(R.string.sales_name_error);
					return;
				}
				
				if (customerId == -1) {
					showToast(R.string.sales_customer_error);
					return;
				}
				
				String spicalString = spicalView.getText().toString();
				if (isStringEmpty(spicalString)) {
					showToast(R.string.purchase_price_error);
					return;
				} else {
					try {
						Float.parseFloat(spicalString);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						showToast(R.string.purchase_price_error);
						return;
					}
				}
				
				ContentValues values = new ContentValues();
				values.put(MySqliteOpenHelper._ID, (String.valueOf(customerId) + merchandiseId).hashCode());
				values.put(PriceSuggest.CUSTOMER_ID, customerId);
				values.put(PriceSuggest.CUSTOMER_PRICE, spicalString);
				values.put(PriceSuggest.MERCHANDISE_ID, merchandiseId);
				if (db.insert(PriceSuggest.TABLENAME, null, values) != -1) {
//					progressDialog.dismiss();
					showChooseDialog(R.string.dialog_content_success);
				} else {
//					progressDialog.dismiss();
					showChooseDialog(R.string.dialog_content_error);
				}
			}
		});
	}
}
