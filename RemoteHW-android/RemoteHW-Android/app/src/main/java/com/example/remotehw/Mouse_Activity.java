package com.example.remotehw;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class Mouse_Activity extends AppCompatActivity {
    private ClientSocket clientSocket;
    private SpeakerThread speakerThread;
    private boolean sbtnEnable = false;
    private boolean rockCheck = false;
    private LinearLayout mouseLayout;
    private LinearLayout pptLayout;
    private ImageButton mouseLbtn;
    private ImageButton mouseRbtn;
    private ImageButton mouseWheelUbtn;
    private ImageButton mouseWheelDbtn;
    private ImageButton pptLBtn;
    private ImageButton pptRBtn;

    ImageButton mouseMenuBtn;
    ImageButton pptMenuBtn;
    ImageButton speakerMenuBtn;
    ImageButton rockMenuBtn;
    MouseTouch mouseTouch=null;

    View touchView = null;
    LinearLayout menuLayout = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse_);
        Intent intent = getIntent();
        clientSocket   =  (ClientSocket)intent.getSerializableExtra("clientSocket");
        mouseTouch = new MouseTouch(clientSocket);

        mouseLbtn = (ImageButton)findViewById(R.id.mouseLbtn);
        mouseRbtn = (ImageButton)findViewById(R.id.mouseRbtn);
        mouseWheelUbtn = (ImageButton)findViewById(R.id.mouseWheelUbtn);
        mouseWheelDbtn = (ImageButton)findViewById(R.id.mouseWheelDbtn);
        pptLBtn = (ImageButton)findViewById(R.id.pptLbtn);
        pptRBtn = (ImageButton)findViewById(R.id.pptRbtn);


        mouseMenuBtn = (ImageButton)findViewById(R.id.mouseMenuBtn);
        pptMenuBtn = (ImageButton)findViewById(R.id.pptMenuBtn);
        speakerMenuBtn = (ImageButton)findViewById(R.id.speakerMenuBtn);
        rockMenuBtn = (ImageButton)findViewById(R.id.rockMenuBtn);

        mouseLayout = (LinearLayout)findViewById(R.id.mouseLayout);
        pptLayout = (LinearLayout)findViewById(R.id.pptLayout);

        ButtonTouch mouseLeftBtnTouch = new ButtonTouch(clientSocket,"LPress","LRelease");
        mouseLbtn.setOnTouchListener(mouseLeftBtnTouch);

        ButtonTouch mouseRightBtnTouch = new ButtonTouch(clientSocket,"RPress","RRelease");
        mouseRbtn.setOnTouchListener(mouseRightBtnTouch);

        ButtonTouch mouseWheelUpBtnTouch = new ButtonTouch(clientSocket,"UpWheelPress","UpWheelRelease");
        mouseWheelUbtn.setOnTouchListener(mouseWheelUpBtnTouch);

        ButtonTouch mouseWheelDownBtnTouch = new ButtonTouch(clientSocket,"DownWheelPress","DownWheelRelease");
        mouseWheelDbtn.setOnTouchListener(mouseWheelDownBtnTouch);

        ButtonTouch pptLeftBtnTouch = new ButtonTouch(clientSocket,"LKeyPress","LKeyRelease");
        pptLBtn.setOnTouchListener(pptLeftBtnTouch);

        ButtonTouch pptRightBtnTouch = new ButtonTouch(clientSocket,"RKeyPress","RKeyRelease");
        pptRBtn.setOnTouchListener(pptRightBtnTouch);



        touchView = (View)findViewById(R.id.touchView);
        touchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mouseTouch.setEvent(event);
                mouseTouch.MouseMove();
                return true;
            }
        });

        menuLayout = (LinearLayout) findViewById(R.id.menuLayout);

        menuLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mouseTouch.setEvent(event);
                mouseTouch.MouseMove();
                return true;
            }
        });

        mouseMenuBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mouseLayout.setVisibility(LinearLayout.VISIBLE);
                pptLayout.setVisibility(LinearLayout.INVISIBLE);
                mouseMenuBtn.setImageResource(R.drawable.mouseon);
                pptMenuBtn.setImageResource(R.drawable.pptoff);
            }
        });

        pptMenuBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mouseLayout.setVisibility(LinearLayout.INVISIBLE);
                pptLayout.setVisibility(LinearLayout.VISIBLE);
                mouseMenuBtn.setImageResource(R.drawable.mouseoff);
                pptMenuBtn.setImageResource(R.drawable.ppton);
            }
        });

        speakerMenuBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(sbtnEnable) {
                    speakerThread.socketClose();
                    sbtnEnable = false;
                    speakerMenuBtn.setImageResource(R.drawable.speakeroff);
                }else{
                    speakerThread = new SpeakerThread(clientSocket);
                    speakerThread.start();
                    sbtnEnable = true;
                    speakerMenuBtn.setImageResource(R.drawable.speakeron);
                }
            }
        });
    }


    public void Rock(View v) {
        if(!rockCheck) {
            rockCheck = true;
            mouseMenuBtn.setVisibility(View.INVISIBLE);
            pptMenuBtn.setVisibility(View.INVISIBLE);
            speakerMenuBtn.setVisibility(View.INVISIBLE);
            rockMenuBtn.setImageResource(R.drawable.rockon);
        }else{
            rockCheck = false;
            mouseMenuBtn.setVisibility(View.VISIBLE);
            pptMenuBtn.setVisibility(View.VISIBLE);
            speakerMenuBtn.setVisibility(View.VISIBLE);
            rockMenuBtn.setImageResource(R.drawable.rockoff);
        }
    }
}
