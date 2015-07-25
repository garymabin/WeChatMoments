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

package cn.garymb.wechatmoments;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import cn.garymb.wechatmoments.common.Constants;
import cn.garymb.wechatmoments.common.ParcelablePoolObject;
import cn.garymb.wechatmoments.controller.IViewController;
import cn.garymb.wechatmoments.model.IDataOperation;
import cn.garymb.wechatmoments.model.Model;
import cn.garymb.wechatmoments.model.data.TweetInfo;
import cn.garymb.wechatmoments.model.data.UserInfo;
import cn.garymb.wechatmoments.widget.IView;
import cn.garymb.wechatmoments.widget.TweetsAdapter;


public class MainActivity extends AppCompatActivity implements IView, IViewController {

    private Toolbar mToolBar;
    private RecyclerView mListView;
    private SwipeRefreshLayout mPullToRefreshLayout;
    private TweetsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        initActionBar();
        initView();
        refreshUserProfile();
    }

    private void initView() {
        final Resources res = getResources();
        int[] imageSize = new int[Constants.USER_AVATAR_IMAGE_WIDTH_INDEX + 1];
        imageSize[Constants.TWEET_SENDER_AVATAR_HEIGHT_INDEX] = res.getDimensionPixelSize(R.dimen.tweet_sender_avatar_image_height);
        imageSize[Constants.TWEET_SENDER_AVATAR_WIDTH_INDEX] = res.getDimensionPixelSize(R.dimen.tweet_sender_avatar_image_height);
        imageSize[Constants.TWEET_SENDER_SINGLE_IMAGE_HEIGHT_INDEX] = res.getDimensionPixelSize(R.dimen.tweet_single_image_fixed_height);
        imageSize[Constants.TWEET_SENDER_SINGLE_IMAGE_WIDTH_INDEX] = res.getDimensionPixelSize(R.dimen.tweet_single_image_fixed_width);
        imageSize[Constants.TWEET_SENDER_MORE_IMAGE_HEIGHT_INDEX] = res.getDimensionPixelSize(R.dimen.tweet_more_image_height);
        imageSize[Constants.TWEET_SENDER_MORE_IMAGE_WIDTH_INDEX] = res.getDimensionPixelSize(R.dimen.tweet_more_image_width);
        imageSize[Constants.USER_PROFILE_IMAGE_HEIGHT_INDEX] = res.getDimensionPixelSize(R.dimen.user_profile_image_height);
        imageSize[Constants.USER_PROFILE_IMAGE_WIDTH_INDEX] = res.getDimensionPixelSize(R.dimen.user_profile_image_width);
        imageSize[Constants.USER_AVATAR_IMAGE_HEIGHT_INDEX] = res.getDimensionPixelSize(R.dimen.user_avatar_image_height);
        imageSize[Constants.USER_AVATAR_IMAGE_WIDTH_INDEX] = res.getDimensionPixelSize(R.dimen.user_avatar_image_width);

        mListView = (RecyclerView) findViewById(R.id.moments_list);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mListView.setLayoutManager(llm);
        mAdapter = new TweetsAdapter(llm, this, imageSize);
        mListView.setAdapter(mAdapter);

        mPullToRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh_layout);
        mPullToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
        mPullToRefreshLayout.setRefreshing(true);
        refreshContent();
    }

    private void refreshContent() {
        final Model model = Model.peekInstance();
        ParcelablePoolObject ppo = model.peekPoolObject();
        Message msg = Message.obtain(null, IDataOperation.REQUEST_TYPE_GET_TWEETS_INFO, ppo);
        model.requestDataOperation(this, msg);
    }

    private void refreshUserProfile() {
        final Model model = Model.peekInstance();
        ParcelablePoolObject ppo = model.peekPoolObject();
        Message msg = Message.obtain(null, IDataOperation.REQUEST_TYPE_GET_USER_INFO, ppo);
        model.requestDataOperation(this, msg);
    }

    private void initActionBar() {
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean handleMessage(Message msg) {
        ParcelablePoolObject ppo = (ParcelablePoolObject) msg.obj;
        Bundle result = ppo.getData();
        switch (msg.what) {
            case IDataOperation.REQUEST_TYPE_GET_USER_INFO:
                if (msg.arg1 == IDataOperation.REQUEST_RESULT_SUCCESS) {
                    UserInfo user = result.getParcelable(IDataOperation.BUNDLE_KEY_RESULT_USER);
                    mAdapter.updateProfile(user);
                }
                break;
            case IDataOperation.REQUEST_TYPE_GET_TWEETS_INFO:
                mPullToRefreshLayout.setRefreshing(false);
                if (msg.arg1 == IDataOperation.REQUEST_RESULT_SUCCESS) {
                    List<TweetInfo> tweets = result.getParcelableArrayList(IDataOperation.BUNDLE_KEY_RESULT_TWEETS);
                    mAdapter.setTweets(tweets);
                }
                break;
        }
        Model.peekInstance().freePoolObject(ppo);
        return false;
    }
}
