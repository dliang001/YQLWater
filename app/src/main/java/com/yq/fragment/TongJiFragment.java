package com.yq.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smtlibrary.Indicator.RVPIndicator;
import com.yq.adapt.Adapter;
import com.yq.fragment.item.TjWaitFragment;
import com.yq.fragment.item.TjYjMoneyFragment;
import com.yq.fragment.item.TjcxFragment;
import com.yq.fragment.item.TjtjFragment;
import com.yq.yqwater.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 四大标签 -- 统计
 *
 */
public class TongJiFragment extends Fragment {

    /** 使用了 这个（指示器）第三方框架*/
    @Bind(R.id.indicator2)
    RVPIndicator mIndicator;

    @Bind(R.id.viewpager2)
    ViewPager mViewPager;

    /** 存放"指示器对应"的 Fragment -- 查询 && 未抄表 && 统计 */
    private List<Fragment> mTabContents;
    private Adapter mAdapter;
    /** 对应的指示器字符串数组 */
    private List<String> mDatas;
    private int mType;

    public static TongJiFragment newInstance() {
        TongJiFragment f = new TongJiFragment();
        return f;
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tongji, container, false);
        ButterKnife.bind(this, view);

        initData1();
        initView();

        return view;
    }

    private void initData1() {

        mDatas = Arrays.asList(getActivity().getResources().getStringArray(R.array.home_tabs2)); // 对应的指示器字符串数组
        mTabContents = new ArrayList<>();
        mTabContents.add(TjcxFragment.newInstance());
        mTabContents.add(TjWaitFragment.newInstance());
        mTabContents.add(TjYjMoneyFragment.newInstance());
        mTabContents.add(TjtjFragment.newInstance());

        mAdapter = new Adapter(getActivity().getSupportFragmentManager(), mTabContents);
    }

    private void initView() {
        mIndicator.setTabItemTitles(mDatas);
        mViewPager.setAdapter(mAdapter);

        // viewpager每次切换的时候， 会重新创建当前界面及左右界面三个界面， 每次切换都要重新onCreate,
        // 所以只要设置viewPager setOffscreenPageLimit即可避免这个问题。
        // viewPager.setOffscreenPageLimit(3);表示三个界面之间来回切换都不会重新加载
        mViewPager.setOffscreenPageLimit(4);
        mIndicator.setViewPager(mViewPager, 0);     // 将ViewPager  和 指示器 关联--默认选中索引为0的标签
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}