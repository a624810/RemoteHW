package com.example.remotehw;

import android.view.MotionEvent;
import android.view.View;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class ButtonTouch implements View.OnTouchListener{
    String sendKey1 = "";
    String sendKey2 = "";
    ClientSocket clientSocket = null;
    String data;

    ButtonTouch(ClientSocket clientSocket, String sendKey1, String sendKey2){
        this.clientSocket = clientSocket;
        this.sendKey1 = sendKey1;
        this.sendKey2 = sendKey2;
    }

    ButtonTouch(ClientSocket clientSocket, String sendKey1){
        this.clientSocket = clientSocket;
        this.sendKey1 = sendKey1;
        this.sendKey2 = null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                sendButton(sendKey1);
                break;
            case MotionEvent.ACTION_UP:
                if(sendKey2!=null) {
                    sendButton(sendKey2);
                }
                break;
        }
        return false;
    }

    public void sendButton(String Data) {
        this.data = Data;
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    String ip = clientSocket.getIp();
                    int port = clientSocket.getPort();
                    SocketAddress socketAddress = new InetSocketAddress(ip,port);
                    DatagramSocket sock = new DatagramSocket();
                    sock.connect(socketAddress);
                    DatagramPacket datagramPacket = new DatagramPacket(data.getBytes(), data.getBytes().length);
                    sock.send(datagramPacket);
                    sock.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}