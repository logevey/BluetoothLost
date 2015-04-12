package com.doutech.bluetoothlost.utils;

import android.bluetooth.BluetoothDevice;

public class DeviceRssi {
	
	private BluetoothDevice device;
	private int rssi;
	
	public DeviceRssi(BluetoothDevice device,int rssi)
	{
		this.device = device;
		this.rssi = rssi;
	}
	
	public int getRssi()
	{
		return rssi;
	}
	
	public void setRssi()
	{
		this.rssi = rssi;
	}
	
	public BluetoothDevice getDevice()
	{
		return device;
	}
	
	public void setDevice()
	{
		this.device = device;
	}
	
}
