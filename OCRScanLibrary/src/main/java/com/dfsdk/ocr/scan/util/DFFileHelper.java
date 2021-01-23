package com.dfsdk.ocr.scan.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Copyright (c) 2018-2019 DEEPFINCH Corporation. All rights reserved.
 */
public class DFFileHelper {
    public static byte[] getAssetResource(Context context, String imageName) {
        byte[] fileByte = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        // 读取license文件内容
        try {
            is = context.getResources().getAssets().open(imageName);
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n;
            while ((n = is.read(buffer)) > 0) {
                baos.write(buffer, 0, n);
            }
            baos.flush();
            fileByte = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("_jni", "createHandle  error  " + e.toString());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return fileByte;
    }

    /**
     * Bitmap转化为ARGB数据，再转化为NV21数据
     *
     * @param src    传入ARGB_8888的Bitmap
     * @param width  NV21图像的宽度
     * @param height NV21图像的高度
     * @return nv21数据
     */
    public static byte[] bitmapToNv21(Bitmap src, int width, int height) {
        if (src != null && src.getWidth() >= width && src.getHeight() >= height) {
            int[] argb = new int[width * height];
            src.getPixels(argb, 0, width, 0, 0, width, height);
            return argbToNv21(argb, width, height);
        } else {
            return null;
        }
    }

    /**
     * ARGB数据转化为NV21数据
     *
     * @param argb   argb数据
     * @param width  宽度
     * @param height 高度
     * @return nv21数据
     */
    private static byte[] argbToNv21(int[] argb, int width, int height) {
        int frameSize = width * height;
        int yIndex = 0;
        int uvIndex = frameSize;
        int index = 0;
        byte[] nv21 = new byte[width * height * 3 / 2];
        for (int j = 0; j < height; ++j) {
            for (int i = 0; i < width; ++i) {
                int R = (argb[index] & 0xFF0000) >> 16;
                int G = (argb[index] & 0x00FF00) >> 8;
                int B = argb[index] & 0x0000FF;
                int Y = (66 * R + 129 * G + 25 * B + 128 >> 8) + 16;
                int U = (-38 * R - 74 * G + 112 * B + 128 >> 8) + 128;
                int V = (112 * R - 94 * G - 18 * B + 128 >> 8) + 128;
                nv21[yIndex++] = (byte) (Y < 0 ? 0 : (Y > 255 ? 255 : Y));
                if (j % 2 == 0 && index % 2 == 0 && uvIndex < nv21.length - 2) {
                    nv21[uvIndex++] = (byte) (V < 0 ? 0 : (V > 255 ? 255 : V));
                    nv21[uvIndex++] = (byte) (U < 0 ? 0 : (U > 255 ? 255 : U));
                }

                ++index;
            }
        }
        return nv21;
    }

    public static String getDetectResultImageDir(){
        StringBuilder takePictureDir = new StringBuilder();
        takePictureDir.append(Environment.getExternalStorageDirectory())
                .append(File.separator)
                .append("df_india_ocr")
                .append(File.separator)
                .append("result_image");
        return takePictureDir.toString();
    }


    public static String saveFile(byte[] data, String filePath, String fileName) {
        String fileAbsolutePath = null;
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
            }
            File file = new File(filePath + File.separator + fileName);
            fileAbsolutePath = file.getAbsolutePath();
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(data);
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            fileAbsolutePath = null;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return fileAbsolutePath;
    }


}
