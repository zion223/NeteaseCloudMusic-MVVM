package com.netease.lib_network;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.kunminx.architecture.utils.Utils;
import com.netease.lib_network.interceptor.NetWorkInterceptor;
import com.netease.lib_network.interceptor.ResponseInterceptor;

import java.util.concurrent.TimeUnit;

import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求工具
 */
public class ApiEngine {
    private volatile static ApiEngine apiEngine;
    private Retrofit retrofit;

    private ApiEngine() {

        //添加网络拦截器
        NetWorkInterceptor netWorkInterceptor = new NetWorkInterceptor();
        //解析返回结果的Interceptor
        ResponseInterceptor responseInterceptor = new ResponseInterceptor();

        // okhttp的日志拦截器
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //缓存
//        int size = 1024 * 1024 * 100;
//        File cacheFile = new File(App.getContext().getCacheDir(), "OkHttpCache");
//        Cache cache = new Cache(cacheFile, size);

        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Utils.getApp()));

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addNetworkInterceptor(netWorkInterceptor)
                //.addInterceptor(logInterceptor)
                .addInterceptor(responseInterceptor)
                .cookieJar(cookieJar)
//                .cache(cache)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static ApiEngine getInstance() {
        if (apiEngine == null) {
            synchronized (ApiEngine.class) {
                if (apiEngine == null) {
                    apiEngine = new ApiEngine();
                }
            }
        }
        return apiEngine;
    }

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }

    public <T> SingleTransformer<T, T> applySchedulers() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .map(getAppErrorHandler())
                        .onErrorResumeNext(new HttpErrorHandle<T>())
                        .observeOn(AndroidSchedulers.mainThread());
    }


    private <T> Function<T, T> getAppErrorHandler() {
        return t -> t;
    }

}
