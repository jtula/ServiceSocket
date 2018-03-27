package com.example.joseph.servicesocket;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class SocketClientService extends Service {

    private static final String TAG = "SocketService";
    SocketClientTask socketClientTask;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service created!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String serverIp = (String) intent.getExtras().get("serverIp");
        String packageName = (String) intent.getExtras().get("packageName");
        String socketMsg = (String) intent.getExtras().get("socketMsg");
        int serverPort = Integer.parseInt((String) intent.getExtras().get("serverPort"));
        socketClientTask = new SocketClientTask(serverIp, serverPort, packageName, socketMsg );
        socketClientTask.execute();

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {

        super.onDestroy();

        Toast.makeText(this, "Service destroyed!", Toast.LENGTH_SHORT).show();

        SocketClient tcpClient = socketClientTask.getTCPClient();

        if (tcpClient.isRunning())
            tcpClient.stopClient();

        socketClientTask.cancel(true);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private class SocketClientTask extends AsyncTask<String, String, SocketClient> {

        private SocketClient tcpClient;
        private String serverIp, packageName, socketMsg;
        private int serverPort;


        public SocketClientTask(String serverIp, int serverPort, String packageName, String socketMsg) {
            this.serverIp = serverIp;
            this.serverPort = serverPort;
            this.packageName = packageName;
            this.socketMsg = socketMsg;
        }


        @Override
        protected SocketClient doInBackground(String... params) {
            try {
                tcpClient = new SocketClient(serverIp,
                                             serverPort,
                                             new SocketClient.MessageCallback() {
                    @Override
                    public void callbackMessageReceiver(String message) {
                        try {
                            publishProgress(message);
                            if (message != null) {
                                Log.d(TAG, message);
                            }
                        } catch (Exception e) {
                           e.printStackTrace();
                        }
                    }
                });
            } catch (NullPointerException e) {

                e.printStackTrace();
            }

            tcpClient.run();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {

            super.onProgressUpdate(values);

            if (values[values.length-1].equals(socketMsg)) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(launchIntent);
            }
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        public SocketClient getTCPClient() {
            return tcpClient;
        }
    }
}
