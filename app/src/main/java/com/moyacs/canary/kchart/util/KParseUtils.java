package com.moyacs.canary.kchart.util;

import android.graphics.Color;
import android.util.Log;


import com.moyacs.canary.kchart.entity.KCandleObj;
import com.moyacs.canary.kchart.entity.KLineObj;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * BOOL
 * （1）计算MA MA=N日内的收盘价之和÷N 
 * （2）计算标准差MD MD=平方根N日的（C－MA）的两次方之和除以N 
 * （3）计算MB、UP、DN线 MB=（N－1）日的MA UP=MB+k×MD DN=MB－k×MD
 *
 * SMA
 * SMA(C,N,M) = (M*C+(N-M)*Y')/N C=今天收盘价－昨天收盘价
 * N＝就是周期比如 6或者12或者24， M＝权重，其实就是1
 *
 * EMA
 * EMA(c,n) ema=2 * (c - y') / (n + 1) + y'; 其中Y’表示上一周期的Y值
 *
 * MACD
 * DIFF:=EMA(CLOSE,SHORT) - EMA(CLOSE,LONG); DEA:=EMA(DIFF,M);
 * 最后画macd  可能用权重乘以一个固定的值  具体以代码为准
 *
 * RSI
 * SMA(C,N,M) = (M*C+(N-M)*Y')/N
 *  LC := REF(CLOSE,1); 
 * RSI$1:SMA(MAX(CLOSE-LC,0),N1,1)/SMA(ABS(CLOSE-LC),N1,1)*100;
 *
 * KDJ
 * kdj 9,3,3 
 * N:=9; P1:=3; P2:=3; 
 * RSV:=(CLOSE-LLV(LOW,N))/(HHV(HIGH,N)-LLV(LOW,N))*100;
 *  K:SMA(RSV,P1,1);
 *  D:SMA(K,P2,1); 
 * J:3*K-2*D;
 */
public class KParseUtils {

    public static final String TAG = "KParseUtils ";

    /**
     * @param datas 传入的数据集合，要求是升序排列
     *              <p>
     *              （1）计算MA
     *              MA=N日内的收盘价之和÷N
     *              （2）计算标准差MD
     *              MD=平方根N日的（C－MA）的两次方之和除以N
     *              （3）计算MB、UP、DN线
     *              MB=（N－1）日的MA
     *              UP=MB+k×MD
     *              DN=MB－k×MD
     *              （K为参数，可根据股票的特性来做相应的调整，一般默认为2）
     * @param n     boll T
     * @param k     boll K
     * @return
     */
    public static List<KLineObj<KCandleObj>> getBollData(List<KCandleObj> datas, int n, int k) {
        if (n < 1 || k < 1)
            return null;
        if (datas == null || datas.isEmpty())
            return null;
        if (n > datas.size())
            return null;

        List<KLineObj<KCandleObj>> linesDatas = new ArrayList<KLineObj<KCandleObj>>();
        List<KCandleObj> zhongList = new ArrayList<KCandleObj>();
        List<KCandleObj> shangList = new ArrayList<KCandleObj>();
        List<KCandleObj> xiaList = new ArrayList<KCandleObj>();

        KCandleObj zhongEntity;
        KCandleObj shangEntity;
        KCandleObj xiaEntity;

        double standtard = 0;
        double squarSum = 0;

        int cycle = n;

        List<KCandleObj> sma = countSma4Boll(datas, n);
        if (sma == null || sma.size() == 0)
            return null;
        for (int i = cycle - 1; i < datas.size(); i++) {
            double smaValue = sma.get(i - n + 1).getNormValue();

            standtard = 0;

            for (int j = i - n + 1; j <= i; j++) {
                standtard += (datas.get(j).getClose() - smaValue)
                        * (datas.get(j).getClose() - smaValue);
            }
            squarSum = Math.sqrt(standtard / n);

            zhongEntity = new KCandleObj(smaValue);
            shangEntity = new KCandleObj(smaValue + squarSum * k);
            xiaEntity = new KCandleObj(smaValue - squarSum * k);
            zhongList.add(zhongEntity);
            xiaList.add(xiaEntity);
            shangList.add(shangEntity);
        }
        Log.v(TAG, "datas =" + datas.size());
        Log.v(TAG, "zhongList 11=" + zhongList.size());
        //确保和 传入的list size一致，
        int size = datas.size() - shangList.size();
        for (int i = 0; i < size; i++) {
            shangList.add(0, new KCandleObj());
            zhongList.add(0, new KCandleObj());
            xiaList.add(0, new KCandleObj());
        }
        Log.v(TAG, "zhongList 22=" + zhongList.size());
        KLineObj<KCandleObj> shangLine = new KLineObj<KCandleObj>();
        shangLine.setDisplay(true);
        shangLine.setLineColor(Color.parseColor("#feb705"));
        shangLine.setLineData(shangList);
        shangLine.setTitle("UPPER");//Boll 上轨
        KLineObj<KCandleObj> zhongLine = new KLineObj<KCandleObj>();
        zhongLine.setDisplay(true);
        zhongLine.setLineColor(Color.parseColor("#fe4a87"));
        zhongLine.setLineData(zhongList);
        zhongLine.setTitle("MID");//Boll 中轨
        KLineObj<KCandleObj> xiaLine = new KLineObj<KCandleObj>();
        xiaLine.setDisplay(true);
        xiaLine.setLineColor(Color.parseColor("#00f4a7"));
        xiaLine.setLineData(xiaList);
        xiaLine.setTitle("LOWER");//Boll 下轨
        linesDatas.add(shangLine);
        linesDatas.add(zhongLine);
        linesDatas.add(xiaLine);
        return linesDatas;
    }


    /***
     * @param list
     * @param days
     */
    public static List<KCandleObj> initSma(List<KCandleObj> list, int days) {
        if (days < 1) {
            return null;
        }
        if (list == null || list.size() == 0)
            return null;
        int cycle = days;

        if (cycle < 20)
            cycle = 20;
        if (cycle > list.size()) {
            //设置的指标参数大于数据集合 不用计算
            return null;
        }

        double sum = 0;

        List<KCandleObj> ma5Values = new ArrayList<KCandleObj>();

        int index = 1;
        for (int i = cycle - 1; i < list.size(); i++) {
            if (i == cycle - 1) {
                for (int j = i - days + 1; j <= i; j++) {
                    sum += list.get(j).getClose();
                    ma5Values.add(new KCandleObj(sum / index));
                    index++;
                }
            } else {
                sum = sum + list.get(i).getClose()
                        - list.get(i - days).getClose();
            }
            ma5Values.add(new KCandleObj(sum / days));

        }

        //确保和 传入的list size一致，
        int size = list.size() - ma5Values.size();
        for (int i = 0; i < size; i++) {
            ma5Values.add(0, new KCandleObj());
        }
        return ma5Values;
    }

    //上面直接赋值给sma 需要确保和 传入的list size一致，但是boll算法的时候不要
    public static List<KCandleObj> countSma4Boll(List<KCandleObj> list, int days) {
        if (days < 1) {
            return null;
        }
        if (list == null || list.size() == 0)
            return null;
        int cycle = days;

        if (cycle < 20)
            cycle = 20;
        if (cycle > list.size()) {
            //设置的指标参数大于数据集合 不用计算
            return null;
        }

        double sum = 0;

        List<KCandleObj> ma5Values = new ArrayList<KCandleObj>();

        for (int i = cycle - 1; i < list.size(); i++) {
            if (i == cycle - 1) {
                for (int j = i - days + 1; j <= i; j++) {
                    sum += list.get(j).getClose();
                }
            } else {
                sum = sum + list.get(i).getClose()
                        - list.get(i - days).getClose();
            }
            ma5Values.add(new KCandleObj(sum / days));

        }

        return ma5Values;
    }


    /**
     * @param list
     * @param params 配置的均线的颜色 title 周期
     * @return
     */

    public static List<KLineObj<KCandleObj>> getSMAData(
            List<KCandleObj> list, List<KLineObj<KCandleObj>> params) {
        List<KLineObj<KCandleObj>> lineDatas = new ArrayList<KLineObj<KCandleObj>>();
        if (params == null)
            return null;
        for (KLineObj<KCandleObj> item : params) {
            if (item == null)
                continue;
            if (item.getValue() <= 0)
                continue;
            if (item.getLineColor() == 0)
                continue;
            List<KCandleObj> maList = initSma(list, (int) item.getValue());
            if (maList != null && maList.size() > 0) {
                item.setLineData(maList);
                lineDatas.add(item);
            }
        }
//        KLineObj<KCandleObj> lineMa1 = new KLineObj<KCandleObj>();
//        lineMa1.setTitle("SMA" + ma1);
//        lineMa1.setLineColor(Color.parseColor("#00f4a7"));
//        List<KCandleObj> ma1List = initSma(list, ma1);
//        if (ma1List != null) {
//            lineMa1.setLineData(ma1List);
//            lineDatas.add(lineMa1);
//        }
//
//        KLineObj<KCandleObj> lineMa2 = new KLineObj<KCandleObj>();
//        lineMa2.setTitle("SMA" + ma2);
//        lineMa2.setLineColor(Color.parseColor("#fe4a87"));
//        List<KCandleObj> ma2List = initSma(list, ma2);
//        if (ma2List != null) {
//            lineMa2.setLineData(ma2List);
//            lineDatas.add(lineMa2);
//        }
//
//        KLineObj<KCandleObj> lineMa3 = new KLineObj<KCandleObj>();
//        lineMa3.setTitle("SMA" + ma3);
//        lineMa3.setLineColor(Color.parseColor("#feb705"));
//        List<KCandleObj> ma3List = initSma(list, ma3);
//        if (ma3List != null) {
//            lineMa3.setLineData(ma3List);
//            lineDatas.add(lineMa3);
//        }

        return lineDatas;
    }

    /**
     * @param list
     * @param n
     * @return
     */
    public static List<KLineObj<KCandleObj>> getEMAData(
            List<KCandleObj> list, int n) {
        List<KLineObj<KCandleObj>> lineDatas = new ArrayList<KLineObj<KCandleObj>>();

        lineDatas.add(getEMAData(list, n, Color.RED));
        return lineDatas;
    }

    /**
     * EMA(c,n)
     * ema=2 * (c - y') / (n + 1) + y';
     * 其中Y’表示上一周期的Y值
     *
     * @param list
     * @param n
     * @return
     */
    private static KLineObj<KCandleObj> getEMAData(
            List<KCandleObj> list, int n, int lineColor) {
        KLineObj<KCandleObj> line = new KLineObj<KCandleObj>();

        if (list == null || list.size() == 0)
            return null;

        int cycle = n;
        if (cycle < 20)
            cycle = 20;
        if (n > list.size()) {
            return null;
        }

        List<KCandleObj> datas = new ArrayList<KCandleObj>();
        double lastEma = list.get(0).getClose();
        for (int i = 1; i < list.size(); i++) {
            if (i <= cycle - 1) {
                double currentEma = 2 * (list.get(i).getClose() - lastEma)
                        / (n + 1) + lastEma;
                lastEma = currentEma;
                datas.add(new KCandleObj(lastEma));
                continue;
            } else {
                lastEma = 2 * (list.get(i).getClose() - lastEma) / (n + 1)
                        + lastEma;
                datas.add(new KCandleObj(lastEma));
            }
        }
        //确保和 传入的list size一致，
        int size = list.size() - datas.size();
        for (int i = 0; i < size; i++) {
            datas.add(0, new KCandleObj());
        }

        line.setLineColor(lineColor);
        line.setLineData(datas);
        line.setTitle("EMA" + n);
        return line;
    }

    /**
     * @param list
     * @param n
     * @return
     */
    public static List<KCandleObj> getEmaValueByIndex(List<KCandleObj> list,
                                                      int n) {

        List<KCandleObj> lines = new ArrayList<KCandleObj>();
        if (list == null || list.size() == 0)
            return lines;
        double lastEma = list.get(0).getClose();
        lines.add(new KCandleObj(lastEma));
        for (int i = 1; i < list.size(); i++) {
            double currentEma = 2 * (list.get(i).getClose() - lastEma)
                    / (n + 1) + lastEma;
            lastEma = currentEma;
            lines.add(new KCandleObj(lastEma));
        }
        return lines;
    }

    private static List<KCandleObj> getDifDatas(List<KCandleObj> list, int x,
                                                int y) {
        if (list == null || list.size() == 0)
            return null;
        int cycle = Math.max(x, y);
        if (cycle < 20)
            cycle = 20;

        if (cycle > list.size()) {
            return null;
        }
        List<KCandleObj> listX = getEmaValueByIndex(list, x);
        List<KCandleObj> listY = getEmaValueByIndex(list, y);

        List<KCandleObj> difs = new ArrayList<KCandleObj>();
        int start = cycle;
        if (start < 1)
            start = 1;
        start--;

        for (int i = 1; i < list.size(); i++) {
            difs.add(new KCandleObj(listX.get(i).getNormValue()
                    - listY.get(i).getNormValue()));
        }
        return difs;
    }

    /**
     * @param list
     * @param z
     * @return
     */
    private static List<KCandleObj> getDeaDatas(List<KCandleObj> list, int x,
                                                int y, int z) {

        List<KCandleObj> difDatas = getDifDatas(list, x, y);
        if (difDatas == null || difDatas.isEmpty())
            return null;

        List<KCandleObj> datas = new ArrayList<KCandleObj>();
        double lastEma = difDatas.get(0).getNormValue();
        double lastDEA = 0;
        for (int i = 1; i < difDatas.size(); i++) {
            if (i < z) {
                double currentEma = 2
                        * (difDatas.get(i).getNormValue() - lastEma) / (z + 1)
                        + lastEma;
                lastEma = currentEma;
                if (i == z - 1)
                    lastDEA = lastEma;
                continue;
            } else {
                lastDEA = (lastDEA * (z - 1) / (z + 1))
                        + (difDatas.get(i).getNormValue() * 2 / (z + 1));
                datas.add(new KCandleObj(lastDEA));
            }
        }
        return datas;
    }

    /**
     * DIFF:=EMA(CLOSE,SHORT) - EMA(CLOSE,LONG);
     * DEA:=EMA(DIFF,M);
     *
     * @param list
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static List<KLineObj<KCandleObj>> getMacdData(
            List<KCandleObj> list, int x, int y, int z) {
        if (list == null || list.size() == 0)
            return null;
        List<KLineObj<KCandleObj>> lineDatas = new ArrayList<KLineObj<KCandleObj>>();

        try {
            List<KCandleObj> difData = getDifDatas(list, x, y);
            List<KCandleObj> deaData = getDeaDatas(list, x, y, z);

            if (difData == null || difData.size() == 0)
                return null;
            if (deaData == null || deaData.size() == 0)
                return null;

            //确保和 传入的list size一致，
            int size = list.size() - difData.size();
            for (int i = 0; i < size; i++) {
                difData.add(0, new KCandleObj());
            }

            //确保和 传入的list size一致，
            int sizeDea = list.size() - deaData.size();
            for (int i = 0; i < sizeDea; i++) {
                deaData.add(0, new KCandleObj());
            }
            KLineObj<KCandleObj> difLine = new KLineObj<KCandleObj>();
            difLine.setTitle("DIF");
            difLine.setLineData(difData);
            difLine.setValue(y);
            difLine.setLineColor(Color.parseColor("#00f4a7"));
            lineDatas.add(difLine);
            KLineObj<KCandleObj> deaLine = new KLineObj<KCandleObj>();
            deaLine.setTitle("DEA");
            deaLine.setLineData(deaData);
            deaLine.setValue(z);
            deaLine.setLineColor(Color.parseColor("#fe4a87"));
            lineDatas.add(deaLine);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lineDatas;
    }

    public static List<KCandleObj> getMacdStickDatas(List<KCandleObj> list,
                                                     int x, int y, int z) {
        List<KCandleObj> difList = getDifDatas(list, x, y);

        List<KCandleObj> deaList = getDeaDatas(list, x, y, z);

        List<KCandleObj> stickData = new ArrayList<KCandleObj>();

        if (difList == null || difList.size() == 0)
            return null;
        if (deaList == null || deaList.size() == 0)
            return null;

        double macd = 0;

        if (deaList != null && !deaList.isEmpty()) {
            for (int i = 0; i < deaList.size(); i++) {
                if (i + z >= difList.size()) {
                    break;
                }
                macd = difList.get(i + z).getNormValue()
                        - deaList.get(i).getNormValue();
                macd *= 2;
                if (macd > 0) {
                    KCandleObj kCandleObj = new KCandleObj();
                    kCandleObj.setHigh(macd);
                    stickData.add(kCandleObj);
                } else {
                    KCandleObj kCandleObj = new KCandleObj();
                    kCandleObj.setLow(macd);
                    stickData.add(kCandleObj);
                }
            }
        }

        //确保和 传入的list size一致，
        int size = list.size() - stickData.size();
        for (int i = 0; i < size; i++) {
            stickData.add(0, new KCandleObj());
        }
        return stickData;
    }

    /**
     * LLV  n周期内的最低值 取low字段
     *
     * @param list
     * @param n
     * @return
     */
    public static List<KCandleObj> LLV(List<KCandleObj> list, int n) {
        if (list == null || list.size() == 0)
            return null;
        List<KCandleObj> datas = new ArrayList<>();

        double minValue = 0;
        for (int i = n - 1; i < list.size(); i++) {
            for (int j = i - n + 1; j <= i; j++) {
                if (j == i - n + 1) {
                    minValue = list.get(j).getLow();
                } else {
                    minValue = Math.min(minValue, list.get(j).getLow());
                }
            }
            datas.add(new KCandleObj(minValue));
        }

        return datas;
    }

    /**
     * HHV n周期内的最大值 取high字段
     *
     * @param list
     * @param n
     * @return
     */
    public static List<KCandleObj> HHV(List<KCandleObj> list, int n) {
        if (list == null || list.size() == 0)
            return null;
        List<KCandleObj> datas = new ArrayList<>();

        double maxValue = list.get(0).getHigh();
        for (int i = n - 1; i < list.size(); i++) {
            for (int j = i - n + 1; j <= i; j++) {
                if (j == i - n + 1) {
                    maxValue = list.get(j).getHigh();
                } else {
                    maxValue = Math.max(maxValue, list.get(j).getHigh());
                }
            }
            datas.add(new KCandleObj(maxValue));
        }
        return datas;
    }

    /**
     * kdj 9,3,3
     * N:=9; P1:=3; P2:=3;
     * RSV:=(CLOSE-LLV(LOW,N))/(HHV(HIGH,N)-LLV(LOW,N))*100;
     * K:SMA(RSV,P1,1);
     * D:SMA(K,P2,1);
     * J:3*K-2*D;
     *
     * @param list
     * @param n
     * @return
     */
    public static List<KLineObj<KCandleObj>> getKDJLinesDatas(
            List<KCandleObj> list, int n) {
        if (list == null || list.size() == 0)
            return null;
        int cycle = n;
        if (n > list.size()) {
            return null;
        }

        List<KCandleObj> kValue = new ArrayList<KCandleObj>();
        List<KCandleObj> dValue = new ArrayList<KCandleObj>();
        List<KCandleObj> jValue = new ArrayList<KCandleObj>();

        List<KCandleObj> maxs = HHV(list, n);
        List<KCandleObj> mins = LLV(list, n);
        if (maxs == null || mins == null)
            return null;
        //确保和 传入的list size一致，
        int size = list.size() - maxs.size();
        for (int i = 0; i < size; i++) {
            maxs.add(0, new KCandleObj());
            mins.add(0, new KCandleObj());
        }
        double rsv = 0;
        double lastK = 50;
        double lastD = 50;
        for (int i = cycle - 1; i < list.size(); i++) {
            if (i >= maxs.size())
                break;
            if (i >= mins.size())
                break;
            double div = maxs.get(i).getNormValue() -mins.get(i).getNormValue();
            if (div == 0) {
               //使用上一次的
            } else {
                rsv = ((list.get(i).getClose() - mins.get(i).getNormValue())
                        / (div)) * 100;
            }

            double k = countSMA(rsv, 3, 1, lastK);

            try {
                BigDecimal big = new BigDecimal(k);
            } catch (Exception e) {
                e.printStackTrace();
            }
            double d = countSMA(k, 3, 1, lastD);
            double j = 3 * k - 2 * d;
            lastK = k;
            lastD = d;
            kValue.add(new KCandleObj(k));
            dValue.add(new KCandleObj(d));
            jValue.add(new KCandleObj(j));
        }


        //确保和 传入的list size一致，
        size = list.size() - kValue.size();
        for (int i = 0; i < size; i++) {
            kValue.add(0, new KCandleObj());
            dValue.add(0, new KCandleObj());
            jValue.add(0, new KCandleObj());
        }

        List<KLineObj<KCandleObj>> lineDatas = new ArrayList<KLineObj<KCandleObj>>();

        KLineObj<KCandleObj> kLine = new KLineObj<KCandleObj>();
        kLine.setLineData(kValue);
        kLine.setTitle("K");
        kLine.setLineColor(Color.parseColor("#00f4a7"));
        lineDatas.add(kLine);

        KLineObj<KCandleObj> dLine = new KLineObj<KCandleObj>();
        dLine.setLineData(dValue);
        dLine.setTitle("D");
        dLine.setLineColor(Color.parseColor("#fe4a87"));
        lineDatas.add(dLine);

        KLineObj<KCandleObj> jLine = new KLineObj<KCandleObj>();
        jLine.setLineData(jValue);
        jLine.setTitle("J");
        jLine.setLineColor(Color.parseColor("#feb705"));
        lineDatas.add(jLine);

        return lineDatas;
    }

    /**
     * SMA(C,N,M) = (M*C+(N-M)*Y')/N
     * C=今天收盘价－昨天收盘价    N＝就是周期比如 6或者12或者24， M＝权重，其实就是1
     *
     * @param c   今天收盘价－昨天收盘价
     * @param n   周期
     * @param m   1
     * @param sma 上一个周期的sma
     * @return
     */
    public static double countSMA(double c, double n, double m, double sma) {
        return (m * c + (n - m) * sma) / n;
    }

    /**
     * SMA(C,N,M) = (M*C+(N-M)*Y')/N
     * LC := REF(CLOSE,1);
     * RSI$1:SMA(MAX(CLOSE-LC,0),N1,1)/SMA(ABS(CLOSE-LC),N1,1)*100;
     */
    public static List<KCandleObj> countRSIdatas(List<KCandleObj> list, int days) {
        List<KCandleObj> rsiList = new ArrayList<KCandleObj>();
        if (list == null)
            return null;
        if (days > list.size())
            return null;

        double smaMax = 0, smaAbs = 0;//默认0
        double lc = 0;//默认0
        double close = 0;
        double rsi = 0;
        for (int i = 1; i < list.size(); i++) {
            KCandleObj entity = list.get(i);
            lc = list.get(i - 1).getClose();
            close = entity.getClose();
            smaMax = countSMA(Math.max(close - lc, 0d), days, 1, smaMax);
            smaAbs = countSMA(Math.abs(close - lc), days, 1, smaAbs);
            rsi = smaMax / smaAbs * 100;
            rsiList.add(new KCandleObj(rsi));
        }
        Log.v(TAG, "rsiList.size()=" + rsiList.size());

        int size = list.size() - rsiList.size();
        for (int i = 0; i < size; i++) {
            rsiList.add(0, new KCandleObj());
        }
        return rsiList;
    }


    /**
     * @param list
     * @param x
     * @param y
     * @param z
     * @return
     */

    public static List<KLineObj<KCandleObj>> getRsiLineDatas(
            List<KCandleObj> list, int x, int y, int z) {
        List<KLineObj<KCandleObj>> lineDatas = new ArrayList<KLineObj<KCandleObj>>();

        KLineObj<KCandleObj> xLine = new KLineObj<KCandleObj>();
        xLine.setLineColor(Color.parseColor("#00f4a7"));
        xLine.setTitle("RSI" + x);
//        xLine.setLineData(initRSIdatas(list, x));
        xLine.setLineData(countRSIdatas(list, x));
        lineDatas.add(xLine);

        KLineObj<KCandleObj> yLine = new KLineObj<KCandleObj>();
        yLine.setLineColor(Color.parseColor("#fe4a87"));
        yLine.setTitle("RSI" + y);
//        yLine.setLineData(initRSIdatas(list, y));
        yLine.setLineData(countRSIdatas(list, y));
        lineDatas.add(yLine);

        KLineObj<KCandleObj> zLine = new KLineObj<KCandleObj>();
        zLine.setLineColor(Color.parseColor("#feb705"));
        zLine.setTitle("RSI" + z);
//        zLine.setLineData(initRSIdatas(list, z));
        zLine.setLineData(countRSIdatas(list, z));
        lineDatas.add(zLine);

        return lineDatas;

    }


}
