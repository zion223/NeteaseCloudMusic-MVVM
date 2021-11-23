package com.netease.music.ui.page.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.netease.lib_api.model.playlist.PlayListCommentEntity;
import com.netease.lib_api.model.song.MusicCommentBean;
import com.netease.lib_image_loader.app.ImageLoaderManager;
import com.netease.lib_network.ApiEngine;
import com.netease.music.R;
import com.netease.music.ui.page.discover.user.UserDetailActivity;
import com.netease.music.util.AnimUtil;
import com.netease.music.util.TimeUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class MultipleSectionGedanCommentAdapter extends BaseSectionQuickAdapter<PlayListCommentEntity, BaseViewHolder> {


    private ImageLoaderManager manager;

    //资源Id, 资源类型 ,context, 数据
    public MultipleSectionGedanCommentAdapter(String commentId, int type, Context context, List<PlayListCommentEntity> data) {
        super(R.layout.item_gedan_comment_header, R.layout.item_gedan_detail_comment, data);
        setNormalLayout(R.layout.item_gedan_detail_comment);
        manager = ImageLoaderManager.getInstance();
        //setOnItemChildClickListener 而不是setOnItemClickListener
        addChildClickViewIds(R.id.iv_item_gedan_comment_avatar_img, R.id.tv_item_gedan_comment_avatar_name, R.id.iv_item_gedan_comment_zan);
        setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.iv_item_gedan_comment_avatar_img:
                case R.id.tv_item_gedan_comment_avatar_name:
                    //点击进入用户详情
                    UserDetailActivity.startActivity(context, data.get(position).getComment().getUser().getUserId());
                    break;
                case R.id.iv_item_gedan_comment_zan:
                    //点赞
                    PlayListCommentEntity entity = (PlayListCommentEntity) adapter.getItem(position);
                    //点赞数量
                    TextView zanCountText = (TextView) adapter.getViewByPosition(position, R.id.tv_item_gedan_comment_zan_count);
                    ImageView praiseView = (ImageView) adapter.getViewByPosition(position, R.id.iv_item_gedan_comment_zan);
                    Boolean parise = (Boolean) praiseView.getTag();

                    Disposable subscribe = ApiEngine.getInstance().getApiService()
                            .likeComment(commentId, entity.getComment().getCommentId(), !parise ? 1 : 2, type)
                            .compose(ApiEngine.getInstance().applySchedulers())
                            .subscribe(commentLikeBean -> {
                                if (commentLikeBean.getCode() == 200) {
                                    //操作成功
                                    praiseView.setTag(!parise);
                                    praiseView.setImageResource(!parise ? R.drawable.ic_parise_red : R.drawable.ic_parise);
                                    zanCountText.setText(!parise ? String.valueOf(entity.getComment().getLikedCount() + 1) : String.valueOf(entity.getComment().getLikedCount()));
                                    zanCountText.setTextColor(!parise ? Color.parseColor("#FF3A3A") : Color.GRAY);
                                    Toast.makeText(context, !parise ? "点赞成功" : "取消赞成功", Toast.LENGTH_SHORT).show();
                                    if (!parise) {
                                        AnimUtil.getLikeAnim(praiseView).start();
                                    }
                                }
                            });
                    break;
                default:
                    break;

            }
        });
    }

    @Override
    protected void convertHeader(@NotNull BaseViewHolder baseViewHolder, @NotNull PlayListCommentEntity playListCommentEntity) {
        baseViewHolder.setText(R.id.tv_gedan_detail_comment_header_title, playListCommentEntity.getHeader());
        baseViewHolder.setText(R.id.tv_gedan_detail_comment_header_count, playListCommentEntity.getCount());
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, PlayListCommentEntity playListCommentEntity) {
        final MusicCommentBean.CommentsBean bean = playListCommentEntity.getComment();
        //用户头像
        ImageView avatarImg = baseViewHolder.getView(R.id.iv_item_gedan_comment_avatar_img);
        manager.displayImageForCircle(avatarImg, bean.getUser().getAvatarUrl());
        //用户昵称
        baseViewHolder.setText(R.id.tv_item_gedan_comment_avatar_name, bean.getUser().getNickname());
        //用户VIP类型
        if (bean.getUser().getVipType() == 11 && bean.getUser().getVipRights() != null) {
            if (bean.getUser().getVipRights().getRedVipAnnualCount() == 1) {
                //年费vip TODO 年vip 图片
                baseViewHolder.setVisible(R.id.iv_item_gedan_comment_avatar_vip, true);
            } else {
                //普通vip
                baseViewHolder.setVisible(R.id.iv_item_gedan_comment_avatar_vip, true);
            }
        }
        //评论时间
        baseViewHolder.setText(R.id.tv_item_gedan_comment_time, TimeUtil.getTimeStandardOnlyYMDChinese(bean.getTime()));
        //点赞数量
        if (bean.getLikedCount() != 0) {
            baseViewHolder.setVisible(R.id.tv_item_gedan_comment_zan_count, true);
            baseViewHolder.setText(R.id.tv_item_gedan_comment_zan_count, String.valueOf(bean.getLikedCount()));
        }
        //评论内容
        baseViewHolder.setText(R.id.tv_item_gedan_comment_content, bean.getContent());
        //回复数量
        if (bean.getBeReplied().size() != 0) {
            baseViewHolder.setVisible(R.id.tv_item_gedan_comment_replied, true);
            baseViewHolder.setText(R.id.tv_item_gedan_comment_replied, bean.getBeReplied().size() + "条回复");
        }

        final ImageView praiseView = baseViewHolder.getView(R.id.iv_item_gedan_comment_zan);
        //tag : true 当前是赞 false当前不是赞
        praiseView.setTag(bean.isLiked());
        praiseView.setImageResource(bean.isLiked() ? R.drawable.ic_parise_red : R.drawable.ic_parise);

    }
}
