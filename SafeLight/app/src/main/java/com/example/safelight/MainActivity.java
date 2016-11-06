package com.example.safelight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
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

    private ArrayList<BleDeviceInfo> mArrayListBleDevice;
    private HashMap<String, BleDeviceInfo> mItemMap;
    private BleDeviceInfo mMaxRssiBeacon;

    // 컨스턴트
    private static final String URL = "ws://192.168.0.4:8080/light_web/echo.do";
    private static final int REQUEST_ENABLE_BT = 1; // 블루투스 ON 요청 횟수
    private static final long SCAN_PERIOD = 1000;       // 10초동안 SCAN 과정을 수행함
    final static int MSG_RECEIVED_ACK = 0x100;
    private static final boolean USING_WINI = true; // 비콘 종류에 따라 true or false
    private static final String TAG = "SCAN";
    private static final boolean IS_DEBUG = true;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    Button.OnClickListener mClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            switch(v.getId())
            {
                case R.id.btn_Scan:
                    scanBLE();
                    break;
                case R.id.btn_service://서비스작동
                    activateService(v);
                    finish();
                    break;
                case R.id.btn_Refresh://보안등정보동기화
                    break;
                case R.id.btn_Route://신고된길만표시
                    break;
                case R.id.btn_Report://신고
                    alertShow(v);
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
        TileProvider coorTileProvider = new CoordTileProvider(getApplicationContext());
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(coorTileProvider));

        // 9호관 중심 화면 설정
        LatLng eng_9 = new LatLng(35.88688, 128.60850);                             // 9호관 좌표
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eng_9, 17));              // 배율

        // 구글맵 내에 마커 찍기
        //print_Marker(mMap);
    }

    // 백그라운드 서비스 실행
    public void activateService(View v){
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),android.R.mipmap.sym_def_app_icon));
        builder.setSmallIcon(android.R.mipmap.sym_def_app_icon);
        builder.setContentTitle("Safe Light Service");
        builder.setContentText("A safe light has scanned. Do you want to check it?");

        Intent intent = new Intent(getApplicationContext(),getClass());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(getClass());
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        builder.setAutoCancel(true);    // notification 클릭시 삭제.

        manager.notify(1, builder.build());
    }

    public void alertShow(View v){
        MyDialogFragment frag = MyDialogFragment.newInstance();
        frag.show(getFragmentManager(),"TAG");
    }


    public void add_Marker(GoogleMap map, double x, double y, double count){

        // circle settings
        int radiusM = (int)count;
        LatLng latLng = new LatLng(x,y);

        // draw circle
        int d = 500; // diameter
        Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint();
        p.setColor(Color.RED);
        c.drawCircle(d/2, d/2, d/2, p);

        // generate BitmapDescriptor from circle Bitmap
        BitmapDescriptor bmD = BitmapDescriptorFactory.fromBitmap(bm);

        // mapView is the GoogleMap
        map.addGroundOverlay(new GroundOverlayOptions().
                image(bmD).
                position(latLng,radiusM*2,radiusM*2).
                transparency(0.4f));
    }

    // 보안등 형태별 마커
    public void add_SecLight(GoogleMap map, double x, double y, int num){

        LatLng obj = new LatLng(x,y);
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.light_green);

        int height=75, width=75;

        // num 1 : 정상
        if(num==0){
            bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.light_green);
        }
        // num 2: 고장
        else if(num==1){
            bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.light_black);
        }
        // num 3: 수리중
        else if(num==2){
            bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.light_red);
        }

        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        map.addMarker(new MarkerOptions().position(obj)
                .title("No."+(num+1))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker arg0) {
                if(arg0.getTitle().substring(0,3)=="No.") // if marker source is clicked
                    Toast.makeText(MainActivity.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                return true;
            }

        });
    }

    public void print_Marker(GoogleMap map){

        // 안전
        for(int i=0;i<CoordInfo.safe.length;i++){
            add_SecLight(map, CoordInfo.safe[i][0],CoordInfo.safe[i][1],0);
        }
        // 수리중
        for(int i=0;i<CoordInfo.fix.length;i++){
            add_SecLight(map, CoordInfo.fix[i][0],CoordInfo.fix[i][1],1);
        }
        // 고장
        for(int i=0;i<CoordInfo.danger.length;i++){
            add_SecLight(map, CoordInfo.danger[i][0],CoordInfo.danger[i][1],2);
        }

        // 마커
        for(int i=0;i<CoordInfo.mark.length;i++){
            add_Marker(map, CoordInfo.mark[i][0],CoordInfo.mark[i][1],CoordInfo.mark[i][2]);
        }
    }


    // 구글맵 디자인 변경하는 클래스
    public static class CoordTileProvider implements TileProvider {

        private static final int TILE_SIZE_DP = 256;

        private final float mScaleFactor;
        private final Bitmap mBorderTile;

        public CoordTileProvider(Context context) {
        /* Scale factor based on density, with a 0.2 multiplier to increase tile generation
         * speed */
            mScaleFactor = context.getResources().getDisplayMetrics().density * 0.2f;
            Paint paint = new Paint();
            paint.setColor(Color.DKGRAY);           // 배경색: darkgray
            paint.setAlpha(0);                     // 투명도: 0~300
            mBorderTile = Bitmap.createBitmap((int) (TILE_SIZE_DP * mScaleFactor),
                    (int) (TILE_SIZE_DP * mScaleFactor), android.graphics.Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(mBorderTile);
            canvas.drawRect(0, 0, TILE_SIZE_DP * mScaleFactor, TILE_SIZE_DP * mScaleFactor,
                    paint);
        }

        @Override
        public Tile getTile(int x, int y, int zoom) {
            Bitmap coordTile = drawTileCoords(x, y, zoom);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            coordTile.compress(Bitmap.CompressFormat.PNG, 0, stream);
            byte[] bitmapData = stream.toByteArray();
            return new Tile((int) (TILE_SIZE_DP * mScaleFactor),
                    (int) (TILE_SIZE_DP * mScaleFactor), bitmapData);
        }

        private Bitmap drawTileCoords(int x, int y, int zoom) {
            Bitmap copy = null;
            synchronized (mBorderTile) {
                copy = mBorderTile.copy(android.graphics.Bitmap.Config.ARGB_8888, true);
            }
            return copy;
        }
    }

    //********************************//
    //       어두운길 신고            //
    //********************************//
    public static class MyDialogFragment extends DialogFragment{
        public static MyDialogFragment newInstance(){
            return new MyDialogFragment();
        }
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("REPORT");
            builder.setMessage("어두운 길로 신고하겠습니까?");
            builder.setPositiveButton("신고하기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //addCount(); 카운트값 갱신하는 함수
                    Log.i("MyTag","신고하기 클릭");
                }
            });
            builder.setNegativeButton("취소",null);
            return builder.create();
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

            switch(msg.what)
            {
                case MSG_RECEIVED_ACK:
                    if(!mStatePacket.check_ID(bAddress))          //보안등 ID가 리스트에 없다 => 보안등 ID요청 ACK
                    {
                        mStatePacket.set_ID(bAddress);
                        // 서버로 비컨의 상태를 전송한다.
                        sendSocketMsg('E', bAddress, bDate, bState);
                    }
                    else if(mStatePacket.check_ID(bAddress))  //상태 정보에대한 ACK
                    {
                        mStatePacket.remove_ID(bAddress);
                    }
                    break;
            }
        }
    };
    private void scanBLE() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 폰이 BLE 지원하지 않는 경우
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
            Permissioncheck();
        }
        scanLeDevice(true);
    }
    private void scanLeDevice(final boolean enable) {
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

        // 비컨 상태정보
        int tempState1 = mBleUtils.hex2dec(scanRecordAsHex.substring(58,60));
        String tempState2 = scanRecordAsHex.substring(60,62);
        bState = mBleUtils.parseState(mBleUtils.byte2bitset((byte)tempState1)) + tempState2;

        // 파싱한 상태정보 값 설정
        mStatePacket.set_State(bState);

        sendSocketMsg('R', bAddress, bDate, null);            // 메세지 전송

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


    //REPORT시 GPS 불러서 count 값 갱신
    public void addCount(){

        int num=0;
        double sum, temp;
        // GPS 정보 획득
        GpsInfo gps = new GpsInfo(MainActivity.this);
        // GPS 사용유무 체크
        if (gps.isGetLocation()) {
        }
        // 좌표 받아오기
        final double x = gps.getLatitude();
        final double y = gps.getLongitude();

        for(int i=0;i<CoordInfo.mark.length-1;i++){
            sum = Math.pow(Math.abs(x-CoordInfo.mark[i][0]),2)+Math.pow(Math.abs(y-CoordInfo.mark[i][1]),2);
            temp = Math.pow(Math.abs(x-CoordInfo.mark[i+1][0]),2)+Math.pow(Math.abs(y-CoordInfo.mark[i+1][1]),2);
            if(sum>temp){
                sum = temp;
                num = i;
            }
        }
        CoordInfo.setMarker(num);

        //http post요청 코드
        Thread thread = new Thread() {

            @Override

            public void run() {

                String urlString = "http://herick.iptime.org:8080/light_web/mobileDanger.do";

                InputStream myInputStream =null;
                StringBuilder sb = new StringBuilder();
                //adding some data to send along with the request to the server
                sb.append("x="+x+"&y="+y);
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

    //   public static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";

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
}

