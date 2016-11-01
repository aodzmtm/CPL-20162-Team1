package com.example.safelight;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.safelight.R.layout.activity_ble_device_scan;

public class BleDeviceScanActivity extends Activity {
    /*
        Constant Variables
     */

    public static String BEACON_UUID;
    public static Boolean saveRSSI;
    private static final long SCAN_PERIOD = 1000;       // 10초동안 SCAN 과정을 수행함
    private static final int REQUEST_ENABLE_BT = 1;
    private static final String TAG = "SCAN";
    private static final boolean IS_DEBUG = true;
    private static final long TIMEOUT_LIMIT = 20;
    private static final long TIMEOUT_PERIOD = 1000;
    //private static final boolean USING_WINI = true; // TI CC2541 사용: true

    // Socket 관련 상수
 //   private static final int SERVER_PORT = 8080;
 //   private static final String SERVER_IP = "ws://192.168.247.1/light_web/echo.do";
    private static final String URL = "ws://192.168.247.1:8080/light_web/echo.do";

//    private static final String DEVICE_ADDR = "D0:39:72:A3:E1:2E";
    final static int MSG_RECEIVED_ACK = 0x100;

    /*
        Class Instance Variables
     */
    private BleDeviceInfo mBleDeviceInfo;
    private BleDeviceListAdapter mBleDeviceListAdapter;
    private BleUtils mBleUtils;
    private ClientThread mClientThread;
    private BleSocketPacket mBleSocketPacket;

    private Send_StatePacket mStatePacket;
    /*
        Member Variables
     */
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BleDeviceInfo> mArrayListBleDevice;   // scan 후 검색된 pebBle 장비를 저장하는 array list
    private HashMap<String, BleDeviceInfo> mItemMap;
    private BleDeviceInfo mMaxRssiBeacon;
    //private Handler mHandler;
    boolean mScanning;

    /*
        Widgets
     */
    ListView mBleListView;

    SharedPreferences setting;                          //SharedPreferences : 파일형태로 데이터 저장해준다.
    //private LogFile logFile;
    //private LogExcelFile logExcelFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_ble_device_scan);

        mBleUtils = new BleUtils();                             //BleUtils: Byte 타입 변환과 거리 계산.
        mArrayListBleDevice = new ArrayList<BleDeviceInfo>();   //BleDeviceInfo : Divice 정보, Set, get, 거리계산, Address 같은지 검사
        mItemMap = new HashMap<String, BleDeviceInfo>();        //MAP : Key와 Value를 묶어서 (Key,Value). Hash : 방대한양의 데이터저장가능
        mMaxRssiBeacon = new BleDeviceInfo();

        mBleSocketPacket = new BleSocketPacket();

        initBluetoothAdapter();                                 // Bluetooth Adapter 설정 (support하는지 안 하는지 )

        setting = PreferenceManager.getDefaultSharedPreferences(this);

        BEACON_UUID = getBeaconUuid(setting);

        saveRSSI = setting.getBoolean("saveRSSI", true);

        /*
        logFile = new LogFile();
        logFile.init();
        logFile.openFileStream();
        */
//
//        logExcelFile = new LogExcelFile();
//        logExcelFile.init();

// //        activity_ble_device_scan.xml 문서의 listview
//        mBleListView = (ListView)findViewById(R.id.listview_blescan);
//        mBleDeviceListAdapter = new BleDeviceListAdapter(this, R.layout.ble_device_row,
//                mArrayListBleDevice, mItemMap);
//        mBleListView.setAdapter(mBleDeviceListAdapter);


        // mClientThread는 서버와 연결.
        mClientThread = new ClientThread(mSocketHandler,BleDeviceScanActivity.this, URL);
        mClientThread.setDaemon(true);
        mClientThread.start();
       // mClientThread.start();//서버로부터 메시지를 계속 받도록한다.

        mStatePacket = new Send_StatePacket();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ble_device_scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();

        BEACON_UUID = getBeaconUuid(setting);

        saveRSSI = setting.getBoolean("saveRSSI", true);

        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent  =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        //scanBleDevice(true);            // BLE 장치 검색\
        mHandler.sendEmptyMessageDelayed(0, SCAN_PERIOD);
        mTimeOut.sendEmptyMessageDelayed(0, TIMEOUT_PERIOD);


    }

    @Override
    protected void onPause() {
        super.onPause();

        //scanBleDevice(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //logFile.closeFileStream();

        if(!mArrayListBleDevice.isEmpty())
            mArrayListBleDevice.clear();
        // handler 의  종료와 콜백 함수 제거
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
        mHandler.removeMessages(0);
        mTimeOut.removeMessages(0);
    }

    /*****************************************************************************************
     *  Function: getBeaconUuid
     *
     *  Description
     *      - 사용 beacon의 UUID 값을 가져옴
     *
     *****************************************************************************************/
    public String getBeaconUuid(SharedPreferences pref)
    {
        String uuid = "";

        uuid = pref.getString("keyUUID", BluetoothUuid.WINI_UUID.toString());

        /*
        if(USING_WINI) {
            uuid = pref.getString("keyUUID", BluetoothUuid.WINI_UUID.toString());
            //uuid = BluetoothUuid.WINI_UUID.toString();
        }
        else {

            uuid = pref.getString("keyUUID", BluetoothUuid.WIZTURN_PROXIMITY_UUID.toString());
            //uuid = BluetoothUuid.WIZTURN_PROXIMITY_UUID.toString();
        }
        */

        return uuid;
    }

    /*****************************************************************************************
     *  Function: initBluetoothAdapter
     *
     *  Description
     *      - Bluetoth Adapter 설정
     *
     *****************************************************************************************/
    public void initBluetoothAdapter()
    {
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
        {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if(mBluetoothAdapter == null)
        {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }


    /*****************************************************************************************
     *  Bluetooth LE Device Scan Handler
     *
     *  Description
     *      - SCAN_PERIOD 간격으로 startLeScan()을 호출하여 beacon을 검색함
     *
     *****************************************************************************************/
    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if(mScanning)
            {
                mScanning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);//StartLeScan  : 비콘스캔
            mHandler.sendEmptyMessageDelayed(0, SCAN_PERIOD);
        }
    };

    /*****************************************************************************************
     *  Bluetooth LE Device Timeout Handler
     *
     *  Description
     *      - TIMEOUT_PERIOD(1000) 간격으로 어뎁터 리스트의 목록들의 timeout을 갱신
     *      - timeout이 0이 되면 목록에서 제거 시킨다.
     *
     *****************************************************************************************/

    private Handler mTimeOut = new Handler(){
        public void handleMessage(Message msg){
            Log.i("TAG","TIMEOUT UPDATE");

            int maxRssi = 0;
            int maxIndex = -1;

            //timeout counter update
            for (int i= 0 ; i < mArrayListBleDevice.size() ; i++){
                mArrayListBleDevice.get(i).timeout--;
                if(mArrayListBleDevice.get(i).timeout == 0){
                    mItemMap.remove(mArrayListBleDevice.get(i).devAddress);
                    mArrayListBleDevice.remove(i);
                }
                else{
                    if(mArrayListBleDevice.get(i).rssi > maxRssi || maxRssi == 0)
                    {
                        maxRssi = mArrayListBleDevice.get(i).rssi;
                        maxIndex = i;
                    }
                }
            }
            TextView text_max_dev = (TextView)findViewById(R.id.text_max_dev);

            if(maxIndex == -1) {
                text_max_dev.setText("No Dev");
            }
            else{
                text_max_dev.setText(maxIndex+1 +"th    "
                        + "major: " + mArrayListBleDevice.get(maxIndex).major + "  "
                        + "minor: " + mArrayListBleDevice.get(maxIndex).minor + "  "
                        + mArrayListBleDevice.get(maxIndex).getRssi() +"dbm");
            }
            mTimeOut.sendEmptyMessageDelayed(0,TIMEOUT_PERIOD);
        }
    };

    /*****************************************************************************************
     *  Socket Handler
     *
     *  Description
     *      - Client Socket에서 수신된 데이터를 UI쪽으로 전송하기 위해 사용함
     *
     *****************************************************************************************/
    private Handler mSocketHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {

            // 비컨에서 받아온 string 메시지
            String handlerText = (String)msg.obj;

            int[] date_int = new int[12];
            int[] ID_int = new int[12];
            char[] date_char = new char[12];
            char[] ID_char = new char[12];

            for(int i=0; i<12; i++){
                date_int[i] = Integer.parseInt(String.valueOf(handlerText.charAt(i+1)));
                date_char[i] = (char)date_int[i];
                ID_int[i] = Integer.parseInt(String.valueOf(handlerText.charAt(i+13)));
                ID_char[i] = (char)ID_int[i];
            }




            switch(msg.what)
            {
                case MSG_RECEIVED_ACK:
                    String date = String.valueOf(date_char);
                    String ID = String.valueOf(ID_char);
                    if(!mStatePacket.check_ID(ID))          //보안등 ID가 리스트에 없다 => 보안등 ID요청 ACK
                    {
                        mStatePacket.set_ID(ID);
                        // 서버로 비컨의 상태를 전송한다.
                        sendSocketMsg('E', ID, date, mStatePacket.get_State());
                    }
                    else if(mStatePacket.check_ID(ID))  //상태 정보에대한 ACK
                    {
                        mStatePacket.remove_ID(ID);
                    }
                    Log.d("Cleint", "msg: " + handlerText);
                    break;
            }
        }
    };

   /* public void scanBleDevice(boolean enable)
    {
        //UUID uuid[] = UUID.fromString(PEBBLE_UUID);
        UUID[] uuid = {UUID.fromString(PEBBLE_UUID)};


        if(enable)
        {
            mHandler.postDelayed(new Runnable() {
               public void run()
               {

                   mScanning = false;
                   mBluetoothAdapter.stopLeScan(mLeScanCallback);
               }
            }, SCAN_PERIOD);

            mScanning = true;

            mBluetoothAdapter.startLeScan(mLeScanCallback);
        }
        else
        {
            mScanning = true;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }*/

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            mScanning = true;
            getBleDeviceInfoFromLeScan(device, rssi, scanRecord);
                    /*
                        Exception 방지를 위해 runOnUiThread()에서 notifyDataSetChanged()를 호출함
                        - Only the original thread that created a view hierarchy can touch its views
                     */
            runOnUiThread(new Runnable() {
                public void run() {
                    mBleDeviceListAdapter.notifyDataSetChanged();
                    //mBleDeviceListAdapter.addOrUpdateItem();
                }
            });

        }
    };


    /*****************************************************************************************
     *  Function: getBleDeviceInfoFromLeScan
     *
     *  Description
     *      - scanRecord[] 데이터를 파싱하여
     *        proximityUUID, Major, Minor, Measured Power, distance 값을 구함
     *
     *****************************************************************************************/
    private void getBleDeviceInfoFromLeScan(BluetoothDevice device, int rssi, byte[] scanRecord)
    {
        String devName;
        String devAddress;
        String scanRecordAsHex;     // 24byte
        String proximityUUID;       // 12 + 5 characters
        String Date;
        String State;

        int rssiValue = rssi;
        devName = device.getName();
        if(devName == null)
            devName = "Unknown";

        devAddress = device.getAddress();
        if(devAddress == null)
            devAddress = "Unknown";

        if(!IS_DEBUG) {
            Log.d(TAG, "getBleDeviceInfoFromLeScan() : rssi: " + rssi +
                    ", addr: " + devName +
                    ", name: " + devAddress);
        }

        //이 비교부분을 위로 올리자.. 일치 하지않으면 뭐할 연산하나....
        scanRecordAsHex = mBleUtils.ByteArrayToString(scanRecord);

        //24byte
        Log.i("Recode",scanRecordAsHex);
        proximityUUID = String.format("%s-%s-%s-%s-%s",
                scanRecordAsHex.substring(18, 26),
                scanRecordAsHex.substring(26, 30),
                scanRecordAsHex.substring(30, 34),
                scanRecordAsHex.substring(34, 38),
                scanRecordAsHex.substring(38, 50));

        Date = scanRecordAsHex.substring(50,56);
        State= scanRecordAsHex.substring(56,60);
        mStatePacket.set_State(State);
//        major = mBleUtils.byteToInt(scanRecord[25], scanRecord[26]);
//        minor = mBleUtils.byteToInt(scanRecord[27], scanRecord[28]);

 //       txPower = scanRecord[29];

        //Log.d(TAG, "proximityUUID: " + proximityUUID);
        //Log.i("DATE","!"+Date);
        //Log.i("STATE","!"+State);

        if(proximityUUID.equals(BEACON_UUID) || proximityUUID.equals(BluetoothUuid.WIZTURN_PROXIMITY_UUID.toString()) || proximityUUID.equals(BluetoothUuid.WINI_UUID.toString()) )
        {
            try {
                Log.d(TAG, "Found Pebble UUID: " + proximityUUID);

                // 가장 근거리의 Beacon 정보를 서버로 전송함
                sendSocketMsg('R', devAddress, Date, State);

            }catch(Exception ex)
            {
                Log.e("Error", "Exception: " + ex.getMessage());
            }

        }
    }

    /*****************************************************************************************
     *  Function: updateBleDeviceList
     *
     *  Description
     *      - GUI Custom List View에 beacon 정보를 새롭게 추가하거나 업데이트 함
     *
     *****************************************************************************************/
//    public void updateBleDeviceList(BleDeviceInfo item)
//    {
//        int index = 0;
//        boolean foundItem = false;
//        int KalmanRSSI =0;
//        /**
//         * HashMap의 key값에 동일한 device address가 있는 경우: update 수행
//         */
//        if(mItemMap.containsKey(item.devAddress))
//        {
//
////            mItemMap.get(item.devAddress).rssi = item.rssi;
//            mItemMap.get(item.devAddress).rssi = (int)mItemMap.get(item.devAddress).rssiKalmanFileter.update(item.rssi);
//            KalmanRSSI = mItemMap.get(item.devAddress).rssi;
////            mItemMap.get(item.devAddress).distance = item.distance;
////            mItemMap.get(item.devAddress).distance2 = item.distance2;
//            mItemMap.get(item.devAddress).distance =  mBleUtils.getDistance(KalmanRSSI, item.txPower);
//            mItemMap.get(item.devAddress).distance2 =  mBleUtils.getDistance_20150515(KalmanRSSI, item.txPower);
//
//            mItemMap.get(item.devAddress).timeout = item.timeout;
//
//            Log.d("Debug", "Major: " + item.major +
//                    ", Minor: " + item.minor +
//                    ", rssi: " + KalmanRSSI +
//                    ", distance: " + item.distance);
//        }
//        else
//        {
//            /**
//             *  HashMap에 해당 item의 device address가 없는 경우, 추가함
//             *  key값: devAddress
//             */
//            mArrayListBleDevice.add(item);
//            mItemMap.put(item.devAddress, item);
//        }
//
//        if(saveRSSI) {
//                    /*
//                    logFile.recodeLogFile(
//                            "dev name: " + devName +
//                                    ", addr: " + devAddress +
//                                    ", major: " + major +
//                                    ", minor: " + minor +
//                                    ", rssi: " + rssi +
//                                    ", txPower: " + txPower +
//                                    ", distance: " + distance + "\n\n");
//
//                    */
//            if(KalmanRSSI == 0)
//                KalmanRSSI = item.rssi;
//            logExcelFile.writeLog(item.devAddress, String.valueOf(item.rssi),String.valueOf(KalmanRSSI));
//        }
//
//        mMaxRssiBeacon = getMaxRssiBeacon();
//
//        // 가장 근거리의 Beacon 정보를 서버로 전송함
//        sendSocketMsg('R', mMaxRssiBeacon.devAddress, mMaxRssiBeacon.major, mMaxRssiBeacon.minor);
//    }

    /*****************************************************************************************
     *  Function: getMaxRssiBeacon
     *
     *  Description
     *      - ArrayList에 저장된 Beacon정보들 중에 RSSI값이 가장 큰 Beacon을 구함
     *      - RSSI는 음수값이 더 큼
     *
     *****************************************************************************************/
    public BleDeviceInfo getMaxRssiBeacon()
    {
        int pos = 0;
        int maxRssi = mArrayListBleDevice.get(0).rssi;

        for(int i = 1; i < mArrayListBleDevice.size(); i++)
        {
            if(maxRssi > mArrayListBleDevice.get(i).rssi)
            {
                pos = i;
                maxRssi = mArrayListBleDevice.get(i).rssi;
            }
        }

        return mArrayListBleDevice.get(pos);
    }
    /***************************************************************************************
     *   Beacon의 정보를 서버로 전송함
     *  - Device Address, Major, Minor 값 전송
     ***************************************************************************************/
    private void sendSocketMsg(char cmd, String devNum, String Date, String State) {
        Log.i("Check", devNum + ";" + Date + ";" + State);
        String sendMsg = mBleSocketPacket.makeRequestMsg(cmd, devNum, Date, State);
        if (sendMsg != null) {
            mClientThread.mSocket.send(sendMsg);

            Log.d("Client", "sendmsg: " + sendMsg);
        }
    }

}
