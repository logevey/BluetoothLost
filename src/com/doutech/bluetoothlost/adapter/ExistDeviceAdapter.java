package com.doutech.bluetoothlost.adapter;

import java.io.File;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.doutech.bluetoothlost.R;
import com.doutech.bluetoothlost.bean.DeviceInfo;
import com.doutech.bluetoothlost.dao.Devicedb;
import com.doutech.bluetoothlost.view.CircleImageView;
import com.doutech.bluetoothlost.view.SwipeListView;

public class ExistDeviceAdapter extends BaseAdapter {

	private List<DeviceInfo> mDatas;  
    private LayoutInflater mInflater;  
    private SwipeListView mSwipeListView ; 
    private Context context;
	private int selectedIconId;
	private Devicedb db;

	public ExistDeviceAdapter(Context context, List<DeviceInfo> mDatas, SwipeListView mSwipeListView) {
		super();
		this.context = context;
		this.mDatas = mDatas;
		this.mInflater = LayoutInflater.from(context);  
		this.mSwipeListView = mSwipeListView;
		db = new Devicedb(context);
	}

	@Override
	public int getCount() {
		return mDatas.size();  
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);  
	}

	@Override
	public long getItemId(int position) {
		return position;  
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		DeviceInfo info = mDatas.get(position);
		convertView = mInflater.inflate(R.layout.item_swipelistview, null);  
        CircleImageView deviceIcon = (CircleImageView) convertView.findViewById(R.id.deviceicon);
        TextView deviceName = (TextView) convertView.findViewById(R.id.devicename);
        TextView bindState = (TextView) convertView.findViewById(R.id.bindstate);
        if (info.getIsSystemIcon() == 1) {
			// 系统图标
        	TypedArray ta = context.getResources().obtainTypedArray(R.array.iconarray);
            selectedIconId = ta.getResourceId(info.getIconIndex(), 0);
    		ta.recycle();
        	deviceIcon.setImageResource(selectedIconId);
		}else {
			//不是系统图标 SD卡或者其他地方的
			File mfile=new File(info.getPhotoName());
			if (mfile.exists()) {//若该文件存在
				Bitmap bm = BitmapFactory.decodeFile(info.getPhotoName());
				deviceIcon.setImageBitmap(bm);
			}
		}
        deviceName.setText(info.getDeviceName());
        bindState.setText(info.getIsOpen() == 1 ? "报警" : "未报警");
//       Button del = (Button) convertView.findViewById(R.id.remove);  
// 
//        del.setOnClickListener(new OnClickListener()  
//        {
//            @Override  
//            public void onClick(View v)  
//            {  
//            	// 在从数据库删除
//            	db.delete(mDatas.get(position).getDeviceName());
//                mDatas.remove(position);  
//                notifyDataSetChanged();  
//                 /** 
//                  * 关闭SwipeListView 
//                  * 不关闭的话，刚删除位置的item存在问题 
//                  * 在监听事件中onListChange中关闭，会出现问题 
//                  */  
//               mSwipeListView.closeOpenedItems();  
//            }  
//        });  
        return convertView;  
	}

}
