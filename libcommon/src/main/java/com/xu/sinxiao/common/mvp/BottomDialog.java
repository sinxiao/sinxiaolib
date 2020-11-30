package com.xu.sinxiao.common.mvp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.xu.sinxiao.common.R;

import java.util.Objects;

public class BottomDialog extends DialogFragment {

    private View.OnClickListener clickListener;
    private int layoutRes = -1;

    public BottomDialog(View.OnClickListener clickListener) {
        setClickListener(clickListener);
    }

    public BottomDialog(View.OnClickListener clickListener, int layoutRes) {
        setClickListener(clickListener);
        this.layoutRes = layoutRes;
    }

    private void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public static BottomDialog newInstance(View.OnClickListener clickListener) {
        Bundle args = new Bundle();
        BottomDialog fragment = new BottomDialog(clickListener);
        fragment.setArguments(args);
        return fragment;
    }

    public static BottomDialog newInstance(View.OnClickListener clickListener, int layoutRes) {
        Bundle args = new Bundle();
        BottomDialog fragment = new BottomDialog(clickListener, layoutRes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = null;
        if (layoutRes != -1) {
            view = inflater.inflate(layoutRes, container, false);
        } else {
            view = inflater.inflate(R.layout.dialog_bottom, container, false);
        }

        if (clickListener != null) {
            bindEvent(view);
//      view.findViewById(R.id.txtChina).setOnClickListener(clickListener);
//      view.findViewById(R.id.txtUAE).setOnClickListener(clickListener);
//      view.findViewById(R.id.txtCancel).setOnClickListener(clickListener);
        }
        slideToUp(view);
        return view;
    }

    private void bindEvent(View view) {
        Objects.requireNonNull(view, "view should not be null");
        Objects.requireNonNull(clickListener, "clickListener should not be null");
        if (view instanceof ViewGroup) {
            ViewGroup prarent = (ViewGroup) view;
            int count = prarent.getChildCount();
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    View child = prarent.getChildAt(i);
                    bindEvent(child);
                }
            }
        } else {
            view.setOnClickListener(clickListener);
        }
    }

    public static void slideToUp(View view) {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        view.startAnimation(slide);
        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}
