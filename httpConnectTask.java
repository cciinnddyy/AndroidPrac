package com.goyourlife.gofit_demo;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.net.URL;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.net.HttpURLConnection;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

import android.app.Activity;

public class httpConnectTask extends AsyncTask<String,String,String>{

    //NETWORK_GET表示发送GET请求
    public static final String NETWORK_GET = "NETWORK_GET";
    //NETWORK_POST_KEY_VALUE表示用POST发送键值对数据
    public static final String NETWORK_POST_KEY_VALUE = "NETWORK_POST_KEY_VALUE";
    //NETWORK_POST_XML表示用POST发送XML数据
    public static final String NETWORK_POST_XML = "NETWORK_POST_XML";
    //NETWORK_POST_JSON表示用POST发送JSON数据
    public static final String NETWORK_POST_JSON = "NETWORK_POST_JSON";

    @Override
    protected String doInBackground(String... strings) {
        //1.open fitness data file
        //fileinputstream =>

        //2.httpclient



        //3.httpPost
        //we can call publishprogress(progress) to execute onProgressUpdate
        String ans = "";
        Map<String,Object> results = new HashMap();
        URL apiAddress = null;
        HttpURLConnection conn =null;
        String requestHeader = null;
        byte[] requestBody = null;
        String responseHeader =null;
        byte[] responseBody = null;


        //http action 
        String action = strings[0];

        if(NETWORK_GET.equals(action)){

        }
        else if (NETWORK_POST_JSON.equals(action))
        {
            try {
                apiAddress = new URL("http://192.168.1.114:3000/step");

                conn = (HttpURLConnection)apiAddress.openConnection();

                conn.setRequestMethod("POST");

                //if there is a request body set to send
                conn.setDoOutput(true);

                conn.setRequestProperty("action",NETWORK_POST_JSON);

                OutputStream os = conn.getOutputStream();

                String fakeBody = "{'name':'Gary','macAddress':1234567}";

                requestBody = strToByteArray(fakeBody);

                os.write(requestBody);


                os.flush();

                os.close();

                InputStream is = conn.getInputStream();
                responseBody = getbyteByInputString(is);

                ans = getStringByBytes(responseBody);


            }


            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        else if(NETWORK_POST_KEY_VALUE.equals(action))
        {

        }


    return ans;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //UI can be changed in here
        //MainActivity
        MainActivity._tag = result;



    }


    //onProgressUpdate (progress); This method is running at main thread
    protected void onProgressUpdate(){
        super.onProgressUpdate();

        //progressDialog.setMessage();

    }





    //2.parse string to Byte[]

    private static byte[] strToByteArray(String str) {
            if (str == null) {
                return null;
            }
            byte[] byteArray = str.getBytes();
            return byteArray;
        }

    //Read data from the inputStream then close the inputStream
    private byte[] getbyteByInputString (InputStream is) throws IOException {
        byte[] result = null;
        BufferedInputStream bfs = new BufferedInputStream(is);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        BufferedOutputStream bfo = new BufferedOutputStream(bao);

        byte[] buffer = new byte[1024*8];

        int length = 0;

        while((length = bfs.read(buffer))>0){
            bfo.write(buffer,0,length);



        }

        bfo.flush();



        result = bao.toByteArray();

        bfo.close();
        bfs.close();

        return result;

    }

    //convert byte[] to string
    private String getStringByBytes(byte[] bytes) {
        String str = "";
        try {
                str = new String(bytes, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }


}