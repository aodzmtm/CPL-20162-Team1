package com.example.safelight;

/**
 * Created by changsu on 2015-03-23.
 */

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class BleDeviceListAdapter extends BaseAdapter {
    private Context mContext;
    LayoutInflater mInflater;
    int mLayout;
    private ArrayList<BluetoothDevice> mBleDeviceArrayList;
    private ArrayList<BleDeviceInfo> mBleDeviceInfoArrayList;

    // 검색된 BLE 장치가 중복 추가되는 부분을 방지하기 위해 HashMap을 사용
    // String: Device Address(key값)
    private HashMap<String, BleDeviceInfo> mHashBleMap = new HashMap<String, BleDeviceInfo>();

    public BleDeviceListAdapter(Context context, int layout, ArrayList<BleDeviceInfo> arBleList,
                                HashMap<String, BleDeviceInfo> hashBleMap)
    {
        mContext = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mBleDeviceInfoArrayList = arBleList;
        mLayout = layout;
        mHashBleMap = hashBleMap;
    }

    public synchronized void addOrUpdateItem(BleDeviceInfo info)
    {
        if(mHashBleMap.containsKey(info.getDevAddress()))
        {
            mHashBleMap.get(info.getDevAddress()).setRssi(info.getRssi());
        }
        else
        {
            mBleDeviceInfoArrayList.add(info);
            mHashBleMap.put(info.getDevAddress(), info);
        }

        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mBleDeviceInfoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBleDeviceInfoArrayList.get(position);
    }

    public void addBleDeviceItem(BleDeviceInfo item)
    {
        if(!mBleDeviceInfoArrayList.contains(item))
            mBleDeviceInfoArrayList.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;

        if(convertView == null)
        {
            convertView = mInflater.inflate(mLayout, parent, false);
        }



        return convertView;
    }


    public void addDevice(BluetoothDevice device)
    {
        if(!mBleDeviceArrayList.contains(device))
        {
            mBleDeviceArrayList.add(device);
        }
    }

    public BluetoothDevice getDevice(int position)
    {
        return mBleDeviceArrayList.get(position);
    }

    public int getBleDeviceCount()
    {
        return mBleDeviceArrayList.size();
    }

    public Object getBleDeviceItem(int i)
    {
        return mBleDeviceArrayList.get(i);
    }

    /*  BleDeviceScanActivity에서 최대 RSSI Beacon을 계산함
    public BleDeviceInfo getMaxRssiBeacon()
    {
        int pos = 0;
        int maxRssi = mBleDeviceInfoArrayList.get(0).rssi;

        for(int i = 1; i  < mBleDeviceInfoArrayList.size() ; i++)
        {
            if(maxRssi < mBleDeviceInfoArrayList.get(pos).rssi)
            {
                maxRssi = mBleDeviceInfoArrayList.get(pos).rssi;
                pos = i;
            }
        }
        return mBleDeviceInfoArrayList.get(pos);
    }
    */
}
