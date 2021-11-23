package com.netease.lib_network;


import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Describe:
 * <p></p>
 *
 * @author zhangruiping
 * @Date 2021/11/22
 */
public abstract class SimpleObserver<T> implements SingleObserver<T> {
    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onSuccess(@NonNull T t) {

    }

    @Override
    public void onError(@NonNull Throwable e) {
        onFailure(e);
    }

    protected abstract void onFailed(ExceptionHandle.ResponseThrowable errorMsg);

    protected void onFailure(Throwable e) {
        if (e instanceof ExceptionHandle.ResponseThrowable) {
            ExceptionHandle.ResponseThrowable throwable = (ExceptionHandle.ResponseThrowable) e;
            onFailed(throwable);
        } else {
            e.printStackTrace();
        }
    }
}
