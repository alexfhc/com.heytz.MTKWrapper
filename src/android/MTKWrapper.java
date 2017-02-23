package com.heytz.MTKWrapper;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import java.net.InetAddress;
import java.util.*;
//import android.os;
//import com.espressif.iot.esptouch.EsptouchTask;
//import com.espressif.iot.esptouch.IEsptouchListener;
//import com.espressif.iot.esptouch.IEsptouchResult;
//import com.espressif.iot.esptouch.IEsptouchTask;
//import com.espressif.iot.esptouch.task.__IEsptouchTask;
//import com.espressif.iot_esptouch_demo.R;
//import esptouch.udp.UDPSocketClient;
//import com.lsd.easy.joine.test.R;

/**
 * This class starts transmit to activation
 */
public class MTKWrapper extends CordovaPlugin {

    private static String TAG = "=====MTKWrapper.class====";

    private Context context;
    private String userName;
    private String deviceLoginID;
    private String devicePassword;
    private int activateTimeout;
    private String activatePort;
    private CallbackContext ESPCallbackContext;
    private String wifiSSID;
    private String ip;
    private String wifiKey;


    private static int[][] desTables = new int[][]{{15, 12, 8, 2}, {13, 8, 10, 1}, {1, 10, 13, 0}, {3, 15, 0, 6}, {11, 8, 12, 7}, {4, 3, 2, 12}, {6, 11, 13, 8}, {2, 1, 14, 7}};
    private Handler mHandler;
//    private final UDPSocketClient mSocketClient;

    private static boolean sendFlag = true;
    public static int CODE_INTERVAL_TIMES = 8;
    public static int CODE_INTERVAL_TIME = 500;
    public static int CODE_TIME = 20;
    public static int CODE_TIMES = 5;
    private String broadcastIp = "255.255.255.255";
    private Set<String> successMacSet = new HashSet();


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        context = cordova.getActivity().getApplicationContext();
    }

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("setWifi")) {

            wifiSSID = args.getString(0);
            wifiKey = args.getString(1);
            userName = args.getString(2);
            //easylinkVersion = args.getInt(3);
            activateTimeout = args.getInt(4);
            activatePort = args.getString(5);
            deviceLoginID = args.getString(6);
            devicePassword = args.getString(7);
            String isSsidHiddenStr = "NO";
            String taskResultCountStr = "1";

            if (wifiSSID == null || wifiSSID.length() == 0 ||
                    wifiKey == null || wifiKey.length() == 0 ||
                    userName == null || userName.length() == 0 ||
                    activatePort == null || activatePort.length() == 0 ||
                    devicePassword == null || devicePassword.length() == 0 ||
                    deviceLoginID == null || deviceLoginID.length() == 0
                    ) {
                Log.e(TAG, "arguments error ===== empty");
                return false;
            }

            return true;
        }
        if (action.equals("startSocket")) {
            ip = args.getString(0);
//            startSocket(ip);
            return true;
        }

        return false;
    }


}
