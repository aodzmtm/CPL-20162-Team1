package com.example.safelight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;

import java.io.ByteArrayOutputStream;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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
                    startActivity(new Intent(MainActivity.this, BleDeviceScanActivity.class));
                    finish();
                    break;
                case R.id.btn_Route:
                    activateService(v);
                    finish();
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


//        //json 활용한 맵 디자인 - 한국 세팅값과 해외 세팅값 변수 이름이 달라서 적용 불가
//        try {
//            // Customise the styling of the base map using a JSON object defined
//            // in a raw resource file.
//            boolean success = mMap.setMapStyle(
//                    MapStyleOptions.loadRawResourceStyle(
//                            this, R.raw.style_json));
//
//            if (!success) {
//                Log.e("MapsActivityRaw", "Style parsing failed.");
//            }
//        } catch (Resources.NotFoundException e) {
//            Log.e("MapsActivityRaw", "Can't find style.", e);
//        }

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


    // 보안등 형태별 마커
    public void add_Marker(GoogleMap map, double x, double y, int num){

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
    }

    public void print_Marker(GoogleMap map){
        double[] x_safe = {35.886919, 35.887305, 35.886523, 35.886193, 35.886753, 35.887103};
        double[] y_safe = {128.609060, 128.609084, 128.609036, 128.609028, 128.608440, 128.608451};
        double[] x_danger = {35.887114};
        double[] y_danger = {128.607893};
        double[] x_fix = {35.886756};
        double[] y_fix = {128.607901};

        // 안전
        for(int i=0;i<6;i++){
            add_Marker(map, x_safe[i],y_safe[i],0);
        }
        // 수리중
        for(int i=0;i<1;i++){
            add_Marker(map, x_fix[i],y_fix[i],1);
        }
        // 고장
        for(int i=0;i<1;i++){
            add_Marker(map, x_danger[i],y_danger[i],2);
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
            paint.setAlpha(200);                     // 투명도: 0~300
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

