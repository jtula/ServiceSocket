package com.example.joseph.servicesocket;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class SocketClientService extends Service {

    SocketClientTask socketClientTask;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service created!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String serverIp = (String) intent.getExtras().get("serverIp");
        int serverPort = Integer.parseInt((String) intent.getExtras().get("serverPort"));
        socketClientTask = new SocketClientTask(serverIp, serverPort );
        socketClientTask.execute();

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service destroyed!", Toast.LENGTH_SHORT).show();
        socketClientTask.cancel(true);
        socketClientTask = null;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private class SocketClientTask extends AsyncTask<String, String, SocketClient> {

        private SocketClient tcpClient;
        private String serverIp;
        private int serverPort;


        public SocketClientTask(String serverIp, int serverPort) {
            this.serverIp = serverIp;
            this.serverPort = serverPort;
        }


        @Override
        protected SocketClient doInBackground(String... params) {
            try {
                tcpClient = new SocketClient(serverIp, serverPort, new SocketClient.MessageCallback() {
                    @Override
                    public void callbackMessageReceiver(String message) {
                        try {
                            publishProgress(message);
                            if (message != null) {
                                Log.d("Socket Message: ", message);
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
        }


        @Override
        protected void onCancelled() {
            tcpClient.sendMessage("Closing connection..");
            tcpClient.stopClient();
            super.onCancelled();
        }
    }
}
