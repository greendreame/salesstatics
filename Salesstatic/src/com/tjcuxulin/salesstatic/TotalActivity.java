package com.tjcuxulin.salesstatic;

import com.tjcuxulin.salesstatic.model.Merchandise;
import com.tjcuxulin.salesstatic.model.Purchase;
import com.tjcuxulin.salesstatic.model.Sales;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

public class TotalActivity extends BaseActivity {
	private final String TAG = "Totalactivity";
	private TableLayout parent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_total);
		parent = (TableLayout) findViewById(R.id.total_parent);

		TextView totalPurchseView = (TextView) findViewById(R.id.total_purchase_total);
		TextView totalSalesView = (TextView) findViewById(R.id.total_sales_total);
		Cursor cursor = db.query(true, Purchase.TABLENAME,
				new String[] { "sum(" + Purchase.PURCHASE_PRICE + "*"
						+ Purchase.PURCHASE_NUMS + ")" }, null, null, null,
				null, null, null);
		if (cursor.moveToNext()) {
			totalPurchseView.setText(getString(R.string.total_purchase_total,
					cursor.getFloat(0)));
		}
		cursor.close();
		cursor = null;

		cursor = db.query(true, Sales.TABLENAME, new String[] { "sum("
				+ Sales.SALES_NUMS + "*" + Sales.SELLING_PRICE + ")" }, null,
				null, null, null, null, null);
		if (cursor.moveToNext()) {
			totalSalesView.setText(getString(R.string.total_sales_total,
					cursor.getFloat(0)));
		}
		cursor.close();
		cursor = null;

		if (isVisiable == false) {
			totalPurchseView.setVisibility(View.GONE);
			totalSalesView.setVisibility(View.GONE);
		}

		loadData();
	}

	private void loadData() {
		Cursor cursor = db.query(true, Purchase.TABLENAME,
				new String[] { Purchase.MERCHANDISE_ID }, null, null, null,
				null, Purchase.PURCHASE_TIMESTAMP + " desc", null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				long _id = cursor.getLong(0);
				String whereClause = Purchase.MERCHANDISE_ID + " = " + _id;
				float purchase = 0;
				Cursor c = db
						.query(true, Purchase.TABLENAME,
								new String[] { "total("
										+ Purchase.PURCHASE_NUMS + ")" },
								whereClause, null, null, null, null, null);
				if (c.getCount() > 0) {
					c.moveToNext();
					purchase = c.getFloat(0);
				} else {
					Log.w(TAG, "can not get purchase total");
					continue;
				}
				c.close();
				c = null;

				String priceRange = null;
				c = db.query(true, Purchase.TABLENAME,
						new String[] { Purchase.PURCHASE_PRICE }, whereClause,
						null, null, null, null, null);
				if (c.getCount() > 0) {
					float min = Float.MAX_VALUE;
					float max = Float.MIN_VALUE;
					while (c.moveToNext()) {
						float value = c.getFloat(0);
						if (value != 0) {
							if (value < min) {
								min = value;
							}

							if (value > max) {
								max = value;
							}
						}
					}
					if (min == max) {
						priceRange = min + "";
					} else {
						if (min != Float.MAX_VALUE) {
							priceRange = min + "~" + max;
						}
					}
				} else {
					Log.w(TAG, "can not get the price");
				}
				c.close();
				c = null;

				float sales = 0;
				whereClause = Sales.MERCHANDISE_ID + " = " + _id;
				c = db.query(true, Sales.TABLENAME, new String[] { "total("
						+ Sales.SALES_NUMS + ")" }, whereClause, null, null,
						null, null, null);
				if (c.getCount() > 0) {
					c.moveToNext();
					sales = c.getFloat(0);
				}
				c.close();
				c = null;

				if (_id != -1 && purchase != 0) {
					c = getCursorById(_id, Merchandise.TABLENAME);
					if (c.moveToNext()) {
						addRow(c.getString(c.getColumnIndex(Merchandise.NAME)),
								purchase,
								priceRange,
								sales,
								c.getString(c
										.getColumnIndex(Merchandise.STANDARD)),
								c.getString(c
										.getColumnIndex(Merchandise.INSTRUCTION)));
					}
				}

			}
		}
		cursor.close();
		cursor = null;

		if (isVisiable == false) {
			TextView priceView = (TextView) findViewById(R.id.total_price);
			priceView.setVisibility(View.GONE);
		}
	}

	private void addRow(String nameString, float purchase, String priceRange,
			float sales, String standardStr, String instructionStr) {
		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.total_item, null);
		TextView nameView = (TextView) view.findViewById(R.id.total_item_name);
		nameView.setText(nameString);
		TextView purchaseView = (TextView) view
				.findViewById(R.id.total_item_purchase);
		purchaseView.setText(purchase + "");
		TextView priceView = (TextView) view
				.findViewById(R.id.total_item_price);
		priceView.setText(priceRange);
		if (isVisiable == false) {
			priceView.setVisibility(View.GONE);
		}
		TextView salesView = (TextView) view
				.findViewById(R.id.total_item_sales);
		salesView.setText(sales + "");
		TextView inventoryView = (TextView) view
				.findViewById(R.id.total_item_inventory);
		inventoryView.setText((purchase - sales) + "");
		TextView standardView = (TextView) view
				.findViewById(R.id.total_item_standard);
		standardView.setText(standardStr);
		TextView instructionView = (TextView) view
				.findViewById(R.id.total_item_instruction);
		instructionView.setText(instructionStr);
		parent.addView(view);
	}
}
