package com.xu.sinxiao.common.mvp.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.IpPrefix;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelStore;
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
import com.xu.sinxiao.common.view.SwipeBackLayout;

import java.util.List;

public abstract class BaseMVPFragment<T extends IPresent> extends BaseFragment implements IView {

    public String NAME = this.getClass().getName();
    private RootFramelayoutBinding rootFragmentBinding;
    protected T present;
    private boolean backSwipe = true;

    public BaseMVPFragment() {
    }

    public void setBackSwipe(boolean backSwipe) {
        this.backSwipe = backSwipe;
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

    public <T extends ViewDataBinding> T getViewDataBind(int resLayout) {
        return DataBindingUtil.inflate(LayoutInflater.from(getContext()), resLayout, null, false);
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
            view = LayoutInflater.from(getContext()).inflate(res, null);
            rootFragmentBinding.contentLayout.addView(view);
            rootFragmentBinding.contentLayout.setVisibility(View.VISIBLE);
        } else {
            rootFragmentBinding.contentLayout.addView(view);
            rootFragmentBinding.contentLayout.setVisibility(View.VISIBLE);
        }
        if (present == null) {
            present = createPresent();
        }
        View rootView = rootFragmentBinding.getRoot();
//        if (backSwipe) {
//            SwipeBackLayout swipeBackLayout = new SwipeBackLayout(getContext());
//            ViewGroup.LayoutParams layoutParams = swipeBackLayout.getLayoutParams();
//            if (layoutParams == null) {
//                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT);
//            } else {
//                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
//                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            }
//            swipeBackLayout.setLayoutParams(layoutParams);
//            swipeBackLayout.setFragment(this, view);
//            rootView = swipeBackLayout;
//        }
        return rootView;
    }

    GestureDetector.SimpleOnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e2.getY();

            if (x > 0) {
                //右滑
            } else {
                //左滑
                finishNow();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return super.getLifecycle();
    }

    @NonNull
    @Override
    public LiveData<LifecycleOwner> getViewLifecycleOwnerLiveData() {
        return super.getViewLifecycleOwnerLiveData();
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return super.getViewModelStore();
    }

    @Nullable
    @Override
    public Object getSharedElementReturnTransition() {
        return super.getSharedElementReturnTransition();
    }

    @Nullable
    @Override
    public Object getSharedElementEnterTransition() {
        return super.getSharedElementEnterTransition();
    }

    @Nullable
    @Override
    public Object getReturnTransition() {
        return super.getReturnTransition();
    }

    @Nullable
    @Override
    public Object getReenterTransition() {
        return super.getReenterTransition();
    }

    @Nullable
    @Override
    public Object getExitTransition() {
        return super.getExitTransition();
    }

    @Nullable
    @Override
    public Object getEnterTransition() {
        return super.getEnterTransition();
    }

    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }

    @NonNull
    @Override
    public LifecycleOwner getViewLifecycleOwner() {
        return super.getViewLifecycleOwner();
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
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
//            rootFragmentBinding.contentLayout.setVisibility(View.GONE);
        });
    }

    @Override
    public void dissLoading() {
        UIExecutor.postRunable(() -> {
//            rootFragmentBinding.contentLayout.setVisibility(View.VISIBLE);
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
            } catch (java.lang.InstantiationException e) {
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
