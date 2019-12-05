package com.example.remotehw;

import android.util.Log;
import android.view.MotionEvent;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class MouseTouch {
    private ClientSocket clientSocket = null;
    private MotionEvent event;

    private float x;
    private float y;
    private float tx;
    private float ty;

    private DatagramSocket sock;

    MouseTouch(ClientSocket clientSocket){
        this.clientSocket = clientSocket;
        openSocket();
    }

    public void setEvent(MotionEvent event){
        this.event = event;
    }

    public void MouseMove() {
//        int pointer_count = event.getPointerCount();
//        if(pointer_count > 2) pointer_count = 2;

        x = event.getX();
        y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("1번", "ACTION_Down");
                setTx(x);
                setTy(y);
                break;
            case MotionEvent.ACTION_UP:
                setTx(x);
                setTy(y);
                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                    event.getPointerId(2); //터치한 순간부터 부여되는 포인트 고유번호.
//                    x = (int) (event.getX(2));
//                    y = (int) (event.getY(2));
//                break;
            case MotionEvent.ACTION_MOVE:
                SendMouseLocation();
                Log.d("3번", "ACTION_Move");
                break;
        }
    }

    public void openSocket(){
        Thread thread = new Thread() {
            @Override
            public void run() {
                String ip = clientSocket.getIp();
                int port = clientSocket.getPort();
                Log.d("ip", ip);
                Log.d("port", String.valueOf(port));
                SocketAddress socketAddress = new InetSocketAddress(ip, port);
                try {
                    sock = new DatagramSocket();
                    sock.connect(socketAddress);
                    Log.d("broadCast", String.valueOf(sock.getBroadcast()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public void SendMouseLocation() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                byte sendX = 0;
                byte sendY = 0;
                try {
                    sendX = (byte) ((tx - x) * (-1));
                    sendY = (byte) ((ty - y) * (-1));
                    String data = sendX + "," + sendY;
                    Log.d("X :", String.valueOf(x));
                    Log.d("Y : ", String.valueOf(y));
                    Log.d("TX :", String.valueOf(tx));
                    Log.d("TY : ", String.valueOf(ty));
                    DatagramPacket datagramPacket = new DatagramPacket(data.getBytes(), data.getBytes().length);
                    sock.send(datagramPacket);
                    tx = x;
                    ty = y;
//                  sock.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public void setTx(float tx) {
        this.tx = tx;
    }

    public void setTy(float ty) {
        this.ty = ty;
    }
}
