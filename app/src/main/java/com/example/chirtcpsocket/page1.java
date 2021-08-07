package com.example.chirtcpsocket;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.chirtcpsocket.ui.main.MyBroadcastReceiver;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;

public class page1 extends Fragment {
    Thread threadA;
    MyBroadcastReceiver mMyReceiver;
    Socket clientSocket;
    InetAddress serverIp;
    int serverPort;
    boolean CONEACCEPT_FLAG=false;
    boolean connectfrag;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("page1","created");
        threadA=new Thread(TCPconnect);
        threadA.start();


        IntentFilter itFilter = new IntentFilter("KEY");//KEY為廣播辨識碼
        mMyReceiver = new MyBroadcastReceiver();
        getActivity().registerReceiver(mMyReceiver, itFilter); //註冊廣播接收器




    }

    private Runnable TCPconnect=new Runnable() {
        @Override
        public void run() {

            while(true)
            {
                Log.e("thread","while");
                connectfrag=CheckConnect();
                if(connectfrag==false)
                    continue;
                else if(connectfrag==true)
                {
                    try
                    {
                        Log.v("connectstate","success");

                        InputStream is = clientSocket.getInputStream();
                        // Buffer the input stream
                        BufferedInputStream bis = new BufferedInputStream(is);
                        // Create a buffer in which to store the data
                        byte[] buffer = new byte[1024];
                        int[] buffer_decode=new int[1024];//資料在這
                        while (clientSocket.isConnected())
                        {
                            int countBytesRead = bis.read(buffer, 0, 30);

                            for(int i=0 ;i<buffer.length;i++)
                                buffer_decode[i]=(buffer[i]&0xff);// byte java:-128~127 to 0~255

                            if(buffer_decode[8]==0xff)
                            {
                                Log.e("data","ok");
                            }

                        }

                    }//try
                    catch (java.io.IOException e)

                    {
                        clientSocket=null;
                        connectfrag=false;
                    }

                }//else if(connectfrag==true)

            }//while(true)

        }//run
    };

    public boolean CheckConnect()
    {
        try {
            Log.e("llllllllllllllll", "1");


            serverIp = InetAddress.getByName("10.147.17.177");
            serverPort = Integer.valueOf("9998");

            Log.e("ADDR", serverIp.toString());
            Log.e("llllllllllllllll", "2");


            clientSocket=new Socket();
            InetSocketAddress isa = new InetSocketAddress(serverIp,serverPort);
            clientSocket.connect(isa,5000);

            Log.e("llllllllllllllll", "3");

            if (clientSocket != null) {
                CONEACCEPT_FLAG=true;
            }
            else
            {
                CONEACCEPT_FLAG=false;

                Log.e("llllllllllllllll", "4");
                //???????????????????製作跳至設定ip之fragment?????????????
            }
        } catch (IOException e) {

            Log.e("msg1", "Socket連線有問題 !");
            Log.e("msg1", "IOException :" + e.toString());
            //???????????????????製作跳至設定ip之fragment?????????????
            /*HomeFragment fm1= new HomeFragment();
            FragmentManager FM = getSupportFragmentManager();
            FM.beginTransaction().replace(R.id.nav_gallery,fm1).commit();*/
            return false;
        }

        if(CONEACCEPT_FLAG == false)
        {
            return false;
        }
        else
        {
            return true;
        }
    }


}
