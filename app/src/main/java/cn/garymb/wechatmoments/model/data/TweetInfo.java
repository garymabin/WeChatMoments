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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import cn.garymb.wechatmoments.common.Constants;

public class TweetInfo extends BaseInfo {

    public List<String> images;
    public UserInfo sender;
    public String content;
    public List<CommentInfo> comments;

    @Override
    protected void readFromParcel(Parcel source) {
        images = source.createStringArrayList();
        sender = source.readParcelable(UserInfo.class.getClassLoader());
        content = source.readString();
        comments = source.createTypedArrayList(CommentInfo.CREATOR);
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
        if (obj.has(Constants.JSON_KEY_TWEET_IMAGES)) {
            JSONArray imageJsonArray = obj.getJSONArray(Constants.JSON_KEY_TWEET_IMAGES);
            int count = imageJsonArray == null ? 0 : imageJsonArray.length();
            images = new ArrayList<>(count);
            for (int i = 0; i < count; i ++) {
                images.add(imageJsonArray.getJSONObject(i).getString(Constants.JSON_KEY_TWEET_IMAGE_URL));
            }
        }
        if (obj.has(Constants.JSON_KEY_TWEET_COMMENTS)) {
            JSONArray commentJsonArray = obj.getJSONArray(Constants.JSON_KEY_TWEET_COMMENTS);
            int count = commentJsonArray == null ? 0 : commentJsonArray.length();
            comments = new ArrayList<>(count);
            for (int i = 0; i < count; i ++) {
                CommentInfo comment = new CommentInfo();
                comment.fromJSONData(commentJsonArray.getJSONObject(i));
                comments.add(comment);
            }
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
        dest.writeStringList(images);
        dest.writeParcelable(sender, flags);
        dest.writeString(content);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[TweetInfo]: ");
        sb.append("images: ").append(images == null ? "[unspecified]" : dumpImages())
                .append(" sender: ").append(sender == null ? "[unspecified]" : sender.toString())
                .append(" content: ").append(content == null ? "[unspecified]" : content)
                .append(" comments: ").append(comments == null ? "[unspecified]" : dumpComments());
        return sb.toString();
    }

    private String dumpComments() {
        StringBuilder sb = new StringBuilder();
        for (CommentInfo ci : comments) {
            sb.append(ci.toString()).append("\n");
        }
        return sb.toString();
    }

    private String dumpImages() {
        StringBuilder sb = new StringBuilder();
        for (String imageUrl : images) {
            sb.append(imageUrl).append("\n");
        }
        return sb.toString();
    }

    public static final Creator<TweetInfo> CREATOR = new Creator<TweetInfo>() {

        @Override
        public TweetInfo createFromParcel(Parcel source) {
            TweetInfo info = new TweetInfo();
            info.readFromParcel(source);
            return info;
        }

        @Override
        public TweetInfo[] newArray(int size) {
            return new TweetInfo[size];
        }
    };
}
