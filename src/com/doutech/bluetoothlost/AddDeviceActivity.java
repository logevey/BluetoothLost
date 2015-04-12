package com.doutech.bluetoothlost;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.doutech.bluetoothlost.bean.DeviceInfo;
import com.doutech.bluetoothlost.dao.Devicedb;
import com.doutech.bluetoothlost.service.BaseService;
import com.doutech.bluetoothlost.utils.SelectPicPopupWindow;
import com.doutech.bluetoothlost.utils.Utils;
import com.doutech.bluetoothlost.view.CircleImageView;

public class AddDeviceActivity extends Activity implements OnClickListener {

	private Devicedb db;
	private ActionBar actionBar;
	private int returnIconId;
	private CircleImageView circleImageView;
	private TextView deviceName;
	private EditText etDeviceName;
	private TextView disattach;
	private int iconIndex;
	private int fromType;
	private String device_name;
	private DeviceInfo info;
	private Switch detailalarmswitch;
	private SeekBar detaildistanceseekbar;
	private SelectPicPopupWindow menuWindow;
	private int isSystemIcon=1;
	private String photoName=null;
	private RelativeLayout nameLayout;
	private BluetoothDevice firstDevice;
	private String deviceMac;
	int deviceDistance;
	Intent connServIntent;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adddevice);
		db = new Devicedb(this);
		Bundle bundle = getIntent().getExtras();
		fromType = (int)bundle.getInt("fromType");
		
		
		detailalarmswitch = (Switch) findViewById(R.id.detailalarmswitch);
	    detaildistanceseekbar = (SeekBar) findViewById(R.id.detaildistanceseekbar);
	    detaildistanceseekbar.setOnSeekBarChangeListener(changeListener);
		circleImageView = (CircleImageView) findViewById(R.id.device_icon);
		circleImageView.setOnClickListener(this);
		nameLayout=(RelativeLayout) findViewById(R.id.nameLayout);
		nameLayout.setOnClickListener(this);
		deviceName = (TextView) findViewById(R.id.device_name);
		
		deviceName.setOnClickListener(this);
		etDeviceName=(EditText) findViewById(R.id.etName);
		
		
		
		
		disattach=(TextView) findViewById(R.id.disattach);
		if(fromType==2){
			disattach.setVisibility(View.VISIBLE);
			disattach.setOnClickListener(this);
			info=(DeviceInfo)bundle.getSerializable("deviceinfo");
	        detailalarmswitch.setChecked(info.getIsOpen() == 1 ? true: false);
	        detaildistanceseekbar.setProgress(info.getDistance());
	        deviceName.setText(info.getDeviceName());
	        etDeviceName.setText(deviceName.getText());
	        if (info.getIsSystemIcon() == 1) {
				// 系统图标
	        	TypedArray ta = getResources().obtainTypedArray(R.array.iconarray);
	            int selectedIconId = ta.getResourceId(info.getIconIndex(), 0);
	    		ta.recycle();
	    		circleImageView.setImageResource(selectedIconId);
			}else {
				//不是系统图标 SD卡或者其他地方的
				File mfile=new File(info.getPhotoName());
				if (mfile.exists()) {//若该文件存在
					Bitmap bm = BitmapFactory.decodeFile(info.getPhotoName());
					circleImageView.setImageBitmap(bm);
				}
			}
		}else if(fromType==1){
			firstDevice = bundle.getParcelable("firstDevice");
			deviceMac = firstDevice.getAddress();
			deviceDistance = -1;
			deviceName.setText(firstDevice.getName());
			connServIntent = new Intent(this, BaseService.class);
			connServIntent.putExtra("firstDevice", firstDevice);
		}
		etDeviceName.setText(deviceName.getText());
		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);// 图标是否显示
		actionBar.setDisplayHomeAsUpEnabled(true);// 向左箭头
		actionBar.setTitle("返回");
		actionBar.setHomeButtonEnabled(true);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	private OnSeekBarChangeListener changeListener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			Utils.showToastMsg(AddDeviceActivity.this, progress + "%");
		}
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == android.R.id.home) {
			onBackPressed();
		}
		
		
		if (id == R.id.finish) {
			//名字不能为空 后面用名字作为从数据库删除的依据
			deviceName.setText(etDeviceName.getText());
			
			if(fromType==1){
				
				
				startService(connServIntent);
				
				//发送绑定标识，添加进数据库
				if(db.existDevice(deviceMac)){
					Utils.showToastMsg(AddDeviceActivity.this, "This device has added!");
					return false;
				}
				if (deviceName.getText().toString().equals("")) {
					Utils.showToastMsg(AddDeviceActivity.this, "设备名不能为空");
				}else if (db.exist(deviceName.getText().toString())) {
					// 已经存在该设备名字
					Utils.showToastMsg(AddDeviceActivity.this, "已存在同名设备");
				}else {
					// 需要保存数据库
					finish();
					Utils.showToastMsg(AddDeviceActivity.this, "绑定成功，生成默认信息...跳转中");
					
					info = new DeviceInfo(deviceMac,iconIndex, photoName, isSystemIcon, deviceName.getText().toString(), detailalarmswitch.isChecked() == true ? 1:0, detaildistanceseekbar.getProgress(),deviceDistance);
					//写数据库
					db.add(info);
					Intent intent = new Intent(AddDeviceActivity.this, AlreadyAddedListActivity.class);
					intent.putExtra("deviceinfo", info);
					startActivity(intent);
				}
			}else if(fromType==2){
				finish();
				Utils.showToastMsg(AddDeviceActivity.this, "修改成功...跳转中");
				//更新数据库
				info.setIsOpen(detailalarmswitch.isChecked() == true ? 1:0);
				info.setDistance(detaildistanceseekbar.getProgress());
				if(!deviceName.getText().toString().equals(info.getDeviceName())){
					db.updateName(info.getDeviceName(), deviceName.getText().toString());
				}
				db.updateIsSystemIcon(deviceName.getText().toString(),isSystemIcon);
				if(isSystemIcon==1){
					db.updateIcon(deviceName.getText().toString(),iconIndex);
				}else{
					db.updatePhotoName(deviceName.getText().toString(),photoName);
				}
				db.updateSwitch(deviceName.getText().toString(), detailalarmswitch.isChecked() == true ? 1:0);
				db.updateDistance(deviceName.getText().toString(), detaildistanceseekbar.getProgress());
				DeviceInfo info2=db.select(deviceName.getText().toString());
				Intent intent = new Intent(AddDeviceActivity.this, CtrlDeviceActivity.class);
				intent.putExtra("deviceinfo", info2);
				startActivity(intent);
			}else{
				
			}
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.device_icon:
			// 跳转去选择图标
			menuWindow = new SelectPicPopupWindow(this, itemsOnClick);
			menuWindow.showAtLocation(this.findViewById(R.id.aleadyAdd), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.device_name:
			// 跳转去改变名字
			etDeviceName.setVisibility(View.VISIBLE);
			etDeviceName.setText(deviceName.getText().toString());
			etDeviceName.setSelection(etDeviceName.getText().toString().length());
			etDeviceName.setFocusable(true);
			etDeviceName.requestFocus();
			((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(etDeviceName, 0);
			etDeviceName.setFocusableInTouchMode(true);
			deviceName.setVisibility(View.INVISIBLE);
//			Intent intent2 = new Intent(this, ChangeName.class);
//			startActivityForResult(intent2, 10);
			break;
			
		case R.id.disattach:
			// 解除绑定，发送解绑标识，并从数据库删除
			finish();
			db.delete(deviceName.getText().toString());
			Utils.showToastMsg(AddDeviceActivity.this, "接棒成功");
			Intent intent3 = new Intent(this, AlreadyAddedListActivity.class);
			startActivity(intent3);
			break;
		}
		
	}
	 private OnClickListener  itemsOnClick = new OnClickListener(){

			public void onClick(View v) {
				menuWindow.dismiss();
				switch (v.getId()) {
				case R.id.btn_take_photo:
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用android自带的照相机
					startActivityForResult(intent, 12);
					break;
				case R.id.btn_pick_photo:	
					Intent intent2 = new Intent(AddDeviceActivity.this, ChooseIconActivity.class);
					startActivityForResult(intent2, 11);
					break;
				default:
					break;
				}
			}
	    };
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 11) {
			try {
				isSystemIcon=1;
				iconIndex = data.getExtras().getInt("iconIndex");
				TypedArray ta = getResources().obtainTypedArray(R.array.iconarray);
				returnIconId = ta.getResourceId(iconIndex, 0);
				ta.recycle();
				System.out.println("id" + returnIconId);
				circleImageView.setImageResource(returnIconId);
			} catch (Exception e) {
				//按下系统返回键没有Extra
				e.printStackTrace();
			}
		}
		if (requestCode == 10) {
			try {
				device_name = data.getExtras().getString("DeviceName");
				if(!device_name.equals(deviceName.getText().toString()))
				{
					if (device_name.equals("")) {
						Utils.showToastMsg(AddDeviceActivity.this, "设备名不能为空");
					}else if (db.exist(device_name)) {
						// 已经存在该设备名字
						Utils.showToastMsg(AddDeviceActivity.this, "已存在同名设备");
					}else{
						deviceName.setText(device_name);
					}
				}
			} catch (Exception e) {
				//按下系统返回键没有Extra
				e.printStackTrace();
			}
		}
		if (requestCode == 12 && resultCode == Activity.RESULT_OK) {
			isSystemIcon=0;
			String sdStatus = Environment.getExternalStorageState();
			// 检测sd是否可用
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
				Log.v("TestFile","SD card is not avaiable/writeable right now.");
			} else {
				Bundle bundle = data.getExtras();
				// 获取相机返回的数据，并转换为Bitmap图片格式
				Bitmap bitmap = (Bitmap) bundle.get("data");
				FileOutputStream b = null;
				String str = null;
				Date date = null;
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");// 获取当前时间，进一步转化为字符串
				date = new Date(System.currentTimeMillis());
				str = format.format(date);
				String fileName = "/sdcard/douya/icon/" + str + ".jpg";
				photoName=fileName;
				circleImageView.setImageBitmap(bitmap);
				// sendBroadcast(fileName);
				File photo = new File(fileName);
				photo.getParentFile().mkdirs();
				if (!photo.exists()) {
					try {
						photo.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					b = new FileOutputStream(fileName);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
					b.flush();
					b.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		super.onActivityResult(requestCode, resultCode, data);

	}
}
