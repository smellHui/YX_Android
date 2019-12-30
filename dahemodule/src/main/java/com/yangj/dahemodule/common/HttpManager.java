package com.yangj.dahemodule.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.tepia.base.AppRoutePath;
import com.tepia.base.CacheConsts;
import com.tepia.base.http.BaseResponse;
import com.tepia.base.http.RetrofitManager;
import com.tepia.base.utils.Utils;
import com.tepia.base.view.floatview.CollectionsUtil;
import com.tepia.guangdong_module.APPCostant;
import com.tepia.guangdong_module.amainguangdong.common.UserManager;
import com.tepia.guangdong_module.amainguangdong.model.UserInfoBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.AreaBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.DataBeanOflistReservoirRoute;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirBean;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirListResponse;
import com.tepia.guangdong_module.amainguangdong.model.xuncha.ReservoirOfflineResponse;
import com.tepia.photo_picker.utils.SPUtils;
import com.yangj.dahemodule.http.UserHttpService;
import com.yangj.dahemodule.model.NewNoticeBean;
import com.yangj.dahemodule.model.UserBean;
import com.yangj.dahemodule.model.UserDataBean;
import com.yangj.dahemodule.model.UserLoginResponse;
import com.yangj.dahemodule.model.WeatherWarnBean;
import com.yangj.dahemodule.model.danger.DangerDataBean;
import com.yangj.dahemodule.model.main.MainDataBean;
import com.yangj.dahemodule.model.main.Route;
import com.yangj.dahemodule.model.patrol.PatrolRecordDataBean;
import com.yangj.dahemodule.model.report.ReportDataBean;
import com.yangj.dahemodule.model.report.ReportDetailDataBean;
import com.yangj.dahemodule.model.user.RolesBean;
import com.yangj.dahemodule.model.user.SysUserBean;
import com.yangj.dahemodule.model.user.SysUserDataBean;
import com.yangj.dahemodule.model.xuncha.ProtalDataBean;
import com.yangj.dahemodule.model.xuncha.RecordDataBean;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.yangj.dahemodule.fragment.DealFragment.DEAL_COMPLETE;
import static com.yangj.dahemodule.fragment.DealFragment.DEAL_DOING;
import static com.yangj.dahemodule.fragment.operate.OperatesFragment.ALL_OPERATE;
import static com.yangj.dahemodule.fragment.operate.OperatesFragment.MINE_OPERATE;

/**
 * Created by Joeshould on 2018/5/22.
 */

public class HttpManager {
    private UserHttpService mRetrofitService;
    private static final HttpManager ourInstance = new HttpManager();
    private static final HttpManager ourInstance_admin = new HttpManager(APPCostant.API_SERVER_USER_ADMIN);
    private static final HttpManager ourInstance_works = new HttpManager(APPCostant.API_SERVER_TASK_AREA);
    private static final HttpManager ourInstance_monitor = new HttpManager(APPCostant.API_SERVER_MONITOR_AREA);
    private static final HttpManager ourInstance_json = new HttpManager("http://202.98.201.103:7000/", true);
    public static final String USERINFO = "USERINFO";
    private static final String TOKEN = "TOKEN";
    private static final String DEFAULT_RES = "DEFAULT_RES";

    private String token;

    public static HttpManager getInstance() {
        return ourInstance;
    }

    public static HttpManager getInstance_ADMIN() {
        return ourInstance_admin;
    }

    public static HttpManager getInstance_Works() {
        return ourInstance_works;
    }

    public static HttpManager getInstance_Monitor() {
        return ourInstance_monitor;
    }

    public static HttpManager getInstance_Json() {
        return ourInstance_json;
    }

    private HttpManager() {
        this.mRetrofitService = RetrofitManager.getRetrofit(APPCostant.API_SERVER_URL).create(UserHttpService.class);
    }

    private HttpManager(String adminstr) {
        this.mRetrofitService = RetrofitManager.getRetrofit(APPCostant.API_SERVER_URL + adminstr).create(UserHttpService.class);
    }

    private HttpManager(String str, boolean distinct) {
        this.mRetrofitService = RetrofitManager.getRetrofit(str).create(UserHttpService.class);
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    public Observable<UserDataBean> login(String username, String password) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("username", username);
        hashMap.put("password", password);
        hashMap.put("randomStr", "");
        hashMap.put("grant_type", "password");
        hashMap.put("scope", "server");
        return mRetrofitService.login(getRequestBody(hashMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private RequestBody getRequestBody(HashMap<String, String> hashMap) {
        StringBuffer data = new StringBuffer();
        if (hashMap != null && hashMap.size() > 0) {
            Iterator iter = hashMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                data.append(key).append("=").append(val).append("&");
            }
        }
        String jso = data.substring(0, data.length() - 1);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"), jso);

        return requestBody;
    }

    private String makeToken() {
        return "Bearer " + getToken();
    }

    /**
     * 【查询】巡检记录列表
     *
     * @param pageType
     * @param reservoirCode
     * @param pageNum
     * @param pageSize
     * @param startDate
     * @param endDate
     * @return
     */
    public Observable<RecordDataBean> getRecordList(int pageType, String reservoirCode, int pageNum, int pageSize, String startDate, String endDate) {
        HashMap<String, String> hashMap = new HashMap<>();
        if (!TextUtils.isEmpty(reservoirCode))
            hashMap.put("reservoirCode", reservoirCode);
        hashMap.put("pageNum", pageNum + "");
        hashMap.put("pageSize", pageSize + "");
        if (!TextUtils.isEmpty(startDate))
            hashMap.put("startDate", startDate);
        if (!TextUtils.isEmpty(endDate))
            hashMap.put("endDate", endDate);
        if (pageType == MINE_OPERATE) {
            return mRetrofitService.getRecordListByMe(makeToken(), hashMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
        if (pageType == ALL_OPERATE) {
            return mRetrofitService.getRecordList(makeToken(), hashMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
        return null;
    }

    /**
     * 【查询】待处理险情列表
     *
     * @param dealType
     * @param reservoirCode
     * @param pageNum
     * @param pageSize
     * @param startDate
     * @param endDate
     * @param source
     * @return
     */
    public Observable<DangerDataBean> getPendingHandleList(int dealType, String reservoirCode, int pageNum, int pageSize, String startDate, String endDate, String source) {
        HashMap<String, String> hashMap = new HashMap<>();
        if (!TextUtils.isEmpty(reservoirCode))
            hashMap.put("reservoirCode", reservoirCode);
        if (!TextUtils.isEmpty(startDate)) {
            hashMap.put("startDate", startDate);
        }
        if (!TextUtils.isEmpty(endDate)) {
            hashMap.put("endDate", endDate);
        }
        if (!TextUtils.isEmpty(source)) {
            hashMap.put("source", source);
        }
        hashMap.put("pageNum", pageNum + "");
        hashMap.put("pageSize", pageSize + "");
        if (dealType == DEAL_DOING) {
            return mRetrofitService.getPendingHandleList(makeToken(), hashMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
        if (dealType == DEAL_COMPLETE) {
            return mRetrofitService.getHandleList(makeToken(), hashMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
        return null;
    }

    /**
     * 【查询】巡检详情
     *
     * @param omRecordCode
     * @return
     */
    public Observable<ProtalDataBean> getPatrolDetail(String omRecordCode) {
        return mRetrofitService.getPatrolDetail(makeToken(), omRecordCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 【查询】险情详情
     *
     * @param reportId
     * @return
     */
    public Observable<ReportDetailDataBean> loadReportDetail(String reportId) {
        return mRetrofitService.loadReportDetail(makeToken(), reportId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 【查询】详情和操作流水
     *
     * @param omRecordCode
     * @return
     */
    public Observable<PatrolRecordDataBean> loadPatrolDetailAndFlow(String omRecordCode) {
        return mRetrofitService.loadPatrolDetailAndFlow(makeToken(), omRecordCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 【查询】我上报的险情
     *
     * @param reservoirCode
     * @param pageNum
     * @param pageSize
     * @param startDate
     * @param endDate
     * @return
     */
    public Observable<ReportDataBean> getReportList(String reservoirCode, int pageNum, int pageSize, String startDate, String endDate) {
        HashMap<String, String> hashMap = new HashMap<>();
        if (!TextUtils.isEmpty(reservoirCode))
            hashMap.put("reservoirCode", reservoirCode);
        hashMap.put("pageNum", pageNum + "");
        hashMap.put("pageSize", pageSize + "");
        if (!TextUtils.isEmpty(startDate))
            hashMap.put("startDate", startDate);
        if (!TextUtils.isEmpty(endDate))
            hashMap.put("endDate", endDate);
        return mRetrofitService.getReportList(makeToken(), hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 【新增】险情反馈
     *
     * @param problemId
     * @param feedbackContent
     * @param files
     * @return
     */
    public Observable<BaseResponse> feekBackProblem(String problemId, String feedbackContent, List<String> files) {
        String token = makeToken();
        Map<String, RequestBody> params = new HashMap<>();
        params.put("problemId", RetrofitManager.convertToRequestBody(problemId));
        params.put("feedbackContent", RetrofitManager.convertToRequestBody(feedbackContent));
        List<File> beforefileList = new ArrayList<>();
        if (!CollectionsUtil.isEmpty(files)) {
            for (int i = 0; i < files.size(); i++) {
                File file = new File(files.get(i));
                beforefileList.add(file);
            }
        }
        List<MultipartBody.Part> beforePathList = RetrofitManager.filesToMultipartBodyParts("pictures", beforefileList);
        return mRetrofitService.feekBackProblem(token, params, beforePathList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 【查询】加载数据接口
     *
     * @param reservoirCode
     * @return
     */
    public Observable<MainDataBean> loadData(String reservoirCode) {
        return mRetrofitService.loadData(makeToken(), reservoirCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 【查询】用户信息
     *
     * @return
     */
    public Observable<SysUserDataBean> getUserInfo() {
        return mRetrofitService.getUserInfo(makeToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * @return
     */
    public Observable<ReservoirListResponse> getReservoirList() {
        String token = getToken();

        return mRetrofitService.getReservoirList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * APP查询某个水库全部信息
     *
     * @param reservoirId
     * @return
     */
    public Observable<ReservoirOfflineResponse> getAllReservoirData(String reservoirId) {
        String token = getToken();

        return mRetrofitService.getAllReservoirData(token, reservoirId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 查询水库最新通知
     *
     * @param reservoirId
     * @return
     */
    public Observable<NewNoticeBean> getNewNotice(String reservoirId) {
        String token = getToken();

        return mRetrofitService.getNewNotice(token, reservoirId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 天气预警
     *
     * @return
     */
    public Observable<WeatherWarnBean> getWeatherWarn() {
        String token = getToken();

        return mRetrofitService.getWeatherWarn(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取当前用户行政区划下拉(省市三级联动)
     *
     * @return
     */
    public Observable<AreaBean> getAreaSelect() {
        String token = getToken();
        /*token = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJndWFuZ2RvbmdhZG" +
                "1pbiIsInVzZXJJZCI6ImFlNTFiYzZiNWE5ZjRjZjA5YTk2YWQ4YjUwNzVjMTYxIiwibmFtZSI" +
                "6IuW5v-S4nOecgeeuoeeQhuWRmCIsImxvZ2luU2NvcGUiOiIwIiwiZXhwIjoxNTU5MjM0OTgwfQ.BwxHE" +
                "rO2eSm5M8WFh4GK-MCv2qJw1X36h97J8MmG1hbWBaqVlf3loJcIAPh-PNZPNmmYTvS2CA-UCzJf3qe-zc" +
                "nYJBNdSFmKYCHkiF3ticSYpbNW2khxF6N_WOjBMr82G3UK5yDKLI_0_iYmW7QaVe9UQ6eHOzBpxwBQfMqIbcs";*/

        return mRetrofitService.getAreaSelect(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResponse> updateNotice(String id, String workOrderId) {
        String token = getToken();

        return mRetrofitService.updateNotice(token, id, workOrderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 【审核】审核记录
     *
     * @param params
     * @return
     */
    public Observable<BaseResponse> examinePatrol(Object... params) {
        String token = makeToken();
        RequestBody body = RetrofitManager.convertToRequestBodyForJson(params);
        return mRetrofitService.examinePatrol(token,body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取用户登录信息
     *
     * @return
     */
    public Observable<UserInfoBean> getLoginUser() {
        String token = getToken();

        return mRetrofitService.getLoginUser(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void saveToken(UserLoginResponse userBean) {
        if (userBean != null) {
            SPUtils.getInstance(Utils.getContext()).putString(TOKEN, userBean.getData());
            SPUtils.getInstance(Utils.getContext()).putString(USERINFO, new Gson().toJson(userBean));

        }

    }

    public void saveRoutes(String str) {
        SPUtils.getInstance().putString("Routes", str);
    }

    public List<Route> getRoutes() {
        String str = SPUtils.getInstance().getString("Routes", "");
        return JSON.parseArray(str, Route.class);
    }

    public void saveUser(String str) {
        SPUtils.getInstance().putString("Userbean", str);
    }

    public UserBean getUser() {
        String str = SPUtils.getInstance().getString("Userbean", "");
        return JSON.parseObject(str, UserBean.class);
    }

    public void saveSysUser(String str) {
        SPUtils.getInstance().putString("SysUser", str);
    }

    public SysUserBean getSysUser() {
        String str = SPUtils.getInstance().getString("SysUser", "");
        return JSON.parseObject(str, SysUserBean.class);
    }

    public void saveRolesBean(String str) {
        SPUtils.getInstance().putString("RolesBean", str);
    }

    public RolesBean getRolesBean() {
        String str = SPUtils.getInstance().getString("RolesBean", "");
        return JSON.parseObject(str, RolesBean.class);
    }

    public boolean isXC() {
        RolesBean role = this.getRolesBean();
        return role != null && role.isXC();
    }

    public String getToken() {
        UserBean userBean = getUser();
        if (userBean == null) return null;
        return userBean.getAccess_token();
    }


    /**
     * 根据水库id 查询离线数据实体
     *
     * @param reservoirId
     * @return
     */
    public DataBeanOflistReservoirRoute getOfflineReservoir(String reservoirId, String userCode, Context context) {
        DataBeanOflistReservoirRoute dataBeanList = null;
        ReservoirBean reservoirBean = DataSupport.where("reservoirId = ? and userCode=?", reservoirId, userCode).findFirst(ReservoirBean.class);
        if (reservoirBean != null) {
            String jsonAboutInfo = reservoirBean.getJsonAboutInfo();
            if (TextUtils.isEmpty(jsonAboutInfo)) {

                showOffine(context);
                return dataBeanList;
            }
            ReservoirOfflineResponse reservoirOfflineResponse = new Gson().fromJson(jsonAboutInfo, ReservoirOfflineResponse.class);
            if (reservoirOfflineResponse != null) {
                dataBeanList = reservoirOfflineResponse.getData();

            }
        }
        return dataBeanList;
    }

    /**
     * 提醒下载离线包
     */
    private void showOffine(Context context) {
        ReservoirBean reservoirBean = UserManager.getInstance().getDefaultReservoir();
        if (context != null && !((Activity) context).isFinishing()) {
            String reservori = "";
            if (reservoirBean != null) {
                reservori = reservoirBean.getReservoir();
            }
            new AlertDialog.Builder(context)
                    .setMessage("请先下载" + reservori + "的基本信息离线包")
                    .setCancelable(true)
                    .setPositiveButton("去下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SPUtils.getInstance().putBoolean(CacheConsts.HAS_ASK, true);
                            ARouter.getInstance().build(AppRoutePath.app_select_reservor_downoffline)
                                    .withSerializable("selectedResrvoir", reservoirBean)
                                    //flag为10表示 离线数据包管理 为"0" 表示选择水库
                                    .withString("offlineFlag", "10")
                                    .navigation();

                        }
                    })
                    .show();
        }

    }

}
