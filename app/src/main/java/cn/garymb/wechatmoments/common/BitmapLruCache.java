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

package cn.garymb.wechatmoments.common;

import android.graphics.Bitmap;

public class BitmapLruCache<K> extends LruCache<K, Bitmap> {

    protected long mMaxByteSize = -1;
    protected long mByteSize = 0;

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public BitmapLruCache(int maxSize, long maxByteSize) {
        super(maxSize);
        mByteSize = 0;
        mMaxByteSize = maxByteSize;
    }

    /**
     * Caches {@code value} for {@code key}. The value is moved to the head of
     * the queue.
     *
     * @return the previous value mapped by {@code key}.
     */
    @Override
    public Bitmap put(K key, Bitmap value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }

        //only calculate consumed memory size when mMaxByteSize is set.
        if (mMaxByteSize > 0) {
            long bytes = ((long) value.getRowBytes()) * value.getHeight();
            //in case we are recycle a bitmap that is now using
            while (bytes + mByteSize > mMaxByteSize && size() >= 3)
                trimToSize(size() - 1);
            mByteSize += bytes;
        }
        return super.put(key, value);
    }
}
