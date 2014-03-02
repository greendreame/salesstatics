package com.tjcuxulin.salesstatic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.tjcuxulin.salesstatic.db.MySqliteOpenHelper;
import com.tjcuxulin.salesstatic.model.Customer;
import com.tjcuxulin.salesstatic.model.Merchandise;
import com.tjcuxulin.salesstatic.model.PriceSuggest;
import com.tjcuxulin.salesstatic.model.Purchase;
import com.tjcuxulin.salesstatic.model.Sales;
import com.tjcuxulin.salesstatic.util.SalesUtil;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button total = (Button) findViewById(R.id.main_total);
		Button suggestIn = (Button) findViewById(R.id.main_suggest_in);
		Button suggestOut = (Button) findViewById(R.id.main_suggest_out);
		Button dataImport = (Button) findViewById(R.id.main_import);
		Button dataExport = (Button) findViewById(R.id.main_export);
		Button purchase = (Button) findViewById(R.id.main_purchase);
		Button sales = (Button) findViewById(R.id.main_sales);
		Button search = (Button) findViewById(R.id.main_search);

		total.setOnClickListener(this);
		suggestIn.setOnClickListener(this);
		suggestOut.setOnClickListener(this);
		dataExport.setOnClickListener(this);
		dataImport.setOnClickListener(this);
		purchase.setOnClickListener(this);
		sales.setOnClickListener(this);
		search.setOnClickListener(this);

		if (isVisiable == false) {
			suggestIn.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.main_suggest_out:
			startActivity(new Intent(getApplicationContext(),
					SuggestOutActivity.class));
			break;
		case R.id.main_purchase:
			startActivity(new Intent(getApplicationContext(),
					PurchaseActivity.class));
			break;
		case R.id.main_sales:
			startActivity(new Intent(getApplicationContext(),
					SalesActivity.class));
			break;
		case R.id.main_search:
			startActivity(new Intent(getApplicationContext(),
					SearchActivity.class));
			break;
		case R.id.main_total:
			startActivity(new Intent(getApplicationContext(),
					TotalActivity.class));
			break;
		case R.id.main_suggest_in:
			startActivity(new Intent(getApplicationContext(),
					SuggestInActvity.class));
			break;
		case R.id.main_import:
			importData();
			break;
		case R.id.main_export:
			exportData();
			break;
		default:
			break;
		}
	}

	private void exportData() {
		SalesUtil.saveStringToFile("jxcData", "", false);
		StringBuilder builder = new StringBuilder();
		try {
			Cursor cursor = db.query(true, Merchandise.TABLENAME, null, null,
					null, null, null, null, null);
			while (cursor.moveToNext()) {
				builder.append(Merchandise.TABLENAME + ",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(MySqliteOpenHelper._ID))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Merchandise.NAME))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Merchandise.NAME_FIRST_CHARS))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Merchandise.NAME_PINYIN))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Merchandise.SELL_PRICE))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Merchandise.STANDARD))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Merchandise.INSTRUCTION))));
				builder.append("\n");
				SalesUtil.saveStringToFile("jxcData", builder.toString(), true);
			}
			cursor.close();
			cursor = null;
			builder.setLength(0);

			cursor = db.query(true, Customer.TABLENAME, null, null, null, null,
					null, null, null);
			while (cursor.moveToNext()) {
				builder.append(Customer.TABLENAME + ",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(MySqliteOpenHelper._ID))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Customer.NAME))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Customer.NAME_FIRST_CHARS))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Customer.NAME_PINYIN))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Customer.CELLPHONE))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Customer.TELEPHONE))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Customer.DEMAND))));
				builder.append("\n");
				SalesUtil.saveStringToFile("jxcData", builder.toString(), true);
			}
			cursor.close();
			cursor = null;
			builder.setLength(0);

			cursor = db.query(true, Purchase.TABLENAME, null, null, null, null,
					null, null, null);
			while (cursor.moveToNext()) {
				builder.append(Purchase.TABLENAME + ",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Purchase.MERCHANDISE_ID))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Purchase.PURCHASE_NUMS))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Purchase.PURCHASE_PRICE))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Purchase.PURCHASE_TIMESTAMP))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Purchase.PURCHASE_DATE))));
				builder.append("\n");
				SalesUtil.saveStringToFile("jxcData", builder.toString(), true);
			}
			cursor.close();
			cursor = null;
			builder.setLength(0);

			cursor = db.query(true, Sales.TABLENAME, null, null, null, null,
					null, null, null);
			while (cursor.moveToNext()) {
				builder.append(Sales.TABLENAME + ",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Sales.MERCHANDISE_ID))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Sales.SALES_NUMS))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Sales.CUSTOMER_ID))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Sales.SELLING_PRICE))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Sales.TIMESTAMP))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(Sales.DATE))));
				builder.append("\n");
				SalesUtil.saveStringToFile("jxcData", builder.toString(), true);
			}
			cursor.close();
			cursor = null;
			builder.setLength(0);

			cursor = db.query(true, PriceSuggest.TABLENAME, null, null, null,
					null, null, null, null);
			while (cursor.moveToNext()) {
				builder.append(PriceSuggest.TABLENAME + ",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(PriceSuggest.MERCHANDISE_ID))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(PriceSuggest.CUSTOMER_PRICE))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(PriceSuggest.CUSTOMER_ID))));
				builder.append(",");
				builder.append(getEncodeResult(cursor.getString(cursor
						.getColumnIndex(MySqliteOpenHelper._ID))));
				builder.append("\n");
				SalesUtil.saveStringToFile("jxcData", builder.toString(), true);
			}
			cursor.close();
			cursor = null;
			showToast(R.string.main_export_success);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void importData() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(
					SalesUtil.getDataFileAblutePath("jxcData")));
			String line = null;
			while ((line = reader.readLine()) != null) {
				ContentValues values = new ContentValues();
				if (line.startsWith(Merchandise.TABLENAME)) {
					String[] columns = line.split(",");
					values.put(MySqliteOpenHelper._ID, getDecodeResult(columns[1]));
					values.put(Merchandise.NAME, getDecodeResult(columns[2]));
					values.put(Merchandise.NAME_FIRST_CHARS, getDecodeResult(columns[3]));
					values.put(Merchandise.NAME_PINYIN, getDecodeResult(columns[4]));
					values.put(Merchandise.SELL_PRICE, getDecodeResult(columns[5]));
					values.put(Merchandise.STANDARD, getDecodeResult(columns[6]));
					values.put(Merchandise.INSTRUCTION, getDecodeResult(columns[7]));
					db.insert(Merchandise.TABLENAME, null, values);
					values.clear();
				} else if (line.startsWith(Customer.TABLENAME)) {
					String[] columns = line.split(",");
					values.put(MySqliteOpenHelper._ID, getDecodeResult(columns[1]));
					values.put(Customer.NAME, getDecodeResult(columns[2]));
					values.put(Customer.NAME_FIRST_CHARS, getDecodeResult(columns[3]));
					values.put(Customer.NAME_PINYIN, getDecodeResult(columns[4]));
					values.put(Customer.CELLPHONE, getDecodeResult(columns[5]));
					values.put(Customer.TELEPHONE, getDecodeResult(columns[6]));
					values.put(Customer.DEMAND, getDecodeResult(columns[7]));
					db.insert(Customer.TABLENAME, null, values);
					values.clear();
				} else if (line.startsWith(Purchase.TABLENAME)) {
					String[] columns = line.split(",");
					values.put(Purchase.MERCHANDISE_ID, getDecodeResult(columns[1]));
					values.put(Purchase.PURCHASE_NUMS, getDecodeResult(columns[2]));
					values.put(Purchase.PURCHASE_PRICE, getDecodeResult(columns[3]));
					values.put(Purchase.PURCHASE_TIMESTAMP, getDecodeResult(columns[4]));
					values.put(Purchase.PURCHASE_DATE, getDecodeResult(columns[5]));
					db.insert(Purchase.TABLENAME, null, values);
					values.clear();
				} else if (line.startsWith(Sales.TABLENAME)) {
					String[] columns = line.split(",");
					values.put(Sales.MERCHANDISE_ID, getDecodeResult(columns[1]));
					values.put(Sales.SALES_NUMS, getDecodeResult(columns[2]));
					values.put(Sales.CUSTOMER_ID, getDecodeResult(columns[3]));
					values.put(Sales.SELLING_PRICE, getDecodeResult(columns[4]));
					values.put(Sales.TIMESTAMP, getDecodeResult(columns[5]));
					values.put(Sales.DATE, getDecodeResult(columns[6]));
					db.insert(Sales.TABLENAME, null, values);
					values.clear();
				} else if (line.startsWith(PriceSuggest.TABLENAME)) {
					String[] columns = line.split(",");
					values.put(PriceSuggest.MERCHANDISE_ID, getDecodeResult(columns[1]));
					values.put(PriceSuggest.CUSTOMER_PRICE, getDecodeResult(columns[2]));
					values.put(PriceSuggest.CUSTOMER_ID, getDecodeResult(columns[3]));
					values.put(MySqliteOpenHelper._ID, getDecodeResult(columns[4]));
					db.insert(PriceSuggest.TABLENAME, null, values);
					values.clear();
				}
			}
			
			showToast(R.string.main_import_success);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private String getEncodeResult(String orign)
			throws UnsupportedEncodingException {
		return URLEncoder.encode(orign, "utf8");
	}
	
	private String getDecodeResult(String orign)
			throws UnsupportedEncodingException {
		return URLDecoder.decode(orign, "utf8");
	}

}
