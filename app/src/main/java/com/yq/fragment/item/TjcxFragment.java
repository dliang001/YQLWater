package com.yq.fragment.item;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.smtlibrary.dialog.SweetAlertDialog;
import com.yq.model.Cbj;
import com.yq.model.QueryBean;
import com.yq.tasks.presenter.TaskPresenterImpl;
import com.yq.tasks.views.QueryView;
import com.yq.utils.TimeUtils;
import com.yq.yqwater.MeApplcition;
import com.yq.yqwater.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * 四大标签 -- 统计<p>
 * 指示器页面之一  --  查询页面
 */
public class TjcxFragment extends Fragment implements OnDateSetListener, QueryView {

    /** 点我选择年月 -- 文本 -- 但是点击事件设在R.id.time3 控件上*/
    @Bind(R.id.textView3)
    TextView textView3;
    /** 弹出选择年月弹窗 -- 文本*/
    @Bind(R.id.time3)
    TextView time3;

    /** 查询 -- 按钮*/
    @Bind(R.id.btn_tj)
    Button btnTj;
    /** 显示查询的结果 -- 文本*/
    @Bind(R.id.result_msg)
    TextView resultMsg;


    /** 点我输入标签号 -- 文本*/
    @Bind(R.id.textView1)
    TextView textView1;
    /** 输入标签号 -- 编辑框*/
    @Bind(R.id.time1)
    EditText time1;

    /** 点我输入手机号 -- 文本*/
    @Bind(R.id.textView2)
    TextView textView2;
    /** 输入手机号 -- 编辑框*/
    @Bind(R.id.time2)
    EditText time2;


    private TimePickerDialog mDialogYearMonth;
    private TaskPresenterImpl taskPresenter;
    private String msg;
    private SweetAlertDialog sweetAlertDialog;
    /** 存放：标签号*/
    private String str1;
    /** 存放：年月 -- 如：201704*/
    private String cbye;
    private Cbj cbj;


    public static TjcxFragment newInstance() {
        return new TjcxFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tj, container, false);
        ButterKnife.bind(this, view);

        time3.setText(TimeUtils.getCurrentTimeyyyyMM());     // 默认是 当前年月，可以修改
        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("正在加载中...");
        taskPresenter = new TaskPresenterImpl(this);

        // 测试
        // time1.setText("0001878478");

        return view;
    }

    /** "查询按钮"的点击事件*/
    @OnClick(R.id.btn_tj)
    void tj() {

        str1 = time1.getText().toString().trim();           // 标签号
        String str2 = time2.getText().toString().trim();    // 手机号

        //        chaxunbt(str2);
        if (TextUtils.isEmpty(str1) && TextUtils.isEmpty(str2)) {
            Toast.makeText(getContext(), "请输入标签号或手机号码", Toast.LENGTH_SHORT).show();
            return;
        } else if (str1.length() < 10 && str2.length() < 11) {
            Toast.makeText(getContext(), "请输入正确的标签号或手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        cbye = time3.getText().toString().trim();           // 年月
        if (TextUtils.isEmpty(cbye) || cbye.length() != 6) {        // 默认是6个字符，不到多或少
            return;
        }

        // Log.d("m520", "标签号=" + str1 + "手机号=" + str2 + "月份=" + cbye);
        taskPresenter.queryData(str1, str2, cbye);
        //        if (cbData.getRet().equals("ok"))
        //            qView.onquerySuccess(cbData);
        //        else {
        //            qView.onFaile("标签号或手机号不存在!");
        //        }

    }

    //查询统计按钮
/*    private void chaxunbt(String cbye) {
//        if (TextUtils.isEmpty(cbye) || cbye.length() != 6)
//            return;
        int allUser = MeApplcition.mgr.selectAlluser(cbye);
        int allMoney = MeApplcition.mgr.selectAllYjMoney(cbye);
        int dayMoney = MeApplcition.mgr.selectDayAllYjMoney();
        int ycbUser = MeApplcition.mgr.selectChaoBiaoCount(cbye);
        int dayChaoBiao = MeApplcition.mgr.selectTodayChaoBiaoCount();

        String msg = "本月应抄表总户数: " + allUser + "\n" +
                "当日已抄表户数: " + dayChaoBiao + "\n" +
                "当日已收费金额: " + dayMoney + "元" + "\n" +
                "本月已抄表户数: " + ycbUser + "\n" +
                "本月未抄表户数: " + (allUser - ycbUser) + "\n" +
                "本月已收费金额: " + allMoney + "元";
        resultMsg.setText(msg);
        resultMsg.setBackgroundResource(R.drawable.bt);
    }*/

    @Override
    public void onquerySuccess(QueryBean cbData) {
        msg = "";
        if (cbData != null) {
            List<QueryBean.DATABean> data = cbData.getDATA();
            for (int i = 0; i < data.size(); i++) {
                QueryBean.DATABean dataBean = data.get(i);
                String mc = dataBean.getMc();
                String bm = dataBean.getBm();
                String bcye = dataBean.getBcye();
                String bqsf = dataBean.getBqsf();
                String bybs = dataBean.getBybs();
                String jfje = dataBean.getJfje();
                String jfrq = dataBean.getJfrq();
                String sybs = dataBean.getSybs();
                String sysl = dataBean.getSysl();
             // String xzje = dataBean.getXzje();   //销账金额
                String scye = dataBean.getScye();
                String ysqj = dataBean.getYsqj();
                String ysxz = dataBean.getYsxz();


                msg =
                        "户\t\t\t名: " + mc + "\n" +
                        "户\t\t\t号: " + bm + "\n" +
                        "用水期间: " + ysqj + "\n" +
                        "本月表数: " + bybs + "\n" +
                        "上月表数: " + sybs + "\n" +
                        "收费水量: " + sysl + "\n" +
                        "上次结余: " + scye + "\n" +
                        "交费金额: " + jfje + "元" + "\n" +
                        "本期水费: " + bqsf + "元" + "\n" +
                        "本次结余: " + bcye + "元" + "\n" +
                        "用水性质: " + ysxz+"\n"+
                        "交费日期: " + jfrq + "\n";
            }
            if (TextUtils.isEmpty(msg)) {
                cbj = MeApplcition.mgr.queryTheCursor(str1);    //查询标签是否存在 -- 在数据库中查
                if (null != cbj){
                    msg = "本月没有记录!";
                }else{
                    msg = "您输入的标签号或手机号不存在!";
                }
            }
            resultMsg.setText(msg);
            resultMsg.setBackgroundResource(R.drawable.bt);

        } else {
            Toast.makeText(getContext(), "查询失败,请重试！", Toast.LENGTH_SHORT).show();
        }
    }




    /******************************** BaseView start -- 跟TaskPresenterImpl有关 *********************/
    @Override
    public String userId() {
        return null;
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



    /*************** OnDateSetListener接口  TimePickerDialog 选择月份了回调这个方法 ******************/
    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Date date = new Date(millseconds);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        String str = format.format(date);
        time3.setText(str);
    }
    /******************************** OnDateSetListener接口 end ****************************************/

    // "选择年月" 文本 的点击事件
    @OnClick(R.id.time3)
    void getTime() {
        mDialogYearMonth = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH)
                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                .setCallBack(TjcxFragment.this)
                .build();
        mDialogYearMonth.show(getActivity().getSupportFragmentManager(), "YEAR_MONTH");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
