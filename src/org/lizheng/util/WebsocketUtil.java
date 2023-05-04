package org.lizheng.util;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class WebsocketUtil extends WebSocketClient {

    private static final CountDownLatch latch = new CountDownLatch(1);

    public WebsocketUtil(URI serverURI) {
        super(serverURI);
    }

    public WebsocketUtil(URI uri,Map<String,String> headers , int connecttimeout) {
        super(uri,new Draft_6455(),headers,connecttimeout);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("连接到远程 WebSocket server.");
        latch.countDown();
    }

    @Override
    public void onMessage(String message) {
        System.out.println("接收到消息 <==== : " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed" + " with code " + code + " and reason " + reason);
        System.out.println("与远程WebSocket server断开连接.");
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("连接到远程 WebSocket server出错: " + ex.getMessage());
    }

    public static void main(String[] args) throws InterruptedException, JSONException {
        // 设置请求的URI和Cookie信息
        String serverUrl = "ws://104.194.232.58:7860/queue/join";
//        String serverUrl = "ws://localhost:8099";
        URI uri = URI.create(serverUrl);
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Cookie","access-token-unsecure=mWK2k9EkCVmUKJYZImsPvQ");
        WebsocketUtil client = new WebsocketUtil(uri,headers,3);
        client.connect();
        System.out.println("连接了？");
        latch.await();
        System.out.println(client.getReadyState());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg","send_hash");
        client.send(jsonObject.toString());
        System.out.println("发送成功?");
        // wait for server to reply
        Thread.sleep(5000);
        client.close();
    }
}
