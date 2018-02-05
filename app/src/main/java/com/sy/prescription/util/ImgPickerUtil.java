package com.sy.prescription.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.cloudcommune.yhonline.imgpicker.ImagePicker;
import com.cloudcommune.yhonline.imgpicker.loader.GlideImageLoader;
import com.cloudcommune.yhonline.imgpicker.view.CropImageView;
import com.sy.prescription.R;
import com.sy.prescription.fragment.PrescriptionFragment;
import com.sy.prescription.view.widget.ActionSheetPanel;

/**
 * Created by zcy on 2017/12/26.
 */

public class ImgPickerUtil {
    public static void initImagePicker(Context context) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setMultiMode(false);
        imagePicker.setCrop(true);
        imagePicker.setSaveRectangle(true);
        imagePicker.setShowCamera(true);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        /*imagePicker.setFocusWidth(600);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(900);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1200);*/
        imagePicker.setFocusWidth((int)(ScreenUtil.getScreenWidth(context) * 0.8));                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight((int)(ScreenUtil.getScreenHeigh(context) * 0.8));                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX((int)(ScreenUtil.getScreenWidth(context) * 0.8));                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY((int)(ScreenUtil.getScreenHeigh(context) * 0.8));
    }
}
