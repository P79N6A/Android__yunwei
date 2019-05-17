package com.zhongzhi.grid.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.zhongzhi.grid.R;
import com.zhongzhi.grid.activity.DepartmentActivity;
import com.zhongzhi.grid.adapter.AAComAdapter;
import com.zhongzhi.grid.adapter.AAViewHolder;
import com.zhongzhi.grid.bean.BaseBean;
import com.zhongzhi.grid.bean.ContactItem;
import com.zhongzhi.grid.bean.ContactListBean;
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
 * Created by Administrator on 2018/7/7.
 */

public class FragmentTab4 extends BaseFragment {
    private ListView listView;
    private AAComAdapter aaComAdapter;
    private TwinklingRefreshLayout mRefresh;
    private int page = 1;
    private ArrayList<ContactItem> mAllList = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_4,container,false);
        listView = (ListView)view.findViewById(R.id.listview);
        mRefresh = (TwinklingRefreshLayout)view.findViewById(R.id.refresh);
        aaComAdapter = new AAComAdapter<ContactItem>(getContext(),R.layout.contact_list_item,mAllList) {
            @Override
            public void convert(AAViewHolder holder, ContactItem mt) {
                TextView name = holder.getTextView(R.id.name);
                name.setText(mt.getOrganiz_post_name());
            }
        };
        listView.setAdapter(aaComAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                Intent intent = new Intent(getContext(),DepartmentActivity.class);
                intent.putExtra("id",contactItem.getOrganiz_code());
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
            ob.put("companyId","0");
            ob.put("page", page);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(Tool.getParams(ob.toString(), getContext(), Constants.COMPANY_LIST), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                mRefresh.finishLoadmore();
                mRefresh.finishRefreshing();
                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    if (bean.getRet() == 1) {
                        ContactListBean contactListBean = new Gson().fromJson(bean.getData(),ContactListBean.class);
//                        if(page == 1){
//                            mAllList = contactListBean.getDepartList();
//                        }else {
//                            mAllList.addAll(contactListBean.getDepartList());
//                        }
                        aaComAdapter.mDatas = contactListBean.getDepartList();
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
