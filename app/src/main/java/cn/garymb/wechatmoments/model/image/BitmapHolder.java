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

package cn.garymb.wechatmoments.model.image;

import android.graphics.Bitmap;
import android.os.Message;

import cn.garymb.wechatmoments.common.ParcelablePoolObject;

public class BitmapHolder {
    private Bitmap mBitmap;
    private int mMessageType;

    private ParcelablePoolObject mOrigParam;

    public BitmapHolder(Message msg, Bitmap bitmap) {
        setBitmap(bitmap);
        setData(msg);
    }

    public BitmapHolder(ParcelablePoolObject param, int messageType, Bitmap bitmap) {
        setBitmap(bitmap);
        mMessageType = messageType;
        mOrigParam = param;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public ParcelablePoolObject getParam() {
        return mOrigParam;
    }

    public int getMessageType() {
        return mMessageType;
    }

    private void setData(Message mData) {
        this.mOrigParam = (ParcelablePoolObject) mData.obj;
        this.mMessageType = mData.what;
    }
}
