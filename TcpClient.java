package com.goyourlife.gofit_demo;

import android.util.Base64;
import android.util.Log;
import android.util.Xml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public class TcpClient {

    public static final String TAG = TcpClient.class.getSimpleName();
    public static final String SERVER_IP = "cindy123.cloudapp.net"; //server IP address
    //public static final String SERVER_IP ="192.168.1.107";
    public static final int SERVER_PORT = 36000;
    // message to send to the server
    private String mServerMessage;
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;
    // while this is true, the server will continue running
    private boolean mRun = false;
    // used to send messages
    private PrintWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;
    private boolean clientSendMsg;

    private boolean closing =false;
    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessage(final String message) {
        final TcpClient tcpclient = this;
        StringBuilder sb = new StringBuilder();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mBufferOut != null) {
                        if(message == ""){
                            tcpclient.stopClient();
                        }
                        else
                        {
                        Log.d(TAG, "Sending: " + message);

                        mBufferOut.println(message);
                        mBufferOut.flush();
                }
                }


            }


        };

        Thread thread = new Thread(runnable);
        thread.start();


    }
    /**
     * Close the connection and release the members
     */
    public void stopClient() {

        mRun = false;

        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;


    }

    public void run() {

        mRun = true;

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            Log.d("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVER_PORT);

            try {

                //sends the message to the server
                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);

                //receives the message which the server sends back
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));

                int charsRead =0;
                char [] buffer = new char[1024];

                //charsRead=mBufferIn.read(buffer);

                //this.sendMessage("I am Client");
                //in this while the client listens for the messages sent by the server
                while (mRun) {


                    charsRead = mBufferIn.read(buffer);
                    //Log.d("charsREad",Integer.toString(charsRead));
                    mServerMessage = new String(buffer).substring(0, charsRead);
                    //mServerMessage = mBufferIn.readLine();

                    if (mServerMessage !=null  && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(mServerMessage);
                        Log.d("RESPONSE FROM SERVER", "S: Received Message: '" + mServerMessage + "'");
                    }


                }



            } catch (Exception e) {
                socket.close();
                //Log.e("TCP", "S: Error", e);
            }
            finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                Log.d("close socket","Close");
                socket.close();
            }

        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the Activity
    //class at on AsyncTask doInBackground

    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

}
