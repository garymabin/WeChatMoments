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

import cn.garymb.wechatmoments.model.data.ImageItem;

public abstract class AbstractImageItemController {

    protected boolean mIsLoaded = false;
    protected ImageItem mImageItem = null;


    public boolean isLoaded(ImageItem item) {
        return mIsLoaded && mImageItem != null && mImageItem.equals(item);
    }

    public void setImageItem(ImageItem item) {
        mImageItem = item;
        mIsLoaded = false;
        onImageItemChanged(item.width, item.height);
    }

    public void setImageItemWithoutDealChange(ImageItem item) {
        mImageItem = item;
        mIsLoaded = false;
    }

    protected abstract void onImageItemChanged(int width, int height);

    public abstract void setBitmap(Bitmap bmp, boolean isAnimationNeeded, boolean isScaleNeeded);

    public ImageItem getImageItem() {
        return mImageItem;
    }
}
