package com.yq.fragment.item;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.smtlibrary.dialog.SweetAlertDialog;
import com.smtlibrary.utils.LogUtils;
import com.smtlibrary.utils.PreferenceUtils;
import com.yq.model.CbData;
import com.yq.model.Cbj;
import com.yq.model.UserArrears;
import com.yq.tasks.presenter.TaskPresenterImpl;
import com.yq.tasks.views.IView;
import com.yq.utils.TimeUtils;
import com.yq.yqwater.MainActivity;
import com.yq.yqwater.MeApplcition;
import com.yq.yqwater.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.yq.yqwater.R.id.bt_sean;
import static com.yq.yqwater.R.id.et_yerdate;


/**
 * 四大标签 -- 下载<p>
 * 指示器页面之一  --  用户数据
 */
public class DownloadDataFragment extends Fragment implements View.OnClickListener, IView, OnDateSetListener {

    /** 选择用水年月 -- 文本 */
    @Bind(et_yerdate)
    TextView etYerdate;
    /** 按钮 -- 下载 */
    @Bind(bt_sean)
    Button btSean;
    /** ListView  */
    @Bind(R.id.lv_mrd)
    ListView lvMrd;
    /** 抄表员号码 -- 文本*/
    @Bind(R.id.et_cnumber)
    TextView etCnumber;


    private List<Cbj> dowcbj = new ArrayList<>();
    private SweetAlertDialog sweetAlertDialog;
    private TaskPresenterImpl taskPresenter;

    private String userName;
    private String usernumb;


    public static DownloadDataFragment newInstance() {
        return new DownloadDataFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("正在加载中...");
        taskPresenter = new TaskPresenterImpl(this);


    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.getmrd, container, false);
        ButterKnife.bind(this, view);

        //获取抄表员的 所有欠费用户
        etYerdate.setText(TimeUtils.getCurrentTimeyyyyMM());  // 获取默认时间 -- 如：201704
        userName = ((MainActivity) getActivity()).userName;
        usernumb = ((MainActivity) getActivity()).usernumb;

//        PreferenceUtils.putString(getActivity(), "userId", usernumb);
//        PreferenceUtils.putString(getActivity(), "userName", userName);

        etCnumber.setText(userName);


        return view;
    }


    @OnClick(R.id.bt_sean)
    public void onClick(View v) {
        switch (v.getId()) {
            case bt_sean:
                LogUtils.sysout("======usid", usernumb);
                final String fhb = etYerdate.getText().toString().trim();

                if (fhb.equals("")) {
                    Toast.makeText(getActivity(), "请填写数据!", Toast.LENGTH_SHORT).show();
                } else {

                    if (MeApplcition.mgr.selectAlluser(fhb) > 0) {  // 大于0，说明这个月已下载到数据库了
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(fhb + "月份已下载!")
                                .setConfirmText("   确定  ")
                                .show();
                    } else
                        taskPresenter.downData(usernumb, fhb);      // 下载 此月，此用户，所有区域的用户数据
                }
                break;
        }
    }


    /******************************** BaseView start -- 跟TaskPresenterImpl有关 *********************/
    @Override
    public String userId() {
        return PreferenceUtils.getString(getActivity(), "userId", "110101");
    }

    @Override
    public void showPress() {
        sweetAlertDialog.show();
    }

    @Override
    public void hidePress() {
        sweetAlertDialog.dismiss();
    }

    @Override
    public void onFaile(String msg) {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(msg)
                .setConfirmText("确认")
                .show();
    }
    @Override
    public void onSuccess() {

    }
    /******************************** BaseView end  -- 跟TaskPresenterImpl有关 ************************/


    /******************************** IView接口 start ****************************************/
    @Override
    public void onDownSuccess(CbData cbData) {
        if (null != cbData.getDATA()) {
            for (int i = 0; i < cbData.getDATA().size(); i++) {     // 一个个获取区域
                CbData.CbjDetail b = cbData.getDATA().get(i);
                dowcbj.addAll(b.getGPOUPDATA());                    // 将所有区域中的"cbj-bean"(用户数据)放到 dowcbj这个全局变量中
            }
        }
        if (dowcbj.size() > 0) {
            MeApplcition.mgr.add(cbData);
            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("下载并保存成功。")
                    .setConfirmText("确认")
                    .show();
        }

    }

    @Override
    public void onDownUserInfo(UserArrears userArrears) {

    }
    /******************************** IView接口 end ****************************************/




    /*************** OnDateSetListener接口  TimePickerDialog 选择月份了回调这个方法 ******************/
    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Date date = new Date(millseconds);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");//精确到分钟
        String str = format.format(date);
        etYerdate.setText(str);
    }
    /******************************** OnDateSetListener接口 end ****************************************/


    // "选择用水年月" 文本 的点击事件
    @OnClick(et_yerdate)
    void getTime() {
        TimePickerDialog mDialogYearMonth = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH)
                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                .setCallBack(this)
                .build();
        mDialogYearMonth.show(getActivity().getSupportFragmentManager(), "YEAR_MONTH");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
