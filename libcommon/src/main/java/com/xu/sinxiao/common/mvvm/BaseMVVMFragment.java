package com.xu.sinxiao.common.mvvm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.xu.sinxiao.common.DialigUtils;
import com.xu.sinxiao.common.R;
import com.xu.sinxiao.common.ToastUtils;
import com.xu.sinxiao.common.UIExecutor;
import com.xu.sinxiao.common.base.Event;
import com.xu.sinxiao.common.base.Status;
import com.xu.sinxiao.common.databinding.RootFramelayoutBinding;
import com.xu.sinxiao.common.fragment.BaseFragment;
import com.xu.sinxiao.common.logger.Logger;
import com.xu.sinxiao.common.mvp.IView;
import com.xu.sinxiao.common.recyleview.base.BaseViewHolderItem;

import java.util.List;

public abstract class BaseMVVMFragment<T extends BaseViewModel> extends BaseFragment implements IView {
    private static ViewModelProvider viewModelProvider = null;
    protected String NAME = this.getClass().getName();
    protected T viewModel;
    private RootFramelayoutBinding rootFragmentBinding;

    public BaseMVVMFragment() {

    }

    public void showDataView() {
        UIExecutor.postRunable(() -> {
            rootFragmentBinding.contentLayout.setVisibility(View.VISIBLE);
        });
    }

    public <T extends ViewDataBinding> T getViewDataBind(int resLayout) {
        return DataBindingUtil.inflate(LayoutInflater.from(getContext()), resLayout, null, false);
    }

    public abstract Class<T> getViewModelType();

    public void bindViewModel(Class<T> clazz) {
        //AppCompatActivity和Fragment这两个页面对象
        //父类ComponentActivity和Fragment实现了LifecycleOwner对象
        //父类FragmentActivity实现了ViewModelStoreOwner对象，又继承了ComponentActivity
        if (viewModelProvider == null) {
            viewModelProvider = new ViewModelProvider(this.getViewModelStore(),
                    new ViewModelProvider.NewInstanceFactory());
        }
        viewModel = viewModelProvider.get(clazz);
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
        if (viewModel == null) {
            bindViewModel(getViewModelType());
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
//    public abstract BaseViewModel createViewModel();

    public abstract int getResView();

    public View getMainView() {
        return null;
    }

//    public void  bindData(){
//
//    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initHeader();
        if (viewModel != null) {
            Bundle bundle = getArguments();
            if (bundle != null) {
                viewModel.dispatchEvent(Event.init(NAME, Event.TYPE_INIT, bundle));
            } else {
                viewModel.dispatchEvent(Event.init(NAME, Event.TYPE_INIT));
            }

            viewModel.data.observe(getViewLifecycleOwner(), new Observer() {
                @Override
                public void onChanged(Object o) {
                    if (o != null) {
                        if (o instanceof Status) {
                            Status status = (Status) o;
                            if (status.getStatusCode() == Status.STATUS_EMPTY) {
                                showErrorView(getString(R.string.no_data));
                            } else if (status.getStatusCode() == Status.STATUS_LOADING) {
                                showLoading();
                            } else if (status.getStatusCode() == Status.STATUS_ERROR) {
                                String errorStr = getString(R.string.sinxiao_net_error);
                                if (status.getData() instanceof String) {
                                    errorStr = (String) status.getData();
                                }
                                showErrorView(errorStr);
                            } else if (status.getStatusCode() == Status.STATUS_NORMAL) {
                                dissLoading();
                            }
                        }
                    }
                }
            });
        }

//        if (viewModel != null) {
//            viewModel.fetchData();
//        }
    }

    /**
     * 一般是绑定，控件的事件
     */
    public abstract void initView(View view);

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
        getActivity().startActivity(intent);
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
                    if (viewModel != null) {
                        rootFragmentBinding.layoutError.setVisibility(View.GONE);
                        viewModel.fetchData();
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
    }

    public void bindData(RecyclerView recyclerView, List<BaseViewHolderItem> datas) {
        super.bindData(recyclerView, datas);
    }

    /**
     * @param recyclerView
     * @param datas
     * @param columnSize
     */
    public void bindGridData(RecyclerView recyclerView, List<BaseViewHolderItem> datas, int columnSize) {
        super.bindGridData(recyclerView, datas, columnSize);
    }

}
