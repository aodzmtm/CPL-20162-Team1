package com.example.safelight;

/**
 * Created by changsu on 2015-04-08.
 */

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;


public class ClientThread extends Thread {

    private static final String Test="ws://echo.websocket.org";
    final static int MSG_RECEIVED_ACK = 0x100;

    public WebSocketClient mSocket;

    //Handler : 다른 객체가 보낸 메시지 수신, 처리하는 객체. 서브 스레드가 보낸 메시지를 받아 UI를 변경한다.
    Handler clientHandler;
    String destAddress;
    URI url;
    String respMsg = "";

//    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
//    byte[] buffer = new byte[1024];
//    int byteRead;

    private Activity mActivity;

//    public ClientThread(Handler handler, String addr, int port)
    public ClientThread(Handler handler, Activity u_Act, String addr)
    {
        clientHandler = handler;
        mActivity = u_Act;
        destAddress = addr;
    }

    public ClientThread(Handler handler)
    {
        clientHandler = handler;
    }

    //서버로부터 메시지 받는 쓰레드
    public void run()
    {
        try
        {
            url = new URI(destAddress);
            Log.i("Test_S2","url:"+url);
        }catch(URISyntaxException e) {
            Log.i("ERROR","URI(ClientThread.java)");
            e.printStackTrace();
            return;
        }

        mSocket = new WebSocketClient(url, new Draft_10()) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.i("Websocket","Opened");
                mSocket.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onMessage(final String message) {
                final String msg = message;
                mActivity.runOnUiThread(new Runnable() {
                      @Override
                    public void run() {
                //          TextView textView = (TextView)findViewById(R.id.messages);
                //          textView.setText(textView.getText() + "\n" + message);
                sendMessageToHandler(MSG_RECEIVED_ACK, msg);

                Log.d("Client", "respMsg: " + msg);
                         }
                  });

            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i("Websocket", "Closed " + reason);
            }

            @Override
            public void onError(Exception ex) {
                Log.i("Websocket", "Error " + ex.getMessage());
            }
        };
        mSocket.connect();
    }

    public void sendMessageToHandler(int id, String recvMsg)
    {
        Message msg = Message.obtain();
        msg.what = id;                      //user-define message code
        msg.obj = recvMsg;
        clientHandler.sendMessage(msg);
    }
}
