package com.netease.music.ui.page.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lxj.xpopup.XPopup;
import com.netease.lib_api.model.user.UserPlayListHeader;
import com.netease.lib_api.model.user.UserPlaylistBean;
import com.netease.lib_common_ui.dialog.CreatePlayListDialog;
import com.netease.lib_image_loader.app.ImageLoaderManager;
import com.netease.lib_network.ApiEngine;
import com.netease.music.R;
import com.netease.music.data.config.TypeEnum;
import com.netease.music.ui.page.discover.square.detail.SongListDetailActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class MultiplePlaylistAdapter extends BaseNodeAdapter {


    public MultiplePlaylistAdapter(Context context, List<BaseNode> nodeList) {
        super(nodeList);
        addFullSpanNodeProvider(new RootNodeProvider());
        addNodeProvider(new NodeProvider());

        //折叠/收起 创建新歌单
        addChildClickViewIds(R.id.iv_mine_gedan_state, R.id.iv_item_gedan_new);
        setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.iv_mine_gedan_state:
                    ImageView changeView = (ImageView) adapter.getViewByPosition(position, R.id.iv_mine_gedan_state);
                    BaseNodeAdapter nodeAdapter = (BaseNodeAdapter) adapter;
                    UserPlayListHeader item = (UserPlayListHeader) nodeAdapter.getItem(position);
                    //折叠或展开
                    nodeAdapter.expandOrCollapse(position);
                    //显示动画  TODO 显示有问题
                    ObjectAnimator.ofFloat(changeView, "rotation", item.isExpanded() ? 270f : 180f, item.isExpanded() ? 180f : 270f).setDuration(300).start();

                    break;
                case R.id.iv_item_gedan_new:
                    new XPopup.Builder(getContext())
                            .asCustom(new CreatePlayListDialog(getContext(), name -> {
                                Disposable subscribe = ApiEngine.getInstance().getApiService().createPlayList(name)
                                        .compose(ApiEngine.getInstance().applySchedulers())
                                        .subscribe(commonMessageBean -> Toast.makeText(context, commonMessageBean.getCode() == 200 ? "歌单创建成功" : "歌单创建失败", Toast.LENGTH_SHORT).show());
                            })).show();
                    break;
                default:
                    break;
            }
        });
        //点击查看歌单详情
        setOnItemClickListener((adapter, view, position) -> {
            Object item = adapter.getItem(position);
            if (item instanceof UserPlayListHeader.UserPlayListContext) {
                UserPlayListHeader.UserPlayListContext entity = (UserPlayListHeader.UserPlayListContext) item;
                SongListDetailActivity.startActivity(context, TypeEnum.PLAYLIST_ID, entity.getPlaylist().getId(), "");
            }
        });
    }

    @Override
    protected int getItemType(@NotNull List<? extends BaseNode> data, int position) {
        BaseNode node = data.get(position);
        if (node instanceof UserPlayListHeader) {
            return 0;
        } else if (node instanceof UserPlayListHeader.UserPlayListContext) {
            return 1;
        }
        return -1;
    }

    public static class RootNodeProvider extends BaseNodeProvider {

        @Override
        public int getItemViewType() {
            return 0;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_mine_gedan_header;
        }

        @Override
        public void convert(@NotNull BaseViewHolder helper, @NotNull BaseNode data) {
            // 数据类型需要自己强转
            UserPlayListHeader entity = (UserPlayListHeader) data;
            helper.setText(R.id.tv_item_gedan_header_lefttitle, entity.getHeader());
            helper.setText(R.id.tv_item_gedan_header_righttext, entity.getPlayNum());
            helper.setVisible(R.id.iv_item_gedan_new, helper.getLayoutPosition() == 0);
        }

        @Override
        public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {

        }
    }

    public static class NodeProvider extends BaseNodeProvider {

        private ImageLoaderManager manager;

        NodeProvider() {
            manager = ImageLoaderManager.getInstance();
        }

        @Override
        public int getItemViewType() {
            return 1;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_mine_gedan_content;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void convert(@NotNull BaseViewHolder helper, @NotNull BaseNode data) {
            // 数据类型需要自己强转
            UserPlayListHeader.UserPlayListContext entity = (UserPlayListHeader.UserPlayListContext) data;
            UserPlaylistBean.PlaylistBean item = entity.getPlaylist();
            ImageView img = helper.getView(R.id.iv_item_gedan_content_img);
            manager.displayImageForCorner(img, item.getCoverImgUrl(), 5);
            helper.setText(R.id.tv_item_gedan_content_toptext, item.getName());
            TextView bottomText = helper.getView(R.id.tv_item_gedan_content_bottomtext);
            helper.setText(R.id.tv_item_gedan_content_bottomtext, item.getTrackCount() + "首");
            bottomText.setText(item.isSubscribed() ? item.getTrackCount() + "首 " + "by " + item.getCreator().getNickname() : item.getTrackCount() + "首");
        }

        @Override
        public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {

            //getAdapter().expandOrCollapse(position);
        }
    }
}
