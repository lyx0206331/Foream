package com.adrian.foream.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.adrian.foream.application.MyApplication;

import java.text.DecimalFormat;

/**
 * Created by adrian on 16-6-8.
 */
public class DataUtil {

    private static SharedPreferences initSP() {
        return MyApplication.newInstance().getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    /**
     * 未知参数
     * @param param
     */
    public static void setUnknownParam(int param) {
        initSP().edit().putInt("unknown", param).commit();
    }

    public static int getUnknownParam() {
        return initSP().getInt("unknown", 6);
    }

    /**
     * wifi密码
     * @param param
     */
    public static void setWifiPwd(String param) {
        initSP().edit().putString("wifi_pwd", param).commit();
    }

    public static String getWifiPwd() {
        return initSP().getString("wifi_pwd", "");
    }

    /**
     * 服务器地址
     * @param param
     */
    public static void setServerAddr(String param) {
        initSP().edit().putString("server_addr", param).commit();
    }

    public static String getServerAddr() {
        return initSP().getString("server_addr", "");
    }

    /**
     * 服务器端口
     * @param param
     */
    public static void setServerPort(int param) {
        initSP().edit().putInt("server_port", param).commit();
    }

    public static int getServerPort() {
        return initSP().getInt("server_port", 8020);
    }

    /**
     * 设备ID
     * @param param
     */
    public static void setDevId(String param) {
        initSP().edit().putString("dev_id", param).commit();
    }

    public static String getDevId() {
        return initSP().getString("dev_id", "w5wgxb5s");
    }

    /**
     * 推流方式
     * @param param 位置，第param个
     */
    public static void setStreamType(int param) {
        initSP().edit().putInt("stream_type", param).commit();
    }

    public static int getStreamType() {
        return initSP().getInt("stream_type", 0);
    }

    /**
     * 分辨率
     * @param param 位置，第param个
     */
    public static void setResRatio(int param) {
        initSP().edit().putInt("res_ratio", param).commit();
    }

    public static int getResRatio() {
        return initSP().getInt("res_ratio", 0);
    }

    /**
     * 码流
     * @param param
     */
    public static void setCodeRate(float param) {
        initSP().edit().putFloat("code_rate", param).commit();
    }

    public static float getCodeRate() {
        return initSP().getFloat("code_rate", 0.4f);
    }

    /**
     * 格式化浮点数为一位小数
     * @param num
     * @return
     */
    public static String formatNum(float num) {
        String result = String .format("%.1f",num);
        return result;
    }
}
