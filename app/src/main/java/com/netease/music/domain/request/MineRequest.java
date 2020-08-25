package com.netease.music.domain.request;

import androidx.lifecycle.MutableLiveData;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.imooc.lib_api.model.user.UserPlayListHeader;
import com.imooc.lib_api.model.user.UserPlaylistBean;
import com.imooc.lib_network.ApiEngine;
import com.kunminx.architecture.domain.request.BaseRequest;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MineRequest extends BaseRequest {


    //用户歌单
    private MutableLiveData<List<BaseNode>> userPlaylist;
    //用户的电台数量


    public MutableLiveData<List<BaseNode>> getUserPlaylist() {
        if (userPlaylist == null) {
            userPlaylist = new MutableLiveData<>();
        }
        return userPlaylist;
    }

    public void requestUserPlaylist(long uid) {
        Disposable subscribe = ApiEngine.getInstance().getApiService().getUserPlaylist(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(playlistBeans -> {
                    List<UserPlaylistBean.PlaylistBean> playlist = playlistBeans.getPlaylist();
                    int subIndex = 0;
                    for (int i = 0; i < playlist.size(); i++) {
                        if (playlist.get(i).getCreator().getUserId() != uid) {
                            subIndex = i;
                            break;
                        }
                    }
                    List<BaseNode> nodeList = new ArrayList<>();
                    List<BaseNode> nodeContextList = new ArrayList<>();
                    List<BaseNode> nodeContext2List = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        nodeContextList.add(new UserPlayListHeader.UserPlayListContext(playlist.get(i)));
                    }
                    for (int i = subIndex; i < playlist.size(); i++) {
                        nodeContext2List.add(new UserPlayListHeader.UserPlayListContext(playlist.get(i)));
                    }
                    nodeList.add(new UserPlayListHeader("创建的歌单", subIndex, nodeContextList));
                    nodeList.add(new UserPlayListHeader("收藏的歌单", playlist.size() - subIndex, nodeContext2List));

                    userPlaylist.postValue(nodeList);
                });
    }
}
