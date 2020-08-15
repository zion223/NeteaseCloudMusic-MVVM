package com.netease.music.ui.page;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.imooc.lib_common_ui.utils.SharePreferenceUtil;
import com.kunminx.architecture.utils.Utils;
import com.netease.music.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import pub.devrel.easypermissions.EasyPermissions;


public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delegate_splash);
        String[] perms = {Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "请打开相关权限", 1, perms);
        }
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        String authToken = SharePreferenceUtil.getInstance(Utils.getApp()).getAuthToken("");
                        if (TextUtils.isEmpty(authToken)) {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        } else {
                            //进入主界面
                            //startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}

