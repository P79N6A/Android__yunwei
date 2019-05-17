package com.zhongzhi.grid.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.zhongzhi.grid.GDactivity.activity.dainjuhe.DianJuHeActivity;
import com.zhongzhi.grid.R;
import com.zhongzhi.grid.activity.DepartmentActivity;
import com.zhongzhi.grid.activity.RepairOrderInfoActivity;
import com.zhongzhi.grid.adapter.AAComAdapter;
import com.zhongzhi.grid.adapter.AAViewHolder;
import com.zhongzhi.grid.bean.BaseBean;
import com.zhongzhi.grid.bean.RepairOrderItem;
import com.zhongzhi.grid.bean.RepairOrderListBean;
import com.zhongzhi.grid.util.Constants;
import com.zhongzhi.grid.util.HttpUtil;
import com.zhongzhi.grid.util.SharedPrefsUtil;
import com.zhongzhi.grid.util.Tool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.zhongzhi.grid.util.ToastUtils.showToast;

/**
 * Created by Administrator on 2018/7/7.
 */

public class FragmentTab1 extends BaseFragment {

    private AAComAdapter aaComAdapter;
    private ListView mListview;
    private ArrayList<RepairOrderItem> mAllRepairOrder = new ArrayList<>();
    private int page = 1;
    private TwinklingRefreshLayout mRefresh;
    private TextView mTitle;
    private Spinner sp1;
    private Spinner sp2;
    private Spinner sp3;
    private String item1;
    private String item2;
    private String item3;
    public List<String> mList1 = new ArrayList<>();
    public List<String> mList2 = new ArrayList<>();
    public List<String> mList3 = new ArrayList<>();
    public static final String AUTONAVI_PACKAGENAME = "com.autonavi.minimap";
    private ArrayList<RepairOrderItem> jsonStr;
    private ArrayAdapter arr_adapter1;
    private ArrayAdapter arr_adapter2;
    private ArrayAdapter arr_adapter3;
    private String latitude;
    private String longitude;
    private String mAddressStr;
    private Context mContext;
    private String lng;
    private String lat;
    private ImageView share;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_1,container,false);

        sp2 = (Spinner) view.findViewById(R.id.sp_area);
        sp3 = (Spinner) view.findViewById(R.id.sp_fault);
        mListview = (ListView)view.findViewById(R.id.listview);
        mRefresh = (TwinklingRefreshLayout)view.findViewById(R.id.refresh);
        mTitle = (TextView)view.findViewById(R.id.title);
        loadingDialog = createLoadingDialog(getContext(),"");
        share=view.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), DianJuHeActivity.class);
             /*   Bundle bundle=new Bundle();
                bundle.putSerializable("mAllRepairOrder",mAllRepairOrder);*/
              //  intent.putExtra("mAllRepairOrder",mAllRepairOrder);
                startActivity(intent);
            }
        });
      //  share.setVisibility(view.VISIBLE);
        /**
         * 取三个下拉框所选的值
         */
        sp1 = (Spinner) view.findViewById(R.id.sp_elect);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView)view;
                tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
                item1 = arr_adapter1.getItem(position).toString();
                mRefresh.startRefresh();
                getArea();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp2 = (Spinner) view.findViewById(R.id.sp_area);
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView)view;
                tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
                item2 = arr_adapter2.getItem(position).toString();
                mRefresh.startRefresh();
                getExplain();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp3 = (Spinner) view.findViewById(R.id.sp_fault);
        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView)view;
                tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
                item3 = arr_adapter3.getItem(position).toString();
                mRefresh.startRefresh();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                page = 1;
                getData();
            }

        });
        getUnit();
        mRefresh.startRefresh();
        return view;
    }


    private void getUnit(){
        if (!HttpUtil.isNetworkConnected(getContext())) {
            showToast(getString(R.string.net_hint));
            return;
        }
        JSONObject ob = new JSONObject();
        try {
            ob.put("organizcode",SharedPrefsUtil.getValue(getContext(),"organizCode",null));
            ob.put("page",page);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        x.http().post(Tool.getParams(ob.toString(), getContext(), Constants.SELECT_MANAGE), new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                mRefresh.finishLoadmore();
                mRefresh.finishRefreshing();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String manage_nuit = jsonObject1.getString("MANAGE_UNIT");
                        mList1.add(manage_nuit);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
    private void getArea() {
        if (!HttpUtil.isNetworkConnected(getContext())) {
            showToast(getString(R.string.net_hint));
            return;
        }
        JSONObject ob = new JSONObject();
        try {
            ob.put("organizcode",SharedPrefsUtil.getValue(getContext(),"organizCode",null));
            ob.put("MANAGE_UNIT",item1);
            ob.put("page",page);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        x.http().post(Tool.getParams(ob.toString(), getContext(), Constants.SELECT_AREA), new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                mRefresh.finishLoadmore();
                mRefresh.finishRefreshing();
                mList2.clear();
                mList2.add("台区名称");
                arr_adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mList2);
                sp2.setAdapter(arr_adapter2);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String manage_nuit = jsonObject1.getString("AREA_NAME");
                        mList2.add(manage_nuit);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
    private void getExplain() {
        if (!HttpUtil.isNetworkConnected(getContext())) {
            showToast(getString(R.string.net_hint));
            return;
        }
        JSONObject ob = new JSONObject();
        try {
            ob.put("organizcode",SharedPrefsUtil.getValue(getContext(),"organizCode",null));
            ob.put("MANAGE_UNIT",item1);
            ob.put("AREA_NAME",item2);
            ob.put("page",page);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        x.http().post(Tool.getParams(ob.toString(), getContext(), Constants.SELECT_EXPLAIN), new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                mRefresh.finishLoadmore();
                mRefresh.finishRefreshing();
                mList3.clear();
                mList3.add("故障类型");
                arr_adapter3 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mList3);
                sp3.setAdapter(arr_adapter3);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String manage_nuit = jsonObject1.getString("DISPACH_EXPLAIN");
                        mList3.add(manage_nuit);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        aaComAdapter = new AAComAdapter<RepairOrderItem>(getContext(),R.layout.repair_order_item,mAllRepairOrder) {
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
                TextView dispatching = holder.getTextView(R.id.dispatching);
                TextView assist = holder.getTextView(R.id.assist);
                TextView benren = holder.getTextView(R.id.benren);
                final TextView jushou = holder.getTextView(R.id.jushou);
                View fg_line = holder.getViews(R.id.fg_ling);
                LinearLayout bottomLayout = (LinearLayout)holder.getViews(R.id.bottom_layout);
               // ImageView location = (ImageView)holder.getViews(R.id.location);
               // location.setTag(mt);
                orderNum.setText("工单编号："+mt.getWORK_LIST_NUM());
                order_gds.setText(mt.getPOEER_SUPPLY_STATION());
                switch (mt.getWORK_LIST_PROCE()){
                    case 1:
                        order_status.setText("未处理");
                        fg_line.setVisibility(View.VISIBLE);
                        bottomLayout.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        order_status.setText("流转中");
                        fg_line.setVisibility(View.VISIBLE);
                        bottomLayout.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        order_status.setText("处理中");
                        fg_line.setVisibility(View.GONE);
                        bottomLayout.setVisibility(View.GONE);
                        break;
                    case 4:
                        order_status.setText("已结束");
                        fg_line.setVisibility(View.GONE);
                        bottomLayout.setVisibility(View.GONE);
                        break;
                }
                order_userNum.setText(mt.getUSER_NUM());
                order_name.setText(mt.getELEC_USER_NAME());
                order_taiqu.setText(mt.getAREA_NAME());
                order_manager.setText(mt.getAREA_ADMINISTRATOR());
                order_gds.setText(mt.getMANAGE_UNIT());
                order_des.setText(mt.getDISPACH_EXPLAIN());
                benren.setTag(mt);
                dispatching.setTag(mt);
                assist.setTag(mt);
                jushou.setTag(mt);
                jushou.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RepairOrderItem repairOrderItem = (RepairOrderItem)view.getTag();
                        confirm1(repairOrderItem.getWORK_LIST_NUM());
                    }
                });
                benren.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RepairOrderItem repairOrderItem = (RepairOrderItem)view.getTag();
                        confirm(repairOrderItem.getWORK_LIST_NUM());
//                        Intent intent = new Intent();
//                        intent.putExtra("worknum",repairOrderItem.getWORK_LIST_NUM());
//                        intent.putExtra("isEdit",true);
//                        intent.setClass(getContext(), RepairOrderInfoActivity.class);
//                        startActivity(intent);
                    }
                });
                dispatching.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RepairOrderItem repairOrderItem = (RepairOrderItem)view.getTag();
                        Intent intent = new Intent(getContext(), DepartmentActivity.class);
                        intent.putExtra("type",1);
                        if(repairOrderItem.getWORK_LIST_PROCE() == 1){
                            intent.putExtra("isInput",true);
                        }
                        intent.putExtra("worknum",repairOrderItem.getWORK_LIST_NUM());
                        startActivityForResult(intent,1001);
                    }
                });
                assist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RepairOrderItem repairOrderItem = (RepairOrderItem)view.getTag();
                        Intent intent = new Intent(getContext(), DepartmentActivity.class);
                        intent.putExtra("type",2);
                        if(repairOrderItem.getWORK_LIST_PROCE() == 1){
                            intent.putExtra("isInput",true);
                        }
                        intent.putExtra("worknum",repairOrderItem.getWORK_LIST_NUM());
                        startActivityForResult(intent,1001);
                    }
                });
            }
        };
        mListview.setAdapter(aaComAdapter);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RepairOrderItem repairOrderItem = (RepairOrderItem) aaComAdapter.getItem(i);
                Intent intent = new Intent();
                intent.putExtra("worknum",repairOrderItem.getWORK_LIST_NUM());
                intent.putExtra("status",repairOrderItem.getWORK_LIST_PROCE());
                intent.setClass(getContext(), RepairOrderInfoActivity.class);
                startActivity(intent);
            }
        });

        mList1.add("供电单位");
        arr_adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mList1);
        sp1.setAdapter(arr_adapter1);



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
            ob.put("organizcode",SharedPrefsUtil.getValue(getContext(),"organizCode",null));
            ob.put("page", page);
            ob.put("MANAGE_UNIT",item1);
            ob.put("AREA_NAME",item2);
            ob.put("DISPACH_EXPLAIN",item3);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(Tool.getParams(ob.toString(), getContext(), Constants.SELECT_ALL), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                mRefresh.finishLoadmore();
                mRefresh.finishRefreshing();
                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    if (bean.getRet() == 1) {
                        RepairOrderListBean repairOrderListBean = new Gson().fromJson(bean.getData(),RepairOrderListBean.class);
                        if(page == 1){
                            mAllRepairOrder = repairOrderListBean.getResults();
                        }else {
                            mAllRepairOrder.addAll(repairOrderListBean.getResults());
                        }
                        mTitle.setText("工单数量（"+mAllRepairOrder.size()+"）");
                        aaComAdapter.mDatas = mAllRepairOrder;
                        aaComAdapter.notifyDataSetChanged();

//                        List<String> myList = new ArrayList<String>();
//                        for (int k=0; k<mAllRepairOrder.size(); k++) {
//                            myList.add(mAllRepairOrder.get(k).getAREA_NAME());
//                        }
//
//                        try {
////                            JSONArray arr = new JSONArray((String)myList);
////                            mList1 = (List) arr.get(3);
////                            mList2 = new ArrayList<>();
//                            for (int i=0;i<mAllRepairOrder.size();i++){
////                                    JSONObject temp = (JSONObject) arr.get(i);
//                                Map<String, Object> map2=new HashMap<String, Object>();
//                                map2.put("AREA_NAME",myList.get(i));
//                                mList2.add(myList.get(i));
////                                    Map<String, String> map1=new HashMap<String, String>();
////                                    map1.put("MANAGE_UNIT",temp.getString("MANAGE_UNIT"));
////                                    mList1.add(map1);
////                                    Map<String, String> map3=new HashMap<String, String>();
////                                    map3.put("DISPACH_EXPLAIN",temp.getString("DISPACH_EXPLAIN"));
////                                    mList3.add(map3);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
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


    /**
     * 本人处理
     */
    private void confirm(String worknum) {
        if (!HttpUtil.isNetworkConnected(getContext())) {
            showToast(getString(R.string.net_hint));
            return;
        }
        loadingDialog.show();
        JSONObject ob = new JSONObject();
        try {
            ob.put("worknum",worknum);
            ob.put("organizCode", SharedPrefsUtil.getValue(getContext(),"organizCode",null));
            ob.put("status", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(Tool.getParams(ob.toString(), getContext(), Constants.CONFIRM), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                loadingDialog.dismiss();
                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    if (bean.getRet() == 1) {
                        mRefresh.startRefresh();
                    } else {
                        showToast(bean.getMsg());
                    }
                } else {
                }
            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                loadingDialog.dismiss();
            }

            @Override
            public void onFinished() {
                loadingDialog.dismiss();
            }
        });
    }

    /**
     * 拒收
     */
    private void confirm1(String worknum) {
        if (!HttpUtil.isNetworkConnected(getContext())) {
            showToast(getString(R.string.net_hint));
            return;
        }
        loadingDialog.show();
        JSONObject ob = new JSONObject();
        try {
            ob.put("worknum",worknum);
            ob.put("organizCode", SharedPrefsUtil.getValue(getContext(),"organizCode",null));
            ob.put("status", "4");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(Tool.getParams(ob.toString(), getContext(), Constants.CONFIRM), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", "成功" + result);
                loadingDialog.dismiss();
                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    if (bean.getRet() == 1) {
                        mRefresh.startRefresh();
                    } else {
                        showToast(bean.getMsg());

                    }

                } else {
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                loadingDialog.dismiss();
            }

            @Override
            public void onFinished() {
                loadingDialog.dismiss();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001 && resultCode == 1){
            mRefresh.startRefresh();
        }
    }
}
