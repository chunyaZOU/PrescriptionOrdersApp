package com.sy.prescription;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.Result;
import com.sy.prescription.config.IMApplication;
import com.sy.prescription.util.DensityUtils;
import com.sy.prescription.zxing.camera.CameraManager;
import com.sy.prescription.zxing.decode.DecodeThread;
import com.sy.prescription.zxing.utils.BeepManager;
import com.sy.prescription.zxing.utils.CaptureActivityHandler;
import com.sy.prescription.zxing.utils.InactivityTimer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.PublicKey;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback {

    private static final String TAG = CaptureActivity.class.getSimpleName();
    public static final int SCAN_FLAG = 0;
    @BindView(R.id.capture_preview)
    SurfaceView capturePreview;
    @BindView(R.id.capture_crop_view)
    RelativeLayout captureCropView;
    @BindView(R.id.imgScan)
    ImageView imgScan;
    @BindView(R.id.imgInitCode)
    ImageView imgInitCode;
    @BindView(R.id.imgCode)
    ImageView imgCode;
    @BindView(R.id.llCode)
    LinearLayout llCode;
    @BindView(R.id.capture_scan_line)
    ImageView imgScanLine;
    @BindView(R.id.capture_container)
    RelativeLayout scanContainer;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.tvScanAmount)
    TextView tvScanAmount;
    @BindView(R.id.tvCodeAmount)
    TextView tvCodeAmount;
    @BindView(R.id.tvPayWay)
    TextView tvPayWay;
    @BindView(R.id.tvLeft)
    TextView tvLeft;
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;

    private Rect mCropRect = null;
    private boolean isHasSurface = false;
    private int TicketType;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    private String scanResult;
    private Dialog dialog, backDialog;
    private String title;
    private boolean isScaned, isFinishScan;//被扫瞄

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);
        ButterKnife.bind(this);
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        getIntentData();
        initNav(title);
        initView();
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
                .RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        imgScanLine.startAnimation(animation);
        capturePreview.getHolder().addCallback(this);
    }

    private void initView() {
        imgInitCode.setOnClickListener(clickListener);
        imgScan.setOnClickListener(clickListener);
        tvLeft.setOnClickListener(clickListener);
        clickListener.onClick(imgScan);
    }

    private void getIntentData() {
        title = getIntent().getStringExtra("title");
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imgInitCode:
                    break;
                case R.id.imgScan:
                    tvState.setText("请保持适当距离进行扫描");
                    isScaned = false;
                    setScanView();
                    break;
            }
        }
    };


    private void setScanView() {
        llCode.setVisibility(View.GONE);
        capturePreview.setVisibility(View.VISIBLE);
        scanContainer.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameraManager == null) {
            cameraManager = new CameraManager(IMApplication.getAppContext());
        }
        handler = null;
        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore surfaceCreated() won't be called, so init the camera here.
            initCamera(capturePreview.getHolder());
        }
        inactivityTimer.onResume();
        if (!isScaned) {
            // initDialog();
        }
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }

        if (backDialog != null) {
            backDialog.dismiss();
            backDialog = null;
        }
        inactivityTimer.onPause();
        cameraManager.closeDriver();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (!isHasSurface) {
            capturePreview.getHolder().removeCallback(this);
        }
        beepManager.close();
        //capturePresenter.removeMsgListener();
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        cameraManager.closeDriver();
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
        scanResult = rawResult.getText();
        Intent intent = new Intent();
        intent.putExtra("num", scanResult);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }
            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("摄像头错误，请检查！");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;
        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        captureCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight() - DensityUtils.dp2Px(this, 48);

        int cropWidth = captureCropView.getWidth();
        int cropHeight = captureCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public static void startAct(Context context, String title) {
        Intent intent = new Intent(context, CaptureActivity.class);
        intent.putExtra("title", title);
        ((OrderDetailActivity) context).startActivityForResult(intent, SCAN_FLAG);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.out_from_right);
    }
}