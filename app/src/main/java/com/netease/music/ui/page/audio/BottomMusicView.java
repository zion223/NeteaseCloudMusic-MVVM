package com.netease.music.ui.page.audio;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lxj.xpopup.XPopup;
import com.netease.lib_api.model.song.AudioBean;
import com.netease.lib_audio.mediaplayer.core.AudioController;
import com.netease.lib_audio.mediaplayer.events.AudioLoadEvent;
import com.netease.lib_audio.mediaplayer.events.AudioPauseEvent;
import com.netease.lib_audio.mediaplayer.events.AudioProgressEvent;
import com.netease.lib_audio.mediaplayer.events.AudioStartEvent;
import com.netease.lib_common_ui.utils.SharePreferenceUtil;
import com.netease.lib_common_ui.widget.CircleProgressButton;
import com.netease.lib_image_loader.app.ImageLoaderManager;
import com.netease.music.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 播放器底部View
 * 接收各种播放器相关事件
 */
public class BottomMusicView extends RelativeLayout {

    private final Context mContext;

    /*
     * View
     */
    private ImageView mLeftView;
    private TextView mTitleView;
    private TextView mAlbumView;  //TODO 歌曲播放时 显示滚动的歌词
    private ImageView mRightView;
    private CircleProgressButton mPlayView;

    /*
     * data
     */
    private AudioBean mAudioBean;

    /*
     * animation
     */
    private ObjectAnimator mRotateAnimator;

    public BottomMusicView(Context context) {
        this(context, null);
    }

    public BottomMusicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomMusicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(getResources().getColor(R.color.white));
        mContext = context;
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.bottom_view, this);
        // 当前没有歌曲 那么不展示底部的菜单栏
        if (AudioController.getInstance().getNowPlaying() == null) {
            setVisibility(GONE);
        }
        rootView.setOnClickListener(v -> {
            //跳到音乐播放Activitity
            if (AudioController.getInstance().getNowPlaying() != null) {
                MusicPlayerActivity.start((Activity) mContext);
            } else {
                Toast.makeText(getContext(), "当前播放队列无歌曲", Toast.LENGTH_SHORT).show();
            }
        });
        mLeftView = rootView.findViewById(R.id.album_view);
        //属性动画 旋转动画
        mRotateAnimator = ObjectAnimator.ofFloat(mLeftView, View.ROTATION.getName(), 0f, 360f);
        mRotateAnimator.setDuration(10000);
        mRotateAnimator.setInterpolator(new LinearInterpolator());
        mRotateAnimator.setRepeatMode(ObjectAnimator.RESTART);// 循环模式
        mRotateAnimator.setRepeatCount(ObjectAnimator.INFINITE);

        mTitleView = rootView.findViewById(R.id.audio_name_view);
        mAlbumView = rootView.findViewById(R.id.audio_album_view);
        mPlayView = rootView.findViewById(R.id.play_view);
        mPlayView.setOnClickListener(v -> {
            //处理播放暂停事件
            if (AudioController.getInstance().getQueue().size() != 0) {
                AudioController.getInstance().playOrPause();
            } else {
                Toast.makeText(getContext(), "当前播放队列没有歌曲", Toast.LENGTH_SHORT).show();
            }
        });
        mRightView = rootView.findViewById(R.id.show_list_view);
        mRightView.setOnClickListener(v -> {
            //显示音乐列表对话框
            if (AudioController.getInstance().getQueue().size() != 0) {
                new XPopup.Builder(getContext())
                        .asCustom(new MusicListDialog(getContext()))
                        .show();
            } else {
                Toast.makeText(getContext(), "当前播放队列没有歌曲", Toast.LENGTH_SHORT).show();
            }

        });
        //显示上次退出时播放的歌曲
        mAudioBean = SharePreferenceUtil.getInstance(mContext).getLatestSong();
        if (mAudioBean != null) {
            mTitleView.setText(mAudioBean.getName());
            mAlbumView.setText(mAudioBean.getAlbum());
            ImageLoaderManager.getInstance().displayImageForCircle(mLeftView, mAudioBean.getAlbumPic());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioLoadEvent(AudioLoadEvent event) {
        //更新当前view为load状态
        mAudioBean = event.mAudioBean;
        showLoadView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioPauseEvent(AudioPauseEvent event) {
        //更新当前view为暂停状态
        showPauseView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioStartEvent(AudioStartEvent event) {
        //更新当前view为播放状态
        showPlayView();
        setVisibility(VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioProgrssEvent(AudioProgressEvent event) {
        //更新当前view的播放进度
        mPlayView.setProgressValue(((float) event.progress / (float) event.maxLength) * 100);
    }

    //加载状态
    private void showLoadView() {
        //目前loading状态的UI处理与pause逻辑一样，分开为了以后好扩展
        if (mAudioBean != null) {
            ImageLoaderManager.getInstance().displayImageForCircle(mLeftView, mAudioBean.getAlbumPic());
            mTitleView.setText(mAudioBean.getName());
            mAlbumView.setText(mAudioBean.getAlbum());
            mPlayView.setPlayOrPause(CircleProgressButton.Status.PLAY);
        }
    }

    //暂停状态
    private void showPauseView() {
        if (mAudioBean != null) {
            mPlayView.setPlayOrPause(CircleProgressButton.Status.PAUSE);
        }
        mRotateAnimator.pause();
    }

    //播放状态
    private void showPlayView() {
        if (mAudioBean != null) {
            mPlayView.setPlayOrPause(CircleProgressButton.Status.PLAY);
        }
        if (mRotateAnimator.isPaused()) {
            mRotateAnimator.resume();
        } else {
            mRotateAnimator.start();
        }
    }
}
