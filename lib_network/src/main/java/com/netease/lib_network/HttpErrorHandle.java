package com.netease.lib_network;


import io.reactivex.Single;
import io.reactivex.functions.Function;

public class HttpErrorHandle<T> implements Function<Throwable, Single<T>> {
    @Override
    public Single<T> apply(Throwable throwable) {
        return Single.error(ExceptionHandle.handleException(throwable));
    }
}
