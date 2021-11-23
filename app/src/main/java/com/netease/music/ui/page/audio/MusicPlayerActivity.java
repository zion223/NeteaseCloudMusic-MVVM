package com.netease.music.ui.page.audio;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.kunminx.architecture.utils.BarUtils;
import com.lxj.xpopup.XPopup;
import com.netease.lib_api.model.song.AudioBean;
import com.netease.lib_audio.mediaplayer.core.AudioController;
import com.netease.lib_audio.mediaplayer.core.CustomMediaPlayer;
import com.netease.lib_audio.mediaplayer.core.PlayMode;
import com.netease.lib_audio.mediaplayer.events.AudioBufferUpdateEvent;
import com.netease.lib_audio.mediaplayer.events.AudioLoadEvent;
import com.netease.lib_audio.mediaplayer.events.AudioPauseEvent;
import com.netease.lib_audio.mediaplayer.events.AudioPlayModeEvent;
import com.netease.lib_audio.mediaplayer.events.AudioProgressEvent;
import com.netease.lib_audio.mediaplayer.events.AudioStartEvent;
import com.netease.lib_audio.mediaplayer.view.IndictorView;
import com.netease.lib_common_ui.lrc.LrcView;
import com.netease.lib_common_ui.utils.SharePreferenceUtil;
import com.netease.lib_network.ApiEngine;
import com.netease.lib_network.ApiService;
import com.netease.music.R;
import com.netease.music.data.config.TypeEnum;
import com.netease.music.ui.page.discover.square.detail.CommentActivity;
import com.netease.music.util.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;


public class MusicPlayerActivity extends AppCompatActivity {

    private static final String TAG = "MusicPlayerActivity";
    private RelativeLayout mBgView;
    private TextView mInfoView;
    private TextView mAuthorView;

    private ImageView mFavouriteView;
    private ImageView mCommectView;

    private SeekBar mProgressView;
    private TextView mStartTimeView;
    private TextView mTotalTimeView;
    private TextView mCommentNum;

    private ImageView mPlayModeView;
    private ImageView mPlayView;
    private ImageView mNextView;
    private ImageView mPreViousView;
    private ImageView mNeddleiew;
    private IndictorView mIndictorView;

    private LinearLayout mLlOpreationView;

    private LrcView lrcView;
    private Animator mFavAnimator;
    private ObjectAnimator mNeddlePlayAnimator;
    private ObjectAnimator mNeddlePauseAnimator;

    /**
     * data
     */
    private AudioBean mAudioBean; //当前正在播放歌曲
    private PlayMode mPlayMode;//当前播放模式
    private List<String> likeList = new ArrayList<>(); //当前喜欢的音乐ID集合

    /**
     * 网络请求service
     */
    private ApiService apiService;

    //从外部启动该Activity
    public static void start(Activity context) {
        Intent intent = new Intent(context, MusicPlayerActivity.class);
        //context为Activity类型
        ActivityCompat.startActivity(context, intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(context).toBundle());

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);
        setContentView(R.layout.activity_music_service_layout);
        //使用转场动画 从底部弹出和退出
        getWindow().setEnterTransition(
                TransitionInflater.from(this).inflateTransition(R.transition.transition_bottom2top));
        EventBus.getDefault().register(this);
        apiService = ApiEngine.getInstance().getApiService();
        initData();
        initView();
        initAnimator();
    }

    private void initAnimator() {

        //播放动作 动画
        mNeddlePlayAnimator = ObjectAnimator.ofFloat(mNeddleiew, "rotation", -25, 0);
        mNeddlePlayAnimator.setDuration(200);
        mNeddlePlayAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mNeddlePlayAnimator.setRepeatCount(0);
        mNeddlePlayAnimator.setInterpolator(new LinearInterpolator());
        //暂停播放动作 动画
        mNeddlePauseAnimator = ObjectAnimator.ofFloat(mNeddleiew, "rotation", 0, -25);
        mNeddlePauseAnimator.setDuration(200);
        mNeddlePauseAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mNeddlePauseAnimator.setRepeatCount(0);
        mNeddlePauseAnimator.setInterpolator(new LinearInterpolator());
    }

    private void initView() {
        mBgView = findViewById(R.id.root_layout);
        mCommentNum = findViewById(R.id.tv_comment_num);
        //歌词
        lrcView = findViewById(R.id.lrc_view);
        lrcView.setOnSingerClickListener(() -> {
            //隐藏歌词布局
            lrcView.setVisibility(View.GONE);
            mNeddleiew.setVisibility(View.VISIBLE);
            mIndictorView.setVisibility(View.VISIBLE);
            mLlOpreationView.setVisibility(View.VISIBLE);
        });

        //拖动歌词条
        lrcView.setDraggable(true, time -> {
            AudioController.getInstance().seekTo(time);
            return true;
        });
        mLlOpreationView = findViewById(R.id.operation_view);
        //背景虚化图
        //ImageLoaderManager.getInstance().displayImageForViewGroup(mBgView, mAudioBean.getAlbumPic(), 100);
        //返回按钮
        findViewById(R.id.back_view).setOnClickListener(v -> onBackPressed());

        //分享
        findViewById(R.id.share_view).setOnClickListener(v -> shareMusic(mAudioBean.getUrl(), mAudioBean.getName()));
        //显示歌曲列表 Dialog
        findViewById(R.id.show_list_view).setOnClickListener(v -> new XPopup.Builder(MusicPlayerActivity.this)
                .asCustom(new MusicListDialog(MusicPlayerActivity.this))
                .show());
        //歌曲信息
        mInfoView = findViewById(R.id.album_view);
        mInfoView.setText(mAudioBean.getName());
        //跑马灯效果焦点获取
        mInfoView.requestFocus();
        //歌曲作者
        mAuthorView = findViewById(R.id.author_view);
        mAuthorView.setText(mAudioBean.getAuthor());
        //唱针
        mNeddleiew = findViewById(R.id.needle);
        //防止执行动画时被遮挡
        mNeddleiew.bringToFront();
        //评论
        mCommectView = findViewById(R.id.iv_comment);
        mCommectView.setOnClickListener(v -> CommentActivity.startActivity(MusicPlayerActivity.this, mAudioBean.getId(), TypeEnum.SONG_ID, mAudioBean.getAlbumPic(), mAudioBean.getAuthor(), mAudioBean.getName()));
        //喜欢音乐
        mFavouriteView = findViewById(R.id.favourite_view);
        //设置喜欢标志为 默认为false
        mFavouriteView.setTag(false);
        //收藏与否
        mFavouriteView.setOnClickListener(v -> changeFavouriteStatus());
        //是否喜欢该音乐
        loadFavouriteStatus();
        mStartTimeView = findViewById(R.id.start_time_view);
        mTotalTimeView = findViewById(R.id.total_time_view);
        mTotalTimeView.setText(mAudioBean.getTotalTime());
        mProgressView = findViewById(R.id.progress_view);
        mProgressView.setProgress(0);
        //seekBar 可拖动
        mProgressView.setEnabled(true);
        mProgressView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            //松开的时候
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //歌曲播放进度
                AudioController.getInstance().seekTo(seekBar.getProgress());

            }
        });
        mPlayModeView = findViewById(R.id.play_mode_view);
        mPlayModeView.setOnClickListener(v -> {
            //切换播放模式 LOOP --> RANDOM --> REPEAT --> LOOP
            switch (mPlayMode) {
                case LOOP:
                    AudioController.getInstance().setPlayMode(PlayMode.RANDOM);
                    break;
                case RANDOM:
                    AudioController.getInstance().setPlayMode(PlayMode.REPEAT);
                    break;
                case REPEAT:
                    AudioController.getInstance().setPlayMode(PlayMode.LOOP);
                    break;
                default:
                    break;
            }
        });

        mPreViousView = findViewById(R.id.previous_view);
        mPreViousView.setOnClickListener(v -> AudioController.getInstance().previous());
        mPlayView = findViewById(R.id.play_view);
        mPlayView.setOnClickListener(v -> AudioController.getInstance().playOrPause());
        mNextView = findViewById(R.id.next_view);
        mNextView.setOnClickListener(v -> AudioController.getInstance().next());
        mIndictorView = findViewById(R.id.indictor_view);
        mIndictorView.setListaner(new IndictorView.OnIndicatorViewStatusChangeListener() {
            @Override
            public void onDrag() {
                //当前唱针处于播放状态时 唱针回退到初始状态
                showPauseAnimator();
            }

            @Override
            public void onIdle() {
                //空闲状态时
                showPlayAnimator();
            }

            @Override
            public void onSingleTouch(MotionEvent event) {

                //显示歌词布局
                lrcView.setVisibility(View.VISIBLE);
                mIndictorView.setVisibility(View.GONE);
                mNeddleiew.setVisibility(View.GONE);
                mLlOpreationView.setVisibility(View.GONE);
            }
        });
        //ViewPager转动时唱针收起

        //播放模式初始化
        updatePlayModeView();
        //唱针位置、播放按钮初始化
        updateStateView();
    }

    private void updateStateView() {
        //还没进去Activity前 歌曲已经播放
        if (AudioController.getInstance().isStartState()) {
            mPlayView.setImageResource(R.mipmap.audio_aj6);
            mNeddleiew.setRotation(0);
        } else {
            mPlayView.setImageResource(R.mipmap.audio_aj7);
            mNeddleiew.setRotation(-25);
        }
    }

    private void updatePlayModeView() {
        switch (mPlayMode) {
            case LOOP:
                mPlayModeView.setImageResource(R.mipmap.ic_player_loop);
                break;
            case RANDOM:
                mPlayModeView.setImageResource(R.mipmap.ic_player_random);
                break;
            case REPEAT:
                mPlayModeView.setImageResource(R.mipmap.ic_player_once);
                break;
            default:
                break;
        }
    }

    //是否喜欢该音乐
    void loadFavouriteStatus() {
        Disposable subscribe = apiService.getLikeList(SharePreferenceUtil.getInstance(getBaseContext()).getUserId())
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(likeListBean -> {
                    likeList = likeListBean.getIds();
                    boolean likeMusic = false;
                    for (String id : likeList) {
                        if (mAudioBean.getId().equals(id)) {
                            likeMusic = true;
                            break;
                        }
                    }
                    //设置红心
                    mFavouriteView.setImageResource(likeMusic ? R.mipmap.audio_aeh : R.mipmap.audio_aef);
                    //已喜欢音乐标识
                    mFavouriteView.setTag(likeMusic);
                });
    }

    //喜欢或取消喜欢该音乐
    private void changeFavouriteStatus() {
        final boolean liked = (boolean) mFavouriteView.getTag();
        Disposable subscribe = apiService.likeMusic(mAudioBean.getId(), !liked)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(likeMusicBean -> {
                    if (likeMusicBean.getCode() == 200) {
                        mFavouriteView.setImageResource(liked ? R.mipmap.audio_aef : R.mipmap.audio_aeh);
                        mFavouriteView.setTag(!liked);
                        if (mFavAnimator != null)
                            mFavAnimator.end();
                        //缩放	SCALE_X、SCALE_Y   1.0 -> 1.2 -> 1.0 动画
                        PropertyValuesHolder animX =
                                PropertyValuesHolder.ofFloat(View.SCALE_X.getName(), 1.0f, 1.2f, 1.0f);
                        PropertyValuesHolder animY =
                                PropertyValuesHolder.ofFloat(View.SCALE_Y.getName(), 1.0f, 1.2f, 1.0f);
                        //属性动画
                        mFavAnimator = ObjectAnimator.ofPropertyValuesHolder(mFavouriteView, animX, animY);
                        //持续加速差值器
                        mFavAnimator.setInterpolator(new AccelerateInterpolator());
                        mFavAnimator.setDuration(300);
                        mFavAnimator.start();
                    }
                });
    }

    private void initData() {
        mAudioBean = AudioController.getInstance().getNowPlaying();
        mPlayMode = AudioController.getInstance().getPlayMode();
        //获取歌词
        Disposable subscribe = apiService.getLyric(mAudioBean.getId())
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(lyricBean -> lrcView.loadLrc(lyricBean.getLrc().getLyric(), lyricBean.getTlyric().getLyric()));

        //获取评论数量
        Disposable subscribecomment = apiService.getMusicComment(mAudioBean.getId())
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(bean -> mCommentNum.setText(bean.getTotal() > 1000 ? "999+" : String.valueOf(bean.getTotal())));

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioPlayModeEvent(AudioPlayModeEvent event) {
        mPlayMode = AudioController.getInstance().getPlayMode();
        updatePlayModeView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioStartEvent(AudioStartEvent event) {
        showPlayView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioPauseEvent(AudioPauseEvent event) {
        showPauseView();
    }

    //加载其他音乐 next pre
    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioLoadEvent(AudioLoadEvent event) {
        //更新notifacation为load状态
        mAudioBean = event.mAudioBean;
        //ImageLoaderManager.getInstance().displayImageForViewGroup(mBgView, mAudioBean.getAlbumPic(), 100);
        mInfoView.setText(mAudioBean.getAlbumInfo());
        mAuthorView.setText(mAudioBean.getAuthor());
        mStartTimeView.setText("00:00");
        mTotalTimeView.setText(mAudioBean.getTotalTime());
        //重新加载是否喜欢该音乐
        loadFavouriteStatus();
        mProgressView.setProgress(0);
        lrcView.updateTime(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioProgressEvent(AudioProgressEvent event) {
        int totalTime = event.maxLength;
        int currentTime = event.progress;
        //设置当前进度
        mProgressView.setProgress(currentTime);
        //设置最大时间
        mProgressView.setMax(totalTime);
        mStartTimeView.setText(TimeUtil.formatTime(currentTime));
        //更新歌词播放进度
        lrcView.updateTime(currentTime);
        //更新状态
        if (event.mStatus == CustomMediaPlayer.Status.PAUSED) {
            //showPauseView();
        } else {
            //showPlayView();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioBufferUpdateEvent(AudioBufferUpdateEvent event) {
        mProgressView.setSecondaryProgress(event.progress);
    }

    private void showPlayView() {
        mPlayView.setImageResource(R.mipmap.audio_aj6);
        showPlayAnimator();
    }


    private void showPauseView() {
        mPlayView.setImageResource(R.mipmap.audio_aj7);
        showPauseAnimator();
    }


    private void showPlayAnimator() {
        //唱针当前角度mNeddleiew.getRotation() : -25.0
        if (mNeddleiew.getRotation() == -25.0) {
            if (mNeddlePlayAnimator.isPaused()) {
                mNeddlePlayAnimator.resume();
            } else {
                mNeddlePlayAnimator.start();
            }
        } else {
            Log.e(TAG, "showPlayAnimator() 唱针状态不正常");
        }
    }

    private void showPauseAnimator() {
        //唱针当前角度mNeddleiew.getRotation() : 0
        if (mNeddleiew.getRotation() == 0) {
            mNeddlePauseAnimator.start();
        } else {
            Log.e(TAG, "showPauseAnimator() 唱针状态不正常");
        }
    }


    private void shareMusic(String url, String name) {
//		final ShareContentData data = ShareContentData.Builder()
//				.shareType(5)
//				.shareTitle(name)
//				.shareText("网易云音乐")
//				.shareTileUrl(url)
//				.shareSite("netease")
//				.shareSiteUrl("http://www.imooc.com")
//				.build();
//
//		ShareDialog dialog = new ShareDialog(this, false, data);
//		dialog.show();
    }

}
