package com.yq.fragment;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.rscja.deviceapi.RFIDWithLF;
import com.smtlibrary.dialog.SweetAlertDialog;
import com.smtlibrary.utils.LogUtils;
import com.smtlibrary.utils.PreferenceUtils;
import com.yq.fragment.item.BaseFragment;
import com.yq.model.BluethBean;
import com.yq.model.Cbj;
import com.yq.tools.MyListAdapter;
import com.yq.utils.PlayRing;
import com.yq.utils.Prices;
import com.yq.utils.TimeUtils;
import com.yq.view.MyListView;
import com.yq.yqwater.MainActivity;
import com.yq.yqwater.MeApplcition;
import com.yq.yqwater.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.yq.utils.Prices.m2;
import static com.yq.yqwater.R.id.bt_inputDzbq;
import static com.yq.yqwater.R.id.bt_print;
import static com.yq.yqwater.R.id.bt_scanrfid;
import static com.yq.yqwater.R.id.bt_sourcebult;
import static com.yq.yqwater.R.id.et_benyuebs;
import static com.yq.yqwater.R.id.et_dzbq;
import static com.yq.yqwater.R.id.tv_dh;
import static com.yq.yqwater.R.id.tv_showscaninfo;

/**
 * 四大标签 -- 抄表
 *
 */
public class ScanFragment extends BaseFragment implements OnDateSetListener {

    /** RFID 感应 */
    public RFIDWithLF mLF;

    /** 点我选择抄表月份 -- 点击事件：弹出年月选择窗口 -- 文本*/
    @Bind(R.id.selectTime)
    TextView selectTime;
    /** 选择的年月放在这里 -- 文本*/
    @Bind(R.id.yuefen)
    TextView yuefen;

    /** 输入10位标签号 -- 编辑框*/
    @Bind(et_dzbq)
    EditText etDzbq;
    /** 查询抄表 -- 按钮*/
    @Bind(bt_inputDzbq)
    Button btInputDzbq;
    /** 扫描抄表 -- 按钮*/
    @Bind(bt_scanrfid)
    Button btScanrfid;

    /** 存放查询后的信息 -- 文本*/
    @Bind(tv_showscaninfo)
    TextView tvShowscaninfo;

    /** 抄表标志 --  已抄；未抄  在本月表数（右边） -- 文本*/
    @Bind(R.id.bybs)
    TextView bybs;
    /** 输入本月表数 -- 编辑框*/
    @Bind(et_benyuebs)
    EditText etBenyuebs;
    /** 保存 -- 按钮*/
    @Bind(R.id.bt_save)
    Button btSave;
    /** 预交水费 -- 按钮*/
    @Bind(R.id.bt_payMoney)
    Button btPayMoney;

    /** 搜索蓝牙 -- 按钮*/
    @Bind(bt_sourcebult)
    Button btSourcebult;
    /** 打印 -- 按钮*/
    @Bind(bt_print)
    Button btPrint;
    /** 显示搜出的蓝牙设备 --  列表控件 */
    @Bind(R.id.lv_scanbul)
    MyListView lvScanbul;

    /** 收款方式: 银行抵扣 -- 文本 -- 一开始是隐藏的*/
    @Bind(R.id.tv_showBankinfo)
    TextView tvShowBankinfo;

    /** 在"本月表数" 中间还有个显示 "电话"的文本*/
    @Bind(tv_dh)
    TextView tvDh;
    /** 存放电话文本的布局 -- 线性布局*/
    @Bind(R.id.phoneLayout)
    LinearLayout phoneLayout;

    private ScanThread scanThread;
    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static BluetoothDevice mdevice = null;
    public static String mDeviceAddress = "";

    /** 蓝牙的适配器 */
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private List<BluetoothDevice> bondedDevicesList;

    /** 存放"蓝牙设备"的列表 */
    private List<BluethBean> searchDevicesList;
    private MyListAdapter mSearchAdapter;
    /** 蓝牙设置--当前连接的？ */
    private BluetoothDevice device;
    /** 蓝牙设置--对应的输出流 */
    private OutputStream outputStream;
    /** 蓝牙设置--对应的输入流 */
    private InputStream inputStream;
    /** 蓝牙设置--对应的socket */
    private BluetoothSocket socket;

    private String prinshowinfo = "";
    private String dzpd = "";
    private Cbj cbj;
    private boolean iscontinue;

    /** 本月总预交钱 */
    private String yjMoneyAll = "0";
    /** 本次预交钱 */
    private String yjMoneyCurrent = "0";

    /** 本月表数 */
    private String beny;
    public static final int ZERO = 0;
    /** 标签号 */
    private String dzbq;
    private String userName;
    private String usernumb;
    /** 上次结余 -- 只有在每次查询的时候才会拿到这个值*/
    private String mTempScjy;                // 上次结余 -- 这个要再这个月抄表内都不变的



    /****************************** 新增控件 *****************************/
    /** 用户编号 -- 户号 */
    @Bind(R.id.tv_Hmph)
    TextView mTvHmph;
    /** 户名 */
    @Bind(R.id.tv_Hm)
    TextView mTvHm;
    /** 电子标签 */
    @Bind(R.id.tv_Dzbq)
    TextView mTvDzbq;
    /** 电话号码 */
    @Bind(R.id.tv_Dh)
    TextView mTvDh;

    /** 本月表数 */
    @Bind(R.id.tv_beny)
    TextView mTvBeny;
    /** 上月表数 */
    @Bind(R.id.tv_Cmds0)
    TextView mTvCmds0;
    /** 本月水量 */
    @Bind(R.id.tv_sl)
    TextView mTvSl;
    /** 单价 */
    @Bind(R.id.tv_Ysxz)
    TextView mTvYsxz;

    /** 上月结余 */
    @Bind(R.id.tv_Scjy)
    TextView mTvScjy;
    /** 本月应收 */
    @Bind(R.id.tv_receivable)
    TextView mTvReceivable;
    /** 本月结余 */
    @Bind(R.id.tv_byjy)
    TextView mTvByjy;

    /** 总预交金 */
    @Bind(R.id.tv_yjMoneyAll)
    TextView mTvYjMoneyAll;
    /** 本次预交 */
    @Bind(R.id.tv_yjMoneyCurrent)
    TextView mTvYjMoneyCurrent;
    /** 已抄标志 -- 文本 */
    @Bind(R.id.tv_Bankinfo)
    TextView mTvBankinfo;


    /** 包裹已抄标志 -- 文本 */
    @Bind(R.id.layout_ycbz)
    LinearLayout mLayoutYcbz;
    /** 已抄标志 -- 文本 */
    @Bind(R.id.tv_ycbz)
    TextView mTvYcbz;



    public static ScanFragment newInstance() {
        return new ScanFragment();
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:                                                             // 读取 RFID 卡失败

                    Toast.makeText(getActivity(), "请重新读卡!", Toast.LENGTH_SHORT).show();
                    break;

                case 1:                                                             // 读取 RFID 卡成功
                    if (msg.obj.toString() == null) {
                        return;
                    }
                    String dzbq = HexToInt(msg.obj.toString()) + "";                // 标签号

                    int len = dzbq.length();
                    while (len < 10) {                                              // 要确保是 10位
                        dzbq = "0" + dzbq;
                        len++;
                    }
                    etDzbq.setText(dzbq);                                           // dzbq = "0000480101";



                    // 从数据库 -- 中查出这个标签号， 对应年月的 数据
                    cbj = MeApplcition.mgr.queryTheCursor(dzbq, yuefen.getText().toString().trim());
                    if (null == cbj) {
                        Toast.makeText(getActivity(), "用户信息不存在!", Toast.LENGTH_SHORT).show();

                        mTvHmph.setText("");          // 用户编号 -- 户号
                        mTvHm.setText("");              // 户名
                        mTvDzbq.setText("");          // 户名
                        mTvDh.setText("");              // 电话号码

                        mTvBeny.setText("");                     // 本月表数
                        mTvCmds0.setText("");        // 上月表数
                        mTvSl.setText("");                       // 本月水量
                        mTvYsxz.setText("");    // 单价


                        mTvScjy.setText("");      // 上月结余
                        mTvReceivable.setText("");               // 本月应收
                        mTvByjy.setText("");                     // 本月结余
                        mTvYjMoneyAll.setText("");               // 总预交金
                        mTvYjMoneyCurrent.setText("");           // 本次预交

                        mLayoutYcbz.setVisibility(View.GONE);

                        mTvBankinfo.setVisibility(View.INVISIBLE);
                    } else {
                        PlayRing.ring(getActivity());

                        bybs.setText(cbj.getIsChaoBiao() == 0 ? "未抄表" : "已抄表");
                        etBenyuebs.setText("");                                             // 情况 ：本月表数
                        yjMoneyAll = MeApplcition.mgr.selectBydzbqYjMoney(dzbq,yuefen.getText().toString().trim()) + "";       // 先获取数据库 -- 里面的金额;                                               // 预交钱

                        btSave.setEnabled(true);

                        mTempScjy = cbj.getTempScjy();                                       // 重新查询

                        mLayoutYcbz.setVisibility(View.VISIBLE);
                        /**********************  start  **************************/
                        mTvHmph.setText(cbj.getHmph());          // 用户编号 -- 户号
                        mTvHm.setText(cbj.getHm());              // 户名
                        mTvDzbq.setText(cbj.getDzbq());          // 户名
                        mTvDh.setText(cbj.getDh());              // 电话号码

                        mTvBeny.setText("");                     // 本月表数
                        mTvCmds0.setText(cbj.getCmds0());        // 上月表数
                        mTvSl.setText("");                       // 本月水量
                        mTvYsxz.setText(Prices.getPrice(cbj.getYsxz()) + "");    // 单价


                        mTvScjy.setText(cbj.getTempScjy());      // 上月结余
                        mTvReceivable.setText("");               // 本月应收
                        mTvByjy.setText("");                     // 本月结余
                        mTvYjMoneyAll.setText("");               // 总预交金
                        mTvYjMoneyCurrent.setText("");           // 本次预交

                        mTvBankinfo.setVisibility(cbj.getDk().equals("1") ? View.VISIBLE : View.INVISIBLE);      // 收款方式: 银行抵扣

                        if(cbj.getIsChaoBiao() == 1){
                            mTvYcbz.setText("已抄");
                            mTvYcbz.setTextColor(Color.RED);
                            mLayoutYcbz.setBackgroundColor(Color.YELLOW);

                            mTvBeny.setText(cbj.getCmds1());         // 本月表数
                            mTvCmds0.setText(cbj.getCmds0());        // 上月表数
                            mTvSl.setText(cbj.getSysl1());           // 本月水量
                            mTvYsxz.setText(Prices.getPrice(cbj.getYsxz()) + "");    // 单价


                            mTvScjy.setText(cbj.getTempScjy());      // 上月结余
                            mTvReceivable.setText("");               // 本月应收
                            mTvByjy.setText(cbj.getScjy());          // 本月结余
                            mTvYjMoneyAll.setText(cbj.getYjMoney() + ""); // 总预交金
                            mTvYjMoneyCurrent.setText("");           // 本次预交

                        }else{
                            mTvYcbz.setText("未抄");
                            mTvYcbz.setTextColor(Color.BLACK);
                            mLayoutYcbz.setBackgroundColor(Color.TRANSPARENT);
                        }

                        /**********************  end  **************************/

                        prinshowinfo = "用户编号:" + cbj.getHmph() + "\n"
                                + "ID卡编号:" + cbj.getDzbq() + "\n"
                                + "户\t\t名:" + cbj.getHm() + "\n"
                                + "上月表数:" + cbj.getCmds0() + "\n"
                                + "上月水量:" + cbj.getSysl0() + "\n"
                                + "上月结余:" + mTempScjy;

                        dzpd = cbj.getDzbq();                                       // 标签号
                        tvShowscaninfo.setText(prinshowinfo);                       // 数据库拿到的数据

                        //tvShowBankinfo.setVisibility(cbj.getDk().equals("1") ? View.VISIBLE : View.GONE);  // 收款方式: 银行抵扣
                        //phoneLayout.setVisibility(View.VISIBLE);
                        //tvDh.setText(TextUtils.isEmpty(cbj.getDh()) ? "" : cbj.getDh());
                    }
                    break;
            }
        }
    };




    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Log.d("m520","onCreateView被执行了...");

        View view = inflater.inflate(R.layout.scan, container, false);
        ButterKnife.bind(this, view);

        btSave.setEnabled(false);
        System.out.println("onCreateView");

        yuefen.setText(TimeUtils.getCurrentTimeyyyyMM());                            // 年月


        mLayoutYcbz.setVisibility(View.GONE);

        // 测试！
        // etDzbq.setText("0005657249");

        searchDevicesList = new ArrayList<BluethBean>();                            // 蓝牙list
        mSearchAdapter = new MyListAdapter(getActivity(), searchDevicesList);
        lvScanbul.setAdapter(mSearchAdapter);
        lvScanbul.setOnItemClickListener(new AdapterView.OnItemClickListener() {    // 蓝牙ListView点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BluethBean b = searchDevicesList.get(position);
                device = b.getBluetoothDevice();

                if (b.isAdd()) {                                    // 曾经配对过的
                    connect(device);                                // 连接获得输入输出流
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

        btPrint.setEnabled(false);
        btSourcebult.setEnabled(false);

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


        //从SharedPreferences取出上月余额 ？
        return view;
    }


    @Override
    public void onPause() {
//        Log.d("m520","onPause被执行了...");
        super.onPause();
    }

    @Override
    public void onStop() {
//        Log.d("m520","onStop被执行了...");
        super.onStop();
    }

    @Override
    public void onDestroy() {
//        Log.d("m520","onDestroy被执行了...");
        if (device != null) {
            close();
            System.out.println("清空!!!");
        }
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }


    /**************************** onCreate  打开RFID  *********************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
//        Log.d("m520","onCreate被执行了...");
        try {
            mLF = RFIDWithLF.getInstance();
            System.out.println("获取成功");
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean ret = mLF.init();
        if (ret) {
            System.out.println("打开成功");
        } else {
            System.out.println("打开失败");
        }
        super.onCreate(savedInstanceState);
    }

    /****************************** 点击事件 ************************************/
    @OnClick({bt_inputDzbq, bt_scanrfid, R.id.bybs, R.id.bt_save, R.id.bt_payMoney, bt_sourcebult, bt_print, R.id.selectTime, R.id.yuefen})
    public void onClick(View v) {
        switch (v.getId()) {
            case bt_scanrfid:                                           // 点击 "扫描抄表"
                scan();
                break;

            case bt_print:                                              // 点击 "打印"
                beny = etBenyuebs.getText().toString().trim();
                if (TextUtils.isEmpty(beny)) {
                    Toast.makeText(getActivity(), "请填写本月表数!", Toast.LENGTH_SHORT).show();
                } else {
                    if (null == cbj)
                        return;

                    bybs.setText(beny);
                    // 计算水量
                    int sl = (Integer.parseInt(beny) - Integer.parseInt(cbj.getCmds0()));
                    // 修改数量 Integer.parseInt(cbj.getDds())*2
                    //                    sl = sl <= Integer.parseInt(cbj.getDds())*2 ? 2 * Integer.parseInt(cbj.getDds()) : sl;

                    // yfs抄表月数返回是0时, 判断当月用水量是否 --- 小于底吨数,是的话就取 -- 底吨数,否者按实际的算
                    if (ZERO == cbj.getYfs())
                        sl = sl <= Integer.parseInt(cbj.getDds()) ? Integer.parseInt(cbj.getDds()) : sl;
                    else {
                        sl = sl <= Integer.parseInt(cbj.getDds()) * cbj.getYfs() ?
                                Integer.parseInt(cbj.getDds()) * cbj.getYfs() : sl;
                    }

                    double money = Prices.getMoney(sl, cbj);

                    String byjy = m2(Double.parseDouble(yjMoneyAll) + Double.parseDouble(mTempScjy) - money);

                    prinshowinfo = "\n" +
                            "用户编号:" + cbj.getHmph() + "\n" +
                            "电子标签号:" + cbj.getDzbq() + "\n" +
                            "户名:" + cbj.getHm() + "\n" +
                            "本月表数:" + beny + "\n" +
                            "上月表数:" + cbj.getCmds0() + "\n" +
                            "本月水量:" + sl + "\n" +
                            "单价:" + Prices.getPrice(cbj.getYsxz()) + "\n" +
                            "上月结余:" + mTempScjy + "\n" +
                            "本次预交:" + yjMoneyCurrent + "\n" +
                            "本月总预交:" + yjMoneyAll + "\n" +
                            "本月应收:" + m2(money) + "\n" +
                            "本月结余:" + byjy + "\n" +
                            "收费人:" + getUserName() + "\n" +
                            "收费时间:" + TimeUtils.getCurrentTime() + "\n\n\n";
                    try {
                        outputStream.write(prinshowinfo.getBytes("GBK"));
                        outputStream.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                //TODO
                // close(); //关闭蓝牙连接socket
                break;


            case bt_sourcebult:                                                             //  "搜索蓝牙" 按钮 -- 不可交互
                Toast.makeText(getActivity(), "正在搜索,请稍等!", Toast.LENGTH_LONG).show();
                checkBluet();
                break;




            case bt_inputDzbq:                                                              // "查询抄表" 按钮
                //etBenyuebs.setText("");                         // 本月读数
                dzbq = etDzbq.getText().toString().trim();      // 标签号

                if (TextUtils.isEmpty(dzbq)) {
                    Toast.makeText(getActivity(), "请填写标签号!", Toast.LENGTH_SHORT).show();
                    mLayoutYcbz.setVisibility(View.GONE);
                    return;
                }

                cbj = MeApplcition.mgr.queryTheCursor(dzbq, yuefen.getText().toString().trim());

                if (null == cbj) {
                    Toast.makeText(getActivity(), "用户信息不存在!", Toast.LENGTH_SHORT).show();

                    mTvHmph.setText("");          // 用户编号 -- 户号
                    mTvHm.setText("");              // 户名
                    mTvDzbq.setText("");          // 户名
                    mTvDh.setText("");              // 电话号码

                    mTvBeny.setText("");                     // 本月表数
                    mTvCmds0.setText("");        // 上月表数
                    mTvSl.setText("");                       // 本月水量
                    mTvYsxz.setText("");    // 单价


                    mTvScjy.setText("");      // 上月结余
                    mTvReceivable.setText("");               // 本月应收
                    mTvByjy.setText("");                     // 本月结余
                    mTvYjMoneyAll.setText("");               // 总预交金
                    mTvYjMoneyCurrent.setText("");           // 本次预交

                    mLayoutYcbz.setVisibility(View.GONE);
                    mTvBankinfo.setVisibility(View.INVISIBLE);

                } else {
                    bybs.setText(cbj.getIsChaoBiao() == 0 ? "未抄表" : "已抄表");
                    yjMoneyAll = MeApplcition.mgr.selectBydzbqYjMoney(dzbq,yuefen.getText().toString().trim()) + "";
                    btSave.setEnabled(true);
                    mTempScjy = cbj.getTempScjy();                                               // 上次结余

                    mLayoutYcbz.setVisibility(View.VISIBLE);
                    /**********************  start  **************************/
                    mTvHmph.setText(cbj.getHmph());          // 用户编号 -- 户号
                    mTvHm.setText(cbj.getHm());              // 户名
                    mTvDzbq.setText(cbj.getDzbq());          // 户名
                    mTvDh.setText(cbj.getDh());              // 电话号码

                    mTvBeny.setText("");                     // 本月表数
                    mTvCmds0.setText(cbj.getCmds0());        // 上月表数
                    mTvSl.setText("");                       // 本月水量
                    mTvYsxz.setText(Prices.getPrice(cbj.getYsxz()) + "");    // 单价


                    mTvScjy.setText(cbj.getTempScjy());      // 上月结余
                    mTvReceivable.setText("");               // 本月应收
                    mTvByjy.setText("");                     // 本月结余
                    mTvYjMoneyAll.setText("");               // 总预交金
                    mTvYjMoneyCurrent.setText("");           // 本次预交


                    mTvBankinfo.setVisibility(cbj.getDk().equals("1") ? View.VISIBLE : View.INVISIBLE);      // 收款方式: 银行抵扣

                    if(cbj.getIsChaoBiao() == 1){
                        mTvYcbz.setText("已抄");
                        mTvYcbz.setTextColor(Color.RED);
                        mLayoutYcbz.setBackgroundColor(Color.YELLOW);

                        mTvBeny.setText(cbj.getCmds1());         // 本月表数
                        mTvCmds0.setText(cbj.getCmds0());        // 上月表数
                        mTvSl.setText(cbj.getSysl1());           // 本月水量
                        mTvYsxz.setText(Prices.getPrice(cbj.getYsxz()) + "");    // 单价


                        mTvScjy.setText(cbj.getTempScjy());      // 上月结余
                        mTvReceivable.setText("");               // 本月应收
                        mTvByjy.setText(cbj.getScjy());          // 本月结余
                        mTvYjMoneyAll.setText(cbj.getYjMoney() + ""); // 总预交金
                        mTvYjMoneyCurrent.setText("");           // 本次预交

                    }else{
                        mTvYcbz.setText("未抄");
                        mTvYcbz.setTextColor(Color.BLACK);
                        mLayoutYcbz.setBackgroundColor(Color.TRANSPARENT);
                    }
                    /**********************  end  **************************/


                    prinshowinfo = "用户编号:" + cbj.getHmph() + "\n" +
                            "ID卡编号:" + cbj.getDzbq() + "\n" +
                            "户\t\t名:" + cbj.getHm() + "\n" +
                            "上月表数:" + cbj.getCmds0() + "\n" +
                            "上月水量:" + cbj.getSysl0() + "\n" +
                            "上月结余:" + mTempScjy;
                    dzpd = cbj.getDzbq();

                    tvShowscaninfo.setText(prinshowinfo);

                    //tvShowBankinfo.setVisibility(cbj.getDk().equals("1") ? View.VISIBLE : View.GONE);  // 收款方式: 银行抵扣
                    //phoneLayout.setVisibility(View.VISIBLE);                            // 显示电话 布局
                    //tvDh.setText(TextUtils.isEmpty(cbj.getDh()) ? "" : cbj.getDh());    // 布局中的文本添加电话
                    // Log.d("m520", "保存前的值yfs=" + cbj.getYfs()+"----dzbq="+dzbq);
                }
                break;


            //保存
            case R.id.bt_save:
                // 0000501048
                // Log.d("m520",cbj.getDds());

                beny = etBenyuebs.getText().toString().trim();              // 本月表数
                if (TextUtils.isEmpty(beny)) {
                    Toast.makeText(getActivity(), "请填写本月表数!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //yjMoney = "0";
                if (cbj.getIsChaoBiao() != 0) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("已经抄表，是否需要重抄")
                            .setCancelText("    否    ")
                            .setConfirmText("    是    ")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    iscontinue = true;
                                    beny = etBenyuebs.getText().toString().trim();
                                    setSaveText();
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    iscontinue = false;
                                }
                            }).show();
                    if (!iscontinue)
                        return;
                }

                setSaveText();

                break;
            //
            case R.id.bt_payMoney:                                                  // 预交水费

                dzbq = etDzbq.getText().toString().trim();

                if(TextUtils.isEmpty(dzbq)){
                    Toast.makeText(getActivity(), "请填写电子标签!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 根据用户标签号查询预交的钱  -- 就是上一次的 -- yjMoney
                int payje = MeApplcition.mgr.selectBydzbqYjMoney(dzbq, yuefen.getText().toString().trim());         // 先获取数据库 -- 里面的金额

                if(payje > 0) {
                    Log.d("m520","进来了2");
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示")
                            .setContentText("本月已预交了" + payje + "元，是否继续预交")
                            .setCancelText("  否  ")
                            .setConfirmText("  是  ")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();

                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    if (TextUtils.isEmpty(etBenyuebs.getText().toString().trim())) {
                                        Toast.makeText(getActivity(), "请填写本月表数!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (null == cbj)
                                        return;
                                    if (cbj.getDk().equals("1")) {          // 收款方式:银行扣款
                                        showBankError();
                                    } else {
                                        intputMoney();
                                    }
                                }
                            }).show();
                }else{
                    if (TextUtils.isEmpty(etBenyuebs.getText().toString().trim())) {
                        Toast.makeText(getActivity(), "请填写本月表数!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (null == cbj)
                        return;
                    if (cbj.getDk().equals("1")) {          // 收款方式:银行扣款
                        showBankError();
                    } else {
                        intputMoney();
                    }
                }

                break;


            case R.id.selectTime:                       // 都是 ： 点我选择抄表月份 弹出年月窗口
                getTime();
                break;

            case R.id.yuefen:                           // 都是 ： 点我选择抄表月份 弹出年月窗口
                getTime();
                break;

        }
    }



    /**
     * 保存
     */
    private void setSaveText() {
        if (cbj.getIsChaoBiao() == 0) {
            if (Integer.parseInt(cbj.getCmds0()) > Integer.parseInt(beny)) {
                showError("本月表码小于上月表码，请检查!");
                return;
            }
        }

        if (cbj.getCmds0().equals(beny) && cbj.getBtbh().equals("0")) {
            showError("水量为0且不在报停状态");
        }

        if (cbj.getCmds0().equals(beny) && cbj.getBtbh().equals("1")) {
            showError("水量为0且在报停状态!");
        }

        int sl = (Integer.parseInt(beny) - Integer.parseInt(cbj.getCmds0()));
        if (sl > 10) {
            if ((sl / Double.parseDouble(cbj.getSysl0()) >= 1.3 ||
                    sl / Double.parseDouble(cbj.getSysl0()) <= 0.7)) {
                PlayRing.ring(getActivity());
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("温馨提示")
                        .setContentText("水量异常,上月水量:" + cbj.getSysl0() + "本月水量:" + sl)
                        .setConfirmText("  确认  ")
                        .setCancelText("  取消  ")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                showText();
                            }
                        }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                }).show();
            } else
                showText();
        } else
            showText();
    }


    /**
     * 点击"保存"按钮,检查输入的数字"正常"<p>
     *
     * 就修改界面数据
     */
    private void showText() {
        int sl = (Integer.parseInt(beny) - Integer.parseInt(cbj.getCmds0()));
        //修改数量 Integer.parseInt(cbj.getDds())*2
        //        sl = sl <= Integer.parseInt(cbj.getDds()) * 2 ? 2 * Integer.parseInt(cbj.getDds()) : sl;

        //yfs抄表月数返回是0时,判断当月用水量是否小于底吨数,是的话就取底吨数,否者按实际的算
        if (ZERO == cbj.getYfs()) {
            // Log.d("m520", "等于零的时候yfs=" + cbj.getYfs()+"----dzbq="+dzbq);
            sl = sl <= Integer.parseInt(cbj.getDds()) ? Integer.parseInt(cbj.getDds()) : sl;
        } else {
//            int value = Integer.parseInt(cbj.getDds());
//            int key = cbj.getYfs();
//            sl = sl <= value * key ? value * key : sl;

            //sl = sl <= Integer.parseInt(cbj.getDds()) * Integer.parseInt(cbj.getYfs()) ? Integer.parseInt(cbj.getDds()) * Integer.parseInt(cbj.getYfs()): sl;
//            Log.d("m520111", "不等于零的时候yfs=" + cbj.getYfs()+"----dzbq="+dzbq);
            sl = sl <= Integer.parseInt(cbj.getDds()) * cbj.getYfs() ? Integer.parseInt(cbj.getDds()) * cbj.getYfs() : sl;
        }

        String money = m2(Prices.getMoney(sl, cbj));
        Double yy = Double.parseDouble(yjMoneyAll) + Double.parseDouble(mTempScjy) - Prices.getMoney(sl, cbj);


        /**********************  start  **************************/
        mTvHmph.setText(cbj.getHmph());          // 用户编号 -- 户号
        mTvHm.setText(cbj.getHm());              // 户名
        mTvDzbq.setText(cbj.getDzbq());          // 户名
        mTvDh.setText(cbj.getDh());              // 电话号码

        mTvBeny.setText(beny);                   // 本月表数
        mTvCmds0.setText(cbj.getCmds0());        // 上月表数
        mTvSl.setText(sl + "");                  // 本月水量
        mTvYsxz.setText(Prices.getPrice(cbj.getYsxz()) + "");    // 单价


        mTvScjy.setText(cbj.getTempScjy());      // 上月结余
        mTvReceivable.setText(money);            // 本月应收
        mTvByjy.setText(Prices.m2(yy));          // 本月结余
        if(yy < 0)
            mTvByjy.setTextColor(Color.RED);
        else
            mTvByjy.setTextColor(Color.BLACK);
        mTvYjMoneyAll.setText(yjMoneyAll);       // 总预交金
        mTvYjMoneyCurrent.setText("");           // 本次预交

        if(cbj.getIsChaoBiao() == 1){
            mTvYcbz.setText("已抄");
            mTvYcbz.setTextColor(Color.RED);
            mLayoutYcbz.setBackgroundColor(Color.YELLOW);

        }else{
            mTvYcbz.setText("未抄");
            mTvYcbz.setTextColor(Color.BLACK);
            mLayoutYcbz.setBackgroundColor(Color.TRANSPARENT);
        }
        /**********************  end  **************************/

        prinshowinfo = "用户编号:" + cbj.getHmph() + "\n" +
                "户\t\t名:" + cbj.getHm() + "\n" +
                "本月表数:" + beny + "\n" +
                "上月表数:" + cbj.getCmds0() + "\n" +
                "本月水量:" + sl + "\n" +
                "单\t\t价:" + Prices.getPrice(cbj.getYsxz()) + "\n" +
                "上月结余:" + mTempScjy + "\n" +
                "本月总预交:" + yjMoneyAll + "\n" +
                "本月应收:" + money + "\n" +
                "本月结余:" + Prices.m2(yy);

        bybs.setText(beny);

        // 将部分字体的颜色改变
        SpannableStringBuilder builder = new SpannableStringBuilder(prinshowinfo);
        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        builder.setSpan(redSpan, prinshowinfo.length()-Prices.m2(yy).length(), prinshowinfo.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if(yy < 0)
            tvShowscaninfo.setText(builder);
        else
            tvShowscaninfo.setText(prinshowinfo);

        tvShowBankinfo.setVisibility(cbj.getDk().equals("1") ? View.VISIBLE : View.GONE);
        //
        MeApplcition.mgr.updatebney(cbj.getDzbq(), beny, Prices.m2(yy), Double.parseDouble(yjMoneyAll), String.valueOf(sl), cbj.getCbye());
        tvDh.setText(TextUtils.isEmpty(cbj.getDh()) ? "" : cbj.getDh());
    }


    private void showError(String msg) {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("温馨提示")
                .setContentText(msg)
                .setConfirmText("确认")
                .show();
    }


    /**
     * 弹出窗口显示 <p>
     * 付款方式是 -- 银行卡扣款        <br>
     * 所以提示 -- 是否需要现场缴费     <br>
     */
    private void showBankError() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("温馨提示")
                .setContentText("该户为银行卡扣款，是否需要现场缴费")
                .setConfirmText("    是    ")
                .setCancelText("    否    ")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        intputMoney();
                    }
                })
                .show();
    }

    /**
     * 弹出窗口！ -- 现场充值
     */
    private void intputMoney() {
        dzbq = etDzbq.getText().toString().trim();
        final EditText editText = new EditText(getActivity());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        new AlertDialog.Builder(getActivity()).setTitle("请输入金额")
                .setView(editText)
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {       // 点击确认
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yjMoneyCurrent = editText.getText().toString().trim();                 // 从"金额"编辑框得到"金额"

                        if (TextUtils.isEmpty(yjMoneyCurrent)) {
                            Toast.makeText(getActivity(), "您还没有输入金额", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String beny = etBenyuebs.getText().toString().trim();           // 年月
                        int sl = (Integer.parseInt(beny) - Integer.parseInt(cbj.getCmds0()));       // 水量

                        //修改数量 Integer.parseInt(cbj.getDds())*2
                        //        sl = sl <= Integer.parseInt(cbj.getDds()) * 2 ? 2 * Integer.parseInt(cbj.getDds()) : sl;

                        //yfs抄表月数返回是0时,判断当月用水量是否小于底吨数,是的话就取底吨数,否者按实际的算
                        if (ZERO == cbj.getYfs()) {
                            // Log.d("m520", "等于零的时候yfs=" + cbj.getYfs()+"----dzbq="+dzbq);
                            sl = sl <= Integer.parseInt(cbj.getDds()) ? Integer.parseInt(cbj.getDds()) : sl;
                        } else {
//            int value = Integer.parseInt(cbj.getDds());
//            int key = cbj.getYfs();
//            sl = sl <= value * key ? value * key : sl;

                            //sl = sl <= Integer.parseInt(cbj.getDds()) * Integer.parseInt(cbj.getYfs()) ? Integer.parseInt(cbj.getDds()) * Integer.parseInt(cbj.getYfs()): sl;
//            Log.d("m520111", "不等于零的时候yfs=" + cbj.getYfs()+"----dzbq="+dzbq);
                            sl = sl <= Integer.parseInt(cbj.getDds()) * cbj.getYfs() ? Integer.parseInt(cbj.getDds()) * cbj.getYfs() : sl;
                        }

                        double money = Prices.getMoney(sl, cbj);                                    // 实际收水费

                        // 根据用户标签号查询预交的钱  -- 就是上一次的 -- yjMoney
                        int payje = MeApplcition.mgr.selectBydzbqYjMoney(dzbq, yuefen.getText().toString().trim());         // 先获取数据库 -- 里面的金额

                        yjMoneyAll = String.valueOf(payje + Integer.parseInt(yjMoneyCurrent));      // 上次预交 + 本次预交 （一个月可以重复提交）

                        double byjy = Double.parseDouble(yjMoneyAll) + Double.parseDouble(mTempScjy) - money;
                        String str_byjy = Prices.m2(byjy);


                        /**********************  start  **************************/
                        mTvHmph.setText(cbj.getHmph());          // 用户编号 -- 户号
                        mTvHm.setText(cbj.getHm());              // 户名
                        mTvDzbq.setText(cbj.getDzbq());          // 户名
                        mTvDh.setText(cbj.getDh());              // 电话号码

                        mTvBeny.setText(beny);                   // 本月表数
                        mTvCmds0.setText(cbj.getCmds0());        // 上月表数
                        mTvSl.setText(sl + "");                  // 本月水量
                        mTvYsxz.setText(Prices.getPrice(cbj.getYsxz()) + "");    // 单价


                        mTvScjy.setText(cbj.getTempScjy());      // 上月结余
                        mTvReceivable.setText(m2(money));        // 本月应收
                        mTvByjy.setText(str_byjy);               // 本月结余
                        if(byjy < 0)
                            mTvByjy.setTextColor(Color.RED);
                        else
                            mTvByjy.setTextColor(Color.BLACK);
                        mTvYjMoneyAll.setText(yjMoneyAll);       // 总预交金
                        mTvYjMoneyCurrent.setText(yjMoneyCurrent);           // 本次预交

                        if(cbj.getIsChaoBiao() == 1){
                            mTvYcbz.setText("已抄");
                            mTvYcbz.setTextColor(Color.RED);
                            mLayoutYcbz.setBackgroundColor(Color.YELLOW);

                        }else{
                            mTvYcbz.setText("未抄");
                            mTvYcbz.setTextColor(Color.BLACK);
                            mLayoutYcbz.setBackgroundColor(Color.TRANSPARENT);
                        }
                        /**********************  end  **************************/

                        prinshowinfo =
                                "用户编号:" + cbj.getHmph() + "\n" +
                                        "户\t\t名:" + cbj.getHm() + "\n" +
                                        "本月表数:" + beny + "\n" +
                                        "上月表数:" + cbj.getCmds0() + "\n" +
                                        "水\t\t量:" + sl + "\n" +
                                        "单\t\t价:" + Prices.getPrice(cbj.getYsxz()) + "\n" +
                                        "上月结余:" + mTempScjy + "\n" +
                                        "本月总预交:" + yjMoneyAll + "\n" +
                                        "本次预交:" + yjMoneyCurrent + "\n" +
                                        "本月应收:" + m2(money) + "\n" +
                                        "本月结余:" + str_byjy;

                        // "本月结余:" + byjy;

                        usernumb = ((MainActivity) getActivity()).usernumb;
                        boolean b = MeApplcition.mgr.addYjMoney(cbj.getHmph(), yjMoneyCurrent, usernumb, TimeUtils.getCurrentTimeRq());

                        if(b) {
                            // 将部分字体的颜色改变
                            SpannableStringBuilder builder = new SpannableStringBuilder(prinshowinfo);
                            // ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                            ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
                            builder.setSpan(redSpan, prinshowinfo.length() - str_byjy.length(), prinshowinfo.length(),
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            if (byjy < 0)
                                tvShowscaninfo.setText(builder);
                            else
                                tvShowscaninfo.setText(prinshowinfo);

                            btSourcebult.setEnabled(true);

                            Cbj cbj123 = MeApplcition.mgr.queryTheCursor(dzbq, yuefen.getText().toString().trim());
                            Log.d("m520", cbj123.toString());
                            Log.d("m520", "Double.parseDouble(yjMoneyAll)" + Double.parseDouble(yjMoneyAll));
                            MeApplcition.mgr.updatebney(dzpd, beny, str_byjy, Double.parseDouble(yjMoneyAll), String.valueOf(sl), cbj.getCbye());



                            // 将要提交的金额
                            // taskPresenter.upPayData(cbj.getHmph(), yjMoney, usernumb);   // 这个是错误的 -- 添加了上次的！
                            // taskPresenter.upPayData(cbj.getHmph(), yjMoneyCurrent, usernumb);

                            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("成功缴费:" + yjMoneyCurrent + "元")
                                    .setConfirmText("确认")
                                    .show();
                        }else {
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("预交金额失败")
                                    .setConfirmText("确认")
                                    .show();
                        }

                    }
                }).show();
    }


    /**
     * 点击"扫描抄表"  -- 执行的代码
     */
    public void scan() {
        boolean bContinuous = false;
        int iBetween = 0;
        if (scanThread == null || !bContinuous || scanThread.isStop()) {
            scanThread = new ScanThread(bContinuous, iBetween, 0);
            scanThread.start();
        } else {
            scanThread.cancel();
        }
    }

    /***************************  弹出窗口 选择时间 -- start **************************************/
    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        Date date = new Date(millseconds);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");//精确到分钟
        String str = format.format(date);
        yuefen.setText(str);
    }



    private void getTime() {
        TimePickerDialog mDialogYearMonth = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH)
                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                .setCallBack(this)
                .build();
        mDialogYearMonth.show(getActivity().getSupportFragmentManager(), "YEAR_MONTH");
    }
    /**************************  弹出窗口 选择时间 -- end ***************************************/



    private String getUserName() {
        String name = PreferenceUtils.getString(getActivity(), "userName", "");
        if (!TextUtils.isEmpty(name))
            return name;

        userName = ((MainActivity) getActivity()).userName;
        return userName;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

    @OnClick(tv_dh)
    public void onClick() {
        //意图：想干什么事
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        //url:统一资源定位符
        //uri:统一资源标示符（更广）
        intent.setData(Uri.parse("tel:" + tvDh.getText().toString().trim()));
        //开启系统拨号器
        startActivity(intent);
    }


    /**
     * 在 mainActivity 中 调用了
     *
     * 进行"扫描抄表"
     *
     * @param keyCode
     * @param event
     */
    public void onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_F9) {
            scan();
        }
    }


    /**
     * ScanThread extends Thread:
     *
     * 点击"扫描抄表"执行的线程
     */
    private class ScanThread extends Thread {

        /** 是否自动 */
        boolean mIsAuto;
        /** 运行时间 */
        int mTime;
        /** ??? */
        int mTagType;

        /** 停止标志 -- false：运行  true：停止运行 */
        boolean threadStop = true;

        /**
         * 线程构造函数
         *
         * @param isAuto        是否自动
         * @param time          运行时间
         * @param tagType       ？？？
         */
        public ScanThread(boolean isAuto, int time, int tagType) {
            mIsAuto = isAuto;
            mTime = time;
            mTagType = tagType;
            threadStop = false;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();

            do {                                                // do{xxx}while(xxx);
                Message msg = Message.obtain();
                Object result = null;

                switch (mTagType) {
                    case 0:
                        result = mLF.readDataWithIDCard(0);     // 将ID卡,"结果"读取出来
                        break;
                    default:
                        break;
                }


                if (result == null || result.toString().equals("-1")) {         //  读取出错
                    msg.what = 0;
                    msg.arg2 = mTagType;

                } else {
                    msg.what = 1;
                    msg.arg2 = mTagType;
                    msg.obj = result;
                    System.out.println("result === " + result);
                    System.out.println("mTagType === " + mTagType);
                }
                mHandler.sendMessage(msg);


                if (mIsAuto) {
                    try {
                        sleep(mTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } while (mIsAuto && !threadStop);

            threadStop = true;
        }

        /**
         * 查询线程是否还在执行
         * @return
         */
        public boolean isStop() {
            return threadStop;
        }

        /**
         * 停止线程<br>
         * 将停止标志设为 true
         */
        public void cancel() {
            threadStop = true;
        }

    }


    private void checkBluet() {
        try {

            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                getActivity().startActivityForResult(enableBtIntent, 1);
            } else {
                bluetoothAdapter.startDiscovery();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                                connect(device);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            }
        }
    };



    /**
     * 蓝牙设备的连接（客户端）
     *
     * @param device 蓝牙设备
     */
    private void connect(BluetoothDevice device) {
        // 固定的UUID
        final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
        UUID uuid = UUID.fromString(SPP_UUID);
        try {
            socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();

            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();

            btPrint.setEnabled(true);                   // 蓝牙连接后，才可以点击 "打印 -- 按钮"
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method m = device.getClass().getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void close() {
        if (null == socket)
            return;
        if (socket.isConnected()) {
            try {
                outputStream.close();
                inputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //16进制转10进制
    public static int HexToInt(String strHex) {
        int nResult = 0;
        if (!IsHex(strHex))
            return nResult;
        String str = strHex.toUpperCase();
        if (str.length() > 2) {
            if (str.charAt(0) == '0' && str.charAt(1) == 'X') {
                str = str.substring(2);
            }
        }
        int nLen = str.length();
        for (int i = 0; i < nLen; ++i) {
            char ch = str.charAt(nLen - i - 1);
            try {
                nResult += (GetHex(ch) * GetPower(16, i));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return nResult;
    }

    //计算16进制对应的数值
    public static int GetHex(char ch) throws Exception {
        if (ch >= '0' && ch <= '9')
            return (int) (ch - '0');
        if (ch >= 'a' && ch <= 'f')
            return (int) (ch - 'a' + 10);
        if (ch >= 'A' && ch <= 'F')
            return (int) (ch - 'A' + 10);
        throw new Exception("error param");
    }

    //计算幂
    public static int GetPower(int nValue, int nCount) throws Exception {
        if (nCount < 0)
            throw new Exception("nCount can't small than 1!");
        if (nCount == 0)
            return 1;
        int nSum = 1;
        for (int i = 0; i < nCount; ++i) {
            nSum = nSum * nValue;
        }
        return nSum;
    }

    //判断是否是16进制数
    public static boolean IsHex(String strHex) {
        int i = 0;
        if (strHex.length() > 2) {
            if (strHex.charAt(0) == '0' && (strHex.charAt(1) == 'X' || strHex.charAt(1) == 'x')) {
                i = 2;
            }
        }
        for (; i < strHex.length(); ++i) {
            char ch = strHex.charAt(i);
            if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f'))
                continue;
            return false;
        }
        return true;
    }


}
