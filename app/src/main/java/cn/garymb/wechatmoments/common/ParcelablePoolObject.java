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

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class ParcelablePoolObject implements Parcelable, IPoolObject{
    private Bundle mInternalParcel = new Bundle();

    @Override
    public void initializePoolObject() {
    }

    @Override
    public void finalizePoolObject() {
        mInternalParcel.clear();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(mInternalParcel);
    }

    public static final Creator<ParcelablePoolObject> CREATOR = new Creator<ParcelablePoolObject>() {

        @Override
        public ParcelablePoolObject createFromParcel(Parcel source) {
            ParcelablePoolObject ob = new ParcelablePoolObject();
            ob.mInternalParcel = source.readBundle();
            return ob;
        }

        @Override
        public ParcelablePoolObject[] newArray(int size) {
            return new ParcelablePoolObject[size];
        }
    };

    public final Bundle getData() {
        return mInternalParcel;
    }
}
