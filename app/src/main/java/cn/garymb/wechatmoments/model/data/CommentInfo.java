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

import cn.garymb.wechatmoments.common.Constants;

public class CommentInfo extends BaseInfo {

    public UserInfo sender;
    public String content;

    @Override
    protected void readFromParcel(Parcel source) {
        sender = source.readParcelable(UserInfo.class.getClassLoader());
        content = source.readString();
    }

    @Override
    public void fromJSONData(JSONObject obj) throws JSONException {
        if (obj.has(Constants.JSON_KEY_TWEET_SENDER)) {
            sender = new UserInfo();
            sender.fromJSONData(obj.getJSONObject(Constants.JSON_KEY_TWEET_SENDER));
        }
        if (obj.has(Constants.JSON_KEY_TWEET_CONTENT)) {
            content = obj.getString(Constants.JSON_KEY_TWEET_CONTENT);
        }
    }

    @Override
    public ByteBuffer toByteBuffer(ByteBuffer buffer) {
        return null;
    }

    @Override
    public void fromByteBuffer(ByteBuffer buffer) {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(sender, flags);
        dest.writeString(content);
    }

    @Override
    public String toString() {
        String str = "[CommentInfo]: sender: ";
        str += (sender == null ? "[unspecified]" : sender.toString());
        str += (" content: ");
        str += (sender == null ? "[unspecified]" : content);
        return str;
    }

    public static final Creator<CommentInfo> CREATOR = new Creator<CommentInfo>() {

        @Override
        public CommentInfo createFromParcel(Parcel source) {
            CommentInfo info = new CommentInfo();
            info.readFromParcel(source);
            return info;
        }

        @Override
        public CommentInfo[] newArray(int size) {
            return new CommentInfo[size];
        }
    };
}
