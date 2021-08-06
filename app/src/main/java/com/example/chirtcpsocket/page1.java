package com.example.chirtcpsocket;

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

public class page1 extends Fragment {
    Thread threadA;
    MyBroadcastReceiver mMyReceiver;

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
            int a=0;
            while(true)
            {
                try
                {
                    a+=1;
                    Thread.sleep(1000);
                    Log.v("broadcast",mMyReceiver.data_light+"");
                }
                catch (Exception e)
                {
                    Log.e("Exception",e.toString());
                }

            }

        }
    };





}
