package com.moyacs.canary.product_fxbtg.contract_kline;

import com.moyacs.canary.base.BaseModul;
import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseRequestListener;
import com.moyacs.canary.base.BaseView;
import com.moyacs.canary.network.HttpResult;
import com.moyacs.canary.product_fxbtg.net_kline.KLineData;

import java.util.List;


/**
 * 作者：luoshen on 2018/3/26 0026 13:14
 * 邮箱：rsf411613593@gmail.com
 * 说明：获取 K 线数据封装的 接口
 */

public class ProductContract {

    public interface ProductView extends BaseView {
        /**
         * 获取成功
         *
         * @param result
         */
        void getKLineDataSucessed(List<KLineData> result);

        /**
         * 获取失败
         *
         * @param errormsg
         */
        void getKLineDataFailed(String errormsg);


    }

    public interface ProductPresenter extends BasePresenter {
        /**
         * 获取 K 线数据
         *
         * @param symbol    品种代码
         * @param startDate 开始时间
         * @param endDate   结束时间
         * @param period    数据间隔，以分钟为单位，
         */
        void getKLineData(String symbol,
                          String startDate,
                          String endDate,
                          String period,
                          String server);
    }

    public interface ProductModul extends BaseModul {
        /**
         * 获取 K 线数据
         *
         * @param symbol    品种代码
         * @param startDate 开始时间
         * @param endDate   结束时间
         * @param period    数据间隔，以分钟为单位，
         */
        void getKLineData(String symbol,
                          String startDate,
                          String endDate,
                          String period,
                          String server);
    }

    public interface ProductKLineRequestListener extends BaseRequestListener {

        /**
         * 请求成功
         *
         * @param result
         */
        void getKLineDataResponseSucessed(HttpResult<List<KLineData>> result);

        /**
         * 请求失败
         *
         * @param errormsg
         */
        void getKLineDataResponseFailed(String errormsg);

    }
}
