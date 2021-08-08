package com.example.chirtcpsocket;

import android.annotation.SuppressLint;
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
    TextView Dlight,Dwet,Dwind,Dtemp,Dwatt,Dhottemp,Dco2,Dch2o,Dchemical,Dpm25,Dpm10,Dsensor,Ddrop,Dface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Dlight=(TextView)getView().findViewById(R.id.data_light);
        Dwet=(TextView)getView().findViewById(R.id.data_wet);
        Dwind=(TextView)getView().findViewById(R.id.data_wind);
        Dtemp=(TextView)getView().findViewById(R.id.data_temp);
        Dwatt=(TextView)getView().findViewById(R.id.data_watt);
        Dhottemp=(TextView)getView().findViewById(R.id.data_hottemp);
        Dco2=(TextView)getView().findViewById(R.id.data_co2);
        Dch2o=(TextView)getView().findViewById(R.id.data_ch2o);
        Dchemical=(TextView)getView().findViewById(R.id.data_chemical);
        Dpm25=(TextView)getView().findViewById(R.id.data_pm25);
        Dpm10=(TextView)getView().findViewById(R.id.data_pm10);
        Dsensor=(TextView)getView().findViewById(R.id.data_sensor);
        Ddrop=(TextView)getView().findViewById(R.id.data_drop);
        Dface=(TextView)getView().findViewById(R.id.data_face);

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
        @SuppressLint("SetTextI18n")
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
                            int countBytesRead = bis.read(buffer, 0, 255);

                            for(int i=0 ;i<buffer.length;i++)
                                buffer_decode[i]=(buffer[i]&0xff);// byte java:-128~127 to 0~255


                            if(buffer_decode[0]==0xFF && buffer_decode[1]==0xA0)//收資料
                            {
                                try
                                {
                                    int dlight=(buffer_decode[2]*256)
                                                    +(buffer_decode[3]);
                                    Dlight.setText(Integer.toString(dlight));

                                    double dtemp=((buffer_decode[4]*256)
                                            +(buffer_decode[5]));
                                    dtemp=dtemp/10;
                                    Dtemp.setText(Double.toString(dtemp));

                                    double dwet=(buffer_decode[6]*256)
                                            +(buffer_decode[7]);
                                    dwet=dwet/10;
                                    Dwet.setText(Double.toString(dwet));

                                    double dwatt=(buffer_decode[8]*65536)
                                            +(buffer_decode[9]*256)
                                            +(buffer_decode[10]);
                                    dwatt=dwatt/100;
                                    Dwatt.setText(Double.toString(dwatt));////

                                    double dwind=(buffer_decode[11]*256)
                                            +(buffer_decode[12]);
                                    dwind=dwind/10;
                                    Dwind.setText(Double.toString(dwind));

                                    double dhottemp=(buffer_decode[13]*256)
                                            +(buffer_decode[14]);
                                    dhottemp=dhottemp/10;
                                    Dhottemp.setText(Double.toString(dhottemp));

                                    double dco2=(buffer_decode[15]*256)
                                            +(buffer_decode[16]);
                                    Dco2.setText(Double.toString(dco2));

                                    double dch2o=(buffer_decode[17]*256)
                                            +(buffer_decode[18]);
                                    Dch2o.setText(Double.toString(dch2o));

                                    double dchemical=(buffer_decode[19]*256)
                                            +(buffer_decode[20]);
                                    Dchemical.setText(Double.toString(dchemical));

                                    double dpm25=(buffer_decode[21]*256)
                                            +(buffer_decode[22]);
                                    Dpm25.setText(Double.toString(dpm25));

                                    double dpm10=(buffer_decode[23]*256)
                                            +(buffer_decode[24]);
                                    Dpm10.setText(Double.toString(dpm10));

                                    double dpmsensor=(buffer_decode[25]);
                                    Dsensor.setText(Double.toString(dpmsensor));

                                    double ddrop=(buffer_decode[26]);
                                    Ddrop.setText(Double.toString(ddrop));

                                    double dfaca=(buffer_decode[27]);
                                    Dface.setText(Double.toString(dfaca));


                                }
                                catch (Exception e)
                                {
                                    Log.e("Exception",e.toString());
                                }
                            }
                            else
                                allclear();



                        }

                    }//try
                    catch (java.io.IOException e)

                    {
                        allclear();
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
    private void allclear()
    {
        Dlight.setText("0");
        Dtemp.setText("0");
        Dwet.setText("0");
        Dwatt.setText("0");
        Dwind.setText("0");
        Dhottemp.setText("0");
        Dco2.setText("0");
        Dch2o.setText("0");
        Dchemical.setText("0");
        Dpm25.setText("0");
        Dpm10.setText("0");
        Dsensor.setText("0");
        Ddrop.setText("0");
        Dface.setText("0");
    }




}
