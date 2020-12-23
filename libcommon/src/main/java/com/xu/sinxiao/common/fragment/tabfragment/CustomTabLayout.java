package com.xu.sinxiao.common.fragment.tabfragment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomTabLayout extends LinearLayout implements View.OnClickListener {
    private ViewPager mViewPager;
    private PagerAdapter mAdapter;
    private List<CustomTabItemLayout> mTabLayouts;
    private OnTabChangedListener mTabListener;
    private TabLayoutOnPageChangeListener mPageChangeListener;
    private ColorStateList mColorStateList;
    private int mCurrentTabIndex;
    private CustomTabItemLayout mCurrentTab;
    private boolean mSmoothSwitch;

    public CustomTabLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOrientation(LinearLayout.HORIZONTAL);
    }

    @Nullable
    private Fragment getItemContent(int pos) {
        if (this.mAdapter == null && this.mViewPager != null) {
            this.mAdapter = this.mViewPager.getAdapter();
        }

        if (this.mAdapter != null && this.mAdapter.getCount() > this.mCurrentTabIndex) {
            if (this.mAdapter instanceof FragmentStatePagerAdapter) {
                return ((FragmentStatePagerAdapter) this.mAdapter).getItem(pos);
            }

            if (this.mAdapter instanceof FragmentPagerAdapter) {
                return ((FragmentPagerAdapter) this.mAdapter).getItem(pos);
            }
        }

        return null;
    }

    public void onClick(View v) {
        CustomTabItemLayout tab = (CustomTabItemLayout) v;
        if (tab != this.mCurrentTab) {
            int index = tab.getIndex();
            this.setCurrentTabSmooth(index);
            if (this.mTabListener != null) {
                this.mTabListener.onTabChanged(tab);
            }
        }

    }

    public void setColor(int defaultColor, int selectedColor) {
        this.mColorStateList = this.createColorStateList(defaultColor, selectedColor);
    }

    public void setColor(ColorStateList stateList) {
        if (stateList != null) {
            this.mColorStateList = stateList;
        }

    }

    public void setViewPager(@NonNull ViewPager viewPager) {
        if (this.mViewPager != null) {
            this.mViewPager.removeOnPageChangeListener(this.mPageChangeListener);
        }

        this.mViewPager = viewPager;
    }

    public void setTabs(@NonNull List<CustomTabItemLayout> tabs) {
        this.mTabLayouts = new ArrayList();
        this.mTabLayouts.addAll(tabs);
    }

    public void setOnTabChangedListener(OnTabChangedListener listener) {
        this.mTabListener = listener;
    }

    public void setUp() {
        if (this.mTabLayouts != null) {
            this.removeAllViews();
            if (this.mTabLayouts.size() > 0) {
                this.setWeightSum((float) this.mTabLayouts.size());
                int index = 0;

                for (Iterator var2 = this.mTabLayouts.iterator(); var2.hasNext(); ++index) {
                    CustomTabItemLayout tabLayout = (CustomTabItemLayout) var2.next();
                    tabLayout.setIndex(index);
                    tabLayout.setOnClickListener(this);
                    this.addView(tabLayout, new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0F));
                    if (this.mColorStateList != null) {
                        tabLayout.setColorStateList(this.mColorStateList);
                    }
                }

                this.switchTab(this.mCurrentTabIndex);
            }
        }

        if (this.mViewPager != null) {
            this.mPageChangeListener = new TabLayoutOnPageChangeListener();
            this.mViewPager.addOnPageChangeListener(this.mPageChangeListener);
            this.mAdapter = this.mViewPager.getAdapter();
            this.switchPage(this.mCurrentTabIndex, false);
        }

    }

    public void setCurrentTabSmooth(int index) {
        this.switchPage(index, this.mSmoothSwitch);
        this.switchTab(index);
    }

    public void setCurrentTab(int index) {
        this.switchPage(index, false);
        this.switchTab(index);
    }

    public void setCurrentTab(@NonNull String tabText) {
        int index = this.findIndexByTabText(tabText);
        if (index != -1) {
            this.setCurrentTab(index);
        }

    }

    public void setNewFlag(int index, int count) {
        CustomTabItemLayout itemLayout = this.findByTabIndex(index);
        if (null != itemLayout) {
            if (0 < count) {
                if (count > 99) {
                    count = 99;
                }

                itemLayout.setNewFlag(String.valueOf(count));
                itemLayout.setNewFlagVisible(true);
            } else {
                itemLayout.setNewFlag("");
                itemLayout.setNewFlagVisible(false);
            }
        }

    }

    @Nullable
    public CustomTabItemLayout findByTabIndex(int index) {
        return this.validIndex(index) ? (CustomTabItemLayout) this.mTabLayouts.get(index) : null;
    }

    @Nullable
    public CustomTabItemLayout findByTabText(String text) {
        int index = this.findIndexByTabText(text);
        return this.validIndex(index) ? (CustomTabItemLayout) this.mTabLayouts.get(index) : null;
    }

    public int findIndexByTabText(String text) {
        if (text != null && this.mTabLayouts != null) {
            for (int i = 0; i < this.mTabLayouts.size(); ++i) {
                if (text.equals(((CustomTabItemLayout) this.mTabLayouts.get(i)).getText())) {
                    return i;
                }
            }
        }

        return -1;
    }

    public void setSmoothSwitch(boolean smoothEnable) {
        this.mSmoothSwitch = smoothEnable;
    }

    public CustomTabItemLayout getCurrentTab() {
        return this.mCurrentTab;
    }

    private void switchTab(int index) {
        if (this.validIndex(index)) {
            CustomTabItemLayout itemLayout = (CustomTabItemLayout) this.mTabLayouts.get(index);
            if (this.mCurrentTab != itemLayout) {
                if (this.mCurrentTab != null) {
                    this.mCurrentTab.setSelected(false);
                }

                itemLayout.setSelected(true);
                this.mCurrentTab = itemLayout;
                this.mCurrentTabIndex = index;
            }
        }
    }

    private void switchPage(int index, boolean smoothScroll) {
        if (this.validIndex(index) && this.mViewPager != null && this.mAdapter != null) {
            this.mViewPager.setCurrentItem(index, smoothScroll);
        }
    }

    private boolean validIndex(int index) {
        return this.mTabLayouts != null && index >= 0 && index < this.mTabLayouts.size();
    }

    private ColorStateList createColorStateList(int defaultColor, int selectedColor) {
        int[][] states = new int[2][];
        int[] colors = new int[2];
        states[0] = SELECTED_STATE_SET;
        colors[0] = selectedColor;
        states[1] = EMPTY_STATE_SET;
        colors[1] = defaultColor;
        return new ColorStateList(states, colors);
    }

    private class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private boolean mNeedNotify;

        private TabLayoutOnPageChangeListener() {
        }

        public void onPageScrollStateChanged(int state) {
            if (!this.mNeedNotify) {
                this.mNeedNotify = state == 1;
            }

        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            Fragment curFragment = CustomTabLayout.this.getItemContent(CustomTabLayout.this.mCurrentTabIndex);
            if (curFragment != null && curFragment.isAdded()) {
                curFragment.onPause();
            }

            Fragment newFragment = CustomTabLayout.this.getItemContent(position);
            if (newFragment != null && newFragment.isAdded()) {
                newFragment.onResume();
            }

            if (this.mNeedNotify && CustomTabLayout.this.validIndex(position)) {
                CustomTabLayout.this.switchTab(position);
                if (CustomTabLayout.this.mTabListener != null) {
                    CustomTabLayout.this.mTabListener.onTabChanged((CustomTabItemLayout) CustomTabLayout.this.mTabLayouts.get(position));
                }
            }

            CustomTabLayout.this.mCurrentTabIndex = position;
            this.mNeedNotify = false;
        }
    }

    public interface OnTabChangedListener {
        void onTabChanged(CustomTabItemLayout var1);
    }
}