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

package cn.garymb.wechatmoments.model.cache;

import android.graphics.Bitmap;
import android.util.Log;

import cn.garymb.wechatmoments.common.BitmapLruCache;

public class BitmapCache implements ICache<String, Bitmap> {

    private static final int DEFAULT_BITMAP_CACHE_COUNT = 200;
    private static final String TAG = "BitmapCache";

    private BitmapLruCache<String> mInternalCache;

    public BitmapCache() {
        Runtime rt = Runtime.getRuntime();
        long maxMemory = rt.maxMemory();
        Log.i(TAG, "set maxMemory to:" + Long.toString(maxMemory));
        mInternalCache = new BitmapLruCache<>(DEFAULT_BITMAP_CACHE_COUNT, maxMemory / 10);
    }

    @Override
    public Bitmap getCache(String key) {
        return mInternalCache.get(key);
    }

    @Override
    public void put(String key, Bitmap cache) {
        mInternalCache.put(key, cache);
    }

    @Override
    public void remove(String key) {
        mInternalCache.remove(key);
    }

    @Override
    public void limitCache(int cacheSize) {
        mInternalCache.trimToSize(cacheSize);
    }

    @Override
    public void clearCache() {
        mInternalCache.evictAll();
    }

}
