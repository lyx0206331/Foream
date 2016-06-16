package com.adrian.foream.activity;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.adrian.foream.R;
import com.adrian.foream.tools.DataUtil;
import com.broadcom.cooee.Cooee;

import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 * Foream配置參數界面
 * @author RanQing
 * create at 16-6-16 下午4:36
 */
public class ConfigActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ConfigActivity.class.getName();
    private static final int MSG_PARSE_DOMAIN = 0;
    private static final int MSG_START_CONFIG = 1;

    private EditText mUnknownET;
    private EditText mWifiPwdET;
    private EditText mServerDomainET;
    private EditText mServerPortET;
    private EditText mDevIdET;
    private Spinner mStreamTypeSP;
    private Spinner mResRatioSP;
    private TextView mCodeRateTV;
    private Button mMinusBtn;
    private Button mPlusBtn;
    private Button mCommitBtn;

    private Thread mThread;
    private boolean mRun = false;
    private String mWifiSsid = null;
    private int mLocalIp;
    private String mStreamType;
    private String mResRatio;
    private String mUnknown;
    private String mWifiPwd;
    private String mServerAddr;
    private String mServerPort;
    private String mDevId;
    private String mCodeRate;

    private float codeRate;
    private int streamTypePos;
    private int resRatioPos;

    private String septor = "|";
    private String sc_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSendSmartConfig();
    }

    @Override
    protected void initVariables() {
        codeRate = DataUtil.getCodeRate();
        streamTypePos = DataUtil.getStreamType();
        resRatioPos = DataUtil.getResRatio();
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_config);
        mUnknownET = (EditText) findViewById(R.id.et_unknown);
        mWifiPwdET = (EditText) findViewById(R.id.et_wifi_pwd);
        mServerDomainET = (EditText) findViewById(R.id.et_server_domain);
        mServerPortET = (EditText) findViewById(R.id.et_server_port);
        mDevIdET = (EditText) findViewById(R.id.et_dev_id);
        mStreamTypeSP = (Spinner) findViewById(R.id.sp_stream_type);
        mResRatioSP = (Spinner) findViewById(R.id.sp_res_ratio);
        mCodeRateTV = (TextView) findViewById(R.id.tv_code_rate);
        mMinusBtn = (Button) findViewById(R.id.btn_minus);
        mPlusBtn = (Button) findViewById(R.id.btn_plus);
        mCommitBtn = (Button) findViewById(R.id.btn_commit);

        mMinusBtn.setOnClickListener(this);
        mPlusBtn.setOnClickListener(this);
        mCommitBtn.setOnClickListener(this);
        mStreamTypeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] streamType = getResources().getStringArray(R.array.stream_type);
                mStreamType = streamType[position];
                streamTypePos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mResRatioSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] resRatio = getResources().getStringArray(R.array.res_ratio);
                mResRatio = resRatio[position];
                resRatioPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void loadData() {
        mUnknownET.setText(DataUtil.getUnknownParam() + "");
        mWifiPwdET.setText(DataUtil.getWifiPwd());
        mServerDomainET.setText(DataUtil.getServerAddr());
        mServerPortET.setText(DataUtil.getServerPort() + "");
        mDevIdET.setText(DataUtil.getDevId());
        mStreamTypeSP.setSelection(DataUtil.getStreamType());
        mResRatioSP.setSelection(DataUtil.getResRatio());
        mCodeRateTV.setText(DataUtil.formatNum(codeRate));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_minus:
                codeRate -= .1f;
                if (codeRate < 0.3f) {
                    codeRate = 5.0f;
                }
                mCodeRateTV.setText(DataUtil.formatNum(codeRate));
                break;
            case R.id.btn_plus:
                codeRate += .1f;
                if (codeRate > 5.0f) {
                    codeRate = .3f;
                }
                mCodeRateTV.setText(DataUtil.formatNum(codeRate));
                break;
            case R.id.btn_commit:
                checkInputInfo();
                break;
            default:
                break;
        }
    }

    /**
     * 验证输入信息
     */
    private void checkInputInfo() {
        if (TextUtils.isEmpty(mUnknownET.getText())) {
            mUnknownET.setError(getString(R.string.error_input_unknown));
            return;
        } else if (TextUtils.isEmpty(mWifiPwdET.getText())) {
            mWifiPwdET.setError(getString(R.string.error_input_wifi_pwd));
            return;
        } else if (TextUtils.isEmpty(mServerDomainET.getText())) {
            mServerDomainET.setError(getString(R.string.error_input_server_addr));
            return;
        } else if (TextUtils.isEmpty(mServerPortET.getText())) {
            mServerPortET.setError(getString(R.string.error_input_server_port));
            return;
        } else if (TextUtils.isEmpty(mDevIdET.getText())) {
            mDevIdET.setText(getString(R.string.error_input_dev_id));
            return;
        }
        mUnknown = mUnknownET.getText().toString();
        mWifiPwd = mWifiPwdET.getText().toString();
        mServerAddr = mServerDomainET.getText().toString();
        mServerPort = mServerPortET.getText().toString();
        mDevId = mDevIdET.getText().toString();
        mCodeRate = mCodeRateTV.getText().toString();

        DataUtil.setUnknownParam(Integer.parseInt(mUnknown));
        DataUtil.setWifiPwd(mWifiPwd);
        DataUtil.setServerAddr(mServerAddr);
        DataUtil.setServerPort(Integer.parseInt(mServerPort));
        DataUtil.setDevId(mDevId);
        DataUtil.setStreamType(streamTypePos);
        DataUtil.setResRatio(resRatioPos);
        DataUtil.setCodeRate(codeRate);

        if (mRun) return;
        // 初始化SmartConfig命令参数
        WifiManager wifiManager = (WifiManager) this.getSystemService(this.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info == null || info.getSSID() == null)
            return;
        mWifiSsid = info.getSSID().replace("\"", "");

        mLocalIp = info.getIpAddress();

        Message msg = mHandler.obtainMessage();
        msg.what = MSG_PARSE_DOMAIN;
        msg.obj = mServerAddr;
        mHandler.sendMessage(msg);

    }

    private void startConfig(String ip) {
        String str_match = mUnknown + septor + mWifiPwd + septor + ip + septor +
                mServerPort + septor + mDevId + septor + mStreamType + septor + mResRatio + septor + mCodeRate;

        sc_password = str_match;// 要发送的信息串.需跟CAMERA配合
        Log.e(TAG, "sc_ssid:" + mWifiSsid);
        Log.e(TAG, "sc_password:" + sc_password);
        Log.e(TAG, "sc_localIp:" + mLocalIp);
        mRun = true;

        // 发送 SmartConfig命令
        Cooee.SetPacketInterval(20);
        mThread = new Thread() {
            private long curTime;

            public void run() {
                curTime = System.currentTimeMillis();
                while (mRun) {
                    if (System.currentTimeMillis() - curTime > 12000) break;
                    Cooee.send(mWifiSsid, sc_password, mLocalIp);
                }

            }
        };

        mThread.start();
    }

    private void stopSendSmartConfig() {
        mRun = false;
    }

    /**
     * 域名转换为ip
     *
     * @param domain
     */
    private void parseDomain2Ip(final String domain) {
        if (TextUtils.isEmpty(domain)) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress add = InetAddress.getByName(domain);
                    String ip = add.getHostAddress();
                    Log.e(TAG, "parse domain to ip : " + domain + "--->" + ip);
                    Message msg = mHandler.obtainMessage();
                    msg.what = MSG_START_CONFIG;
                    msg.obj = ip;
                    mHandler.sendMessage(msg);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_PARSE_DOMAIN:
                    parseDomain2Ip(msg.obj.toString());
                    break;
                case MSG_START_CONFIG:
                    String ip = msg.obj.toString();
                    startConfig(ip);
                    break;
            }
        }
    };

}
