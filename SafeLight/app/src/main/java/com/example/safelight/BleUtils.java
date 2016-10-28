package com.example.safelight;

import android.util.Log;

/**
 * Created by changsu on 2015-03-28.
 * 각종 연산을 위한 class 임
 *  - 거리 계산
 *
 */
public class BleUtils {

    public BleUtils()
    {

    }


    /*
       byte[]를 String 타입으로 변환
    */
    public String ByteArrayToString(byte[] scanRecord)
    {
        /*
            byte -> String (2byte로 계산됨)
            따라서 scanRecord.length * 2의 크기로 StringBuilder를 생성함
         */
        StringBuilder hex = new StringBuilder(scanRecord.length * 2);

        for(byte b : scanRecord)
        {
            hex.append(String.format("%02x", b));
        }

        return hex.toString();
    }

    /*
        Conversion byte to integer value
        byte는 signed 값이므로 0xff로 and 연산을 수행함
     */
    public int byteToInt(byte value1, byte value2)
    {
        return ((value1 & 0xff) << 8 | (value2 & 0xff));
    }

    /*
        거리 계산 공식은 자료 조사 후 재수정 해야 됨
     */
    public double getDistance(int rssi, int mPower)
    {
        double distance = 0.0;  // 단위는 meter

        if(mPower == 0)
            mPower = -1;

        /*
            RSSI = Measured Power - 10 * n * log(distance)
            n = 2 ( in free space)

            distance = 10 ^ ((Measured Power) - RSSI) / (10 * 2));
         */

        distance = Math.pow(10, ((double)mPower - rssi) / (10 * 2));
        distance = Math.round(distance * 100) / 100.0;

        Log.d("Util", "distance: " + distance);
        return distance;
    }

    /*
        거리계산 - 2015.5.15 Ver

        아래링크를 참조하여 작성함
        https://medium.com/truth-labs/beacon-tracking-with-node-js-and-raspberry-pi-794afa880318
     */
    public double getDistance_20150515(int rssiValue, int txPower)
    {
        if (rssiValue == 0)
            return -1.0;
        double ratio = rssiValue * 1.0 / txPower;
        if (ratio < 1.0)
            return Math.pow(ratio, 10);

        return (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
    }
}
