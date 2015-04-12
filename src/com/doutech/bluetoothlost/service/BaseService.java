package com.doutech.bluetoothlost.service;

import com.doutech.bluetoothlost.ConnectDevice;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;

public class BaseService extends Service {
	
	private static final String  TAG = BaseService.class.getSimpleName();
	
	private BluetoothDevice firstDevice;
	
	private ConnectDevice mConnectDevice;
	
	private Thread mThread;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		System.out.println("i am service of ConnService...");
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		//mDevices = intent.getExtras().getParcelableArrayList("mDevices");
		//final BluetoothDevice device = mDevices.get(0);
		firstDevice = intent.getExtras().getParcelable("firstDevice");
		final BluetoothDevice device = firstDevice;
		System.out.println("i an ssert of statjnt connmam g .."+device.getName());
		System.out.println("the msgedeivice os  name ...");
		
		mConnectDevice = new ConnectDevice(this);
		
		mThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				
				mConnectDevice.initialize();
				mConnectDevice.connectDevice(device.getAddress());
				
				if(mConnectDevice.isConnected(device))
				{
					System.out.println("the device isConnected...");
				}else {
					mConnectDevice.connectDevice(device.getAddress());
				}
			}
		});
		
		
		mThread.start();
		
		return super.onStartCommand(intent, flags, startId);
	}
			

}
