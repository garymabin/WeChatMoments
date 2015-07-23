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

package cn.garymb.wechatmoments.model.data;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;

public class ImageItem extends BaseInfo {

    public int width;
    public int height;
    public String url;
    public String id;

    public ImageItem(int width, int height, String url) {
        this.width = width;
        this.height = height;
        this.url = url;
        this.id = this.url + "_" + this.width + "_" + this.height;
    }

    //only for Parcelable usage.
    private ImageItem() {
    }

    @Override
    protected void readFromParcel(Parcel source) {
        this.width = source.readInt();
        this.height = source.readInt();
        this.url = source.readString();
        this.id = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(url);
        dest.writeString(id);
    }

    @Override
    public void fromJSONData(JSONObject obj) throws JSONException {
    }

    @Override
    public ByteBuffer toByteBuffer(ByteBuffer buffer) {
        buffer = putInt(buffer, width);
        buffer = putInt(buffer, height);
        buffer = putString(buffer, url);
        buffer = putString(buffer, id);
        return buffer;
    }

    @Override
    public void fromByteBuffer(ByteBuffer buffer) {
        width = getInt(buffer);
        height = getInt(buffer);
        url = getString(buffer);
        id = getString(buffer);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof ImageItem))
            return false;

        ImageItem target = (ImageItem) o;
        if (this.id != null && this.id.equals(target.id))
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {

        @Override
        public ImageItem createFromParcel(Parcel source) {
            ImageItem item = new ImageItem();
            item.readFromParcel(source);
            return item;
        }

        @Override
        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };
}
