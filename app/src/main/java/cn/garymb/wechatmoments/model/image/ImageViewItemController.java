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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.widget.ImageView;

import cn.garymb.wechatmoments.StaticApplication;
import cn.garymb.wechatmoments.common.BitmapUtils;

public class ImageViewItemController extends AbstractImageItemController {
    private static final int FADE_IN_TIME = 400;

    protected ImageView mImageView;

    private int mDefaultResId;

    public ImageViewItemController(ImageView view, int defaultRes) {
        mDefaultResId = defaultRes;
        mImageView = view;
    }

    @Override
    protected void onImageItemChanged(int width, int height) {
        if (mImageView != null) {
            Bitmap defaultBitmap = BitmapUtils.createNewBitmapWithResource
                    (StaticApplication.peekInstance().getResources(), mDefaultResId, new int[]{width, height}, true);
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageView.setImageBitmap(defaultBitmap);
        }
    }

    @Override
    public void setBitmap(Bitmap bmp, boolean isAnimationNeeded, boolean isScaleNeeded) {
        if (mImageView != null) {
            if (bmp != null) {
                if(isScaleNeeded) {
                    mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                if (isAnimationNeeded) {
                    showTransitionDrawable(mImageView, bmp);
                } else {
                    mImageView.setImageBitmap(bmp);
                }
                mIsLoaded = true;
            }
        }
    }

    private void showTransitionDrawable(ImageView v, Bitmap bitmap) {
        final TransitionDrawable td = new TransitionDrawable(
                new Drawable[] {
                        new ColorDrawable(
                                android.R.color.transparent),
                        new BitmapDrawable(StaticApplication.peekInstance().getResources(),
                                bitmap) });
        v.setImageDrawable(td);
        td.startTransition(FADE_IN_TIME);
    }
}
