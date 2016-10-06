package com.example.safelight;

import android.util.Log;

/***
 *  소켓 통신을 위한 패킷 정의 (Android <-> PC Server)
 *   - Command ID, Device Address, Major, Minor 값 전송
 *
 *
 */

public class BleSocketPacket {
    private final static int STX = 0x02;
    private final static int ETX = 0x03;
    private final static int COMMAND_NONE = 0x0;
    private final static char COMMAND_REQUEST_ID = 'R';     // Request (Android -> Server)   : ID
    private final static char COMMAND_REQUEST_ST = 'E';     // Request (Android -> Server)   : STATE
    private final static char COMMAND_RESPONSE =   'E';     // Response(Server -> Android)
    private static final String CDMA = "9900000000XX00000001011231+000-000";

    private final static String HEADER_NAME = "bleproj";

    String mHeaderName = HEADER_NAME;
    int mPacketLength;
    int mCommandId;
    String mDeviceAddr;

    public BleSocketPacket()
    {
        mHeaderName = HEADER_NAME;
        mPacketLength = 0;
        mCommandId = COMMAND_NONE;
        mDeviceAddr = "";
    }

    /*
        Request Message 생성
        +------------+-------------+-------------+
        +     Date   | Command ID  | Dev Address |
        +------------+-------------+-------------+
        OR
        +------------+-------------+-------+-------+
        + Command ID | Beacon ID   | Major | Minor |
        +------------+-------------+-------+-------+
     */

    public String makeRequestMsg(int cmd, String devNum, String Date, String State)
    {
        String msg=null;

        //서버에 보안등 ID요청
        if(cmd == COMMAND_REQUEST_ID)
        {
            msg = STX + Date + COMMAND_REQUEST_ID  + devNum + ETX;     // devNum : MAC

            Log.i("MSG",msg);
        }
        //서버에 BEACON 상태 전송
        else if(cmd == COMMAND_REQUEST_ST)
        {
            msg = STX + Date + COMMAND_REQUEST_ST + devNum + CDMA  + State + ETX;
                   // String.valueOf(major) + ";" + String.valueOf(minor);        // devNum : ID
        }
        else
        {
            //에러 확인용
            Log.i("BleSocketPacket","makeRequst Msg unexpected cmd"+cmd);
        }
        return msg;
    }
}
