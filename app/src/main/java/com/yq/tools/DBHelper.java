package com.yq.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.smtlibrary.utils.LogUtils;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sddata.db";
    private static final int DATABASE_VERSION = 5;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * 这个方法
     * 1、在第一次打开数据库的时候才会走
     * 2、在清除数据之后再次运行-->打开数据库，这个方法会走
     * 3、没有清除数据，不会走这个方法
     * 4、数据库升级的时候这个方法不会走
     */
    public void onCreate(SQLiteDatabase db) {

        //组
        db.execSQL("CREATE TABLE IF NOT EXISTS gpinfo" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "GROUPID VARCHAR(16), " +
                "GROUPNUM INTEGER, " +
                "GROUPNAME VARCHAR(32))");


        db.execSQL("CREATE TABLE IF NOT EXISTS sbinfo" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "dzbq VARCHAR(16), " +
                "hmph VARCHAR(8), " +
                "cmds0 VARCHAR(32), " +
                "hm VARCHAR(32)," +
                "dz VARCHAR(32)," +
                "dh VARCHAR(32)," +
                "sysl0 VARCHAR(32)," +
                "sysl1 VARCHAR(32)," +
                "rq VARCHAR(32)," +
                "cbye VARCHAR(16)," +
                "qsf VARCHAR(16)," +
                "ysxz VARCHAR(16)," +
                "scjy VARCHAR(16)," +
                "dds VARCHAR(6)," +
                "btbh VARCHAR(32)," +
                "dk VARCHAR(6)," +
                "sfy VARCHAR(16)," +
                "fbh VARCHAR(8)," +
                "Jd VARCHAR(8)," +
                "Wd VARCHAR(8)," +
                "yjMoney INTEGER," +                    //预交金额
                "isChaoBiao INTEGER default 0," +       //是否抄表
                "isUpload INTEGER default 0," +
                "cmds1 VARCHAR(32)," +
                "yfs INTEGER,"+
                "tempScjy VARCHAR(32)"+                 // 确保上月结余不变 -- 因为抄表后会改变 scjy 才上传
                ")");


        // 预交金额
        db.execSQL("CREATE TABLE IF NOT EXISTS YjMoney" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "isUpload INTEGER default 0," +
                "hmph VARCHAR(8), " +                       // 用户编号
                "yjMoney INTEGER," +                        // 预交金额
                "czybm VARCHAR(8)," +
                "time VARCHAR(16))");                       // 登录账号 -- 抄表员编号


    }


    /**
     * 1、第一次创建数据库的时候，这个方法不会走
     * 2、清除数据后再次运行(相当于第一次创建)这个方法不会走
     * 3、数据库已经存在，而且版本升高的时候，这个方法才会调用
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // 使用for实现跨版本升级数据库
        for (int i = oldVersion; i < newVersion; i++) {
            String sql,sql1,sql2,sql3,sql4,sql5;
            switch (i) {
                case 1:
                    sql = " ALTER TABLE sbinfo ADD COLUMN fbh VARCHAR(8)";
                    sql1 = " ALTER TABLE sbinfo ADD COLUMN Jd VARCHAR(8)";
                    sql2 = " ALTER TABLE sbinfo ADD COLUMN Wd VARCHAR(8)";
                    db.execSQL(sql);
                    db.execSQL(sql1);
                    db.execSQL(sql2);
                    LogUtils.sysout("==========升级数据库", "");
                    break;
                case 2:
                    sql = " ALTER TABLE sbinfo ADD COLUMN dh VARCHAR(32)";
                    db.execSQL(sql);
                    LogUtils.sysout("==========升级数据库", "");
                    break;
                case 3:
                    sql = " ALTER TABLE sbinfo ADD COLUMN tempScjy VARCHAR(32)";
                    db.execSQL(sql);
                    LogUtils.sysout("==========升级数据库", "");
                    break;
                case 4:
                    sql = "CREATE TABLE IF NOT EXISTS YjMoney" +
                            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "isUpload INTEGER default 0," +
                            "hmph VARCHAR(8), " +                       // 用户编号
                            "yjMoney INTEGER," +                        // 预交金额
                            "czybm VARCHAR(8)," +                       // 登录账号 -- 抄表员编号
                            "time VARCHAR(16))";                        // 上传时间
                    db.execSQL(sql);
                    LogUtils.sysout("==========升级数据库", "");
                    break;
                default:
                    break;
            }
        }

    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
         * 执行数据库的降级操作
         * 1、只有新版本比旧版本低的时候才会执行
         * 2、如果不执行降级操作，会抛出异常
         */

        super.onDowngrade(db, oldVersion, newVersion);
    }
}