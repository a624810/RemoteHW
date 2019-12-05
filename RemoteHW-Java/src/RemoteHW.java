import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.DataInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class RemoteHW {
   String ReceiveIp;
   int ReceivePort;
   DatagramPacket packet;
   byte[] inbuf = new byte[1024];
   
   
   public static final int WHEEL_UP = -1;
   public static final int WHEEL_DOWN = 1;
   
   public class Wheel extends Thread{
      Robot robot;
      boolean flag = true;
      int UpDown;
      
      public Wheel(Robot robot, int UpDown) {
            this.robot = robot;
            this.UpDown = UpDown;
      }
      @Override
      public void run() {
         while(flag) {
            if(UpDown == WHEEL_UP)
               robot.mouseWheel(-1);
            else
               robot.mouseWheel(1);
            try {
               sleep(30);
            } catch (InterruptedException e) {
               
            }
         }
      }
      
      public void setflag(boolean flag) {
         this.flag = flag;
      }
   };

   
   public RemoteHW(){
      try {
         Client tx = new Client();
         PointerInfo   pointer = MouseInfo.getPointerInfo();
         Robot robot = new Robot();   
         DatagramSocket MyService = new DatagramSocket(11001);
         System.out.println("서버가 활성되었습니다.");   
         Wheel upwheel= null;
         Wheel downwheel= null;
         
      while (true) {
         packet = new DatagramPacket(inbuf,inbuf.length);
         MyService.receive(packet);
         String str = new String(inbuf, 0, packet.getLength());
         System.out.println("클라이언트 측 데이터 받아오기");
         System.out.println("받아온 메시지: " + str);
         String sxy = str.toString();
         //여기서부터 받아온 메시지 처리 ********************
         
         switch(sxy) {
         case "LPress":
            robot.mousePress(InputEvent.BUTTON1_MASK);   
            break;
         case "LRelease":
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            break;
         case "RPress":
            robot.mousePress(InputEvent.BUTTON3_MASK);
            break;
         case "RRelease":
            robot.mouseRelease(InputEvent.BUTTON3_MASK);
            break;
         case "LKeyPress":
            robot.keyPress(37);
            break;
         case "LKeyRelease":
            robot.keyRelease(37);
            break;
         case "RKeyPress":
            robot.keyPress(39);
            break;
         case "RKeyRelease":
            robot.keyRelease(39);
            break;
         case "UpWheelPress":
        	upwheel = new Wheel(robot,WHEEL_UP);
            upwheel.start();
            break;
         case "UpWheelRelease":
        	upwheel.setflag(false);
            break;
         case "DownWheelPress":
        	downwheel = new Wheel(robot,WHEEL_DOWN);
        	downwheel.start();
            break;
         case "DownWheelRelease":
        	downwheel.setflag(false);
            break;
            
         default:
            if(sxy.contains("speakerOn"))
              {   
                  StringTokenizer ReceiveIPort = new StringTokenizer(sxy);
                  ReceiveIp = ReceiveIPort.nextToken(",");
                  tx.SetIp(ReceiveIp);
                 tx.SetHz(48000);
                 tx.SetBits(16);
                  tx.captureAudio();
                  break;
              }
            else if(sxy.equals("speakerOff")) {
               tx.stopCapture=true;
               break;
            }
            StringTokenizer XYpoint = new StringTokenizer(sxy);
            String x = XYpoint.nextToken(",");
            String y = XYpoint.nextToken(","); 
            pointer = MouseInfo.getPointerInfo();
                int x1 = Integer.parseInt(x); 
                int y1 = Integer.parseInt(y);
               robot.mouseMove(pointer.getLocation().x + x1, pointer.getLocation().y + y1);
            break;
         }
         //받아온 메시지 처리 끝 ********************
         System.out.println("종료");
      }
      
   }catch (Exception e){
      e.printStackTrace();
   }
}
   
}