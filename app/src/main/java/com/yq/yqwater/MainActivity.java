package com.yq.yqwater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.smtlibrary.tabbars.BadgeDismissListener;
import com.smtlibrary.tabbars.JPTabBar;
import com.smtlibrary.tabbars.OnTabSelectListener;
import com.smtlibrary.tabbars.anno.NorIcons;
import com.smtlibrary.tabbars.anno.SeleIcons;
import com.smtlibrary.tabbars.anno.Titles;
import com.smtlibrary.utils.LogUtils;
import com.yq.adapt.Adapter;
import com.yq.fragment.BuletoothFragment;
import com.yq.fragment.DownFragment;
import com.yq.fragment.ScanFragment;
import com.yq.fragment.TongJiFragment;
import com.yq.fragment.UpdateFragment;
import com.yq.tools.DBManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private ViewPager mViewPager;
    private List<Fragment> mTabContents;
    private Adapter mAdapter;



    /** 低下的标题栏 */
    private JPTabBar mTabbar;
    /** 四大标签中正在显示的Fragment */
    private Fragment fragment;
    private long exitTime;

    /** 户名 */
    public String userName;
    /** 登录账号 */
    public String usernumb;



    @Titles
    private static final int[] mTitles = {R.string.update, R.string.scan, R.string.tj, R.string.download, R.string.buletooth};

    @SeleIcons
    private static final int[] mSeleIcons = {R.mipmap.upload, R.mipmap.scan, R.mipmap.chart, R.mipmap.download, R.mipmap.bluetooth};

    @NorIcons
    private static final int[] mNormalIcons = {R.mipmap.upload_nomal, R.mipmap.scan_nomal, R.mipmap.chart_nomal, R.mipmap.download_nomal, R.mipmap.bluetooth_nomal};


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MeApplcition.mgr = new DBManager(this);

        initView();

        startService(new Intent(this, MyUpLoadService.class));

        Intent intent = getIntent();
        userName= intent.getStringExtra("key");
        usernumb= intent.getStringExtra("value");


//        Log.d("m520",userName);
//        Log.d("m520","usernumb"+usernumb);
    }

    private void initView() {

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabbar = (JPTabBar) findViewById(R.id.tabbar);

        initData();
    }

    private void initData() {
        mTabContents = new ArrayList<>();
        mTabContents.add(UpdateFragment.newInstance());
        mTabContents.add(ScanFragment.newInstance());
        mTabContents.add(TongJiFragment.newInstance());
        mTabContents.add(DownFragment.newInstance(1));
        mTabContents.add(BuletoothFragment.newInstance());

        mAdapter = new Adapter(getSupportFragmentManager(), mTabContents);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(5);

        //设置容器
        mTabbar.setContainer(mViewPager);
        //切换页面
        mTabbar.setSelectTab(1, false);
        fragment = mTabContents.get(1);
        //设置Badge消失的代理
        mTabbar.setDismissListener(new BadgeDismissListener() {
            @Override
            public void onDismiss(int position) {
            }
        });
        mTabbar.setTabListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int index) {
                fragment = mTabContents.get(index);
            }

            @Override
            public void onClickMiddle(View middleBtn) {
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_F9) {
            if (fragment instanceof ScanFragment) {
                LogUtils.sysout("========key:", keyCode);

                ScanFragment scanFragment = (ScanFragment) fragment;
                scanFragment.onKeyDown(keyCode, event);
            }
        }

        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != MeApplcition.mgr)
            MeApplcition.mgr.closeDb();

        stopService(new Intent(this, MyUpLoadService.class));
    }

}
