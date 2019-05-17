package com.zhongzhi.grid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.zhongzhi.grid.R;

/**
 * Created by Administrator on 2018/7/7.
 */

public class FragmentTab3 extends BaseFragment {
    private RadioGroup mRadioGroup;
    private FragmentDayStatistical fragmentDayStatistical;
    private FragmentMonthStatistical fragmentMonthStatistical;
    private FragmentYearStatistical fragmentYearStatistical;
    private FragmentManager fm;
    private FragmentTransaction ft;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_3,container,false);
        fm = getChildFragmentManager();
        ft = fm.beginTransaction();
        mRadioGroup = (RadioGroup)view.findViewById(R.id.radiogroup);
        return view;
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
                if (fragmentDayStatistical==null){
                    fragmentDayStatistical=new FragmentDayStatistical();
                    ft.add(R.id.fragment_content,fragmentDayStatistical,"fragmentDayStatistical");
                }else {
                    ft.show(fragmentDayStatistical);
                }

                break;
            case 1:
                if (fragmentMonthStatistical==null){
                    fragmentMonthStatistical=new FragmentMonthStatistical();
                    ft.add(R.id.fragment_content,fragmentMonthStatistical,"fragmentMonthStatistical");
                }else {
                    ft.show(fragmentMonthStatistical);
                }

                break;
            case 2:
                if (fragmentYearStatistical==null){
                    fragmentYearStatistical=new FragmentYearStatistical();
                    ft.add(R.id.fragment_content,fragmentYearStatistical,"fragmentYearStatistical");
                }else {
//                    mShoppingCartFragment.clearCheckHashMap();
                    ft.show(fragmentYearStatistical);
                }

                break;

        }

        ft.commitAllowingStateLoss();
    }

    public void hideFragment(FragmentTransaction ft){
        //如果不为空，就先隐藏起来
        if (fragmentDayStatistical!=null){
            ft.hide(fragmentDayStatistical);
        }
        if(fragmentMonthStatistical!=null) {
            ft.hide(fragmentMonthStatistical);
        }
        if(fragmentYearStatistical!=null) {
            ft.hide(fragmentYearStatistical);
        }

    }
}
