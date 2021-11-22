package com.netease.lib_network;


import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Describe:
 * <p></p>
 *
 * @author zhangruiping
 * @Date 2021/11/22
 */
public abstract class SimpleObserver<T> implements Observer<T> {


    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull T t) {
        onSuccess(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        onFailure(e);
    }

    @Override
    public void onComplete() {

    }

    protected abstract void onSuccess(T result);

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
