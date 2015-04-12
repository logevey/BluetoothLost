package com.doutech.bluetoothlost;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class ChooseIconActivity extends Activity {

	private HashMap<String, Object> iconmap;
	private GridView gv;
	private ArrayList<HashMap<String, Object>> items;
	private SimpleAdapter gvAdapter;
	private ActionBar actionBar;
	private int selectedIndex;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chooseicon);
		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);//图标是否显示
        actionBar.setDisplayHomeAsUpEnabled(true);//向左箭头
        actionBar.setTitle("选择图标");
        actionBar.setHomeButtonEnabled(true);
		
		initData();
		gvAdapter = new SimpleAdapter(this, items,R.layout.item_gridview,new String[] {"icon"},new int[] {R.id.chooseicon});
		gv = (GridView) findViewById(R.id.gv);
		gv.setAdapter(gvAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				gv.setSelector(getResources().getDrawable(R.drawable.item_bg));
				selectedIndex = position;
			}
		});
	}
	
	private void initData() {
		TypedArray ta = getResources().obtainTypedArray(R.array.iconarray);
		// ta.getDrawable(index);
		int len = ta.length();
		int[] iconIds = new int[len];
		for (int i = 0; i < len; i++) {
			iconIds[i] = ta.getResourceId(i, 0);
		}
		ta.recycle();
		items = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < iconIds.length; i++) {
			iconmap = new HashMap<String, Object>();
			iconmap.put("icon", iconIds[i]);
			items.add(iconmap);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.finish) {
			Intent data=new Intent();  
	        data.putExtra("iconIndex", selectedIndex);
	        setResult(11, data);
			finish();
		}else if (id == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
	
}
