package com.zhongzhi.grid.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.zhongzhi.grid.R;
import com.zhongzhi.grid.adapter.AAComAdapter;
import com.zhongzhi.grid.adapter.AAViewHolder;
import com.zhongzhi.grid.bean.BaseBean;
import com.zhongzhi.grid.bean.ContactItem;
import com.zhongzhi.grid.bean.ContactListBean;
import com.zhongzhi.grid.myview.MyListView;
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
 * Created by Administrator on 2018/7/12.
 */

public class DepartmentActivity extends BaseActivity {
    private String id;
    private MyListView listView;
    private MyListView listView2;
    private AAComAdapter aaComAdapter;
    private AAComAdapter aaComAdapter2;
    private TwinklingRefreshLayout mRefresh;
    private int page = 1;
    private int type;
    private String worknum;
    private ArrayList<ContactItem> mAllList = new ArrayList<>();
    private ArrayList<ContactItem> mAllList2 = new ArrayList<>();
    private String phoneStr;
    private boolean isInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        id = getIntent().getExtras().getString("id");
        type = getIntent().getExtras().getInt("type");
        isInput = getIntent().getExtras().getBoolean("isInput");
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
                Intent intent = new Intent(DepartmentActivity.this,DepartmentActivity.class);
                intent.putExtra("id",contactItem.getOrganiz_code());
                intent.putExtra("worknum",worknum);
                intent.putExtra("type",type);
                intent.putExtra("isInput",isInput);

                startActivityForResult(intent,1001);
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ContactItem contactItem = (ContactItem) aaComAdapter2.getItem(i);
                if(type == 1 ){
                    if(isInput){
                        alert_edit(contactItem);
                    }else {
                        confirm(contactItem.getORGANIZ_CODE(),null);
                    }

                }else if(type == 2 ) {
                    confirm(contactItem.getORGANIZ_CODE(),null);
                }

            }
        });
    }

    public void alert_edit(final ContactItem contactItem ){
        final EditText et = new EditText(this);
        new AlertDialog.Builder(this).setTitle("请输入派单说明")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!TextUtils.isEmpty(et.getText().toString().trim())){
                            confirm(contactItem.getORGANIZ_CODE(),et.getText().toString().trim());
                        }

                        //按下确定键后的事件
//                        Toast.makeText(getApplicationContext(), et.getText().toString(),Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("取消",null).show();
    }

    @Override
    protected void initView() {
        super.initView();
        loadingDialog = createLoadingDialog(this,"");
        TextView title = (TextView)findViewById(R.id.elect);
        title.setText("部门");
        ImageView back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        listView = (MyListView) findViewById(R.id.listview);
        listView2 = (MyListView) findViewById(R.id.listview2);
        mRefresh = (TwinklingRefreshLayout)findViewById(R.id.refresh);
        aaComAdapter = new AAComAdapter<ContactItem>(this,R.layout.department_list_item,mAllList) {
            @Override
            public void convert(AAViewHolder holder, ContactItem mt) {
                TextView name = holder.getTextView(R.id.name);
                name.setText(mt.getOrganiz_post_name());
            }
        };
        listView.setAdapter(aaComAdapter);

        aaComAdapter2 = new AAComAdapter<ContactItem>(this,R.layout.contact_list_user_item,mAllList) {
            @Override
            public void convert(AAViewHolder holder, final ContactItem mt) {
                TextView name = holder.getTextView(R.id.user_name);
                TextView phone = holder.getTextView(R.id.phone);
                TextView call = holder.getTextView(R.id.call);
                name.setText(mt.getName());
                phone.setText(mt.getPHONE_NUM1());

                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ContextCompat.checkSelfPermission(DepartmentActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            Log.i(TAG, "需要授权 ");
                            if (ActivityCompat.shouldShowRequestPermissionRationale(DepartmentActivity.this, Manifest.permission.CALL_PHONE)) {
//                Log.i(TAG, "拒绝过了");
                                Toast.makeText(DepartmentActivity.this, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
                            } else {
//                Log.i(TAG, "进行授权");
                                ActivityCompat.requestPermissions(DepartmentActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 100);
                            }
                        } else {
//            Log.i(TAG, "不需要授权 ");

                            Intent intent = new Intent(Intent.ACTION_CALL);
                            phoneStr = mt.getPHONE_NUM1();
                            Uri data = Uri.parse("tel:" + phoneStr);
                            intent.setData(data);
                            startActivity(intent);
                        }

                    }
                });
            }
        };
        listView2.setAdapter(aaComAdapter2);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Log.i(TAG, "同意授权");
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + phoneStr);
                intent.setData(data);
                startActivity(intent);
            } else {
//                Log.i(TAG, "拒绝授权");

            }
        }
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
            ob.put("companyId",id);
            ob.put("page", page);
            if (TextUtils.isEmpty(id)){
                ob.put("organizcode", SharedPrefsUtil.getValue(this,"organizCode",null));
            }
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
                            mAllList2 = contactListBean.getPageData().getResults();
                        }else {
//                            mAllList.addAll(contactListBean.getDepartList());
                            mAllList2.addAll(contactListBean.getPageData().getResults());
                        }
                        aaComAdapter.mDatas = mAllList;
                        aaComAdapter.notifyDataSetChanged();
                        aaComAdapter2.mDatas = mAllList2;
                        aaComAdapter2.notifyDataSetChanged();
                        if(mAllList.size() > 0){
                            listView.setVisibility(View.VISIBLE);
                        }else {
                            listView.setVisibility(View.GONE);
                        }
                        if(mAllList2.size() > 0){
                            listView2.setVisibility(View.VISIBLE);
                        }else {
                            listView2.setVisibility(View.GONE);
                        }
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
     * 获取数据
     */
    private void confirm(String midOperatPerson,String str) {
        if (!HttpUtil.isNetworkConnected(this)) {
            showToast(getString(R.string.net_hint));
            return;
        }
        loadingDialog.show();
        JSONObject ob = new JSONObject();
        try {
            ob.put("worknum",worknum);
            if(!TextUtils.isEmpty(str)){
                ob.put("explain",str);
            }
            ob.put("organizCode", midOperatPerson);
            if (type == 1){
                ob.put("status", "2");
            }else {
                ob.put("status", "3");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
//        MyLog.e("ssss", "herer is ob===>" + ob.toString());
        x.http().post(Tool.getParams(ob.toString(), this, Constants.CONFIRM), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
                Log.e("请求成功", "成功" + result);
                BaseBean bean = BaseBean.praseJSONObject(result);
                if (bean != null) {
                    if (bean.getRet() == 1) {
                        setResult(1);
                        finish();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001 && resultCode == 1){
            setResult(1);
            finish();
        }
    }
}
