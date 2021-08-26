package com.tjmedicine.emergency.model.takePhoto;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.orhanobut.logger.Logger;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;

/**
 * @author QiZai
 * @desc
 * @date 2018/5/24 17:35
 */

public class PhotoUtils {

    public static final int CODE_GALLERY_REQUEST = 0x11;
    private static final int CODE_CAMERA_REQUEST = 0x12;
    public static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x21;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x22;

    public static final int TYPE_AVATAR = 1;
    public static final int TYPE_NORMAL = 2;

    private static CropConfig config = new CropConfig();

    private static Uri imageUri;
    private static Uri mCameraUri;

    private static boolean sIsCrop = true;
    private static Activity mContext ;

    /**
     * 自动获取相机权限
     */
    public static void autoObtainCameraPermission(Activity context) {
        autoObtainCameraPermission(context, true);
    }

    private static String mCameraPhotoPath;

    /**
     * 自动获取相机权限
     */
    public static void autoObtainCameraPermission(Activity context, boolean isCrop) {
        sIsCrop = isCrop;
        mContext = context;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.CAMERA)) {
                Toast.makeText(context, "您已经拒绝过一次", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            openCamera(context);
        }
    }

    public static void openCamera(Activity context) {
        if (hasSdcard()) {
            String name = String.format("image-%d.jpg", System.currentTimeMillis());
            String path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath();
            //String path = Environment.getExternalStorageDirectory().getPath();
            File file = new File(path, name);
            mCameraPhotoPath = file.toString();
            mCameraUri = Uri.fromFile(file);
            //通过FileProvider创建一个content类型的Uri
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                imageUri = FileProvider.getUriForFile(context, "com.tjmedicine.emergency", file);
                Logger.d("hahah：111");
            } else {
                Logger.d("hahha：222");
                imageUri = mCameraUri;
            }

            Logger.d("mCameraUri："+mCameraUri);
            Logger.d("imageUri："+imageUri);
            takePicture(context, imageUri, CODE_CAMERA_REQUEST);

        } else {
            Toast.makeText(context, "设备没有SD卡！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 自动获取sdk权限
     */
    public static void autoObtainStoragePermission(Activity context) {
        autoObtainStoragePermission(context, true);
    }

    /**
     * 自动获取sdk权限
     */
    public static void autoObtainStoragePermission(Activity context, boolean isCrop) {
        sIsCrop = isCrop;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(context, CODE_GALLERY_REQUEST);
        }

    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    private static void setType(int type) {
        if (type == TYPE_AVATAR) {
            config.isOval = true;
            config.aspectRatioX = 1;
            config.aspectRatioY = 1;
            config.hideBottomControls = true;
            config.showGridLine = false;
            config.showOutLine = false;
            config.maxHeight = 400;
            config.maxWidth = 400;
        } else if (type == TYPE_NORMAL) {//什么都不用做
        } else {
        }
    }

    /**
     * @param activity    当前activity
     * @param imageUri    拍照后照片存储路径
     * @param requestCode 调用系统相机请求码
     */
    public static void takePicture(Activity activity, Uri imageUri, int requestCode) {
        if (config == null) config = new CropConfig();

        setType(TYPE_AVATAR);
        Intent intentCamera = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //将拍照结果保存至photo_file的Uri中，不保留在相册中
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intentCamera, requestCode);
    }

    /**
     * @param activity    当前activity
     * @param requestCode 打开相册的请求码
     */
    public static void openPic(Activity activity, int requestCode) {
        if (config == null) config = new CropConfig();

        setType(TYPE_AVATAR);

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(pickIntent, requestCode);

//        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        photoPickerIntent.setType("image/*");
//        activity.startActivityForResult(photoPickerIntent, requestCode);
    }

    /**
     * 图片裁切
     *
     * @param context
     * @param sourceUri
     */
    private static void startCropActivity(Activity context, Uri sourceUri) {
        String name = String.format("imagecrop-%d", System.currentTimeMillis()) + ".jpg";
//        File fileUri = new File(Environment.getExternalStorageDirectory().getPath(), name);
        File fileUri = new File(context.getCacheDir(), name);

        Uri mDestinationUri = Uri.fromFile(fileUri);
        UCrop uCrop = UCrop.of(sourceUri, mDestinationUri);
        uCrop.withAspectRatio(config.aspectRatioX, config.aspectRatioY);
        uCrop.withMaxResultSize(config.maxWidth, config.maxHeight);
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.NONE, UCropActivity.NONE);
        options.setCompressionQuality(config.quality);
        // options.setOvalDimmedLayer(config.isOval);
        options.setCircleDimmedLayer(config.isOval);
        options.setShowCropGrid(config.showGridLine);
        options.setHideBottomControls(config.hideBottomControls);
        options.setShowCropFrame(config.showOutLine);

        options.setToolbarColor(config.toolbarColor);
        options.setToolbarWidgetColor(config.toolbarWidgetColor);
        options.setStatusBarColor(config.statusBarColor);
        Logger.d("执行777");
        uCrop.withOptions(options);

        uCrop.start(context);
    }

    /**
     * 注意，调用时data为null的判断
     *
     * @param context
     * @param cropHandler
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public static void onActivityResult(int requestCode, int resultCode, Intent data, Activity context, CropHandler cropHandler) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CODE_GALLERY_REQUEST) {//第一次，选择图片后返回
                Logger.d("执行44444");
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    if (sIsCrop) startCropActivity(context, data.getData());
                    else noCropCallback(context, cropHandler, data.getData());
                } else {
                    Toast.makeText(context, "Cannot retrieve selected image", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {//第二次返回，图片已经剪切好
                Logger.d("执行55555");
                Uri finalUri = UCrop.getOutput(data);
                cropHandler.handleCropResult(finalUri, config.tag);

            } else if (requestCode == CODE_CAMERA_REQUEST) {//第一次，拍照后返回，因为设置了MediaStore.EXTRA_OUTPUT，所以data为null，数据直接就在uri中
                Logger.d("执行6666");
                if (sIsCrop) {
                    startCropActivity(context, imageUri);
                }
                else{
                    cropHandler.handleCropResult(mCameraUri, config.tag);
                }
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            cropHandler.handleCropError(data);
        }
    }

    /**
     * 不裁切图片时的回调
     *
     * @param context
     * @param sourceUri
     */
    private static void noCropCallback(Context context, CropHandler cropHandler, Uri sourceUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(sourceUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            cropHandler.handleCropResult(Uri.parse(cursor.getString(column_index)), config.tag);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }



    public static class CropConfig {
        public int aspectRatioX = 1;
        public int aspectRatioY = 1;
        public int maxWidth = 1080;
        public int maxHeight = 1920;

        // options
        public int tag;
        public boolean isOval = false;// 是否为椭圆
        public int quality = 80;

        public boolean hideBottomControls = true;   // 底部操作条
        public boolean showGridLine = true;         // 内部网格
        public boolean showOutLine = true;          // 最外面的矩形线

        public @ColorInt
        int toolbarColor = Color.parseColor("#0591F1"); // 标题栏背景色
        public @ColorInt
        int toolbarWidgetColor = Color.WHITE;           // 标题栏字体颜色
        public @ColorInt
        int statusBarColor = toolbarColor;           // 状态栏颜色


        public void setAspectRation(int x, int y) {
            this.aspectRatioX = x;
            this.aspectRatioY = y;
        }

        public void setMaxSize(int width, int height) {
            this.maxHeight = height;
            this.maxWidth = width;
        }

    }


    public interface CropHandler {
        void handleCropResult(Uri uri, int tag);

        void handleCropError(Intent data);
    }
}
