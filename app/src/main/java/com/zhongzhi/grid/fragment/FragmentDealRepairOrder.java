package com.zhongzhi.grid.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.zhongzhi.grid.R;
import com.zhongzhi.grid.activity.HistoryRepairOrderInfoActivity;
import com.zhongzhi.grid.activity.MapActivity;
import com.zhongzhi.grid.adapter.AAComAdapter;
import com.zhongzhi.grid.adapter.AAViewHolder;
import com.zhongzhi.grid.bean.BaseBean;
import com.zhongzhi.grid.bean.HistoryRepairOrderListBean;
import com.zhongzhi.grid.bean.RepairOrderItem;
import com.zhongzhi.grid.bean.RepairOrderListBean;
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

public class FragmentDealRepairOrder extends BaseFragment {

    private AAComAdapter aaComAdapter;
    private ListView mListView;
    public TwinklingRefreshLayout mRefresh;
    private int page = 1;
    private ArrayList<RepairOrderItem> mAllRepairOrder = new ArrayList<>();
    private RadioButton titleTv;
    public String time;
    public void setTitleTv(RadioButton titleTv) {
        this.titleTv = titleTv;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repair_order,container,false);
        loadingDialog = createLoadingDialog(getContext(),"");
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

        aaComAdapter = new AAComAdapter<RepairOrderItem>(getContext(),R.layout.repair_order_item2,mAllRepairOrder) {
            @Override
            public void convert(AAViewHolder holder, RepairOrderItem mt) {
                TextView orderNum = holder.getTextView(R.id.order_num);
                TextView order_status = holder.getTextView(R.id.order_status);
                TextView order_userNum = holder.getTextView(R.id.order_userNum);
                TextView order_name = holder.getTextView(R.id.order_name);
                TextView order_taiqu = holder.getTextView(R.id.order_taiqu);
                TextView order_gds = holder.getTextView(R.id.order_gds);
                TextView order_manager = holder.getTextView(R.id.order_manager);
                TextView order_des = holder.getTextView(R.id.order_des);
                ImageView location = (ImageView)holder.getViews(R.id.location);
                orderNum.setText("工单编号："+mt.getWORK_LIST_NUM());
                order_gds.setText(mt.getPOEER_SUPPLY_STATION());
                switch (mt.getWORK_LIST_PROCE()){
                    case 1:
                        order_status.setText("未处理");
                        break;
                    case 2:
                        order_status.setText("流转中");
                        break;
                    case 3:
                        order_status.setText("处理中");
                        break;
                    case 4:
                        order_status.setText("已结束");
                        break;
                }
                order_userNum.setText(mt.getUSER_NUM());
                order_name.setText(mt.getELEC_USER_NAME());
                order_taiqu.setText(mt.getAREA_NAME());
                order_manager.setText(mt.getAREA_ADMINISTRATOR());
                order_des.setText(mt.getDISPACH_EXPLAIN());
                location.setTag(mt);
                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RepairOrderItem repairOrderItem = (RepairOrderItem) view.getTag();
                        if(TextUtils.isEmpty(repairOrderItem.getLONGITUDE())){
                            showToast("位置信息不存在");
                        }else {
                            Intent intent = new Intent(getContext(), MapActivity.class);
                            intent.putExtra("lat",repairOrderItem.getLATITUDE());
                            intent.putExtra("lng",repairOrderItem.getLONGITUDE());
                            startActivity(intent);
                        }

                    }
                });
            }
        };
        mListView.setAdapter(aaComAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RepairOrderItem repairOrderItem = (RepairOrderItem) aaComAdapter.getItem(i);
                Intent intent = new Intent();
                intent.putExtra("worknum",repairOrderItem.getWORK_LIST_NUM());
                intent.setClass(getContext(), HistoryRepairOrderInfoActivity.class);
                startActivity(intent);
            }
        });
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
            ob.put("status", "3");
            ob.put("time", time);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(Tool.getParams(ob.toString(), getContext(), Constants.HISTORY_COUNTALL), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                mRefresh.finishLoadmore();
                mRefresh.finishRefreshing();
                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    if (bean.getRet() == 1) {
                        HistoryRepairOrderListBean repairOrderListBean = new Gson().fromJson(bean.getData(),HistoryRepairOrderListBean.class);
                        if(page == 1){
                            mAllRepairOrder = repairOrderListBean.getPageData().getResults();
                        }else {
                            mAllRepairOrder.addAll(repairOrderListBean.getPageData().getResults());
                        }
                        titleTv.setText("已处理("+repairOrderListBean.getCountc()+")");
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
