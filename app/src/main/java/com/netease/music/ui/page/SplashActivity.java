package com.netease.music.ui.page;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.imooc.lib_common_ui.utils.SharePreferenceUtil;
import com.kunminx.architecture.utils.BarUtils;
import com.kunminx.architecture.utils.Utils;
import com.netease.music.MainActivity;
import com.netease.music.R;
import com.netease.music.ui.page.login.LoginActivity;
import com.netease.music.util.MusicUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import pub.devrel.easypermissions.EasyPermissions;


public class SplashActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);

        setContentView(R.layout.delegate_splash);
        String[] perms = {Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "请打开相关权限", 1, perms);
        } else {
            jumpIntoMainActivity();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        jumpIntoMainActivity();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    private void jumpIntoMainActivity() {
        Disposable subscribe = Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(aLong -> {
                    //初始化本地音乐数量  在授予权限后在初始化 避免出现未授权异常
                    if (SharePreferenceUtil.getInstance(Utils.getApp()).getLocalMusicCount() == -1) {
                        SharePreferenceUtil.getInstance(Utils.getApp()).saveLocalMusicCount(MusicUtils.queryMusicSize(Utils.getApp(), MusicUtils.START_FROM_LOCAL));
                    }
                    String authToken = SharePreferenceUtil.getInstance(Utils.getApp()).getAuthToken("");
                    if (TextUtils.isEmpty(authToken)) {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        //进入主界面
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                });
    }
}

