package com.tjcuxulin.salesstatic.db;

import com.tjcuxulin.salesstatic.model.Customer;
import com.tjcuxulin.salesstatic.model.Merchandise;
import com.tjcuxulin.salesstatic.model.PriceSuggest;
import com.tjcuxulin.salesstatic.model.Purchase;
import com.tjcuxulin.salesstatic.model.Sales;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteOpenHelper extends SQLiteOpenHelper {
	private final static int DB_VERSION = 1;
	private final static String DB_NAME = "salesstatics";
	public static final String _ID = "_id";
	
	private static final String CREATE_TABLE_MERCHANDISE = "create table "
			+ Merchandise.TABLENAME + " ("
			+ _ID
			+ " integer not null primary key, "
			+ Merchandise.NAME + " TEXT UNIQUE, "
			+ Merchandise.NAME_FIRST_CHARS + " TEXT, "
			+ Merchandise.NAME_PINYIN + " TEXT, "
			+ Merchandise.SELL_PRICE + " float,"
			+ Merchandise.STANDARD + " TEXT, "
			+ Merchandise.INSTRUCTION + " TEXT)";
	
	private static final String CREATE_TABLE_CUSTOMER = "create table "
			+ Customer.TABLENAME + " ("
			+ _ID
			+ " integer not null primary key, "
			+ Customer.NAME + " TEXT UNIQUE, "
			+ Customer.NAME_FIRST_CHARS + " TEXT, "
			+ Customer.NAME_PINYIN + " TEXT, "
			+ Customer.CELLPHONE + " TEXT, "
			+ Customer.TELEPHONE + " TEXT, "
			+ Customer.DEMAND + " TEXT)";
	
	private static final String CREATE_TABLE_PURCHASE = "create table "
			+ Purchase.TABLENAME + " ("
			+ _ID
			+ " integer not null primary key autoincrement, "
			+ Purchase.MERCHANDISE_ID + " integer, "
			+ Purchase.PURCHASE_NUMS + " float, "
			+ Purchase.PURCHASE_PRICE + " float,"
			+ Purchase.PURCHASE_DATE + " TEXT, "
			+ Purchase.PURCHASE_TIMESTAMP + " integer UNIQUE)";
	
	private static final String CREATE_TABLE_SALES = "create table "
			+ Sales.TABLENAME + " ("
			+ _ID
			+ " integer not null primary key autoincrement, "
			+ Sales.MERCHANDISE_ID + " integer, "
			+ Sales.SELLING_PRICE + " float, "
			+ Sales.SALES_NUMS + " float,"
			+ Sales.CUSTOMER_ID + " integer, "
			+ Sales.TIMESTAMP + " integer UNIQUE, "
			+ Sales.DATE + " TEXT)";
	
	private static final String CREATE_TABLE_PRICE_SUGGEST = "create table "
			+ PriceSuggest.TABLENAME + " ("
			+ _ID
			+ " integer not null primary key UNIQUE, "
			+ PriceSuggest.MERCHANDISE_ID + " integer, "
			+ PriceSuggest.CUSTOMER_PRICE + " float, "
			+ PriceSuggest.CUSTOMER_ID + " integer)";
	
	public MySqliteOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE_MERCHANDISE);
		db.execSQL(CREATE_TABLE_CUSTOMER);
		db.execSQL(CREATE_TABLE_PURCHASE);
		db.execSQL(CREATE_TABLE_SALES);
		db.execSQL(CREATE_TABLE_PRICE_SUGGEST);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
