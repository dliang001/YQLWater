package com.yq.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yq.model.CbData;
import com.yq.model.Cbj;
import com.yq.model.TjNotChaoBiao;
import com.yq.model.UpCbjBean;
import com.yq.model.YjMoneyBean;
import com.yq.utils.ComparatorByHmph;
import com.yq.utils.TimeUtils;
import com.yq.utils.ToFormatUtil;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mac on 16/11/30.
 */

public class DBManager {

    private DBHelper helper;
    private SQLiteDatabase db;


    public DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }


    public void add(CbData cbData) {
        try {
            if (null != db) {
                db.close();
                db = helper.getWritableDatabase();
            }
            db.beginTransaction();  //开始事务
            if (db.isOpen()) {
                for (CbData.CbjDetail b : cbData.getDATA()) {
                    ContentValues grValue = new ContentValues();
                    grValue.put("GROUPID", b.getGROUPID());
                    grValue.put("GROUPNUM", b.getGROUPNUM());
                    grValue.put("GROUPNAME", b.getGROUPNAME());
                    db.insertOrThrow("gpinfo", null, grValue);
                    for (Cbj cbj : b.getGPOUPDATA()) {
                        ContentValues values = new ContentValues();
                        values.put("dzbq", cbj.getDzbq());
                        values.put("hmph", cbj.getHmph());
                        values.put("cmds0", cbj.getCmds0());
                        values.put("cmds1", cbj.getCmds1());
                        values.put("hm", cbj.getHm());
                        values.put("rq", cbj.getRq());
                        values.put("dz", cbj.getDz());
                        values.put("qsf", cbj.getQsf());
                        values.put("sysl0", cbj.getSysl0());
                        values.put("sysl1", cbj.getSysl1());
                        values.put("dh", cbj.getDh());

                        values.put("ysxz", cbj.getYsxz());
                        values.put("scjy", cbj.getScjy());
                        values.put("dds", cbj.getDds());
                        values.put("btbh", cbj.getBtbh());
                        values.put("dk", cbj.getDk());
                        values.put("sfy", cbj.getSfy());
                        values.put("cbye", cbj.getCbye());
                        values.put("Jd", cbj.getJd());
                        values.put("Wd", cbj.getWd());
                        values.put("fbh", cbj.getFbh());
                        values.put("yfs", cbj.getYfs());

                        values.put("yjMoney", 0);

                        values.put("tempScjy", cbj.getScjy());  // 201770419
                        db.insertOrThrow("sbinfo", null, values);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    /**
     * 查询抄表
     * @param dzbp
     * @param cbye
     * @return
     */
    public Cbj queryTheCursor(String dzbp, String cbye) {
        Cbj cbj = new Cbj();
        Cursor c = db.rawQuery("SELECT * FROM sbinfo where dzbq = ? and cbye = ?", new String[]{dzbp, cbye});
        while (c.moveToNext()) {
            getData(cbj, c);
            return cbj;
        }
        c.close();
        return null;
    }

    /**
     * 查询用户标签号
     * @param dzbp
     * @return
     */
    public Cbj queryTheCursor(String dzbp) {
        Cbj cbj = new Cbj();
        Cursor c = db.rawQuery("SELECT * FROM sbinfo where dzbq = ?", new String[]{dzbp});
        while (c.moveToNext()) {
            getData(cbj, c);
            return cbj;
        }
        c.close();
        return null;
    }


    /**
     * 根据用户编号查询信息
     * @param hmph
     * @return
     */
    public Cbj queryByHmphCursor(String hmph) {
        Cbj cbj = new Cbj();
        Cursor c = db.rawQuery("SELECT * FROM sbinfo where hmph = ?", new String[]{hmph});
        while (c.moveToNext()) {
            getData(cbj, c);
            return cbj;
        }
        c.close();
        return null;
    }

    /**
     * 根据用户编号查询信息 -- 条件月份
     * @param hmph
     * @return
     */
    public boolean queryByHmphAndCbye(String hmph, String cbye) {
        Cursor c = db.rawQuery("SELECT * FROM sbinfo where hmph = ? and cbye = ?",
                new String[]{hmph, cbye});
        if (c.moveToFirst()) {
            return true;
        }
        c.close();
        return false;
    }

    private void getData(Cbj cbj, Cursor c) {
        cbj.setDzbq(c.getString(c.getColumnIndex("dzbq")));
        cbj.setDz(c.getString(c.getColumnIndex("dz")));
        cbj.setCmds0(c.getString(c.getColumnIndex("cmds0")));
        cbj.setHm(c.getString(c.getColumnIndex("hm")));
        cbj.setCmds1(c.getString(c.getColumnIndex("cmds1")));
        cbj.setRq(c.getString(c.getColumnIndex("rq")));
        cbj.setQsf(c.getString(c.getColumnIndex("qsf")));
        cbj.setHmph(c.getString(c.getColumnIndex("hmph")));
        cbj.setSysl0(c.getString(c.getColumnIndex("sysl0")));
        cbj.setSysl1(c.getString(c.getColumnIndex("sysl1")));
        cbj.setYsxz(c.getString(c.getColumnIndex("ysxz")));
        cbj.setCbye(c.getString(c.getColumnIndex("cbye")));
        cbj.setDh(c.getString(c.getColumnIndex("dh")));

        cbj.setScjy(c.getString(c.getColumnIndex("scjy")));
        cbj.setDds(c.getString(c.getColumnIndex("dds")));
        cbj.setBtbh(c.getString(c.getColumnIndex("btbh")));
        cbj.setDk(c.getString(c.getColumnIndex("dk")));
        cbj.setSfy(c.getString(c.getColumnIndex("sfy")));
        cbj.setIsChaoBiao(c.getInt(c.getColumnIndex("isChaoBiao")));
        cbj.setIsUpload(c.getInt(c.getColumnIndex("isUpload")));
        cbj.setYjMoney(c.getInt(c.getColumnIndex("yjMoney")));
        cbj.setYfs(c.getInt(c.getColumnIndex("yfs")));

        cbj.setTempScjy(c.getString(c.getColumnIndex("tempScjy")));
    }


    /**
     * 获取一条数据 -- 已抄但是未上传的数据
     *
     * @return
     */
    public UpCbjBean getFirstData() {
        UpCbjBean cbj = new UpCbjBean();
        if (db.isOpen()) {
            Cursor c = db.rawQuery("SELECT * FROM sbinfo where isUpload = 0 and isChaoBiao = 1 ", null);
            if (c.moveToFirst()) {
                cbj.setHmph(c.getString(c.getColumnIndex("hmph")));
                cbj.setRq(c.getString(c.getColumnIndex("rq")));
                cbj.setCmds1(c.getString(c.getColumnIndex("cmds1")));
                cbj.setFBH(c.getString(c.getColumnIndex("fbh")));
            }
            c.close();
        }
        return cbj;
    }

    /**
     * 获取一条数据 -- 未上传的预交金额
     *
     * @return
     */
    public YjMoneyBean getYjMoneyFirstData() {
        YjMoneyBean yjm = new YjMoneyBean();
        if (db.isOpen()) {
            Cursor c = db.rawQuery("SELECT * FROM YjMoney where isUpload = 0", null);
            if (c.moveToFirst()) {

                yjm.setId(c.getString(c.getColumnIndex("_id")));
                yjm.setIsUpload(c.getString(c.getColumnIndex("isUpload")));
                yjm.setHmph(c.getString(c.getColumnIndex("hmph")));           // 用户编号
                yjm.setYjMoney(c.getString(c.getColumnIndex("yjMoney")));    // 预交金额
                yjm.setCzybm(c.getString(c.getColumnIndex("czybm")));         // 登录账号 -- 抄表员编号
                yjm.setTime(c.getString(c.getColumnIndex("time")));          // 上传时间
            }
            c.close();
        }
        return yjm;
    }


    /**
     * 修改上传标志
     *
     * @param hmph
     * @param rq
     */
    public void updateUpload(String hmph, String rq) {
        ContentValues values = new ContentValues();
        values.put("hmph", hmph);
        values.put("rq", rq);
        values.put("isUpload", 1);
        db.update("sbinfo", values, "hmph =? and rq = ?", new String[]{hmph, rq});
    }

    /**
     * 修改上传标志 -- 预交水费的
     *
     * @param id
     */
    public void updateUploadYjMoney(String id) {
        ContentValues values = new ContentValues();
        values.put("isUpload", 1);
        db.update("YjMoney", values, "_id = ?", new String[]{id});
    }


    /**
     * 根据用户标签号 + 抄表月 查询预交的钱
     *
     * @return
     */
    public int selectBydzbqYjMoney(String dzbq, String cbye) {

        //Log.i("m520", "dzbq:"+dzbq + "   cbye:" + cbye);
        if (db.isOpen()) {
            //Cursor c = db.rawQuery("SELECT * FROM sbinfo where dzbq="+ dzbq +" and cbye=" + cbye, null);

            Cursor c = db.rawQuery("SELECT * FROM sbinfo where dzbq = ? and cbye = ?", new String[]{dzbq, cbye});

            if (c.moveToFirst()) {
                return c.getInt(c.getColumnIndex("yjMoney"));
            }
            c.close();
        }
        return 0;
    }



    /**
     * 根据用户编号 和 抄表月 查询预交的钱
     *
     * @param hmph          用户编号 -- 户号
     * @param cbye          抄表月
     *
     * @return
     */
    public int selectByhmphYjMoney(String hmph, String cbye) {

        if (db.isOpen()) {
            Cursor c = db.rawQuery("SELECT * FROM sbinfo where hmph = ? and cbye = ? ",
                    new String[]{hmph,cbye});

            if (c.moveToFirst()) {
                return c.getInt(c.getColumnIndex("yjMoney"));

            }
            c.close();
        }
        return 0;
    }


    /**
     * 修改预交水费
     *
     * @param hmph      用户编号 -- 户号
     * @param jyMoney   预交水费
     * @return
     */
    public boolean updateYjMoney(String hmph,String jyMoney) {
        ContentValues values = new ContentValues();
        values.put("yjMoney", jyMoney);
        db.update("sbinfo", values, "hmph =?", new String[]{hmph});
        return true;
    }

    /**
     * 修改户号的电子标签
     *
     * @param hmph      用户编号 -- 户号
     * @param dzbq      电子标签
     * @return
     */
    public boolean updateDzbq(String hmph,String dzbq) {
        ContentValues values = new ContentValues();
        values.put("dzbq", dzbq);
        int count = db.update("sbinfo", values, "hmph =?", new String[]{hmph});
        if(count > 0)
            return true;
        else
            return false;
    }

    /**
     * 修改户号的电话号码
     *
     * @param hmph      用户编号 -- 户号
     * @param dh      电话号码
     * @return
     */
    public boolean updateDh(String hmph,String dh) {
        ContentValues values = new ContentValues();
        values.put("dh", dh);
        int count = db.update("sbinfo", values, "hmph =?", new String[]{hmph});
        if(count > 0)
            return true;
        else
            return false;
    }

    /**
     * 修改本月表数
     *  还要将上传标志改为 0 -- 未上传
     *
     * @param dzbq          标签号
     * @param beny          本月抄表
     * @param bejy          上次节余
     * @param jyMoney       预交金额
     * @param sysl1         本月水量
     * @param cbye          抄表年月（201704 不是 详细的）
     * @return
     */
    public boolean updatebney(String dzbq, String beny, String bejy, double jyMoney, String sysl1, String cbye) {
        ContentValues values = new ContentValues();
        values.put("cmds1", beny);
        values.put("scjy", bejy);
        values.put("yjMoney", jyMoney);
        values.put("isChaoBiao", 1);
        values.put("sysl1", sysl1);
        values.put("rq", TimeUtils.getCurrentTimeRq());     // 抄表日期 详细的  -- yyyy-MM-dd HH:mm:ss.sss
        values.put("isUpload", 0);

        db.update("sbinfo", values, "dzbq =? and cbye = ?", new String[]{dzbq, cbye});
        return true;
    }


    /**
     * 查询数据总数
     *
     * @return
     */
    public int selectChaoBiaoCount(String cbye) {
        Cursor c = db.rawQuery("select * from sbinfo where isChaoBiao = 1  and cbye = '" + cbye + "'", null);
        return c.getCount();
    }
    /**
     * 查询本月已上传总户数
     *
     * @return
     */
    public int uploadCount(String cbye) {
        Cursor c = db.rawQuery("select * from sbinfo where isUpload = 1  and cbye = '" + cbye + "'", null);
        return c.getCount();
    }


    /**
     * 查询数据总数
     *
     * @return
     */
    public int selectTodayChaoBiaoCount() {
        String day = TimeUtils.getCurrentyyyyMMdd();
        Cursor c = db.rawQuery("select * from sbinfo where isChaoBiao = 1  and rq like '%" + day + "%'", null);
        return c.getCount();
    }

//    /**
//     * 所有预交的钱
//     *
//     * @param cbye
//     * @return
//     */
//    public int selectAllYjMoney(String cbye) {
//        Cursor c = db.rawQuery("select sum(yjMoney) as num from sbinfo where isChaoBiao = 1 and cbye = '" + cbye + "'", null);
//        while (c.moveToNext()) {
//            return c.getInt(c.getColumnIndex("num"));
//        }
//        return 0;
//    }
    /**
     * 所有预交的钱
     *
     * @param cbye
     * @return
     */
    public int selectAllYjMoney(String cbye) {

        String timeStr = cbye;          // 201705
        StringBuilder  sb = new StringBuilder (timeStr);
        sb.insert(4, "-");
        String timeStrNew = sb.toString();  // 2017-05

        Cursor c = db.rawQuery("select sum(yjMoney) as num from YjMoney where time like '%" + timeStrNew + "%'", null);
        while (c.moveToNext()) {
            return c.getInt(c.getColumnIndex("num"));
        }
        return 0;
    }

//    /**
//     * 当天--所有预交的钱
//     *
//     * @return
//     */
//    public int selectDayAllYjMoney() {
//        String day = TimeUtils.getCurrentyyyyMMdd();
//        //增加 or isChaoBiao = 0
//        Cursor c = db.rawQuery("select sum(yjMoney) as num from sbinfo where (isChaoBiao = 1 or isChaoBiao = 0) and rq like '%" + day + "%'", null);
//        while (c.moveToNext()) {
//            return c.getInt(c.getColumnIndex("num"));
//        }
//        return 0;
//    }

    /**
     * 当天--所有预交的钱
     *
     * @return
     */
    public int selectDayAllYjMoney() {
        String day = TimeUtils.getCurrentyyyyMMdd();
        //增加 or isChaoBiao = 0
        Cursor c = db.rawQuery("select sum(yjMoney) as num from YjMoney where time like '%" + day + "%'", null);
        while (c.moveToNext()) {
            return c.getInt(c.getColumnIndex("num"));
        }
        return 0;
    }

    /**
     * 将传入的"月份"在"数据库中"查询出来<p>
     * select count(*) as num from sbinfo where cbye = cbye<br>
     * 重命名为 num 这个字段名 -- 最后将这个数（此月要抄的记录数）返回 ; 没有查询到返回 0
     * @return
     */
    public int selectAlluser(String cbye) {
        Cursor c = db.rawQuery("select count(*) as num from sbinfo where cbye = '" + cbye + "' ", null);
        while (c.moveToNext()) {
            return c.getInt(c.getColumnIndex("num"));  //
        }
        return 0;
    }

    /**
     * 更加月份查询未抄表数据 <p>
     * 户号，户名，电子标签，电话，地址
     *
     * @param cbye 月份
     * @return
     */
    public ArrayList<TjNotChaoBiao> selectAllNotChaoBiaoByCbye(String cbye) {

        ArrayList<TjNotChaoBiao> tjNotChaoBiaoList = new ArrayList<>();

        Cursor c = db.rawQuery("select hmph,hm,dzbq,dh,dz from sbinfo where isChaoBiao = 0 and cbye = ? ", new String[]{cbye});
        while (c.moveToNext()) {
            TjNotChaoBiao bean = new TjNotChaoBiao();

            bean.setHmph(c.getString(0));
            bean.setHm(c.getString(1));
            bean.setDzbq(c.getString(2));
            bean.setDh(c.getString(3));
            bean.setDz(c.getString(4));

            tjNotChaoBiaoList.add(bean);
        }

        //必须是Comparator中的compare方法和Collections.sort方法配合使用才管用
        ComparatorByHmph mc = new ComparatorByHmph() ;
        Collections.sort(tjNotChaoBiaoList, mc) ;

        return tjNotChaoBiaoList;
    }

    /**
     * 查询某月未上传的"预交金额" <p>
     *
     *
     * @param cbye 月份
     * @return
     */
    public ArrayList<YjMoneyBean> selectAllNotUpLoadYjMoneyCbye(String cbye) {

        ArrayList<YjMoneyBean> yjMoneyBeanList = new ArrayList<>();

        String timeStr = cbye;          // 201705
        StringBuilder  sb = new StringBuilder (timeStr);
        sb.insert(4, "-");
        String timeStrNew = sb.toString();  // 2017-05

        //Cursor c = db.rawQuery("select * from YjMoney where isUpload = 0 and cbye = ? ", new String[]{cbye});
        Cursor c = db.rawQuery("select * from YjMoney where isUpload = 0 and time like ? ", new String[]{"%" + timeStrNew+"%"});

        while (c.moveToNext()) {
            YjMoneyBean bean = new YjMoneyBean();

            bean.setId(c.getString(0));
            bean.setIsUpload(c.getString(1));
            bean.setHmph(c.getString(2));
            bean.setYjMoney(c.getString(3));
            bean.setCzybm(c.getString(4));
            bean.setTime(c.getString(5));

            yjMoneyBeanList.add(bean);
        }

        //必须是Comparator中的compare方法和Collections.sort方法配合使用才管用
//        ComparatorByHmph mc = new ComparatorByHmph() ;
//        Collections.sort(tjNotChaoBiaoList, mc) ;

        return yjMoneyBeanList;
    }


    /**
     * 添加"需要预交的费用"
     *
     * @param hmph          用户编号
     * @param yjMoney       预交金额
     * @param czybm         登录账号 -- 抄表员编号
     */
    public boolean addYjMoney(String hmph, String yjMoney, String czybm, String time){
        ContentValues value = new ContentValues();
        value.put("isUpload", 0);
        value.put("hmph", hmph);
        value.put("yjMoney", yjMoney);
        value.put("czybm", czybm);
        value.put("time", time);
        long id = db.insertOrThrow("YjMoney", null, value);

        return id > 0 ? true : false ;

    }

    /**
     * 关闭数据库
     */
    public void closeDb() {
        if (null != db)
            db.close();
    }
}
