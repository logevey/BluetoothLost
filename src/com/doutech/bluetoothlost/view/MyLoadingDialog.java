package com.doutech.bluetoothlost.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.doutech.bluetoothlost.R;

public class MyLoadingDialog extends Dialog {

	private TextView tv;
	private ImageView iv;
	private static MyLoadingDialog dialog;
	private static AsyncTask<?, ?, ?> mTask;

	public MyLoadingDialog(Context context) {
		this(context, 0);
	}

	public MyLoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog);
		tv = (TextView) findViewById(R.id.tv_loadingmsg);
		iv = (ImageView) findViewById(R.id.loadingImageView);

	}

	private void setMessage(String msg) {
		tv.setText(msg);
	}

	private Drawable getImageBackGround(){
		return iv.getBackground();
	}
	public static MyLoadingDialog show(Context context, AsyncTask<?, ?, ?> task, String message) {
		mTask = task;
		dialog = new MyLoadingDialog(context,
				R.style.CustomProgressDialog);
		dialog.setCanceledOnTouchOutside(false);
//		dialog.setOnCancelListener(null);
		dialog.setCancel();
		dialog.show();
		dialog.setMessage(message);
		AnimationDrawable animationDrawable = (AnimationDrawable) dialog.getImageBackGround();
        animationDrawable.start();
		return dialog;
	}
	private void setCancel() {
		dialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// 点击取消就要停止后台所做的任务
				mTask.cancel(true);
			}
		});
	}

	public static void disattach(){
		dialog.dismiss();
	}

	
}
