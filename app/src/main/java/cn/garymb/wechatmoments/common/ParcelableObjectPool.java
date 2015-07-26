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

public class ParcelableObjectPool {
    protected final int MAX_PREE_OBJECT_INDEX;

    protected ParcelablePoolObjectFactory mFactory;
    protected ParcelablePoolObject[] mFreeObjects;

    protected int mFreeObjectIndex = -1;

    public ParcelableObjectPool(ParcelablePoolObjectFactory factory, int maxSize) {
        MAX_PREE_OBJECT_INDEX = maxSize - 1;
        mFreeObjects = new ParcelablePoolObject[maxSize];
        mFactory = factory;
    }

    public synchronized ParcelablePoolObject newObject() {
        ParcelablePoolObject obj;
        if (mFreeObjectIndex == -1) {
            obj = mFactory.createPoolObject();
        } else {
            obj = mFreeObjects[mFreeObjectIndex];
            mFreeObjectIndex--;
        }
        obj.initializePoolObject();
        return obj;
    }

    public synchronized void freeObject(ParcelablePoolObject obj) {
        if (obj != null) {
            obj.finalizePoolObject();
            if (mFreeObjectIndex < MAX_PREE_OBJECT_INDEX) {
                mFreeObjectIndex ++;
                mFreeObjects[mFreeObjectIndex] = obj;
            }
        }
    }
}
