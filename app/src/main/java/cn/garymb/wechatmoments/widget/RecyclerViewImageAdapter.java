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

package cn.garymb.wechatmoments.widget;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import cn.garymb.wechatmoments.R;
import cn.garymb.wechatmoments.common.ParcelablePoolObject;
import cn.garymb.wechatmoments.controller.IViewController;
import cn.garymb.wechatmoments.model.IDataOperation;
import cn.garymb.wechatmoments.model.Model;
import cn.garymb.wechatmoments.model.data.ImageItem;
import cn.garymb.wechatmoments.model.image.AbstractImageItemController;
import cn.garymb.wechatmoments.model.image.BitmapHolder;
import cn.garymb.wechatmoments.model.image.ImageItemInfoHelper;
import cn.garymb.wechatmoments.model.image.ImageViewItemController;

public abstract class RecyclerViewImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IViewController {

    public static class BaseImageViewHolder extends RecyclerView.ViewHolder {
        public AbstractImageItemController[] imageControllers;
        public BaseImageViewHolder(View itemView) {
            super(itemView);
        }
    }

    private LinearLayoutManager mLayoutManager;

    public RecyclerViewImageAdapter(LinearLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    protected void requestImage(ImageItem item) {
        Message msg;
        ParcelablePoolObject poolObj = Model.peekInstance().peekPoolObject();
        Bundle param = poolObj.getData();
        param.putParcelable(IDataOperation.BUNDLE_KEY_PARAM_IMAGE_REQUEST_ITEM, item);
        if (ImageItemInfoHelper.isImageExist(item)) {
            msg = Message.obtain(null, IDataOperation.REQUEST_TYPE_DECODE_IMAGE_BITMAP, poolObj);
        } else {
            msg = Message.obtain(null, IDataOperation.REQUEST_TYPE_GET_IMAGE_BITMAP, poolObj);
        }
        msg.setData(param);
        Model.peekInstance().requestDataOperation(this, msg);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case IDataOperation.REQUEST_TYPE_GET_IMAGE_BITMAP:
            case IDataOperation.REQUEST_TYPE_DECODE_IMAGE_BITMAP:
                BitmapHolder holder = ((BitmapHolder) msg.obj);
                if (msg.arg1 == IDataOperation.REQUEST_RESULT_SUCCESS) {
                    ImageItem item = holder.getParam().getData().getParcelable(
                            IDataOperation.BUNDLE_KEY_PARAM_IMAGE_REQUEST_ITEM);
                    AbstractImageItemController controller = findTargetImageController(item);
                    if (controller != null && !controller.isLoaded(item)) {
                        controller.setBitmap(holder.getBitmap(), true, true);
                    }
                }
                Model.peekInstance().freePoolObject(holder.getParam());
                break;

            default:
                break;
        }
        return true;
    }

    private AbstractImageItemController findTargetImageController(ImageItem item) {
        if (item == null)
            return null;

        if (mLayoutManager != null) {
            int count = mLayoutManager.findLastVisibleItemPosition() -
                    mLayoutManager.findFirstVisibleItemPosition() + 1;
            for (int i = 0; i < count; i++) {
                View v = mLayoutManager.getChildAt(i);
                if (v == null)
                    continue;
                Object ob = v.getTag();
                if (ob == null || !(ob instanceof BaseImageViewHolder))
                    continue;
                BaseImageViewHolder holder = (BaseImageViewHolder) ob;
                for (AbstractImageItemController c : holder.imageControllers) {
                    if (c != null && item.equals(c.getImageItem()))
                        return c;
                }
            }
        }
        return null;
    }

    protected void loadImage(BaseImageViewHolder bih, int index, ImageView imageView, ImageItem item) {
        Bitmap b = Model.peekInstance().getBitmap(item);
        if (b != null) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(b);
        } else {
            bih.imageControllers[index] =
                    new ImageViewItemController(imageView, R.mipmap.ic_single_img_default);
            bih.imageControllers[index].setImageItem(item);
            requestImage(item);
        }
    }

    protected void updateItemAtPosition(int position) {
        int firstVisiblePosition = mLayoutManager.findFirstVisibleItemPosition();
        int viewPos = position - firstVisiblePosition;
        if (viewPos >= 0 && viewPos < mLayoutManager.getChildCount()) {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) mLayoutManager.getChildAt(viewPos).getTag();
            onBindViewHolder(holder, position);
        }
    }
}
