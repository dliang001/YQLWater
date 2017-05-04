package com.yq.model;

import android.bluetooth.BluetoothDevice;

/**
 * 蓝牙信息
 *
 */
public class BluethBean {
    private BluetoothDevice bluetoothDevice;
    private boolean isAdd;

    public BluethBean(BluetoothDevice bluetoothDevice, boolean isAdd) {
        this.bluetoothDevice = bluetoothDevice;
        this.isAdd = isAdd;
    }

    /** 蓝牙信息的详情 */
    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }
    /** 蓝牙信息的详情 */
    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    /** 这个蓝牙是不是被添加过的？ -- 是就不用配对连接 */
    public boolean isAdd() {
        return isAdd;
    }
    /** 这个蓝牙是不是被添加过的？ -- 是就不用配对连接 */
    public void setAdd(boolean add) {
        isAdd = add;
    }
}
