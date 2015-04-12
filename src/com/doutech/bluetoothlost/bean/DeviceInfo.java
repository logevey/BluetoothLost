package com.doutech.bluetoothlost.bean;

import java.io.Serializable;

import android.R.integer;

public class DeviceInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4991914220307022385L;
	private int iconIndex;
	private String photoName;
	private int isSystemIcon;
	private String deviceName;
	private int isOpen = 1;
	private int distance;
	private String device_mac;
	private int device_distance;
	
	public DeviceInfo(String device_mac,int iconIndex, String photoName, int isSystemIcon,
			String deviceName, int isOpen, int distance,int device_distance) {
		super();
		this.device_mac=device_mac;
		this.iconIndex = iconIndex;
		this.photoName = photoName;
		this.isSystemIcon = isSystemIcon;
		this.deviceName = deviceName;
		this.isOpen = isOpen;
		this.distance = distance;
		this.device_distance=device_distance;
	}
	public int getIconIndex() {
		return iconIndex;
	}
	public void setIconIndex(int iconIndex) {
		this.iconIndex = iconIndex;
	}
	public String getPhotoName() {
		return photoName;
	}
	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
	public int getIsSystemIcon() {
		return isSystemIcon;
	}
	public void setIsSystemIcon(int isSystemIcon) {
		this.isSystemIcon = isSystemIcon;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public int getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getDeviceDistance() {
		return device_distance;
	}
	public void setDeviceDistance(int device_distance) {
		this.device_distance = device_distance;
	}
	public String getDeviceMac() {
		// TODO Auto-generated method stub
		return device_mac;
	}
	public void setDeviceMac(String device_mac) {
		this.device_mac = device_mac;
	}
}
