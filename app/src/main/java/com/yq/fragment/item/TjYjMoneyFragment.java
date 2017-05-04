package com.yq.fragment.item;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.smtlibrary.dialog.SweetAlertDialog;
import com.yq.adapt.TjYjMoneyListAdapter;
import com.yq.model.Cbj;
import com.yq.model.YjMoneyBean;
import com.yq.utils.TimeUtils;
import com.yq.yqwater.MeApplcition;
import com.yq.yqwater.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * 四大标签 -- 统计<p>
 * 指示器页面之一  --  查询页面
 */
public class TjYjMoneyFragment extends Fragment implements OnDateSetListener {

    /** 点我选择年月 -- 文本 -- 但是点击事件设在R.id.time3 控件上*/
    @Bind(R.id.textView3)
    TextView textView3;
    /** 弹出选择年月弹窗 -- 文本*/
    @Bind(R.id.tv_select_time)
    TextView mTvSelectTime;

    /** 未上传的预交金额 */
    @Bind(R.id.tv_not_upload_count)
    TextView mTvNotUpLoadCount;

    /** 查询 -- 按钮*/
    @Bind(R.id.btn_tj_yjmoney)
    Button mBtnTjYjmoney;

    /** 显示未抄用户 */
    @Bind(R.id.lv_tj_yjmoney)
    ListView mListView;


    private TimePickerDialog mDialogYearMonth;
    private SweetAlertDialog sweetAlertDialog;


    ArrayList<YjMoneyBean> beanList = new ArrayList<>();
    TjYjMoneyListAdapter adapter;

    /** 存放：年月 -- 如：201704*/
    private String cbye;
    private Cbj cbj;


    private android.os.Handler mHandler = new android.os.Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            beanList = MeApplcition.mgr.selectAllNotUpLoadYjMoneyCbye(mTvSelectTime.getText().toString().trim());
            mTvNotUpLoadCount.setText(beanList.size() + "");

            adapter = new TjYjMoneyListAdapter(getActivity(), beanList);
            mListView.setAdapter(adapter);
            // adapter.notifyDataSetChanged();

            // for(TjNotChaoBiao bean : beanList)
            //    Log.d("m520", bean.toString());

        }
    };


    public static TjYjMoneyFragment newInstance() {
        return new TjYjMoneyFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tj_yjmoney, container, false);
        ButterKnife.bind(this, view);

        mTvSelectTime.setText(TimeUtils.getCurrentTimeyyyyMM());     // 默认是 当前年月，可以修改
        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("正在加载中...");


        adapter = new TjYjMoneyListAdapter(getActivity(), beanList);
        mListView.setAdapter(adapter);

        return view;
    }

    /** "查询按钮"的点击事件*/
    @OnClick(R.id.btn_tj_yjmoney)
    void onClick() {
        //Toast.makeText(getActivity(),"点击查询了", Toast.LENGTH_SHORT).show();

        mHandler.sendEmptyMessage(0);
    }



    /*************** OnDateSetListener接口  TimePickerDialog 选择月份了回调这个方法 ******************/
    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Date date = new Date(millseconds);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        String str = format.format(date);
        mTvSelectTime.setText(str);
    }
    /******************************** OnDateSetListener接口 end ****************************************/

    // "选择年月" 文本 的点击事件
    @OnClick(R.id.tv_select_time)
    void getTime() {
        mDialogYearMonth = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH)
                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                .setCallBack(TjYjMoneyFragment.this)
                .build();
        mDialogYearMonth.show(getActivity().getSupportFragmentManager(), "YEAR_MONTH");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
