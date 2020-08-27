package com.netease.lib_api.model.search;

import androidx.room.Entity;


/**
 * 用于存放历史搜索的GreenDao Bean
 */
@Entity
public class SearchHistoryBean {

    String keyowrds;

    public SearchHistoryBean(String keyowrds) {
        this.keyowrds = keyowrds;
    }

    public SearchHistoryBean() {
    }

    public String getKeyowrds() {
        return keyowrds;
    }

    public void setKeyowrds(String keyowrds) {
        this.keyowrds = keyowrds;
    }
}
