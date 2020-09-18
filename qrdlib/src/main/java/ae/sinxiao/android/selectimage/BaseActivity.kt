package ae.sinxiao.android.selectimage

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * Created by silence on 11/03/2020
 * Describe:
 */
@Keep
abstract class BaseActivity : RequestPermissionsActivity(), CoroutineScope by MainScope(), View.OnClickListener {

    lateinit var TAG: String

    abstract val contentLayoutId: Int

    abstract fun initViewModel()

    abstract fun initObserve()

    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun initData()

    lateinit var context: Context;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = this::class.java.simpleName
        context = this
//    LogUtils.d(TAG, "onCreate")
        setContentView(contentLayoutId)
        if (getWhiteTheme()) {
            whiteStatusBar()
        } else {
            greenStatusBar()
        }
        initViewModel()
        initObserve()
        initView(savedInstanceState)
        initData()
    }

    open fun getWhiteTheme(): Boolean {
        return true
    }

    /**
     * 默认是白底黑字
     */
    private fun whiteStatusBar() {
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //判断是否能修改状态栏文字颜色
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }
    }

    private fun greenStatusBar() {
        //判断是否能修改状态栏文字颜色
        if (!StatusBarUtil.setStatusBarDarkTheme(
                        this, false
                )
        ) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000)
        }
        StatusBarUtil.setStatusBarColor(
                this, Color.parseColor("#009553")
        )
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


    override fun onStart() {
        super.onStart()
//    LogUtils.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
//    UpdateManager.mContext = this
//    LogUtils.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
//    LogUtils.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
//    LogUtils.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
//    LogUtils.d(TAG, "onDestroy")
        cancel()
    }

    override fun attachBaseContext(newBase: Context?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            super.attachBaseContext(newBase);
        } else {
            //zh：中文
//      super.attachBaseContext(SaladinApplication.getInstance().initLanguage(newBase));
            super.attachBaseContext(baseContext);
        }
    }

    override fun onClick(v: View?) {

    }

//    fun <T : ViewModel> getViewModel(modelClass: Class<T>): T {
//        return ViewModelProvider(this).get(modelClass)
//    }

//    private var loadingDialog: LoadingDialog? = null
//
//    fun showLoading() {
//        if (loadingDialog == null) {
//            loadingDialog = LoadingDialog(this)
//        }
//        loadingDialog?.showDialog()
//    }
//
//    fun dismissLoading() {
//        if (loadingDialog?.dialog?.isShowing == true) {
//            loadingDialog?.dismissDialog()
//        }
//    }
}