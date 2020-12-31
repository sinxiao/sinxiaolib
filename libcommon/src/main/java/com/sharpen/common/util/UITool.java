package com.sharpen.common.util;

import android.widget.TextView;

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
}
