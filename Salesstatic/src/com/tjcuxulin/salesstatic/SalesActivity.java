package com.tjcuxulin.salesstatic;

import com.tjcuxulin.salesstatic.control.MyAutoCompleteAdapter;
import com.tjcuxulin.salesstatic.model.PurchaseList;
import com.tjcuxulin.salesstatic.model.SalesList;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class SalesActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sales);
		init();
	}

	private void init() {
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
		final Button continueView = (Button) findViewById(R.id.sales_continue);
		customerView.setAdapter(customerAdapter);
		customerView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String keyWord = s.toString();
				Cursor cursor = db.query(true, SalesList.TABLENAME,
						new String[] { SalesList.NAME }, SalesList.NAME
								+ " like '%" + keyWord + "%' or "
								+ SalesList.NAME_FIRST_CHARS + " like '"
								+ keyWord + "%' or " + SalesList.NAME_PINYIN
								+ " like '" + keyWord + "%'", null, null, null,
						null, null);

				customerAdapter.clear();
				if (cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						customerAdapter.add(cursor.getString(0));
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
		nameView.setAdapter(nameAdapter);
		nameView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String keyWord = s.toString();
				Cursor cursor = db.query(true, PurchaseList.TABLENAME,
						new String[] { PurchaseList.NAME }, PurchaseList.NAME
								+ " like '%" + keyWord + "%' or "
								+ PurchaseList.NAME_FIRST_CHARS + " like '"
								+ keyWord + "%' or " + PurchaseList.NAME_PINYIN
								+ " like '" + keyWord + "%'", null, null, null,
						null, null);

				nameAdapter.clear();
				if (cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						nameAdapter.add(cursor.getString(0));
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

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		continueView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}
}
