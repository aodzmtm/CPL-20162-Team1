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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
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

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    // 클래스 인스턴스
    private BluetoothAdapter mBluetoothAdapter;
    private BleDeviceListAdapter mBLeDeviceListAdapter;
    private Send_StatePacket mStatePacket;
    private BleSocketPacket mBleSocketPacket;
    private ClientThread mClientThread;
    private BleUtils mBleUtils;

    // 컨스턴트
    private static final String URL = "ws://ip:8080/light_web/lampinfo.do";
    private static final int REQUEST_ENABLE_BT = 1; // 블루투스 ON 요청 횟수
    private static final long SCAN_PERIOD = 1000;       // 10초동안 SCAN 과정을 수행함
    final static int MSG_RECEIVED_ACK = 0x100;
    private static final boolean USING_WINI = true; // 비콘 종류에 따라 true or false
    private static final String TAG = "SCAN";
    private static final boolean IS_DEBUG = true;

    String bAddress;
    String bDate;
    String bState;

    boolean mScanning;
    public static String BEACON_UUID;
    public static  Boolean saveRSSI;
    SharedPreferences setting;

    ImageButton mBtnScan;
    ImageButton mBtnRoute;
    ImageButton mBtnRefresh;
    ImageButton mBtnReport;
    ImageButton mBtnInfo;
    private CustomDialogFragment mCustomDialog;

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
        // mClientThread.start();//서버로부터 메시지를 계속 받도록한다.
        mBleSocketPacket = new BleSocketPacket();
        mStatePacket = new Send_StatePacket();


        mBtnScan = (ImageButton) findViewById(R.id.btn_Scan);
        mBtnScan.setOnClickListener(mClickListener);

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
                case R.id.btn_Route:
                    activateService(v);
                    break;
                case R.id.btn_Refresh:
                    break;
                case R.id.btn_Report:
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
        print_Marker(mMap);
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
                    Log.d("Cleint", "msg: " + handlerText);
                    break;
            }
        }
    };
    private void scanBLE() {
        // 폰이 BLE 지원하지 않는 경우
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
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
                    // ble 디바이스 정보 추출 함수 콜
                    getBleDeviceInfoFromLeScan(device, rssi, scanRecord);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBLeDeviceListAdapter.addDevice(device);
                            mBLeDeviceListAdapter.notifyDataSetChanged();
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
        Log.i("Recode",scanRecordAsHex);
        proximityUUID = String.format("%s-%s-%s-%s-%s",
                scanRecordAsHex.substring(18, 26),
                scanRecordAsHex.substring(26, 30),
                scanRecordAsHex.substring(30, 34),
                scanRecordAsHex.substring(34, 38),
                scanRecordAsHex.substring(38, 50));


        // 날짜 파싱
        byte[] tempDate = {scanRecord[25], scanRecord[26], scanRecord[27]};
        bDate = String.valueOf(mBleUtils.ByteArrtoCharArr(tempDate));

        byte[] tempState = {scanRecord[28], scanRecord[29]};
        bState = String.valueOf(mBleUtils.ByteArrtoCharArr(tempState));
        mStatePacket.set_State(bState);

        //       txPower = scanRecord[29];

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
        Log.i("Check", devNum + ";" + Date);
        String sendMsg = mBleSocketPacket.makeRequestMsg(cmd, devNum, Date, State);
        if (sendMsg != null) {
            mClientThread.mSocket.send(sendMsg);

            Log.d("Client", "sendmsg: " + sendMsg);
        }
    }


    //REPORT시 GPS 불러서 count 값 갱신
    private void addCount(){

        int num=0;
        double sum, temp;
        // GPS 정보 획득
        GpsInfo gps = new GpsInfo(MainActivity.this);
        // GPS 사용유무 체크
        if (gps.isGetLocation()) {
        }
        // 좌표 받아오기
        double x = gps.getLatitude();
        double y = gps.getLongitude();

        for(int i=0;i<CoordInfo.mark.length-1;i++){
            sum = Math.pow(Math.abs(x-CoordInfo.mark[i][0]),2)+Math.pow(Math.abs(y-CoordInfo.mark[i][1]),2);
            temp = Math.pow(Math.abs(x-CoordInfo.mark[i+1][0]),2)+Math.pow(Math.abs(y-CoordInfo.mark[i+1][1]),2);
            if(sum>temp){
                sum = temp;
                num = i;
            }
        }
        CoordInfo.setMarker(num);
    }

    //********************************//
    //          Info Dial             //
//    //********************************//
//    public class InfoDial extends DialogFragment
//    {
//        Context mContext = getApplicationContext();
//        Dialog dialog = new Dialog(mContext);
//        dialog.setContentView(R.layout.custom_dialog);
//        dialog.setTitle("커스텀 다이얼로그");
//        TextView text = (TextView) dialog.findViewById(R.id.text);
//        text.setText("Hello, this is a custom dialog!");
//        ImageView image = (ImageView) dialog.findViewById(R.id.image);
//        image.setImageResource(R.drawable.icon)
//        Dialog dialog = new Dialog(MainActivity.this);
//        dialog.setContentView(R.layout);
//        dialog.setTitle("Custom Dialog");
//
//        TextView tv = (TextView) dialog.findViewById(R.id.text);
//        tv.setText("Hello. This is a Custom Dialog !");
//
//        ImageView iv = (ImageView) dialog.findViewById(R.id.image);
//        iv.setImageResource(R.drawable.suji);
//
//        dialog.show();
}

