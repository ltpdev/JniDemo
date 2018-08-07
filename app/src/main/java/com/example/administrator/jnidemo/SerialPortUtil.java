package com.example.administrator.jnidemo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPortUtil {
   public static SerialPort serialPort=null;
   public static InputStream inputStream=null;
   public static OutputStream outputStream=null;
   public static Thread receiveThread=null;
    public static boolean flag = false;
    public static String serialData;


    public static void openSerialPort(){
          try {
              serialPort=new SerialPort(new File("/dev/ttyS3"),4800,0);
              inputStream=serialPort.getInputStream();
              outputStream=serialPort.getOutputStream();
              flag=true;
              receiveSerialPort();
          }catch (Exception e){
              e.printStackTrace();
          }
    }
    /**
     * 接收串口数据的方法
     */
    private static void receiveSerialPort() {
        Log.i("test","接收串口数据");
        if(receiveThread != null)
            return;
        /*定义一个handler对象要来接收子线程中接收到的数据
            并调用Activity中的刷新界面的方法更新UI界面
         */
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1){
                    MainActivity.refreshTextView(serialData);
                }
            }
        };
        /*创建子线程接收串口数据
         */
        receiveThread = new Thread(){
            @Override
            public void run() {
                while (flag) {
                    try {
                        byte[] readData = new byte[1024];
                        if (inputStream == null) {
                            return;
                        }
                        int size = inputStream.read(readData);
                        if (size>0 && flag) {
                            serialData = new String(readData,0,size);
                            Log.i("test", "接收到串口数据:" + serialData);
                            /*将接收到的数据封装进Message中，然后发送给主线程
                             */
                            handler.sendEmptyMessage(1);
                            Thread.sleep(1000);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        //启动接收线程
        receiveThread.start();

    }


    public static void  closeSerialPort(){
        try {
            if(inputStream != null) {
                inputStream.close();
            }
            if(outputStream != null){
                outputStream.close();
            }
            if (serialPort!=null){
                serialPort.close();
            }
            flag = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendSerialPort(String data){
        Log.i("test","发送串口数据");
        try {
              byte[]sendData=data.getBytes();
              outputStream.write(sendData);
              outputStream.flush();
            Log.i("test","串口数据发送成功");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("test","串口数据发送失败");
        }
    }





}
