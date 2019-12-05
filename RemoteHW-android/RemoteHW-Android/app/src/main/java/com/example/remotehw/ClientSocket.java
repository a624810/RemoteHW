package com.example.remotehw;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

@SuppressWarnings("serial")
public class ClientSocket extends Socket implements Serializable {

    String ip;
    int port;

    ClientSocket(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp(){
        return ip;
    }

    public int getPort(){
        return port;
    }
}