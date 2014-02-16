package com.tjcuxulin.salesstatic;

import com.tjcuxulin.salesstatic.model.PurchaseList;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class PurchaseActivity extends BaseActivity {
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_purchase);

		final AutoCompleteTextView nameView = (AutoCompleteTextView) findViewById(R.id.purchase_name);
		final EditText numsView = (EditText) findViewById(R.id.purchase_nums);
		final EditText priceView = (EditText) findViewById(R.id.purchase_price);
		final EditText standardView = (EditText) findViewById(R.id.purchase_standard);
		final EditText instructionView = (EditText) findViewById(R.id.purchase_instruction);
		Button ok = (Button) findViewById(R.id.purchase_ok);
		adapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.autocomplete_items);
		nameView.setAdapter(adapter);

		nameView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String keyWord = s.toString();
				Cursor cursor = db.query(true, PurchaseList.TABLENAME,
						new String[] { PurchaseList.NAME },
						PurchaseList.NAME + " like '%" + keyWord + "%'", null,
						null, null, null, null);

				if (cursor.getCount() > 0) {
					adapter.clear();
					while (cursor.moveToNext()) {
						adapter.add(cursor.getString(0));
					}
					adapter.notifyDataSetChanged();
				}
				
				cursor.close();
			}
		});
		
		nameView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String nameString = nameView.getText().toString();
				String standardString = getStandard(nameString);
				if (standardString != null) {
					standardView.setText(standardString);
				}
				
				String instructionString = getStandard(nameString);
				if (instructionString != null) {
					instructionView.setText(instructionString);
				}
			}
		});

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String nameString = nameView.getText().toString();
				if (isstringEmpty(nameString)) {
					showToast(R.string.sales_name_error);
					return;
				}

				String numsString = numsView.getText().toString();
				if (isstringEmpty(numsString)) {
					showToast(R.string.sales_name_error);
					return;
				}

				String priceString = priceView.getText().toString();
				String standardString = standardView.getText().toString();
				String instructionString = instructionView.getText().toString();
				ContentValues values = new ContentValues();
				values.put(PurchaseList.NAME, nameString);
				values.put(PurchaseList.PURCHASE_NUMS, numsString);
				values.put(PurchaseList.PURCHASE_PRICE, priceString);
				values.put(PurchaseList.PURCHASE_STANDARD, standardString);
				values.put(PurchaseList.PURCHASE_INSTRUCTION, instructionString);
				long timestamp = System.currentTimeMillis();
				values.put(PurchaseList.PURCHASE_TIMESTAMP, timestamp);
				values.put(PurchaseList.PURCHASE_DATE, formatDate(timestamp));
				if (db.insert(PurchaseList.TABLENAME, null, values) != -1) {
					showToast(R.string.purchase_success);
					finish();
				} else {
					showToast(R.string.purchase_error);
				}
			}
		});
	}
}
