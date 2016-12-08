package com.example.wyz.everynews1.mvp.presenter.impl;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.example.wyz.everynews1.MyApp;
import com.example.wyz.everynews1.R;
import com.example.wyz.everynews1.common.LoadPhotoType;
import com.example.wyz.everynews1.common.PhotoRequestType;
import com.example.wyz.everynews1.listener.RequestCallBack;
import com.example.wyz.everynews1.mvp.interfactor.impl.PhotoDetailInteractorImpl;
import com.example.wyz.everynews1.mvp.presenter.PhotoDetailPresenter;
import com.example.wyz.everynews1.mvp.presenter.base.BasePresenterImpl;
import com.example.wyz.everynews1.mvp.view.PhotoDetailView;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by Wyz on 2016/11/19.
 */
public class PhotoDetailPresenterImpl  extends BasePresenterImpl<PhotoDetailView,Uri>
    implements  PhotoDetailPresenter,RequestCallBack<Uri>{

    private PhotoDetailInteractorImpl mPhotoInteractor;
    private Activity mActivity;
    private  int mRequestType=-1;
    @Inject
    public PhotoDetailPresenterImpl(PhotoDetailInteractorImpl photoDetailInteractor,Activity activity) {
        mPhotoInteractor=photoDetailInteractor;
        mActivity=activity;
    }


    @Override
    public void handlePicture(String imageUrl, @LoadPhotoType.PhotoRequestTypeChecker int type) {
        mRequestType=type;
        mPhotoInteractor.loadPicture(this,imageUrl);
    }

    @Override
    public void success(Uri imageUri) {
        super.success(imageUri);
        switch (mRequestType){
            case PhotoRequestType.TYPE_SHARE:
                share(imageUri);
                break;
            case PhotoRequestType.TYPE_SAVE:
                showSavePathMsg(imageUri);
                break;
            case PhotoRequestType.TYPE_SET_WALLPAPER:
                setWallpaper(imageUri);
                break;
        }
    }

    private void setWallpaper(Uri imageUri) {
        WallpaperManager wallpaperManager=WallpaperManager.getInstance(mActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            File wallpaperFile = new File(imageUri.getPath());
            Uri contentURI = getImageContentUri(mActivity, wallpaperFile.getAbsolutePath());
//                    Uri uri1 = getImageContentUri(mActivity, imageUri.getPath());
            mActivity.startActivity(wallpaperManager.getCropAndSetWallpaperIntent(contentURI));
        } else {
            try {
                wallpaperManager.setStream(mActivity.getContentResolver().openInputStream(imageUri));
                mView.showMsg(MyApp.getAppContext().getString(R.string.set_wallpaper_success));
            } catch (IOException e) {

                mView.showMsg(e.getMessage());
            }
        }
    }

    private Uri getImageContentUri(Context context, String absolutePath) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , new String[]{MediaStore.Images.Media._ID}
                , MediaStore.Images.Media.DATA + "=? "
                , new String[]{absolutePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(id));

        } else if (!absolutePath.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, absolutePath);
            return context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            return null;
        }
    }

    private void showSavePathMsg(Uri imageUri) {
        mView.showMsg(mActivity.getString(R.string.picture_already_save_to, imageUri.getPath()));
    }

    private void share(Uri imageUri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.setType("image/jpeg");
        mActivity.startActivity(Intent.createChooser(intent, MyApp.getAppContext().getString(R.string.share)));
    }


}
