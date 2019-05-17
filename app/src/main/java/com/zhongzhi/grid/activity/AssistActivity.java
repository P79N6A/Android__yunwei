package com.zhongzhi.grid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.zhongzhi.grid.bean.ContactItem;
import com.zhongzhi.grid.bean.ContactListBean;
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

public class AssistActivity extends BaseActivity {
    private ListView listView;
    private AAComAdapter aaComAdapter;
    private TwinklingRefreshLayout mRefresh;
    private int page = 1;
    private String worknum;
    private ArrayList<ContactItem> mAllList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatching);
        worknum = getIntent().getExtras().getString("worknum");
        initView();

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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ContactItem contactItem = (ContactItem) aaComAdapter.getItem(i);
                Intent intent = new Intent(AssistActivity.this,DepartmentActivity.class);
                intent.putExtra("type",2);
                intent.putExtra("worknum",worknum);
                intent.putExtra("id",contactItem.getOrganiz_code());
                startActivityForResult(intent,1001);
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        ImageView back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        TextView title = (TextView)findViewById(R.id.elect);
        title.setText("协助选择");
//        TextView confirm = (TextView)findViewById(R.id.save);
//        confirm.setText("确定");
//        confirm.setOnClickListener(this);

        listView = (ListView)findViewById(R.id.listview);
        mRefresh = (TwinklingRefreshLayout)findViewById(R.id.refresh);
        aaComAdapter = new AAComAdapter<ContactItem>(this,R.layout.contact_list_item,mAllList) {
            @Override
            public void convert(AAViewHolder holder, ContactItem mt) {
                TextView name = holder.getTextView(R.id.name);
                name.setText(mt.getOrganiz_post_name());
            }
        };
        listView.setAdapter(aaComAdapter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.save:
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
            ob.put("companyId","0");
            ob.put("page", page);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(Tool.getParams(ob.toString(), this, Constants.COMPANY_LIST), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                mRefresh.finishLoadmore();
                mRefresh.finishRefreshing();
                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    if (bean.getRet() == 1) {
                        ContactListBean contactListBean = new Gson().fromJson(bean.getData(),ContactListBean.class);
                        if(page == 1){
                            mAllList = contactListBean.getDepartList();
                        }else {
                            mAllList.addAll(contactListBean.getDepartList());
                        }
                        aaComAdapter.mDatas = mAllList;
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001 && resultCode == 1){
            finish();
        }
    }
}
