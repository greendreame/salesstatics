package com.tjcuxulin.salesstatic;

import java.text.DateFormat;

import com.tjcuxulin.salesstatic.db.MySqliteOpenHelper;
import com.tjcuxulin.salesstatic.model.PurchaseList;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends Activity {
	protected SQLiteDatabase db;
	protected Toast mToast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (!(this instanceof MainActivity)) {
			db = new MySqliteOpenHelper(getApplicationContext()).getWritableDatabase();
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (db != null) {
			db.close();
		}
	}
	
	protected void showToast(int resid) {
        if (mToast == null) {
            mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        }
        mToast.setText(resid);
        mToast.show();
    }
	
	public boolean isstringEmpty(String str) {
		if (str == null || str.equals("null") || str.equals("")) {
			return true;
		}
		
		return false;
	}
	
	public String formatDate(long time) {
		DateFormat formatter = DateFormat.getDateInstance();
		return formatter.format(time);
	}
	
	public String getStandard(String nameString) {
		String result = null;
		if (db != null) {
			Cursor cursor = db.query(true, PurchaseList.TABLENAME,
					new String[] { PurchaseList.PURCHASE_STANDARD },
					PurchaseList.NAME + " = '" + nameString + "' and (" + PurchaseList.PURCHASE_STANDARD + " != NULL or " + PurchaseList.PURCHASE_STANDARD + " != '')", null,
					null, null, null, null);
			if (cursor.getCount() > 0) {
				cursor.moveToNext();
				result = cursor.getString(0);
			}
			
			cursor.close();
			cursor = null;
		}
		
		return result;
	}
	
	public String getInstruction(String nameString) {
		String result = null;
		if (db != null) {
			Cursor cursor = db.query(true, PurchaseList.TABLENAME,
					new String[] { PurchaseList.PURCHASE_INSTRUCTION },
					PurchaseList.NAME + " = '" + nameString + "' and (" + PurchaseList.PURCHASE_INSTRUCTION + " != NULL or " + PurchaseList.PURCHASE_INSTRUCTION + " != '')", null,
					null, null, null, null);
			if (cursor.getCount() > 0) {
				cursor.moveToNext();
				result = cursor.getString(0);
			}
			
			cursor.close();
			cursor = null;
		}
		
		return result;
	}
}
