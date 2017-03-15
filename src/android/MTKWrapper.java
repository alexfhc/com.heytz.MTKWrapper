package com.mediatek.elian;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
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
    private CallbackContext MTKCallbackContext;
    private String wifiSSID;
    private String password;
    private String ip;
    private ElianNative elian;
    private ServerSocket server = null;
    private Socket socket;
    private BufferedReader sendBack;
    private boolean wait;

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
        mHandler = new Handler() {

        };

    }

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("setWifi")) {
            MTKCallbackContext = callbackContext;
            wifiSSID = getSSID();
            password = args.getString(0);
            ip = intToIp(getMobileIP());
            mHandler.postDelayed(timeoutRun, 20000L);

            boolean result = ElianNative.LoadLib();

            elian = new ElianNative();
            int libVersion = elian.GetLibVersion();
            int protoVersion = elian.GetProtoVersion();
//            startSocketServer();
//            elian.InitSmartConnection(null, 0, 1);
//            elian.StartSmartConnection(wifiSSID, password, ip);
//            startSmartConnection(wifiSSID, password, ip);
            return true;
        }
        if (action.equals("startSocket")) {
            ip = args.getString(0);
            return true;
        }
        if (action.equals("dealloc")) {
            stopSmartConnection();
            mHandler.removeCallbacks(timeoutRun);
            return true;
        }
        return false;
    }

    private Runnable timeoutRun = new Runnable() {
        public void run() {
            stopSmartConnection();
            wait = false;
            if (server != null) {
                server = null;
                socket = null;
            }
            MTKCallbackContext.error("timeout");
        }
    };

    private void startSocketServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (server == null) {
                        server = new ServerSocket(8000);
                    }
                    wait = true;
                    while (wait) {
                        socket = server.accept();
                        sendBack = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String message = sendBack.readLine();
                        sendBack.close();
                        socket.close();
                        wait = false;
                        server.close();
                        MTKCallbackContext.success(message);
                    }
                } catch (Exception se) {
                    Log.e(TAG, se.toString());
                    MTKCallbackContext.error("socket failed");
                }
            }
        }).start();
    }

    private void startSmartConnection(final String wifiSSID, final String password, final String ip) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    elian.InitSmartConnection(null, 1, 1);
                    elian.StartSmartConnection(wifiSSID, password, ip);
//                    Thread.sleep(5000L);
                } catch (Exception se) {
                    Log.e(TAG, se.toString());
                    MTKCallbackContext.error("smart connection failed");
                } finally {
//                    elian.StopSmartConnection();
                }
            }
        }).start();
    }

    private void stopSmartConnection() {
        try {
            if (elian != null)
                elian.StopSmartConnection();
        } catch (Exception e) {
        }
    }

    private int getMobileIP() {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return wifiInfo.getIpAddress();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return 0;
        }
    }

    private String getSSID() {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return wifiInfo.getSSID();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return "";
        }
    }

    private String intToIp(int ipInt) {
        return new StringBuilder().append(((ipInt >> 24) & 0xff)).append('.')
                .append((ipInt >> 16) & 0xff).append('.').append(
                        (ipInt >> 8) & 0xff).append('.').append((ipInt & 0xff))
                .toString();
    }
}
