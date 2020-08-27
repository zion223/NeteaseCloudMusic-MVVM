package com.netease.music.ui.base.binding_adapter;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.netease.lib_image_loader.image.CornerTransform;
import com.netease.lib_image_loader.image.CustomRequestListener;
import com.netease.lib_image_loader.image.ImageUtils;
import com.netease.music.R;

import java.io.File;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

public class ImageBindingAdapter {

    @BindingAdapter(value = {"imageRes"}, requireAll = false)
    public static void setImageRes(ImageView imageView, int imageRes) {
        imageView.setImageResource(imageRes);
    }

    @BindingAdapter(value = {"imageRes"}, requireAll = false)
    public static void setImageRes(ImageView imageView, Drawable imageRes) {
        imageView.setImageDrawable(imageRes);
    }

    @BindingAdapter(value = {"showDrawable", "drawableShowed"}, requireAll = false)
    public static void showDrawable(ImageView view, boolean showDrawable, int drawableShowed) {
        view.setImageResource(showDrawable ? drawableShowed : android.R.color.transparent);
    }

    /**
     * 加载ImageView的图片
     */
    @BindingAdapter(value = {"imageUrl", "placeHolder"}, requireAll = false)
    public static void displayImageForView(ImageView view, String url, Drawable placeHolder) {
        Glide.with(view.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .placeholder(placeHolder)
                .transition(withCrossFade())
                .into(view);

    }

    public static File getImageFile(Context context, String url) throws ExecutionException, InterruptedException {
        return Glide.with(context)
                .downloadOnly()
                .load(url)
                .submit()
                .get();

    }

    /**
     * 加载圆形图片
     */
    @BindingAdapter(value = {"circleImageUrl"}, requireAll = false)
    public static void displayImageForCircle(final ImageView imageView, final String url) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(final Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(imageView.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }


    /**
     * 加载圆角图片
     *
     * @param imageView
     * @param url
     */
    @BindingAdapter(value = {"cornerImageUrl", "imgCorner"}, requireAll = false)
    public static void displayImageForCorner(final ImageView imageView, String url, int corner) {
        if (corner == 0) corner = 5;
        CornerTransform transformation = new CornerTransform(imageView.getContext(), ImageUtils.dip2px(imageView.getContext(), corner));
        transformation.setExceptCorner(false, false, false, false);
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .transform(transformation)
                .into(imageView);
    }

    @BindingAdapter(value = {"imageUrl", "radis"}, requireAll = false)
    public static void displayImageForViewGroup(final ViewGroup group, String url, final int radius) {
        Glide.with(group.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .into(new SimpleTarget<Bitmap>() {//设置宽高
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        final Bitmap res = resource;
                        Disposable subscribe = Observable.just(resource)
                                .map(new Function<Bitmap, Drawable>() {
                                    @Override
                                    public Drawable apply(Bitmap bitmap) {
                                        return new BitmapDrawable(
                                                //毛玻璃效果
                                                ImageUtils.doBlur(res, radius, true)
                                        );
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Drawable>() {
                                    @Override
                                    public void accept(Drawable drawable) {
                                        group.setBackground(drawable);
                                    }
                                });
                    }
                });
    }

    @BindingAdapter(value = {"imageUrl", "radis"}, requireAll = false)
    public static void displayImageForImageView(final ImageView view, String url, final int radius) {
        Glide.with(view.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .into(new SimpleTarget<Bitmap>() {//设置宽高
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        final Bitmap res = resource;
                        Disposable subscribe = Observable.just(resource)
                                .map(new Function<Bitmap, Drawable>() {
                                    @Override
                                    public Drawable apply(Bitmap bitmap) {
                                        //毛玻璃效果
                                        return new BitmapDrawable(ImageUtils.doBlur(res, radius, true));
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Drawable>() {
                                    @Override
                                    public void accept(Drawable drawable) {
                                        view.setBackground(drawable);
                                    }
                                });
                    }
                });
    }

    public static void displayImageForNotification(Context context, RemoteViews rv, int id,
                                                   Notification notification, int NOTIFICATION_ID, String url) {
        displayImageForTarget(context,
                initNotificationTarget(context, id, rv, notification, NOTIFICATION_ID), url);
    }

    /*
     * 初始化Notification Target
     */
    private static NotificationTarget initNotificationTarget(Context context, int id, RemoteViews rv,
                                                             Notification notification, int NOTIFICATION_ID) {
        return new NotificationTarget(context, id, rv, notification, NOTIFICATION_ID);
    }

    private static void displayImageForTarget(Context context, Target target, String url) {
        displayImageForTarget(context, target, url, null);
    }

    /**
     * 为非view加载图片
     */
    private static void displayImageForTarget(Context context, Target target, String url,
                                              CustomRequestListener requestListener) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .transition(withCrossFade())
                .fitCenter()
                .listener(requestListener)
                .into(target);
    }

    @SuppressLint("CheckResult")
    private static RequestOptions initCommonRequestOption() {

        return new RequestOptions()
                //.placeholder(R.mipmap.ic_album_demo)//loading时显示的图片
                .error(R.mipmap.ic_album_demo)//load失败时显示的图片
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)//缓存策略
                .skipMemoryCache(false)//使用内存缓存
                .priority(Priority.NORMAL);
    }

}
