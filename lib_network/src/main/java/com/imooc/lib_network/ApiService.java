package com.imooc.lib_network;


import com.imooc.lib_api.model.album.AlbumDetailBean;
import com.imooc.lib_api.model.album.AlbumDynamicBean;
import com.imooc.lib_api.model.album.AlbumSublistBean;
import com.imooc.lib_api.model.artist.ArtistSublistBean;
import com.imooc.lib_api.model.banner.BannerBean;
import com.imooc.lib_api.model.dj.DjBannerBean;
import com.imooc.lib_api.model.dj.DjCategoryRecommendBean;
import com.imooc.lib_api.model.dj.DjCatelistBean;
import com.imooc.lib_api.model.dj.DjDetailBean;
import com.imooc.lib_api.model.dj.DjPaygiftBean;
import com.imooc.lib_api.model.dj.DjProgramBean;
import com.imooc.lib_api.model.dj.DjRecommendBean;
import com.imooc.lib_api.model.dj.DjRecommendTypeBean;
import com.imooc.lib_api.model.dj.DjSubBean;
import com.imooc.lib_api.model.mv.MvSublistBean;
import com.imooc.lib_api.model.mv.VideoBean;
import com.imooc.lib_api.model.mv.VideoDetailBean;
import com.imooc.lib_api.model.mv.VideoGroupBean;
import com.imooc.lib_api.model.mv.VideoRelatedBean;
import com.imooc.lib_api.model.mv.VideoUrlBean;
import com.imooc.lib_api.model.notification.CommonMessageBean;
import com.imooc.lib_api.model.notification.UserCloudBean;
import com.imooc.lib_api.model.playlist.CatlistBean;
import com.imooc.lib_api.model.playlist.DailyRecommendBean;
import com.imooc.lib_api.model.playlist.HighQualityPlayListBean;
import com.imooc.lib_api.model.playlist.MainRecommendPlayListBean;
import com.imooc.lib_api.model.playlist.MyFmBean;
import com.imooc.lib_api.model.playlist.PlayModeIntelligenceBean;
import com.imooc.lib_api.model.playlist.PlaylistDetailBean;
import com.imooc.lib_api.model.playlist.RecommendPlayListBean;
import com.imooc.lib_api.model.playlist.TopListBean;
import com.imooc.lib_api.model.search.AlbumSearchBean;
import com.imooc.lib_api.model.search.FeedSearchBean;
import com.imooc.lib_api.model.search.HotSearchDetailBean;
import com.imooc.lib_api.model.search.PlayListSearchBean;
import com.imooc.lib_api.model.search.RadioSearchBean;
import com.imooc.lib_api.model.search.SimiSingerBean;
import com.imooc.lib_api.model.search.SingerAblumSearchBean;
import com.imooc.lib_api.model.search.SingerDescriptionBean;
import com.imooc.lib_api.model.search.SingerSearchBean;
import com.imooc.lib_api.model.search.SingerSongSearchBean;
import com.imooc.lib_api.model.search.SongSearchBean;
import com.imooc.lib_api.model.search.SynthesisSearchBean;
import com.imooc.lib_api.model.search.UserSearchBean;
import com.imooc.lib_api.model.song.CommentLikeBean;
import com.imooc.lib_api.model.song.LikeMusicBean;
import com.imooc.lib_api.model.song.LyricBean;
import com.imooc.lib_api.model.song.MusicCanPlayBean;
import com.imooc.lib_api.model.song.NewSongBean;
import com.imooc.lib_api.model.song.PlayListCommentBean;
import com.imooc.lib_api.model.song.SongDetailBean;
import com.imooc.lib_api.model.user.FollowBean;
import com.imooc.lib_api.model.user.LikeListBean;
import com.imooc.lib_api.model.user.LoginBean;
import com.imooc.lib_api.model.user.MainEventBean;
import com.imooc.lib_api.model.user.UserDetailBean;
import com.imooc.lib_api.model.user.UserEventBean;
import com.imooc.lib_api.model.user.UserFollowedBean;
import com.imooc.lib_api.model.user.UserFollowerBean;
import com.imooc.lib_api.model.user.UserPlaylistBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    String BASE_URL = "http://62.234.57.125:3000/";

    @GET("login/cellphone")
    Observable<LoginBean> login(@Query("phone") String phone, @Query("password") String password);

    @GET("captcha/sent")
    Observable<CommonMessageBean> capture(@Query("phone") String phone);

    @GET("video/url")
    Observable<VideoUrlBean> getVideoUrl(@Query("id") String id);

    @GET("user/cloud")
    Observable<UserCloudBean> getUserCloudMusic();

    @GET("dj/detail")
    Observable<DjDetailBean> getRadioDetail(@Query("rid") String id);

    @GET("dj/program")
    Observable<DjProgramBean> getRadioProgram(@Query("rid") String id, @Query("asc") boolean asc);

    @GET("album")
    Observable<AlbumDetailBean> getAlbumDetail(@Query("id") long id);

    @GET("album/detail/dynamic")
    Observable<AlbumDynamicBean> getAlbumDynamic(@Query("id") long id);

    @GET("register/cellphone")
    Observable<LoginBean> register(@Query("phone") String phone, @Query("password") String password, @Query("capture") String capture);

    @GET("logout")
    Observable<CommonMessageBean> logout();

    @GET("banner")
    Observable<BannerBean> getBanner(@Query("type") String type);

    @GET("dj/banner")
    Observable<DjBannerBean> getRadioBanner();

    @GET("recommend/resource")
    Observable<MainRecommendPlayListBean> getRecommendPlayList();

    @GET("recommend/songs")
    Observable<DailyRecommendBean> getDailyRecommend();

    @GET("toplist")
    Observable<TopListBean> getTopList();

    @GET("dj/recommend")
    Observable<DjRecommendBean> getRadioRecommend();

    @GET("dj/recommend/type")
    Observable<DjRecommendTypeBean> getDjRecommend(@Query("type") int type);

    @GET("top/playlist")
    Observable<RecommendPlayListBean> getPlayList(@Query("cat") String type, @Query("limit") int limit);

    @GET("top/playlist/highquality")
    Observable<HighQualityPlayListBean> getHighquality(@Query("limit") int limit, @Query("before") long before);

    @GET("playlist/catlist")
    Observable<CatlistBean> getCatlist();

    @GET("playlist/detail")
    Observable<PlaylistDetailBean> getPlaylistDetail(@Query("id") long id);

    @GET("check/music")
    Observable<MusicCanPlayBean> getMusicCanPlay(@Query("id") long id);

    @GET("user/playlist")
    Observable<UserPlaylistBean> getUserPlaylist(@Query("uid") long uid);

    @GET("user/event")
    Observable<UserEventBean> getUserEvent(@Query("uid") long uid, @Query("limit") int limit, @Query("lasttime") long time);

    @GET("user/detail")
    Observable<UserDetailBean> getUserDetail(@Query("uid") long uid);

    @GET("follow")
        // t:1关注 0:取消关注
    Observable<FollowBean> getUserFollow(@Query("id") long uid, @Query("t") int t);


    @GET("user/follows")
    Observable<UserFollowerBean> getUserFollower(@Query("id") long uid);

    @GET("user/followeds")
    Observable<UserFollowedBean> getUserFollowed(@Query("id") long uid);

    @GET("search/hot/detail")
    Observable<HotSearchDetailBean> getSearchHotDetail();

    @GET("search")
    Observable<SongSearchBean> getSongSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<FeedSearchBean> getFeedSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<SingerSearchBean> getSingerSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<AlbumSearchBean> getAlbumSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<PlayListSearchBean> getPlayListSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<RadioSearchBean> getRadioSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<UserSearchBean> getUserSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("search")
    Observable<SynthesisSearchBean> getSynthesisSearch(@Query("keywords") String keywords, @Query("type") int type);

    @GET("artists")
    Observable<SingerSongSearchBean> getSingerHotSong(@Query("id") long id);

    @GET("artist/album")
    Observable<SingerAblumSearchBean> getSingerAlbum(@Query("id") long id);

    @GET("artist/desc")
    Observable<SingerDescriptionBean> getSingerDesc(@Query("id") long id);

    @GET("simi/artist")
    Observable<SimiSingerBean> getSimiSinger(@Query("id") long id);

    @GET("likelist")
    Observable<LikeListBean> getLikeList(@Query("uid") long uid);

    @GET("song/detail")
    Observable<SongDetailBean> getSongDetail(@Query("ids") String ids);

    @GET("like")
    Observable<LikeMusicBean> likeMusice(@Query("id") long id);

    @GET("comment/music")
    Observable<PlayListCommentBean> getMusicComment(@Query("id") String id);

    @GET("comment/playlist")
    Observable<PlayListCommentBean> getPlayListComment(@Query("id") String id);

    @GET("comment/album")
    Observable<PlayListCommentBean> getAlbumComment(@Query("id") String id);

    @GET("video/group/list")
    Observable<VideoGroupBean> getVideoGroup();

    @GET("video/group")
    Observable<VideoBean> getVideoTab(@Query("id") long id);

    @GET("video/detail")
    Observable<VideoDetailBean> getVideoDetail(@Query("id") String id);

    @GET("video/timeline/recommend")
    Observable<VideoBean> getVideoRecommend();

    @GET("related/allvideo")
    Observable<VideoRelatedBean> getVideoRelated(@Query("id") String id);

    /**
     * 给评论点赞
     * id : 资源 id, 如歌曲 id, mv id
     * cid : 评论 id
     * t : 是否点赞 ,1 为点赞 ,0 为取消点赞
     * tpye: 数字 , 资源类型 , 对应歌曲 , mv, 专辑 , 歌单 , 电台, 视频对应以下类型
     * 0: 歌曲 1: mv 2: 歌单 3: 专辑 4: 电台 5: 视频 6: 动态
     */
    @GET("comment/like")
    Observable<CommentLikeBean> likeComment(@Query("id") String id, @Query("cid") long cid, @Query("t") int t, @Query("type") int type);

    /**
     * 给资源点赞
     * type : 资源类型 1: mv 4: 电台 5: 视频 6: 动态
     * t : 是否点赞 ,1 为点赞 ,0 为取消点赞
     * id: 资源 id
     * PS  注意：如给动态点赞，不需要传入 id，需要传入 threadId,可通过 event,/user/event 接口获取，如： /resource/like?t=1&type=6&threadId=A_EV_2_6559519868_32953014
     */
    @GET("resource/like")
    Observable<CommentLikeBean> likeResource(@Query("id") long id, @Query("t") int t, @Query("type") int type);

    @GET("resource/like")
    Observable<CommentLikeBean> likeEventResource(@Query("threadId") String id, @Query("t") int t, @Query("type") int type);

    @GET("playmode/intelligence/list")
    Observable<PlayModeIntelligenceBean> getIntelligenceList(@Query("id") long id, @Query("pid") long pid);

    @GET("playlist/subscribe")
        // t=1 收藏 2 取消收藏
    Observable<CommonMessageBean> subscribePlayList(@Query("id") long id, @Query("t") long t);

    @GET("playlist/create")
    Observable<CommonMessageBean> createPlayList(@Query("name") String name);

    @GET("album/sub")
        // t=1 收藏 2 取消收藏
    Observable<CommonMessageBean> subscribeAlbum(@Query("id") long id, @Query("t") long t);

    @GET("top/album")
    Observable<AlbumSearchBean.ResultBean> getTopAlbum(@Query("limit") int limit);

    @GET("album/newest")
    Observable<AlbumSearchBean.ResultBean> getNewAlbum();

    @GET("top/song")
        //PS.全部:0 华语:7  欧美:96 日本:8 韩国:16
    Observable<NewSongBean> getTopSong(@Query("type") int type);

    @GET("album/sublist")
    Observable<AlbumSublistBean> getAlbumSublist();

    @GET("artist/sublist")
    Observable<ArtistSublistBean> getArtistSublist();

    @GET("mv/sublist")
    Observable<MvSublistBean> getMvSublist();

    @GET("personal_fm")
    Observable<MyFmBean> getMyFm();

    @GET("event")
    Observable<MainEventBean> getMainEvent();

    @GET("lyric")
    Observable<LyricBean> getLyric(@Query("id") long id);

    @GET("comment/playlist")
    Observable<PlayListCommentBean> getPlaylistComment(@Query("id") long id);

    @GET("comment/video")
    Observable<PlayListCommentBean> getVideoComment(@Query("id") String id);

    @GET("dj/paygift")
    Observable<DjPaygiftBean> getDjPaygift(@Query("limit") int limit, @Query("offset") int offset);

    @GET("dj/category/recommend")
    Observable<DjCategoryRecommendBean> getDjCategoryRecommend();

    @GET("dj/catelist")
    Observable<DjCatelistBean> getDjCatelist();

    @GET("dj/sub")
        // 1 订阅 0取消订阅
    Observable<DjSubBean> getSubRadio(@Query("rid") String rid, @Query("t") int isSub);

}
