

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


public class MainFrame extends JFrame {
   //전역변수
   private JFrame frame;
   private JTextField txtIp;
   private JTextArea textArea;
   private JButton BtnExit;
   AudioFormat audioFormat;
   List<String> mixerNameLists;
   int CountMixer;
   String[] ArrayMixerNames;
   
   float hz;
   int bits;
   
   //메인
   public static void main(String[] args) {
      new MainFrame();
      RemoteHW remoteHw = new RemoteHW();
   }
   
   //ip를 얻어오는 메서드
   public String getIp() {
      String ip = "";
      try {
         InetAddress inetAddress = InetAddress.getLocalHost();
         ip = inetAddress.getHostAddress();
      }catch (UnknownHostException e) {
         e.printStackTrace();
      }
      return ip;
   }
   
   public MainFrame() {
      //프레임에 띄어줄 ip값
      String ip = getIp();

      //메인 프레임 생성
      frame = new JFrame();
      frame.getContentPane().setBackground(Color.WHITE);
      frame.setBounds(100, 100, 350, 368);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().setLayout(null);
      
      //ip :  라벨
      JLabel lblIp = new JLabel("ip \uC8FC\uC18C :");
      lblIp.setBounds(80, 12, 51, 15);
      frame.getContentPane().add(lblIp);
      //ip 값 텍스트필드에 출력
      txtIp = new JTextField();
      txtIp.setBounds(135, 10, 110, 21);
      txtIp.setEditable(false);
      txtIp.setText(ip);
      frame.getContentPane().add(txtIp);
      txtIp.setColumns(10);
      
      //종료버튼
      BtnExit = new JButton();
      BtnExit.setBounds(135, 260, 70, 25);
      BtnExit.setText("종료");
      //종료를 위한 액션리스너 생성
      BtnExit.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            //종료
            System.exit(0);
            
         }
      });
      frame.getContentPane().add(BtnExit);
      
      //사용설명을 위한 텍스트에어리어 생성
      Border border = new LineBorder(Color.LIGHT_GRAY, 4, true);   
      textArea = new JTextArea();
      textArea.setBackground(Color.WHITE);   
      textArea.setBorder(border);
      textArea.setForeground(Color.BLACK);
      textArea.setText("                                 *사용방법* \n      RemoteHW 어플리케이션을 핸드폰에\n      다운 받은 후 좌측에 보이는 IP주소를\n      어플리케이션 하단에 입력해준다.\n\n\n\n\n                                 *주의사항*\n     소리가 들리지 않을 시 사운드 제어판을 열어\n     스테레오 믹서를 기본장치로 등록해주세요\n     이후 환경에 맞는 출력장치를 선택해 주세요");
      textArea.setEditable(false);
      textArea.setBounds(35, 38, 264, 269);
      frame.getContentPane().add(textArea);
      textArea.setColumns(10);
      
      
      //프레임 보이기
      frame.setVisible(true);
   
   }
}