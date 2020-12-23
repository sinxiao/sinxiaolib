package com.xu.sinxiao.common;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;

import com.xu.sinxiao.common.logger.Logger;

public class DrawableUtils {

    public static Drawable tintDrawable(@NonNull Drawable drawable, @NonNull ColorStateList colors) {
        try {
            int[][] states = (int[][]) ReflectUtils.getObjectByFieldName(colors, "mStateSpecs", int[][].class);
            if (states != null) {
                StateListDrawable stateListDrawable = new StateListDrawable();

                for (int i = 0; i < states.length; ++i) {
                    stateListDrawable.addState(states[i], drawable);
                }

                Drawable.ConstantState state = stateListDrawable.getConstantState();
                drawable = DrawableCompat.wrap((Drawable) (state == null ? stateListDrawable : state.newDrawable())).mutate();
                DrawableCompat.setTintList(drawable, colors);
            }
        } catch (Exception var5) {
            Logger.e(var5.getMessage());
        }

        return drawable;
    }

    public static GradientDrawable buildShapeStrokeOvalDrawable(int strokeWidth, @ColorInt int strokeColor, @ColorInt int solidColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setColor(solidColor);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        return gradientDrawable;
    }


    public static GradientDrawable buildShapeStrokeRectDrawable(int strokeWidth, @ColorInt int strokeColor, @ColorInt int solidColor, int corner) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius((float) corner);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        gradientDrawable.setColor(solidColor);
        return gradientDrawable;
    }

    public static Drawable buildShapeStrokeRectDrawable(int strokeWidth, @ColorInt int strokeColor, @ColorInt int solidColor, int xTopLeftCorner, int yTopLeftCorner, int xTopRightCorner, int yTopRightCorner, int xBottomRightCorner, int yBottomRightCorner, int xBottomLeftCorner, int yBottomLeftCorner) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadii(new float[]{(float) xTopLeftCorner, (float) yTopLeftCorner, (float) yTopRightCorner, (float) xTopRightCorner, (float) xBottomRightCorner, (float) yBottomRightCorner, (float) xBottomLeftCorner, (float) yBottomLeftCorner});
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        gradientDrawable.setColor(solidColor);
        return gradientDrawable;
    }

    public static ColorStateList buildColorStateList(@ColorInt int disableColor, @ColorInt int selectedColor, @ColorInt int normalColor) {
        int[] colors = new int[]{disableColor, selectedColor, normalColor};
        int[][] states = new int[][]{{-16842910}, {16842913}, new int[0]};
        return new ColorStateList(states, colors);
    }

    public static ColorStateList buildColorStateList(@ColorInt int disableColor, @ColorInt int selectedColor, @ColorInt int pressedColor, @ColorInt int normalColor) {
        int[] colors = new int[]{disableColor, selectedColor, pressedColor, normalColor};
        int[][] states = new int[][]{{-16842910}, {16842913}, {16842919}, new int[0]};
        return new ColorStateList(states, colors);
    }

    public static StateListDrawable buildStateListDrawable(Drawable disableDrawable, Drawable selectedDrawable, Drawable pressedDrawable, Drawable normalDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        if (null != disableDrawable) {
            stateListDrawable.addState(new int[]{-16842910}, disableDrawable);
        }

        if (null != selectedDrawable) {
            stateListDrawable.addState(new int[]{16842913, -16842919}, selectedDrawable);
        }

        if (null != pressedDrawable) {
            stateListDrawable.addState(new int[]{16842919}, pressedDrawable);
        }

        if (null != normalDrawable) {
            stateListDrawable.addState(new int[0], normalDrawable);
        }

        return stateListDrawable;
    }

    public static StateListDrawable buildFocuseStateListDrawable(Drawable disableDrawable, Drawable focusedDrawable, Drawable normalDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        if (null != disableDrawable) {
            stateListDrawable.addState(new int[]{-16842910}, disableDrawable);
        }

        if (null != focusedDrawable) {
            stateListDrawable.addState(new int[]{16842908}, focusedDrawable);
        }

        if (null != normalDrawable) {
            stateListDrawable.addState(new int[0], normalDrawable);
        }

        return stateListDrawable;
    }

    public static StateListDrawable buildColorStateListDrawable(@ColorInt int disableColor, @ColorInt int selectedColor, @ColorInt int pressedColor, @ColorInt int normalColor) {
        return buildStateListDrawable(disableColor != 0 ? new ColorDrawable(disableColor) : null, selectedColor != 0 ? new ColorDrawable(selectedColor) : null, pressedColor != 0 ? new ColorDrawable(pressedColor) : null, normalColor != 0 ? new ColorDrawable(normalColor) : null);
    }
}
