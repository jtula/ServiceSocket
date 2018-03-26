package com.example.joseph.servicesocket;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class SocketClient {

    private static final String TAG = "SocketClient";
    private String serverIp, incomingMessage;
    private int serverPort;
    private MessageCallback listener = null;
    private PrintWriter out;
    private BufferedReader in;
    private boolean run = false;


    public SocketClient(String serverIp, int serverPort, MessageCallback listener) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.listener = listener;
    }


    public void sendMessage(String message){
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
            Log.d(TAG, "Sent Message: " + message);
        }
    }


    public void stopClient() {
        run = false;
    }


    public void run() {

        run = true;

        try {

            Log.d(TAG, "Connecting...");

            Socket socket = new Socket(this.serverIp, this.serverPort);

            try {
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.d(TAG, "In/Out created");

                while (run) {

                    incomingMessage = in.readLine();

                    if (incomingMessage != null && listener != null)
                        listener.callbackMessageReceiver(incomingMessage);

                    incomingMessage = null;
                }

            } catch (Exception e) {
                Log.d(TAG, "Error", e);
            } finally {
                out.flush();
                out.close();
                in.close();
                socket.close();
                Log.d(TAG, "Socket Closed!");
            }

        } catch (Exception e) {
            Log.d(TAG, "Error", e);
        }

    }

    public boolean isRunning() {

        return run;
    }



    /**
     * Callback Interface for sending received messages to 'onPublishProgress' method in AsyncTask.
     *
     */
    public interface MessageCallback {
        /**
         * Method overriden in AsyncTask 'doInBackground' method while creating the com.example.joseph.servicesocket.SocketClient object.
         * @param message Received message from server app.
         */
        void callbackMessageReceiver(String message);
    }
}

