package com.xu.sinxiao.common.ui.main;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.databinding.adapters.TextViewBindingAdapter;
import com.xu.sinxiao.common.base.Event;
import com.xu.sinxiao.common.base.Status;
import com.xu.sinxiao.common.logger.Logger;
import com.xu.sinxiao.common.mvvm.BaseViewModel;
import com.xu.sinxiao.common.recyleview.base.BaseViewHolderItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LoginViewModel extends BaseViewModel<LoginViewModel.LoginStatus> {

    public LoginData loginData;

    public LoginViewModel() {
        loginData = new LoginData();
    }

    public static class LoginStatus extends Status {
        ArrayList<BaseViewHolderItem> items;

        public LoginStatus(ArrayList<BaseViewHolderItem> items) {
            data = items;
        }
    }

    public static class LoginData implements Serializable, Parcelable {
        public ObservableField<String> edtName = new ObservableField<>();
        public ObservableField<String> edtCode = new ObservableField<>();
        public ObservableField<String> area = new ObservableField<>();
        public ObservableField<List<Uri>> uris = new ObservableField<List<Uri>>();
        public ObservableField<String> videoPath = new ObservableField<String>();

        public TextViewBindingAdapter.OnTextChanged textChanged = new TextViewBindingAdapter.OnTextChanged() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        };

        public View.OnClickListener loginClick = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        };

        public LoginData() {

        }

        protected LoginData(Parcel in) {
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<LoginData> CREATOR = new Creator<LoginData>() {
            @Override
            public LoginData createFromParcel(Parcel in) {
                return new LoginData(in);
            }

            @Override
            public LoginData[] newArray(int size) {
                return new LoginData[size];
            }
        };

        public TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    @BindingAdapter("isGone")
    public void isGone(TextView textView, boolean isGone) {
        if (textView != null) {
            if (isGone) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDispatchEvent(Event event) {
        if (event.getType().equals(Event.TYPE_ENTER)) {
            String code = loginData.edtCode.get();
            String name = loginData.edtName.get();
            Logger.e("xxx name :: " + name + "  code >>> " + code);
        }
    }


    @Override
    public void fetchData() {

    }
}
