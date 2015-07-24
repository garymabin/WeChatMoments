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

package cn.garymb.wechatmoments.test;

import android.os.Bundle;
import android.os.Message;
import android.test.InstrumentationTestCase;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import cn.garymb.wechatmoments.common.ParcelablePoolObject;
import cn.garymb.wechatmoments.controller.IViewController;
import cn.garymb.wechatmoments.model.IDataOperation;
import cn.garymb.wechatmoments.model.Model;
import cn.garymb.wechatmoments.model.data.TweetInfo;
import cn.garymb.wechatmoments.model.data.UserInfo;

public class ModelTest extends InstrumentationTestCase {

    private CountDownLatch signal;

    public static class MockViewController implements IViewController {

        private CountDownLatch internalSignal;

        public MockViewController(CountDownLatch signal) {
            internalSignal = signal;
        }
        @Override
        public boolean handleMessage(Message msg) {
            ParcelablePoolObject ppo = (ParcelablePoolObject) msg.obj;
            Bundle result = ppo.getData();
            switch (msg.what) {
                case IDataOperation.REQUEST_TYPE_GET_USER_INFO:
                    UserInfo user = result.getParcelable(IDataOperation.BUNDLE_KEY_RESULT_USER);
                    assertNotNull(user);
                    Log.v("ModelTest", "testRetriveUserInfo: " + user.toString());
                    internalSignal.countDown();
                    break;
                case IDataOperation.REQUEST_TYPE_GET_TWEETS_INFO:
                    List<TweetInfo> tweets = result.getParcelableArrayList(IDataOperation.BUNDLE_KEY_RESULT_TWEETS);
                    assertNotNull(tweets);
                    dumpTweets(tweets);
                    internalSignal.countDown();
                    break;
                case IDataOperation.REQUEST_TYPE_GET_IMAGE_BITMAP:
                case IDataOperation.REQUEST_TYPE_DECODE_IMAGE_BITMAP:
                    break;
            }
            Model.peekInstance().freePoolObject(ppo);
            return false;
        }

        private void dumpTweets(List<TweetInfo> tweets) {
            Log.v("ModelTest", "testRetriveTweets: \n");
            for (TweetInfo tweet: tweets) {
                Log.v("ModelTest", tweet.toString());
            }
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Model.peekInstance();
    }

    public void testRetriveUserInfo() throws InterruptedException {
        signal = new CountDownLatch(1);
        ParcelablePoolObject ppo = Model.peekInstance().peekPoolObject();
        Message msg = Message.obtain(null, IDataOperation.REQUEST_TYPE_GET_USER_INFO, ppo);
        Model.peekInstance().requestDataOperation(new MockViewController(signal), msg);
        signal.await();
    }

    public void testRetriveTweets() throws InterruptedException {
        signal = new CountDownLatch(1);
        ParcelablePoolObject ppo = Model.peekInstance().peekPoolObject();
        Message msg = Message.obtain(null, IDataOperation.REQUEST_TYPE_GET_TWEETS_INFO, ppo);
        Model.peekInstance().requestDataOperation(new MockViewController(signal), msg);
        signal.await();
    }
 }
