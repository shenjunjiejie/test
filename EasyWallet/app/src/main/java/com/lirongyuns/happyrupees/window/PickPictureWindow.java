package com.lirongyuns.happyrupees.window;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import androidx.core.content.FileProvider;

import com.lirongyuns.happyrupees.R;
import com.lirongyuns.happyrupees.annonation.Click;
import com.lirongyuns.happyrupees.annonation.ContentView;

import java.io.File;

/**
 * 图片选择的方式
 */
@ContentView(R.layout.window_pick_picture_lirongyun)
public class PickPictureWindow extends SlideWindow {

    public static final int ALBUM = 1;
    public static final int TAKE_PHOTO = 2;
    public static final int CROP = 3;
    //拍照时图片的保存目录
    private String folderPath;
    //拍照时的图片uri
    private Uri picUri;
    //拍照时完整的文件路径
    private String filePath = "";
    //截取图片的路径
    private String cropImagePath = "";
    //file provider权限名称
    private String authority;

    public PickPictureWindow(Activity activity) {
        super(activity, SHOW_BOTTOM);
    }

    @Override
    protected void onCreate() {
        String path = "";
        File folder = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            folderPath = getActivity().getExternalFilesDir(null).getPath();
        }else{
            path = getActivity().getApplication().getFilesDir().getAbsolutePath();
            //getApplication().getFilesDir().getAbsolutePath()
            folder = new File(path);
            if(!folder.exists()) folder.mkdirs();
            folderPath = folder.getAbsolutePath() + "/";
        }
    }

    @Click({R.id.tv_take_photo, R.id.tv_cancel})
    private void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tv_picture_album:
//                Intent intent = new Intent(Intent.ACTION_PICK,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                getActivity().startActivityForResult(intent, ALBUM);
//                dismiss();
//                break;
            case R.id.tv_take_photo:
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//MediaStore.ACTION_IMAGE_CAPTURE
                //获得保存图片的公共路径
                String fileName = "IMG"+System.currentTimeMillis()+".jpg";
                filePath = folderPath + fileName;
                File file = new File(filePath);
    
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                
                Activity activity = getActivity();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        String path = getActivity().getExternalFilesDir(null).getPath();
                        picUri =  Uri.parse(path);
                    }else{
                        intent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        picUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", file);//
                    }
                } else {
                    picUri = Uri.fromFile(file);
                }
                // 下面这句指定调用相机拍照后的照片存储的路径
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                dismiss();
                activity.startActivityForResult(intent2, TAKE_PHOTO);
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    /**
     * 图片裁剪
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        cropImagePath = folderPath + "Head"+System.currentTimeMillis()+".jpg";
        Uri cropImageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//允许临时的读和写
        }

        cropImageUri = Uri.fromFile(new File(cropImagePath));//保存路径的uri
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        //该参数可以不设定用来规定裁剪区的宽高比
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //该参数设定为你的imageView的大小
//        intent.putExtra("outputX", 256);
//        intent.putExtra("outputY", 256);
        intent.putExtra("scale", true);
        //是否返回bitmap对象
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出图片的格式
        intent.putExtra("noFaceDetection", true); // 头像识别
        getActivity().startActivityForResult(intent, CROP);
    }

    /**
     * 获取裁剪后的地址
     * @return
     */
    public String getCropImagePath() {
        return cropImagePath;
    }

    /**
     * 获取拍照时的图片Uri
     */
    public Uri getPicUri()
    {
        return picUri;
    }

    /**
     * 获取拍照时的图片路径
     */
    public String getFilePath()
    {
        return filePath;
    }

    /**
     * Android 7.0 以上必须设置
     * @param authority 权限名称
     */
    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
