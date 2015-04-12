package com.doutech.bluetoothlost.service;

import java.util.List;

import com.doutech.bluetoothlost.utils.Utils;
import com.doutech.bluetoothlost.utils.Uuid;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Handler;
import android.util.Log;

public class ConnectHandle implements GattCallbackResult {
	
	private static final String TAG = ConnectHandle.class.getSimpleName();
	
	
	Handler mHandler;
	
	private Uuid mUuid;
	
	private  void test()
	{
		System.out.println("test the interface in gatt....");
		
	}


	@Override
	public void onConnectionStateChange(final BluetoothGatt gatt, int status,
			int newState) {
		// TODO Auto-generated method stub
		
		//将连接状态广播出去，当后台接收到gatt断开后重新连接设备
		    		
    	    		
	}
 	
	
	
	
	@Override
	public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
		// TODO Auto-generated method stub
	  
		
	}


	@Override
	public void onServicesDiscovered(BluetoothGatt gatt, int status) {
		// TODO Auto-generated method stub
		displayGattServices(gatt.getServices());
		
	}




	@Override
	public void onCharacteristicRead(BluetoothGatt gatt,
			BluetoothGattCharacteristic characteristic, int status) {
		// TODO Auto-generated method stub
		
		if (status == BluetoothGatt.GATT_SUCCESS) 
			Log.e(TAG,"onCharRead "+gatt.getDevice().getName()
					+" read "
					+characteristic.getUuid().toString()
					+" -> "
					+Utils.bytesToHexString(characteristic.getValue()));
		         String read = "onCharRead "+gatt.getDevice().getName()
				+" read "
				+characteristic.getUuid().toString()
				+" -> "
				+Utils.bytesToHexString(characteristic.getValue());
               System.out.println(read);
		
		
	}


	@Override
	public void onCharacteristicChanged(BluetoothGatt gatt,
			BluetoothGattCharacteristic characteristic) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	

	private void displayGattServices(List<BluetoothGattService> services) {
		// TODO Auto-generated method stub
		if (services == null)
			return;
		for (BluetoothGattService service : services) {

			// Service 的字段信息
			int type = service.getType();
			Log.i(TAG, "-->service type:" + Utils.getServiceType(type));
			Log.i(TAG, "-->includedServices size:"
					+ service.getIncludedServices().size());
			Log.i(TAG, "-->service uuid:" + service.getUuid());

			// Characteristic 的字段信息
			List<BluetoothGattCharacteristic> gattCharacteristics = service
					.getCharacteristics();
			for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
				Log.i(TAG, "---->char uuid:" + gattCharacteristic.getUuid());

				int permission = gattCharacteristic.getPermissions();
				Log.i(TAG,
						"---->char permission:"
								+ Utils.getCharPermission(permission));

				int property = gattCharacteristic.getProperties();
				Log.i(TAG,
						"---->char property:"
								+ Utils.getCharPropertie(property));

				byte[] data = gattCharacteristic.getValue();
				if (data != null && data.length > 0) {
					Log.i(TAG, "---->char value:" + new String(data));
					
					
					// 读取或写Uuid服务
					
					mHandler = new Handler();
					mUuid = new Uuid();
					
					if(gattCharacteristic.getUuid().toString().equals(mUuid.UUID_DEV_AlARM_EN)){        			
	        			//测试读取当前Characteristic数据，会触发mOnDataAvailable.onCharacteristicRead()
	        			mHandler.postDelayed(new Runnable() {
	                        @Override
	                        public void run() {
	                        	//mBLE.readCharacteristic(gattCharacteristic);
	                        }
	                    }, 500);
	        			
	        			//接受Characteristic被写的通知,收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
	        		//	mBLE.setCharacteristicNotification(gattCharacteristic, true);
	        			
	        			
	        			//设置数据内容
	        			byte[] byteDemo = new byte[1];
	        			byteDemo[0] = 1;
//	        			byteDemo[1] = 0;
//	        			byteDemo[2] = 0;
//	        			byteDemo[3] = 0;
	        			gattCharacteristic.setValue(byteDemo);
	        			
	        			//往蓝牙模块写入数据
	        			//mBLE.writeCharacteristic(gattCharacteristic);
					
					
					}
					
					
					

					// -----Descriptors的字段信息-----//
					List<BluetoothGattDescriptor> gattDescriptors = gattCharacteristic
							.getDescriptors();
					for (BluetoothGattDescriptor gattDescriptor : gattDescriptors) {
						Log.i(TAG,
								"-------->desc uuid:"
										+ gattDescriptor.getUuid());
						int descPermission = gattDescriptor.getPermissions();
						Log.i(TAG,
								"-------->desc permission:"
										+ Utils.getDescPermission(descPermission));

						byte[] desData = gattDescriptor.getValue();
						gattDescriptor
								.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
						if (desData != null && desData.length > 0) {
							Log.i(TAG, "-------->desc value:"
									+ new String(desData));
						}
					}
				}
			}
            
         
	}
		
	}


	@Override
	public void onCharacteristicWrite(BluetoothGatt gatt,
			BluetoothGattCharacteristic characteristic, int status) {
		// TODO Auto-generated method stub
		System.out.println("the characteristic in mainactivity has changed  yes...");
		
		Log.e(TAG,"onCharWrite "+gatt.getDevice().getName()
				+" write "
				+characteristic.getUuid().toString()
				+" -> "
				+new String(characteristic.getValue()));
	}
	
	

}
