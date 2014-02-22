package com.tjcuxulin.salesstatic;


import android.content.Intent;
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
		Button error = (Button) findViewById(R.id.main_entry);
		Button purchase = (Button) findViewById(R.id.main_purchase);
		Button sales = (Button) findViewById(R.id.main_sales);
		Button search = (Button) findViewById(R.id.main_search);

		total.setOnClickListener(this);
		error.setOnClickListener(this);
		purchase.setOnClickListener(this);
		sales.setOnClickListener(this);
		search.setOnClickListener(this);
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
		case R.id.main_entry:
			startActivity(new Intent(getApplicationContext(), EntryActivity.class));
			break;
		case R.id.main_purchase:
			startActivity(new Intent(getApplicationContext(), PurchaseActivity.class));
			break;
		case R.id.main_sales:
			startActivity(new Intent(getApplicationContext(), SalesActivity.class));
			break;
		case R.id.main_search:
			startActivity(new Intent(getApplicationContext(), SearchActivity.class));
			break;
		case R.id.main_total:
			startActivity(new Intent(getApplicationContext(), TotalActivity.class));
			break;

		default:
			break;
		}
	}

}
