package com.doutech.bluetoothlost;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.doutech.bluetoothlost.adapter.ExistDeviceAdapter;
import com.doutech.bluetoothlost.bean.DeviceInfo;
import com.doutech.bluetoothlost.dao.Devicedb;
import com.doutech.bluetoothlost.utils.Utils;
import com.doutech.bluetoothlost.view.BaseSwipeListViewListener;
import com.doutech.bluetoothlost.view.RefreshableView;
import com.doutech.bluetoothlost.view.RefreshableView.PullToRefreshListener;
import com.doutech.bluetoothlost.view.SwipeListView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class AlreadyAddedListActivity extends Activity {

	private static final int TAKE_PHOTO = 0;
	private Devicedb db;
	private SwipeListView swipeListView;
	private ExistDeviceAdapter adapter;
	private List<DeviceInfo> infos;
	private Timer timer;
	private int clickTime;
	RefreshableView refreshableView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alreadaddedlist);

		db = new Devicedb(this);
		swipeListView = (SwipeListView) findViewById(R.id.swipeListView);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menuoflist, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();// 从数据库拿数据
		Utils.showToastMsg(this, db.getLastID()+"");
		adapter = new ExistDeviceAdapter(this, infos, swipeListView);
		swipeListView.requestFocusFromTouch();
		swipeListView.setAdapter(adapter);
		swipeListView.setSwipeListViewListener(new BaseSwipeListViewListener() {
			@Override
			public void onClickFrontView(int position) {
				super.onClickFrontView(position);
				Utils.showToastMsg(getApplicationContext(), "点击了第" + position
						+ "个");
				// 进入详情页面 传进对象

				Intent intent = new Intent(AlreadyAddedListActivity.this,
						CtrlDeviceActivity.class);
				intent.putExtra("deviceinfo", infos.get(position));
				startActivity(intent);
			}
			@Override
			public void onClickBackView(int position) {
				super.onClickBackView(position);
				// 注册点击后面View
			}

		});
		refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用android自带的照相机
				startActivityForResult(intent, TAKE_PHOTO);
				refreshableView.finishRefreshing();
			}
		}, 0);
		
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) { // 监听物理键
		if (keyCode == KeyEvent.KEYCODE_MENU) {// menu键
			// 相关响应代码
			Intent intent = new Intent(AlreadyAddedListActivity.this,
					SettingActivity.class);
			startActivity(intent);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {// 返回键
			// 相关响应代码
			onBackPressed();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {// 音量键下
			// 相关响应代码
			Utils.showToastMsg(getApplicationContext(), "down");
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {// 音量键上
			// 相关响应代码
			Utils.showToastMsg(getApplicationContext(), "up");
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressLint("ShowToast")
	public void onBackPressed() {
		timer = new Timer();
		clickTime++;
		if (clickTime == 1) {
			Toast.makeText(this, "再次点击退出程序...", 0).show();
		}
		if (clickTime == 2) {
			System.exit(-1);
			clickTime = 0;
		}
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				clickTime = 0;
			}
		}, 500);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		int id = item.getItemId();
		if (id == R.id.add) {
			
			Intent intent = new Intent(AlreadyAddedListActivity.this,
					MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("isFirst", 0);
			intent.putExtras(bundle);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressLint({ "SimpleDateFormat", "SdCardPath" })
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		// 处理照相机拍照的图片
		if (requestCode == TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
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
				String fileName = "/sdcard/douya/" + str + ".jpg";
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

	private void initData() {
		// 查找数据库 拿到数据
		infos = db.queryAll();
	}

}
