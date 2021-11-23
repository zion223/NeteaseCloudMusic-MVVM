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

package com.netease.music;

import com.kunminx.architecture.BaseApplication;
import com.kunminx.architecture.utils.Utils;
import com.netease.lib_audio.app.AudioHelper;
import com.netease.music.service.MusicService;

/**
 * @author Administrator
 */
public class NeteaseMusicApplication extends BaseApplication {

    //TODO tip：可借助 Application 来管理一个应用级 的 SharedViewModel，
    // 实现全应用范围内的 生命周期安全 且 事件源可追溯的 视图控制器 事件通知。

    @Override
    public void onCreate() {
        super.onCreate();
        AudioHelper.init(this);
        MusicService.startMusicService();
        Utils.init(this);
    }

}
