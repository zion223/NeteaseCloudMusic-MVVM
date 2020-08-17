package com.imooc.lib_network;


import com.imooc.lib_api.model.album.AlbumSublistBean;
import com.imooc.lib_api.model.artist.ArtistSublistBean;
import com.imooc.lib_api.model.banner.BannerBean;
import com.imooc.lib_api.model.dj.DjCategoryRecommendBean;
import com.imooc.lib_api.model.dj.DjCatelistBean;
import com.imooc.lib_api.model.dj.DjDetailBean;
import com.imooc.lib_api.model.dj.DjPaygiftBean;
import com.imooc.lib_api.model.dj.DjProgramBean;
import com.imooc.lib_api.model.dj.DjRecommendBean;
import com.imooc.lib_api.model.dj.DjRecommendTypeBean;
import com.imooc.lib_api.model.dj.DjSubBean;
import com.imooc.lib_api.model.mv.MvSublistBean;
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
import com.imooc.lib_api.model.song.MusicCommentBean;
import com.imooc.lib_api.model.song.PlayListCommentBean;
import com.imooc.lib_api.model.song.SongDetailBean;
import com.imooc.lib_api.model.user.LikeListBean;
import com.imooc.lib_api.model.user.LoginBean;
import com.imooc.lib_api.model.user.MainEventBean;
import com.imooc.lib_api.model.user.UserDetailBean;
import com.imooc.lib_api.model.user.UserEventBean;
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

    @GET("user/cloud")
    Observable<UserCloudBean> getUserCloudMusic();

    @GET("register/cellphone")
    Observable<LoginBean> register(@Query("phone") String phone, @Query("password") String password, @Query("capture") String capture);

    @GET("logout")
    Observable<CommonMessageBean> logout();

    @GET("banner")
    Observable<BannerBean> getBanner(@Query("type") String type);

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
    Observable<SongDetailBean> getSongDetail(@Query("ids") long ids);

    @GET("like")
    Observable<LikeMusicBean> likeMusice(@Query("id") long id);

    @GET("comment/music")
    Observable<MusicCommentBean> getMusicComment(@Query("id") long id);

    @GET("comment/like")
    Observable<CommentLikeBean> likeComment(@Query("id") long id, @Query("cid") long cid, @Query("t") int t, @Query("type") int type);

    @GET("playmode/intelligence/list")
    Observable<PlayModeIntelligenceBean> getIntelligenceList(@Query("id") long id, @Query("pid") long pid);

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

    @GET("dj/paygift")
    Observable<DjPaygiftBean> getDjPaygift(@Query("limit") int limit, @Query("offset") int offset);

    @GET("dj/category/recommend")
    Observable<DjCategoryRecommendBean> getDjCategoryRecommend();

    @GET("dj/catelist")
    Observable<DjCatelistBean> getDjCatelist();

    @GET("dj/sub")
    Observable<DjSubBean> subDj(@Query("rid") long rid, @Query("t") int isSub);

    @GET("dj/program")
    Observable<DjProgramBean> getDjProgram(@Query("rid") long rid);

    @GET("dj/detail")
    Observable<DjDetailBean> getDjDetail(@Query("rid") long rid);
}
