package com.netease.music.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.netease.lib_audio.app.AudioHelper;
import com.netease.lib_audio.mediaplayer.core.AudioController;
import com.netease.lib_audio.mediaplayer.events.AudioFavouriteEvent;
import com.netease.lib_audio.mediaplayer.events.AudioLoadEvent;
import com.netease.lib_audio.mediaplayer.events.AudioPauseEvent;
import com.netease.lib_audio.mediaplayer.events.AudioReleaseEvent;
import com.netease.lib_audio.mediaplayer.events.AudioStartEvent;
import com.netease.music.R;
import com.netease.music.ui.page.audio.NotificationHelper;
import com.netease.music.util.AppCommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MusicService extends Service implements NotificationHelper.NotificationHelperListener {


    //actions
    private static final String ACTION_START = "ACTION_START";

    private NotificationReceiver mReceiver;

    public static void startMusicService() {
        if (!AppCommonUtils.isServiceRunning(MusicService.class.getName())) {
            Intent intent = new Intent(AudioHelper.getContext(), MusicService.class);
            intent.setAction(ACTION_START);
            AudioHelper.getContext().startForegroundService(intent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        registerBroadcastReceiver();
    }

    private void createNotificationChannel() {
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
        //Intent nfIntent = new Intent(this, XXXActivity.class); //点击后跳转的界面，可以设置跳转数据

        builder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0)) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher)) // 设置下拉列表中的图标(大图标)
                //.setContentTitle("SMI InstantView") // 设置下拉列表里的标题
                .setSmallIcon(R.drawable.ic_launcher) // 设置状态栏内的小图标
                .setContentText("云音乐服务") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

        /*以下是对Android 8.0的适配*/
        //普通notification适配
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("share_screen_id");
        }
        //前台服务notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("share_screen_id", "shareScreen", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        startForeground(110, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        if (intent != null) {
            if (ACTION_START.equals(intent.getAction())) {
                //初始化前台Notification
                NotificationHelper.getInstance().init(this);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unRegisterBroadcastReceiver();
    }

    private void registerBroadcastReceiver() {
        if (mReceiver == null) {
            mReceiver = new NotificationReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(NotificationReceiver.ACTION_STATUS_BAR);
            registerReceiver(mReceiver, filter);
        }
    }

    private void unRegisterBroadcastReceiver() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioLoadEvent(AudioLoadEvent event) {
        //更新notifacation为load状态
        NotificationHelper.getInstance().showLoadStatus(event.mAudioBean);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioPauseEvent(AudioPauseEvent event) {
        //更新notifacation为暂停状态
        NotificationHelper.getInstance().showPauseStatus();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioStartEvent(AudioStartEvent event) {
        //更新notifacation为播放状态
        NotificationHelper.getInstance().showPlayStatus();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioFavouriteEvent(AudioFavouriteEvent event) {
        //更新notifacation收藏状态
        NotificationHelper.getInstance().changeFavouriteStatus(event.isFavourite);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioReleaseEvent(AudioReleaseEvent event) {
        //移除notifacation
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    //绑定Notifiction
    @Override
    public void onNotificationInit() {
        //startForeground(NotificationHelper.NOTIFICATION_ID, NotificationHelper.getInstance().getNotification());
    }


    //通知栏消息接收器
    public static class NotificationReceiver extends BroadcastReceiver {

        public static final String ACTION_STATUS_BAR =
                AudioHelper.getContext().getPackageName() + ".NOTIFICATION_ACTIONS";
        public static final String EXTRA = "extra";
        public static final String EXTRA_PLAY = "play_pause";
        public static final String EXTRA_NEXT = "play_next";
        public static final String EXTRA_PRE = "play_previous";
        public static final String EXTRA_FAV = "play_favourite";
        public static final String EXTRA_CANCLE = "cancle_notification";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || TextUtils.isEmpty(intent.getAction())) {
                return;
            }
            String extra = intent.getStringExtra(EXTRA);
            if (!TextUtils.isEmpty(extra)) {
                switch (extra) {
                    case EXTRA_PLAY:
                        //处理播放暂停事件,可以封到AudioController中
                        AudioController.getInstance().playOrPause();
                        break;
                    case EXTRA_PRE:
                        AudioController.getInstance().previous(); //不管当前状态，直接播放
                        break;
                    case EXTRA_NEXT:
                        AudioController.getInstance().next();
                        break;
                    case EXTRA_FAV:
                        //AudioController.getInstance().changeFavourite();
                        break;
                    case EXTRA_CANCLE:
                        NotificationHelper.getInstance().getNotificationManager().cancel(NotificationHelper.NOTIFICATION_ID);
                    default:
                        break;
                }
            }

        }
    }
}
