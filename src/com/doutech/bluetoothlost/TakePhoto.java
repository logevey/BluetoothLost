package com.doutech.bluetoothlost;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class TakePhoto extends Activity implements AnimationListener {
	
	private ImageView welcomeIv;
	private Animation fadeAnimation;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		sp = getSharedPreferences("douyatech", MODE_PRIVATE);
		sp.edit();
		welcomeIv = (ImageView) findViewById(R.id.welcomeiv);
		fadeAnimation = AnimationUtils.loadAnimation(this, R.anim.abc_fade_in);
		fadeAnimation.setFillAfter(true);
		fadeAnimation.setDuration(1000);
		welcomeIv.startAnimation(fadeAnimation);
		fadeAnimation.setAnimationListener(this);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		
		//首次启动进入SplashActivity 否则进入MainActivity
		goTakePhoto();
	}
	
	private void goTakePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用android自带的照相机
		startActivityForResult(intent, 1);
	}
	private void goAlreadAddedList(){
		finish();
		Intent intent = new Intent(this, AlreadyAddedListActivity.class);
		startActivity(intent);
	}
	@SuppressLint({ "SimpleDateFormat", "SdCardPath" }) @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		// 处理照相机拍照的图片
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
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
		goAlreadAddedList();
		//super.onActivityResult(requestCode, resultCode, data);
		
	}
	
	@Override
	public void onAnimationStart(Animation animation) {
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}
}
