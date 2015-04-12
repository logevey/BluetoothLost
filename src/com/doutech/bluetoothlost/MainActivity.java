package com.doutech.bluetoothlost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.doutech.bluetoothlost.utils.DeviceRssi;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private ImageButton ibtn;
	private ImageButton ibtn_button;
	private ActionBar actionBar;
	
	
	private boolean mScanning;
	
	private BluetoothAdapter mBluetoothAdapter;
	private final static int REQUEST_ENABLE_BT =1;
	private Handler mHandler;
	
	private List<BluetoothDevice> mDevices ;
	
	private List<DeviceRssi> mDeviceRssis;
	
	
	//设置搜索BLE设备的时间
	private static final long SCAN_PERIOD = 10000; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		
//		mDeviceRssis = new ArrayList<DeviceRssi>();
		//mHandler = new Handler();
		int isFirst = (int)bundle.getInt("isFirst");
		if(isFirst==1){
			 requestWindowFeature(Window.FEATURE_NO_TITLE);
		}else{
			actionBar = getActionBar();
			actionBar.setDisplayShowHomeEnabled(false);// 图标是否显示
			actionBar.setDisplayHomeAsUpEnabled(true);// 向左箭头
			actionBar.setHomeButtonEnabled(true);
			actionBar.setTitle("已绑定");
		}
		setContentView(R.layout.activity_main);
        ibtn = (ImageButton) findViewById(R.id.adddevice);
        ibtn.setOnClickListener(this);
        ibtn_button= (ImageButton) findViewById(R.id.adddevice_button);
        ibtn_button.setOnClickListener(this);
        mHandler = new Handler();
		mDevices = new ArrayList<BluetoothDevice>();

		mDeviceRssis = new ArrayList<DeviceRssi>();
    	init_ble();
      }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.adddevice:			
			new MyAsyncTask().execute();
			break;
		case R.id.adddevice_button:			
			new MyAsyncTask().execute();
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
		Intent intent = new Intent(MainActivity.this, AlreadyAddedListActivity.class);
		startActivity(intent);
	}
	
	
	
	private class MyAsyncTask extends AsyncTask<Void, Void, Void>{
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(MainActivity.this);
			dialog.setTitle("正在搜索设备...");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			scanLeDevice(true);
			try {
				//后台操作放在此处处理，对蓝牙设备的扫描。现在只是线程睡眠
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		private BluetoothDevice firstDevice;
		
		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();
			if(mDeviceRssis.isEmpty())
			{
				System.out.println("the mDeviceRssis is empty");
				return;
			}
			firstDevice = mDeviceRssis.get(0).getDevice();
			 
			Intent intent = new Intent(MainActivity.this, AddDeviceActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("fromType", 1);//表明是搜索添加的请求转入到AddDeviceActivity界面
			bundle.putParcelable("firstDevice", firstDevice);
			intent.putExtras(bundle);
			finish();
			if(firstDevice != null)
			{
				if(mScanning)
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
			startActivity(intent);
			}
			else
			{
				System.out.println("device is not scanned..");
			}
			
			super.onPostExecute(result);
		}
	}
	
	
	public void init_ble()
	{
		//Use this check to determine whether BLE is supported on the device.
		if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
		{
			
			Toast.makeText(this, "BLE_not_supported", Toast.LENGTH_LONG).show();
			finish();
		}
		
		// Initializes Bluetooth adapter
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		
		if(mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled())
		{
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
		}
		
		
		
	}

	
	private void scanLeDevice(final boolean enable)
	{
		
		if(enable)
		{
			mHandler.postDelayed(new Runnable() {
				
				public void run() {
					
				mScanning = false;
				mBluetoothAdapter.stopLeScan(mLeScanCallback);
					
				}
			}, SCAN_PERIOD);
			
				mScanning = true;
				mBluetoothAdapter.startLeScan(mLeScanCallback);
		}else {
			
				mScanning = false;
				mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
		
		
	}
	
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		
		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			
			
			
			//需要通过判断防丢器与手机的距离，连接与手机最近的防丢器
			
			System.out.println("the Ble device's name is ..."+device.getName());
			System.out.println("the Ble device's rssi is ..."+rssi);
			System.out.println("the Ble device's address is ..."+device.getAddress());
			
			if(!isExistDevice(mDevices, device))
			{
				mDevices.add(device);
				mDeviceRssis.add(new DeviceRssi(device, rssi));
			}
			
			System.out.println("the first device in list is ..."+mDevices.get(0).getName());
			System.out.println("the first device in list is ..."+mDeviceRssis.get(0).getDevice().getName()+"rssi is :"+mDeviceRssis.get(0).getRssi());
			sortDevices();
		}
	};
	
	private void sortDevices() {
		// TODO Auto-generated method stub
		
		Comparator comparator = new Comparator() {

			@Override
			public int compare(Object lhs, Object rhs) {
				// TODO Auto-generated method stub
				
				DeviceRssi deviceRssi1 = (DeviceRssi) lhs;
				DeviceRssi deviceRssi2 = (DeviceRssi) rhs;
				
				if(deviceRssi1.getRssi() < deviceRssi2.getRssi())
					return 1;
				else if(deviceRssi1.getRssi() == deviceRssi2.getRssi())
					return 0;
				else if(deviceRssi1.getRssi() >deviceRssi2.getRssi())
					return -1;
				
				
				
				return 0;
			}
			
		};
		Collections.sort(mDeviceRssis, comparator);
		
		for(int i=0;i<mDeviceRssis.size();i++)
		{
			DeviceRssi dRssi = mDeviceRssis.get(i);
			System.out.println("the rssi is ..."+dRssi.getRssi());
			System.out.println("the device is ..."+dRssi.getDevice().getName());
		}
		
	}
	
	private boolean isExistDevice(List<BluetoothDevice> mDevices, BluetoothDevice device)
	{
		
		for (BluetoothDevice bluetoothDevice : mDevices)
		{
			if(bluetoothDevice.getAddress().equals(device.getAddress()))
			{
				return true;
			}
		}
		
		return false;
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.menuofmain, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
	

}
