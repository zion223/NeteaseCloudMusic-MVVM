package com.netease.lib_network;


import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class HttpErrorHandle<T> implements Function<Throwable, Observable<T>> {
    @Override
    public Observable<T> apply(Throwable throwable) {
        return Observable.error(ExceptionHandle.handleException(throwable));
    }
}
