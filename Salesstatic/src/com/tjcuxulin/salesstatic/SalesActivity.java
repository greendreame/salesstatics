package com.tjcuxulin.salesstatic;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

import com.tjcuxulin.salesstatic.control.MyAutoCompleteAdapter;
import com.tjcuxulin.salesstatic.model.Customer;
import com.tjcuxulin.salesstatic.model.Merchandise;
import com.tjcuxulin.salesstatic.model.Sales;
import com.tjcuxulin.salesstatic.util.SalesUtil;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;

public class SalesActivity extends BaseActivity {
	private final String TAG = "SalesActivity";
	private long merchandiseId = -1;
	private long customerId = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sales);
		init();
	}

	private void init() {
		final ArrayList<SimpleEntry<Integer, String>> merchandiselist = new ArrayList<SimpleEntry<Integer, String>>();
		final ArrayList<SimpleEntry<Integer, String>> customerslist = new ArrayList<SimpleEntry<Integer, String>>();
		final MyAutoCompleteAdapter customerAdapter = new MyAutoCompleteAdapter(
				getApplicationContext(), R.layout.autocomplete_items);
		final MyAutoCompleteAdapter nameAdapter = new MyAutoCompleteAdapter(
				getApplicationContext(), R.layout.autocomplete_items);
		final AutoCompleteTextView customerView = (AutoCompleteTextView) findViewById(R.id.sales_customer);
		final EditText telView = (EditText) findViewById(R.id.sales_customer_tel);
		final EditText phoneView = (EditText) findViewById(R.id.sales_customer_phone);
		final EditText demandView = (EditText) findViewById(R.id.sales_customer_demand);
		final AutoCompleteTextView nameView = (AutoCompleteTextView) findViewById(R.id.sales_name);
		final EditText numsView = (EditText) findViewById(R.id.sales_nums);
		final EditText priceView = (EditText) findViewById(R.id.sales_price);
		final Button ok = (Button) findViewById(R.id.sales_ok);
		customerView.setAdapter(customerAdapter);
		customerView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String keyWord = s.toString();
				Cursor cursor = db.query(true, Customer.TABLENAME,
						new String[] { Customer._ID, Customer.NAME },
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
				} else {
					customerId = -1;
					telView.setText(null);
					phoneView.setText(null);
					demandView.setText(null);
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
				Cursor cursor = getCursorById(customerId, Customer.TABLENAME);
				if (cursor.moveToNext()) {
					telView.setText(cursor.getString(cursor
							.getColumnIndex(Customer.TELEPHONE)));
					phoneView.setText(cursor.getString(cursor
							.getColumnIndex(Customer.CELLPHONE)));
					demandView.setText(cursor.getString(cursor
							.getColumnIndex(Customer.DEMAND)));
				}
				cursor.close();
				cursor = null;
			}
		});
		nameView.setAdapter(nameAdapter);
		nameView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String keyWord = s.toString();
				Cursor cursor = db.query(true, Merchandise.TABLENAME,
						new String[] { Merchandise._ID, Merchandise.NAME },
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
				} else {
					merchandiseId = -1;
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
		nameView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				merchandiseId = merchandiselist.get(0).getKey();
			}
		});

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String customerStr = customerView.getText().toString();
				if (isStringEmpty(customerStr)) {
					showToast(R.string.sales_customer_error);
					return;
				}

				String phoneNumString = phoneView.getText().toString();
				if (!isStringEmpty(phoneNumString)) {
					boolean flag = false;
					try {
						Long.parseLong(phoneNumString);
						if (phoneNumString.length() != 11) {
							flag = true;
							Log.d(TAG, "phoneNumString.length() != 11");
						}
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						flag = true;
						Log.d(TAG, "NumberFormatException");
					}

					if (flag) {
						showToast(R.string.sales_customer_phone_error);
						return;
					}
				}

				final ContentValues values = new ContentValues();
				values.put(Customer.NAME, customerStr);
				values.put(Customer.NAME_FIRST_CHARS,
						SalesUtil.getPinYinHeadChar(customerStr));
				values.put(Customer.NAME_PINYIN,
						SalesUtil.getPinYin(customerStr));
				values.put(Customer.CELLPHONE, phoneNumString);
				values.put(Customer.TELEPHONE, telView.getText().toString());
				values.put(Customer.DEMAND, demandView.getText().toString());

				if (merchandiseId == -1) {
//					progressDialog.dismiss();
					AlertDialog.Builder builder = new AlertDialog.Builder(
							SalesActivity.this);
					builder.setMessage(R.string.sales_name_not_exists);
					builder.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									if (customerId == -1) {
										customerId = db.insert(
												Customer.TABLENAME, null,
												values);
									} else {
										db.update(
												Customer.TABLENAME,
												values,
												Customer._ID + "=" + customerId,
												null);
									}

									showToast(R.string.sales_success);
								}
							});
					builder.setNegativeButton(R.string.cancel, null);
					builder.create().show();
					return;
				}

				String numsString = numsView.getText().toString();
				if (isStringEmpty(numsString)) {
					showToast(R.string.sales_nums_error);
					return;
				}

				String priceString = priceView.getText().toString();
				if (isStringEmpty(priceString)) {
					showToast(R.string.purchase_price_error);
					return;
				} else {
					try {
						Float.parseFloat(priceString);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						showToast(R.string.purchase_price_error);
						return;
					}
				}

				values.clear();

				if (customerId == -1) {
					showToast(R.string.sales_customer_entry_error);
					return;
				}

				values.put(Sales.CUSTOMER_ID, customerId);
				values.put(Sales.MERCHANDISE_ID, merchandiseId);
				values.put(Sales.SALES_NUMS, numsString);
				values.put(Sales.SELLING_PRICE, priceString);
				long timestamp = System.currentTimeMillis();
				values.put(Sales.TIMESTAMP, timestamp);
				values.put(Sales.DATE, formatDate(timestamp));
				if (db.insert(Sales.TABLENAME, null, values) != -1) {
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
