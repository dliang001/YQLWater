package com.yq.tasks.presenter;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.smtlibrary.https.OkHttpUtils;
import com.smtlibrary.utils.JsonUtils;
import com.smtlibrary.utils.LogUtils;
import com.yq.model.CbData;
import com.yq.model.Dh;
import com.yq.model.LoginBean;
import com.yq.model.LoginPrams;
import com.yq.model.Prams;
import com.yq.model.QueryBean;
import com.yq.model.QueryPrams;
import com.yq.model.RetData;
import com.yq.model.UpCbj;
import com.yq.model.UpCbjBean;
import com.yq.model.UserArrears;
import com.yq.tasks.views.IView;
import com.yq.tasks.views.LoginView;
import com.yq.tasks.views.QueryView;
import com.yq.tasks.views.UView;
import com.yq.utils.WriteFileToSDUtils;
import com.yq.yqwater.MeApplcition;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by gbh on 16/12/7.
 */

public class TaskPresenterImpl implements TaskPresenter {

    private String baseUrl = "http://219.139.44.245:8086/webServiceXSD1/CbjServlet.action?method=";

    private IView iView;
    private UView uView;
    private LoginView lView;
    private QueryView qView;

    public TaskPresenterImpl(IView iView) {
        this.iView = iView;
    }

    public TaskPresenterImpl(UView uView) {
        this.uView = uView;
    }

    public TaskPresenterImpl(LoginView lView) {
        this.lView = lView;
    }

    public TaskPresenterImpl(QueryView qView) {
        this.qView = qView;
    }

    public TaskPresenterImpl() {

    }

    /**
     * 登录
     *
     * @param id
     * @param psw
     */
    @Override
    public void loginData(String id, String psw) {
        String url = baseUrl + "get_dl&&";
        lView.showPress();
        List<OkHttpUtils.Param> paramList = new ArrayList<>();
        paramList.add(new OkHttpUtils.Param("DATA", new LoginPrams(id, psw).toString()));

        OkHttpUtils.post(url, paramList, new OkHttpUtils.ResultCallBack<String>() {
            @Override
            public void onSuccess(String response) {
                //                Log.d("m520",response);
                lView.hidePress();
                try {
                    LoginBean cbData = JsonUtils.deserialize(response, LoginBean.class);
                    if (cbData.getRet().equals("ok"))
                        lView.onLoginSuccess(cbData);
                    else {
                        lView.onFaile("登录失败,请重试。");
                    }
                } catch (Exception e) {
                    //                    e.printStackTrace();
                    lView.onFaile("账号或密码错误,请重试!");
                }

            }

            @Override
            public void onFailure(Exception e) {
                lView.hidePress();
                lView.onFaile("网络异常,请重试。");
            }
        });
    }

    /**
     * 查询<p>
     * 查询 某个"标签号" 在 "某月"的信息
     *
     * @param bqh       标签号
     * @param dh        电话号码
     * @param date      日期 -- 201704
     */
    @Override
    public void queryData(String bqh, String dh, String date) {

        String url = baseUrl + "get_jfmx&&";
        qView.showPress();
        List<OkHttpUtils.Param> paramList = new ArrayList<>();
        paramList.add(new OkHttpUtils.Param("DATA", new QueryPrams(bqh, dh, date).toString()));
        OkHttpUtils.post(url, paramList, new OkHttpUtils.ResultCallBack<String>() {
            @Override
            public void onSuccess(String response) {
                // Log.i("shen", response);                    // 太长了，显示不出来
                WriteFileToSDUtils.writeFileToSD(response);    // 将数据写到日志文件
                qView.hidePress();
                try {
                    QueryBean  cbData = JsonUtils.deserialize(response, QueryBean.class);
                    if (cbData.getRet().equals("ok"))
                        qView.onquerySuccess(cbData);
                    else {
                        qView.onFaile("标签号或手机号不存在!");
                    }
                } catch (Exception e) {
                    // e.printStackTrace();
                    qView.onFaile("标签号或手机号不存在!");
                }

            }

            @Override
            public void onFailure(Exception e) {
                qView.hidePress();
                qView.onFaile("网络异常,请重试。");
            }
        });
    }

    /**
     * 获取用户欠费信息
     *
     * @param id        客户编号
     * @param fbh       水表编号
     */
    @Override
    public void getUserArrearsInfo(String id, String fbh) {
        String url = baseUrl + "get_qfxx&";
        iView.showPress();
        List<OkHttpUtils.Param> paramList = getParams(id, fbh);
        OkHttpUtils.post(url, paramList, new OkHttpUtils.ResultCallBack<String>() {
            @Override
            public void onSuccess(String response) {
                LogUtils.sysout("=====res", response);
                iView.hidePress();
                UserArrears cbData = JsonUtils.deserialize(response, UserArrears.class);
                if (cbData.getRet().equals("ok"))
                    iView.onDownUserInfo(cbData);           // 子类实现
                else {
                    iView.onFaile("下载失败,请重试。");
                }

            }

            @Override
            public void onFailure(Exception e) {
                iView.hidePress();
                iView.onFaile("网络异常,请重试。");
            }
        });
    }





    /**
     * 异步--下载数据
     */
    public class downDataTask extends AsyncTask<String, Integer, Boolean> {

        IView taskIView;
        String strUrl;
        String json;

        public downDataTask(IView iView, String url){
            taskIView = iView;
            this.strUrl = url;

        }

        @Override
        protected void onPreExecute() {                                         // 执行前
            super.onPreExecute();
            taskIView.showPress();
        }

        @Override
        protected Boolean doInBackground(String... strings) {            // 执行中

            boolean b = false;

            HttpURLConnection urlConn = null;
            try {
                // 新建一个URL对象
                URL url = new URL(strUrl);
                // 打开一个HttpURLConnection连接
                urlConn = (HttpURLConnection) url.openConnection();
                // 设置连接超时时间
                urlConn.setConnectTimeout(20 * 1000);
                // 设置请求方式
                urlConn.setRequestMethod("GET");
                // 设置读取时间
                urlConn.setReadTimeout(20 * 1000);

                // 开始连接
                urlConn.connect();
                // 判断请求是否成功
                if (urlConn.getResponseCode() == 200) {
                    // 获取返回的数据
                    InputStream in = urlConn.getInputStream();
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();

                    byte[] buff = new byte[1024];
                    int len = -1;
                    while((len = in.read(buff)) != -1){
                        bout.write(buff, 0, len);
                    }

                    json = new String(bout.toByteArray());
                    // Log.d("m520", "get -- json:"+json);

                    b = true;
                } else {
                    Log.d("m520", "Get方式请求失败");
                    b = false;
                }
            }catch (Exception e){
                Log.d("m520", "get -- e.getMessage():" + e.getMessage());
                b = false;
            }

            // 关闭连接
            urlConn.disconnect();

            return b;
        }

        @Override
        protected void onPostExecute(Boolean b) {           // 执行后
            super.onPostExecute(b);

            if(b){
                taskIView.hidePress();
                try {
                    CbData cbData = JsonUtils.deserialize(json, CbData.class);
                    if (cbData.getRet().equals("ok"))
                        taskIView.onDownSuccess(cbData);
                    else {
                        taskIView.onFaile("下载失败,请重试。");
                    }
                } catch (Exception e) {
                    // e.printStackTrace();
                    taskIView.onFaile("下载失败,请重试。");
                }

            }else{
                taskIView.hidePress();
                taskIView.onFaile("网络异常,请重试。");
            }


        }
    }

    /**
     * 下载数据<p>
     * 下载 此月，此用户，所有区域的用户数据
     *
     * @param id
     * @param fbh
     */
    @Override
    public void downData(String id, String fbh) {
//        String url = baseUrl + "get_cbsj&";

        String url = baseUrl + "get_cbsj&DATA={" +
                "'id':'" + id +
                "','fbh':'" + fbh +
                "'}";

        new downDataTask(iView, url).execute();

    }

    /**
     * 上传电话
     *
     * @param hmph
     * @param Dh
     */
    @Override
    public void uploadPhone(String hmph, String Dh) {
        String url = baseUrl + "Send_dh&";
        List<OkHttpUtils.Param> paramList = new ArrayList<>();
        Prams p = new Prams();
        p.setHmph(hmph);
        p.setDh(Dh);
        List<Prams> data = new ArrayList<>();
        data.add(p);
        paramList.add(new OkHttpUtils.Param("jsh", uView.userId()));
        paramList.add(new OkHttpUtils.Param("DATA", new Dh(data).toString()));
        LogUtils.sysout("=====req:", new Dh(data).toString());
        uView.showPress();
        OkHttpUtils.post(url, paramList, new OkHttpUtils.ResultCallBack<String>() {
            @Override
            public void onSuccess(String response) {
                LogUtils.sysout("=====res", response);
                uView.hidePress();
                RetData retData = JsonUtils.deserialize(response, RetData.class);
                if (retData.getRet().equals("ok")) {
                    uView.onSuccess();
                } else {
                    uView.onFaile("上传失败,请重试。");
                }
            }

            @Override
            public void onFailure(Exception e) {
                uView.hidePress();
                uView.onFaile("网络异常,请重试。");
            }
        });
    }

    /**
     * 上传缴费
     *
     * @param yjMoneyDatabaseId   这回条预交金额的数据库的ID
     * @param id        Hmph 户号
     * @param je        预交金额
     * @param sid       登录用户 -- 操作员
     */
    @Override
    public void upPayData(final String yjMoneyDatabaseId, final String id, String je, String sid) {
        String url = "http://219.139.44.245:8086/webServiceXSD1/CbjServlet.action?method=get_sbjf&&";
        //uView.showPress();
        List<OkHttpUtils.Param> paramList = new ArrayList<>();
        Prams p = new Prams();
        p.setId(id);
        p.setJE(je);
        p.setSfy(sid);
        paramList.add(new OkHttpUtils.Param("DATA", JsonUtils.serialize(p)));

        OkHttpUtils.post(url, paramList, new OkHttpUtils.ResultCallBack<String>() {
            @Override
            public void onSuccess(String response) {
                LogUtils.sysout("=====res", response);
                //uView.hidePress();
                RetData retData = null;
                try {
                    retData = JsonUtils.deserialize(response, RetData.class);
                    if (retData.getRet().equals("ok")) {
                        //Log.d("m520","UploadYjMoney -- ok");

                        //在本地数据库中修改上传标志
                        MeApplcition.mgr.updateUploadYjMoney(yjMoneyDatabaseId);

                    } else {
                        //uView.onFaile("上传失败,请重试。");
                        Log.d("m520","UploadYjMoney -- error");
                    }
                } catch (Exception e) {
                    //                    e.printStackTrace();
                    //uView.onFaile("上传失败,请重试。");
                    Log.d("m520","upPayData --- e.getMessage():" + e.getMessage());
                }


            }

            @Override
            public void onFailure(Exception e) {
                //uView.hidePress();
                //uView.onFaile("网络异常,请重试。");
            }
        });
    }

    /**
     * 上传性质 -- 上传标签号
     *
     * @param id 用户编号
     * @param xz 标签号
     */
    @Override
    public void upXzData(String id, String xz) {
        String url = baseUrl + "Send_xz&";
        uView.showPress();
        List<OkHttpUtils.Param> paramList = new ArrayList<>();
        Prams p = new Prams();
        p.setHmph(id);
        p.setFBH("");
        p.setYsxz(xz);
        //        p.setDzbp(dzbp);
        List<Prams> data = new ArrayList<>();
        data.add(p);
        paramList.add(new OkHttpUtils.Param("jsh", uView.userId()));
        paramList.add(new OkHttpUtils.Param("DATA", new Dh(data).toString()));
        LogUtils.sysout("=====update", new Dh(data).toString());
        OkHttpUtils.post(url, paramList, new OkHttpUtils.ResultCallBack<String>() {
            @Override
            public void onSuccess(String response) {
                //                LogUtils.sysout("=====res", response);
                Log.v("m520", response);
                uView.hidePress();
                RetData retData = JsonUtils.deserialize(response, RetData.class);
                if (retData.getRet().equals("ok")) {
                    uView.onSuccess();
                } else {
                    uView.onFaile("上传失败,请重试。");
                }

            }

            @Override
            public void onFailure(Exception e) {

                uView.hidePress();
                uView.onFaile("网络异常,请重试。");
            }
        });
    }

    /**
     * 上传抄表记录
     */
    @Override
    public void upDzbqData(final UpCbjBean cbj, String userId) {
        String url = baseUrl + "Send_data&";
        List<OkHttpUtils.Param> paramList = new ArrayList<>();
        List<UpCbjBean> data = new ArrayList<>();
        data.add(cbj);
        paramList.add(new OkHttpUtils.Param("jsh", userId));
        paramList.add(new OkHttpUtils.Param("DATA", new UpCbj(data).toString()));

        OkHttpUtils.post(url, paramList, new OkHttpUtils.ResultCallBack<String>() {
            @Override
            public void onSuccess(String response) {
                LogUtils.sysout("=====res", response);

                RetData retData = JsonUtils.deserialize(response, RetData.class);

                if (retData.getRet().equals("ok")) {
                    //在本地数据库中修改上传标志
                    MeApplcition.mgr.updateUpload(cbj.getHmph(), cbj.getRq());
                }

            }

            @Override
            public void onFailure(Exception e) {
            }
        });
    }


    @NonNull
    private List<OkHttpUtils.Param> getParams(String id, String fbh) {
        List<OkHttpUtils.Param> paramList = new ArrayList<>();
        paramList.add(new OkHttpUtils.Param("DATA", new Prams(id, fbh).toString()));

        Log.d("m520",new Prams(id, fbh).toString());
        return paramList;
    }
}
