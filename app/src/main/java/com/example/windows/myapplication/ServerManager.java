package com.example.windows.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.TextView;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.util.HttpRequestParser;
import com.yanzhenjie.andserver.website.AssetsWebsite;
import com.yanzhenjie.andserver.website.WebSite;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Pattern;

public class ServerManager {

    private static final String TAG = "ServerManager";

    private TextView tv;

    ServerManager(TextView tv){
        this.tv = tv;
        tv.setText("初始化完成");
    }

    public void initServer(Context mainActivity) {

        tv.setText("启动中");

        AssetManager assets = mainActivity.getAssets();
        WebSite webSite = new AssetsWebsite(assets, "");

        AndServer andServer = new AndServer.Build()
                .website(webSite)
                .timeout(30 * 1000)
                .port(9527)
                .registerHandler("test", new request())
                .listener(mListener)
                .build();
        Server server = andServer.createServer();
        server.start();

        tv.setText("启动完成");
    }

    /**
     * 监听事件
     */
    private Server.Listener mListener = new Server.Listener() {

        @Override
        public void onStarted() {
            Log.e(TAG, "onStarted: ");
            tv.setText("启动成功");
        }

        @Override
        public void onStopped() {
            Log.e(TAG, "onStopped: ");
            tv.setText("停止");
        }

        @Override
        public void onError(Exception e) {
            Log.e(TAG, "onError: " + e.getMessage());
            tv.setText("启动失败");
        }
    };

    public class request implements RequestHandler {

        @Override
        public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws IOException {

            Map<String, String> params = HttpRequestParser.parse(request);
            String k = params.get("k");
            Log.e(TAG, "k: " + k);
            tv.setText("收到参数k:"+k);
            StringEntity stringEntity = new StringEntity(Integer.valueOf(k)*2+"", "utf-8");
            response.setEntity(stringEntity);
        }
    }


    private static final Pattern IPV4_PATTERN = Pattern.compile("^(" +
            "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
            "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");

    private static boolean isIPv4Address(String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    //获取本机IP地址
    public static InetAddress getLocalIPAddress() {
        Enumeration<NetworkInterface> enumeration = null;
        try {
            enumeration = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                NetworkInterface nif = enumeration.nextElement();
                Enumeration<InetAddress> inetAddresses = nif.getInetAddresses();
                if (inetAddresses != null)
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        if (!inetAddress.isLoopbackAddress() && isIPv4Address(inetAddress.getHostAddress())) {
                            return inetAddress;
                        }
                    }
            }
        }
        return null;
    }
}
