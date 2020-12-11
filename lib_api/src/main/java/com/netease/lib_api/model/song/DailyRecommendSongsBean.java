package com.netease.lib_api.model.song;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class DailyRecommendSongsBean {
    /**
     * name : JoJo
     * id : 27917548
     * pst : 0
     * t : 0
     * ar : [{"id":48514,"name":"Boz Scaggs","tns":[],"alias":[]}]
     * alia : []
     * pop : 85
     * st : 0
     * rt :
     * fee : 8
     * v : 6
     * crbt : null
     * cf :
     * al : {"id":2694934,"name":"The Essential Boz Scaggs","picUrl":"https://p1.music.126.net/_YmTuIMoG0dh2OJSOE-LPw==/17829680556434243.jpg","tns":[],"pic_str":"17829680556434243","pic":17829680556434244}
     * dt : 354746
     * h : {"br":320000,"fid":0,"size":14220536,"vd":-2.65076E-4}
     * m : {"br":160000,"fid":0,"size":7123590,"vd":0.0324002}
     * l : {"br":96000,"fid":0,"size":4284811,"vd":-2.65076E-4}
     * a : null
     * cd : 2
     * no : 1
     * rtUrl : null
     * ftype : 0
     * rtUrls : []
     * djId : 0
     * copyright : 1
     * s_id : 0
     * mark : 0
     * mv : 0
     * mst : 9
     * cp : 7001
     * rtype : 0
     * rurl : null
     * publishTime : 1382976000007
     */

    @ColumnInfo(name = "song_name")
    private String name;
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "song_pst")
    private int pst;
    @ColumnInfo(name = "song_t")
    private int t;
    @ColumnInfo(name = "song_pop")
    private int pop;
    @ColumnInfo(name = "song_st")
    private int st;
    @ColumnInfo(name = "song_rt")
    private String rt;
    @ColumnInfo(name = "song_fee")
    private int fee;
    @ColumnInfo(name = "song_v")
    private int v;
    @ColumnInfo(name = "song_cf")
    private String cf;
    @ColumnInfo(name = "song_al")
    private AlBean al;
    //歌曲时间
    @ColumnInfo(name = "song_dt")
    private long dt;
    @Ignore
    private HBean h;
    @Ignore
    private MBean m;
    @Ignore
    private LBean l;
    @Ignore
    private Object a;
    private String cd;
    private int no;
    @Ignore
    private Object rtUrl;
    private int ftype;
    private int djId;
    private int copyright;
    private int s_id;
    private long mark;
    @Ignore
    private long mv;
    private int mst;
    private int cp;
    private int rtype;
    @Ignore
    private Object rurl;
    private long publishTime;

    private List<ArBean> ar;
    @Ignore
    private List<?> alia;
    @Ignore
    private List<?> rtUrls;
    @ColumnInfo(name = "song_reason")
    private String recommendReason;

    public void setMv(long mv) {
        this.mv = mv;
    }

    public String getRecommendReason() {
        return recommendReason;
    }

    public void setRecommendReason(String recommendReason) {
        this.recommendReason = recommendReason;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPst() {
        return pst;
    }

    public void setPst(int pst) {
        this.pst = pst;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public int getPop() {
        return pop;
    }

    public void setPop(int pop) {
        this.pop = pop;
    }

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }


    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public AlBean getAl() {
        return al;
    }

    public void setAl(AlBean al) {
        this.al = al;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public HBean getH() {
        return h;
    }

    public void setH(HBean h) {
        this.h = h;
    }

    public MBean getM() {
        return m;
    }

    public void setM(MBean m) {
        this.m = m;
    }

    public LBean getL() {
        return l;
    }

    public void setL(LBean l) {
        this.l = l;
    }

    public Object getA() {
        return a;
    }

    public void setA(Object a) {
        this.a = a;
    }

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public Object getRtUrl() {
        return rtUrl;
    }

    public void setRtUrl(Object rtUrl) {
        this.rtUrl = rtUrl;
    }

    public int getFtype() {
        return ftype;
    }

    public void setFtype(int ftype) {
        this.ftype = ftype;
    }

    public int getDjId() {
        return djId;
    }

    public void setDjId(int djId) {
        this.djId = djId;
    }

    public int getCopyright() {
        return copyright;
    }

    public void setCopyright(int copyright) {
        this.copyright = copyright;
    }

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    public long getMark() {
        return mark;
    }

    public void setMark(long mark) {
        this.mark = mark;
    }

    public long getMv() {
        return mv;
    }

    public void setMv(int mv) {
        this.mv = mv;
    }

    public int getMst() {
        return mst;
    }

    public void setMst(int mst) {
        this.mst = mst;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public int getRtype() {
        return rtype;
    }

    public void setRtype(int rtype) {
        this.rtype = rtype;
    }

    public Object getRurl() {
        return rurl;
    }

    public void setRurl(Object rurl) {
        this.rurl = rurl;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public List<ArBean> getAr() {
        return ar;
    }

    public void setAr(List<ArBean> ar) {
        this.ar = ar;
    }

    public List<?> getAlia() {
        return alia;
    }

    public void setAlia(List<?> alia) {
        this.alia = alia;
    }

    public List<?> getRtUrls() {
        return rtUrls;
    }

    public void setRtUrls(List<?> rtUrls) {
        this.rtUrls = rtUrls;
    }

    public static class AlBean {
        /**
         * id : 2694934
         * name : The Essential Boz Scaggs
         * picUrl : https://p1.music.126.net/_YmTuIMoG0dh2OJSOE-LPw==/17829680556434243.jpg
         * tns : []
         * pic_str : 17829680556434243
         * pic : 17829680556434244
         */

        private long id;
        private String name;
        private String picUrl;
        private String pic_str;
        private long pic;
        private List<?> tns;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getPic_str() {
            return pic_str;
        }

        public void setPic_str(String pic_str) {
            this.pic_str = pic_str;
        }

        public long getPic() {
            return pic;
        }

        public void setPic(long pic) {
            this.pic = pic;
        }

        public List<?> getTns() {
            return tns;
        }

        public void setTns(List<?> tns) {
            this.tns = tns;
        }
    }

    public static class HBean {
        /**
         * br : 320000
         * fid : 0
         * size : 14220536
         * vd : -2.65076E-4
         */

        private int br;
        private String fid;
        private int size;
        private double vd;

        public int getBr() {
            return br;
        }

        public void setBr(int br) {
            this.br = br;
        }

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public double getVd() {
            return vd;
        }

        public void setVd(double vd) {
            this.vd = vd;
        }
    }

    public static class MBean {
        /**
         * br : 160000
         * fid : 0
         * size : 7123590
         * vd : 0.0324002
         */

        private int br;
        private String fid;
        private int size;
        private double vd;

        public int getBr() {
            return br;
        }

        public void setBr(int br) {
            this.br = br;
        }

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public double getVd() {
            return vd;
        }

        public void setVd(double vd) {
            this.vd = vd;
        }
    }

    public static class LBean {
        /**
         * br : 96000
         * fid : 0
         * size : 4284811
         * vd : -2.65076E-4
         */

        private int br;
        private String fid;
        private int size;
        private double vd;

        public int getBr() {
            return br;
        }

        public void setBr(int br) {
            this.br = br;
        }

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public double getVd() {
            return vd;
        }

        public void setVd(double vd) {
            this.vd = vd;
        }
    }

    public static class ArBean {
        /**
         * id : 48514
         * name : Boz Scaggs
         * tns : []
         * alias : []
         */

        private long id;
        private String name;
        private List<?> tns;
        private List<?> alias;

        public String getId() {
            return String.valueOf(id);
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<?> getTns() {
            return tns;
        }

        public void setTns(List<?> tns) {
            this.tns = tns;
        }

        public List<?> getAlias() {
            return alias;
        }

        public void setAlias(List<?> alias) {
            this.alias = alias;
        }
    }
}

