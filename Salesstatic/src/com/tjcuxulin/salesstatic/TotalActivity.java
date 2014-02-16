package com.tjcuxulin.salesstatic;

import com.tjcuxulin.salesstatic.model.PurchaseList;
import com.tjcuxulin.salesstatic.model.SalesList;

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
		loadData();
	}

	private void loadData() {
		Cursor cursor = db.query(true, PurchaseList.TABLENAME,
				new String[] { PurchaseList.NAME }, null, null, null, null,
				PurchaseList.PURCHASE_TIMESTAMP + " desc", null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				String nameString = cursor.getString(0);
				float purchase = 0;
				Cursor c = db.query(true, PurchaseList.TABLENAME,
						new String[] { "total(" + PurchaseList.PURCHASE_NUMS
								+ ")" }, PurchaseList.NAME + " = '"
								+ nameString + "'", null, null, null, null,
						null);
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
				c = db.query(true, PurchaseList.TABLENAME,
						new String[] { PurchaseList.PURCHASE_PRICE },
						PurchaseList.NAME + " = '" + nameString + "'", null,
						null, null, null, null);
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
				c = db.query(true, SalesList.TABLENAME, new String[] { "total("
						+ SalesList.SALES_NUMS + ")" }, PurchaseList.NAME
						+ " = '" + nameString + "'", null, null, null, null,
						null);
				if (c.getCount() > 0) {
					c.moveToNext();
					sales = c.getFloat(0);
				}
				c.close();
				c = null;

				if (nameString != null && purchase != 0) {
					addRow(nameString, purchase, priceRange, sales);
				}

			}
		}
		cursor.close();
		cursor = null;
	}

	private void addRow(String nameString, float purchase, String priceRange,
			float sales) {
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
		TextView salesView = (TextView) view
				.findViewById(R.id.total_item_sales);
		salesView.setText(sales + "");
		TextView inventoryView = (TextView) view
				.findViewById(R.id.total_item_inventory);
		inventoryView.setText((purchase - sales) + "");
		TextView standardView = (TextView) view
				.findViewById(R.id.total_item_standard);
		standardView.setText(getStandard(nameString));
		TextView instructionView = (TextView) view
				.findViewById(R.id.total_item_instruction);
		instructionView.setText(getInstruction(nameString));
		parent.addView(view);
	}
}
