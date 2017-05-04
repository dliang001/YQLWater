package com.yq.yqwater;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.smtlibrary.utils.LogUtils;
import com.smtlibrary.utils.PreferenceUtils;
import com.yq.model.UpCbjBean;
import com.yq.model.YjMoneyBean;
import com.yq.tasks.presenter.TaskPresenter;
import com.yq.tasks.presenter.TaskPresenterImpl;

/**
 *
 */
public class MyUpLoadService extends Service {

    /** 线程 运行/停止 标志*/
    private boolean isRun;
    private TaskPresenter taskPresenter;
    private String userId;

    public MyUpLoadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        taskPresenter = new TaskPresenterImpl();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRun = true;

        userId = PreferenceUtils.getString(this, "userId", "110101");

        UploadData uploadData = new UploadData();
        uploadData.start();

        UploadYjMoney uploadYjMoney = new UploadYjMoney();
        uploadYjMoney.start();


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        isRun = false;
        super.onDestroy();
    }



    /**
     * 五秒上传一个客户
     */
    class UploadData extends Thread {

        @Override
        public void run() {
            super.run();
            while (isRun) {
                try {
                    //Log.d("m520", "UploadData");
                    LogUtils.sysout("====uploadData:", getId());
                    Thread.sleep(5000);
                    if (null != MeApplcition.mgr) {
                        UpCbjBean cbj = MeApplcition.mgr.getFirstData();
                        if (!TextUtils.isEmpty(cbj.getHmph()))
                            taskPresenter.upDzbqData(cbj, userId);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * 五秒上传一个预交金额
     */
    class UploadYjMoney extends Thread {

        @Override
        public void run() {
            super.run();
            while (isRun) {
                try {
                    LogUtils.sysout("====uploadYjMoney:", getId());
                    Thread.sleep(5000);
                    if (null != MeApplcition.mgr) {
                        YjMoneyBean yjm = MeApplcition.mgr.getYjMoneyFirstData();

                        if (!TextUtils.isEmpty(yjm.getYjMoney())) {
                            taskPresenter.upPayData(yjm.getId(), yjm.getHmph(), yjm.getYjMoney(), yjm.getCzybm());

                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //Log.d("m520", "UploadYjMoney -- e.getMessage()" + e.getMessage());
                }

            }
        }
    }

}
