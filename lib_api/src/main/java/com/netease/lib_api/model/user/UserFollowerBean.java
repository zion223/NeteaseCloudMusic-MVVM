package com.netease.lib_api.model.user;

import com.netease.lib_api.model.search.UserSearchBean;

import java.util.ArrayList;

public class UserFollowerBean {

    private ArrayList<UserSearchBean.ResultBean.UserprofilesBean> follow;

    public ArrayList<UserSearchBean.ResultBean.UserprofilesBean> getFollow() {
        return follow;
    }

    public void setFollow(ArrayList<UserSearchBean.ResultBean.UserprofilesBean> follow) {
        this.follow = follow;
    }
}
