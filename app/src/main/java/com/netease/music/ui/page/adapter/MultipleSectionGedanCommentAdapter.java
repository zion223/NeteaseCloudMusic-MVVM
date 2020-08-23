package com.netease.music.ui.page.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.imooc.lib_api.model.playlist.PlayListCommentEntity;
import com.imooc.lib_api.model.song.MusicCommentBean;
import com.imooc.lib_image_loader.app.ImageLoaderManager;
import com.netease.music.R;
import com.netease.music.ui.page.discover.user.UserDetailActivity;
import com.netease.music.util.TimeUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MultipleSectionGedanCommentAdapter extends BaseSectionQuickAdapter<PlayListCommentEntity, BaseViewHolder> {


    private ImageLoaderManager manager;
    private String resourceId;
    private Context mContext;
    private int commentType;

    public MultipleSectionGedanCommentAdapter(String commentId, int type, Context context, List<PlayListCommentEntity> data) {
        super(R.layout.item_gedan_comment_header, R.layout.item_gedan_detail_comment, data);
        setNormalLayout(R.layout.item_gedan_detail_comment);
        manager = ImageLoaderManager.getInstance();
        resourceId = commentId;
        mContext = context;
        commentType = type;
        //TODO 不好使
//        addChildClickViewIds(R.id.iv_item_gedan_comment_avatar_img, R.id.tv_item_gedan_comment_avatar_name, R.id.iv_item_gedan_comment_zan);
//        setOnItemClickListener((adapter, view, position) -> {
//            switch (view.getId()) {
//                case R.id.iv_item_gedan_comment_avatar_img:
//                case R.id.tv_item_gedan_comment_avatar_name:
//                    //点击进入用户详情
//                    UserDetailActivity.startActivity(context, data.get(position).getComment().getUser().getUserId());
//                    break;
//                case R.id.iv_item_gedan_comment_zan:
//                    //点赞
//                    break;
//                default:
//                    break;
//
//            }
//        });
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

        View avtarView = baseViewHolder.getView(R.id.iv_item_gedan_comment_avatar_img);
        View nameView = baseViewHolder.getView(R.id.tv_item_gedan_comment_avatar_name);
        //点击进入用户详情
        avtarView.setOnClickListener(v -> UserDetailActivity.startActivity(mContext, playListCommentEntity.getComment().getUser().getUserId()));
        nameView.setOnClickListener(v -> UserDetailActivity.startActivity(mContext, playListCommentEntity.getComment().getUser().getUserId()));
        //TODO  评论点赞和取消点赞
//        baseViewHolder.setOnClickListener(R.id.iv_item_gedan_comment_zan, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //是否已经点过赞
//                Boolean parise = (Boolean) praiseView.getTag();
//                //点赞或取消点赞
//                RequestCenter.getlikeComment(commentId, bean.getCommentId(), !parise, commentType, new DisposeDataListener() {
//                    @Override
//                    public void onSuccess(Object responseObj) {
//                        CommentLikeBean result = (CommentLikeBean) responseObj;
//                        if (result.getCode() == 200) {
//                            praiseView.setTag(!parise);
//                            if (!parise) {
//                                praiseView.setImageResource(R.drawable.ic_parise_red);
//                                //点赞
//                                AnimUtil.getLikeAnim(praiseView).start();
//                                baseViewHolder.setText(R.id.tv_item_gedan_comment_zan_count, String.valueOf(bean.getLikedCount() + 1));
//                                baseViewHolder.setTextColor(R.id.tv_item_gedan_comment_zan_count, Color.parseColor("#FF3A3A"));
//                                Toast.makeText(mContext, "点赞成功", Toast.LENGTH_SHORT).show();
//                            } else {
//                                baseViewHolder.setText(R.id.tv_item_gedan_comment_zan_count, String.valueOf(bean.getLikedCount()));
//                                praiseView.setImageResource(R.drawable.ic_parise);
//                                baseViewHolder.setTextColor(R.id.tv_item_gedan_comment_zan_count, Color.GRAY);
//                                Toast.makeText(mContext, "取消赞成功", Toast.LENGTH_SHORT).show();
//                            }
//
//                        } else {
//                            Toast.makeText(mContext, "点赞或取消赞失败", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Object reasonObj) {
//                        Toast.makeText(mContext, "点赞或取消赞失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//        });
    }
}
