package com.zhongzhi.grid.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.zhongzhi.grid.R;
import com.zhongzhi.grid.adapter.AAComAdapter;
import com.zhongzhi.grid.adapter.AAViewHolder;
import com.zhongzhi.grid.bean.BaseBean;
import com.zhongzhi.grid.bean.ExperienceItem;
import com.zhongzhi.grid.bean.ExperienceListBean;
import com.zhongzhi.grid.util.Constants;
import com.zhongzhi.grid.util.HttpUtil;
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

public class ExperienceIndustryActivity extends BaseActivity {
    private AAComAdapter aaComAdapter;
    private ListView mListView;
    private TwinklingRefreshLayout mRefresh;
    private int page = 1;
    private ArrayList<ExperienceItem> mAllRepairOrder = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience_industry);
        initView();
        aaComAdapter = new AAComAdapter<ExperienceItem>(this,R.layout.experience_list_item,mAllRepairOrder) {
            @Override
            public void convert(AAViewHolder holder, ExperienceItem mt) {
               TextView title = holder.getTextView(R.id.elect);
               TextView content = holder.getTextView(R.id.content);
               title.setText(mt.getTITLE());
               content.setText(mt.getCONTENT());
            }
        };
        mListView.setAdapter(aaComAdapter);
        mRefresh.startRefresh();
    }

    @Override
    protected void initView() {
        super.initView();
        ImageView back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        TextView title = (TextView)findViewById(R.id.elect);
        title.setText("行业经验");
        mListView = (ListView)findViewById(R.id.listview);
        mRefresh = (TwinklingRefreshLayout)findViewById(R.id.refresh);
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

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        if (!HttpUtil.isNetworkConnected(this)) {
            showToast(getString(R.string.net_hint));
            return;
        }

        JSONObject ob = new JSONObject();
        try {
//            ob.put("username", SharedPrefsUtil.getValue(this,"userName",null));
            ob.put("page", page);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(Tool.getParams(ob.toString(), this, Constants.EXPERIENCE_LIST), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                mRefresh.finishLoadmore();
                mRefresh.finishRefreshing();
                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    if (bean.getRet() == 1) {
                        ExperienceListBean repairOrderListBean = new Gson().fromJson(bean.getData(),ExperienceListBean.class);
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
