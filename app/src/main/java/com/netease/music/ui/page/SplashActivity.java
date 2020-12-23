package com.netease.music.ui.page;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.netease.lib_common_ui.utils.SharePreferenceUtil;
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
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class SplashActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    String[] perms = {Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);

        setContentView(R.layout.delegate_splash);

        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, getString(R.string.reuqest_permission), 1, perms);
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
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perm) {
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, getString(R.string.reuqest_permission), 1, perms);
        } else {
            jumpIntoMainActivity();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perm) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perm)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    private void jumpIntoMainActivity() {
        Disposable subscribe = Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(aLong -> {
                    //初始化本地音乐数量  在授予权限后在初始化 避免出现未授权异常
                    final SharePreferenceUtil preferenceUtil = SharePreferenceUtil.getInstance(Utils.getApp());
                    if (preferenceUtil.getLocalMusicCount(-1) == -1) {
                        preferenceUtil.saveLocalMusicCount(MusicUtils.queryMusicSize(Utils.getApp(), MusicUtils.START_FROM_LOCAL));
                    }
                    String authToken = preferenceUtil.getAuthToken("");
                    if (TextUtils.isEmpty(authToken)) {
                        //进入登录界面
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    } else {
                        //进入主界面
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                    finish();
                });
    }
}

