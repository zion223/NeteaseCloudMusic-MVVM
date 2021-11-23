package com.netease.music.ui.page.audio;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lxj.xpopup.core.CenterPopupView;
import com.netease.lib_api.model.user.UserPlaylistBean;
import com.netease.lib_common_ui.utils.SharePreferenceUtil;
import com.netease.lib_image_loader.app.ImageLoaderManager;
import com.netease.lib_network.ApiEngine;
import com.netease.music.R;

import java.util.List;

import io.reactivex.disposables.Disposable;

//收藏音乐到创建的歌单
public class MusicCollectDialog extends CenterPopupView {

    private Context mContext;
    //歌曲ID 从外部传入
    private String mTracks;
    private RecyclerView mRecyclerView;
    private PlayListAdapter mAdapter;

    public MusicCollectDialog(@NonNull Context context, String trackId) {
        super(context);
        this.mContext = context;
        this.mTracks = trackId;
    }

    public MusicCollectDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mRecyclerView = findViewById(R.id.recycler_playlist);
        //获取当前用户ID
        final int userId = SharePreferenceUtil.getInstance(mContext).getUserId();
        Disposable subscribe = ApiEngine.getInstance().getApiService().getUserPlaylist(userId)
                .compose(ApiEngine.getInstance().applySchedulers())
                .subscribe(userPlaylistBean -> {
                    List<UserPlaylistBean.PlaylistBean> playlist = userPlaylistBean.getPlaylist();
                    int subIndex = 0;
                    for (int i = 0; i < playlist.size(); i++) {
                        if (playlist.get(i).getCreator().getUserId() != userId) {
                            subIndex = i;
                            break;
                        }
                    }
                    //用户创建的歌单列表
                    List<UserPlaylistBean.PlaylistBean> createPlayList = playlist.subList(0, subIndex);
                    mAdapter = new PlayListAdapter(createPlayList);
                    mAdapter.setOnItemClickListener((adapter, view, position) -> {
                        UserPlaylistBean.PlaylistBean entity = (UserPlaylistBean.PlaylistBean) adapter.getItem(position);

                        Disposable subscribe1 = ApiEngine.getInstance().getApiService().trackPlayList(entity.getId(), mTracks, "add")
                                .compose(ApiEngine.getInstance().applySchedulers())
                                .subscribe(commonMessageBean -> {
                                    Toast.makeText(getContext(), commonMessageBean.getCode() == 200 ? "收藏成功" : "收藏失败或不支持收藏本地歌曲: " + commonMessageBean.getMsg(), Toast.LENGTH_SHORT).show();
                                    dismiss();
                                });
                    });
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                });

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_collect_music;
    }


    static class PlayListAdapter extends BaseQuickAdapter<UserPlaylistBean.PlaylistBean, BaseViewHolder> {

        PlayListAdapter(@Nullable List<UserPlaylistBean.PlaylistBean> data) {
            super(R.layout.dialog_collect_music_item, data);

        }

        @Override
        protected void convert(BaseViewHolder helper, UserPlaylistBean.PlaylistBean item) {
            helper.setText(R.id.tv_item_collect_content_toptext, item.getName());
            helper.setText(R.id.tv_item_collect_content_bottomtext, item.getTrackCount() + "首");
            ImageLoaderManager.getInstance().displayImageForCorner((ImageView) helper.getView(R.id.iv_item_collect_content_img), item.getCoverImgUrl(), 5);
        }
    }
}
