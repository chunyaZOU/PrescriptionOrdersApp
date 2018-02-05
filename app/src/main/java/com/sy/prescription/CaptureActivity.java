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
import com.sy.prescription.model.CouponInfo;
import com.sy.prescription.util.DensityUtils;
import com.sy.prescription.util.GsonUtil;
import com.sy.prescription.util.HttpRequest;
import com.sy.prescription.util.Urls;
import com.sy.prescription.zxing.camera.CameraManager;
import com.sy.prescription.zxing.decode.DecodeThread;
import com.sy.prescription.zxing.utils.BeepManager;
import com.sy.prescription.zxing.utils.CaptureActivityHandler;
import com.sy.prescription.zxing.utils.InactivityTimer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback {

    private static final String TAG = CaptureActivity.class.getSimpleName();
    public static final int PAY_FLAG = 0;//收款
    public static final int REFUND_FLAG = 1;//退款
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
    private String amount;
    private int type;
    private int tags;
    private boolean isScaned, isFinishScan;//被扫瞄
    //private CapturePresenter capturePresenter;
    private int which;
    private String orderSn = "";
    private String reppasswd = ""; //重试单号
    private int reCount = 0; //重试次数

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);
        ButterKnife.bind(this);
        //capturePresenter = new CapturePresenter(this);
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
        tags = getIntent().getIntExtra("tags",0);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvLeft:
                    finish();
                    break;
                case R.id.imgInitCode:
                    break;
                case R.id.imgScan:
                    if (tags == 1){
                        tvState.setText("请扫描会员卡号");
                    }else{
                        tvState.setText("请扫描消费者的门票号完成验证");
                    }
                    isScaned = false;
                   setScanView();
                    break;
            }
        }
    };


    private void CheckTicket(String ticket) {
        Map<String, String> params = new HashMap<>();
        params.put("a", "verify");
        params.put("password", ticket);
        reppasswd =ticket;
        reCount =0;
        HttpRequest.HttpPost(Urls.API_URL, Urls.API_VERSION_1, Urls.Method.CHECK_COUPON, Urls.MethodType.POST, params, httpCallback);
    }

    private void CheckTicketDo(String ticket) {
        Map<String, String> params = new HashMap<>();
        params.put("a", "use");
        params.put("password", ticket);
        HttpRequest.HttpPost(Urls.API_URL, Urls.API_VERSION_1, Urls.Method.CHECK_COUPON, Urls.MethodType.POST, params, httpDoCallback);
    }


    private void UserChange(String card_id) {
        Map<String, String> params = new HashMap<>();
        params.put("a", "UserChange");
        params.put("card_id", card_id);
        HttpRequest.HttpPost(Urls.API_URL, Urls.API_VERSION_1, Urls.Method.CHECK_COUPON, Urls.MethodType.POST, params, httpDoCallback);
    }

    private HttpRequest.HttpCallback httpDoCallback = new HttpRequest.HttpCallback() {
        @Override
        public void httpSuccess(String response) {
            restartPreviewAfterDelay(1000);//再次扫描
            hideToastAnim();
            CouponInfo CouInfo = GsonUtil.json2T(response, CouponInfo.class);
            if (CouInfo.getStatus() != 1){
                if (reCount < 3) {
                    CheckTicketDo(reppasswd);
                    reCount++;
                }
            }
        }
        @Override
        public void httpFail(String response) {
            //重试销毁次数3次，三次后就释放了不在占用资源了
            if (reCount < 3) {
                CheckTicketDo(reppasswd);
                reCount++;
            }
        }
    };


    private HttpRequest.HttpCallback httpCallback = new HttpRequest.HttpCallback() {
        @Override
        public void httpSuccess(String response) {
            restartPreviewAfterDelay(1500);//再次扫描
          //  etFormNum.setText("");
            hideToastAnim();
            CouponInfo CouInfo = GsonUtil.json2T(response, CouponInfo.class);
            if (CouInfo.getStatus() == 1){
                PlayTicketType(CouInfo.getData().getType(),CouInfo.getData().getPassword(),CouInfo.getMsg());
            }else{
                tvState.setText(CouInfo.getMsg());
            }
        }

        @Override
        public void httpFail(String response) {
            restartPreviewAfterDelay(1000);//再次扫描
            hideToastAnim();
            try {
                CouponInfo CouInfo = GsonUtil.json2T(response, CouponInfo.class);
                PlayTicketType(0,"",CouInfo.getMsg());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };


    //  1 儿童票 2亲子票 3单人票 4双人票 5三人票 6 团体票  7 一周票
    //扫别人成功后提交信息
    //让别人扫也提交信息 并根据返回值生成二维码
    private void PlayTicketType(int TicketType,final String passwd,String err) {
        boolean is_success ;
        switch (TicketType) {
            case 1:
                beepManager.playBeepSound(R.raw.ertongpiao);//三恩票
                if (err=="") err ="验证成功，儿童票";
                tvState.setText(err);
                is_success = true;
                break;
            case 2:
                beepManager.playBeepSound(R.raw.qinzipiao);//声音与振动
                if (err=="") err ="验证成功，亲子票";
                tvState.setText(err);
                is_success = true;
                break;
            case 3:
                beepManager.playBeepSound(R.raw.danrenpiao);//声音与振动
                if (err=="")  err ="验证成功，单人票";
                tvState.setText(err);
                is_success = true;
                break;
            case 4:
                beepManager.playBeepSound(R.raw.shuangrenpiao);//声音与振动
                if (err=="")  err ="验证成功，双人票";
                tvState.setText(err);
                is_success = true;
                break;
            case 5:
                beepManager.playBeepSound(R.raw.sanrenpiao);//三恩票
                if (err=="")  err ="验证成功，三人票";
                tvState.setText(err);
                is_success = true;
                break;
            case 6:
                beepManager.playBeepSound(R.raw.tuantipiao);//三恩票
                if (err=="")  err ="验证成功，团体票";
                tvState.setText(err);
                is_success = true;
                break;
            case 7:
                beepManager.playBeepSound(R.raw.yizhoupiao);//三恩票
                if (err=="")  err ="验证成功，一周票";
                tvState.setText(err);
                is_success = true;
                break;
            default:
                beepManager.playBeepSound(R.raw.wuxiaopiao);//声音与振动
                if (err=="")  err ="验证成功，无效票";
                tvState.setText(err);
                is_success = false;
                break;
        }
        if (is_success) new Thread(new Runnable() { @Override  public void run() {reppasswd = passwd;CheckTicketDo(passwd);}}).start();
    }

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
        if (tags == 1){
            restartPreviewAfterDelay(3000);//再次扫描
            showToastAnim("正在查询...");

        }else{
            showToastAnim("正在验证...");
            CheckTicket(scanResult);
        }
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

    public static void startAct(Context context, String title,int tags) {
        Intent intent = new Intent(context, CaptureActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("tags", tags);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.out_from_right);
    }
}