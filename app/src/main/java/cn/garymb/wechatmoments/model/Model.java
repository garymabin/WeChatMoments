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
import android.os.Message;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;

import cn.garymb.wechatmoments.StaticApplication;
import cn.garymb.wechatmoments.common.OkHttpStack;
import cn.garymb.wechatmoments.common.ParcelableObjectPool;
import cn.garymb.wechatmoments.common.ParcelablePoolObject;
import cn.garymb.wechatmoments.common.ParcelablePoolObjectFactory;
import cn.garymb.wechatmoments.controller.IViewController;
import cn.garymb.wechatmoments.core.DataExecutors;
import cn.garymb.wechatmoments.model.data.ImageItem;

public class Model implements  IDataOperation{

    private static final int MAX_BUNDLE_FACTORY_SIZE = 20;

    private static Model INSTANCE;
    private RequestQueue mDataQueue;
    private ParcelableObjectPool mBundlePool;
    private DataExecutors mExecutors;
    private ImageModelHelper mImageModelHelper;
    private DataModelHelper mDataModelHelper;

    public static Model peekInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Model(StaticApplication.peekInstance());
        }
        return INSTANCE;
    }

    private Model(StaticApplication app) {
        mDataQueue = Volley.newRequestQueue(app, new OkHttpStack(new OkHttpClient()));
        mBundlePool = new ParcelableObjectPool(new ParcelablePoolObjectFactory(), MAX_BUNDLE_FACTORY_SIZE);
        mExecutors = new DataExecutors(mDataQueue);
        mImageModelHelper = new ImageModelHelper(mExecutors);
        mDataModelHelper = new DataModelHelper(mExecutors);
    }

    @Override
    public void requestDataOperation(IViewController target, Message msg) {
        if (msg.what == IDataOperation.REQUEST_TYPE_GET_IMAGE_BITMAP ||
                msg.what == IDataOperation.REQUEST_TYPE_DECODE_IMAGE_BITMAP) {
            mImageModelHelper.requestDataOperation(target, msg);
        } else {
            mDataModelHelper.requestDataOperation(target, msg);
        }
    }

    @Override
    public void cancelDataOperation(IViewController target, Message msg) {
        if (msg.what == IDataOperation.REQUEST_TYPE_GET_IMAGE_BITMAP ||
                msg.what == IDataOperation.REQUEST_TYPE_DECODE_IMAGE_BITMAP) {
            mImageModelHelper.cancelDataOperation(target, msg);
        } else {
            mDataModelHelper.cancelDataOperation(target, msg);
        }
    }

    public Bitmap getBitmap(ImageItem item) {
        return mImageModelHelper.getBitmap(item);
    }

    public ParcelablePoolObject peekPoolObject() {
        return mBundlePool.newObject();
    }

    public void freePoolObject(ParcelablePoolObject obj) {
        mBundlePool.freeObject(obj);
    }
}
