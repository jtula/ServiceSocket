package com.example.joseph.servicesocket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText serverIp, serverPort, packageName, socketMsg;
    Button buttonOn, buttonOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverIp = findViewById(R.id.serverIp);
        serverPort = findViewById(R.id.serverPort);
        packageName = findViewById(R.id.packageName);
        socketMsg = findViewById(R.id.socketMsg);
        buttonOn = findViewById(R.id.buttonOn);
        buttonOff = findViewById(R.id.buttonOff);

        buttonOn.setOnClickListener(buttonClickListener);
        buttonOff.setOnClickListener(buttonClickListener);
        buttonOff.setEnabled(false);
    }

    View.OnClickListener buttonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.buttonOn:
                    String strIp = serverIp.getText().toString();
                    String strPort = serverPort.getText().toString();
                    String pkgName = packageName.getText().toString();
                    String sckMsg = socketMsg.getText().toString();

                    if (strIp.matches("") || strPort.matches("") || pkgName.matches("") || sckMsg.matches("")) {
                        Toast.makeText(MainActivity.this, "Please fill all inputs!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent serviceIntent = new Intent(MainActivity.this, SocketClientService.class);
                    serviceIntent.putExtra("serverIp", strIp);
                    serviceIntent.putExtra("serverPort", strPort);
                    serviceIntent.putExtra("packageName", pkgName);
                    serviceIntent.putExtra("socketMsg", sckMsg);
                    startService(serviceIntent);
                    buttonOff.setEnabled(true);
                    buttonOn.setEnabled(false);
                    break;
                case R.id.buttonOff:
                    stopService(new Intent(MainActivity.this, SocketClientService.class));
                    buttonOn.setEnabled(true);
                    buttonOff.setEnabled(false);
                    break;
            }
        }
    };
}
