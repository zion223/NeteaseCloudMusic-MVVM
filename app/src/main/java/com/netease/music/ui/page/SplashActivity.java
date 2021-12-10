package com.netease.music.ui.page;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kunminx.architecture.utils.BarUtils;
import com.kunminx.architecture.utils.Utils;
import com.netease.lib_common_ui.utils.SharePreferenceUtil;
import com.netease.lib_network.ApiEngine;
import com.netease.lib_network.ExceptionHandle;
import com.netease.lib_network.SimpleObserver;
import com.netease.music.MainActivity;
import com.netease.music.R;
import com.netease.music.ui.page.login.LoginActivity;
import com.netease.music.util.MusicUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 闪屏页
 * @author Zhangruiping
 */
public class SplashActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {


    private static final int REQUEST_CODE = 1;

    private static final String[] PERMS = {Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Disposable timerDisposable;
    private Observable<Long> timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(null);
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);

        setContentView(R.layout.delegate_splash);
        timer = Observable.timer(2, TimeUnit.SECONDS);
        if (!EasyPermissions.hasPermissions(this, PERMS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.reuqest_permission), REQUEST_CODE, PERMS);
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
        if (requestCode == REQUEST_CODE && !EasyPermissions.hasPermissions(this, PERMS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.reuqest_permission), 1, PERMS);
        } else {
            jumpIntoMainActivity();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perm) {
        if (requestCode == REQUEST_CODE && EasyPermissions.somePermissionPermanentlyDenied(this, perm)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    private void jumpIntoMainActivity() {
        ApiEngine.getInstance().getApiService().checkNetwork()
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(new SimpleObserver<ResponseBody>() {
                    @Override
                    public void onSuccess(@NotNull ResponseBody result) {
                        timerDisposable = timer.subscribe(aLong -> {
                            //初始化本地音乐数量  在授予权限后在初始化 避免出现未授权异常
                            final SharePreferenceUtil preferenceUtil = SharePreferenceUtil.getInstance(Utils.getApp());
                            if (preferenceUtil.getLocalMusicCount(-1) == -1) {
                                preferenceUtil.saveLocalMusicCount(MusicUtils.queryMusicSize(Utils.getApp(), MusicUtils.START_FROM_LOCAL));
                            }
                            // 根据token选择跳转Activity
                            startActivity(new Intent(SplashActivity.this,
                                    TextUtils.isEmpty(preferenceUtil.getAuthToken("")) ? LoginActivity.class : MainActivity.class));
                            finish();
                        });
                    }

                    @Override
                    protected void onFailed(ExceptionHandle.ResponseThrowable errorMsg) {
                        Toast.makeText(SplashActivity.this, errorMsg.message + getString(R.string.check_network), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerDisposable != null && !timerDisposable.isDisposed()) {
            timerDisposable.dispose();
        }
    }
}

