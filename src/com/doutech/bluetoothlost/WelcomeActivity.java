package com.doutech.bluetoothlost;

import java.util.List;

import com.doutech.bluetoothlost.dao.Devicedb;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class WelcomeActivity extends Activity implements AnimationListener {
	
	private ImageView welcomeIv;
	private Animation fadeAnimation;
	private SharedPreferences sp;
	private boolean isFirstIn = true;
	private Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		sp = getSharedPreferences("douyatech", MODE_PRIVATE);
		editor = sp.edit();
		if(!hasShortcut())
		{
			addShortcut();
		}
		isFirstIn = sp.getBoolean("isFirstIn", true);
		welcomeIv = (ImageView) findViewById(R.id.welcomeiv);
		fadeAnimation = AnimationUtils.loadAnimation(this, R.anim.abc_fade_in);
		fadeAnimation.setFillAfter(true);
		fadeAnimation.setDuration(1000);
		welcomeIv.startAnimation(fadeAnimation);
		fadeAnimation.setAnimationListener(this);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		finish();
		//首次启动进入SplashActivity 否则进入MainActivity
		if (isFirstIn) {
			//首次进入
			goSplash();
			editor.putBoolean("isFirstIn", false);
			editor.commit();
		}else {
			Devicedb db;
			db = new Devicedb(this);
			if(db.isEmpty()){
				goMain();
        	}else{
        		goAlreadAddedList();
        	}
			
		}
	}
	//添加快捷方式
	private void addShortcut(){
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.searchShotCut));
		shortcut.putExtra("duplicate", false); //不允许重复创建 
		Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
		//shortcutIntent.setClassName(this, WelcomeActivity.class.getName());
		shortcutIntent.setClassName(this, TakePhoto.class.getName());
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		//快捷方式的图标 
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable.adddevice_large);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		sendBroadcast(shortcut);
	}
 
	private boolean hasShortcut()
	{
	        boolean isInstallShortcut = false;
	        final ContentResolver cr = this.getContentResolver();
	        final String AUTHORITY =WelcomeActivity.getAuthorityFromPermission(this, "com.android.launcher.permission.READ_SETTINGS");

	        
	        final Uri CONTENT_URI = Uri.parse("content://" +AUTHORITY + "/favorites?notify=true");
	        Cursor c = cr.query(CONTENT_URI,new String[] {"title","iconResource" },"title=?",
	        new String[] {this.getString(R.string.searchShotCut).trim()}, null);
	        if(c!=null && c.getCount()>0){
	            isInstallShortcut = true ;
	        }
	        return isInstallShortcut ;
	}
	static String getAuthorityFromPermission(Context context, String permission){
	    if (permission == null) return null;
	    List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
	    if (packs != null) {
	        for (PackageInfo pack : packs) { 
	            ProviderInfo[] providers = pack.providers; 
	            if (providers != null) { 
	                for (ProviderInfo provider : providers) { 
	                    if (permission.equals(provider.readPermission)) return provider.authority;
	                    if (permission.equals(provider.writePermission)) return provider.authority;
	                } 
	            }
	        }
	    }
	    return null;
	}
	private void goMain() {
		
		Intent intent = new Intent(this, MainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("isFirst", 1);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private void goSplash() {
		Intent intent = new Intent(this, SplashActivity.class);
		startActivity(intent);
	}
	private void goAlreadAddedList(){
		Intent intent = new Intent(this, AlreadyAddedListActivity.class);
		startActivity(intent);
	}
	@Override
	public void onAnimationStart(Animation animation) {
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}
}
