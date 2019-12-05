package com.example.remotehw;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void Start(View v) {
        Intent intent = new Intent(getApplicationContext(), Mouse_Activity.class);
        EditText IpTxt = findViewById(R.id.editText);

        String ip = IpTxt.getText().toString();
        int port = 11001;
        ClientSocket clientSocket = new ClientSocket(ip,port);
        Bundle bundle = new Bundle();
        bundle.putSerializable("clientSocket",clientSocket);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
