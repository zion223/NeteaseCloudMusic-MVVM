/*
 * Copyright 2018-present KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netease.music.ui.page.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.netease.lib_api.model.album.AlbumOrSongBean;
import com.netease.lib_api.model.playlist.MainRecommendPlayListBean;
import com.netease.music.data.bean.LibraryInfo;

public class DiffUtils {

    private DiffUtil.ItemCallback<LibraryInfo> mLibraryInfoItemCallback;


    private DiffUtil.ItemCallback<MainRecommendPlayListBean.RecommendBean> mRecommendPlaylistItemCallback;

    private DiffUtil.ItemCallback<AlbumOrSongBean> mAlbumOrSongItemCallback;

    private DiffUtils() {
    }

    private static DiffUtils sDiffUtils = new DiffUtils();

    public static DiffUtils getInstance() {
        return sDiffUtils;
    }

    public DiffUtil.ItemCallback<LibraryInfo> getLibraryInfoItemCallback() {
        if (mLibraryInfoItemCallback == null) {
            mLibraryInfoItemCallback = new DiffUtil.ItemCallback<LibraryInfo>() {
                @Override
                public boolean areItemsTheSame(@NonNull LibraryInfo oldItem, @NonNull LibraryInfo newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull LibraryInfo oldItem, @NonNull LibraryInfo newItem) {
                    return oldItem.getTitle().equals(newItem.getTitle());
                }
            };
        }
        return mLibraryInfoItemCallback;
    }


    public DiffUtil.ItemCallback<MainRecommendPlayListBean.RecommendBean> getRecommendPlayListItemCallback() {
        if (mRecommendPlaylistItemCallback == null) {
            mRecommendPlaylistItemCallback = new DiffUtil.ItemCallback<MainRecommendPlayListBean.RecommendBean>() {
                @Override
                public boolean areItemsTheSame(@NonNull MainRecommendPlayListBean.RecommendBean oldItem, @NonNull MainRecommendPlayListBean.RecommendBean newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull MainRecommendPlayListBean.RecommendBean oldItem, @NonNull MainRecommendPlayListBean.RecommendBean newItem) {
                    return oldItem.getId() == newItem.getId();
                }
            };
        }
        return mRecommendPlaylistItemCallback;
    }

    public DiffUtil.ItemCallback<AlbumOrSongBean> getAlbumOrSongItemCallback() {
        if (mAlbumOrSongItemCallback == null) {
            mAlbumOrSongItemCallback = new DiffUtil.ItemCallback<AlbumOrSongBean>() {
                @Override
                public boolean areItemsTheSame(@NonNull AlbumOrSongBean oldItem, @NonNull AlbumOrSongBean newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull AlbumOrSongBean oldItem, @NonNull AlbumOrSongBean newItem) {
                    return oldItem.getId() == newItem.getId();
                }
            };
        }
        return mAlbumOrSongItemCallback;
    }
}
