package com.example.safelight;

import java.util.ArrayList;

/**
 * Created by User on 2016-09-21.
 */
public class Send_StatePacket extends Thread
{
    private ArrayList<String> ID_ARRAY;
    private String state;

    public Send_StatePacket()
    {
        ID_ARRAY=new ArrayList<String>();
        state=null;
   //     mClientThread = mC;
    }

    public void set_State(String st){ this.state = st;}
    public String get_State(){return state;}

    public void set_ID(String ID)
    {
        ID_ARRAY.add(ID);
    }
    public void remove_ID(String ID)
    {
        ID_ARRAY.remove( ID_ARRAY.indexOf(ID));
    }
    public boolean check_ID(String ID)
    {
        return ID_ARRAY.contains(ID);
    }

    public void run()
    {

    }
}
