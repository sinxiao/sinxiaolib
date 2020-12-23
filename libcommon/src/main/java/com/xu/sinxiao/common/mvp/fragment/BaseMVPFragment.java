package com.xu.sinxiao.common.mvp.fragment;

import android.content.Intent;
import android.net.IpPrefix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xu.sinxiao.common.DialigUtils;
import com.xu.sinxiao.common.R;
import com.xu.sinxiao.common.ToastUtils;
import com.xu.sinxiao.common.UIExecutor;
import com.xu.sinxiao.common.databinding.RootFramelayoutBinding;
import com.xu.sinxiao.common.fragment.BaseFragment;
import com.xu.sinxiao.common.logger.Logger;
import com.xu.sinxiao.common.mvp.BasePresent;
import com.xu.sinxiao.common.mvp.IPresent;
import com.xu.sinxiao.common.mvp.IView;
import com.xu.sinxiao.common.mvp.MvpEvent;
import com.xu.sinxiao.common.recyleview.CommonRecycleViewAdapter;
import com.xu.sinxiao.common.recyleview.base.BaseViewHolderItem;

import java.util.List;

public abstract class BaseMVPFragment<T extends IPresent> extends BaseFragment implements IView {

    public String NAME = this.getClass().getName();
    private RootFramelayoutBinding rootFragmentBinding;
    protected T present;

    public BaseMVPFragment() {
    }

    public abstract T createPresent();

    public abstract int getResView();

    public View getMainView() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showDataView() {
        UIExecutor.postRunable(() -> {
            rootFragmentBinding.contentLayout.setVisibility(View.VISIBLE);
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
//        rootFragmentBinding = DataBindingUtil.setContentView(this, R.layout.root_framelayout);
        rootFragmentBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.root_framelayout, null, false);
        View view = getMainView();
        if (view == null) {
            int res = getResView();
            if (res == 0) {
                res = R.layout.default_framelayout;
            }
            rootFragmentBinding.contentLayout.addView(LayoutInflater.from(getContext()).inflate(res, null));
            rootFragmentBinding.contentLayout.setVisibility(View.VISIBLE);
        } else {
            rootFragmentBinding.contentLayout.addView(view);
            rootFragmentBinding.contentLayout.setVisibility(View.VISIBLE);
        }
        if (present == null) {
            present = createPresent();
        }

        return rootFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initHeader();
        bindData();
        if (present != null) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                present.dispatchEvent(MvpEvent.init(NAME, MvpEvent.TYPE_INIT, bundle));
            } else {
                present.dispatchEvent(MvpEvent.init(NAME, MvpEvent.TYPE_INIT));
            }

        }
        if (present != null) {
            present.fetchData();
        }
    }

    /**
     * 一般是绑定，控件的事件
     */
    public abstract void initView(View view);

    /**
     * 向控件上绑定数据
     */
    public abstract void bindData();

    public void initHeader() {

    }

    @Override
    public void showToast(String info) {
        ToastUtils.show(getContext(), info);
    }

    @Override
    public void showDialog(String info) {
        DialigUtils.showInforDialog(getContext(), info);
    }

    @Override
    public void showError(String error) {
        DialigUtils.showErrorDialog(getContext(), error);

    }

    @Override
    public void showLoading() {
        UIExecutor.postRunable(() -> {
            rootFragmentBinding.loadingLayout.setVisibility(View.VISIBLE);
            rootFragmentBinding.contentLayout.setVisibility(View.GONE);
        });
    }

    @Override
    public void dissLoading() {
        UIExecutor.postRunable(() -> {
            rootFragmentBinding.contentLayout.setVisibility(View.VISIBLE);
            rootFragmentBinding.loadingLayout.setVisibility(View.GONE);
        });

    }

    @Override
    public void finishNow() {
        getActivity().finish();
    }

    @Override
    public void launch(@NonNull Intent intent) {

    }

    @Override
    abstract public void freshData(Object object);

    public void showErrorView(String info) {
        UIExecutor.postRunable(() -> {
            rootFragmentBinding.txtTip.setText(info);
            rootFragmentBinding.layoutError.setVisibility(View.VISIBLE);
            rootFragmentBinding.layoutError.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (present != null) {
                        rootFragmentBinding.layoutError.setVisibility(View.GONE);
                        present.fetchData();
                    }
                }
            });
            rootFragmentBinding.contentLayout.setVisibility(View.GONE);
        });
    }

    @Nullable
    public static BaseFragment newInstance(@NonNull String name, @NonNull Bundle bundle) {
        BaseFragment baseFragment = newInstance(name);
        if (baseFragment != null) {
            baseFragment.setArguments(bundle);
        }

        return baseFragment;
    }

    public static BaseFragment newInstance(String name) {
        try {
            Class c = Class.forName(name);
            try {
                return (BaseFragment) c.newInstance();
            } catch (InstantiationException e) {
//                e.printStackTrace();
                Logger.e("" + e.getMessage());
            }
        } catch (ClassNotFoundException var2) {
//            LogUtils.e(var2);
            Logger.e("" + var2.getMessage());
        } catch (InstantiationException var3) {
//            LogUtils.e(var3);
            Logger.e("" + var3.getMessage());
        } catch (IllegalAccessException var4) {
//            LogUtils.e(var4);
            Logger.e("" + var4.getMessage());
        }

        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (present != null) {
            present.destory();
        }
    }

    public void bindData(RecyclerView recyclerView, List<BaseViewHolderItem> datas) {
        CommonRecycleViewAdapter commonRecycleViewAdapter = new CommonRecycleViewAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(commonRecycleViewAdapter);
        commonRecycleViewAdapter.updateData(datas);
    }
}