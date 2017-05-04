package com.yq.fragment.item;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smtlibrary.dialog.SweetAlertDialog;
import com.yq.utils.TimeUtils;
import com.yq.yqwater.MeApplcition;
import com.yq.yqwater.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.yq.yqwater.R.id.bt_upphone;
import static com.yq.yqwater.R.id.et_userid;
import static com.yq.yqwater.R.id.et_userphone;

/**
 * 四大标签 -- 上传<p>
 * 指示器页面之一  --  上传电话页面
 */

public class UploadDhFragment extends BaseFragment implements View.OnClickListener {

    /** 用户编号 -- 编辑框 */
    @Bind(et_userid)
    EditText etUserid;

    /** 电话号码 -- 编辑框 */
    @Bind(et_userphone)
    EditText etUserphone;

    /** 上传数据 -- 按钮 */
    @Bind(bt_upphone)
    Button btUpphone;

    /** 用户编号 */
    String upid = "";
    /** 电话号码 */
    String upnumber = "";


    public static UploadDhFragment newInstance() {
        return new UploadDhFragment();
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upphonenumber, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    // 点击事件
    @OnClick(R.id.bt_upphone)
    public void onClick(View v) {
        switch (v.getId()) {
            case bt_upphone:

                upid = etUserid.getText().toString().trim();
                upnumber = etUserphone.getText().toString().trim();

                if (TextUtils.isEmpty(upid) || TextUtils.isEmpty(upnumber)) {
                    Toast.makeText(getActivity(), "请填写数据!", Toast.LENGTH_SHORT).show();
                    return;
                }
                taskPresenter.uploadPhone(upid, upnumber);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onSuccess() {

        SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("修改结果")
                //.setContentText("上传成功 \n 本地修改成功")
                .setConfirmText("  确认  ")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });
        //Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();

        boolean b = MeApplcition.mgr.updateDh(upid,upnumber);
        if(b) {
            // Toast.makeText(getActivity(), "本地修改成功", Toast.LENGTH_SHORT).show();
            dialog.setContentText("上传成功 \n 本地修改成功");
        } else {
            // Toast.makeText(getActivity(), "本地修改失败", Toast.LENGTH_SHORT).show();
            dialog.setContentText("上传成功 \n 本地修改失败");
        }

        dialog.show();

        etUserid.setText("");
        etUserphone.setText("");
    }
}
