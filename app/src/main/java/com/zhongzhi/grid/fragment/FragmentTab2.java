package com.zhongzhi.grid.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongzhi.grid.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Administrator on 2018/7/7.
 */

public class FragmentTab2 extends BaseFragment {
    private RadioGroup mRadioGroup;
    private FragmentAllRepairOrder fragmentAllRepairOrder;
    private FragmentUntreatedRepairOrder fragmentUntreatedRepairOrder;
    private FragmentDealRepairOrder fragmentDealRepairOrder;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private ImageView calendar;
    private DatePickerDialog datePicker;
    private TextView mDateTv;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_2,container,false);
//        fm = getActivity().getSupportFragmentManager();
        radioButton1 = (RadioButton)view.findViewById(R.id.radio1);
        radioButton2 = (RadioButton)view.findViewById(R.id.radio2);
        radioButton3 = (RadioButton)view.findViewById(R.id.radio3);
        fm = getChildFragmentManager();
        ft = fm.beginTransaction();
        mRadioGroup = (RadioGroup)view.findViewById(R.id.radiogroup);
        calendar = (ImageView)view.findViewById(R.id.calendar);
        mDateTv = (TextView)view.findViewById(R.id.date);
        calendar.setOnClickListener(this);
        Calendar c4 = Calendar.getInstance(); // 当时的日期和时间
          datePicker=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                int month = monthOfYear+1;
                String monthStr = "";
                String dayStr = "";
                if(month < 10){
                    monthStr = "0"+month;
                }else {
                    monthStr = month+"";
                }
                if(dayOfMonth < 10){
                    dayStr = "0"+dayOfMonth;
                }else {
                    dayStr = dayOfMonth+"";
                }
                mDateTv.setText(year+"-"+monthStr+"-"+dayStr);
                if(radioButton1.isChecked()){
                    fragmentAllRepairOrder.time = mDateTv.getText().toString();
                    fragmentAllRepairOrder.mRefresh.startRefresh();
                }else if(radioButton2.isChecked()){
                    fragmentUntreatedRepairOrder.time = mDateTv.getText().toString();
                    fragmentUntreatedRepairOrder.mRefresh.startRefresh();
                }else if(radioButton3.isChecked()){
                    fragmentDealRepairOrder.time = mDateTv.getText().toString();
                    fragmentDealRepairOrder.mRefresh.startRefresh();
                }
//                Toast.makeText(getContext(), year+"year "+(monthOfYear+1)+"month "+dayOfMonth+"day", Toast.LENGTH_SHORT).show();
            }
        }, c4.get(Calendar.YEAR), c4.get(Calendar.MONTH), c4.get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
          mDateTv.setText(dateFormat.format(c4.getTime()));
        return view;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.calendar:
                datePicker.show();
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radio1:
                        showFragment(0);
                        break;
                    case R.id.radio2:
                        showFragment(1);
                        break;
                    case R.id.radio3:
                        showFragment(2);
                        break;
                }
            }
        });
        showFragment(0);
    }

    public void showFragment(int index){

        FragmentTransaction ft = fm.beginTransaction();
        hideFragment(ft);
        switch (index){

            case 0:

                /**
                 * 如果Fragment为空，就新建一个实例
                 * 如果不为空，就将它从栈中显示出来
                 */
                if (fragmentAllRepairOrder==null){
                    fragmentAllRepairOrder=new FragmentAllRepairOrder();
                    fragmentAllRepairOrder.setTitleTv(radioButton1);
                    fragmentAllRepairOrder.setTitleTv2(radioButton2);
                    fragmentAllRepairOrder.setTitleTv3(radioButton3);
                    ft.add(R.id.fragment_content,fragmentAllRepairOrder,"fragmentAllRepairOrder");
                }else {
                    ft.show(fragmentAllRepairOrder);
                }

                break;
            case 1:
                if (fragmentUntreatedRepairOrder==null){
                    fragmentUntreatedRepairOrder=new FragmentUntreatedRepairOrder();
                    fragmentUntreatedRepairOrder.setTitleTv(radioButton2);
                    ft.add(R.id.fragment_content,fragmentUntreatedRepairOrder,"fragmentUntreatedRepairOrder");
                }else {
                    ft.show(fragmentUntreatedRepairOrder);
                }

                break;
            case 2:
                if (fragmentDealRepairOrder==null){
                    fragmentDealRepairOrder=new FragmentDealRepairOrder();
                    fragmentDealRepairOrder.setTitleTv(radioButton3);
                    ft.add(R.id.fragment_content,fragmentDealRepairOrder,"fragmentDealRepairOrder");
                }else {
//                    mShoppingCartFragment.clearCheckHashMap();
                    ft.show(fragmentDealRepairOrder);
                }

                break;

        }

        ft.commitAllowingStateLoss();
    }

    public void hideFragment(FragmentTransaction ft){
        //如果不为空，就先隐藏起来
        if (fragmentAllRepairOrder!=null){
            ft.hide(fragmentAllRepairOrder);
        }
        if(fragmentUntreatedRepairOrder!=null) {
            ft.hide(fragmentUntreatedRepairOrder);
        }
        if(fragmentDealRepairOrder!=null) {
            ft.hide(fragmentDealRepairOrder);
        }

    }
}
