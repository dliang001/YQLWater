package com.yq.fragment.item;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smtlibrary.dialog.SweetAlertDialog;
import com.yq.yqwater.MeApplcition;
import com.yq.yqwater.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.yq.yqwater.R.id.et_userxz;
import static com.yq.yqwater.R.id.et_xzuserid;

/**
 * 四大标签 -- 上传<p>
 * 指示器页面之一  --  上传标签号页面
 */

public class UploadXzFragment extends BaseFragment implements View.OnClickListener {

    /** 用户编号 -- 编辑框 */
    @Bind(et_xzuserid)
    EditText etXzuserid;

    /** 标签号 -- 编辑框 */
    @Bind(et_userxz)
    EditText etUserxz;

    @Bind(R.id.bt_upxz)
    Button btUpxz;

    /** 用户编号 */
    String xzId = "";
    /** 标签号 */
    String xzValue = "";

    public static UploadXzFragment newInstance() {
        return new UploadXzFragment();
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upxz, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.bt_upxz)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_upxz:
                xzId = etXzuserid.getText().toString().trim();
                xzValue = etUserxz.getText().toString().trim();

                if (TextUtils.isEmpty(xzId) || TextUtils.isEmpty(xzValue)) {                // 两个"编辑框"都要填写
                    Toast.makeText(getActivity(), "请填写数据!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(! (xzValue.length() == 10)){
                    Toast.makeText(getActivity(), "请填10位的标签号!", Toast.LENGTH_SHORT).show();
                    return;
                }

                taskPresenter.upXzData(xzId, xzValue);
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



        boolean b = MeApplcition.mgr.updateDzbq(xzId,xzValue);
        if(b) {
            // Toast.makeText(getActivity(), "本地修改成功", Toast.LENGTH_SHORT).show();
            dialog.setContentText("上传成功 \n 本地修改成功");
        } else {
            // Toast.makeText(getActivity(), "本地修改失败", Toast.LENGTH_SHORT).show();
            dialog.setContentText("上传成功 \n 本地修改失败");
        }

        dialog.show();
        etXzuserid.setText("");
        etUserxz.setText("");
    }
}
