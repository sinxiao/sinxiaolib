package com.xu.sinxiao.common.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.xu.sinxiao.common.BackgroundExecutor;
import com.xu.sinxiao.common.Configer;
import com.xu.sinxiao.common.R;
import com.xu.sinxiao.common.UIExecutor;
import com.xu.sinxiao.common.Utils;
import com.xu.sinxiao.common.mvp.IPresent;
import com.xu.sinxiao.common.mvp.fragment.BaseMVPFragment;

import java.util.HashMap;

import ae.sinxiao.android.qrd.ScanQrdActivity;
import ae.sinxiao.android.qrd.model.QrdScanResult;

public class MainFragment extends BaseMVPFragment {

    private MainViewModel mViewModel;
    private Button btnClick;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public IPresent createPresent() {
        return null;
    }

    @Override
    public int getResView() {
        return R.layout.main_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Configer.getInstance().init(getContext());
        btnClick = view.findViewById(R.id.click);
        btnClick.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                HashMap<String, String> exts = new HashMap<>();
                exts.put(ScanQrdActivity.KEY_Hide_Select_Image, "1");
                ScanQrdActivity.startScanQrdActivity(getContext(), new ScanQrdActivity.ScanListener() {
                    @Override
                    public void onScanSucess(QrdScanResult scanResult) {
                        Toast.makeText(getContext(), scanResult.getValue(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onScanFail(String msg) {

                    }

                    @Override
                    public void onScanCancled() {

                    }
                }, exts);
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
    public void initView(View view) {

    }

    @Override
    public void bindData() {

    }

    @Override
    public void freshData(Object object) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

}