package com.example.safelight;

/**
 * Created by changsu on 2015-03-23.
 */

import android.bluetooth.BluetoothDevice;

public class BleDeviceInfo {

    public static int TIME_OUT = 20;

    public BluetoothDevice btDevice;   // Bluetooth Device
    public String proximityUuid;       // UUID
    public String devName;             // Device Name
    public String devAddress;          // Device Address
    public int timeout;                // defatlt: 10; decrease per second

    public int major;                  // Major
    public int minor;                  // Minor
    public int measuredPower;          // Measured Power
    public int txPower;                // Tx Power
    public int rssi;                   // RSSI
    public double distance;            // Distance
    public double distance2;            // Distance

    //Device Info
    public String hwVersion;           // H/W Version
    public String fwVersion;           // Firmware Version
    public KalmanFilter rssiKalmanFileter;

    //Constructor
    public BleDeviceInfo() {
        this.proximityUuid = "";
        this.devName = "";
        this.devAddress = "";

        this.major = 0;
        this.minor = 0;
        this.measuredPower = 0;
        this.txPower = 0;
        this.rssi = 0;
        this.distance = 0;
        this.distance2 = 0;

        this.hwVersion = "";
        this.fwVersion = "";

        this.rssiKalmanFileter = new KalmanFilter(0);
    }


    //BluetoothDevice를 추가한 생성자
    //Constructor with Parms
    public BleDeviceInfo(BluetoothDevice device, String proximityUuid, String devName,
                         String devAddress, int major, int minor, int mPower, int rssi,
                         int txPower, double distance)
    {
        this.btDevice = device;
        this.proximityUuid = proximityUuid;
        this.devName = devName;
        this.devAddress = devAddress;
        this.major = major;
        this.minor = minor;
        this.measuredPower = mPower;
        this.txPower = txPower;

        this.rssi = rssi;
        this.distance = distance;
        this.timeout = TIME_OUT;

        this.rssiKalmanFileter = new KalmanFilter(this.rssi);
    }

    // Measured Power 제외, 거리 1개
    public BleDeviceInfo(String proximityUuid, String devName,
                         String devAddress, int major, int minor, int txPower, int rssi, double distance)
    {

        this.proximityUuid = proximityUuid;
        this.devName = devName;
        this.devAddress = devAddress;
        this.major = major;
        this.minor = minor;
        //this.measuredPower = mPower;
        this.txPower = txPower;

        this.rssi = rssi;
        this.distance = distance;
        this.timeout = TIME_OUT;

        this.rssiKalmanFileter = new KalmanFilter(this.rssi);
    }

    // Measured Power를 제외한 생성자
    public BleDeviceInfo(String proximityUuid, String devName,
                         String devAddress, int major, int minor, int txPower, int rssi, double distance, double distance2)
    {

        this.proximityUuid = proximityUuid;
        this.devName = devName;
        this.devAddress = devAddress;
        this.major = major;
        this.minor = minor;
        //this.measuredPower = mPower;
        this.txPower = txPower;

        this.rssi = rssi;
        this.distance = distance;
        this.distance2 = distance2;
        this.timeout = TIME_OUT;

        this.rssiKalmanFileter = new KalmanFilter(this.rssi);
    }

    /*
        아래의 get, set 함수는 사용하지 않음: 추후 사용을 위해 남겨둠
     */
    /*----------------------------------------------------------*/
    public String getProximityUuid()
    {
        return this.proximityUuid;
    }
    public void setProximityUuid(String uuid)
    {
        this.proximityUuid = uuid;
    }

    public String getDeviceName()
    {
        return this.devName;
    }

    public void setDeviceName(String deviceName)
    {
        this.devName = devName;
    }

    public String getDevAddress()
    {
        return this.devAddress;
    }

    public void setDevAddress(String deviceAddr)
    {
        this.devAddress = deviceAddr;
    }

    public int getMajor()
    {
        return this.major;
    }

    public void setMajor(int major)
    {
        this.major = major;
    }

    public int getMinor()
    {
        return this.minor;
    }

    public void setMinor(int minor)
    {
        this.minor = minor;
    }

    public String getMeasuredPower()
    {
        return String.valueOf(this.measuredPower);
    }

    public void setMeasuredPower(int mPower)
    {
        this.measuredPower = mPower;
    }

    public int getRssi()
    {
        return this.rssi;
    }

    public void setRssi(int rssi)
    {
        this.rssi = rssi;
    }

    public int getTxPower()
    {
        return this.txPower;
    }

    public void setTxPower(int power)
    {
        this.txPower = power;
    }

    public String getHwVersion()
    {
        return this.hwVersion;
    }

    public String getFwVersion()
    {
        return this.fwVersion;
    }

    public double getDistance()
    {
        return this.distance;
    }

    public void setDistance(double distance)
    {
        this.distance = distance;
    }

    public double getDistance2()
    {
        return this.distance2;
    }

    public void setDistance2(double distance2)
    {
        this.distance2 = distance2;
    }

    public int getTimeout(){ return this.timeout; }

    public void setTimeout(int timeout){ this.timeout = timeout; }
    /*----------------------------------------------------------*/
    /*
        거리 계산
     */
    public double estimateDistance(int rssiValue, int txPower)
    {
        if(txPower == 0)
        {
            txPower = -1;
        }

        if(rssiValue == 0)
        {
            rssiValue = 0;
        }

        this.distance = Math.pow(10, ((double)txPower - rssiValue) / (10 * 2));

        return distance;
    }

    /*
        BleDeviceInfo의 devAddress만 비교하기 위함
     */
    public boolean equals(BleDeviceInfo b) {
        if(b.devAddress.equals(this.devAddress))
            return true;
        else
            return false;
    }

}
