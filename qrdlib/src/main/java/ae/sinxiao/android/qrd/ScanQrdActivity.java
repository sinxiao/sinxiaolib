package ae.sinxiao.android.qrd;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xu.sinxiao.rxbus.RxBus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ae.sinxiao.android.qrd.model.QrdScanResult;
import ae.sinxiao.android.selectimage.ImageSelectorActivity;
import ae.sinxiao.android.qrd.core.BGAQRCodeUtil;
import ae.sinxiao.android.qrd.core.BaseEvent;
import ae.sinxiao.android.qrd.core.QRCodeView;
import ae.sinxiao.android.qrd.core.ScanEvent;
import ae.sinxiao.android.qrd.zbar.ZBarView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


@Keep

public class ScanQrdActivity extends AppCompatActivity implements QRCodeView.Delegate, EasyPermissions.PermissionCallbacks {
    public static final String KEY_Hide_Select_Image = "hideSelectImage";
    private final static int REQUEST_CODE_GALLEY = 0x02;
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private static ScanListener scanListener;
    private TextView txtLigthInfor;

    public static final int SCAN_DEFAULT_EVENT_ID = -1;

    private int currentEvent;

    public static interface ScanListener {
        void onScanSucess(QrdScanResult scanResult);

        void onScanFail(String msg);

        void onScanCancled();
    }

    public static void startScanQrdActivity(Context context, ScanListener listener) {
        Intent intent = new Intent(context, ScanQrdActivity.class);
        ScanQrdActivity.scanListener = listener;
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(BaseEvent.EVENT_ID, SCAN_DEFAULT_EVENT_ID);
        context.startActivity(intent);
    }

    /**
     * 可以设置参数，设置 hideSelectImage : 1 ,
     *
     * @param context
     * @param listener
     * @param extas
     */
    public static void startScanQrdActivity(Context context, ScanListener listener, HashMap<String, String> extas) {
        Intent intent = new Intent(context, ScanQrdActivity.class);
        ScanQrdActivity.scanListener = listener;
        if (extas != null && extas.size() > 0) {
            Iterator<String> iterator = extas.keySet().iterator();
            do {
                String v = iterator.next();
                String value = extas.get(v);
                intent.putExtra(v, value);
            } while (iterator.hasNext());
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(BaseEvent.EVENT_ID, SCAN_DEFAULT_EVENT_ID);
        context.startActivity(intent);
    }

    private static final String TAG = ScanQrdActivity.class.getSimpleName();
//    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;

    private ZBarView mZBarView;
    private boolean on = false;
    private ImageView igvLight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        StatusBarUtil.setStatusBarDarkTheme(this, true);
//    StatusBarUtil.setStatusBarColor(
//        this, ContextCompat.getColor(this, R.color.transparent)
//    );

        if (getIntent() != null && getIntent().hasExtra(BaseEvent.EVENT_ID)) {
            currentEvent = getIntent().getIntExtra(BaseEvent.EVENT_ID, SCAN_DEFAULT_EVENT_ID);
        }

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scan_qrd);
        mZBarView = findViewById(R.id.zbarview);
        txtLigthInfor = findViewById(R.id.txtLigthInfor);
        mZBarView.setDelegate(this);
        ImageView igvClose = findViewById(R.id.igvClose);
        igvClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        igvLight = findViewById(R.id.igvLight);
        igvLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on = !on;
                if (on) {
                    mZBarView.openFlashlight();
                    igvLight.setImageResource(R.mipmap.light_on);
                    txtLigthInfor.setText(R.string.tap_off_light);
                } else {
                    mZBarView.closeFlashlight();
                    igvLight.setImageResource(R.mipmap.light_off);
                    txtLigthInfor.setText(R.string.tap_on_light);
                }
            }
        });

        findViewById(R.id.layoutSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // 透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        RelativeLayout llRoot = findViewById(R.id.ll_root);
        View statusBarView = new View(this);
        statusBarView.setBackgroundColor(Color.TRANSPARENT);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this));
        // 在根布局中添加一个状态栏高度的View
        llRoot.addView(statusBarView, 0, lp);
        String v = getIntent().getStringExtra("hideSelectImage");
        if (v != null && v.equals("1")) {
            findViewById(R.id.layoutSelect).setVisibility(View.GONE);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    private void openGallery() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
        )
        ) {
            captureImageByPhotos();
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    getString(
                            R.string.pxr_sdk_permission
                    ),
                    Constants.PermissionCode.PERMISSION_WEB_PHOTO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            );
        }
    }

    /**
     * 打开相册
     */
    private void captureImageByPhotos() {
        Intent intent = new Intent(this, ImageSelectorActivity.class);
        startActivityForResult(intent, REQUEST_CODE_GALLEY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_GALLEY == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    return;
                }
                String imgPath = data.getStringExtra("image_data_single");
                String result = BGAQRCodeUtil.scanningImage(imgPath);

                if (result != null) {
//          Log.e("xxxx", " ====== " + result);
//                    Toast.makeText(this, "scan the info：" + result.getText(), Toast.LENGTH_SHORT).show();
                    onScanQRCodeSuccess(result);
                } else {
                    Toast.makeText(this, getText(R.string.scan_nothing), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQRCodePermissions();
        try {
            if (hasCamersPermissions()) {
                mZBarView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        mZBarView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
        mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别

    }

    @Override
    protected void onStop() {
        mZBarView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (scanListener != null) {
            scanListener.onScanCancled();
        }
    }

    @Override
    protected void onDestroy() {
        mZBarView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
        scanListener = null;
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    private boolean scanFirst = false;

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "onScanQRCodeSuccess result:" + result);
        if (!scanFirst) {
            scanFirst = true;
            setTitle("Scan Infor：" + result);
            vibrate();
//            mZBarView.startSpot(); // 开始识别
            QrdScanResult payByScanResult = new QrdScanResult(result);
            String packageName = getPackageName();
            payByScanResult.setPachageName(packageName);
            if (packageName.equals("ae.payby.android.saladin")) {
                payByScanResult.setPayby(true);
                payByScanResult.setTotok(false);
            } else if (packageName.contains("totok")) {
                payByScanResult.setTotok(true);
                payByScanResult.setPayby(false);
            } else {
                payByScanResult.setTotok(false);
                payByScanResult.setPayby(false);
            }
            if (currentEvent == SCAN_DEFAULT_EVENT_ID) {
                if (scanListener != null) {
                    scanListener.onScanSucess(payByScanResult);
                }
            } else if (currentEvent == ScanEvent.SCAN_RESULT_WEB_HANDLE) {
                if (!TextUtils.isEmpty(payByScanResult.getValue())) {
                    ScanEvent scanEvent = new ScanEvent(ScanEvent.SCAN_RESULT_WEB_HANDLE);
                    scanEvent.putExtra("scan_result", payByScanResult.getValue());
                    RxBus.getInstance().post(scanEvent);
                }
            } else if (currentEvent == 0x1001) {
                if (!TextUtils.isEmpty(payByScanResult.getValue())) {
                    Intent intent = new Intent();
                    intent.setAction("com.pb.bike.action");
                    intent.putExtra("scan_result", payByScanResult.getValue());
                    sendBroadcast(intent);
                }
            }

        }
        finish();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
//        String tipText = mZBarView.getScanBoxView().getTipText();
//        String ambientBrightnessTip = "\n" + getString(R.string.too_dim_turn_light);//too_dim_turn_light
        if (isDark) {
//            if (!tipText.contains(ambientBrightnessTip)) {
//                mZBarView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
//            }

        } else {
//            if (tipText.contains(ambientBrightnessTip)) {
//                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
//                mZBarView.getScanBoxView().setTipText(tipText);
//            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == Constants.PermissionCode.PERMISSION_WEB_PHOTO) {
            openGallery();
        } else {
            mZBarView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZBarView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
            mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (scanListener != null) {
            scanListener.onScanFail("No permission");
        }
    }

    private boolean hasCamersPermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        return EasyPermissions.hasPermissions(this, perms);
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "Scanning the QR code requires permission to open the camera.", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
        if (scanListener != null) {
            scanListener.onScanFail("Open Camera failed");
        }
    }
}
