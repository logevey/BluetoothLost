package com.doutech.bluetoothlost;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.doutech.bluetoothlost.adapter.MyPagerAdapter;

public class SplashActivity extends Activity {

	private int [] imageIds;
	private ViewPager vp;
	private List<ImageView> imageList = new ArrayList<ImageView>();
	private List<View> dots = new ArrayList<View>();
	private int oldDotPos = 0;
	private GestureDetector detector;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		detector = new GestureDetector(gestureListener);
		imageIds = new int[]{
            	R.drawable.splash_1,	
            	R.drawable.splash_2,	
            	R.drawable.splash_3,	
            	R.drawable.splash_4,	
            };
		initImageList();
		vp = (ViewPager) findViewById(R.id.vp);
		vp.setAdapter(new MyPagerAdapter(imageList));
		vp.setOnPageChangeListener(pageChangeListener);
		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return detector.onTouchEvent(event);
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		detector.onTouchEvent(ev);
	    return super.dispatchTouchEvent(ev);  
	}
	
	private SimpleOnGestureListener gestureListener = new SimpleOnGestureListener(){
		 @Override  
	        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,  
	                float velocityY) {  
	            if (vp.getCurrentItem() == 3) {  
	                if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY()  
	                        - e2.getY())  
	                        && (e1.getX() - e2.getX() <= (-50) || e1  
	                                .getX() - e2.getX() >= 50)) {  
	                    if (e1.getX() - e2.getX() >= 50) {  
	                    	finish();
	                    	
                    		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    		startActivity(intent);
	                    	
	                    	
	                        return true;  
	                    }  
	                }  
	            }  
	            return false;  
	        }
	};
	
	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int position) {
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			dots.get(oldDotPos).setBackgroundResource(R.drawable.dot_normal);
			oldDotPos = position; 
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	
	private void initImageList() {
		for (int i = 0; i < imageIds.length; i++) {			
			ImageView imageView = new ImageView(this);
        	imageView.setBackgroundResource(imageIds[i]);
        	imageList.add(imageView);
		}
	    dots.add(findViewById(R.id.dot_0));
	    dots.add(findViewById(R.id.dot_1));
	    dots.add(findViewById(R.id.dot_2));
	    dots.add(findViewById(R.id.dot_3));
	}
}
