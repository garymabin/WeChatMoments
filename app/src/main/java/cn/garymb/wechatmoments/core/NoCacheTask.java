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

package cn.garymb.wechatmoments.core;

import android.os.Bundle;
import android.os.Message;

import com.android.volley.Request;

import cn.garymb.wechatmoments.common.Constants;
import cn.garymb.wechatmoments.common.ParcelablePoolObject;
import cn.garymb.wechatmoments.core.request.TweetsRequest;
import cn.garymb.wechatmoments.core.request.UserInfoRequest;
import cn.garymb.wechatmoments.model.IDataOperation;

public class NoCacheTask extends BaseDataTask {

    public NoCacheTask(Message data, TaskCallback<Message> callback) {
        super(data, callback);
    }

    @Override
    public boolean perfromPretreat() {
        return false;
    }

    @Override
    public Request<?> getVolleyRequest() {
        Request<?> request = null;
        Bundle param = ((ParcelablePoolObject) mData.obj).getData();
        switch (mData.what) {
            case IDataOperation.REQEST_TYPE_GET_USER_INFO:
                request = new UserInfoRequest(Constants.USER_URL, this, this, mData);
                break;
            case IDataOperation.REQEST_TYPE_GET_TWEETS_INFO:
                request = new TweetsRequest(Constants.USER_URL, this, this, mData);
                break;
        }
        return request;
    }

    @Override
    public boolean executeOnExecutors(IExecutors executors) {
        executors.executeOnVolley(this);
        return false;
    }
}
