package com.yq.yqwater;

import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.smtlibrary.utils.LogUtils;
import com.yq.tools.DBManager;

import java.io.InputStream;
import java.io.OutputStream;

public class MeApplcition extends Application {

    public static RequestQueue rq;
    public static Context context;
    public static DBManager mgr;

    /** 蓝牙设置--对应的输出流 */
    public static OutputStream bluetoothOutputStream = null;
    /** 蓝牙设置--对应的输入流 */
    public static InputStream bluetoothInputStream = null;
    /** 蓝牙设置--对应的socket */
    public static BluetoothSocket bluetoothSocket = null;

    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();
        LogUtils.LOG_DEBUG = BuildConfig.DEBUG;
        rq = Volley.newRequestQueue(getApplicationContext());
        System.out.println("MeApplcition");
    }

    public static RequestQueue getHttpQueue() {
        return rq;
    }

}
