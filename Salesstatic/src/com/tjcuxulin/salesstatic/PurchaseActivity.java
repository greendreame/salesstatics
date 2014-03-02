package com.tjcuxulin.salesstatic;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

import com.tjcuxulin.salesstatic.control.MyAutoCompleteAdapter;
import com.tjcuxulin.salesstatic.db.MySqliteOpenHelper;
import com.tjcuxulin.salesstatic.model.Merchandise;
import com.tjcuxulin.salesstatic.model.Purchase;
import com.tjcuxulin.salesstatic.util.SalesUtil;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class PurchaseActivity extends BaseActivity {
	private MyAutoCompleteAdapter adapter;
	private ArrayList<SimpleEntry<Integer, String>> list;
	private long _id = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_purchase);

		list = new ArrayList<SimpleEntry<Integer, String>>();

		final AutoCompleteTextView nameView = (AutoCompleteTextView) findViewById(R.id.purchase_name);
		final EditText numsView = (EditText) findViewById(R.id.purchase_nums);
		final EditText priceView = (EditText) findViewById(R.id.purchase_price);
		final EditText sellpriceView = (EditText) findViewById(R.id.purchase_sell_price);
		final EditText standardView = (EditText) findViewById(R.id.purchase_standard);
		final EditText instructionView = (EditText) findViewById(R.id.purchase_instruction);
		Button ok = (Button) findViewById(R.id.purchase_ok);
		adapter = new MyAutoCompleteAdapter(getApplicationContext(),
				R.layout.autocomplete_items);
		nameView.setAdapter(adapter);

		nameView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String keyWord = s.toString();
				
				if (_id != -1) {
					_id = -1;
					numsView.setText(null);
					priceView.setText(null);
					priceView.setText(null);
					standardView.setText(null);
					instructionView.setText(null);
				}
				
				Cursor cursor = db.query(true, Merchandise.TABLENAME,
						new String[] { MySqliteOpenHelper._ID, Merchandise.NAME },
						Merchandise.NAME + " like '%" + keyWord + "%' or "
								+ Merchandise.NAME_FIRST_CHARS + " like '"
								+ keyWord + "%' or " + Merchandise.NAME_PINYIN
								+ " like '" + keyWord + "%'", null, null, null,
						null, null);

				adapter.clear();
				list.clear();
				if (cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						SimpleEntry<Integer, String> entry = new SimpleEntry<Integer, String>(
								cursor.getInt(0), cursor.getString(1));
						list.add(entry);
						adapter.add(cursor.getString(1));
					}
				}
				
				adapter.notifyDataSetChanged();

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
				_id = list.get(0).getKey();
				Cursor cursor = getCursorById(_id, Merchandise.TABLENAME);
				if (cursor.moveToNext()) {
					standardView.setText(cursor.getString(cursor
							.getColumnIndex(Merchandise.STANDARD)));
					instructionView.setText(cursor.getString(cursor
							.getColumnIndex(Merchandise.INSTRUCTION)));
				}
				cursor.close();
				cursor = null;
			}
		});

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String nameString = nameView.getText().toString();
				if (isStringEmpty(nameString)) {
					showToast(R.string.sales_name_error);
					return;
				}

				String numsString = numsView.getText().toString();
				if (isStringEmpty(numsString)) {
					showToast(R.string.purchase_nums_error);
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
				
				String sellpriceString = sellpriceView.getText().toString();
				if (isStringEmpty(sellpriceString)) {
					showToast(R.string.purchase_price_error);
					return;
				} else {
					try {
						Float.parseFloat(sellpriceString);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						showToast(R.string.purchase_sell_price_error);
						return;
					}
				}

				String standardString = standardView.getText().toString();
				String instructionString = instructionView.getText()
						.toString();
				ContentValues values = new ContentValues();

				values.put(Merchandise.NAME, nameString);
				values.put(Merchandise.NAME_FIRST_CHARS,
						SalesUtil.getPinYinHeadChar(nameString));
				values.put(Merchandise.NAME_PINYIN,
						SalesUtil.getPinYin(nameString));
				values.put(Merchandise.STANDARD, standardString);
				values.put(Merchandise.INSTRUCTION, instructionString);
				values.put(Merchandise.SELL_PRICE, sellpriceString);
				if (_id == -1) {
					values.put(MySqliteOpenHelper._ID, nameString.hashCode());
					_id = db.insert(Merchandise.TABLENAME, null, values);
				} else {
					db.update(Merchandise.TABLENAME, values, MySqliteOpenHelper._ID
							+ "=" + _id, null);
				}

				values.clear();

				if (_id == -1) {
					showToast(R.string.purchase_error);
					return;
				}

				values.put(Purchase.MERCHANDISE_ID, _id);
				values.put(Purchase.PURCHASE_NUMS, numsString);
				values.put(Purchase.PURCHASE_PRICE, priceString);
				long timestamp = System.currentTimeMillis();
				values.put(Purchase.PURCHASE_TIMESTAMP, timestamp);
				values.put(Purchase.PURCHASE_DATE, formatDate(timestamp));
				if (db.insert(Purchase.TABLENAME, null, values) != -1) {
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
