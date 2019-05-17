package com.zhongzhi.grid.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongzhi.grid.myview.SwipeListLayout;

public class AAViewHolder {
	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;

	public AAViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConvertView.setTag(this);


	}

	public static AAViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position,
                                   boolean showFresh) {

		if (convertView == null || showFresh) {
			return new AAViewHolder(context, parent, layoutId, position);
		} else {
			try {
				AAViewHolder holder = (AAViewHolder) convertView.getTag();
				holder.mPosition = position;
				return holder;
			} catch (Exception e) {
				e.printStackTrace();
				return new AAViewHolder(context, parent, layoutId, position);
			}
		}
	}

	/**
	 * 通过viewId获取控件
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			view.setVisibility(View.VISIBLE);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * 返回当前位置
	 * 
	 * @return
	 */
	public int getPosition() {
		return mPosition;
	}

	// ---------------------设置每个控件值
	/**
	 * 为TextView设置值
	 * 
	 * @param viewId
	 * @param text
	 */
	public AAViewHolder setText(int viewId, String text) {
		((TextView) getView(viewId)).setText(text);
		return this;

	}

	/**
	 * 为TextView设置值
	 *
	 * @param viewId
	 * @param text
	 */
	public AAViewHolder setText(int viewId, Spanned text) {
		((TextView) getView(viewId)).setText(text);
		return this;

	}

	public TextView getTextView(int viewId, String text) {
		TextView textView = ((TextView) getView(viewId));
		textView.setText(text);
		return textView;

	}

	public TextView getTextView(int viewId) {
		TextView textView = ((TextView) getView(viewId));
		return textView;

	}
	public CheckBox getCheckBox(int viewId) {
		CheckBox textView = ((CheckBox) getView(viewId));
		return textView;

	}

	public View getViews(int viewId) {
		View textView = getView(viewId);
		return textView;

	}

	public WebView getWebView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			view.setVisibility(View.VISIBLE);
			mViews.put(viewId, view);
			WebView mWebView = (WebView)view;
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
			mWebView.getSettings().setAllowFileAccess(true);
			mWebView.getSettings().setBuiltInZoomControls(false);
			mWebView.getSettings().setSupportZoom(true);
			mWebView.getSettings().setSaveFormData(false);
			mWebView.setHorizontalScrollBarEnabled(false);
			mWebView.setVerticalScrollBarEnabled(false);
			mWebView.loadUrl("file:///android_asset/charts.html");
		}
		return (WebView) view;
	}

	public LinearLayout getLine(int viewId) {
		LinearLayout line = ((LinearLayout) getView(viewId));
		return line;

	}

	public RelativeLayout getRela(int viewId) {
		RelativeLayout line = ((RelativeLayout) getView(viewId));
		return line;

	}

	/**
	 * 为Rating设置值
	 * 
	 * @param viewId
	 *
	 */
	public AAViewHolder setRating(int viewId, float rating) {
		((RatingBar) getView(viewId)).setRating(rating);
		return this;

	}

	/**
	 * ImageView
	 * 
	 * @param viewId
	 */
	public ImageView getImage(int viewId) {
		return (ImageView) getView(viewId);
	}

	/**
	 * 
	 * @param viewId
	 * @return Button
	 */
	public Button getBtn(int viewId) {
		return (Button) getView(viewId);
	}

	/**
	 *
	 * @param viewId
	 * @return SwipeListLayout
	 */
	public SwipeListLayout getSwipeListLayout(int viewId) {
		return (SwipeListLayout) getView(viewId);
	}
	/**
	 * 为TextView设置值
	 * 
	 * @param viewId
	 * @param text
	 */
	public AAViewHolder setTextHtml(int viewId, String text) {
		((TextView) getView(viewId)).setText(Html.fromHtml(text));
		return this;
	}

	public AAViewHolder setSelectButton(int viewId, String text, boolean selected) {
		TextView tv = ((TextView) getView(viewId));
		tv.setText(text);
		tv.setSelected(selected);
		return this;
	}

	public AAViewHolder setTextColor(int viewId, int color) {
		((TextView) getView(viewId)).setTextColor(color);
		return this;
	}

	public AAViewHolder setVisiable(int viewId, int visiable) {
		View view = getView(viewId);
		view.setVisibility(visiable);
		return this;
	}

	public AAViewHolder setImgResourceId(int viewId, int id) {
		ImageView view = getView(viewId);
		view.setImageResource(id);
		return this;
	}

}
