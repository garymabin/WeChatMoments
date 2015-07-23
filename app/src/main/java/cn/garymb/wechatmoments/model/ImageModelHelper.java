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

package cn.garymb.wechatmoments.model;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import java.util.WeakHashMap;

import cn.garymb.wechatmoments.controller.IViewController;
import cn.garymb.wechatmoments.core.IExecutors;
import cn.garymb.wechatmoments.model.cache.BitmapCache;
import cn.garymb.wechatmoments.model.cache.ICache;
import cn.garymb.wechatmoments.model.image.BitmapHolder;

public class ImageModelHelper extends Handler implements IDataOperation {

    private WeakHashMap<BitmapHolder, IViewController> mNotifyTargets = new WeakHashMap<BitmapHolder, IViewController>();
    private ICache<String, Bitmap> mCache;
    private IExecutors mExecutors;

    public ImageModelHelper(IExecutors executors) {
        mExecutors = executors;
        mCache = new BitmapCache();
    }


    @Override
    public void requestDataOperation(IViewController target, Message msg) {

    }

    @Override
    public void cancelDataOperation(IViewController target, Message msg) {

    }
}
