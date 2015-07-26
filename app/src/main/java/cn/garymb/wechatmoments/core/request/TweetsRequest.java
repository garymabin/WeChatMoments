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

package cn.garymb.wechatmoments.core.request;

import android.os.Bundle;
import android.os.Message;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;

import cn.garymb.wechatmoments.common.ParcelablePoolObject;
import cn.garymb.wechatmoments.model.IDataOperation;
import cn.garymb.wechatmoments.model.data.TweetInfo;

public class TweetsRequest extends BasicJSONRequest<Message> {

    public TweetsRequest(String url, Response.Listener<Message> listener, Response.ErrorListener errorListener, Message param) {
        super(url, listener, errorListener, param);
    }

    @Override
    protected Message parseResult(String ob) {
        int result = IDataOperation.REQUEST_RESULT_SUCCESS;
        try {
            JSONArray arrays = new JSONArray(ob);
            int size = arrays.length();
            ArrayList<TweetInfo> tweets = new ArrayList<>(size);
            for (int i = 0; i < size; i ++) {
                TweetInfo tweet = new TweetInfo();
                tweet.fromJSONData(arrays.getJSONObject(i));
                // ignore tweets that error occurs.
                if (!tweet.isErrorOccurs()) {
                    tweets.add(tweet);
                }
            }
            Bundle param = ((ParcelablePoolObject) mParam.obj).getData();
            param.putParcelableArrayList(IDataOperation.BUNDLE_KEY_RESULT_TWEETS, tweets);
        } catch (JSONException e ) {
            e.printStackTrace();
            result = IDataOperation.REQUEST_RESULT_FAILED;
        }
        mParam.arg1 = result;
        return mParam;
    }

    @Override
    protected Map<String, String> getParamsFromBundle(Bundle param) {
        return null;
    }
}
