package com.example.chirtcpsocket;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.chirtcpsocket.ui.main.MyBroadcastReceiver;

import static android.provider.Contacts.SettingsColumns.KEY;

public class page3 extends Fragment {
    Button sendBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_page3, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IntentFilter itFilter = new IntentFilter("KEY");//KEY為廣播辨識碼
        MyBroadcastReceiver mMyReceiver = new MyBroadcastReceiver();
        getActivity().registerReceiver(mMyReceiver, itFilter); //註冊廣播接收器




        sendBtn=(Button)getView().findViewById(R.id.btnsend);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("bundleKey", "result");
                getParentFragmentManager().setFragmentResult("requestKey", bundle);
                Log.e("Isend","result");
                Log.e("Isend2","result");

                Intent qw=new Intent("KEY");
                qw.putExtra("light","hellola");
                getActivity().sendBroadcast(qw);

            }
        });
    }
}
