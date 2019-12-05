
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class Client {
	
boolean stopCapture = false;
AudioFormat audioFormat;
TargetDataLine targetDataLine;
BufferedOutputStream out = null;
BufferedInputStream in = null;
DatagramSocket sock = null;
DatagramPacket datagramPacket = null;
String mixername=null;
Mixer mixerName;
String mixerNames[];
SourceDataLine sourceDataLine;
Mixer.Info[] mixerInfos;
String ip;
float hz;
int bits;
//public static void main(String[] args) {
//    Client tx = new Client();
//    tx.captureAudio();
//}

public void SetIp(String ip) {
	this.ip = ip;
}
public void SetHz(float hz) {
	this.hz = hz;
}
public void SetBits(int bits) {
	this.bits = bits;
}

public void captureAudio() {
    try {
    	//Datagrame Socket Open
    	
    		SocketAddress socketAddress = new InetSocketAddress(ip,50005);
            sock = new DatagramSocket();
            sock.connect(socketAddress);
    	
        //audioFormat을 가져와서 데이터라인을 설정
        
        audioFormat = getAudioFormat(hz,bits);
		DataLine.Info dataLineInfo = new DataLine.Info(
                TargetDataLine.class, audioFormat);
		showLineInfo(dataLineInfo);
	
        //오디오시스템 객체에서 위에서 설정한 데이터라인을 지원하는지 여부체크
        System.out.println("오디오시스템 라인 지원여부 : " + AudioSystem.isLineSupported(dataLineInfo));
       
		//시스템의 오디오 믹서 정보들을 가져온다.
        mixerInfos = AudioSystem.getMixerInfo();
        
        //믹서들의 이름과 위에서 설정한 데이터라인 지원여부를 체크
        List<Mixer> suppoertedMixer = getMixerSupportedLists(mixerInfos, dataLineInfo);
        
        for(Mixer mixer : suppoertedMixer) {
        	System.out.println(mixer.toString());
        }
        
        //소리를 가져올 믹스 설정
        Mixer mixer = suppoertedMixer.get(0);
        //위에서 설정한 데이터라인에 해당하는 라인을 믹서에서 가져와 열어줌
        targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
        targetDataLine.open(audioFormat);
        targetDataLine.start();
        
        
        //소리전송
        Thread captureThread = new CaptureThread();
        captureThread.start();

    } catch (Exception e) {
        System.out.println(e);
        try {
			sock.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		}
        System.exit(0);
        
    }
}
class CaptureThread extends Thread {
	//데이터 그램 패킷의 버퍼크기 설정
    byte tempBuffer[] = new byte[1024];

    @Override
    public void run() {
        stopCapture = false;
        try {
            while (!stopCapture) {
            	//타겟데이터라인에서 버퍼를읽어서 UDP소켓전송
                targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                datagramPacket = new DatagramPacket(tempBuffer,tempBuffer.length);
                sock.send(datagramPacket);
           }
           } catch (Exception e) {
            System.out.println(e);
            
            System.exit(0);
        }
        targetDataLine.close();
    }
}

//데이터 라인 설정 정보를 출력해줌.
public static void showLineInfo(final Line.Info lineInfo)
{
  System.out.println("  " + lineInfo.toString());

  if (lineInfo instanceof DataLine.Info)
   {
     DataLine.Info dataLineInfo = (DataLine.Info)lineInfo;

     AudioFormat [] formats = dataLineInfo.getFormats();
     for (final AudioFormat format : formats)
       System.out.println("    " + format.toString());
   }
}

//오디오 포맷 설정
public AudioFormat getAudioFormat(float SetsampleRate , int SetsampleSizeInBits) {
    float sampleRate = SetsampleRate;
    int sampleSizeInBits = SetsampleSizeInBits;
    int channels = 2;
    boolean signed = true;
    boolean bigEndian = false;
    System.out.println(SetsampleRate+","+SetsampleSizeInBits);
    return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
            bigEndian);
}

//믹서들의 이름과 위에서 설정한 데이터라인 지원여부를 체크하는 메소드
public List<Mixer> getMixerSupportedLists(Mixer.Info[] mixerInfos, DataLine.Info dataLineInfo) {

	List<Mixer> mixerList = new ArrayList<Mixer>();
	
	if (mixerInfos != null){
    	for (Info mixerInfo : mixerInfos) {
			Mixer mixer = AudioSystem.getMixer(mixerInfo);
			// if line supported
			if (mixer.isLineSupported(dataLineInfo)) {
				try {
					mixerList.add(mixer);
					String mixerName = new String(mixerInfo.getName().getBytes("iso-8859-1"),"ms949");
					System.out.println("믹서명 : " + mixerName + ", 믹서라인지원여부 : " + mixer.isLineSupported(dataLineInfo));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
				
			}
		}
    }
	return mixerList;

}
}

