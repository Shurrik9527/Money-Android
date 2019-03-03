package com.moyacs.canary.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import com.moyacs.canary.common.PreferenceSetting;
import com.ta.utdid2.device.UTDevice;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static final String TAG = "StringUtil";
    //之前是16太长
    public static final int MAX_LENGTH_nikName = 15;
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS");

    public static boolean isNickName(Context context, String nikName) {
        if (StringUtil.isEmpty(nikName)) {
            ToastUtils.showShort("请输入昵称");
            return false;
        }

        if (getNickLength(nikName) > MAX_LENGTH_nikName) {
            ToastUtils.showShort("昵称过长");
            return false;
        }
        return true;
    }

    /**
     * 获取字符串的长度，对双字符（包括汉字）按两位计数
     *
     * @param value
     * @return
     */
    public static int getNickLength(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    private static boolean matcher(String reg, String string) {
        boolean tem = false;
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(string);
        tem = matcher.matches();
        return tem;
    }


    public static boolean isEmpty(String str) {
        if (null == str)
            return true;
        if (str.trim().length() == 0)
            return true;
        if (str.trim().equalsIgnoreCase("null"))
            return true;
        return false;
    }

    /**
     * 后去隐藏的点换号码
     *
     * @param phone
     * @return 135****7877
     */
    public static String getHintPhone(String phone) {
        if (isEmpty(phone))
            return "";
        if (!isMobile(phone))
            return phone;
        String pho = phone.substring(0, 3) + "****" + phone.substring(7, 11);
        return pho;
    }

    /**
     * 隐藏卡号
     * @param cardNo
     * @return
     */
    public static String getHintCardNo(String cardNo) {
        if (isEmpty(cardNo))
            return "";

        String no = cardNo.substring(0, 3) + " ******** " + cardNo.substring(cardNo.length()-4, cardNo.length());
        return no;
    }

    /**
     * 获取字符串中第一串数字
     *
     * @param string
     * @return
     */
    public static String matcherNumber(String string) {
        if (string == null)
            return "";
        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher(string);
        while (m.find()) {
            String s = m.group(1);
//            if (s.length() == AppSetting.VAilCODE_LENGTH)
//                return s;
            return s;
        }
        return "";
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        try {
            Pattern p = null;
            Matcher m = null;
            boolean b = false;
//            p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号11位
            p = Pattern.compile("^[1][0-9]{10}$"); // 验证手机号11位
            m = p.matcher(str);
            b = m.matches();
            return b;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 手机号中间显示xxxx
     *
     * @param phone
     * @return
     */
    public static String delPhone(String phone, String repleace) {
        try {
            Pattern p = Pattern.compile("[1][0-9]{10}");
            Matcher m = p.matcher(phone);
            while (m.find()) {
                String phonStr = m.group();
                StringBuilder rel = new StringBuilder();
                rel.append(phonStr.substring(0, 3));
                for (int i = 0; i < 4; i++) {
                    rel.append(repleace);
                }
                rel.append(phonStr.substring(7, 11));
                phone = phone.replace(phonStr, rel.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phone;
    }

    /**
     * 电话号码验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isHomePhone(String str) {
        try {
            Pattern p1 = null, p2 = null;
            Matcher m = null;
            boolean b = false;
            p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
            p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
            if (str.length() > 9) {
                m = p1.matcher(str);
                b = m.matches();
            } else {
                m = p2.matcher(str);
                b = m.matches();
            }
            return b;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 密码是否合格
     * 字母或数子 或字母和组合组成 大于6位
     *
     * @param password
     * @return
     */
    public static boolean isPassWord(String password) {
        if (password == null)
            return false;
        String str = "[a-zA-Z0-9]{6,12}";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(password);
        return m.matches();
    }


    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 去掉影响居中的标签
     *
     * @param source
     * @return
     */
    public static String moveCenterTag(String source) {
        if (source == null)
            return null;
//        source = replace(source, "<p>", "");
        source = moveTag(source, "p");
        source = replace(source, "</p>", "");
//        source = replace(source, "<div>", "");
        source = moveTag(source, "div");
        source = replace(source, "</div>", "");
        source = moveTag(source, "span");
        source = replace(source, "</span>", "");
        source = replace(source, "</br>", "");
        source = replace(source, "<br>", "");
        source = replace(source, "<o:p>", "");
        source = replace(source, "</o:p>", "");
        source = replace(source, "\n", "");
        return source;
    }

    /**
     * 替换<a></>标签
     *
     * @param source
     * @return
     */
    public static String moveTagA(String source) {
        if (source == null)
            return null;
        Pattern p = Pattern.compile("<a[^>]*>");//<a>
        Matcher m = p.matcher(source);
        while (m.find()) {
            String find = m.group();
            source = source.replace(find, "");
        }
        source = replace(source, "</a>", "");
        return source;
    }

    /**
     * 替换指定标签标签
     *
     * @param source
     * @param tag
     * @return
     */
    public static String moveTag(String source, String tag) {
        if (tag == null)
            return source;
        if (source == null)
            return null;
        String reg = "<" + tag + "[^>]*>";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(source);
        while (m.find()) {
            String find = m.group();
            source = source.replace(find, "");
        }
        source = replace(source, "</" + tag + ">", "");
        return source;
    }

    public static String replace(String source, String regex, String replacement) {
        int index = -1;
        StringBuffer buffer = new StringBuffer();
        while ((index = source.indexOf(regex)) >= 0) {
            buffer.append(source.substring(0, index));
            buffer.append(replacement);
            source = source.substring(index + regex.length());
        }
        buffer.append(source);
        return buffer.toString();
    }


    public static String urlEncode(String obj) {
        return urlEncode(obj, "GBK");
    }

    public static String urlEncode(String obj, String charset) {
        String result = null;
        if (obj != null) {
            try {
                result = URLEncoder.encode(obj, charset);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
                return result;
            }
        }
        return result;
    }

    public static String urlDecode(String obj) {
        return urlDecode(obj, "GBK");
    }

    public static String urlDecode(String obj, String charset) {
        String result = null;
        if (obj != null) {
            try {
                result = URLDecoder.decode(obj, charset);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
                return result;
            }
        }
        return result;
    }


    public static int numberOfStr(String str, String con) {
        str = " " + str;
        if (str.endsWith(con)) {
            return str.split(con).length;
        } else {
            return str.split(con).length - 1;
        }
    }


    public static String md5(String str) {
        StringBuffer buf = new StringBuffer("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[] = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return buf.toString();
    }

    public static String getDomain(String url) {
        try {
            URI uri = new URI(url);
            return uri.getHost();
        } catch (URISyntaxException e) {
            return null;
        }
    }


    public static String bSubstring(String s, int length) {
        try {
            byte[] bytes = s.getBytes("Unicode");
            int n = 0;
            int i = 2;
            for (; i < bytes.length && n < length; i++) {
                if (i % 2 == 1) {
                    n++;
                } else {
                    if (bytes[i] != 0) {
                        n++;
                    }
                }
            }

            if (i % 2 == 1) {
                if (bytes[i - 1] != 0)
                    i = i - 1;
                else
                    i = i + 1;

            }

            return new String(bytes, 0, i, "Unicode");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String genMachineCode(String token) {
        String result = "";
        try {
            String t = Long.toString(Long.valueOf(token.substring(0, 5)), 32);
            result = t + String.valueOf(token).substring(5, String.valueOf(token).length());
        } catch (Exception e) {
        }
        return result;
    }


    public static String convertStreamToString(InputStream is) {
        /*
        * To convert the InputStream to String we use the BufferedReader.readLine()
        * method. We iterate until the BufferedReader return null which means
        * there's no more data to read. Each line will appended to a StringBuilder
        * and returned as String.
        */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String res = sb.toString();
        Log.v(TAG, "res=" + res);
        return res;
    }


    public static String urlDecoder(String obj) {
        return urlDecoder(obj, "gbk");
    }

    public static String urlDecoder(String obj, String charset) {
        String result = null;
        if (obj != null) {
            try {
                result = URLDecoder.decode(obj, charset);
            } catch (UnsupportedEncodingException e) {
                return result;
            }
        }
        return result;
    }

    public static String getConstellation(String birthday) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(birthday);
            int month = date.getMonth() + 1;
            int day = date.getDate();
            switch (month) {
                case 1:
                    if (day <= 19) {
                        str = "魔蝎座";
                    } else {
                        str = "水瓶座";
                    }
                    break;
                case 2:
                    if (day <= 18) {
                        str = "水瓶座";
                    } else {
                        str = "双鱼座";
                    }
                    break;
                case 3:
                    if (day <= 20) {
                        str = "双鱼座";
                    } else {
                        str = "白羊座";
                    }
                    break;
                case 4:
                    if (day <= 19) {
                        str = "白羊座";
                    } else {
                        str = "金牛座";
                    }
                    break;
                case 5:
                    if (day <= 20) {
                        str = "金牛座";
                    } else {
                        str = "双子座";
                    }
                    break;
                case 6:
                    if (day <= 21) {
                        str = "双子座";
                    } else {
                        str = "巨蟹座";
                    }
                    break;
                case 7:
                    if (day <= 22) {
                        str = "巨蟹座";
                    } else {
                        str = "狮子座";
                    }
                    break;
                case 8:
                    if (day <= 22) {
                        str = "狮子座";
                    } else {
                        str = "处女座";
                    }
                    break;
                case 9:
                    if (day <= 22) {
                        str = "处女座";
                    } else {
                        str = "天秤座";
                    }
                    break;
                case 10:
                    if (day <= 23) {
                        str = "天秤座";
                    } else {
                        str = "天蝎座";
                    }
                    break;
                case 11:
                    if (day <= 22) {
                        str = "天蝎座";
                    } else {
                        str = "射手座";
                    }
                    break;
                case 12:
                    if (day <= 21) {
                        str = "射手座";
                    } else {
                        str = "魔蝎座";
                    }
                    break;
                default:
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;

    }

    /**
     * 返回手机串号
     *
     * @return
     */
    public static String getIMEI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //imei
            String imei = telephonyManager.getDeviceId();
            if (!StringUtil.isEmpty(imei))
                return imei;
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前设备信息
     */
    public static String getDeviceInfo() {
        StringBuilder str = new StringBuilder();
        try {
//            str.append("BRAND=" + DeviceUtils.getManufacturer());//厂商meizu
//            str.append("|MODEL=" + DeviceUtils.getModel());//型号mx5
//            str.append("|RELEASE=" + ConvertUtil.NVL(Build.VERSION.RELEASE, ""));//5.1
//            str.append("|SDK_INT=" + ConvertUtil.NVL(Build.VERSION.SDK_INT, ""));//22
//            str.append("|CPU_ABI_=" + ConvertUtil.NVL(Build.CPU_ABI, ""));//cpu1
//            str.append("|CPU_ABI2=" + ConvertUtil.NVL(Build.CPU_ABI2, ""));//cpu2
//            str.append("|PRODUCT=" + ConvertUtil.NVL(Build.PRODUCT, ""));//m3note
//            str.append("|DISPLAY=" + DeviceUtils.getSDKVersion());//手机显示的版本号 Flyme 5.1.10.0A

        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
        Log.v(TAG, str.toString());
        return str.toString();
    }


    /**
     * 作为手机deviceId
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
//        try {
//            String id = "";
//            //设备id ANDROID_ID
//            id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//            if (!StringUtil.isEmpty(id))
//                return id;
//            //取系统的serialno,反射获取
//            Class<?> c = Class.forName("android.os.SystemProperties");
//            Method get = c.getMethod("get", String.class, String.class);
//            id = (String) (get.invoke(c, "ro.serialno", "unknown"));
//            if (!StringUtil.isEmpty(id))
//                return id;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        String utdid = null;
        try {
            //阿里唯一标示库utdid4all.jar;这里可以直接用，umeng的推送用到这个库,生成24位字符串
            utdid = UTDevice.getUtdid(context);
            if (!StringUtil.isEmpty(utdid)) {
//                Log.v(TAG, "getDeviceId utdid="+utdid);
                //有特殊字符串， 这里我直接md5成32位的
                utdid = StringUtil.md5(utdid);
                return utdid;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
        //上面的标示 应该不会为空，防止别人的sdk，可能有问题
        //如果上面的唯一标示没有获取到，就自己生成一个存在本地
        if (isEmpty(utdid)) {
            utdid = PreferenceSetting.getString(context, "local_utdid");
            if (isEmpty(utdid)) {
                utdid = System.currentTimeMillis() + "";
                //统一成32位的
                utdid = StringUtil.md5(utdid);
                PreferenceSetting.setString(context, "local_utdid", utdid);
            }
        }
        return utdid;
    }

    public static String getMobinfo(Context context) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
//            stringBuffer.append("brand_" + DeviceUtils.getc(Build.BRAND, ""));
//            stringBuffer.append("_model_" + ConvertUtil.NVL(Build.MODEL, ""));
//            stringBuffer.append("_cpu_" + ConvertUtil.NVL(Build.CPU_ABI, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    public static String getMac(Context context) {
        String str = "";
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            str = info.getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return str;
    }


    public static String formatPlayCount(long playcount) {

        double y = playcount / 100000000f;
        if (playcount / 100000000 > 0) {
            return round(y, 1) + "亿";
        }
        double w = playcount / 10000f;
        if (playcount / 10000 > 0) {
            return round(w, 1) + "万";
        }
//        double q = playcount/1000f;
//        if(playcount/1000>0){
//            return round(q,2)+"千";
//        }
        return String.valueOf(playcount);
    }

    public static double round(double v, int scale) {
        String temp = "####.";
        for (int i = 0; i < scale; i++) {
            temp += "0";
        }
        return Double.valueOf(new java.text.DecimalFormat(temp).format(v));
    }

    public static byte[] decode(final byte[] bytes) {
        return Base64.decode(bytes, 2);
    }

    public static String encode(final byte[] bytes) {
        return new String(Base64.encode(bytes, 2));
    }

    public static String formatDate(int hour) {
        String src = "";
        if (hour >= 23 || hour < 1) {
            src = "子时";
        } else if (hour >= 1 && hour < 3) {
            src = "丑时";
        } else if (hour >= 3 && hour < 5) {
            src = "寅时";
        } else if (hour >= 5 && hour < 7) {
            src = "卯时";
        } else if (hour >= 7 && hour < 9) {
            src = "辰时";
        } else if (hour >= 9 && hour < 11) {
            src = "巳时";
        } else if (hour >= 11 && hour < 13) {
            src = "午时";
        } else if (hour >= 13 && hour < 15) {
            src = "未时";
        } else if (hour >= 15 && hour < 17) {
            src = "申时";
        } else if (hour >= 17 && hour < 19) {
            src = "酉时";
        } else if (hour >= 19 && hour < 21) {
            src = "戌时";
        } else if (hour >= 21 && hour < 23) {
            src = "亥时";
        }
        return src;
    }

    public static String formatDate1(int position) {
        String src = "";
        switch (position) {
            case 0:
                src = "子时";
                break;
            case 1:
                src = "丑时";
                break;
            case 2:
                src = "寅时";
                break;
            case 3:
                src = "卯时";
                break;
            case 4:
                src = "辰时";
                break;
            case 5:
                src = "巳时";
                break;
            case 6:
                src = "午时";
                break;
            case 7:
                src = "未时";
                break;
            case 8:
                src = "申时";
                break;
            case 9:
                src = "酉时";
                break;
            case 10:
                src = "戌时";
                break;
            case 11:
                src = "亥时";
                break;
        }
        return src;
    }

    public static int formatDateIndex(int hour) {
        int position = -1;
        if (hour >= 23 && hour < 1) {
            position = 0;
        } else if (hour >= 1 && hour < 3) {
            position = 1;
        } else if (hour >= 3 && hour < 5) {
            position = 2;
        } else if (hour >= 5 && hour < 7) {
            position = 3;
        } else if (hour >= 7 && hour < 9) {
            position = 4;
        } else if (hour >= 9 && hour < 11) {
            position = 5;
        } else if (hour >= 11 && hour < 13) {
            position = 6;
        } else if (hour >= 13 && hour < 15) {
            position = 7;
        } else if (hour >= 15 && hour < 17) {
            position = 8;
        } else if (hour >= 17 && hour < 19) {
            position = 9;
        } else if (hour >= 19 && hour < 21) {
            position = 10;
        } else if (hour >= 21 && hour < 23) {
            position = 11;
        }
        return position;
    }


    /**
     * 数字每三位显示,
     *
     * @param number
     * @return
     */
    public static String forNumber(int number) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(number);
    }


//    public static void main(String[] args) {
//
////        System.out.println(formatPlayCount(1000));
////        String sr = urlEncode("1","utf-8");
////        System.out.println(sr);
//
//    }


    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static String toString(Object object) {
        if (object == null)
            return null;
        else if (object instanceof Calendar)
            return StringUtil.simpleDateFormat.format(new Timestamp(
                    ((Calendar) object).getTimeInMillis()));
        else if (object instanceof Date)
            return StringUtil.simpleDateFormat.format((Date) object);
        else if (object instanceof InputStream) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InputStream in = (InputStream) object;
            //读取缓存
            byte[] buffer = new byte[2048];
            int length = 0;
            try {
                while ((length = in.read(buffer)) != -1) {
                    bos.write(buffer, 0, length);//写入输出流
                }

            } catch (IOException e) {

            } finally {
//				try {
//					in.close();
//				} catch (IOException e) {
//
//				}
            }
            return bos.toString();
        } else
            return String.valueOf(object);
    }

    /**
     * If a string is null or an empty string, it's a null string.
     *
     * @param str String
     * @return boolean
     */
    public static boolean isNull(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * If a string is not null and an empty string, it's not a null string.
     *
     * @param str String
     * @return boolean
     */
    public static boolean isNotNull(String str) {
        return str != null && str.trim().length() > 0;
    }

    /**
     * Compare strings.
     *
     * @param str1 String
     * @param str2 String
     * @return boolean
     */
    public static boolean compare(String str1, String str2) {
        if (str1 != null)
            return str1.equals(str2);
        if (str2 != null)
            return str2.equals(str1);

        return true;
    }

    /**
     * Compare strings.
     *
     * @param str1 String
     * @param str2 String
     * @return boolean
     */
    public static boolean compareIgnoreCase(String str1, String str2) {
        if (str1 != null)
            return str1.equalsIgnoreCase(str2);
        if (str2 != null)
            return str2.equalsIgnoreCase(str1);

        return true;
    }

    /**
     * Return the not null string.
     *
     * @param str1 String
     * @param str2 String
     * @return String
     */
    public static String returnNotNull(String str1, String str2) {
        return StringUtil.isNotNull(str1) ? str1 : str2;
    }

    /**
     * If a string is not null, then convert it to upper case.
     *
     * @param str String
     * @return String
     */
    public static String toUpperCaseOnStrategy(String str) {
        return str;
    }

    public static String toUpperCase(String str) {
        return str == null ? str : str.toUpperCase();
    }

    /**
     * If a string is not null, then convert it to lower case.
     *
     * @param str String
     * @return String
     */
    public static String toLowerCaseIfNotNull(String str) {
        return str == null ? str : str.toLowerCase();
    }

    /**
     * Concatenate strings with dot.
     *
     * @param str1 String
     * @param str2 String
     * @return String
     */
    public static String concatWithDot(String str1, String str2) {
        return str1 + "." + str2;
    }

    /**
     * Concatenate strings with dot.
     *
     * @param str1 String
     * @param str2 String
     * @param str3 String
     * @return String
     */
    public static String concatWithDot(String str1, String str2, String str3) {
        return str1 + "." + str2 + "." + str3;
    }

    /**
     * Concatenate strings with separator.
     *
     * @param strings   List
     * @param separator String
     * @return String
     */
    public static String concatWithSeparator(List<String> strings, String separator) {
        return StringUtil.concatWithSeparator(strings, separator, "");
    }

    /**
     * Concatenate strings with separator.
     *
     * @param strings   List
     * @param separator String
     * @param afterLast String
     * @return String
     */
    public static String concatWithSeparator(List<String> strings, String separator,
                                             String afterLast) {
        if (strings == null || strings.size() == 0)
            return "";

        String returnString = "";
        for (int i = 0; i < strings.size(); i++) {
            returnString += strings.get(i);

            if (i < strings.size() - 1)
                returnString += separator;
        }

        returnString += afterLast;
        return returnString;
    }

    /**
     * Generate recursion string with gene and repeat time.
     *
     * @param recursionGene String
     * @param repeatTime    int
     * @param separator     String
     * @return String
     */
    public static String generateRecursionString(String recursionGene,
                                                 int repeatTime, String separator) {
        return StringUtil.generateRecursionString(recursionGene, repeatTime,
                separator, "");
    }

    /**
     * Generate recursion string with gene and repeat time.
     *
     * @param recursionGene String
     * @param repeatTime    int
     * @param separator     String
     * @param afterLast     String
     * @return String
     */
    public static String generateRecursionString(String recursionGene,
                                                 int repeatTime, String separator, String afterLast) {
        if (repeatTime <= 0)
            return "";

        String returnString = "";
        for (int i = 0; i < repeatTime; i++) {
            returnString += recursionGene;

            if (i < repeatTime - 1)
                returnString += separator;
        }

        returnString += afterLast;
        return returnString;
    }

    /**
     * Generate StringTokenizer with "/".
     *
     * @param str String
     * @return StringTokenizer
     */
    public static StringTokenizer toStringTokenizer(String str) {
        return StringUtil.toStringTokenizer(str, "/");
    }

    /**
     * Generate StringTokenizer with given delim.
     *
     * @param str   String
     * @param delim String
     * @return StringTokenizer
     */
    public static StringTokenizer toStringTokenizer(String str, String delim) {
        return new StringTokenizer(str, delim);
    }

    /**
     * Convert a string collection to string array.
     *
     * @param collection Collection
     * @return String[]
     */
    public static String[] toStringArray(Collection collection) {
        List strs = new LinkedList(collection);
        String str[] = new String[strs.size()];
        for (int i = 0; i < strs.size(); i++)
            str[i] = (String) strs.get(i);

        return str;
    }

    /**
     * The string "false", "NO" and "0" mean false, and true if other.
     *
     * @param str String
     * @return boolean
     */
    public static boolean toBoolean(String str) {
        if (str == null)
            return false;

        str = str.trim().toUpperCase();
        if ("FALSE".equals(str) || "NO".equals(str) || "0".equals(str)
                || "NOT".equals(str))
            return false;

        return true;
    }

    public static String replaceBackSlashWithSlash(String filePath) {
        int index = -1;
        if ((index = filePath.indexOf("\\")) < 0)
            return filePath;
        String prefix = filePath.substring(0, index);
        String postfix = filePath.substring(index + 1, filePath.length());
        filePath = prefix + "/" + postfix;
        return StringUtil.replaceBackSlashWithSlash(filePath);
    }

    public static String substring(String splitStr, int bytes) {
        int cutLength = 0;
        int byteNum = bytes;
        byte bt[] = splitStr.getBytes();
        if (bytes > 1) {
            for (int i = 0; i < byteNum; i++) {
                if (bt[i] < 0) {
                    cutLength++;

                }
            }

            if (cutLength % 2 == 0) {
                cutLength /= 2;
            } else {
                cutLength = 0;
            }
        }
        int result = cutLength + --byteNum;
        if (result > bytes) {
            result = bytes;
        }
        if (bytes == 1) {
            if (bt[0] < 0) {
                result += 2;

            } else {
                result += 1;
            }
        }
        String substrx = new String(bt, 0, result);
        return substrx;
    }


    public static String utf8ToUnicode(String inStr) {
        char[] myBuffer = inStr.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < inStr.length(); i++) {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(myBuffer[i]);
            if (ub == Character.UnicodeBlock.BASIC_LATIN) {
                //英文及数字等
                sb.append(myBuffer[i]);
            } else if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                //全角半角字符
                int j = (int) myBuffer[i] - 65248;
                sb.append((char) j);
            } else {
                //汉字
                int s = (int) myBuffer[i];
                String hexS = Integer.toHexString(s);
                String unicode = "\\u" + hexS;
                sb.append(unicode.toLowerCase());
            }
        }
        return sb.toString();
    }

    public static String unicodeToUtf8(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed\\uxxxx encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    public static String getMD5String(String str) {
        String md5Str = null;
        if (str != null && str.length() != 0) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(str.getBytes());
                byte b[] = md.digest();

                StringBuffer buf = new StringBuffer("");
                for (int offset = 0; offset < b.length; offset++) {
                    int i = b[offset];
                    if (i < 0)
                        i += 256;
                    if (i < 16)
                        buf.append("0");
                    buf.append(Integer.toHexString(i));
                }
                //32位
                md5Str = buf.toString();
                //16位   md5Str = buf.toString().substring(8, 24);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return md5Str;
    }

    public static boolean checkMailFormat(String mailAddress) {
        String regex = "(\\w[\\w\\.\\-]*)@\\w[\\w\\-]*[\\.(com|cn|org|edu|hk)]+[a-z]$";
        return Pattern.matches(regex, mailAddress);
    }

    public static boolean validateMoblie(String phone) {
        int l = phone.length();
        boolean rs = false;
        switch (l) {
            case 7:
                if (matchingText("^(13[0-9]|15[0-9]|18[7|8|9|6|5])\\d{4}$", phone)) {
                    rs = true;
                }
                break;
            case 11:
                if (matchingText("^(13[0-9]|15[0-9]|18[7|8|9|6|5])\\d{4,8}$", phone)) {
                    rs = true;
                }
                break;
            default:
                rs = false;
                break;
        }
        return rs;
    }

    private static boolean matchingText(String expression, String text) {
        Pattern p = Pattern.compile(expression); // 正则表达式
        Matcher m = p.matcher(text); // 操作的字符串
        boolean b = m.matches();
        return b;
    }

    public static int strlen(String str) {
        if (str == null || str.length() <= 0) {
            return 0;
        }
        int len = 0;

        char c;
        for (int i = str.length() - 1; i >= 0; i--) {
            c = str.charAt(i);
            if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                len++;
            } else {
                if (Character.isLetter(c)) {    //中文
                    len += 2;
                } else {     //符号或控制字符
                    len++;
                }
            }
        }
        return len;
    }

    public static String changeStreamToString(InputStream stream, String encode) throws IOException {

        byte[] b100k = new byte[200000];

        int pos = 0;
        while (true) {
            int len = stream.read(b100k, pos, b100k.length - pos);
            if (len <= 0) break;
            else pos = pos + len;
        }

        if (pos >= b100k.length - 1) {
            throw new IOException("ERROR:The stream size is more than " + b100k.length + " bytes");
        }

        return new String(b100k, 0, pos, encode);
    }

    public static String concat(Object... args) {
        StringBuffer buf = new StringBuffer();
        for (Object arg : args) {
            buf.append(arg);
        }
        return buf.toString();
    }

    /**
     * 移除标点符号
     *
     * @param string
     * @return
     */
    public static String moveDot(String string) {
        try {
            if (isEmpty(string))
                return "";
            string = string.replaceAll("[\\p{P}‘’“”]", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    /**
     * 解决 字符串以 结尾为“.”，在点后拼接个 0
     * @param s
     * @return
     */
    public static String endWithPoint(String s){
        if (s.endsWith(".")) {
            s +="0";
            return s;
        }
        return s;
    }


    private static long lastClickTime;
    /**
     * 防止多次点击事件处理
     * @return
     * @author songdiyuan
     */
    public synchronized static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


}