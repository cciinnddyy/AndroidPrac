package com.goyourlife.gofit_demo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;

public class ConnectTask extends AsyncTask<String, String, TcpClient> {

    private TcpClient mTcpClient;
    private boolean isClientStop;
    private String macAddress;
    //private String stepTable;
    @Override
    protected TcpClient doInBackground(String... message) {

        macAddress=message[0];
        //this.stepTable = message[0];

        //we create a TCPClient object
        mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
            @Override
            //here the messageReceived method is implemented
            public void messageReceived(String message) {
                //this method calls the onProgressUpdate
                if(message!=""){
                    publishProgress(message);
                }



            }
        });


        mTcpClient.run();



        return null;
    }


    @Override
    protected void onPostExecute(TcpClient mTcpClient){
        super.onPostExecute(mTcpClient);

        Log.d("Finish","Finish");
    }

    public String getDateTime(){
        SimpleDateFormat sdFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String strDate = sdFormat.format(date);
//System.out.println(strDate);
        return strDate;
    }
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        //response received from server
        Log.d("test", "response " + values[0]);

        if(values[0]=="Closing"){
            mTcpClient.stopClient();
        }
        else {
            //process server response here....
            //read file (steptable)
            java.util.TimeZone tz = java.util.TimeZone.getDefault();
            String timeZone = tz.getID();
            Context cxt = MainActivity.maincontext;
            String timenow = getDateTime();


            try {

                String[] filenames = new String[4];

                filenames[0] = "stepTable.json";
                filenames[1] = "sleepTable.json";
                filenames[2] = "pulseTable.json";
                filenames[3] = "fitnessTable.json";

                FileInputStream fts;
                BufferedReader bfw;
                for (String filename : filenames) {
                    fts = cxt.openFileInput(filename);

                    //Log.d("length:", Integer.toString(length)+"\n"+Integer.toString(length02));
                    //Log.d("length of the file",Integer.toString(fts.available()));
                    StringBuilder sb = new StringBuilder();
                    InputStreamReader inputStreamReader = new InputStreamReader(fts, "UTF-8");
                    bfw = new BufferedReader(inputStreamReader);

                    while (bfw.ready()) {

                        sb.append("{\"FileName\":\"" + filename + "\",\"MacAddress\":\"" + macAddress + "\",\"timeZone\":\"" + timeZone + "\",\"Content\":");
                        String line;

                        while ((line = bfw.readLine()) != null) {
                            sb.append(line);

                        }

                        sb.append("}");

                    }
                    //mTcpClient.sendMessage(Integer.toString(sb.length()));
                    //sb is correct file content
                    mTcpClient.sendMessage(sb.toString());

                    bfw.close();

                }


                //mTcpClient.stopClient();
                mTcpClient.sendMessage("");


                //BufferReader的作用是为其它Reader提供缓冲功能。创建BufferReader时，我们会通过它的构造函数指定某个Reader为参数。
                // BufferReader会将该Reader中的数据分批读取，每次读取一部分到缓冲中；操作完缓冲中的这部分数据之后，再从Reader中读取下一部分的数据。
                //为什么需要缓冲呢？原因很简单，效率问题！缓冲中的数据实际上是保存在内存中，而原始数据可能是保存在硬盘或NandFlash中
                // 而我们知道，从内存中读取数据的速度比从硬盘读取数据的速度至少快10倍以上。
                //String data = bfw.readLine();
                //mTcpClient.sendMessage(data);
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
        }

    }

}

