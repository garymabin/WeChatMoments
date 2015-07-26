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

public class UserInfo extends BaseInfo {

    public String profileImage;
    public String avatar;
    public String nick;
    public String userName;

    @Override
    protected void readFromParcel(Parcel source) {
        profileImage = source.readString();
        avatar = source.readString();
        nick = source.readString();
        userName = source.readString();
    }

    @Override
    public void fromJSONData(JSONObject obj) throws JSONException {
        if (obj.has(Constants.JSON_KEY_USER_PROFILE_NAME)) {
            profileImage = obj.getString(Constants.JSON_KEY_USER_PROFILE_NAME);
        }
        if (obj.has(Constants.JSON_KEY_USER_AVATAR)) {
            avatar = obj.getString(Constants.JSON_KEY_USER_AVATAR);
        }
        if (obj.has(Constants.JSON_KEY_USER_NICK)) {
            nick = obj.getString(Constants.JSON_KEY_USER_NICK);
        }
        if (obj.has(Constants.JSON_KEY_USER_USERNAME)) {
            userName = obj.getString(Constants.JSON_KEY_USER_USERNAME);
        }
    }

    @Override
    public ByteBuffer toByteBuffer(ByteBuffer buffer) {
        //fill in this if we need any binary format serialization.
        return buffer;
    }

    @Override
    public void fromByteBuffer(ByteBuffer buffer) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(profileImage);
        dest.writeString(avatar);
        dest.writeString(nick);
        dest.writeString(userName);
    }

    @Override
    public String toString() {
        String str = "[UserInfo]: ";
        str += "profileImage: ";
        str += (profileImage == null ? "[unspecified]" : profileImage);
        str += (" avatar: ");
        str += (avatar == null ? "[unspecified]" : avatar);
        str += (" nick: ");
        str += (nick == null ? "[unspecified]" : nick);
        str += (" userName: ");
        str += (userName == null ? "[unspecified]" : userName);
        return str;
    }
}
