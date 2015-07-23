/*
 * Copyright 2015 Garymabin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.garymb.wechatmoments;

import android.app.Application;
import android.os.Environment;

import java.io.File;

import cn.garymb.wechatmoments.common.Constants;

public class StaticApplication extends Application {

    private static StaticApplication INSTANCE;

    private String mBaseCacheDir;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public static StaticApplication peekInstance() {
        return INSTANCE;
    }

    private void initCacheDir() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            mBaseCacheDir = getExternalCacheDir().toString();
        } else {
            mBaseCacheDir = getCacheDir().toString();
        }
        File dir = new File(mBaseCacheDir + File.separator + Constants.CACHE_IMAGES_DIR);
        dir.mkdirs();
    }

    public String getCacheDir(int type) {
        if (type == Constants.CACHE_IMAGES_TYPE) {
            return mBaseCacheDir + File.separator + Constants.CACHE_IMAGES_DIR;
        } else {
            return null;
        }
    }
}
