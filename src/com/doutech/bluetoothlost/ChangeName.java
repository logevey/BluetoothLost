package com.doutech.bluetoothlost;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class ChangeName extends Activity {

	private EditText et;
	private ActionBar actionBar;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changename);
		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);//图标是否显示
        actionBar.setDisplayHomeAsUpEnabled(true);//向左箭头
        actionBar.setTitle("修改名字");
        actionBar.setHomeButtonEnabled(true);
		
		et = (EditText) findViewById(R.id.et);
		et.setFocusable(true);
		et.requestFocus();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menusave, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.save) {
			Intent data=new Intent();  
	        data.putExtra("DeviceName", et.getText().toString());
	        setResult(12, data);
			finish();
		}else if (id == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
	
}
