package com.moyacs.canary.kchart.fragment;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.moyacs.canary.base.BaseFragment;
import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.kchart.chart.candle.KLineView;
import com.moyacs.canary.kchart.chart.cross.KCrossLineView;
import com.moyacs.canary.kchart.entity.KCandleObj;
import com.moyacs.canary.kchart.entity.KLineNormal;
import com.moyacs.canary.kchart.entity.KLineObj;
import com.moyacs.canary.kchart.listener.OnKChartClickListener;
import com.moyacs.canary.kchart.listener.OnKCrossLineMoveListener;
import com.moyacs.canary.kchart.listener.OnKLineTouchDisableListener;
import com.moyacs.canary.kchart.util.KDisplayUtil;
import com.moyacs.canary.kchart.util.KNumberUtil;
import com.moyacs.canary.kchart.util.KParamConfig;
import com.moyacs.canary.kchart.util.KParseUtils;
import com.moyacs.canary.netty.codec.Quotation;
import com.moyacs.canary.product_fxbtg.Product_constans;
import com.moyacs.canary.product_fxbtg.contract_kline.ProductContract;
import com.moyacs.canary.product_fxbtg.contract_kline.ProductPresenterImpl;
import com.moyacs.canary.product_fxbtg.net_kline.KLineData;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import www.moyacs.com.myapplication.R;

import static com.moyacs.canary.product_fxbtg.Product_constans.PARAM_KLINE_1D_WEIPAN_new;


/**
 * android:hardwareAccelerated="true"
 * 硬件加速问题  导致多层次的view重新绘制；
 * 设置hardwareAccelerated true
 */
public class KLineFragment extends BaseFragment implements OnKCrossLineMoveListener,
        OnKLineTouchDisableListener, OnKChartClickListener, ProductContract.ProductView {
    public static final String TAG = "KLineFragment";
    private KLineView kLineView;
    private LinearLayout crossInfoView;
    private TextView tvTime, tvOpen, tvClose, tvHigh, tvLow, tvRate, tvRateChange;
    private View layoutLoading = null, layoutContent = null;
    private View mainNormal, subNormal;
    //默认值 也就是NormUnionCandleStickChart的默认值  modify by fangzhu
    private int lastBottomNorm = KLineNormal.NORMAL_MACD;
    private int lastTopNorm = KLineNormal.NORMAL_SMA;

    private View mainNormalView, subNormalView, mainNormalViewLand, subNormalViewLand;
    private View landTypeView;

    private float mainF = 4 / 5F;//竖屏时候主图占整体的高度
    private float subF = 1 / 5F;//竖屏时候附图占整体的高度
    private float lanMainF = 2 / 3F;//横屏时候主图占整体的高度
    private float lanSubF = 1 / 3F;//横屏时候附图占整体的高度
    //传入的code ,code 为 品种的英文名称
    private String code;
    private String cycle;//周期
    //P 层对象
    private ProductContract.ProductPresenter presenter;
    //时间格式化
    private SimpleDateFormat simpleDateFormat;
    private int digit;
    //K 线图数据 请求开始时间
    private String startDate;
    //结束时间
    private String endDate;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private KCandleObj kCandleObj_refresh;
    //K线数据是否设置成功
    private boolean isEvent4SMA = false;

    public static KLineFragment newInstance(Bundle bundle) {
        KLineFragment fragment = new KLineFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    //处理回收 如home键后长时间
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("toptype", lastTopNorm);
        outState.putInt("bottomtype", lastBottomNorm);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_kline;
    }

    @Override
    protected void initView() {
        isEvent4SMA = false;
        landTypeView = rootView.findViewById(R.id.landTypeView);
        KCrossLineView crossLineView = rootView.findViewById(R.id.crossLineView);
//        crossLineView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //十字线出现的时候 详细信息。这里是activity 绑定的
        crossInfoView = rootView.findViewById(R.id.crossInfoView);
        tvTime = crossInfoView.findViewById(R.id.tv_time);
        tvOpen = crossInfoView.findViewById(R.id.tv_open);
        tvClose = crossInfoView.findViewById(R.id.tv_close);
        tvHigh = crossInfoView.findViewById(R.id.tv_high);
        tvLow = crossInfoView.findViewById(R.id.tv_low);
        tvRate = crossInfoView.findViewById(R.id.tv_rate);
        tvRateChange = crossInfoView.findViewById(R.id.tv_rateChange);
        LogUtils.d("digit  :    " + digit);

        kLineView = rootView.findViewById(R.id.klineView);
        //设置精度
        kLineView.setNumberScal(digit);
        //设置配置的k线颜色
        kLineView.setCandlePostColor(getResources().getColor(R.color.k_post));
        kLineView.setCandleNegaColor(getResources().getColor(R.color.k_neg));
        kLineView.setCrossLineView(crossLineView);
        kLineView.setShowSubChart(true);
        kLineView.setAxisYtopHeight(0);//最顶部不留白
        kLineView.setAxisYmiddleHeight(KDisplayUtil.dip2px(getActivity(), 47));
        //Y轴价格坐标 在边框内部
        kLineView.setAxisTitlein(true);
        //阻断touch事件的分发 并且setOnKLineTouchDisableListener中处理逻辑
        kLineView.setTouchEnable(true);
        //不显示显示指标线的值 SMA10:100 RSI:这些tips不显示
        kLineView.setShowTips(true);

        mainNormal = rootView.findViewById(R.id.mainNormal);
        subNormal = rootView.findViewById(R.id.subNormal);
        mainNormalView = rootView.findViewById(R.id.tab_SMA);
        subNormalView = rootView.findViewById(R.id.tab_MACD);
        mainNormalView.setSelected(true);
        subNormalView.setSelected(true);

        mainNormalViewLand = rootView.findViewById(R.id.tab_SMA_land);
        subNormalViewLand = rootView.findViewById(R.id.tab_MACD_land);
        mainNormalViewLand.setSelected(true);
        subNormalViewLand.setSelected(true);

        layoutContent = rootView.findViewById(R.id.layoutContent);
        layoutLoading = rootView.findViewById(R.id.layoutLoding);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //当前为横屏
            handLandView(true);
            kLineView.setMainF(lanMainF);
            kLineView.setSubF(lanSubF);
        } else {
            //切换到竖屏
            handLandView(false);
            kLineView.setMainF(mainF);
            kLineView.setSubF(subF);
        }
    }

    @Override
    protected void intListener() {
        rootView.findViewById(R.id.tab_SMA).setOnClickListener(normalListener);
        rootView.findViewById(R.id.tab_EMA).setOnClickListener(normalListener);
        rootView.findViewById(R.id.tab_BOLL).setOnClickListener(normalListener);
        rootView.findViewById(R.id.tab_MACD).setOnClickListener(normalListener);
        rootView.findViewById(R.id.tab_RSI).setOnClickListener(normalListener);
        rootView.findViewById(R.id.tab_KDJ).setOnClickListener(normalListener);
        rootView.findViewById(R.id.tab_SMA_land).setOnClickListener(normalListener);
        rootView.findViewById(R.id.tab_EMA_land).setOnClickListener(normalListener);
        rootView.findViewById(R.id.tab_BOLL_land).setOnClickListener(normalListener);
        rootView.findViewById(R.id.tab_MACD_land).setOnClickListener(normalListener);
        rootView.findViewById(R.id.tab_RSI_land).setOnClickListener(normalListener);
        rootView.findViewById(R.id.tab_KDJ_land).setOnClickListener(normalListener);

        //十字线出现的滑动逻辑
        kLineView.setOnKCrossLineMoveListener(this);
        //阻断touch事件的分发逻辑  listView headerView,这里还是用listview的onitemClick，touch太容易触发
        // kLineView.setOnKLineTouchDisableListener(this);
        kLineView.setOnKChartClickListener(this);
    }

    @Override
    protected void initData() {
        initBundleData();
        presenter = new ProductPresenterImpl(this);
        presenter.getKLineData(code, startDate, endDate, cycle, "DEMO");
        registerEventBus();
    }

    protected void initBundleData() {
        startDate = getArguments().getString("startDate");
        LogUtils.d("startDate:        " + startDate);
        endDate = getArguments().getString("endDate");
        LogUtils.d("endDate:        " + endDate);
        code = getArguments().getString("code");
        //一分钟，五分钟等的代号 ，一个 String 类型的数字
        cycle = getArguments().getString("interval");
        //精度
        digit = getArguments().getInt("digit");
    }

    /**
     * 规避横竖屏切换时候重新进入 onCreate 生命周期
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //当前为横屏
            handLandView(true);
            kLineView.setMainF(lanMainF);
            kLineView.setSubF(lanSubF);
        } else {
            //切换到竖屏
            handLandView(false);
            kLineView.setMainF(mainF);
            kLineView.setSubF(subF);
        }
    }

    /**
     * 根据横竖屏，切换显示不同的 view 和 状态
     *
     * @param isLand
     */
   private void handLandView(boolean isLand) {
        if (isLand) {
            kLineView.setAxisYmiddleHeight(KDisplayUtil.dip2px(getActivity(), 15));
            //隐藏竖屏的指标
            mainNormal.setVisibility(View.GONE);
            subNormal.setVisibility(View.GONE);
            //显示横屏指标
            if (landTypeView != null)
                landTypeView.setVisibility(View.VISIBLE);
        } else {
            kLineView.setAxisYmiddleHeight(KDisplayUtil.dip2px(getActivity(), 47));
            //显示竖屏的指标
            mainNormal.setVisibility(View.VISIBLE);
            subNormal.setVisibility(View.VISIBLE);
            //隐藏横屏指标
            if (landTypeView != null)
                landTypeView.setVisibility(View.GONE);
        }
    }

    /**
     * 判断是否为横屏状态
     *
     * @return
     */
    public boolean isLandScape() {
        return Configuration.ORIENTATION_LANDSCAPE == getActivity().getResources()
                .getConfiguration().orientation;
    }

    private void event4MACD() {
        kLineView.setSubLineData(KParseUtils.getMacdData(listData,
                KParamConfig.getMacdTParam1(getActivity()),
                KParamConfig.getMacdTParam2(getActivity()),
                KParamConfig.getMacdKParam(getActivity())));
        kLineView.setSubList(KParseUtils.getMacdStickDatas(listData,
                KParamConfig.getMacdTParam1(getActivity()),
                KParamConfig.getMacdTParam2(getActivity()),
                KParamConfig.getMacdKParam(getActivity())));
        kLineView.setSubNormal(KLineNormal.NORMAL_MACD);
    }

    private void event4KDJ() {
        kLineView.setSubLineData(KParseUtils.getKDJLinesDatas(listData,
                KParamConfig.getKdjKParam(getActivity())));
        kLineView.setSubNormal(KLineNormal.NORMAL_KDJ);
    }

    private void event4VOL() {
        kLineView.setSubNormal(KLineNormal.NORMAL_VOL);
    }

    private void event4RSI() {
        kLineView.setSubLineData(KParseUtils.getRsiLineDatas(listData,
                KParamConfig.getRsiParam1(getActivity()),
                KParamConfig.getRsiParam2(getActivity()),
                KParamConfig.getRsiParam3(getActivity())));
        kLineView.setSubNormal(KLineNormal.NORMAL_RSI);
    }


    private void event4SMA() {

        kLineView.setMainNormal(KLineNormal.NORMAL_SMA);
        if (Product_constans.PARAM_KLINE_5M_WEIPAN_new.equals(cycle)
                || Product_constans.PARAM_KLINE_5M.equals(cycle)
                || Product_constans.PARAM_KLINE_1M_WEIPAN_new.equals(cycle)) {

            List<KLineObj<KCandleObj>> smAcfg = KParamConfig.getSMAcfg(getActivity(), false);

            kLineView.setMainLineData(KParseUtils.getSMAData(listData, smAcfg));
        } else {
            kLineView.setMainLineData(KParseUtils.getSMAData(listData,
                    KParamConfig.getSMAcfg(getActivity(), true)));
        }

        isEvent4SMA = true;
    }

    private void event4BOLL() {
        kLineView.setMainNormal(KLineNormal.NORMAL_BOLL);
        kLineView.setMainLineData(KParseUtils.getBollData(listData,
                KParamConfig.getBoolTParam(getActivity()),
                KParamConfig.getBoolKParam(getActivity())));
    }

    private void event4EMA() {
        kLineView.setMainNormal(KLineNormal.NORMAL_EMA);
        kLineView.setMainLineData(KParseUtils.getEMAData(listData,
                KParamConfig.getEmaParam(getActivity())));
    }

    private View.OnClickListener normalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.tab_SMA) {
                event4SMA();
                mainNormalView.setSelected(false);
                mainNormalView = view;
                mainNormalView.setSelected(true);
                mainNormalViewLand.setSelected(false);
                mainNormalViewLand = rootView.findViewById(R.id.tab_SMA_land);
                mainNormalViewLand.setSelected(true);
            }
            if (id == R.id.tab_EMA) {
                event4EMA();
                mainNormalView.setSelected(false);
                mainNormalView = view;
                mainNormalView.setSelected(true);
                mainNormalViewLand.setSelected(false);
                mainNormalViewLand = rootView.findViewById(R.id.tab_EMA_land);
                mainNormalViewLand.setSelected(true);
            }
            if (id == R.id.tab_BOLL) {
                event4BOLL();
                mainNormalView.setSelected(false);
                mainNormalView = view;
                mainNormalView.setSelected(true);
                mainNormalViewLand.setSelected(false);
                mainNormalViewLand = rootView.findViewById(R.id.tab_BOLL_land);
                mainNormalViewLand.setSelected(true);
            }

            //附图
            if (id == R.id.tab_MACD) {
                event4MACD();
                subNormalView.setSelected(false);
                subNormalView = view;
                subNormalView.setSelected(true);
                subNormalViewLand.setSelected(false);
                subNormalViewLand = rootView.findViewById(R.id.tab_MACD_land);
                subNormalViewLand.setSelected(true);
            }
            if (id == R.id.tab_RSI) {
                event4RSI();
                subNormalView.setSelected(false);
                subNormalView = view;
                subNormalView.setSelected(true);
                subNormalViewLand.setSelected(false);
                subNormalViewLand = rootView.findViewById(R.id.tab_RSI_land);
                subNormalViewLand.setSelected(true);
            }
            if (id == R.id.tab_KDJ) {
                event4KDJ();
                subNormalView.setSelected(false);
                subNormalView = view;
                subNormalView.setSelected(true);
                subNormalViewLand.setSelected(false);
                subNormalViewLand = rootView.findViewById(R.id.tab_KDJ_land);
                subNormalViewLand.setSelected(true);
            }
            if (id == R.id.tab_SMA_land) {
                rootView.findViewById(R.id.tab_SMA).performClick();
            }
            if (id == R.id.tab_EMA_land) {
                rootView.findViewById(R.id.tab_EMA).performClick();
            }
            if (id == R.id.tab_BOLL_land) {
                rootView.findViewById(R.id.tab_BOLL).performClick();
            }
            if (id == R.id.tab_MACD_land) {
                rootView.findViewById(R.id.tab_MACD).performClick();
            }
            if (id == R.id.tab_RSI_land) {
                rootView.findViewById(R.id.tab_RSI).performClick();
            }
            if (id == R.id.tab_KDJ_land) {
                rootView.findViewById(R.id.tab_KDJ).performClick();
            }
        }
    };

    /**
     * 十字线 滑动显示对应的日期K线信息
     *
     * @param object
     */
    @Override
    public void onCrossLineMove(KCandleObj object) {
        try {
            if (crossInfoView == null)
                return;
            if (object == null)
                return;
            if (kLineView.getkCandleObjList() == null)
                return;
            //昨收价格为上一个k线的收盘价,如果是第一个k线就是open价
            double zuoClose = object.getClose();
            //获取到当前的位置
            int index = kLineView.getTouchIndex();
            //上一个k线的位置
            index = index - 1;
            if (index >= 0 && index < kLineView.getkCandleObjList().size()) {
                zuoClose = kLineView.getkCandleObjList().get(index).getClose();
            }

            tvTime.setText(object.getTime());

            //收盘价用白色标示，其他大于close 红色，小于绿色
//            tvClose.setText(ProFormatConfig.formatByCodes(type + "|" + code, object.getClose()));//KNumberUtil
//            tvOpen.setText(ProFormatConfig.formatByCodes(type + "|" + code, object.getOpen() + ""));
//            tvHigh.setText(ProFormatConfig.formatByCodes(type + "|" + code, object.getHigh() + ""));
//            tvLow.setText(ProFormatConfig.formatByCodes(type + "|" + code, object.getLow() + ""));

            tvClose.setText(object.getClose() + "");//KNumberUtil
            tvOpen.setText(object.getOpen() + "");
            tvHigh.setText(object.getHigh() + "");
            tvLow.setText(object.getLow() + "");
            //开盘价大于上一个k线的收盘价 红色
            if (object.getOpen() >= zuoClose) {
                tvOpen.setTextColor(getResources().getColor(R.color.color_opt_gt));
            } else {
                tvOpen.setTextColor(getResources().getColor(R.color.color_opt_lt));
            }
            //最高价大于上一个k线的收盘价 红色
            if (object.getHigh() >= object.getClose()) {
                tvHigh.setTextColor(getResources().getColor(R.color.color_opt_gt));
            } else {
                tvHigh.setTextColor(getResources().getColor(R.color.color_opt_lt));
            }
            //最低价大于上一个k线的收盘价 红色
            if (object.getLow() >= object.getClose()) {
                tvLow.setTextColor(getResources().getColor(R.color.color_opt_gt));
            } else {
                tvLow.setTextColor(getResources().getColor(R.color.color_opt_lt));
            }
            double rate = object.getClose() - zuoClose;
            //涨幅的计算  (当前k线的收盘价-上一个k线的收盘价)/上一个k线的收盘价*100%
            tvRate.setText(KNumberUtil.beautifulDouble(rate));

            String percent = KNumberUtil.beautifulDouble(NumberUtils.divide(NumberUtils.multiply(rate, 100), zuoClose, 4));
            tvRateChange.setText("" + percent + "%");
            if (rate >= 0) {
                tvRate.setTextColor(getResources().getColor(R.color.color_opt_gt));
                tvRateChange.setTextColor(getResources().getColor(R.color.color_opt_gt));
            } else {
                tvRate.setTextColor(getResources().getColor(R.color.color_opt_lt));
                tvRateChange.setTextColor(getResources().getColor(R.color.color_opt_lt));
            }

            if (!kLineView.isToucInLeftChart()) {
                //touch在右边，将信息放在左边
                crossInfoView.setGravity(Gravity.LEFT);
            } else {
                crossInfoView.setGravity(Gravity.RIGHT);
            }
            if (crossInfoView.getVisibility() != View.VISIBLE)
                crossInfoView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCrossLineHide() {
        if (crossInfoView == null)
            return;
        crossInfoView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void event() {

    }


    @Override
    public boolean onSingleClick() {
        return false;
    }

    @Override
    public boolean onDoubleClick() {
        //双击横屏竖屏切换
        if (Configuration.ORIENTATION_LANDSCAPE == getResources()
                .getConfiguration().orientation) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        return false;
    }

    @Override
    public boolean onLongPress() {
        return false;
    }

    @Override
    public void unsubscribe() {
        presenter.unsubscribe();
    }

    //时间周期标准
    Integer cycle_standard = Integer.valueOf(PARAM_KLINE_1D_WEIPAN_new);

    private List<KCandleObj> listData = new ArrayList<>();

    @Override
    public void getKLineDataSucessed(List<KLineData> result) {
        LogUtils.d("获取 K 线数据长度  ：" + result.size());
        Log.i(TAG, "获取 K 线数据长度  ：" + result.size());

        //如果没有这行代码，全屏时候回崩溃。
        if (!isAdded())
            return;

        //当前的时间周期
        Integer cycle_custom = Integer.valueOf(cycle);

        for (int i = 0; i < result.size(); i++) {
            KLineData kLineData = result.get(i);
            KCandleObj kCandleObj = new KCandleObj();
            //最高
            kCandleObj.setHigh(kLineData.getHigh());
            //最低
            kCandleObj.setLow(kLineData.getLow());
            //今开
            kCandleObj.setOpen(kLineData.getOpen());
            //昨收
            kCandleObj.setClose(kLineData.getClose());
//            long timeLong = kLineData.getTime() ;
            LogUtils.d("kLineData.getTime()  :  " + kLineData.getTime());
            long timeLong = kLineData.getTime() - 3 * 60 * 60 * 1000;
            //天之前的时间格式
            if (cycle_custom < cycle_standard) {
                simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
            } else {//以天为单位的时间格式
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            }

            String time_format = TimeUtils.millis2String(timeLong, simpleDateFormat);
            //long 类型时间
            kCandleObj.setTimeLong(timeLong);
            //格式化的时间字符串
            kCandleObj.setTime(time_format);
            LogUtils.d("K线 历史数据：  " + kCandleObj.toString());
            Log.i(TAG, "K线 历史数据：  : " + kCandleObj.toString());
            listData.add(kCandleObj);
        }

        if (layoutContent != null)
            layoutContent.setVisibility(View.VISIBLE);
        if (layoutLoading != null)
            layoutLoading.setVisibility(View.GONE);

        setData(listData);
    }

    @Override
    public void getKLineDataFailed(String errormsg) {
        LogUtils.d("获取 K 线数据失败   ：" + errormsg);
    }


    private void setData(List<KCandleObj> list) {
        kLineView.setkCandleObjList(list);
        //主图指标 ，默认为 SMA
        if (lastTopNorm == KLineNormal.NORMAL_SMA) {
            event4SMA();
        } else if (lastTopNorm == KLineNormal.NORMAL_EMA) {
            event4EMA();
        } else if (lastTopNorm == KLineNormal.NORMAL_BOLL) {
            event4BOLL();
        }
        //附图指标，根据历史记录设置 ，默认为 MACD
        if (lastBottomNorm == KLineNormal.NORMAL_VOL) {
            event4VOL();
        } else if (lastBottomNorm == KLineNormal.NORMAL_KDJ) {
            event4KDJ();
        } else if (lastBottomNorm == KLineNormal.NORMAL_RSI) {
            event4RSI();
        } else if (lastBottomNorm == KLineNormal.NORMAL_MACD) {
            event4MACD();
        }
        kLineView.postInvalidate();
    }

    /**
     * 刷新最后一个K线
     *
     * @param toAddendK
     */
    public void setLastKData(KCandleObj toAddendK) {
        //周线不好处理，直接不管
        if (Product_constans.PARAM_KLINE_WEEK_WEIPAN_new.equals(cycle)) {
            return;
        }

        if (toAddendK != null) {
            List<KCandleObj> list = kLineView.getkCandleObjList();
            if (list == null || list.size() == 0)
                return;
            //如果少于两条k线值
            if (list.size() < 2)
                return;
            KCandleObj lastK = list.get(list.size() - 1);

            //格式化显示的时间
            String formartT = formatDate(new Date(toAddendK.getTimeLong()), "MM-dd HH:mm");
            if (Product_constans.PARAM_KLINE_1D_WEIPAN_new.equals(cycle)) {
                //日线
                formartT = formatDate(new Date(toAddendK.getTimeLong()), "yyyy-MM-dd");
            }
            toAddendK.setTime(formartT);

            String toAddendKT = formatDate(new Date(toAddendK.getTimeLong()), "yyyy-MM-dd HH:mm:ss");
            String lastKT = formatDate(new Date(lastK.getTimeLong()), "yyyy-MM-dd HH:mm:ss");
            Log.v(TAG, "toAddendKT=" + toAddendKT + "  lastKT=" + lastKT + "  formartT=" + formartT);

            /**
             * 按照5分钟来算
             *
             * 出现的情况
             * 添加的时间是  17:03
             * 1、17:00  17:01  替换最后一个 -－>  17:00 17:03
             *
             * 添加的时间是  17:06
             * 2、17:00 17:04
             *
             * 第一个k线刚好是停盘之后的k线
             * 添加时间是 08:00
             */
            //刷新最后一个k线；1、替换最后一根k线 2、时间停留久了，添加一根新的k线
            boolean isReplaceLast = false;
            //k线传过来的最后一个k线就是当前没结束的周期k线，
            // 比如5分钟周期的，上一个是17:00，当前时间是17:01，那么传过来的是17:05的k线；传过来的时间就是 17:05的
            // 需要更新的和最后一个是同一个
            String strlastKT = formatDate(new Date(lastK.getTimeLong()), "yyyy-MM-dd HH:mm");
            String strtoAddendKT = formatDate(new Date(toAddendK.getTimeLong()), "yyyy-MM-dd HH:mm");
            if (Product_constans.PARAM_KLINE_1M_WEIPAN_new.equals(cycle)) {
                //分时图
                if (strlastKT.equals(strtoAddendKT)) {
                    Log.v(TAG, "remove");
                    isReplaceLast = true;
                    list.remove(lastK);
                }
            } else if (Product_constans.PARAM_KLINE_1D_WEIPAN_new.equals(cycle)) {
                //如果是日线直接替换
                isReplaceLast = true;
                list.remove(lastK);
            } else {
                if (Product_constans.cycleTMap.containsKey(cycle)) {
                    if (lastK.getTimeLong() - list.get(list.size() - 2).getTimeLong() >=
                            Product_constans.cycleTMap.get(cycle).longValue()
                            && toAddendK.getTimeLong() < lastK.getTimeLong()) {
                        isReplaceLast = true;
                        list.remove(lastK);
                    }
                }
            }

            long addT = 0;
            if (Product_constans.cycleTMap.containsKey(cycle)) {
                addT = Product_constans.cycleTMap.get(cycle).longValue();
            }

            //设置K线的开高低收
            if (isReplaceLast) {
                //如果是替换的话,
                // 开盘价就是 最后k线的开盘价，
                toAddendK.setOpen(lastK.getOpen());
                // 最高价就是最后k线和当前的价比较的较高价，字段close 接收的最新价
                toAddendK.setHigh(Math.max(lastK.getHigh(), toAddendK.getClose()));
                // 最低价就是最后k线和当前的价比较的较低价,字段close 接收的最新价
                toAddendK.setLow(Math.min(lastK.getLow(), toAddendK.getClose()));
                //收盘价就是最新价格

                //因为最后一个k线就是下一个k线周期的结束时间，如果是替换最后一个K线的话，时间设置成下一个k线的结束结时间
                toAddendK.setTime(lastK.getTime());
                toAddendK.setTimeLong(lastK.getTimeLong());
            } else {
                //新加的k线 都是最新价格
                toAddendK.setOpen(lastK.getClose());
                toAddendK.setHigh(lastK.getClose());
                toAddendK.setLow(lastK.getClose());

                //update at 2017-04-19
                //因为最后一个k线就是下一个k线周期的结束时间 新加的k线就要设置成 下一个周期的结束时间
                if (Product_constans.PARAM_KLINE_1M_WEIPAN_new.equals(cycle)) {
                    //直接使用刷新获取到的时间
                    String strT = formatDate(new Date(toAddendK.getTimeLong()), "MM-dd HH:mm");
                    toAddendK.setTime(strT);
                    toAddendK.setTimeLong(toAddendK.getTimeLong());
                } else {
                    //大于一分钟的  直接使用替换的时间对的上
                    String strT = formatDate(new Date(lastK.getTimeLong() + addT), "MM-dd HH:mm");
                    toAddendK.setTime(strT);
                    toAddendK.setTimeLong(lastK.getTimeLong() + addT);
                }
            }
            list.add(toAddendK);
            //上一次在最后的位置，或者是新加了一根k线还在最后位置，手动移动位置显示最新的k线
            if (kLineView.getDrawIndexEnd() == list.size() - 1 - 1
                    || kLineView.getDrawIndexEnd() == list.size() - 1) {
                Log.v(TAG, "getDrawIndexEnd() " + kLineView.getDrawIndexEnd() + " list.size()=" + list.size());
                //是最后一个
                kLineView.setDrawIndexEnd(list.size());
            }
            lastTopNorm = kLineView.getMainNormal();
            lastBottomNorm = kLineView.getSubNormal();
            setData(list);
        }

    }


    public String formatDate(Date aDate, String formatStr) {
        if (aDate == null)
            return "";
        formatter = new SimpleDateFormat(formatStr);
        return formatter.format(aDate);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetNettyDate(Quotation quotation) {
        //如果不是当前的品种代码，直接返回
        if (!code.equals(quotation.getSymbol())) {
            return;
        }
        if (kCandleObj_refresh != null) {
            kCandleObj_refresh = null;
        }
        if (listData == null || listData.size() <= 0) {
            return;
        }
        kCandleObj_refresh = new KCandleObj();

        Long unixTime = quotation.getUnixTime();
        long l = unixTime + 5 * 60 * 60 * 1000;
        //设置 long 时间
//        kCandleObj_refresh.setTimeLong(quotation.getUnixTime());
        kCandleObj_refresh.setTimeLong(l);
        //最新买入价
        double ask = quotation.getAsk();
        kCandleObj_refresh.setHigh(ask);
        kCandleObj_refresh.setLow(ask);
        kCandleObj_refresh.setOpen(ask);
        kCandleObj_refresh.setClose(ask);
        String time = TimeUtils.millis2String(l, simpleDateFormat);
        kCandleObj_refresh.setTime(quotation.getTime());
        kCandleObj_refresh.setTime(time);
        //获取 socket 数据之前，是否绘制完毕
        if (isEvent4SMA) {
            //刷新数据
            setLastKData(kCandleObj_refresh);
        }
    }

}
