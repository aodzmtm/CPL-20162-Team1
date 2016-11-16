package com.example.safelight;

/**
 * Created by CHANGJUN on 2016-11-16.
 */

import android.os.Handler;
public class ServiceThread extends Thread{
    Handler handler;
    boolean isRun = true;

    public ServiceThread(Handler handler){
        this.handler = handler;
    }

    public void stopForever(){
        synchronized (this) {
            this.isRun = false;
        }
    }

    public void run(){
        //반복적으로 수행할 작업을 한다.
        while(isRun){
            try{
                MainActivity ac = new MainActivity();
                ac.scanBLE();
                Thread.sleep(30000); //10초마다 메시지를 보냄
            }catch (Exception e) {}
            handler.sendEmptyMessage(0);//쓰레드에 있는 핸들러에게 메세지를 보냄
        }
    }
}
