package com.xu.sinxiao.common.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xu.sinxiao.common.AESUtil;
import com.xu.sinxiao.common.BackgroundExecutor;
import com.xu.sinxiao.common.Configer;
import com.xu.sinxiao.common.R;
import com.xu.sinxiao.common.UIExecutor;
import com.xu.sinxiao.common.Utils;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private Button btnClick;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Configer.getInstance().init(getContext());
        btnClick = view.findViewById(R.id.click);
        btnClick.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
//                String data = "xxxxx";
//                String encrypt = Utils.bytes2HexString(AESUtil.encryptByte2Byte(data.getBytes(), "123456"));
//                encrypt = Base64.encodeToString(AESUtil.encryptByte2Byte(data.getBytes(), "123456"), Base64.DEFAULT);
//                Utils.showError(" encrypt >>> " + encrypt);
//                byte[] datl = AESUtil.decryptByte2Byte(Base64.decode(encrypt, Base64.DEFAULT), "123456");
//                Utils.showError(" data >>> " + new String(datl));
                UIExecutor.postRunable(new Runnable() {
                    @Override
                    public void run() {
                        btnClick.setText("on change ....");
                    }
                });
                BackgroundExecutor.post(new Runnable() {
                    @Override
                    public void run() {
                        Utils.showError("BackgroundExecutor post ok ....");
                        Utils.saveSpfWithEncrypt(getContext(), "mainTest", "aaaa211223232099494xxxxxxxxxxxjfjfdhshdhuehfjdhjdhfdjh");
                        Utils.showLog("save mainTest ok ");
                        String encrpyData = Utils.getStringFromSpf(getContext(), "mainTest");
                        Utils.showLog("encrpyData is >>> " + encrpyData);
                        String data = Utils.getSpfWithEncrypt(getContext(), "mainTest");
                        Utils.showLog("data is >>> " + data);
                    }
                });
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

}