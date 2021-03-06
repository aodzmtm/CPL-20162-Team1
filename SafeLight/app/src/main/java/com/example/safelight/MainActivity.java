package com.example.safelight;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    // 클래스 인스턴스
    private BluetoothAdapter mBluetoothAdapter;
    private BleDeviceListAdapter mBLeDeviceListAdapter;
    private Send_StatePacket mStatePacket;
    private BleSocketPacket mBleSocketPacket;
    private ClientThread mClientThread;
    private BleUtils mBleUtils;
    private BackPressCloseHandler backPressCloseHandler;

    private ArrayList<BleDeviceInfo> mArrayListBleDevice;
    private HashMap<String, BleDeviceInfo> mItemMap;
    private BleDeviceInfo mMaxRssiBeacon;

    // 마커 배열 할당에 쓰일 상수
    private static int rows_sec = 0;
    private static int rows_mark = 0;
    private static int rows_sec_server = 0;
    private static int rows_mark_server = 0;
    private static double[][] markers_sec = null;
    private static double[][] markers = null;
    private static double[][] markers_sec_server = null;
    private static double[][] markers_server = null;

    private static boolean isMarker = true;
    private static boolean onService = false;

    // 컨스턴트
    private static final String IP = "knuce.iptime.org";     // IP주소 설정하기
    private static final String URL = "ws://"+IP+":8080/light_web/echo.do";
    private static final int REQUEST_ENABLE_BT = 1; // 블루투스 ON 요청 횟수
    private static final long SCAN_PERIOD = 1000;       // 10초동안 SCAN 과정을 수행함
    final static int MSG_RECEIVED_ACK = 0x100;
    private static final boolean USING_WINI = true; // 비콘 종류에 따라 true or false

    // GPS 현재 위치 받아올 x,y 좌표
    private static double x = 0.0;
    private static double y = 0.0;

    String bAddress;
    String bDate;
    String bState;

    boolean isMarsh = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    boolean mScanning;
    public static String BEACON_UUID;
    public static  Boolean saveRSSI;
    SharedPreferences setting;

    ImageButton mBtnScan;
    ImageButton mBtnRoute;
    ImageButton mBtnRefresh;
    ImageButton mBtnReport;
    ImageButton mBtnInfo;
    ImageButton mBtnActive;

    private CustomDialogFragment mCustomDialog;

    //permission
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        // 처음 실행시 먼저 Map fragment 정보를 layout 파일로부터 읽어들인다.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // 다음으로 로컬 파일에 저장돼있던 보안등고 마커 정보 데이터를 읽어온다.
        loadTxt();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // 비콘 통신 관련
        setting = PreferenceManager.getDefaultSharedPreferences(this);
        BEACON_UUID = getBeaconUuid(setting);
        saveRSSI = setting.getBoolean("saveRSSI", true);


        // mClientThread는 서버와 연결.
        mClientThread = new ClientThread(mSocketHandler,MainActivity.this, URL);
        mClientThread.setDaemon(true);
        mClientThread.start();
        mBleSocketPacket = new BleSocketPacket();
        mStatePacket = new Send_StatePacket();

        mBleUtils = new BleUtils();
        mItemMap = new HashMap<String, BleDeviceInfo>();
        mMaxRssiBeacon = new BleDeviceInfo();
        mArrayListBleDevice = new ArrayList<BleDeviceInfo>();

        mBtnScan = (ImageButton) findViewById(R.id.btn_Scan);
        mBtnScan.setOnClickListener(mClickListener);

        mBtnActive = (ImageButton) findViewById(R.id.btn_service);
        mBtnActive.setOnClickListener(mClickListener);

        mBtnRoute = (ImageButton) findViewById(R.id.btn_Route);
        mBtnRoute.setOnClickListener(mClickListener);

        mBtnRefresh = (ImageButton) findViewById(R.id.btn_Refresh);
        mBtnRefresh.setOnClickListener(mClickListener);

        mBtnReport = (ImageButton) findViewById(R.id.btn_Report);
        mBtnReport.setOnClickListener(mClickListener);

        mBtnInfo = (ImageButton) findViewById(R.id.btn_Info);
        mBtnInfo.setOnClickListener(mClickListener);

        backPressCloseHandler = new BackPressCloseHandler(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        killService();
    }

    // 뒤로가기 두 번 클릭시 종료하는 기능
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    Button.OnClickListener mClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            switch(v.getId())
            {
                case R.id.btn_Scan: // 비컨 스캔
                    ProgressDial(v.getId());
                    scanBLE();
                    break;
                case R.id.btn_service://서비스작동
                    activateService();
                    finish();
                    break;
                case R.id.btn_Refresh://보안등정보동기화
                    ProgressDial(v.getId());
                    mMap.clear();
                    getInfo();
                    print_updatedMarker(mMap);
                    break;
                case R.id.btn_Route://신고된길, 보안등 필터링
                    mMap.clear();
                    print_onlyMarker(mMap);
                    break;
                case R.id.btn_Report://신고 기능
                    // GPS 통신 관련 매니저 생성
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    alertShow(lm);
                    break;
                case R.id.btn_Info:
                    mCustomDialog = new CustomDialogFragment();
                    mCustomDialog.show(getFragmentManager(),"Info");
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 구글맵 위에 디자인 그리는 형태로 tile 사용
        //TileProvider coorTileProvider = new CoordTileProvider(getApplicationContext());
        //mMap.addTileOverlay(new TileOverlayOptions().tileProvider(coorTileProvider));

        // 테스트하는 보안등 중심 화면 설정
        LatLng eng_9 = new LatLng(35.887025, 128.609459);                    // 테스트 장소
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eng_9, 17));       // 화면 배율

        // 구글맵 내에 마커 찍기
        print_Marker(mMap);
    }

    // 백그라운드 서비스 실행
    public void activateService(){
        onService = true;
        Toast.makeText(getApplicationContext(),"서비스를 시작합니다",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this,MyService.class);
        startService(intent);
    }
    public  void killService(){
        onService = false;
        Toast.makeText(getApplicationContext(),"서비스를 종료합니다.",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this,MyService.class);
        stopService(intent);
    }

    private void alertShow(final LocationManager lm){
        final AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("어두운 길로 신고하시겠습니까?").setCancelable(
                false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'Yes' Button
                        try{
                            //ProgressDial(id);
                            // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기
                            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                                    100, // 통지사이의 최소 시간간격 (miliSecond)
                                    1, // 통지사이의 최소 변경거리 (m)
                                    mLocationListener);
                            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                                    100, // 통지사이의 최소 시간간격 (miliSecond)
                                    1, // 통지사이의 최소 변경거리 (m)
                                    mLocationListener);
                            addCount(lm);
                        }catch(SecurityException ex){
                        }

                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("Title");
        // Icon for AlertDialog
        alert.setIcon(R.drawable.cast_ic_notification_on);
        alert.show();
    }



    // 최초 앱 실행시 로컬에서 불러온 정보를 토대로 구글맵 상에 보안등, 마커를 출력한다.
    public void print_Marker(final GoogleMap map) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                // 보안등
                for (int i = 0; i < rows_sec; i++) {
                    add_SecLight(map, markers_sec[i][1], markers_sec[i][2], markers_sec[i][3]);
                }
                // 마커
                for (int i = 0; i < rows_mark; i++) {
                    add_Marker(map, markers[i][1], markers[i][2], markers[i][3]);
                }

            }

        });
    }

    // 서버와 동기화를 통해 얻어온 정보로 구글맵 상에 마커와 보안등을 새로 출력한다.
    public void print_updatedMarker(final GoogleMap map) {

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            public void run() {

                // 보안등
                for (int i = 0; i < rows_sec_server; i++) {
                    add_SecLight(map, markers_sec_server[i][1], markers_sec_server[i][2], markers_sec_server[i][3]);
                }

                // 마커
                for (int i = 0; i < rows_mark_server; i++) {
                    add_Marker(map, markers_server[i][1], markers_server[i][2], markers_server[i][3]);
                }

            }

        }, 3500);
    }

    // 필터링 기능을 작동했을때 마커, 보안등의 정보를 번갈아 출력한다.
    public void print_onlyMarker(final GoogleMap map) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                if(isMarker){
                    // 필터링을 위해 마커만 표시
                    for (int i = 0; i < rows_mark; i++) {
                        add_Marker(map, markers[i][1], markers[i][2], markers[i][3]);
                    }
                    isMarker = false;
                }
                else{
                    // 보안등
                    for (int i = 0; i < rows_sec; i++) {
                        add_SecLight(map, markers_sec[i][1], markers_sec[i][2], markers_sec[i][3]);
                    }
                    isMarker = true;
                }

            }

        });
    }

    // 보안등 마커(아이콘) 출력 부분
    // add_SecLight에는 인자로 x, y 좌표와 상태정보가 넘어간다. 상태 정보에 따라 보안등 아이콘 색깔이 달라진다.
    public void add_SecLight(GoogleMap map, double x, double y, double num){

        LatLng obj = new LatLng(x,y);
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.light_green);

        int height=75, width=75;

        // num 1 : 정상
        if(num==0.0){
            bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.light_green);
        }
        // num 2: 고장
        else if(num==1.0){
            bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.light_black);
        }
        // num 3: 수리중
        else if(num==2.0){
            bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.light_red);
        }

        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        map.addMarker(new MarkerOptions().position(obj)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
    }

    // 신고된 길 마커 출력 부분
    // add_Marker에는 인자로 x, y 좌표와 count 값이 넘어간다. count 값에 따라 지도상에 찍을 원 형태의 마커 반지름이 달라진다.
    public void add_Marker(GoogleMap map, double x, double y, double count){

        if(count>0) {

            // circle settings
            int radiusM = (int) count;
            LatLng latLng = new LatLng(x, y);

            // draw circle
            int d = 250; // diameter
            Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bm);
            Paint p = new Paint();
            p.setColor(Color.RED);
            c.drawCircle(d / 2, d / 2, d / 2, p);

            // generate BitmapDescriptor from circle Bitmap
            BitmapDescriptor bmD = BitmapDescriptorFactory.fromBitmap(bm);

            // mapView is the GoogleMap
            map.addGroundOverlay(new GroundOverlayOptions().
                    image(bmD).
                    position(latLng, radiusM * 2, radiusM * 2).
                    transparency(0.4f));
        }
    }

    // 로딩이 필요한 경우 Progress Dialog를 출력한다.
    public void ProgressDial(int id){
        final ProgressDialog dialog;

        switch(id) {
            case R.id.btn_Scan:
                dialog = ProgressDialog.show(MainActivity.this, "", "스캔중 입니다. 잠시 기다려주세요", true);
                dialog.show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 3500);//1000=1s
                break;
            case R.id.btn_Refresh:
                dialog = ProgressDialog.show(MainActivity.this, "", "데이터 동기화중입니다. 잠시 기다려주세요", true);
                dialog.show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 3500);//1000=1s
                break;
        }
    }


    private View.OnClickListener Listener = new View.OnClickListener() {
        public void onClick(View v) {
            mCustomDialog.dismiss();
        }
    };


    // 비컨 스캔하는 핸들러 (LeScan 함수 실행)
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

    // 소켓 통신 담당 핸들러
    private Handler mSocketHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            String handlerText = (String)msg.obj;
            String bID = handlerText.substring(14,20);

            switch(msg.what)
            {
                case MSG_RECEIVED_ACK:
                    if(!mStatePacket.check_ID(bID))       // 최초에 앱에서 서버로 보안등 ID 요청을 한 경우 서버로부터 ID가 응답으로 온다.
                    {
                        mStatePacket.set_ID(bID);         // 앱에서는 서버에서 받은 ID 값을 등록한다.
                        sendSocketMsg('E', bID, bDate, bState); // 등록한 ID 정보와 함께 스캔한 보안등 상태 정보를 다시 서버로 보낸다.
                    }
                    else if(mStatePacket.check_ID(bID))  // 앱에서 상태 정보를 서버로 넘겼을때는 응답 형태가 ID가 아닌 ACK로 오게 된다.
                    {
                        mStatePacket.remove_ID(bID);      // 이 경우에는 등록했던 ID를 삭제하고 통신을 마친다.
                    }
                    break;
            }
        }
    };

    // 블루투스 환경 설정 및, 비컨 스캔 함수 호출
    public void scanBLE() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 폰이 BLE 지원하지 않는 경우 이용할 수 없다.
        if(mBluetoothAdapter == null){                          //if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        // 블루투스 어댑터 초기화
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // 블루투스 ON 체크 및 요청
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        if(isMarsh)
        {
            Permissioncheck();      // SDK 버전에 따라 권한 체크를 해줘야 한다.
        }
        scanLeDevice(true);         // 블루투스 환경 설정이 끝났으면 비컨 스캔을 실시한다.
    }


    // 비컨 스캔 함수
    public void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    getBleDeviceInfoFromLeScan(device, rssi, scanRecord);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("onLeScan", device.toString());
                        }
                    });
                }
            };


    // 비컨 스캔을 통해 얻은 데이터를 파싱하고 서버로 전송한다.
    private void getBleDeviceInfoFromLeScan(BluetoothDevice device, int rssi, byte[] scanRecord)
    {
        String devName;
        String scanRecordAsHex;     // 24byte
        String proximityUUID;       // 12 + 5 characters

        int rssiValue = rssi;
        devName = device.getName();
        if(devName == null)
            devName = "Unknown";

        // MAC Address 파싱
        String[] tempAddr = device.getAddress().split(":");
        bAddress = String.valueOf(mBleUtils.StringArrtoCharArr(tempAddr));

        // bAddress = device.getAddress();
        if(bAddress == null)
            bAddress = "Unknown";
        Log.i("address: ", bAddress);

        scanRecordAsHex = mBleUtils.ByteArrayToString(scanRecord);

        //24byte
        proximityUUID = String.format("%s-%s-%s-%s-%s",
                scanRecordAsHex.substring(18, 26),
                scanRecordAsHex.substring(26, 30),
                scanRecordAsHex.substring(30, 34),
                scanRecordAsHex.substring(34, 38),
                scanRecordAsHex.substring(38, 50));

        // 현재시간 받아오기
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy:ss");
        Date currentYear = new Date();
        String mDate = mSimpleDateFormat.format ( currentYear );

        // 년도
        String bYear = mDate.substring(2,4);
        // 월
        int tempMonth = BleUtils.hex2dec(scanRecordAsHex.substring(51,52));
        String bMonth = mBleUtils.dec2str(tempMonth);
        // 일
        int tempDay= BleUtils.hex2dec(scanRecordAsHex.substring(53,54));
        String bDay = mBleUtils.dec2str(tempDay);
        // 시
        int tempHour = BleUtils.hex2dec(scanRecordAsHex.substring(55,56));
        String bHour = mBleUtils.dec2str(tempHour);
        // 분
        int tempMin = BleUtils.hex2dec(scanRecordAsHex.substring(57,58));
        String bMin = mBleUtils.dec2str(tempMin);
        // 초
        String bSec = mDate.substring(5,7);

        // 파싱한 최종 날짜 및 시간
        bDate = bYear + bMonth + bDay + bHour + bMin + bSec;

        // 비컨 상태정보 파싱
        int tempState1 = mBleUtils.hex2dec(scanRecordAsHex.substring(58,60));
        bState = mBleUtils.parseState(mBleUtils.byte2bitset((byte)tempState1));

        // 파싱한 상태정보 값 설정
        mStatePacket.set_State(bState);

        sendSocketMsg('R', bAddress, bDate, null);            // 서버로 파싱한 정보를 전송한다. cmd는 'R'

    }
    public String getBeaconUuid(SharedPreferences pref)
    {
        String uuid = "";

        if(USING_WINI) {
            uuid = pref.getString("keyUUID", BluetoothUuid.WINI_UUID.toString());
            //uuid = BluetoothUuid.WINI_UUID.toString();
        }
        else {
            uuid = pref.getString("keyUUID", BluetoothUuid.WIZTURN_PROXIMITY_UUID.toString());
            //uuid = BluetoothUuid.WIZTURN_PROXIMITY_UUID.toString();
        }

        return uuid;
    }

    // 비컨 정보 전송하는 소켓 함수
    private void sendSocketMsg(char cmd, String devNum, String Date, String State) {
        String sendMsg = mBleSocketPacket.makeRequestMsg(cmd, devNum, Date, State);
        if (sendMsg != null) {
            mClientThread.mSocket.send(sendMsg);
        }
    }


    // 동기화시 http 연결을 통해 json 타입의 비컨 및 신고지역 정보를 받아온다.
    public void getInfo(){
        //http post요청 코드
        Thread thread = new Thread() {



            @Override

            public void run() {

                // http 연결 부분
                String urlString = "http://"+IP+":8080/light_web/mobileLampData.do";
                String urlString2 = "http://"+IP+":8080/light_web/mobileDangerData.do";
                //adding some data to send along with the request to the server

                URL url, url2;
                try {
                    url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(10 * 1000);
                    conn.setReadTimeout(10 * 1000);

                  /*  conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");*/
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestMethod("POST");
                    // this is were we're adding post data to the request

                    String line = null;
                    String json ="";


                    url2 = new URL(urlString2);
                    HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                    conn2.setDoOutput(true);
                    conn2.setDoInput(true);
                    conn2.setRequestMethod("POST");

                    String line2 = null;
                    String json2 ="";


                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line = reader.readLine()) != null) {

                            json+=line;

                        }

                        BufferedReader reader2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
                        while ((line2 = reader2.readLine()) != null) {

                            json2+=line2;

                        }

                        JSONParser jsonParser = new JSONParser();
                        // JSON데이터를 넣어 JSON Object 로 만들어 준다.
                        JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
                        // books의 배열을 추출
                        JSONArray lampInfoArray = (JSONArray) jsonObject.get("LampVo");


                        JSONObject jsonObject2 = (JSONObject) jsonParser.parse(json2);
                        // books의 배열을 추출
                        JSONArray markerInfoArray = (JSONArray) jsonObject2.get("NeedLocationVo");

                        rows_sec_server = lampInfoArray.size();
                        rows_mark_server = markerInfoArray.size();

                        // 불러온 만큼 보안등, 마커 배열 동적 생성
                        markers_sec_server = new double[lampInfoArray.size()][4];
                        markers_server = new double[markerInfoArray.size()][4];

                        // 보안등 정보 입력, 보안등은 [][0]번 값이 0이다.
                        for (int i = 0; i < lampInfoArray.size(); i++) {
                            // 배열 안에 있는것도 JSON형식 이기 때문에 JSON Object 로 추출
                            JSONObject lampVoObject = (JSONObject) lampInfoArray.get(i);

                            // 보안등의 상태는 [][3]번 값을 구분하는데, 0이면 정상, 1이면 완전 고장, 2면 부분 고장인 상태다.

                            // 정상인 보안등일 경우
                            if(Integer.valueOf(lampVoObject.get("power_off").toString())==0
                                    & Integer.valueOf(lampVoObject.get("abnormal_blink").toString())==0
                                    & Integer.valueOf(lampVoObject.get("short_circuit").toString())==0
                                    & Integer.valueOf(lampVoObject.get("lamp_failure").toString())==0
                                    & Integer.valueOf(lampVoObject.get("lamp_state").toString())==0)
                            {
                                markers_sec_server[i][0] = 0.0;
                                markers_sec_server[i][1] = Double.valueOf(lampVoObject.get("x").toString());
                                markers_sec_server[i][2] = Double.valueOf(lampVoObject.get("y").toString());
                                markers_sec_server[i][3] = 0.0;
                            }
                            // 완전히 고장난 보안등일 경우
                            else if(Integer.valueOf(lampVoObject.get("power_off").toString())!=0
                                    & Integer.valueOf(lampVoObject.get("abnormal_blink").toString())!=0
                                    & Integer.valueOf(lampVoObject.get("short_circuit").toString())!=0
                                    & Integer.valueOf(lampVoObject.get("lamp_failure").toString())!=0
                                    & Integer.valueOf(lampVoObject.get("lamp_state").toString())!=0)
                            {
                                markers_sec_server[i][0] = 0.0;
                                markers_sec_server[i][1] = Double.valueOf(lampVoObject.get("x").toString());
                                markers_sec_server[i][2] = Double.valueOf(lampVoObject.get("y").toString());
                                markers_sec_server[i][3] = 1.0;
                            }
                            // 이상이 있는 보안등일 경우
                            else
                            {
                                markers_sec_server[i][0] = 0.0;
                                markers_sec_server[i][1] = Double.valueOf(lampVoObject.get("x").toString());
                                markers_sec_server[i][2] = Double.valueOf(lampVoObject.get("y").toString());
                                markers_sec_server[i][3] = 2.0;
                            }
                        }

                        // 마커 정보 입력, 마커는 [][0]번 값이 1이다.
                        // [][3]번 값은 신고된 만큼의 count 값이다.
                        for (int i = 0; i < markerInfoArray.size(); i++) {
                            JSONObject markerObject = (JSONObject) markerInfoArray.get(i);
                            markers_server[i][0] = 1.0;
                            markers_server[i][1] = Double.valueOf(markerObject.get("x").toString());
                            markers_server[i][2] = Double.valueOf(markerObject.get("y").toString());
                            markers_server[i][3] = Double.valueOf(markerObject.get("count").toString());
                        }

                        // 서버에서 받아온 값을 로컬 파일에 저장한다.
                        saveText(markers_sec_server, markers_server);

                    } catch (Exception e) {
                        System.out.println("Error reading JSON string:" + e.toString());
                    }

                } catch (Exception e) {
                    System.out.println("error");
                    //handle the exception !
                    //Log.d(TAG,e.getMessage());
                }

            }
        };

        thread.start();
    }

    // REPORT 기능 수시 GPS listener를 통해 현재 좌표를 얻고,
    // 가장 가까이에 있는 신고 지역의 좌표를 서버로 전송한다.
    // (서버에서는 해당 좌표에 count 값을 1 증가시킨다)
    private void addCount(LocationManager lm){
        final double xtoServ, ytoServ;
        int num=0;
        double sum = Math.pow(Math.abs(x-markers[0][1]),2)+Math.pow(Math.abs(y-markers[0][2]),2);
        double temp = 0.0;

        // 가장 가까이에 있는 신고 지역 좌표 구하는 부분
        for(int i=0;i<markers.length;i++){
            temp = Math.pow(Math.abs(x-markers[i][1]),2)+Math.pow(Math.abs(y-markers[i][2]),2);
            if(sum > temp){
                sum = temp;
                num = i;
            }
        }
        System.out.println("x="+x+"&y="+y);
        xtoServ = markers[num][1];
        ytoServ = markers[num][2];
        // GPS 자원 해제
        try{
            lm.removeUpdates(mLocationListener);

        }catch(SecurityException ex){
        }

        // 좌표 정보를 넘겨주기 위해 서버와 HTTP 연결을 한다.
        Thread thread = new Thread() {

            @Override

            public void run() {
                String urlString = "http://"+IP+":8080/light_web/mobileDanger.do";
                InputStream myInputStream = null;
                StringBuilder sb = new StringBuilder();
                //adding some data to send along with the request to the server
                sb.append("x="+xtoServ+"&y="+ytoServ);
                URL url;
                try {
                    url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    OutputStreamWriter wr = new OutputStreamWriter(conn
                            .getOutputStream());
                    // this is were we're adding post data to the request
                    wr.write(sb.toString());
                    wr.flush();
                    myInputStream = conn.getInputStream();
                    wr.close();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "http 요청 실패", Toast.LENGTH_LONG).show();
                    //handle the exception !
                    //Log.d(TAG,e.getMessage());
                }
            }
        };
        thread.start();
    }


    // GPS 좌표 변화 감지 리스너 ( REPORT 기능을 수행할 때만 작동한다 )
    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("test", "onLocationChanged, location:" + location);
            x = location.getLongitude(); //경도
            y = location.getLatitude();   //위도
        }
        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };

    //   public static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";

    // SDK 버전에 따라 권한 체크가 필요한 경우가 있다.
    public void Permissioncheck(){
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {

            // 이 권한을 필요한 이유를 설명해야하는가?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다
                // 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다

            }
        }
        else{
            scanLeDevice(true);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    scanLeDevice(true);
                    // 권한 허가
                    // 해당 권한을 사용해서 작업을 진행할 수 있습니다
                } else {
                    // 권한 거부
                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
                }
                return;
        }
    }

    // 기존에 로컬에 저장된 보안등 및 마커 정보 불러오기
    public void loadTxt(){
        Thread thread = new Thread(){
            public void run(){
                try{
                    // 파일 이름은 lamp.txt
                    FileInputStream fis = openFileInput("lamp.txt");
                    byte[] data = new byte[fis.available()];
                    while(fis.read(data) != -1){
                        String s = new String(data);
                        String[] temp = s.split("\n");
                        for (int i=0; i<temp.length;i++) {
                            // 좌표값이 보안등인 경우
                            if (Double.valueOf(temp[i].substring(0, 1)) == 0.0) {
                                rows_sec++;
                            }
                            // 좌표값이 마커인 경우
                            else if (Double.valueOf(temp[i].substring(0, 1)) == 1.0) {
                                rows_mark++;
                            }
                        }
                    }
                    fis.close();

                    // 파일에 저장된 갯수만큼 보안등과 신고지역 배열 생성
                    // 보안등은 markers_sec
                    // 신고지역은 markers
                    markers_sec = new double[rows_sec][4];
                    markers = new double[rows_mark][4];

                    rows_sec=0; rows_mark=0;

                    // BufferedReader에서 seek 기능이 없으므로, 배열 동적 할당 후에
                    // 파일을 다시 불러와서 값을 생성했던 배열에 값을 채워넣는다.
                    FileInputStream fis2 = openFileInput("lamp.txt");
                    byte[] data2 = new byte[fis2.available()];
                    while(fis2.read(data2) != -1){
                        String s = new String(data2);
                        String[] temp = s.split("\n");
                        for (int i=0; i<temp.length;i++){

                            /* 1열값이 0.0: 보안등, 1.0: 마커
                               2열값  : x 좌표
                               3열값  : y 좌표
                               4열값  : 보안등인 경우 상태(0: 정상, 1: 수리중 2: 고장)
                                        마커인 경우 report 누적값                       */
                            // 좌표값이 보안등인 경우
                            if(Double.valueOf(temp[i].substring(0, 1))==0.0){
                                markers_sec[rows_sec][0] = Double.valueOf(temp[i].substring(0, 1));
                                markers_sec[rows_sec][1] = Double.valueOf(temp[i].substring(2, 11));
                                markers_sec[rows_sec][2] = Double.valueOf(temp[i].substring(12, 22));
                                markers_sec[rows_sec][3] = Double.valueOf(temp[i].substring(23, temp[i].length()));
                                rows_sec++;
                            }
                            // 좌표값이 마커인 경우
                            else if(Double.valueOf(temp[i].substring(0, 1))==1.0){
                                markers[rows_mark][0] = Double.valueOf(temp[i].substring(0, 1));
                                markers[rows_mark][1] = Double.valueOf(temp[i].substring(2, 11));
                                markers[rows_mark][2] = Double.valueOf(temp[i].substring(12, 22));
                                markers[rows_mark][3] = Double.valueOf(temp[i].substring(23, temp[i].length()));
                                rows_mark++;
                            }
                        }
                    }
                    x = markers[0][1];
                    y = markers[0][2];
                    fis2.close();

                }catch (Exception e){               // 초기 마커 데이터가 없는 경우, 자동으로 동기화를 시작한다.
                    //getInfo();
                    //print_updatedMarker(mMap);
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    // 동기화된 정보를 기존 로컬 파일(lamp.txt) 에 덮어쓰기
    public void saveText(final double[][] array_light, final double[][] array_marker){
        Thread thread = new Thread(){
            public void run(){
                try{
                    FileOutputStream fos = openFileOutput("lamp.txt", Context.MODE_PRIVATE);
                    for(int i=0;i<array_light.length;i++){
                        StringBuilder temp = new StringBuilder("");
                        temp.append((int)array_light[i][0]+":"+
                                String.format("%.6f",array_light[i][1])+":"+
                                String.format("%.6f",array_light[i][2])+":"+
                                (int)array_light[i][3]+"\r\n");
                        fos.write(temp.toString().getBytes());
                        System.out.println(temp.toString());
                    }
                    for(int i=0;i<array_marker.length;i++){
                        StringBuilder temp = new StringBuilder("");
                        temp.append((int)array_marker[i][0]+":"+
                                String.format("%.6f",array_marker[i][1])+":"+
                                String.format("%.6f",array_marker[i][2])+":"+
                                (int)array_marker[i][3]+"\r\n");
                        fos.write(temp.toString().getBytes());
                        System.out.println(temp.toString());
                    }
                    fos.close();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}