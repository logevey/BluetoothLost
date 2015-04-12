package com.doutech.bluetoothlost.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.doutech.bluetoothlost.bean.DeviceInfo;

public class Devicedb {

	private DeviceSqliteHelper helper;

	public Devicedb(Context context) {
		helper = new DeviceSqliteHelper(context);
	}

	/**
	 * 将List<DeviceInfo>的所有项添加到数据库--这里不用
	 * 
	 * @param DeviceInfos
	 */
	public void addAll(List<DeviceInfo> DeviceInfos) {
		for (DeviceInfo info : DeviceInfos) {
			add(info);
		}
	}

	/**
	 * 判断是否已经存在于数据库中，即是否已经添加过
	 * 
	 * @param name
	 *            设备名称，唯一的，以此为依据
	 * @return
	 */
	public boolean exist(String name) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("deviceinfo", null, "devicename=?",
				new String[] { name }, null, null, null);
		return cursor.moveToNext();
	}
	public boolean existDevice(String deviceMac) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("deviceinfo", null, "devicemac=?",
				new String[] { deviceMac }, null, null, null);
		return cursor.moveToNext();
	}

	public boolean isEmpty() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("deviceinfo", null, null, null, null, null,
				null);
		return !(cursor.moveToNext());
	}

	public void add(DeviceInfo device) {
		SQLiteDatabase db = helper.getWritableDatabase();
		// db.execSQL("insert into person (name) values(?)", new
		// Object[]{name});
		ContentValues contentValues = new ContentValues();
		contentValues.put("devicemac", device.getDeviceMac());
		contentValues.put("icon_index", device.getIconIndex());
		contentValues.put("photoname", device.getPhotoName());
		contentValues.put("issystemicon", device.getIsSystemIcon());
		contentValues.put("devicename", device.getDeviceName());
		contentValues.put("switch", device.getIsOpen());
		contentValues.put("distance", device.getDistance());
		contentValues.put("devicedistance", device.getDeviceDistance());
		db.insert("deviceinfo", null, contentValues);
	}

	/**
	 * 查询所有已经绑定的设备
	 * 
	 * @return List<DeviceInfo>
	 */
	public List<DeviceInfo> queryAll() {
		List<DeviceInfo> infos = new ArrayList<DeviceInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		// Cursor cursor=db.rawQuery("select * from person where name=?",new
		// String[]{name});
		Cursor cursor = db.query("deviceinfo", null, null, null, null, null,
				null);
		while (cursor.moveToNext()) {
			cursor.getInt(cursor.getColumnIndex("_id"));
			String devicemac = cursor.getString(cursor
					.getColumnIndex("devicemac"));
			int iconIndex = cursor.getInt(cursor.getColumnIndex("icon_index"));
			String photoName = cursor.getString(cursor
					.getColumnIndex("photoname"));
			int isSystemIcon = cursor.getInt(cursor
					.getColumnIndex("issystemicon"));
			String deviceName = cursor.getString(cursor
					.getColumnIndex("devicename"));
			int isOpen = cursor.getInt(cursor.getColumnIndex("switch"));
			int distance = cursor.getInt(cursor.getColumnIndex("distance"));
			int devicedistance = cursor.getInt(cursor.getColumnIndex("devicedistance"));
			DeviceInfo info = new DeviceInfo(devicemac,iconIndex, photoName,
					isSystemIcon, deviceName, isOpen, distance,devicedistance);
			infos.add(info);
		}
		return infos;
	}

	public void updateName(String oldDeviceName, String newDeviceName) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("devicename", newDeviceName);
		db.update("deviceinfo", cv, "devicename = ?",
				new String[] { oldDeviceName });
	}

	public void updateIcon(String DeviceName, int newIcon) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("icon_index", newIcon);
		db.update("deviceinfo", cv, "devicename = ?",
				new String[] { DeviceName });
	}

	public void updateSwitch(String DeviceName, int newSwitch) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("switch", newSwitch);
		db.update("deviceinfo", cv, "devicename = ?",
				new String[] { DeviceName });
	}

	public void updateIsSystemIcon(String DeviceName, int newSwitch) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("issystemicon", newSwitch);
		db.update("deviceinfo", cv, "devicename = ?",
				new String[] { DeviceName });
	}

	public void updatePhotoName(String DeviceName, String newPhotoName) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("photoname", newPhotoName);
		db.update("deviceinfo", cv, "devicename = ?",
				new String[] { DeviceName });
	}

	public void updateDistance(String DeviceName, int newDistance) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("distance", newDistance);
		db.update("deviceinfo", cv, "devicename = ?",
				new String[] { DeviceName });
	}
	public void updateDeviceDistance(String DeviceMac, int newDeviceDistance) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("devicedistance", newDeviceDistance);
		db.update("deviceinfo", cv, "devicemac = ?",
				new String[] { DeviceMac });
	}

	public DeviceInfo select(String deviceName) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(true, "deviceinfo", null, "devicename = '"
				+ deviceName + "'", null, null, null, null, null);
		while (cursor.moveToNext()) {
			DeviceInfo info = new DeviceInfo(cursor.getString(1),
					cursor.getInt(2), cursor.getString(3),cursor.getInt(4),
					cursor.getString(5), cursor.getInt(6), cursor.getInt(7),cursor.getInt(8));
			return info;
		}
		return null;
	}

	public DeviceInfo selectFormID(int id) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(true, "deviceinfo", null,
				"_id = '" + id + "'", null, null, null, null, null);
		while (cursor.moveToNext()) {
			DeviceInfo info = new DeviceInfo(cursor.getString(1),
					cursor.getInt(2), cursor.getString(3),cursor.getInt(4),
					cursor.getString(5), cursor.getInt(6), cursor.getInt(7),cursor.getInt(8));
			return info;
		}
		return null;
	}

	public int getID(String deviceName) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(true, "deviceinfo", null, "devicename = '"
				+ deviceName + "'", null, null, null, null, null);
		while (cursor.moveToNext()) {
			return cursor.getInt(0);
		}
		return -1;
	}

	public int getLastID() {
		List<DeviceInfo> infos = this.queryAll();
		if (infos.isEmpty()) {
			return -1;
		}
		DeviceInfo info = infos.get(infos.size() - 1);
		int lastID = this.getID(info.getDeviceName());
		return lastID;
	}

	public void delete(String devicename) {
		SQLiteDatabase db = helper.getWritableDatabase();
		// db.execSQL("delete from person where name=?", new Object[]{name});
		db.delete("deviceinfo", "devicename=?", new String[] { devicename });
	}

}
