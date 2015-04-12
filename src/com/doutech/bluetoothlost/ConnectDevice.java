package com.doutech.bluetoothlost;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import com.doutech.bluetoothlost.dao.Devicedb;
import com.doutech.bluetoothlost.service.ConnectHandle;
import com.doutech.bluetoothlost.service.GattCallbackResult;
import com.doutech.bluetoothlost.utils.RssitoDistance;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass.Device;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ConnectDevice {

	
	private static final String TAG = ConnectDevice.class.getSimpleName();
	
	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothGatt mBluetoothGatt;
	
	private Context mContext;
	
	GattCallbackResult mGattCallbackResult;
	
	private RssitoDistance rssitoDistance;
	
	public int jishu =0;
	
	public double[] nrssi = new double[100];
	
	public int count = 0;
	
	private double distance;
	private int mdistance;
	
	private Map<String, BluetoothGatt> gattMap = new HashMap();
	
	public int isConnected;
	
	private BluetoothDevice device;
	
	private String deviceAddress;
	
	private Devicedb db;
	public ConnectDevice(Context c)
	{
		mContext = c;
		db = new Devicedb(c);
	}
	
	
	
	@SuppressLint("NewApi") 
	public boolean initialize()
	{
		
		if(mBluetoothManager == null)
		{
			mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				
				Toast.makeText(mContext, "Unable to initialize BluetoothManager", Toast.LENGTH_LONG).show();
				return false;
			}
		}
		
		mBluetoothAdapter = mBluetoothManager.getAdapter();
		
		if (mBluetoothAdapter == null) {
			
			Toast.makeText(mContext, "Unable to obtain a BluetoothAdapter", Toast.LENGTH_LONG).show();
		}
		
		return true;
	}
	
	
	@SuppressLint("NewApi") 
	public boolean connectDevice (final String address)
	{
		
			deviceAddress = address;
			
			if(mBluetoothAdapter == null || address == null)
			{
				Log.w(TAG, "BluetoothAdapter not initialized or unspecified address ");
				return false;
				
			}
			
			final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
			System.out.println("get the remoteDevice success.");
			
			if (device == null) {
				
				Log.d(TAG, "device not found. Unable to connect.");
				return false;
			}
			
			mBluetoothGatt = device.connectGatt(this.mContext, false, mBluetoothGattCallback);
			if (mBluetoothGatt == null) {
				
				Log.d(TAG, "gatt not connect.");
				return false;
				
			}else {
				System.out.println(" gatt of "+device.getName()+" is connect success...");
			}
		
			
			return true;
		
	}
	
	  @SuppressLint("NewApi") 
	  public int getConnectionState(BluetoothDevice paramBluetoothDevice)
	  {
	    try
	    {
	      if (!this.mBluetoothAdapter.isEnabled())
	        return 0;
	      int i = this.mBluetoothManager.getConnectionState(paramBluetoothDevice, 7);
	      return i;
	    }
	    catch (Exception localException)
	    {
	    }
	    return 0;
	  }
	
	  public boolean isConnected(BluetoothDevice paramBluetoothDevice)
	  {
	    if(isConnected == 2)
	    	return true;
	    return false;
	    	
	  }
	
	
	@SuppressLint("NewApi") 
	private final BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {
		
		public void onConnectionStateChange(final BluetoothGatt gatt, int status, int newState) {
			
		
			
			mGattCallbackResult = new ConnectHandle();
			mGattCallbackResult.onConnectionStateChange(gatt, status, newState);
			
			
			
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				
				//gatt.discoverServices();
				System.out.println("bluetoothGatt is connected success.");
				
			}else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				
				
				System.out.println("bluetoothGatt of status is ..."+status);
				System.out.println("bluetoothGatt is not connected.");
				
			}
			
			isConnected = newState;
			
			rssitoDistance = new RssitoDistance();
			
//			TimerTask dis_task = new TimerTask() {
//				
//				@Override
//				public void run() {
			
			  TimerTask task = new TimerTask()
	    	    {
	    	    	public void run()
	    	    	{	
	    	    		if (jishu <= 99) {
	    	    			gatt.readRemoteRssi();
	    	    			jishu++;
	    	    		}else {
	    	    			
	    	    			
	    	    			jishu = 0;
				
				       //取消定时器之后进行距离转换操作
	    	    			
	    	    		distance = rssitoDistance.rssitodis(nrssi);
	    	    		
	    	    		mdistance = (int)distance;
	    	    		
	    	    		System.out.println("the distance of the devic is ..."+distance);
	    	    		System.out.println("the distance of the devic is ..."+mdistance);
	    	    		//更新数据库里面设备距离
	    	    		db.updateDeviceDistance(deviceAddress, mdistance);
	    	    		//发送距离变化广播
	    	    		Intent intent = new Intent("android.intent.action.MAIN");
	    	    		intent.putExtra("deviceMac", deviceAddress);
	    	    		intent.putExtra("deviceDistance", mdistance);
	    	    		mContext.sendBroadcast(intent);
	    	    		this.cancel();
	    	    		}
	    	    		
	    	    	}
	    	    };
	 	Timer mRssiTimer = new Timer();
	 	mRssiTimer.schedule(task, 1000, 50);
		

//				}
//			};
//     	
//     	
//     	Timer mDisTimer = new Timer();
//     	mDisTimer.schedule(dis_task, 1000, 20000);
				
			
			
		};
				
		
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			
			//mGattCallbackResult = new ConnectHandle();
			//mGattCallbackResult.onReadRemoteRssi(gatt, rssi, status);
			
			nrssi[count]=(double)rssi;
			count++;
		//	System.out.println("the count is ...."+count);
			
			
		};
		
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			
		
			mGattCallbackResult = new ConnectHandle();
			mGattCallbackResult.onServicesDiscovered(gatt, status);
			
		};
		
		
		public void onCharacteristicWrite(BluetoothGatt gatt, android.bluetooth.BluetoothGattCharacteristic characteristic, int status) {
			
			mGattCallbackResult = new ConnectHandle();
			mGattCallbackResult.onCharacteristicWrite(gatt, characteristic, status);
		};
		
	};
	
	
	
	
	  /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
	
	@SuppressLint("NewApi") 
	public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.readCharacteristic(characteristic);
	}
	
	/**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */

	@SuppressLint("NewApi") 
	public void setCharacteristicNotification(
			BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
	}
	
	/**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
	
	 @SuppressLint("NewApi") 
	 public void writeCharacteristic(BluetoothGattCharacteristic characteristic){
    	 mBluetoothGatt.writeCharacteristic(characteristic);
    }

	 
	
	
}
