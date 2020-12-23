package com.xu.sinxiao.common.fragment.tabfragment;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.databinding.DataBindingUtil;

import com.xu.sinxiao.common.R;
import com.xu.sinxiao.common.databinding.TabItemBinding;

public class TabItemView extends FrameLayout {

    private BaseTabNavigationFragment.TabItemData tabItemData;
    private TabItemBinding tabItemBinding;
    private int defaultColor;
    private int selectedColor;

    public TabItemView(Context context, BaseTabNavigationFragment.TabItemData tabItemData) {
        super(context);
        this.tabItemData = tabItemData;
        tabItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.tab_item, this, true);
        tabItemBinding.igvRes.setImageResource(tabItemData.resId);
        if (TextUtils.isEmpty(tabItemData.name)) {
            tabItemBinding.txtName.setVisibility(View.GONE);
        } else {
            tabItemBinding.txtName.setText(tabItemData.name);
            tabItemBinding.txtName.setVisibility(View.VISIBLE);
        }

    }

    public void setSelectColor(int color) {
        this.selectedColor = selectedColor;
    }

    public void setSelected() {
        if (selectedColor != 0) {
            tabItemBinding.igvRes.setColorFilter(selectedColor);
            tabItemBinding.txtName.setTextColor(selectedColor);
        } else {
            tabItemBinding.igvRes.setSelected(true);
        }
    }

    public void setUnSelected() {
        if (defaultColor != 0) {
            tabItemBinding.igvRes.setColorFilter(defaultColor);
            tabItemBinding.txtName.setTextColor(defaultColor);
        } else {
            tabItemBinding.igvRes.setSelected(false);
        }

    }

    public void setDefaultColor(int defaultColor) {
        this.defaultColor = defaultColor;
    }

    public BaseTabNavigationFragment.TabItemData getTabItemData() {
        return tabItemData;
    }
}
