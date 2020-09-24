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

package com.netease.music.data.repository;

import android.os.Handler;

import androidx.lifecycle.MutableLiveData;


/**
 * Create by KunMinX at 19/10/29
 */
public class DataRepository implements ILocalSource, IRemoteSource {

    private static final DataRepository S_REQUEST_MANAGER = new DataRepository();
    private MutableLiveData<String> responseCodeLiveData;

    private DataRepository() {
    }

    public static DataRepository getInstance() {
        return S_REQUEST_MANAGER;
    }

    public MutableLiveData<String> getResponseCodeLiveData() {
        if (responseCodeLiveData == null) {
            responseCodeLiveData = new MutableLiveData<>();
        }
        return responseCodeLiveData;
    }

    public void requestResponseCode() {
        //模拟2s中后返回数据
        new Handler().postDelayed(() -> responseCodeLiveData.postValue("code"), 2000);
    }

}
