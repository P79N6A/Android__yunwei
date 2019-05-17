package com.zhongzhi.grid.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.zhongzhi.grid.R;

import java.util.ArrayList;
import java.util.List;


public abstract class AAComAdapter<T> extends BaseAdapter {

	protected Context mContext;
	public List<T> mDatas = new ArrayList<T>();
	protected LayoutInflater mInflater;
	private int mlayoutId;
	private AAViewHolder holder;
	private int topSize = -1;
	private SparseArray<String> arraycolor;
	private boolean showFresh = false;
	private boolean showNoData = true;
	public int nextpage = 1;
	private int selectIndex = -1;
	private ImageView nullImage;
    private int resource;
	public int getSelectIndex() {
		return selectIndex;
	}

	public void setSelectIndex(int selectIndex) {
		this.selectIndex = selectIndex;
	}

	public void setNotShowNoData() {
		showNoData = false;
	}

	public AAComAdapter(Context context, int layoutId, List<T> datas, boolean showFresh) {
		this.mContext = context;
		this.mlayoutId = layoutId;
		mInflater = LayoutInflater.from(context);
		this.mDatas.clear();
		this.mDatas.addAll(datas);
		this.showFresh = showFresh;
	}

	public AAComAdapter(Context context, int layoutId, List<T> datas) {
		this.mContext = context;
		this.mlayoutId = layoutId;
		mInflater = LayoutInflater.from(context);
		this.mDatas.clear();
		this.mDatas.addAll(datas);
		if (this.mDatas.size() == 0) {
			this.mDatas.add(null);
		}
	}

	public AAComAdapter(Context context, int layoutId) {
		this.mContext = context;
		this.mlayoutId = layoutId;
		mInflater = LayoutInflater.from(context);
		this.mDatas.clear();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (mDatas.get(position) == null && mDatas.size() == 1) {
			if(showNoData) {
				View view = LayoutInflater.from(mContext).inflate(R.layout.sl_list_nodata, parent, false);
				nullImage = (ImageView) view.findViewById(R.id.nodata_iv);
				if (resource>0){
					nullImage.setImageResource(resource);
				}
				return view;
			}else
				return LayoutInflater.from(mContext).inflate(R.layout.null_layout, parent, false);
		} else {

			holder = AAViewHolder.get(mContext, convertView, parent, mlayoutId, position, showFresh);
			convert(holder, (T) getItem(position));
			return holder.getConvertView();
		}

	}

	public void addTextColor(int position, String color) {
		if (arraycolor == null) {
			arraycolor = new SparseArray<String>();
		}
		arraycolor.put(position, color);
	}

	public String getTextColor(int position) {
		return arraycolor.get(position);
	}

	public void setNoDataPic(int resource){
		this.resource = resource;
	}

	public abstract void convert(AAViewHolder holder, T mt);

	public AAViewHolder getHolder() {
		return holder;
	}

	public void resetData(List<T> mDatas) {
		this.mDatas.clear();
		this.mDatas.addAll(mDatas);
		if (showNoData) {
			if (this.mDatas.size() == 0) {
				this.mDatas.add(null);
			}
		}
//		notifyDataSetChanged();
	}

	public void addData(List<T> mDatas) {
		this.mDatas = mDatas;
//		notifyDataSetChanged();
	}

	public void setSize(int size){
		this.topSize = size;
	}
	@Override
	public int getCount() {
		int size = mDatas.size();
		if (topSize !=-1 && topSize < size)
			return topSize;
		else
			return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
