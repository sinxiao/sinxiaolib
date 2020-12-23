package com.xu.sinxiao.common.fragment.tabfragment;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.xu.sinxiao.common.R;
import com.xu.sinxiao.common.databinding.LayoutTabViewpagerBinding;
import com.xu.sinxiao.common.mvp.IPresent;
import com.xu.sinxiao.common.mvp.fragment.BaseMVPFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTabNavigationFragment extends BaseMVPFragment {
    private LayoutTabViewpagerBinding layoutTabViewpagerBinding;

    private FragmentPagerAdapter fragmentPagerAdapter;

    private List<Fragment> fragments = new ArrayList<>();

    @NonNull
    public abstract List<TabItemData> getTabItemData();

    @Override
    public View getMainView() {
        layoutTabViewpagerBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_tab_viewpager, null, false);
        return layoutTabViewpagerBinding.getRoot();
    }

    @Override
    public IPresent createPresent() {
        return null;
    }

    @Override
    public int getResView() {
        return 0;
    }

    @Override
    public void initView(View view) {
        layoutTabViewpagerBinding.tabBase.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                tab.getPosition();
                layoutTabViewpagerBinding.tabViewpager.setCurrentItem(tab.getPosition(), false);
                ((TabItemView) tab.getCustomView()).setSelected();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TabItemView) tab.getCustomView()).setUnSelected();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        List<TabItemData> tabItemDatas = getTabItemData();
        if (tabItemDatas != null && tabItemDatas.size() > 0) {
            int size = tabItemDatas.size();
            for (int i = 0; i < size; i++) {
                TabItemData tabItemData = tabItemDatas.get(i);
                fragments.add(tabItemData.fragment);
                tabItemData.idx = i;
                layoutTabViewpagerBinding.tabBase.addTab(new TabLayout.Tab().setCustomView(new TabItemView(getContext(), tabItemData)));
            }
        }
        fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };

        layoutTabViewpagerBinding.tabViewpager.setAdapter(fragmentPagerAdapter);
        layoutTabViewpagerBinding.tabViewpager.setScrollable(false);


    }

    @Override
    public void bindData() {

    }

    public static class TabItemData implements Serializable {
        public int idx;
        public String name;
        public int resId;
        public Fragment fragment;
        public int unRead;
        public boolean showPoint;
    }

}
