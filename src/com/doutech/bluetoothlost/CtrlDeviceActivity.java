package com.doutech.bluetoothlost;

import java.io.File;






import com.doutech.bluetoothlost.bean.DeviceInfo;
import com.doutech.bluetoothlost.dao.Devicedb;
import com.doutech.bluetoothlost.utils.Utils;
import com.doutech.bluetoothlost.view.CircleButton;
import com.doutech.bluetoothlost.view.CircleImageView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class CtrlDeviceActivity extends Activity implements OnClickListener,
		OnTouchListener, android.view.GestureDetector.OnGestureListener {

	private DeviceInfo deviceinfo;
	private CircleImageView deviceIcon;
	private CircleButton btn_call;
	private int selectedIconId;
	private LinearLayout layout;
	private Devicedb db;
	private GestureDetector mGestureDetector;
	private ProgressBar pb;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ctrldevice);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);// 图标是否显示
		actionBar.setDisplayHomeAsUpEnabled(true);// 向左箭头
		db = new Devicedb(this);
		Bundle bundle = getIntent().getExtras();
		deviceinfo = (DeviceInfo) bundle.getSerializable("deviceinfo");
		
		layout = (LinearLayout) findViewById(R.id.layout);
		layout.setOnTouchListener(this);
		
		deviceIcon = (CircleImageView) findViewById(R.id.devicephoto);
		btn_call = (CircleButton) findViewById(R.id.btn_call);
		pb = (ProgressBar) findViewById(R.id.pb);
		
		mGestureDetector = new GestureDetector(this);
		if (deviceinfo.getIsSystemIcon() == 1) {
			// 系统图标
			TypedArray ta = getResources().obtainTypedArray(R.array.iconarray);
			selectedIconId = ta.getResourceId(deviceinfo.getIconIndex(), 0);
			ta.recycle();
			deviceIcon.setImageResource(selectedIconId);
		} else {
			// 不是系统图标 SD卡或者其他地方的
			File mfile = new File(deviceinfo.getPhotoName());
			if (mfile.exists()) {// 若该文件存在
				Bitmap bm = BitmapFactory.decodeFile(deviceinfo.getPhotoName());
				deviceIcon.setImageBitmap(bm);
			}
		}
		actionBar.setTitle(deviceinfo.getDeviceName());
		//广播接收器
		BroadcastReceiver mReceiver=new BroadcastReceiver(){
			

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				String deviceMac=intent.getStringExtra("deviceMac");
				if(deviceMac.equals(deviceinfo.getDeviceMac())){
					int deviceDistance=intent.getIntExtra("deviceDistance", 100);
					pb.setProgress(deviceDistance);
				}
			}
		};
		IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
		registerReceiver(mReceiver, intentFilter);  
			 
		btn_call.setOnClickListener(this);
		Utils.showToastMsg(getApplicationContext(), deviceinfo.getDeviceDistance()+ " ");
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_call) {
			// 跳转到搜寻设备界面
			Intent intent = new Intent(CtrlDeviceActivity.this,
					SearchActivity.class);
			intent.putExtra("deviceinfo", deviceinfo);
			startActivity(intent);

		}
	}

	public void onBackPressed() {
		finish();
		Intent intent = new Intent(CtrlDeviceActivity.this,
				AlreadyAddedListActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menuofctrlactivity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			finish();
			Intent intent1 = new Intent(CtrlDeviceActivity.this,
					AlreadyAddedListActivity.class);
			startActivity(intent1);
			break;
		case R.id.setting:
			finish();
			Intent intent2 = new Intent(CtrlDeviceActivity.this,
					AddDeviceActivity.class);
			Bundle bundle2 = new Bundle();
			bundle2.putSerializable("deviceinfo", deviceinfo);
			bundle2.putInt("fromType", 2);
			intent2.putExtras(bundle2);
			startActivity(intent2);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		Utils.showToastMsg(CtrlDeviceActivity.this, "onTouch");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub

		return true;

	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.d("onLongPress", e.toString());
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		int deviceID = db.getID(deviceinfo.getDeviceName());
		int x = (int) (e2.getX() - e1.getX());
		Utils.showToastMsg(CtrlDeviceActivity.this, x + ""+deviceID);
		if (x > 0) {
			while(true){
				if (deviceID > 0) {
					deviceID--;
					DeviceInfo newDeviceInfo = db.selectFormID(deviceID);
					if (newDeviceInfo != null) {
	
						finish();
						Intent intent = new Intent(CtrlDeviceActivity.this,
								CtrlDeviceActivity.class);
						intent.putExtra("deviceinfo", newDeviceInfo);
						startActivity(intent);
						overridePendingTransition(R.anim.push_right_in,
								R.anim.push_right_out);
						break;
					}
				}
				else{
					break;
				}
			}
		} else if (x < -0) {
			while(true){
				if(deviceID<db.getLastID()){
					deviceID++;
					DeviceInfo newDeviceInfo1 = db.selectFormID(deviceID);
					if (newDeviceInfo1 != null) {
						finish();
						Intent intent = new Intent(CtrlDeviceActivity.this,
								CtrlDeviceActivity.class);
						intent.putExtra("deviceinfo", newDeviceInfo1);
						startActivity(intent);
						overridePendingTransition(R.anim.push_left_in,
								R.anim.push_left_out);
						break;
					}
				}else{
					break;
				}
			}
		} else {

		}

		return false;
	}
}
