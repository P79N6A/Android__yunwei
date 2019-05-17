package com.zhongzhi.grid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.zhongzhi.grid.R;
import com.zhongzhi.grid.adapter.AAComAdapter;
import com.zhongzhi.grid.adapter.AAViewHolder;
import com.zhongzhi.grid.bean.BaseBean;
import com.zhongzhi.grid.bean.PerformanceItem;
import com.zhongzhi.grid.bean.PerformanceListBean;
import com.zhongzhi.grid.util.Constants;
import com.zhongzhi.grid.util.HttpUtil;
import com.zhongzhi.grid.util.SharedPrefsUtil;
import com.zhongzhi.grid.util.Tool;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;

import static com.zhongzhi.grid.util.ToastUtils.showToast;

/**
 * Created by Administrator on 2018/7/9.
 */

public class FragmentYearStatistical extends BaseFragment {

    private TwinklingRefreshLayout mRefresh;
    private int page = 1;
    private AAComAdapter aaComAdapter;
    private ListView mListView;
    private ArrayList<PerformanceItem> mAllRepairOrder = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repair_order,container,false);
        mListView = (ListView)view.findViewById(R.id.listview);
        mRefresh = (TwinklingRefreshLayout)view.findViewById(R.id.refresh);
        mRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                page = 1;
                getData();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                page = page + 1;
                getData();
            }
        });
        mRefresh.startRefresh();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        aaComAdapter = new AAComAdapter<PerformanceItem>(getContext(),R.layout.statistical_list_item,mAllRepairOrder) {
            @Override
            public void convert(AAViewHolder holder, PerformanceItem mt) {
                TextView date = holder.getTextView(R.id.date);
                TextView num = holder.getTextView(R.id.num);
                TextView score = holder.getTextView(R.id.score);
                date.setText(mt.getShowTime());
                if(!TextUtils.isEmpty(mt.getCount())){
                    num.setText(mt.getCount()+"个");
                }
                if(!TextUtils.isEmpty(mt.getScore())){
                    score.setText(mt.getScore()+"分");
                }
            }
        };
        mListView.setAdapter(aaComAdapter);
    }

    /**
     * 获取数据
     */
    private void getData() {
        if (!HttpUtil.isNetworkConnected(getContext())) {
            showToast(getString(R.string.net_hint));
            return;
        }

        JSONObject ob = new JSONObject();
        try {
            ob.put("organizcode", SharedPrefsUtil.getValue(getContext(),"organizCode",null));
            ob.put("page", page);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(Tool.getParams(ob.toString(), getContext(), Constants.YDETAILS), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                mRefresh.finishLoadmore();
                mRefresh.finishRefreshing();
                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    if (bean.getRet() == 1) {
                        PerformanceListBean repairOrderListBean = new Gson().fromJson(bean.getData(),PerformanceListBean.class);
                        if(page == 1){
                            mAllRepairOrder = repairOrderListBean.getResults();
                        }else {
                            mAllRepairOrder.addAll(repairOrderListBean.getResults());
                        }
                        aaComAdapter.mDatas = mAllRepairOrder;
                        aaComAdapter.notifyDataSetChanged();
                    } else {
                        showToast(bean.getMsg());

                    }

                } else {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                mRefresh.finishLoadmore();
                mRefresh.finishRefreshing();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                mRefresh.finishLoadmore();
                mRefresh.finishRefreshing();
            }

            @Override
            public void onFinished() {
                mRefresh.finishLoadmore();
                mRefresh.finishRefreshing();
            }
        });
    }
}
