package com.yq.yqwater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.smtlibrary.dialog.SweetAlertDialog;
import com.yq.model.LoginBean;
import com.yq.tasks.presenter.TaskPresenterImpl;
import com.yq.tasks.views.LoginView;
import com.yq.utils.TimeUtils;

import java.util.List;

/**
 * 主标签:
 *
 * @author tanK
 * @time 2017/3/6  21:56
 * @desc ${TODD}
 */
public class LoginActivity extends Activity implements LoginView ,Thread.UncaughtExceptionHandler{
    private EditText mNameEt;
    private EditText mPwdEt;
    private TaskPresenterImpl taskPresenter;
    private SweetAlertDialog sweetAlertDialog;
    LoginBean.DATABean dataBean;

    /** 户名 */
    String usernam;//姓名
    /** 登录账号 */
    String usernumb;//操作员编码

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
        mNameEt = (EditText) findViewById(R.id.name_et);
        mPwdEt = (EditText) findViewById(R.id.pwd_et);

        // 测试
        // mNameEt.setText("100000");
        // mPwdEt.setText("yc7741045");
    }

    public void loginClick(View v) {
        if(TimeUtils.compareTime(TimeUtils.getCurrentTimeyyyyMMdd(),"20170524","yyyyMMdd")) {

            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("提示")
                    .setContentText("使用时间已到")
                    .setConfirmText("  确认  ")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    }).show();
            return;
        }



        final String name = mNameEt.getText().toString();
        final String pwd = mPwdEt.getText().toString();
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
