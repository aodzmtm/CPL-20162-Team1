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
import android.widget.Button;
import android.widget.TextView;

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


        TextView txtUuid = (TextView)convertView.findViewById(R.id.text_uuid);
        txtUuid.setText("UUID: " + mBleDeviceInfoArrayList.get(position).proximityUuid);

        TextView txtBdName = (TextView)convertView.findViewById(R.id.text_bd_name);
        txtBdName.setText("Device Name: " + mBleDeviceInfoArrayList.get(position).devName);

        TextView txtBdAddress = (TextView)convertView.findViewById(R.id.text_bd_address);
        txtBdAddress.setText("Dev Address: " + mBleDeviceInfoArrayList.get(position).devAddress);

        TextView txtMajor = (TextView)convertView.findViewById(R.id.text_major);
        txtMajor.setText("Major: " + String.valueOf(mBleDeviceInfoArrayList.get(position).major));

        TextView txtMinor = (TextView)convertView.findViewById(R.id.text_minor);
        txtMinor.setText("Minor: " + String.valueOf(mBleDeviceInfoArrayList.get(position).minor));

        TextView txtRssi = (TextView)convertView.findViewById(R.id.text_rssi);
        txtRssi.setText("RSSI: " + String.valueOf(mBleDeviceInfoArrayList.get(position).rssi) + " dbm");

        TextView txtTxPower = (TextView)convertView.findViewById(R.id.text_txpower);
        //txtTxPower.setText("Tx Power: " + String.valueOf(mBleDeviceInfoArrayList.get(position).measuredPower) + " dbm");
        txtTxPower.setText("Tx Power: " + String.valueOf(mBleDeviceInfoArrayList.get(position).txPower) + " dbm");      // changsu

        TextView txtDistance = (TextView)convertView.findViewById(R.id.text_distance);
        txtDistance.setText("Distance: " + String.valueOf(mBleDeviceInfoArrayList.get(position).distance) + " m (" + String.format("%.2f", mBleDeviceInfoArrayList.get(position).distance2) + "m)");

        TextView txtTimeout = (TextView)convertView.findViewById(R.id.text_timeout);
        txtTimeout.setText("Timeout: " + String.valueOf(mBleDeviceInfoArrayList.get(position).timeout));

        Button btnConnect = (Button)convertView.findViewById(R.id.button_connect);
        btnConnect.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v)
            {
                // connect 함수 연결
            }
        });


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
