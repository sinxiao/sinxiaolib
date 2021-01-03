package com.sharpen.common.util;

import android.widget.TextView;

import java.util.List;

public class UITool {
    public static String viewText(TextView textView){
        if(textView == null || textView.getText()==null){
            return null;
        }
        return textView.getText().toString();
    }
    public static String viewTextTrim(TextView textView){
        if(textView == null || textView.getText()==null){
            return null;
        }
        return textView.getText().toString().trim();
    }
    public static void setViewText(TextView textView, String val){
        if(textView == null){
            return;
        }
        textView.setText(val);
    }

    /**
     * 批量设置textView的值
     * @param val
     * @param tvs
     */
    public static void setVtAll(String val, TextView ...tvs){
        for(TextView elem : tvs){
            if(elem != null){
                elem.setText(val);
            }
        }
    }

    /**
     * 批量设置textView的颜色
     * @param color
     * @param tvs
     */
    public static void setViewColor(int color, TextView ...tvs){
        for(TextView elem : tvs){
            if(elem != null){
                elem.setBackgroundResource(color);
            }
        }
    }

    /**
     * 批量设置textView的颜色
     * @param color
     * @param tvs
     */
    public static void setViewColorArr(int color, List<TextView> tvs){
        for(TextView elem : tvs){
            if(elem != null){
                elem.setBackgroundResource(color);
            }
        }
    }
}
