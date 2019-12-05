package com.example.remotehw;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class SpeakerThread extends Thread{
    //버퍼크기 설정
    private byte[] inbuf = new byte[1024];
    private DatagramSocket MyService = null;
    private AudioTrack track = null;
    private DatagramPacket packet;
    private String serverIP = null;
    private ClientSocket clientSocket;
    private int serverPort = 50005;

    SpeakerThread(ClientSocket clientSocket){
        this.clientSocket = clientSocket;
    }
    @Override
    public void run() {
        try {
            Log.d("b","소켓 객체 생성");
            MyService = new DatagramSocket(serverPort);
            sendSpeakerOnOff(true);
            track = new AudioTrack(AudioManager.STREAM_MUSIC, 48000, AudioFormat.CHANNEL_OUT_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT, 1024*4, AudioTrack.MODE_STREAM);
            track.play();
            while (true) {
                //패킷 받기~
                packet = new DatagramPacket(inbuf, inbuf.length);
                MyService.receive(packet);
                inbuf=packet.getData();

                track.write(inbuf, 0,1024);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(track != null) {
                track.stop();
            }
            if(MyService != null) {
                MyService.close();
            }
            sendSpeakerOnOff(false);
            Log.d("d","thread dead");
        }
    }

    public void socketClose() { // Call me from some other thread

        MyService.close();

    }

    //스마트폰(Server) IP 가져오기
    private void getServerIP(){
        try {
            Socket socket = new Socket("www.google.com", 80);
            serverIP = socket.getLocalAddress().toString();
            serverIP = serverIP.substring(1,serverIP.length());
            Log.d("a", serverIP);
            socket.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendSpeakerOnOff(boolean onOff){
        String ip = clientSocket.getIp();
        int port = clientSocket.getPort();
        SocketAddress socketAddress = new InetSocketAddress(ip,port);
        getServerIP();
        //serverIP, serverPort 패킷전송
        try {
            DatagramSocket sock = new DatagramSocket();
            sock.connect(socketAddress);
            String data = serverIP + ",speakerOn";
            if(!onOff){
                data = "speakerOff";
            }
            DatagramPacket datagramPacket = new DatagramPacket(data.getBytes(), data.getBytes().length);
            sock.send(datagramPacket);
            sock.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}