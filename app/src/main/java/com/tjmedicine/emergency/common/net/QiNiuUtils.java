package com.tjmedicine.emergency.common.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.tjmedicine.emergency.EmergencyApplication;
import com.tjmedicine.emergency.common.global.GlobalConstants;
import com.tjmedicine.emergency.utils.GsonUtils;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author QiZai
 * @desc
 * @date 2018/9/28 17:11
 */

public class QiNiuUtils {

    private static QiNiuUtils instance;
    private static UploadManager mUploadManager;
    private UploadResult mUploadResult;
    private String mFilePath;

    private QiNiuUtils() {
    }

    /**
     * 获取 QiNiuUtils 实例
     *
     * @return
     */
    public static QiNiuUtils getInstance() {
        if (instance == null) {
            synchronized (QiNiuUtils.class) {
                if (instance == null) {
                    instance = new QiNiuUtils();
                    mUploadManager = new UploadManager();
                }
            }
        }
        return instance;
    }

    /**
     * token回调
     */
    private HttpProvider.ResponseCallback getTokenCallback = new HttpProvider.ResponseCallback() {
        @Override
        public void callback(String responseText) {


            ResponseEntity<String> res = GsonUtils.parseJsonWithClass(responseText,
                    new TypeToken<ResponseEntity<String>>() {
                    }.getType());
            if (res.getCode() == 200) {
                uploadFinal(res.getData());
            } else {
                uploadFail(res.getMsg());
            }
        }
    };

    /**
     * 获取token
     */
    private void getToken() {
        HttpProvider.doGet(GlobalConstants.BASE_SERVER_URL+"/file/uptoken", getTokenCallback);
//        HttpProvider.doGet("http://192.168.1.142:8096/app/qiniu/getQiniuToken", getTokenCallback);
    }

    /**
     * 对外开放上传接口
     *
     * @param filePath
     * @param uploadResult
     */
    public void upload(String filePath, UploadResult uploadResult) {
        mFilePath = filePath;
        mUploadResult = uploadResult;
        getToken();
    }

    /**
     * 获取文件名
     *
     * @return
     */
    private String getFileName() {
        return "AD_UPLOAD_FILE_" + System.currentTimeMillis() + mFilePath.substring(mFilePath.lastIndexOf("."));
    }
    private void uploadFinal(String token) {
        final String fileName = getFileName();
        File file = new File(mFilePath);
        mUploadManager.put(file, fileName, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    if (mUploadResult != null)
                        mUploadResult.success(GlobalConstants.BASE_QI_NIU_URL+"/"+ fileName);
                } else {
                    uploadFail("文件上传失败，请稍后再试！");
                }

                Log.i("qiniu", key + ",\r\n " + info);
            }
        }, null);
    }

    /**
     * 上传失败
     */
    private void uploadFail(String info) {
        if (mUploadResult != null) mUploadResult.fail();
     //   Toast.makeText(MyApplication.getContext(), info, Toast.LENGTH_SHORT).show();
    }

    public interface UploadResult {
        /**
         * 上传成功
         */
        void success(String path);

        /**
         * 长传失败
         */
        void fail();
    }


    /**
     * 压缩上传路径
     * @param path
     * @return
     */
    public String compressImageUpload(String path) {
        String filename = path.substring(path.lastIndexOf("/") + 1);
        Bitmap image = getImage(path);
        return saveMyBitmap(filename, image);
    }

    /**
     * 图片按比例大小压缩方法（根据路径获取图片并压缩）
     *
     * @param srcPath
     * @return
     */
    private Bitmap getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 将压缩的bitmap保存到SDCard卡临时文件夹，用于上传
     *
     * @param filename
     * @param bit
     * @return
     */
    private String saveMyBitmap(String filename, Bitmap bit) {
        String baseDir = EmergencyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()+"/121急救/";
        String filePath = baseDir + filename;
        File dir = new File(baseDir);
        if (!dir.exists()) {
            dir.mkdir();
        }

        File f = new File(filePath);
        try {
            f.createNewFile();
            FileOutputStream fOut = null;
            fOut = new FileOutputStream(f);
            bit.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return filePath;
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }
}
