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
import android.text.TextUtils;
import android.util.Log;

import java.util.WeakHashMap;

import cn.garymb.wechatmoments.controller.IViewController;
import cn.garymb.wechatmoments.core.DecodeTask;
import cn.garymb.wechatmoments.core.IExecutors;
import cn.garymb.wechatmoments.core.ITask;
import cn.garymb.wechatmoments.core.ImageTask;
import cn.garymb.wechatmoments.model.cache.BitmapCache;
import cn.garymb.wechatmoments.model.cache.ICache;
import cn.garymb.wechatmoments.model.data.ImageItem;
import cn.garymb.wechatmoments.model.image.BitmapHolder;

public class ImageModelHelper extends Handler implements IDataOperation, ITask.TaskCallback<BitmapHolder> {

    private static final String TAG = "ImageModelHelper";

    private WeakHashMap<BitmapHolder, IViewController> mNotifyTargets = new WeakHashMap<>();
    private ICache<String, Bitmap> mCache;
    private IExecutors mExecutors;

    public ImageModelHelper(IExecutors executors) {
        mExecutors = executors;
        mCache = new BitmapCache();
    }


    @Override
    public void requestDataOperation(IViewController target, Message msg) {
        BitmapHolder holder = new BitmapHolder(msg, null);
        ITask<?> task;
        if (msg.what == IDataOperation.REQUEST_TYPE_DECODE_IMAGE_BITMAP) {
            task = new DecodeTask(holder, this, mCache);
        } else {
            task = new ImageTask(holder, this, mCache);
        }
        task.executeOnExecutors(mExecutors);
        mNotifyTargets.put(holder, target);
    }

    @Override
    public void cancelDataOperation(IViewController target, Message msg) {
        //TODO
    }

    @Override
    public void onTaskTimeout(BitmapHolder result) {
        //TODO
    }

    @Override
    public void onTaskFinish(BitmapHolder result) {
        if (result == null) {
            Log.e(TAG, "why we received a null BitmapHolder ??");
            return;
        }
        int resultCode = IDataOperation.REQUEST_RESULT_FAILED;
        if (result.getBitmap() != null) {
            resultCode = IDataOperation.REQUEST_RESULT_SUCCESS;
        }
        mNotifyTargets.get(result).handleMessage(
                Message.obtain(null, result.getMessageType(), resultCode, 0, result));
    }

    /*package*/ Bitmap getBitmap(ImageItem item) {
        if (TextUtils.isEmpty(item.url)) {
            return null;
        }
        return mCache.getCache(item.id);
    }
}
