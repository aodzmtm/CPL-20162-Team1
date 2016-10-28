package com.example.safelight;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class MapActivity extends ActionBarActivity {
	private String pathServerIp = "155.230.90.196";
	private int pathServerPort = 9998;
	private String mapServerIp = "155.230.90.196";
	private int mapServerPort = 9988;

	private RelativeLayout background;
	private String pathBuffer;
	private String[] path;
	private String start;
	private boolean isStart;
	private String end;

	private ArrayList<Node> node;
	private int level;
	private ImageView mapView;
	private Bitmap[] map;
	private Bitmap[] undergroundMap;
	private int maxLevel;
	private int minLevel;
	private TextView levelView;
	private Button upButton;
	private Button downButton;
	private ListView levelList;

	private PathView pathView;
	private ImageView arrowView;
	private TextView curPosition, destination;

	private MapServerConnectThread mapThread;
	private PathServerConnectThread pathThread;
	private boolean threadFinished;
	private Handler socketHandler;
	private ProgressBar loadingProgress;
	private TextView loadingText;

	private SensorManager sensorManager;
	private Sensor accelSensor, magneticSensor;
	private SensorEventListener sensorListener;
	private long moveToNextTime;
	private boolean isTurning;

	private float[] accelData = null, magneticData = null;
	private float[] rotation = new float[9];
	private float[] result = new float[3];
	private float baseAngle;
	private float standardAngle;
	private boolean standardSetted;

	private boolean loading;
	private Button moveButton;
	private boolean isPacketTransmitted;

	// BLE Variable ///////////////////////////////////////////////////////////////////////////////
	/*
        Constant Variables
     */

	public static String BEACON_UUID;       // changsu
	public static Boolean saveRSSI;
	private static final long SCAN_PERIOD = 1000;       // 10�ʵ��� SCAN ������ ������
	private static final int REQUEST_ENABLE_BT = 1;
	private static final String TAG = "SCAN";
	private static final boolean IS_DEBUG = true;
	private static final long TIMEOUT_PERIOD = 1000;
	private static final boolean USING_WINI = true; // TI CC2541 ���: true



	/*
        Class Instance Variables
     */
	private BleDeviceListAdapter mBleDeviceListAdapter;
	private BleUtils mBleUtils;
	/*
        Member Variables
     */
	private BluetoothAdapter mBluetoothAdapter;
	private ArrayList<BleDeviceInfo> mArrayListBleDevice;   // scan �� �˻��� pebBle ��� �����ϴ� array list
	private HashMap<String, BleDeviceInfo> mItemMap;
	boolean mScanning;

	/*
        Widgets
     */
	ListView mBleListView;
	SharedPreferences setting;
	private TextView closedBeaconView;
 	///////////////////////////////////////////////////////////////////////////////////////////////


    private String foundMaxStrengthBluetoothBeacon = "pebBLE_522_100";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		isStart = false;
		background = (RelativeLayout) findViewById(R.id.backgroundLayout);
		node = new ArrayList<Node>();

		pathBuffer = null;
		path = new String[0];

		mapView = (ImageView) findViewById(R.id.mapView);
		arrowView = (ImageView) findViewById(R.id.arrowView);

		levelView = (TextView) findViewById(R.id.levelView);

		upButton = (Button) findViewById(R.id.upButton);
		downButton = (Button) findViewById(R.id.downButton);
		levelList = (ListView) findViewById(R.id.levelList);

		loadingProgress = (ProgressBar) findViewById(R.id.loadingBar);
		loadingText = (TextView) findViewById(R.id.loadingText);

		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		standardSetted = false;

		isTurning = false;

		// BLE Variable ///////////////////////////////////////////////////////////////////////////
		closedBeaconView = (TextView) findViewById(R.id.closedBeacon);
		mBleUtils = new BleUtils();
		mArrayListBleDevice = new ArrayList<BleDeviceInfo>();
		mItemMap = new HashMap<String, BleDeviceInfo>();

		initBluetoothAdapter();     // Bluetooth Adapter ����

		setting = PreferenceManager.getDefaultSharedPreferences(this);

		BEACON_UUID = getBeaconUuid(setting);


		// activity_ble_device_scan.xml ������ listview
		mBleDeviceListAdapter = new BleDeviceListAdapter(this, R.layout.ble_device_row,
				mArrayListBleDevice, mItemMap);


		//////////////////////////////////////////////////////////////////////////////////////////
		sensorListener = new SensorEventListener() {
			@Override
			public void onSensorChanged(SensorEvent event) {

				if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
					accelData = event.values.clone();
				} else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
					magneticData = event.values.clone();
				}
				if(accelData != null && magneticData != null && !loading) {
					SensorManager.getRotationMatrix(rotation, null, accelData, magneticData);
					SensorManager.getOrientation(rotation, result);

					float direction;
					direction = (float) Math.toDegrees(-result[0]);

					direction -= baseAngle+90;

					while(direction < 0) direction += 360;
					if(!standardSetted) {
						standardSetted = true;
						standardAngle = direction;
					}

					int seed = 0;
					if(standardAngle < 10) {
						seed = 180;
					} else if(standardAngle > 350) {
						seed = -180;
					}
					float newStandardAngle = standardAngle + seed, newDirection = direction + seed;
					if(newDirection > 360) newDirection -= 360;
					if(newDirection < 0) newDirection += 360;
					if(newDirection < newStandardAngle - 10 || newDirection > newStandardAngle + 10) {
						standardAngle = direction;
					}

					mapView.setRotation(standardAngle);
					pathView.setRotation(standardAngle);

					if(path.length > 1) {
						float x = (getNode(path[1]).getX() - getNode(path[0]).getX());
						x = x * mapView.getWidth() / mapView.getHeight();
						float y = (getNode(path[0]).getY() - getNode(path[1]).getY());
						float arrowAngle = (float) Math.toDegrees(Math.acos(y / Math.sqrt(x * x + y * y)));
						if(x < 0) arrowAngle *= -1;
						arrowView.setRotation(arrowAngle+standardAngle);
					}

					mapView.scrollTo((int) encodeCoord('x', getNode(path[0]).getX()-50), (int) encodeCoord('y', getNode(path[0]).getY()-50));
					pathView.scrollTo((int) encodeCoord('x', getNode(path[0]).getX()-50), (int) encodeCoord('y', getNode(path[0]).getY()-50));
				}
			}

			@Override
			public void onAccuracyChanged(Sensor arg0, int arg1) {
				// TODO Auto-generated method stub

			}


		};


		moveButton = (Button) findViewById(R.id.moveButton);
		moveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(path.length > 2) {
					pathThread = new PathServerConnectThread();
					isPacketTransmitted = false;
					pathThread.execute(path[2]);

					while(!isPacketTransmitted);

					int newLength = path.length - 2;
					for(int i = 0; i < newLength; i++) {
						path[i] = path[i+2];
					}
					setLevel(getNode(path[0]).getLevel());
				}
			}

		});

		threadFinished = false;
		socketHandler = new SocketHandler(this);
		mapThread = new MapServerConnectThread();
		mapThread.execute();

		pathView = new PathView(this);
		background.addView(pathView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		arrowView.bringToFront();

		loading = true;



	}

	class BeaconIDThread extends Thread {
		public void run() {
			try {
				int i = -1;
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

				while(!threadFinished) {
					// Temporarily Code
					if(isStart) {
						sleep(5000);
					}
					isStart = true;

				/* String[] tempBeaconID = new String[3];
				tempBeaconID[0] = "BEACON_0003_0_003_00";
				tempBeaconID[1] = "BEACON_0003_0_003_01";
				tempBeaconID[2] = "BEACON_0003_0_003_03";
				i = (i + 1) % 3; */

				// Receive Beacon ID
				//String beaconId = tempBeaconID[i];

                // YOGI2
                String beaconId = foundMaxStrengthBluetoothBeacon;

				pathThread = new PathServerConnectThread();
				isPacketTransmitted = false;
				pathThread.execute(beaconId);

				while(!isPacketTransmitted);

				moveToNext(beaconId);
			}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void moveToNext(String BeaconID) {
		int newStart;
		for(newStart = 0; newStart < path.length; newStart++) {
			if(path[newStart].equals(BeaconID)) {
				break;
			}
		}
		int newLength = path.length - newStart;
		if(newLength > 0) {
			String[] temp = new String[newLength];
			for(int i = 0; i < newLength; i++) {
				temp[i] = path[i+newStart];
			}
			path = temp;

			Message msg = socketHandler.obtainMessage();
			msg.what = Constant.SOCKET_FINISH;
			socketHandler.sendMessage(msg);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		sensorManager.registerListener(sensorListener, accelSensor, SensorManager.SENSOR_DELAY_FASTEST);
		sensorManager.registerListener(sensorListener, magneticSensor, SensorManager.SENSOR_DELAY_FASTEST);



	}

	@Override
	public void onPause() {
		super.onPause();
		sensorManager.unregisterListener(sensorListener);
	}

	@Override
	public void onStop() {
		super.onStop();
		threadFinished = true;
	}


	class PathView extends View {
		Paint paint;
		Path paintPath;

		public PathView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			paint = new Paint();
			paintPath = new Path();
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			// TODO Auto-generated method stub
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			this.setMeasuredDimension(mapView.getWidth(), mapView.getHeight());
		}

		@Override
		protected void onDraw(Canvas canvas) {
			paint.setColor(Color.RED);
			paint.setStrokeWidth(10);
			paint.setTextAlign(Paint.Align.CENTER);
			paint.setTextSize(20);

			if(pathBuffer == null)
				return;

			end = path[path.length-1];
			String prev = path[0];

			for(int i = 1; i < path.length; i++) {
				if(getNode(prev).getLevel() == level && getNode(path[i]).getLevel() == level) {
					canvas.drawLine(encodeCoord('x', getNode(prev).getX()), encodeCoord('y', getNode(prev).getY()),
							encodeCoord('x', getNode(path[i]).getX()), encodeCoord('y', getNode(path[i]).getY()), paint);
				}

				if(getNode(prev).getLevel() == level && getNode(path[i]).getLevel() > getNode(prev).getLevel()) {
					canvas.drawRect(encodeCoord('x', getNode(prev).getX())-30, encodeCoord('y', getNode(prev).getY())-30, encodeCoord('x', getNode(prev).getX())+30, encodeCoord('y', getNode(prev).getY())+30, paint);
				} else if (getNode(prev).getLevel() == level && getNode(path[i]).getLevel() < getNode(prev).getLevel()) {
					canvas.drawRect(encodeCoord('x', getNode(prev).getX())-30, encodeCoord('y', getNode(prev).getY())-30, encodeCoord('x', getNode(prev).getX())+30, encodeCoord('y', getNode(prev).getY())+30, paint);
					paint.setColor(Color.WHITE);
					canvas.drawRect(encodeCoord('x', getNode(prev).getX())-20, encodeCoord('y', getNode(prev).getY())-20, encodeCoord('x', getNode(prev).getX())+20, encodeCoord('y', getNode(prev).getY())+20, paint);
					paint.setColor(Color.RED);
				}
				prev = path[i];
			}

			if(getNode(path[0]).getLevel() == level) {
				if(getNode(path[0]) != getNode(end))
					canvas.drawCircle(encodeCoord('x', getNode(path[0]).getX()), encodeCoord('y', getNode(path[0]).getY()), 30, paint);
			}
			if(getNode(end).getLevel() == level) {
				paint.setStrokeWidth(20);
				canvas.drawLine(encodeCoord('x', getNode(end).getX())-30, encodeCoord('y', getNode(prev).getY())-30,
						encodeCoord('x', getNode(end).getX())+30, encodeCoord('y', getNode(prev).getY())+30, paint);
				canvas.drawLine(encodeCoord('x', getNode(end).getX())+30, encodeCoord('y', getNode(prev).getY())-30,
						encodeCoord('x', getNode(end).getX())-30, encodeCoord('y', getNode(prev).getY())+30, paint);
				paint.setStrokeWidth(5);
			}
			super.onDraw(canvas);
		}
	}

	public float cos(float angle) {
		return (float) Math.cos(Math.toRadians(angle));
	}

	public float sin(float angle) {
		return (float) Math.sin(Math.toRadians(angle));
	}

	public float encodeCoord(char direction, float Coord) {
		switch(direction) {
		case 'x':
			return mapView.getWidth() * Coord / 100;
		case 'y':
			return mapView.getHeight() * Coord / 100;
		default:
			return 0;
		}
	}

	public float decodeCoord(char direction, float Coord) {
		switch(direction) {
		case 'x':
			return Coord * 100 / mapView.getWidth();
		case 'y':
			return Coord * 100 / mapView.getHeight();
		default:
			return 0;
		}
	}

	public void setLevel(int level) {
		this.level = level;
		if(level > 0) {
			levelView.setText(""+level);
			mapView.setImageBitmap(map[level-1]);
		} else if(level < 0) {
			levelView.setText("B"+(-level));
			mapView.setImageBitmap(undergroundMap[-level-1]);
		}
		pathView.invalidate();
	}

	public Node getNode(String name) {
		for(Node i : node) {
			if(i.getName().equals(name)) {
				return i;
			}
		}
		return null;
	}

	class PathServerConnectThread extends AsyncTask<String, Void, Void> {
		boolean retry;

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			String beaconID = params[0];
			String command = "newPath";

			for(String s : path) {
				if(beaconID.equals(s)) {
					Log.i("onPath", "onPath");
					command = "onPath " + path[0];
					break;
				}
			}

			String sum = beaconID + " " + command;
			isPacketTransmitted = true;

			retry = true;
			while(retry) {
				Socket socket = null;
				try {
					SocketAddress socketAddress = new InetSocketAddress(pathServerIp, pathServerPort);
					socket = new Socket();
					socket.setSoTimeout(5000);
					socket.connect(socketAddress, 5000);

					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					byte[] out = new byte[100];
					out = sum.getBytes();
					dos.write(out);
					dos.flush();

					if(command == "newPath") {
						DataInputStream dis = new DataInputStream(socket.getInputStream());
						byte[] buffer = new byte[2048];
						dis.read(buffer);
						pathBuffer = new String(buffer, 0, buffer.length);
						pathBuffer = pathBuffer.trim();
						path = pathBuffer.split("-");
						end = path[path.length-1];
						Log.i("path", pathBuffer);

						Message msg = socketHandler.obtainMessage();
						msg.what = Constant.SOCKET_FINISH;
						socketHandler.sendMessage(msg);

						msg = socketHandler.obtainMessage();
						msg.what = Constant.PROGRESS;
						socketHandler.sendMessage(msg);

						if(path.length <= 2) break;
						sum = path[2];
					}
					retry = false;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					retry = false;
				} finally {
					try {
						socket.close();
					} catch (Exception e) {
						Log.i("Exception", "Path Server Connect");
					}
				}
			}
			return null;
		}

	}

	class MapServerConnectThread extends AsyncTask<String, Void, Void> {
		boolean retry;

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			retry = true;
			Socket socket = null;
			while(retry) {
				try {
					SocketAddress socketAddress = new InetSocketAddress(mapServerIp, mapServerPort);
					socket = new Socket();
					socket.setSoTimeout(5000);
					socket.connect(socketAddress, 5000);

					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					DataInputStream dis = new DataInputStream(socket.getInputStream());
					byte[] sendBuffer = new byte[100];
					String sendString;
					byte[] recvBuffer = new byte[20480];
					String recvString;
					String[] recvStrings;

					sendString = "NodeCount";
					sendBuffer = sendString.getBytes();
					dos.write(sendBuffer);
					dos.flush();

					int recvLen = dis.read(recvBuffer);
					recvString = new String(recvBuffer, 0, recvLen);
					recvStrings = recvString.split(" ");
					maxLevel = Integer.parseInt(recvStrings[0]);
					minLevel = Integer.parseInt(recvStrings[1]);

					Message msg = socketHandler.obtainMessage();
					msg.what = Constant.SET_LEVEL;
					msg.arg1 = maxLevel-minLevel;
					socketHandler.sendMessage(msg);

					map = new Bitmap[maxLevel];
					undergroundMap = new Bitmap[-minLevel];
					for(int i = minLevel; i <= maxLevel; i++) {
						if(i != 0) {
							sendString = "MapImage "+i;
							sendBuffer = sendString.getBytes();
							dos.write(sendBuffer);
							dos.flush();

							int imageSize = 0;
							ByteBuffer bb = null;
							int level = 0;
							recvLen = 0;
							int buffLen = 0;
							while((recvLen = dis.read(recvBuffer, 0, 20480)) > 0) {
								if(threadFinished) {
									break;
								}
								if(bb == null) {
									int headerLen = 0;
									recvString = new String(recvBuffer, 0, recvLen);
									recvStrings = recvString.split(" ");
									level = Integer.parseInt(recvStrings[0]);
									imageSize = Integer.parseInt(recvStrings[1]);
									baseAngle = Float.parseFloat(recvStrings[2]);
									headerLen = recvStrings[0].length() + recvStrings[1].length() + recvStrings[2].length() + 3;

									bb = ByteBuffer.allocate(imageSize);
									bb.put(recvBuffer, headerLen, recvLen - headerLen);
								} else {
									bb.put(recvBuffer, 0, recvLen);
								}
								buffLen += recvLen;
								if(buffLen > imageSize) break;
							}
							if(threadFinished) {
								break;
							}

							if(level > 0) {
								map[level-1] = BitmapFactory.decodeByteArray(bb.array(), 0, imageSize);
								if(map[level-1] == null) {
									Log.i("error", String.valueOf(buffLen));
									return null;

								}
							} else if(level < 0) {
								undergroundMap[-level-1] = BitmapFactory.decodeByteArray(bb.array(), 0, imageSize);
								if(undergroundMap[-level-1] == null) {
									Log.i("error", String.valueOf(buffLen));
								}
							}

							msg = socketHandler.obtainMessage();
							msg.what = Constant.PROGRESS;
							socketHandler.sendMessage(msg);
						}
					}

					sendString = "Node";
					sendBuffer = sendString.getBytes();
					dos.write(sendBuffer);
					dos.flush();

					for(int i = 0; i < 2; i++) {
						recvString = dis.readLine();
						int count = Integer.parseInt(dis.readLine());
						for(int j = 0; j < count; j++) {
							if(threadFinished) {
								break;
							}

							dis.readLine();
							String name = dis.readLine().split("=")[1];
							int level = Integer.parseInt(dis.readLine().split("=")[1]);
							recvString = dis.readLine();
							float X = Float.parseFloat(recvString.split("=")[1].split(",")[0]);
							float Y = Float.parseFloat(recvString.split("=")[1].split(",")[1]);
							Log.i("where", name + " " + level + " " + X + " " + Y + " ");
							node.add(new Node(name, level, X, Y));
						}
					}

					if(!threadFinished) {
						msg = socketHandler.obtainMessage();
						msg.what = Constant.PROGRESS;
						socketHandler.sendMessage(msg);

						Thread beaconIDThread = new BeaconIDThread();
						beaconIDThread.start();
					}
					retry = false;
				} catch (Exception e1) {
					e1.printStackTrace();
					retry = false;
					AlertDialog.Builder alt_bld = new AlertDialog.Builder(MapActivity.this);
				} finally {
					try {
						socket.close();
					} catch (Exception e) {
						Log.i("Exception", "Path Server Connect");
					}
				}
			}
			return null;
		}
	}

	private static class SocketHandler extends Handler {
		private final WeakReference<MapActivity> mActivity;
		public SocketHandler(MapActivity activity) {
			mActivity = new WeakReference<MapActivity>(activity);
		}
		public void handleMessage(Message msg) {
			MapActivity activity = mActivity.get();
			if (activity != null) {
				activity.handleMessage(msg);
			}
		}
	}

	private void handleMessage(Message msg) {
		switch(msg.what) {
		case Constant.SOCKET_FINISH:
			Log.i("start", path[0]);
            if (path[0] != null) {
                if (getNode(path[0]) != null)
                    setLevel(getNode(path[0]).getLevel());
                else
                    Log.e("start", "Error! getNode(path[0]) == null");
            }
            else
                Log.e("start", "Error! path[0] == null");

			if(loading) {
				arrowView.setImageResource(getArrowToNextRoute());
				upButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(level < maxLevel) {
							level++;
							if(level == 0)
								if(maxLevel == 0)
									level--;
								else
									level++;
							setLevel(level);
						}
					}
				});
				downButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(level > minLevel) {
							level--;
							if(level == 0)
								if(minLevel == 0)
									level++;
								else
									level--;
							setLevel(level);
						}
					}
				});
				ArrayList<String> list = new ArrayList<String>();
				for(int i = maxLevel; i >= minLevel; i--) {
					if(i < 0) {
						list.add("B"+(-i));
					} else if(i > 0) {
						list.add(""+i);
					}
				}
				ArrayAdapter<String> adapter = new LevelAdapter(this, list);
				levelList.setAdapter(adapter);
				levelList.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						try {
							setLevel(Integer.parseInt((String) parent.getItemAtPosition(position)));
						} catch(NumberFormatException e) {
							String basementLevel = (String) parent.getItemAtPosition(position);
							setLevel(-Integer.parseInt(basementLevel.substring(1)));
						}
					}
				});
				loading = false;
			}
			break;
		case Constant.SET_LEVEL:
			loadingProgress.setMax(msg.arg1+2);
			break;
		case Constant.PROGRESS:
			loadingProgress.setProgress(loadingProgress.getProgress()+1);
			if(loadingProgress.getProgress() == loadingProgress.getMax()) {
				loadingProgress.setVisibility(View.INVISIBLE);
				loadingText.setVisibility(View.INVISIBLE);
			}
			break;
		case Constant.ALERT_DIALOG:
			AlertDialog alert = ((AlertDialog.Builder) msg.obj).create();
			alert.setTitle("Cannot Connect to Server!");
			alert.setIcon(R.drawable.cannot_connect);
			alert.show();
		}
	}

	private int getArrowToNextRoute() {
		if(path.length <= 2) return R.drawable.arrow_straight;

		float x0 = getNode(path[1]).getX()-getNode(path[0]).getX();
		float y0 = getNode(path[1]).getY()-getNode(path[0]).getY();
		float x1 = getNode(path[2]).getX()-getNode(path[1]).getX();
		float y1 = getNode(path[2]).getY()-getNode(path[1]).getY();

		double angle = Math.toDegrees(Math.acos((x0 * x1 + y0 * y1) / (Math.sqrt(x0 * x0 + y0 * y0) * Math.sqrt(x1 * x1 + y1 * y1))));

		if(angle < 45) return R.drawable.arrow_straight;
		if(angle > 135) return R.drawable.arrow_turn;

		if(x0 * y1 - y0 * x1 > 0) return R.drawable.arrow_right;
		else return R.drawable.arrow_left;
	}






	// BLE FUNC ///////////////////////////////////////////////////////////////////////////////////


	@Override
	protected void onDestroy() {
		super.onDestroy();


		if(!mArrayListBleDevice.isEmpty())
			mArrayListBleDevice.clear();
		// handler ��  ����� �ݹ� �Լ� ����
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
		mHandler.removeMessages(0);
		mTimeOut.removeMessages(0);
		mTimeOut.removeMessages(0);
	}

	/*****************************************************************************************
	 *  Function: getBeaconUuid
	 *
	 *  Description
	 *      - ��� beacon�� UUID ���� ������
	 *
	 *****************************************************************************************/
	public String getBeaconUuid(SharedPreferences pref)
	{
		String uuid = "";

        uuid = pref.getString("keyUUID", BluetoothUuid.WIZTURN_PROXIMITY_UUID.toString());

		/*
		if(USING_WINI) {
			uuid = BluetoothUuid.WINI_UUID.toString();
		}
		else {
			uuid = pref.getString("keyUUID", BluetoothUuid.WIZTURN_PROXIMITY_UUID.toString());
		}
        */

		return uuid;
	}

	/*****************************************************************************************
	 *  Function: initBluetoothAdapter
	 *
	 *  Description
	 *      - Bluetoth Adapter ����
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
	 *      - SCAN_PERIOD �������� startLeScan()�� ȣ���Ͽ� beacon�� �˻���
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
			mBluetoothAdapter.startLeScan(mLeScanCallback);
			mHandler.sendEmptyMessageDelayed(0, SCAN_PERIOD);
		}
	};

	/*****************************************************************************************
	 *  Bluetooth LE Device Timeout Handler
	 *
	 *  Description
	 *      - TIMEOUT_PERIOD(1000) �������� ��� ����Ʈ�� ��ϵ��� timeout�� ����
	 *      - timeout�� 0�� �Ǹ� ��Ͽ��� ���� ��Ų��.
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

			if(maxIndex == -1) {
				closedBeaconView.setText("No Dev");
			}
			else{
				closedBeaconView.setText(maxIndex+1 +"th    "
						+ "major: " + mArrayListBleDevice.get(maxIndex).major + "  "
						+ "minor: " + mArrayListBleDevice.get(maxIndex).minor + "  "
						+ mArrayListBleDevice.get(maxIndex).getRssi() +"dbm");

                //  YOGI
                foundMaxStrengthBluetoothBeacon = mArrayListBleDevice.get(maxIndex).devName + "_" + mArrayListBleDevice.get(maxIndex).major + "_" + mArrayListBleDevice.get(maxIndex).minor;
			}
			mTimeOut.sendEmptyMessageDelayed(0, TIMEOUT_PERIOD);
		}
	};



	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
			mScanning = true;
			getBleDeviceInfoFromLeScan(device, rssi, scanRecord);
                    /*
                        Exception ������ ���� runOnUiThread()���� notifyDataSetChanged()�� ȣ����
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
	 *      - scanRecord[] �����͸� �Ľ��Ͽ�
	 *        proximityUUID, Major, Minor, Measured Power, distance ���� ����
	 *
	 *****************************************************************************************/
	private void getBleDeviceInfoFromLeScan(BluetoothDevice device, int rssi, byte[] scanRecord)
	{
		String devName;
		String devAddress;
		String scanRecordAsHex;     // 24byte
		String proximityUUID;       // 12 + 5 characters
		int major, minor;
		//int measuredPower;
		int txPower;                // changsu: ȥ���� ���ֱ� ���� measuredPower�� txPower�� ������
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

		//�� �񱳺κ��� ���� �ø���.. ��ġ ���������� ���� �����ϳ�....
		scanRecordAsHex = mBleUtils.ByteArrayToString(scanRecord);

		//24byte
		proximityUUID = String.format("%s-%s-%s-%s-%s",
				scanRecordAsHex.substring(18, 26),
				scanRecordAsHex.substring(26, 30),
				scanRecordAsHex.substring(30, 34),
				scanRecordAsHex.substring(34, 38),
				scanRecordAsHex.substring(38, 50));


		major = mBleUtils.byteToInt(scanRecord[25], scanRecord[26]);
		minor = mBleUtils.byteToInt(scanRecord[27], scanRecord[28]);

		txPower = scanRecord[29];
		Log.d("TEST", "UUID: "+ proximityUUID + "    txPower: " + scanRecord[29]);

		if(proximityUUID.equals(BEACON_UUID) || proximityUUID.equals(BluetoothUuid.WIZTURN_PROXIMITY_UUID.toString()) || proximityUUID.equals(BluetoothUuid.WINI_UUID.toString()))
		{
			try {
				Log.d(TAG, "Found Pebble UUID: " + proximityUUID);

				double distance = mBleUtils.getDistance(rssiValue, txPower);
				double distance2 = mBleUtils.getDistance_20150515(rssiValue, txPower);

				Log.d(TAG, "dev name: " + devName +
						", addr: " + devAddress +
						", major: " + major +
						", minor: " + minor +
						", rssi: " + rssi +
						", txPower: " + txPower +
						", distance: " + distance +
						", distance2: " + distance2);

				BleDeviceInfo item = new BleDeviceInfo(proximityUUID, devName, devAddress, major, minor,
						txPower, rssiValue, distance, distance2);


				updateBleDeviceList(item);


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
	 *      - GUI Custom List View�� beacon ������ ���Ӱ� �߰��ϰų� ������Ʈ ��
	 *
	 *****************************************************************************************/
	public void updateBleDeviceList(BleDeviceInfo item)
	{
		int KalmanRSSI =0;
		/**
		 * HashMap�� key���� ������ device address�� �ִ� ���: update ����
		 */
		if(mItemMap.containsKey(item.devAddress))
		{

			mItemMap.get(item.devAddress).rssi = (int)mItemMap.get(item.devAddress).rssiKalmanFileter.update(item.rssi);
			KalmanRSSI = mItemMap.get(item.devAddress).rssi;
			mItemMap.get(item.devAddress).distance =  mBleUtils.getDistance(KalmanRSSI, item.txPower);
			mItemMap.get(item.devAddress).distance2 =  mBleUtils.getDistance_20150515(KalmanRSSI, item.txPower);

			mItemMap.get(item.devAddress).timeout = item.timeout;

			Log.d("Debug", "Major: " + item.major +
					", Minor: " + item.minor +
					", rssi: " + KalmanRSSI +
					", distance: " + item.distance);
		}
		else
		{
			/**
			 *  HashMap�� �ش� item�� device address�� ���� ���, �߰���
			 *  key��: devAddress
			 */
			mArrayListBleDevice.add(item);
			mItemMap.put(item.devAddress, item);
		}



	}

}


