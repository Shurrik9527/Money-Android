package com.moyacs.canary.kchart.data;

import org.json.JSONObject;

/**
 * Created by fangzhu on 2015/3/25.
 */
public class JSONObjectUtil {

    public static double getDouble (JSONObject object, String name, double deaultVal) {
        try {
            if (object == null)
                return deaultVal;
            if (object.has(name) && object.get(name) != null && !object.get(name).toString().equalsIgnoreCase("null"))
                return object.getDouble(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deaultVal;
    }


    public static int getInt (JSONObject object, String name, int deaultVal) {
        try {
            if (object == null)
                return deaultVal;
            if (object.has(name) && object.get(name) != null && !object.get(name).toString().equalsIgnoreCase("null"))
                return object.getInt(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deaultVal;
    }

    public static long getLong (JSONObject object, String name, long deaultVal) {
        try {
            if (object == null)
                return deaultVal;
            if (object.has(name) && object.get(name) != null && !object.get(name).toString().equalsIgnoreCase("null"))
                return object.getLong(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deaultVal;
    }

    public static String getString (JSONObject object, String name, String deaultVal) {
        try {
            if (object == null)
                return deaultVal;
            if (object.has(name) && object.get(name) != null) {
                String val = object.getString(name);
                if (val == null || val.trim().equalsIgnoreCase("null"))
                    return deaultVal;
                return val;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return deaultVal;
    }

    public static boolean getBoolean (JSONObject object, String name, boolean deaultVal) {
        try {
            if (object == null)
                return deaultVal;
            if (object.has(name) && object.get(name) != null && !object.get(name).toString().equalsIgnoreCase("null"))
                return object.getBoolean(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deaultVal;
    }

}
