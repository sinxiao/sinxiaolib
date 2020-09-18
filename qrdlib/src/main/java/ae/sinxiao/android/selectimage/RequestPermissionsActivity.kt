package ae.sinxiao.android.selectimage

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

/**
 * @ProjectName: PayBy-App
 * @ClassName: RequestPermissionsActivity
 * @Author: liuzhicheng
 * @CreateDate: 2020/4/2
 * @Description:
 * @Version: 1.0
 */
abstract class RequestPermissionsActivity : AppCompatActivity() {
    companion object {

        const val DEFAULT_REQUEST_PERMISSION = 30000

        var requestSuccess: (() -> Unit)? = null
        var requestRefuse: (() -> Intent)? = null
        var requestPermissions: Array<String>? = null


        fun runWithPermissions(
            activity: Activity,
            permissions: Array<String>,
            refuse: (() -> Intent)? = null,
            run: () -> Unit
        ) {
            if (EasyPermissions.hasPermissions(
                    activity,
                    *permissions
                )
            ) {
                run()
            } else { //申请权限
                requestSuccess = run
                requestRefuse = refuse
                requestPermissions = permissions
                ActivityCompat.requestPermissions(
                    activity,
                    permissions,
                    DEFAULT_REQUEST_PERMISSION
                )
            }
        }
    }

    private var inPermission = false

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.somePermissionPermanentlyDenied(this, permissions.asList())
        inPermission = true

        when (requestCode) {
            DEFAULT_REQUEST_PERMISSION -> {
                if (EasyPermissions.hasPermissions(this, *permissions)) {
                    inPermission = false
                    requestSuccess?.let {
                        it.invoke()
                    }
                    requestSuccess = null
                } else if (EasyPermissions.somePermissionPermanentlyDenied(
                        this,
                        permissions.asList()
                    )
                ) {
                    //用户点击不再显示
                    if (requestRefuse == null) {
                        AppSettingsDialog.Builder(this)
                            .build()
                            .show()
                    } else {
                        requestRefuse?.let {
                            startActivityForResult(
                                it.invoke(),
                                DEFAULT_REQUEST_PERMISSION
                            )
                        }
                        requestRefuse = null
                    }

                } else {
                    //用户拒绝
                    ActivityCompat.requestPermissions(
                        this,
                        permissions,
                        DEFAULT_REQUEST_PERMISSION
                    )


                }

            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            //开启设置页面
            DEFAULT_REQUEST_PERMISSION -> {
                inPermission = false
                requestSuccess?.let {
                    it.invoke()
                }
                requestSuccess = null
            }
        }
        when (resultCode) {
            //用户点击不再显示 之后返回
            RESULT_CANCELED -> {
                if (inPermission) {
                    requestPermissions?.let {
                        requestSuccess?.let {
                            runWithPermissions(
                                this,
                                requestPermissions!!,
                                requestRefuse,
                                requestSuccess!!
                            )
                        }
                    }

                }
            }
        }
    }
}


