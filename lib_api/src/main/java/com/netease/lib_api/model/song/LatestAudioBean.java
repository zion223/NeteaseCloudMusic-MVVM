package com.netease.lib_api.model.song;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;


/**
 * 最近播放的歌曲实体
 */
@Entity
public class LatestAudioBean {


    @Ignore
    public LatestAudioBean(String id, @NotNull String url, @NotNull String name, @NotNull String author,
                           @NotNull String album, @NotNull String albumInfo, @NotNull String albumPic,
                           @NotNull String totalTime) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.author = author;
        this.album = album;
        this.albumInfo = albumInfo;
        this.albumPic = albumPic;
        this.totalTime = totalTime;
    }

    public LatestAudioBean() {
    }

    @PrimaryKey(autoGenerate = true)
    private int uid;

    //歌曲Id
    @ColumnInfo(name = "song_id")
    private String id;
    //地址
    @ColumnInfo(name = "song_url")
    private String url;

    //歌名
    @ColumnInfo(name = "song_name")
    private String name;

    //作者
    @ColumnInfo(name = "song_author")
    private String author;

    //所属专辑
    @ColumnInfo(name = "song_album")
    private String album;

    @ColumnInfo(name = "song_albumInfo")
    private String albumInfo;

    //专辑封面
    @ColumnInfo(name = "song_albumPic")
    private String albumPic;

    //时长
    @ColumnInfo(name = "song_totaltime")
    private String totalTime;

    // 最近播放的类型 歌曲 视频 电台 备用 TODO
    @ColumnInfo(name = "audio_type")
    private int type;

    //将LatestAudioBean转化成AudioBean
    public static AudioBean convertToAudioBean(LatestAudioBean item) {
        return new AudioBean(item.getId(), item.getUrl(), item.getName(), item.getAuthor(), item.getAlbum(), item.getAlbumInfo(), item.getAlbumPic(), item.getTotalTime());
    }

    public static LatestAudioBean convertAudioToLatestBean(AudioBean item) {
        return new LatestAudioBean(item.getId(), item.getUrl(), item.getName(), item.getAuthor(), item.getAlbum(), item.getAlbumInfo(), item.getAlbumPic(), item.getTotalTime());
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String mUrl) {
        this.url = mUrl;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumPic() {
        return this.albumPic;
    }

    public void setAlbumPic(String albumPic) {
        this.albumPic = albumPic;
    }

    public String getAlbumInfo() {
        return this.albumInfo;
    }

    public void setAlbumInfo(String albumInfo) {
        this.albumInfo = albumInfo;
    }

    public String getTotalTime() {
        return this.totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getMUrl() {
        return this.url;
    }

    public void setMUrl(String mUrl) {
        this.url = mUrl;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof AudioBean)) {
            return false;
        }
        return ((LatestAudioBean) other).id.equals(this.id);
    }


    @Override
    public String toString() {
        return "AudioBean{" +
                "id='" + id + '\'' +
                ", Url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", album='" + album + '\'' +
                ", albumInfo='" + albumInfo + '\'' +
                ", albumPic='" + albumPic + '\'' +
                ", totalTime='" + totalTime + '\'' +
                '}';
    }


    private static String getSongPlayUrl(long id) {
        return "https://music.163.com/song/media/outer/url?id=" + id + ".mp3";
    }
}

