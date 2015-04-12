package com.doutech.bluetoothlost;

import com.doutech.bluetoothlost.bean.DeviceInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SearchActivity extends Activity implements OnClickListener {

	private TextView findout;
	private TextView cancle;
	private DeviceInfo deviceinfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		Bundle bundle = getIntent().getExtras();
        deviceinfo = (DeviceInfo)bundle.getSerializable("deviceinfo");
		findout = (TextView) findViewById(R.id.findout);
		cancle = (TextView) findViewById(R.id.cancle);
		findout.setOnClickListener(this);
		cancle.setOnClickListener(this);
		/////////////  根据全局常量在图中绘制出寻找的东西 可以使用WindowMagager的addView方法   ///////////////////////
		/////////////  根据全局常量在图中绘制出寻找的东西 可以使用WindowMagager的addView方法   ///////////////////////
		/////////////  根据全局常量在图中绘制出寻找的东西 可以使用WindowMagager的addView方法   ///////////////////////
		/////////////  根据全局常量在图中绘制出寻找的东西 可以使用WindowMagager的addView方法   ///////////////////////
		/////////////  根据全局常量在图中绘制出寻找的东西 可以使用WindowMagager的addView方法   ///////////////////////
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancle:
			finish();
			Intent intent = new Intent(SearchActivity.this, CtrlDeviceActivity.class);
			intent.putExtra("deviceinfo", deviceinfo);
			startActivity(intent);
			break;
		case R.id.findout:
			finish();
			Intent intent2 = new Intent(SearchActivity.this, CtrlDeviceActivity.class);
			intent2.putExtra("deviceinfo", deviceinfo);
			startActivity(intent2);
			break;
		}
	}
}
