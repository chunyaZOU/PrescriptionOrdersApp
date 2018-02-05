package com.sy.prescription.util;

import android.content.Context;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;

/**
 * Created by linhz on 2015/12/9.
 * Email: linhaizhong@ta2she.com
 */
public class FrescoHelper {

    public static void initFresco(Context context) {
        ImagePipelineConfig.Builder pipeConfigBuilder = ImagePipelineConfig.newBuilder(context);
        String cachePath = StoragePathManager.getInstance().getCachePath();
        DiskCacheConfig.Builder diskBuilder = DiskCacheConfig.newBuilder(context);
        diskBuilder.setBaseDirectoryPath(new File(cachePath));
        diskBuilder.setBaseDirectoryName("image_cache");
        diskBuilder.setMaxCacheSize(60 * 1024 * 1024L); // 50M
        diskBuilder.setMaxCacheSizeOnLowDiskSpace(20 * 1024 * 1024L);//20M
        diskBuilder.setMaxCacheSizeOnVeryLowDiskSpace(10 * 1024 * 1024L);//10M
        pipeConfigBuilder.setMainDiskCacheConfig(diskBuilder.build());
        Fresco.initialize(context, pipeConfigBuilder.build());
    }
}