package com.doutech.bluetoothlost.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DeviceSqliteHelper extends SQLiteOpenHelper {

	public DeviceSqliteHelper(Context context) {
		super(context, "DeviceInfoSqlite.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists deviceinfo(_id integer primary key autoincrement,devicemac varchar(20),icon_index varchar(10),photoname varchar(10),issystemicon varchar(10),devicename varchar(10),switch varchar(10),distance varchar(10),devicedistance varchar(10))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
