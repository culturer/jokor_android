package vip.jokor.im.util.base;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

    private static final String TAG = "FileUtil";

    private static String ROOT_PATH = "";

    //头像目录
    public static String ICON_PATH = ROOT_PATH + "/icon";
    //聊天目录
    public static String CHAT_PATH_IMG = "";
    public static String CHAT_PATH_AUDIO = "";
    public static String CHAT_PATH_VIDEO = "";


    public static void initFilePath(Context context){
        ROOT_PATH = context.getExternalFilesDir("").getAbsolutePath();
        createFolder(ICON_PATH);
        createFolder(ROOT_PATH+"/chat");
        CHAT_PATH_IMG = ROOT_PATH + "/chat/img";
        CHAT_PATH_AUDIO = ROOT_PATH + "/chat/audio";
        CHAT_PATH_VIDEO = ROOT_PATH + "/chat/video";
        createFolder(CHAT_PATH_IMG);
        createFolder(CHAT_PATH_AUDIO);
        createFolder(CHAT_PATH_VIDEO);
        Log.i(TAG, "initFilePath: "+ROOT_PATH);
        Log.i(TAG, "CHAT_PATH_IMG: "+CHAT_PATH_IMG);
        Log.i(TAG, "CHAT_PATH_AUDIO: "+CHAT_PATH_AUDIO);
        Log.i(TAG, "CHAT_PATH_VIDEO: "+CHAT_PATH_VIDEO);
    }

    /**
     * 创建文件夹
     **/
    public static void createFolder(String path) {
        File Folder = new File(path );
        if (!Folder.exists())//判断文件夹是否存在，不存在则创建文件夹，已经存在则跳过
        {
            Folder.mkdir();//创建文件夹
        } else {
            Log.i(TAG, "文件夹已存在");
        }
    }

    public static void downLogo(String url, ProgressListener progressListener, HttpCallback callback){
        ThreadUtil.startThreadInPool(() -> RxVolley.download(ICON_PATH,url,progressListener,callback));
    }

    public static String saveBitmap(Bitmap bitmap, String bitName,Context context){
        String fileName ;
        File file ;
        if(Build.BRAND .equals("Xiaomi") ){
            // 小米手机
          fileName = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/"+bitName ;
        }else{ // Meizu 、Oppo
          fileName = Environment.getExternalStorageDirectory().getPath()+"/DCIM/"+bitName ;
        }
        file = new File(fileName);
        if(file.exists()){
          file.delete();
        }
        FileOutputStream out;
        try{
          out = new FileOutputStream(file);
        // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
          if(bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out))
          {
            out.flush();
            out.close();
        // 插入图库
             MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), bitName, null);
          }
        } catch (IOException e)
        {
          e.printStackTrace();
        }
        // 发送广播，通知刷新图库的显示
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
        return fileName;
    }
    
    public static File uri2File(Context context,Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();

        AssetFileDescriptor fd = null;
        try {
            fd = contentResolver.openAssetFileDescriptor(uri, "r");
            if (fd != null) {
                FileInputStream inputStream = fd.createInputStream();
//                    int total = inputStream.available();
//                    Log.i(TAG, "onActivityResult: total "+total);
//                    byte[] bs = new byte[total];
//                    inputStream.read(bs);
//                    QiNiuUtil.getInstance().getToken(bs);

                File file = new File(context.getCacheDir(), "cache");
                copy(inputStream, file);
                Log.i(TAG, "onActivityResult: file length --- " + file.length());
                return file;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void copy(File src, ParcelFileDescriptor parcelFileDescriptor) throws IOException {
        FileInputStream istream = new FileInputStream(src);
        try {
            FileOutputStream ostream = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
            try {
                 copy(istream, ostream);
            } finally {
                ostream.close();
            }
        } finally {
            istream.close();
        }
    }

    public static void copy(ParcelFileDescriptor parcelFileDescriptor, File dst) throws IOException {
        FileInputStream istream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
        try {
            FileOutputStream ostream = new FileOutputStream(dst);
            try {
                 copy(istream, ostream);
            } finally {
                ostream.close();
            }
        } finally {
            istream.close();
        }
    }

    public static void copy(FileInputStream fileInputStream, File dst) throws IOException {
        try {
            FileOutputStream ostream = new FileOutputStream(dst);
            try {
                 copy(fileInputStream, ostream);
            } finally {
                ostream.close();
            }
        } finally {
            fileInputStream.close();
        }
    }


    public static void copy(InputStream ist, OutputStream ost) throws IOException {
        byte[] buffer = new byte[4096];
        int byteCount = 0;
        while ((byteCount = ist.read(buffer)) != -1) {  // 循环从输入流读取 buffer字节
            ost.write(buffer, 0, byteCount);        // 将读取的输入流写入到输出流
        }
    }

}
