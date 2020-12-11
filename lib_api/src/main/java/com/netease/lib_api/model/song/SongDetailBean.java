package com.netease.lib_api.model.song;


import java.util.List;

public class SongDetailBean {

    /**
     * songs : [{"name":"JoJo","id":27917548,"pst":0,"t":0,"ar":[{"id":48514,"name":"Boz Scaggs","tns":[],"alias":[]}],"alia":[],"pop":85,"st":0,"rt":"","fee":8,"v":6,"crbt":null,"cf":"","al":{"id":2694934,"name":"The Essential Boz Scaggs","picUrl":"https://p1.music.126.net/_YmTuIMoG0dh2OJSOE-LPw==/17829680556434243.jpg","tns":[],"pic_str":"17829680556434243","pic":17829680556434244},"dt":354746,"h":{"br":320000,"fid":0,"size":14220536,"vd":-2.65076E-4},"m":{"br":160000,"fid":0,"size":7123590,"vd":0.0324002},"l":{"br":96000,"fid":0,"size":4284811,"vd":-2.65076E-4},"a":null,"cd":"2","no":1,"rtUrl":null,"ftype":0,"rtUrls":[],"djId":0,"copyright":1,"s_id":0,"mark":0,"mv":0,"mst":9,"cp":7001,"rtype":0,"rurl":null,"publishTime":1382976000007}]
     * privileges : [{"id":27917548,"fee":8,"payed":0,"st":0,"pl":128000,"dl":0,"sp":7,"cp":1,"subp":1,"cs":false,"maxbr":999000,"fl":128000,"toast":false,"flag":256,"preSell":false}]
     * code : 200
     */

    private int code;
    private List<DailyRecommendSongsBean> songs;
    private List<PrivilegesBean> privileges;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DailyRecommendSongsBean> getSongs() {
        return songs;
    }

    public void setSongs(List<DailyRecommendSongsBean> songs) {
        this.songs = songs;
    }

    public List<PrivilegesBean> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<PrivilegesBean> privileges) {
        this.privileges = privileges;
    }


    public static class PrivilegesBean {
        /**
         * id : 27917548
         * fee : 8
         * payed : 0
         * st : 0
         * pl : 128000
         * dl : 0
         * sp : 7
         * cp : 1
         * subp : 1
         * cs : false
         * maxbr : 999000
         * fl : 128000
         * toast : false
         * flag : 256
         * preSell : false
         */

        private long id;
        private int fee;
        private int payed;
        private int st;
        private int pl;
        private int dl;
        private int sp;
        private int cp;
        private int subp;
        private boolean cs;
        private int maxbr;
        private int fl;
        private boolean toast;
        private int flag;
        private boolean preSell;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public int getPayed() {
            return payed;
        }

        public void setPayed(int payed) {
            this.payed = payed;
        }

        public int getSt() {
            return st;
        }

        public void setSt(int st) {
            this.st = st;
        }

        public int getPl() {
            return pl;
        }

        public void setPl(int pl) {
            this.pl = pl;
        }

        public int getDl() {
            return dl;
        }

        public void setDl(int dl) {
            this.dl = dl;
        }

        public int getSp() {
            return sp;
        }

        public void setSp(int sp) {
            this.sp = sp;
        }

        public int getCp() {
            return cp;
        }

        public void setCp(int cp) {
            this.cp = cp;
        }

        public int getSubp() {
            return subp;
        }

        public void setSubp(int subp) {
            this.subp = subp;
        }

        public boolean isCs() {
            return cs;
        }

        public void setCs(boolean cs) {
            this.cs = cs;
        }

        public int getMaxbr() {
            return maxbr;
        }

        public void setMaxbr(int maxbr) {
            this.maxbr = maxbr;
        }

        public int getFl() {
            return fl;
        }

        public void setFl(int fl) {
            this.fl = fl;
        }

        public boolean isToast() {
            return toast;
        }

        public void setToast(boolean toast) {
            this.toast = toast;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public boolean isPreSell() {
            return preSell;
        }

        public void setPreSell(boolean preSell) {
            this.preSell = preSell;
        }

        @Override
        public String toString() {
            return "PrivilegesBean{" +
                    "id=" + id +
                    ", fee=" + fee +
                    ", payed=" + payed +
                    ", st=" + st +
                    ", pl=" + pl +
                    ", dl=" + dl +
                    ", sp=" + sp +
                    ", cp=" + cp +
                    ", subp=" + subp +
                    ", cs=" + cs +
                    ", maxbr=" + maxbr +
                    ", fl=" + fl +
                    ", toast=" + toast +
                    ", flag=" + flag +
                    ", preSell=" + preSell +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SongDetailBean{" +
                "code=" + code +
                ", songs=" + songs +
                ", privileges=" + privileges +
                '}';
    }
}
