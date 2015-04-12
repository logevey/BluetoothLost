package com.doutech.bluetoothlost.service;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

public interface GattCallbackResult {
	
	
	public abstract void onServicesDiscovered(BluetoothGatt gatt, int status);

	public abstract void onConnectionStateChange(final BluetoothGatt gatt,
			int status, int newState);

	public abstract void onCharacteristicRead(BluetoothGatt gatt,
			BluetoothGattCharacteristic characteristic, int status);

	public abstract void onCharacteristicChanged(BluetoothGatt gatt,
			BluetoothGattCharacteristic characteristic);

	public abstract void onReadRemoteRssi(BluetoothGatt gatt, int rssi,
			int status);

	public abstract void onCharacteristicWrite(BluetoothGatt gatt,
			android.bluetooth.BluetoothGattCharacteristic characteristic,
			int status);
	
}
