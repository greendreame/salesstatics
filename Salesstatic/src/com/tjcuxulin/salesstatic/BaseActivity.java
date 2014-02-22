package com.tjcuxulin.salesstatic;

import com.tjcuxulin.salesstatic.db.MySqliteOpenHelper;
import com.tjcuxulin.salesstatic.util.SalesUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

public class BaseActivity extends Activity {
	protected SQLiteDatabase db;
	protected Toast mToast;
	protected static final int DELAY_TIME = 500;
	protected static final int MSG_FINISH = 0;
	protected static Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_FINISH:
				BaseActivity baseActivity = (BaseActivity) msg.obj;
				baseActivity.finish();
				break;

			default:
				break;
			}
		};
	};
//	protected ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (!(this instanceof MainActivity)) {
			db = new MySqliteOpenHelper(getApplicationContext())
					.getWritableDatabase();
//			progressDialog = new ProgressDialog(this);
//			progressDialog.setTitle(R.string.dialog_loading_title);
//			progressDialog.setMessage(getString(R.string.dialog_loading_content));
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (db != null) {
			db.close();
			db = null;
		}
	}

	protected void showToast(int resid) {
		if (mToast == null) {
			mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		}
		mToast.setText(resid);
		mToast.show();
	}

	public boolean isStringEmpty(String str) {
		return TextUtils.isEmpty(str);
	}

	public String formatDate(long timeStamp) {
		return SalesUtil.getDateFormatString("yyyy-MM-dd", timeStamp);
	}

	public Cursor getCursorById(long id, String table) {
		if (db != null && id != -1) {
			return db.query(true, table, null, "_id = " + id, null, null, null,
					null, null);
		}

		return null;
	}

	public String getColumnString(String columnName, long _id, String table) {
		String result = null;
		if (db != null && !TextUtils.isEmpty(columnName) && _id != -1) {
			Cursor cursor = db.query(true, table, new String[] { columnName },
					"_id = " + _id, null, null, null, null, null);
			if (cursor.getCount() > 0) {
				result = cursor.getString(0);
			}
			cursor.close();
			cursor = null;
		}

		return result;
	}
	
	protected void showChooseDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(id);
		builder.setPositiveButton(R.string.ok, null);
		builder.setNegativeButton(R.string.cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.what = MSG_FINISH;
				msg.obj = BaseActivity.this;
				handler.sendMessageDelayed(msg, DELAY_TIME);
			}
		});
		builder.create().show();
	}
}
