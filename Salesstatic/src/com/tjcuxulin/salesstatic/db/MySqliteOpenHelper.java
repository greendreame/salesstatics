package com.tjcuxulin.salesstatic.db;

import com.tjcuxulin.salesstatic.model.PurchaseList;
import com.tjcuxulin.salesstatic.model.SalesList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteOpenHelper extends SQLiteOpenHelper {
	private final static int DB_VERSION = 1;
	private final static String DB_NAME = "salesstatics";
	
	private static final String CREATE_TABLE_PURCHASE = "create table "
			+ PurchaseList.TABLENAME + " ("
			+ PurchaseList._ID
			+ " integer not null primary key autoincrement, "
			+ PurchaseList.NAME + " TEXT, "
			+ PurchaseList.PURCHASE_NUMS + " float, "
			+ PurchaseList.PURCHASE_PRICE + " float,"
			+ PurchaseList.PURCHASE_STANDARD + " TEXT, "
			+ PurchaseList.PURCHASE_INSTRUCTION + " TEXT)";
	
	private static final String CREATE_TABLE_SALES = "create table "
			+ SalesList.TABLENAME + " ("
			+ SalesList._ID
			+ " integer not null primary key autoincrement, "
			+ SalesList.NAME + " TEXT, "
			+ SalesList.SELLING_PRICE + " float, "
			+ SalesList.SALES_NUMS + " float,"
			+ SalesList.CUSTOMER + " TEXT, "
			+ SalesList.CUSTOMER_CELL_PHONE + " TEXT, "
			+ SalesList.CUSTOMER_TELEPHONE + " TEXT, "
			+ SalesList.CUSTOMER_DEMAND + " TEXT)";
	
	public MySqliteOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE_PURCHASE);
		db.execSQL(CREATE_TABLE_SALES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
