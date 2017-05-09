package com.yq.fragment;

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
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rscja.deviceapi.RFIDWithLF;
import com.smtlibrary.dialog.SweetAlertDialog;
import com.smtlibrary.utils.LogUtils;
import com.yq.model.BluethBean;
import com.yq.tools.BluethListAdapter;
import com.yq.view.MyListView;
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

/**
 * 四大标签 -- 上传
 *
 */
public class BuletoothFragment extends Fragment implements View.OnClickListener {

    /** 已连接蓝牙名称--文本 */
    @Bind(R.id.tv_buletooth_name)
    TextView mTvBluetoothName;

    /** 搜索蓝牙--按钮 */
    @Bind(R.id.btn_search_bluetooth)
    Button mBtnSearchBluetooth;
    /** 测试--按钮 */
    @Bind(R.id.btn_test)
    Button mBtnTest;
    /** 放蓝牙的 listview */
    @Bind(R.id.lv_bluetooth)
    MyListView mLvBluetooth;



    /** 蓝牙的适配器 */
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private List<BluetoothDevice> bondedDevicesList;

    /** 存放"蓝牙设备"的列表 */
    private List<BluethBean> searchDevicesList;
    private BluethListAdapter mSearchAdapter;
    /** 蓝牙设置--当前连接的？ */
    private BluetoothDevice device;

    SweetAlertDialog searchDialog = null;


    public static BuletoothFragment newInstance() {
        BuletoothFragment f = new BuletoothFragment();
        return f;
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buletooth, container, false);
        ButterKnife.bind(this, view);
        initListener();
        initData();
        return view;
    }

    private void initListener() {
        mBtnSearchBluetooth.setOnClickListener(this);
        mBtnTest.setOnClickListener(this);
    }

    private void initData() {
        mBtnTest.setEnabled(false);

        mTvBluetoothName.setText("未连接蓝牙设备！");

        searchDevicesList = new ArrayList<BluethBean>();                            // 蓝牙list
        mSearchAdapter = new BluethListAdapter(getActivity(), searchDevicesList);
        mLvBluetooth.setAdapter(mSearchAdapter);
        mLvBluetooth.setOnItemClickListener(new AdapterView.OnItemClickListener() {    // 蓝牙ListView点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BluethBean b = searchDevicesList.get(position);
                device = b.getBluetoothDevice();

                if (b.isAdd()) {                                    // 曾经配对过的
                    close();
                    new connectTask(device).execute();                                // 连接获得输入输出流
                } else {
                    try {
                        // 配对
                        Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                        createBondMethod.invoke(device);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });


        // 注册用以接收到--已搜索到的--蓝牙设备的receiver      过滤器
        IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mFilter.addAction(BluetoothDevice.ACTION_FOUND);
        mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        // 注册广播接收器，接收并处理搜索结果
        getActivity().registerReceiver(receiver, mFilter);


        if (null != bluetoothAdapter) {

            // 得到所有———已经配对的————蓝牙适配器对象
            // Set -- 集合 没有重复
            Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();

            if (devices.size() > 0) {
                //用迭代
                for (Iterator iterator = devices.iterator(); iterator.hasNext(); ) {
                    //得到BluetoothDevice对象,也就是说得到配对的蓝牙适配器
                    BluetoothDevice device = (BluetoothDevice) iterator.next();

                    //得到远程蓝牙设备的地址
                    LogUtils.sysout("mytag", device.getAddress());

                    searchDevicesList.add(new BluethBean(device, true));        // 放到 蓝牙ListView ,刷新数据
                    mSearchAdapter.notifyDataSetChanged();
                }
            }

        }
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("onReceiveonReceive");
            String action = intent.getAction();
            // 获得已经搜索到的蓝牙设备
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // 搜索到的不是已经绑定的蓝牙设备
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    // 防止重复添加
                    if (searchDevicesList.indexOf(device) == -1)
                        searchDevicesList.add(new BluethBean(device, false));
                    mSearchAdapter.notifyDataSetChanged();
                }
                // 搜索完成
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {

            } else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                // 状态改变的广播
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                if (TextUtils.isEmpty(name) || device.getName().equalsIgnoreCase(name)) {
                    int connectState = device.getBondState();
                    switch (connectState) {
                        case BluetoothDevice.BOND_NONE:  //10
                            Toast.makeText(getActivity(), "取消配对：" + device.getName(), Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothDevice.BOND_BONDING:  //11
                            Toast.makeText(getActivity(), "正在配对：" + device.getName(), Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothDevice.BOND_BONDED:   //12
                            Toast.makeText(getActivity(), "完成配对：" + device.getName(), Toast.LENGTH_SHORT).show();
                            try {
                                // 连接
                                new connectTask(device).execute();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            }

            if(searchDialog != null)
                searchDialog.dismiss();
        }
    };




    private void unpairDevice(BluetoothDevice device) {
        try {
            Method m = device.getClass().getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void close() {
        if (null == MeApplcition.bluetoothSocket)
            return;
        if (MeApplcition.bluetoothSocket.isConnected()) {
            try {
                MeApplcition.bluetoothOutputStream.close();
                MeApplcition.bluetoothInputStream.close();
                MeApplcition.bluetoothSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 异步 -- 蓝牙设备的连接（客户端）
     */
    public class connectTask extends AsyncTask<String, Integer, Boolean> {
        SweetAlertDialog dialog = null;
        BluetoothDevice mDevice = null;

        /**
         * 蓝牙设备的连接（客户端）
         *
         * @param device 蓝牙设备
         */
        public connectTask(BluetoothDevice device){
            mDevice = device;
        }

        @Override
        protected void onPreExecute() {                                         // 执行前
            super.onPreExecute();
            dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("正在连接蓝牙设备...");

            dialog.show();

        }

        @Override
        protected Boolean doInBackground(String... strings) {            // 执行中

            boolean b = false;
            // 固定的UUID              00001101-0000-1000-8000-00805F9B34FB
            final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
            UUID uuid = UUID.fromString(SPP_UUID);

            try {
                MeApplcition.bluetoothSocket = mDevice.createRfcommSocketToServiceRecord(uuid);
                MeApplcition.bluetoothSocket.connect();

                MeApplcition.bluetoothOutputStream = MeApplcition.bluetoothSocket.getOutputStream();
                MeApplcition.bluetoothInputStream = MeApplcition.bluetoothSocket.getInputStream();

                b = true;
            } catch (Exception e) {
                e.printStackTrace();
                //Log.d("m520","e.getMessage():"+e.getMessage());
                b = false;
            }

            return b;
        }

        @Override
        protected void onPostExecute(Boolean b) {           // 执行后
            super.onPostExecute(b);
            dialog.dismiss();

            if(b) {
                dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("连接结果")
                        .setContentText("连接成功！")
                        .setConfirmText("  确认  ")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();

                            }
                        });
                mBtnTest.setEnabled(true);

                if(device != null)
                    mTvBluetoothName.setText("已连接-"+device.getName());

            }else{
                dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("连接结果")
                        .setContentText("连接失败！")
                        .setConfirmText("  确认  ")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();

                            }
                        });

                mBtnTest.setEnabled(false);
                mTvBluetoothName.setText("未连接蓝牙设备！");
            }

            dialog.show();
        }
    }



    /**
     * 异步--搜索蓝牙设备
     */
    public class searchTask extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {                                         // 执行前
            super.onPreExecute();
            searchDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("正在搜索蓝牙设备...");

            searchDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {            // 执行中
            try {
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    getActivity().startActivityForResult(enableBtIntent, 1);
                } else {
                    bluetoothAdapter.startDiscovery();
                }
            } catch (Exception e) {
                e.printStackTrace();
                searchDialog.dismiss();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String str) {           // 执行后
            super.onPostExecute(str);


        }
    }

    @Override
    public void onDestroy() {
        if (device != null) {
            close();
            // System.out.println("清空!!!");
        }
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btn_search_bluetooth:                 // 搜索蓝牙
                new searchTask().execute();
                break;

            case R.id.btn_test:
                String test = "这是测试!\n\n\n\n";;

                if(MeApplcition.bluetoothSocket != null) {
                    if(MeApplcition.bluetoothSocket.isConnected() &&
                            MeApplcition.bluetoothOutputStream != null) {
                        try {
                            MeApplcition.bluetoothOutputStream.write(test.getBytes("GBK"));
                            MeApplcition.bluetoothOutputStream.flush();
                            Log.d("m520", "喵喵喵？");
                        }catch (IOException e) {
                            e.printStackTrace();
                            mBtnTest.setEnabled(false);
                            mTvBluetoothName.setText("未连接蓝牙设备！");
                            Toast.makeText(getActivity(), "连接错误："+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("m520", "IOException :e.getMessage()"+e.getMessage());
                        }catch (Exception e){
                            Log.d("m520", "Exception :e.getMessage()"+e.getMessage());
                        }
                    }else{
                        Toast.makeText(getActivity(), "请连接蓝牙设备", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "请选择蓝牙设备", Toast.LENGTH_SHORT).show();
                }

                break;

            default:

                break;
        }
    }


}
