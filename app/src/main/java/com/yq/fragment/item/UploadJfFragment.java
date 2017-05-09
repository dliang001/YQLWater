package com.yq.fragment.item;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smtlibrary.dialog.SweetAlertDialog;
import com.smtlibrary.utils.LogUtils;
import com.yq.model.BluethBean;
import com.yq.model.Cbj;
import com.yq.tools.BluethListAdapter;
import com.yq.utils.TimeUtils;
import com.yq.view.MyListView;
import com.yq.yqwater.MainActivity;
import com.yq.yqwater.MeApplcition;
import com.yq.yqwater.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.yq.yqwater.R.id.bt_print;
import static com.yq.yqwater.R.id.bt_uppay;
import static com.yq.yqwater.R.id.et_paysnumber;
import static com.yq.yqwater.R.id.et_payuserid;
import static com.yq.yqwater.R.id.et_payuserje;

/**
 * 上传金额 -- 预交金额
 */

public class UploadJfFragment extends BaseFragment implements View.OnClickListener {

    /** 用户编号 -- 编辑框 */
    @Bind(et_payuserid)
    EditText etPayuserid;
    /** 金额 -- 编辑框 */
    @Bind(et_payuserje)
    EditText etPayuserje;
    /** 用户编号 -- 编辑框 */
    @Bind(et_paysnumber)
    TextView etPaysnumber;
    /** 收费员 -- 文本框 */
    @Bind(bt_uppay)
    Button btUppay;
    /** 打印 -- 按钮 */
    @Bind(R.id.bt_print)
    Button btPrint;//打印



    private String userName;
    private String usernumb;


    private String prinshowinfo;
    private String payId;       // 户号
    private String payje;       // 本次缴费金额
    private String totalpayje;  // 总缴费金额
    private String payTime;     // 缴费时间


    public static UploadJfFragment newInstance() {
        return new UploadJfFragment();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.uppayment, container, false);
        ButterKnife.bind(this, view);

        userName = ((MainActivity) getActivity()).userName;
        usernumb = ((MainActivity) getActivity()).usernumb;

        etPaysnumber.setText(userName);

        btPrint.setEnabled(true);

        return view;
    }


//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        userName = ((MainActivity) getActivity()).userName;
//        usernumb = ((MainActivity) getActivity()).usernumb;
//        etPaysnumber.setText(userName);
//    }

    @OnClick({bt_uppay, bt_print})
    public void onClick(View v) {
        switch (v.getId()) {
            case bt_uppay:
                payId = etPayuserid.getText().toString().trim();
                payje = etPayuserje.getText().toString().trim();

                if (TextUtils.isEmpty(payId) || TextUtils.isEmpty(payje) || TextUtils.isEmpty(usernumb)) {
                    Toast.makeText(getActivity(), "请填写数据!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(MeApplcition.mgr.queryByHmphAndCbye(payId, TimeUtils.getCurrentTimeyyyyMM())) {

                    upYjMoney();

                }else{
                    SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示")
                            .setContentText("当月没有下载此用户")
                            .setConfirmText("  确认  ")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            });

                    dialog.show();
                }

                break;

            //打印
            case bt_print:

                if (TextUtils.isEmpty(payId)) {
                    Toast.makeText(getContext(), "缴费成功后才能打印", Toast.LENGTH_SHORT).show();
                } else {
                    Cbj cbj = MeApplcition.mgr.queryByHmphCursor(payId);
                    prinshowinfo = "                   预交水费小票" + "\n\n" +
                            "户号:" + payId + "\n" +
                            "户名:" + cbj.getHm() + "\n" +
                            "地址:" + cbj.getDz() + "\n" +
                            "预交金额:" + payje + "\n" +
                            "收费人员:" + userName + "\n" +
                            "收费时间:" + payTime + "\n" +
                            "鸦鹊岭自来水厂" + "\n" +
                            "电话:7741045" + "\n\n\n";

                    if(MeApplcition.bluetoothSocket != null) {
                        if(MeApplcition.bluetoothSocket.isConnected() &&
                                MeApplcition.bluetoothOutputStream != null) {
                            try {
                                MeApplcition.bluetoothOutputStream.write(prinshowinfo.getBytes("GBK"));
                                MeApplcition.bluetoothOutputStream.flush();
                            }catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "连接错误："+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getActivity(), "请连接蓝牙设备", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getActivity(), "请选择蓝牙设备", Toast.LENGTH_SHORT).show();
                    }

                    break;
                }
        }
    }


    /**
     * 上传金额
     */
    private void upYjMoney(){
        payTime = TimeUtils.getCurrentTime();
        boolean b = MeApplcition.mgr.addYjMoney(payId, payje, usernumb, TimeUtils.getCurrentTimeRq());

        SweetAlertDialog dialog = null;
        if(b) {
            dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("上传结果")
                    .setContentText("成功缴费:" + payje + "元")
                    .setConfirmText("  确认  ")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();

                            if (MeApplcition.mgr.queryByHmphAndCbye(payId, TimeUtils.getCurrentTimeyyyyMM())) {

                                int yjmoney = MeApplcition.mgr.selectByhmphYjMoney(payId, TimeUtils.getCurrentTimeyyyyMM());//先获取数据库里面的金额
                                Log.d("m520", "数据库原有金额" + yjmoney);
                                totalpayje = String.valueOf(yjmoney + Integer.parseInt(payje));
                                Log.d("m520", "所有预交金额" + totalpayje);
                                MeApplcition.mgr.updateYjMoney(payId, totalpayje);      //先保存到数据库
                            }

                        }
                    });
        }else {
            dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("上传结果")
                    .setContentText("上传失败")
                    .setConfirmText("  确认  ")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();

                        }
                    });
        }

        dialog.show();
        etPayuserid.setText("");
        etPayuserje.setText("");
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
