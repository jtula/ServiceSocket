package com.example.joseph.servicesocket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText serverIp, serverPort;
    Button buttonOn, buttonOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverIp = (EditText) findViewById(R.id.serverIp);
        serverPort = (EditText) findViewById(R.id.serverPort);
        buttonOn = (Button) findViewById(R.id.buttonOn);
        buttonOff = (Button) findViewById(R.id.buttonOff);

        buttonOn.setOnClickListener(buttonClickListener);
        buttonOff.setOnClickListener(buttonClickListener);
    }

    View.OnClickListener buttonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.buttonOn:
                    String strIp = serverIp.getText().toString();
                    String strPort = serverPort.getText().toString();

                    if (strIp.matches("") || strPort.matches("")) {
                        Toast.makeText(MainActivity.this, "Please fill all inputs!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent serviceIntent = new Intent(MainActivity.this, SocketClientService.class);
                    serviceIntent.putExtra("serverIp", strIp);
                    serviceIntent.putExtra("serverPort", strPort);
                    startService(serviceIntent);
                    break;
                case R.id.buttonOff:
                    stopService(new Intent(MainActivity.this, SocketClientService.class));
                    break;
            }
        }
    };
}
