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
import android.graphics.Bitmap.Config;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import cn.garymb.wechatmoments.core.request.ExtendImageRequest;
import cn.garymb.wechatmoments.model.IDataOperation;
import cn.garymb.wechatmoments.model.cache.ICache;
import cn.garymb.wechatmoments.model.data.ImageItem;
import cn.garymb.wechatmoments.model.image.BitmapHolder;

public class ImageTask implements ITask<BitmapHolder>, Response.Listener<BitmapHolder>, Response.ErrorListener {

    private ICache<String, Bitmap> mBitmapCache;
    private TaskCallback<BitmapHolder> mCallback;
    private final BitmapHolder mHolder;
    private final ImageItem mImageItem;

    public ImageTask(BitmapHolder holder, TaskCallback<BitmapHolder> callback,
                     ICache<String, Bitmap> cache) {
        mBitmapCache = cache;
        mCallback = callback;
        mHolder = holder;
        mImageItem = mHolder.getParam().getData().getParcelable(IDataOperation.BUNDLE_KEY_PARAM_IMAGE_REQUEST_ITEM);
    }

    @Override
    public boolean perfromPretreat() {
        return false;
    }

    @Override
    public Request<?> getVolleyRequest() {
        return new ExtendImageRequest(mImageItem.url, this, mHolder, Config.ARGB_8888, this);
    }

    @Override
    public boolean executeOnExecutors(IExecutors executors) {
        executors.executeOnVolley(this);
        return false;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mHolder.getParam().getData().putString(IDataOperation.BUNDLE_KEY_RESULT_MESSAGE, error.getMessage());
        mCallback.onTaskFinish(mHolder);
    }

    @Override
    public void onResponse(BitmapHolder response) {
        mBitmapCache.put(mImageItem.id, response.getBitmap());
        mCallback.onTaskFinish(response);
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
