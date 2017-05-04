package com.yq.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smtlibrary.Indicator.RVPIndicator;
import com.yq.adapt.Adapter;
import com.yq.fragment.item.DownloadDataFragment;
import com.yq.fragment.item.DownloadQfFragment;
import com.yq.yqwater.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 四大标签 -- 下载
 *
 */
public class DownFragment extends Fragment {


    /** 使用了 这个（指示器）第三方框架*/
    @Bind(R.id.indicator1)
    RVPIndicator mIndicator;
    @Bind(R.id.viewpager1)
    ViewPager mViewPager;

    /** 存放"指示器对应"的 Fragment -- 用户数据 && 用户欠费信息 */
    private List<Fragment> mTabContents;
    private Adapter mAdapter;
    /** 对应的指示器字符串数组 */
    private List<String> mDatas;
    private int mType;

    public static DownFragment newInstance(int type) {
        DownFragment f = new DownFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);            // 在MainActivity中传递了————1
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 当Activity类动态加载fragment时可以通过fragment的setArguments()传入值，
        // 并在fragment类中通过fragment的getArguments()方法获得传入的值；
        if (getArguments() != null) {
            mType = getArguments().getInt("type");
        }
//        Intent intent = getActivity().getIntent();
//        userName = intent.getStringExtra("userName");
//        Log.d("m520",userName);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.down, container, false);

        ButterKnife.bind(this, view);
        initData1();
        initView();
        return view;
    }

    /**
     * 下载的
     */
    private void initData1() {

        mDatas = Arrays.asList(getActivity().getResources().getStringArray(R.array.home_tabs1));  // 对应的指示器字符串数组

        mTabContents = new ArrayList<>();
        mTabContents.add(DownloadDataFragment.newInstance());
        mTabContents.add(DownloadQfFragment.newInstance());

        mAdapter = new Adapter(getActivity().getSupportFragmentManager(), mTabContents);
    }

    private void initView() {

        mIndicator.setTabItemTitles(mDatas);
        mViewPager.setAdapter(mAdapter);

        // viewpager每次切换的时候， 会重新创建当前界面及左右界面三个界面， 每次切换都要重新onCreate,
        // 所以只要设置viewPager setOffscreenPageLimit即可避免这个问题。
        // viewPager.setOffscreenPageLimit(3);表示三个界面之间来回切换都不会重新加载
        mViewPager.setOffscreenPageLimit(2);
        mIndicator.setViewPager(mViewPager, 0);     // 将ViewPager  和 指示器 关联--默认选中索引为0的标签
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
