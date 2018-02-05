package com.sy.prescription;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.command.GpUtils;
import com.gprinter.io.GpDevice;
import com.gprinter.service.GpPrintService;
import com.newland.mtype.module.common.printer.PrintContext;
import com.newland.mtype.module.common.printer.Printer;
import com.newland.mtype.module.common.printer.PrinterStatus;
import com.sy.prescription.device.N900Device;
import com.sy.prescription.util.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrintManageActivity extends BaseActivity {

    @BindView(R.id.tvErtong)
    TextView tvErtong;
    @BindView(R.id.tvQinzi)
    TextView tvQinzi;
    @BindView(R.id.tvDanren)
    TextView tvDanren;
    @BindView(R.id.tvShuangren)
    TextView tvShuangren;
    @BindView(R.id.tvSanren)
    TextView tvSanren;
    @BindView(R.id.tvStore)
    TextView tvStore;

    @BindView(R.id.tvRight)
    TextView tvRight;



    private Dialog dialog, backDialog;
    private String formNum, result, msgResult;
    private Printer printer;
    private N900Device n900Device;
    private String Msg;
    private int PrintType;
    private boolean is_print = false;

    private GpService mGpService;
    private PrinterServiceConnection conn = null;
    private int mPrinterIndex = 0;
    private static final int MAIN_QUERY_PRINTER_STATUS = 0xfe;
    private static final int REQUEST_PRINT_LABEL = 0xfd;
    private static final int REQUEST_PRINT_RECEIPT = 0xfc;
    private boolean connentLoading = false;
    private boolean connentStatus = false;
    private boolean day = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_manage);
        ButterKnife.bind(this);
        initNav("现场售票");
        connection();
        initView();
    }

    private void initView() {
        tvRight.setVisibility(View.VISIBLE);
    }

    private void connection() {
        conn = new PrinterServiceConnection();
        Intent intent = new Intent(this, GpPrintService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
    }

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // Log.i("ServiceConnection", "onServiceDisconnected() called");
            mGpService = null;
           PrintStatusView(0); // ToastUtil.show("已经关闭");

        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService =GpService.Stub.asInterface(service);
            try {
                registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_DEVICE_REAL_STATUS));
                registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_CONNECT_STATUS));
                registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_RECEIPT_RESPONSE));
                connectOrDisConnectToDevice();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    void connectOrDisConnectToDevice() {
        int rel = 0;
        int PrinterId = 0;
        try {
            rel = mGpService.openPort(PrinterId, 4,"DC:0D:30:04:31:45", 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
        System.out.print("连接结果  "+String.valueOf(r));
        if (r != GpCom.ERROR_CODE.SUCCESS) {
            if (r == GpCom.ERROR_CODE.DEVICE_ALREADY_OPEN) {
                hideToastAnim();
                PrintStatusView(1); // 连接成功
            } else {
                ToastUtil.show(GpCom.getErrorText(r));
                PrintStatusView(0); // 连接失败
            }
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
           System.out.print("票据模式 广播  "+action.toString());
            if (GpCom.ACTION_CONNECT_STATUS.equals(intent.getAction())) {
                int type = intent.getIntExtra(GpPrintService.CONNECT_STATUS, 0);
                int id = intent.getIntExtra(GpPrintService.PRINTER_ID, 0);
                Log.d("print", "链接状态 " + type);
                if (type == GpDevice.STATE_CONNECTING) {

                    PrintStatusView(2); // 连接中！！！

                } else if (type == GpDevice.STATE_NONE) {
                  PrintStatusView(0);
                } else if (type == GpDevice.STATE_INVALID_PRINTER) {
                    //ToastUtil.show("连接成功，请使用打印机!");
                    PrintStatusView(1);
                } else if (type == GpDevice.STATE_VALID_PRINTER) {
                    PrintStatusView(1);
                //    ToastUtil.show("连接成功!");
                }


            }

            if (action.equals(GpCom.ACTION_DEVICE_REAL_STATUS)) {
                // 业务逻辑的请求码，对应哪里查询做什么操作
                int requestCode = intent.getIntExtra(GpCom.EXTRA_PRINTER_REQUEST_CODE, -1);
                // 判断请求码，是则进行业务操作
                if (requestCode == MAIN_QUERY_PRINTER_STATUS) {
                    int status = intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS, 16);
                    String str;
                    if (status == GpCom.STATE_NO_ERR) {
                        str = "打印机正常";
                    } else {
                        str = "打印机 ";
                        if ((byte) (status & GpCom.STATE_OFFLINE) > 0) {
                            str += "脱机";
                        }
                        if ((byte) (status & GpCom.STATE_PAPER_ERR) > 0) {
                            str += "缺纸";
                        }
                        if ((byte) (status & GpCom.STATE_COVER_OPEN) > 0) {
                            str += "打印机开盖";
                        }
                        if ((byte) (status & GpCom.STATE_ERR_OCCURS) > 0) {
                            str += "打印机出错";
                        }
                        if ((byte) (status & GpCom.STATE_TIMES_OUT) > 0) {
                            str += "查询超时";
                        }
                    }
                    ToastUtil.show("打印机状态：" + str);
                } else if (requestCode == REQUEST_PRINT_RECEIPT) {
                    int status = intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS, 16);
                    if (status == GpCom.STATE_NO_ERR) {
                        sendReceipt();
                    } else {
                        ToastUtil.show("查询打印机状态错误");
                    }
                }
            }
        }
    };

    private void PrintStatusView(int i) {
        if (i == 0){
            hideToastAnim();
            ToastUtil.show("连接已关闭");
            tvRight.setText("连接");
            tvRight.setClickable(true);
            connentLoading = false;
            connentStatus = false;
        }else if (i== 1){
            hideToastAnim();
            ToastUtil.show("连接成功");
            tvRight.setText("关闭");
            tvRight.setClickable(true);
            connentLoading = false;
            connentStatus = true;
            showTicketType("请选择销售模式");
        }else{
            if (!connentLoading){
                showToastAnim("连接中..");
                tvRight.setText("连接中");
                tvRight.setClickable(false);
                connentLoading = true;
            }
            connentStatus = false;
        }
    }


    void sendReceipt() {
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
         esc.addPrintAndFeedLines((byte) 3);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addText("Sample\n"); // 打印文字
        esc.addPrintAndLineFeed();

		/* 打印文字 */
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
		/* 绝对位置 具体详细信息请查看GP58编程手册 */
        esc.addText("智汇");
        esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 10);
        esc.addSetAbsolutePrintPosition((short) 3);
        Bitmap b =   getImage(320,500,"测试门票",16);
        esc.addRastBitImage(b, 450, 0); // 打印图片
        if (!b.isRecycled()) {
            b.recycle();
        }
		/* 打印一维条码 *//*
        esc.addText("Print code128\n"); // 打印文字
        esc.addSelectPrintingPositionForHRICharacters(EscCommand.HRI_POSITION.BELOW);//
        // 设置条码可识别字符位置在条码下方
        esc.addSetBarcodeHeight((byte) 60); // 设置条码高度为60点
        esc.addSetBarcodeWidth((byte) 1); // 设置条码单元宽度为1
        esc.addCODE128(esc.genCodeB("SMARNET")); // 打印Code128码
        esc.addPrintAndLineFeed();
		/*
		 * QRCode命令打印 此命令只在支持QRCode命令打印的机型才能使用。 在不支持二维码指令打印的机型上，则需要发送二维条码图片
		 */
/*        esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31); // 设置纠错等级
        esc.addSelectSizeOfModuleForQRCode((byte) 5);// 设置qrcode模块大小
        esc.addStoreQRCodeData("www.smarnet.cc");// 设置qrcode内容
        esc.addPrintQRCode();// 打印QRCode
        esc.addPrintAndLineFeed();*/

       // esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印左对齐
        //esc.addText("Completed!\r\n"); // 打印结束
        // 开钱箱
       // esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
        esc.addPrintAndFeedLines((byte) 4);
        esc.addCutPaper();
        esc.addQueryPrinterStatus();
        Vector<Byte> datas = esc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String sss = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rs;
        try {
            rs = mGpService.sendEscCommand(mPrinterIndex, sss);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    /**
     * 显示打票類型
     *
     */
    private void showTicketType(String tips) {
        AlertDialog updateDialog = new AlertDialog.Builder(this).create();
        updateDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "预售票15天", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                day = false;
            }
        });
        updateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "当日票", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                day = true;
            }
        });
        updateDialog.setCancelable(false);
        updateDialog.setCanceledOnTouchOutside(false);
       updateDialog.setTitle("选择售票有效期");
        updateDialog.setMessage(tips);
        updateDialog.show();
    }


    public static Bitmap getImage(int width, int height, String mString, int size) {
        return getImage(width, height, mString, size, Color.BLACK, Typeface.create("微软雅黑", Typeface.NORMAL));
    }

    public static Bitmap getImage(int width, int height, String mString, int size, int color, Typeface font) {
        long startTime = System.currentTimeMillis();
        int xw = width;
        int yh = height;
        Bitmap bmp = Bitmap.createBitmap(xw, yh, Bitmap.Config.ARGB_8888);
        //图象大小要根据文字大小算下,以和文本长度对应
        Canvas canvasTemp = new Canvas(bmp);
        canvasTemp.drawColor(Color.WHITE);
        Paint p = new Paint();
        p.setColor(color);
        p.setTypeface(font);
        p.setAntiAlias(true);//去除锯齿
        p.setFilterBitmap(true);//对位图进行滤波处理
        p.setTextSize(22);
        canvasTemp.drawText(mString, 50, 100, p);
        canvasTemp.drawText("30元", 50, 150, p);
        canvasTemp.drawText("2017年05月22日前有效", 50, 200, p);
        Hashtable hints = new Hashtable();
        long estimatedTime = System.currentTimeMillis() - startTime;
        Log.d("time","文字处理完成，耗时"+String.valueOf(estimatedTime));
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //容错级别
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置空白边距的宽度
//            hints.put(EncodeHintType.MARGIN, 2); //default is 4
        int angle = 90;
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        String content = "88888855";
        int widthPix = 290;
        int heightPix = 290;
        // 图像数据转换，使用了矩阵转换
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
            int[] pixels = new int[widthPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;
                    }
                }
            }

            resizedBitmap.setPixels(pixels, 0, widthPix,5, 5, widthPix, heightPix);

            estimatedTime = System.currentTimeMillis() - startTime;
            Log.d("time","二维码处理完成，耗时"+String.valueOf(estimatedTime));

        } catch (WriterException e) {
            e.printStackTrace();
        }
     // canvasTemp.save(Canvas.ALL_SAVE_FLAG);
     //   canvasTemp.restore();

        estimatedTime = System.currentTimeMillis() - startTime;
        Log.d("time","旋转完成，耗时"+String.valueOf(estimatedTime));
        return resizedBitmap;
    }

    //  1 儿童票 2亲子票 3单人票 4双人票 5三人票
  @OnClick({R.id.tvErtong, R.id.tvQinzi, R.id.tvDanren, R.id.tvShuangren, R.id.tvSanren  , R.id.tvStore  , R.id.tvRight  })
    public void onClick(View view) {
        switch (view.getId()) {
            // 连接断开打印机 开始
            case R.id.tvRight:
                if (connentStatus){
                    try {
                        mGpService.closePort(0);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }else{
                    connectOrDisConnectToDevice();
                }
                break;
            // 连接断开打印机 结束

            // 切换票据模式 开始
            case R.id.tvStore:
                if ( day){
                    showTicketType("当前为当日票模式，有效期当天，请选择新的售票模式");
                }else{
                    showTicketType("当前为预售票模式，15天内有效，请选择新的售票模式");
                }
                break;
            // 切换票据模式 结束


            case R.id.tvQinzi:
                try {
                    mGpService.closePort(0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                break;


            case R.id.tvErtong:
                PrintType = 1;
                printTicket();
                break;
            case R.id.tvShuangren:
                PrintType = 4;
                printTicket();

                break;
            case R.id.tvSanren:
                PrintType = 5;

                try {
                    int sa   =    mGpService.getPrinterConnectStatus(0);
                    System.out.print(sa);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                break;
/*            case R.id.tvStore:
                PrintType = 6;

       *//*         ImageView imgview = (ImageView) findViewById(R.id.imageView);

                 Bitmap img =   getImage(300,500,"测试门票",16);

                imgview.setImageBitmap(img);

*//*
                break;*/
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastUtil.show(msg.obj.toString());
        }
    };


    private void initBackDialog() {
        backDialog = new Dialog(PrintManageActivity.this, R.style.CustomDialog);
        backDialog.setContentView(R.layout.print_msg);
        backDialog.setCancelable(false);
        backDialog.setCanceledOnTouchOutside(false);
        backDialog.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backDialog.dismiss();
                backDialog = null;
            }
        });
        backDialog.findViewById(R.id.tvOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backDialog.dismiss();
                backDialog = null;
               printTicket();
            }
        });
        backDialog.show();
    }

    //  1 儿童票 2亲子票 3单人票 4双人票 5三人票

    private void printTicket(){

        if (PrintType == 1){
            try {
                int type = mGpService.getPrinterCommandType(mPrinterIndex);
                if (type == GpCom.ESC_COMMAND) {
                    mGpService.queryPrinterStatus(mPrinterIndex, 3000, MAIN_QUERY_PRINTER_STATUS);
                } else {
                    Toast.makeText(this, "Printer is not receipt mode", Toast.LENGTH_SHORT).show();
                }
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        }

        if (PrintType == 2){
            try {
                int rel = mGpService.printeTestPage(mPrinterIndex); //
                Log.i("ServiceConnection", "rel " + rel);
                GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
                if (r != GpCom.ERROR_CODE.SUCCESS) {
                    Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
                }
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        }

        if (PrintType == 4){
            try {
                int type = mGpService.getPrinterCommandType(mPrinterIndex);
                if (type == GpCom.ESC_COMMAND) {
                    mGpService.queryPrinterStatus(mPrinterIndex, 3000, REQUEST_PRINT_RECEIPT);
                } else {
                    Toast.makeText(this, "Printer is not receipt mode", Toast.LENGTH_SHORT).show();
                }
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    public String printOrder(int type) {
        StringBuffer scriptBuffer = new StringBuffer();
        try {
            if (printer.getStatus() != PrinterStatus.NORMAL) {
                return "打印失败！打印机状态不正确" + printer.getStatus();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "打印失败！打印机状态不正确" + e;
        }
        try {
            is_print = false;
            scriptBuffer.append("!hz l\n !asc l\n");// 设置标题字体为大号
            scriptBuffer.append("!yspace 20\n");// 设置行间距 ,取值【0,60】，默认6
            scriptBuffer.append("*text c 水上乐园自助门票\n");
            scriptBuffer.append("!hz n\n !asc n\n");// 设置内容字体为中号
            scriptBuffer.append("!yspace 10\n");// 设置内容行间距
            scriptBuffer.append("*line" + "\n");// 打印虚线
            switch (type) {
                case 1:
                    scriptBuffer.append("*qrcode c 430450844097\n"); //亲子儿童票
                    scriptBuffer.append("*feedline 1\n");
                    scriptBuffer.append("*text c 4304 5084 4097\n");
                    scriptBuffer.append("*line" + "\n");// 打印虚线
                    scriptBuffer.append("*text c 儿童票\n");
                    break;
                case 2:
                    scriptBuffer.append("*qrcode c 389496251096\n"); //亲子票
                    scriptBuffer.append("*feedline 1\n");
                    scriptBuffer.append("*text c 3894 9625 1096\n");
                    scriptBuffer.append("*line" + "\n");// 打印虚线
                    scriptBuffer.append("*text c 亲子票\n");
                    break;
                case 3:
                    scriptBuffer.append("*qrcode c 399740580737\n"); //单人票
                    scriptBuffer.append("*feedline 1\n");
                    scriptBuffer.append("*text c 3997 4058 0737\n");
                    scriptBuffer.append("*line" + "\n");// 打印虚线
                    scriptBuffer.append("*text c 单人票\n");
                    break;
                case 4:
                    scriptBuffer.append("*qrcode c 520006572923\n"); //双人票
                    scriptBuffer.append("*feedline 1\n");
                    scriptBuffer.append("*text c 5200 0657 2923\n");
                    scriptBuffer.append("*line" + "\n");// 打印虚线
                    scriptBuffer.append("*text c 双人票\n");
                    break;
                case 5:
                    scriptBuffer.append("*qrcode c 568992501307\n"); //三人票
                    scriptBuffer.append("*feedline 1\n");
                    scriptBuffer.append("*text c 5689 9250 1307\n");
                    scriptBuffer.append("*line" + "\n");// 打印虚线
                    scriptBuffer.append("*text c 三人票\n");
                    break;
                case 6:

                    StringBuffer scriptBuffer1 = new StringBuffer();
                    scriptBuffer1.append("!hz l\n !asc l\n");// 设置标题字体为大号
                    scriptBuffer1.append("!yspace 20\n");// 设置行间距 ,取值【0,60】，默认6
                    scriptBuffer1.append("*text c 水上乐园便利店1\n");
                    scriptBuffer1.append("!hz n\n !asc n\n");// 设置内容字体为中号
                    scriptBuffer1.append("!yspace 10\n");// 设置内容行间距
                    scriptBuffer1.append("*line" + "\n");// 打印虚线
                    scriptBuffer1.append("*qrcode c http://hd.shop.sh.cn/wap/index.php?ctl=store_pay&id=99\n"); //单人票
                    scriptBuffer1.append("*line" + "\n");// 打印虚线
                    scriptBuffer1.append("*text l 此二维码可以消费充值的会员卡余额\n");
                    scriptBuffer1.append("*line" + "\n");// 打印虚线
                    scriptBuffer1.append("*text l 服务热线:0371-27585888\n");
                    scriptBuffer1.append("*line" + "\n");// 打印虚线
                    scriptBuffer1.append("*feedline 3\n");
                    is_print = true;
                    try {
                        printer.printByScript(PrintContext.defaultContext(), scriptBuffer1.toString().getBytes("GBK"), 60, TimeUnit.SECONDS);
                        Msg = "打印成功";
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Msg = "打印失败" + e;
                    }
                    break;
            }

            scriptBuffer.append("*line" + "\n");// 打印虚线
            scriptBuffer.append("*text l 本票只能验证一次，无法二次使用，如有异常请联系工作人员\n");
            scriptBuffer.append("*line" + "\n");// 打印虚线
            scriptBuffer.append("*text l 服务热线:0371-27585888\n");
            scriptBuffer.append("*line" + "\n");// 打印虚线
            scriptBuffer.append("*feedline 3\n");
            if (is_print == false){
                try {
                    printer.printByScript(PrintContext.defaultContext(), scriptBuffer.toString().getBytes("GBK"), 60, TimeUnit.SECONDS);
                    Msg = "打印成功";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Msg = "打印失败" + e;
                }
            }
            return Msg;
        } catch (Exception e) {
            return "打印脚本异常" + e;
        }
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, PrintManageActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.in_from_right, R.anim.out_from_right);
    }

    @Override
    protected void onDestroy() {
        // TODO 自动生成的方法存根
        super.onDestroy();
        if (conn != null) {
            unbindService(conn); // unBindService
        }
        try {
            mGpService.closePort(0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        unregisterReceiver(mBroadcastReceiver);

    }
/*
    *//**
     * 打印机连接回调
     *//*
    public class ConnectCallback extends JBPrinterConnectCallback.Stub {

        @Override
        public void onConnecting(final int mId) throws RemoteException {

            Log.d(TAG, "--------onConnecting----------");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvCurrent.setText(getStatus(mId));
                }
            });
        }

        @Override
        public void onDisconnect(final int mId) throws RemoteException {
            Log.d(TAG, "--------onDisconnect----------");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvCurrent.setText(getStatus(mId));
                }
            });
        }

        @Override
        public void onConnected(final int mId) throws RemoteException {
            Log.d(TAG, "--------onConnected----------");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvCurrent.setText(getStatus(mId));
                }
            });
        }

//        @Override
//        public IBinder asBinder() {
//            return null;
//        }
    }*/
}