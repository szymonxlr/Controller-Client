package com.example.szymonxlr.a209382_projekt;

import java.net.Socket;

/**
 * Created by szymonxlr on 21.01.2018.
 */
public class SocketHandler {
    private static Socket socket;

    public static synchronized Socket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(Socket socket){
        SocketHandler.socket = socket;
    }
}