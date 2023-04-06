package com.ivision.gamereact.model;


import com.tuio.TuioClient;

public class GamepadClient  extends TuioClient {

    public void connect() {
        if(!isConnected()) super.connect();
    }
}
