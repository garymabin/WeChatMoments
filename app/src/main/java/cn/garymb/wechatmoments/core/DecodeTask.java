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

package cn.garymb.wechatmoments.core;

import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.File;

import cn.garymb.wechatmoments.common.BitmapUtils;
import cn.garymb.wechatmoments.model.IDataOperation;
import cn.garymb.wechatmoments.model.cache.ICache;
import cn.garymb.wechatmoments.model.data.ImageItem;
import cn.garymb.wechatmoments.model.image.BitmapHolder;
import cn.garymb.wechatmoments.model.image.ImageItemInfoHelper;

public class DecodeTask implements ITask<BitmapHolder>, Response.Listener<BitmapHolder>, Response.ErrorListener {

    private TaskCallback<BitmapHolder> mCallback;
    private final BitmapHolder mHolder;
    private final ImageItem mImageItem;
    private ICache<String, Bitmap> mBitmapCache;

    public DecodeTask(BitmapHolder holder, TaskCallback<BitmapHolder> callback, ICache<String, Bitmap> cache) {
        mCallback = callback;
        mHolder = holder;
        mImageItem = mHolder.getParam().getData().getParcelable(IDataOperation.BUNDLE_KEY_PARAM_IMAGE_REQUEST_ITEM);
        mBitmapCache = cache;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
    }

    @Override
    public void onResponse(BitmapHolder response) {
    }

    @Override
    public boolean executeOnExecutors(IExecutors executors) {
        executors.executeOnLocal(this);
        return false;
    }

    @Override
    public Request<?> getVolleyRequest() {
        return null;
    }

    @Override
    public boolean perfromPretreat() {
        String path = ImageItemInfoHelper.getImageSavedPath(mImageItem);
        if (!new File(path).exists()) {
            return false;
        }
        Bitmap bitmap = BitmapUtils.createNewBitmapAndCompressByFile(path, new int[]{mImageItem.width, mImageItem.height}, true);
        if (bitmap != null) {
            mBitmapCache.put(mImageItem.id, bitmap);
            mHolder.setBitmap(bitmap);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void onPretreatResult(int result) {
        mCallback.onTaskFinish(mHolder);
    }
}
