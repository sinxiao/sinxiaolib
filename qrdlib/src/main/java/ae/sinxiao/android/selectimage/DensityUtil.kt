package ae.sinxiao.android.selectimage

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.WindowManager


/**
 * Created by silence on 2020-02-14
 * Describe:
 */
object DensityUtil {

    fun dp2px(context: Context, dp: Float): Int {
        return (0.5f + getDisplayMetrics(context).density * dp).toInt()
    }

    fun px2dp(context: Context, px: Float): Int {
        return (px / getDisplayMetrics(context).density + 0.5f).toInt()
    }

    fun dp2px(dp: Float): Int {
        return (0.5f + Resources.getSystem().displayMetrics.density * dp).toInt()
    }

    fun px2dp(px: Float): Int {
        return (px / Resources.getSystem().displayMetrics.density + 0.5f).toInt()
    }

    fun getScreenWidth(context: Context): Int {
        return getDisplayMetrics(context).widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        return getDisplayMetrics(context).heightPixels
    }

    private fun getDisplayMetrics(context: Context): DisplayMetrics {
        return context.resources.displayMetrics
    }

    fun getRealScreenRelatedInformation(context: Context): ScreenBean {
        val screenBean = ScreenBean()
        val windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        if (windowManager != null) {
            val outMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getRealMetrics(outMetrics)
            val widthPixels = outMetrics.widthPixels
            val heightPixels = outMetrics.heightPixels
            val densityDpi = outMetrics.densityDpi
            val density = outMetrics.density
            val scaledDensity = outMetrics.scaledDensity
            //可用显示大小的绝对宽度（以像素为单位）。
            //可用显示大小的绝对高度（以像素为单位）。
            //屏幕密度表示为每英寸点数。
            //显示器的逻辑密度。
            //显示屏上显示的字体缩放系数。
            screenBean.widthPixels = widthPixels
            screenBean.heightPixels = heightPixels
            screenBean.density = density
            screenBean.densityDpi = densityDpi
//            Log.d(
//                "display",
//                "widthPixels = " + widthPixels + ",heightPixels = " + heightPixels + "\n" +
//                        ",densityDpi = " + densityDpi + "\n" +
//                        ",density = " + density + ",scaledDensity = " + scaledDensity
//            )
        }
        return screenBean
    }

}