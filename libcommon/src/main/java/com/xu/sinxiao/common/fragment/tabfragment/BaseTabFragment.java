package com.xu.sinxiao.common.fragment.tabfragment;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.xu.sinxiao.common.R;
import com.xu.sinxiao.common.databinding.LayoutPageBaseTabViewpagerBinding;
import com.xu.sinxiao.common.fragment.BaseFragment;
import com.xu.sinxiao.common.mvp.IPresent;
import com.xu.sinxiao.common.mvp.fragment.BaseMVPFragment;
import com.xu.sinxiao.common.view.ScrollAbleViewPager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class BaseTabFragment extends BaseMVPFragment implements CustomTabLayout.OnTabChangedListener {

    public static final int DF_SCREEN_PAGE_LIMIT = 5;
    public int mScreenPageLimit = 5;
    public CustomTabLayout mTabLayout;
    public View mTabLine;
    public ScrollAbleViewPager mViewPager;
    public int mInitSelIndex = 0;
    protected Bundle mExtras;
    private LayoutPageBaseTabViewpagerBinding databind;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initExtraData();
    }

    @NonNull
    public abstract List<TabItemData> getTabItemData();

    @Override
    public View getMainView() {
        databind = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.layout_page_base_tab_viewpager, null, false);
        return databind.getRoot();
    }

    @Override
    public IPresent createPresent() {
        return null;
    }

    @Override
    public int getResView() {
        return 0;
    }

    public void initExtraData() {
        this.mExtras = this.getArguments();
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }

    }

    @Override
    public void initView(View view) {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTabView(view);
    }

    public void initTabView(View view) {
        this.mTabLayout = databind.tabLayout;
        mTabLine = databind.tabLine;
        mViewPager = databind.tabViewpager;//(ScrollAbleViewPager)this.findSubViewById(id.tab_viewpager);
        mViewPager.setOffscreenPageLimit(this.mScreenPageLimit);
        mViewPager.setOverScrollMode(2);
        initTab();
    }

    public void onTabChanged(CustomTabItemLayout customTabItemLayout) {
        switch (customTabItemLayout.getIndex()) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
//                updateMyInfoData(customTabItemLayout);
                break;
            default:
                break;
        }
    }

    public void initTab() {
        List<TabItemData> list = this.getTabItemData();
        if (0 == list.size()) {
            throw new RuntimeException("BaseTabFragment getTabItemData is empty");
        } else {
            int N = list.size();
            List<BaseFragment> listFragment = new ArrayList(N);
            List<CustomTabItemLayout> tabs = new ArrayList(N);
            Iterator var5 = list.iterator();

            while (var5.hasNext()) {
                TabItemData data = (TabItemData) var5.next();
                if (data.invalidData()) {
                    throw new RuntimeException("BaseTabFragment getTabItemData is invalid");
                }

                listFragment.add(newInstance(data.mFragmentName, this.mExtras));
                tabs.add(CustomTabItemLayout.build(this.getContext(), data.mIconResIds, data.mText));
            }
            this.mScreenPageLimit = N;
            this.mViewPager.setOffscreenPageLimit(this.mScreenPageLimit);

            this.mViewPager.setAdapter(new SimpleTabAdapter(this.getChildFragmentManager(), listFragment));
            this.mTabLayout.setOnTabChangedListener(this);
            this.mTabLayout.setViewPager(this.mViewPager);
            this.mTabLayout.setTabs(tabs);
            this.mTabLayout.setCurrentTab(this.mInitSelIndex);
            this.mTabLayout.setColor(this.getTabStateColorList());
            this.mTabLayout.setUp();
            this.onTabChanged(this.mTabLayout.getCurrentTab());
        }
    }

    public ColorStateList getTabStateColorList() {
        return getResources().getColorStateList(R.color.color_nor_666666_sel_themecolor);
    }

    public void setOffscreenPageLimit(int limit) {
        this.mScreenPageLimit = limit;
        if (null != this.mViewPager) {
            this.mViewPager.setOffscreenPageLimit(this.mScreenPageLimit);
        }

    }

    public void setInitSelIndex(int index) {
        this.mInitSelIndex = index;
    }

    public void setTabBackground(@DrawableRes int background) {
        if (null != this.mTabLayout) {
            this.mTabLayout.setBackgroundResource(background);
        }

    }

    public void setTabBackgroundColor(int color) {
        if (null != this.mTabLayout) {
            this.mTabLayout.setBackgroundColor(color);
        }

    }

    public void setTabLineColor(@ColorInt int color) {
        if (null != this.mTabLine) {
            this.mTabLine.setBackgroundColor(color);
        }

    }

    @Override
    public void bindData() {

    }

    public void setPagerScrollable(boolean scrollable) {
        if (this.mViewPager != null) {
            this.mViewPager.setScrollable(scrollable);
        }

    }

    public void setCurrentTab(int index) {
        if (null != this.mTabLayout) {
            this.mTabLayout.setCurrentTab(index);
        }

    }

    public void setCurrentTab(String tag) {
        if (null != this.mTabLayout) {
            this.mTabLayout.setCurrentTab(tag);
        }

    }

    public void setNewFlag(int index, int count) {
        if (null != this.mTabLayout) {
            this.mTabLayout.setNewFlag(index, count);
        }

    }

    public class SimpleTabAdapter extends FragmentPagerAdapter {
        public List<? extends Fragment> fragments;

        public SimpleTabAdapter(@NonNull FragmentManager fm, @NonNull List<? extends Fragment> fragments) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.fragments = fragments;
        }

        public Fragment getItem(int position) {
            return (Fragment) this.fragments.get(position);
        }

        public int getCount() {
            return this.fragments.size();
        }

        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
    }

    public static final class TabItemData {
        String mFragmentName;
        int[] mIconResIds;
        String mText;

        public TabItemData(@NonNull String fragment, @DrawableRes int[] resIds, String text) {
            this.mFragmentName = fragment;
            this.mIconResIds = resIds;
            this.mText = text;
        }

        public TabItemData(@NonNull Class<? extends Fragment> fragment, @DrawableRes int[] resIds, String text) {
            this(fragment.getName(), resIds, text);
        }

        boolean invalidData() {
            return TextUtils.isEmpty(this.mFragmentName);
        }
    }

}
