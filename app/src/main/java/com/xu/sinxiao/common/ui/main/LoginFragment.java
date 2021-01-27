package com.xu.sinxiao.common.ui.main;

import android.net.Uri;
import android.view.View;

import com.xu.sinxiao.common.R;
import com.xu.sinxiao.common.base.Event;
import com.xu.sinxiao.common.databinding.LayoutLoginFragmentBinding;
import com.xu.sinxiao.common.mvvm.BaseMVVMFragment;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends BaseMVVMFragment<LoginViewModel> {
    public static final String PAGE_NAME = "com.xu.sinxiao.common.ui.main.LoginFragment";


    private LayoutLoginFragmentBinding databind;

    @Override
    public Class getViewModelType() {
        return LoginViewModel.class;
    }

    @Override
    public View getMainView() {
        databind = (LayoutLoginFragmentBinding) getViewDataBind(R.layout.layout_login_fragment);
        databind.setLifecycleOwner(this);
        return databind.getRoot();
    }

    @Override
    public int getResView() {
        return R.layout.layout_login_fragment;
    }

    @Override
    public void initView(View view) {
        databind.setLoginData(viewModel.loginData);
        databind.btnLogin.setOnClickListener(v -> {
            List<Uri> uris = new ArrayList<>();
//            viewModel.loginData.uris.set();
            viewModel.dispatchEvent(Event.init(PAGE_NAME, Event.TYPE_ENTER));
        });
    }

    @Override
    public void freshData(Object object) {

    }

//    @Override
//    public void freshData(Object object) {
//
//    }
}
