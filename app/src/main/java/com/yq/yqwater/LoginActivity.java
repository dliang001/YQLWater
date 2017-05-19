package com.yq.yqwater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.smtlibrary.dialog.SweetAlertDialog;
import com.yq.model.LoginBean;
import com.yq.tasks.presenter.TaskPresenterImpl;
import com.yq.tasks.views.LoginView;
import com.yq.utils.SharePrefUtil;
import com.yq.utils.TimeUtils;
import com.yq.view.ClearEditText;

import java.util.List;

/**
 * 主标签:
 *
 * @author tanK
 * @time 2017/3/6  21:56
 * @desc ${TODD}
 */
public class LoginActivity extends Activity implements LoginView ,Thread.UncaughtExceptionHandler{
    private ClearEditText mNameEt;
    private ClearEditText mPwdEt;
    private TaskPresenterImpl taskPresenter;
    private SweetAlertDialog sweetAlertDialog;
    LoginBean.DATABean dataBean;

    /** 户名 */
    String usernam;//姓名
    /** 登录账号 */
    String usernumb;//操作员编码


    /** 记住账号密码 -- 选择框 */
    private CheckBox mCbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //在此调用下面方法，才能捕获到线程中的异常
        Thread.setDefaultUncaughtExceptionHandler(this);

        initUI();
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("正在加载中...");
        taskPresenter = new TaskPresenterImpl(this);
    }

    private void initUI() {
        mNameEt = (ClearEditText) findViewById(R.id.name_et);
        mPwdEt = (ClearEditText) findViewById(R.id.pwd_et);

        mCbRemember = (CheckBox) findViewById(R.id.cb_remember);

        boolean isRemember = SharePrefUtil.getBoolean(LoginActivity.this, SharePrefUtil.KEY.LOGIN_REMEMBER, true);
        mCbRemember.setChecked(isRemember);

        if(isRemember){
            mNameEt.setText(SharePrefUtil.getString(LoginActivity.this, SharePrefUtil.KEY.LOGIN_ACCOUNT, ""));
            mPwdEt.setText(SharePrefUtil.getString(LoginActivity.this, SharePrefUtil.KEY.LOGIN_PASSWORD, ""));
        }

        // 测试
        // mNameEt.setText("100000");
        // mPwdEt.setText("yc7741045");
    }

    public void loginClick(View v) {
        if(TimeUtils.compareTime(TimeUtils.getCurrentTimeyyyyMMdd(),"20170615","yyyyMMdd")) {

            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("提示")
                    .setContentText("试用时间已到")
                    .setConfirmText("  确认  ")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    }).show();
            return;
        }



        final String name = mNameEt.getText().toString().trim();
        final String pwd = mPwdEt.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else {


            taskPresenter.loginData(name, pwd);

        }
        //        if("ysb".equals(name) && "123456".equals(pwd)){
        //            Toast.makeText(this, "登录成功！",Toast.LENGTH_SHORT ).show();
        //            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        //            intent.putExtra("userName","yushunbao");
        //            startActivity(intent);
        //            overridePendingTransition(0,0);
        //            finish();}
    }

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
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(msg)
                .setConfirmText("确认")
                .show();
    }

    @Override
    public void onSuccess() {

    }


    @Override
    public void onLoginSuccess(LoginBean cbData) {
        if (cbData != null) {
            Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show();

            // 记录用户密码到本地！
            SharePrefUtil.saveString(LoginActivity.this, SharePrefUtil.KEY.LOGIN_ACCOUNT, mNameEt.getText().toString().trim());
            SharePrefUtil.saveString(LoginActivity.this, SharePrefUtil.KEY.LOGIN_PASSWORD, mPwdEt.getText().toString().trim());
            SharePrefUtil.saveBoolean(LoginActivity.this, SharePrefUtil.KEY.LOGIN_REMEMBER, mCbRemember.isChecked());



            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

            List<LoginBean.DATABean> data = cbData.getDATA();
            for (int i = 0; i < data.size(); i++) {
                dataBean = data.get(i);
                usernam = dataBean.getXm();
                usernumb = dataBean.getCzybm();
            }

            Log.d("m520","Loging -- usernumb:"+usernumb );

            intent.putExtra("key", usernam);
            intent.putExtra("value", usernumb);
            startActivity(intent);
            finish();

        }else{
            Toast.makeText(this, "登录失败,请重试！", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //在此处理异常， arg1即为捕获到的异常
        Log.d("m520","+++++++++++++++++++++++++++++++++++++++++++++  Exception  ++++++++++++++++++++++++++++++++++++++++++++++++");
        Log.d("m520","AAA:   " + ex);
        Log.d("m520","+++++++++++++++++++++++++++++++++++++++++++++  Exception  ++++++++++++++++++++++++++++++++++++++++++++++++");
    }
}
