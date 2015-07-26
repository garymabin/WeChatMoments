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

package cn.garymb.wechatmoments.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;

import java.util.LinkedList;
import java.util.List;

import cn.garymb.wechatmoments.R;
import cn.garymb.wechatmoments.common.Constants;
import cn.garymb.wechatmoments.controller.IViewController;
import cn.garymb.wechatmoments.model.data.ImageItem;
import cn.garymb.wechatmoments.model.data.TweetInfo;
import cn.garymb.wechatmoments.model.data.UserInfo;
import cn.garymb.wechatmoments.model.image.ImageViewItemController;

public class TweetsAdapter extends RecyclerViewImageAdapter implements IViewController {

    public static class BaseViewHolder extends RecyclerViewImageAdapter.BaseImageViewHolder {

        public static final int IMAGE_CONTROLLER_SENDER_AVATAR_INDEX = 0;
        public static final int IMAGE_CONTROLLER_USER_DEF_INDEX_BEGIN = 1;

        public ImageView senderAvatarImage;
        public TextView senderNameText;
        public DocumentView senderContentText;
        public RecyclerView commentsContainer;

        public CommentsAdapter commentsAdapter;

        public BaseViewHolder(Context context, View itemView) {
            super(itemView);
            senderAvatarImage = (ImageView) itemView.findViewById(R.id.tweet_sender_avatar_image);
            senderNameText = (TextView) itemView.findViewById(R.id.tweet_sender_nick_text);
            senderContentText = (DocumentView) itemView.findViewById(R.id.tweet_sender_content_text);
            commentsContainer = (RecyclerView) itemView.findViewById(R.id.comments_layout);
            commentsContainer.setLayoutManager(
                    new org.solovyev.android.views.llm.LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            commentsAdapter = new CommentsAdapter(context);
            commentsContainer.setAdapter(commentsAdapter);
        }
    }


    public static class HeaderViewHolder extends BaseImageViewHolder {

        public static final int IMAGE_CONTROLLER_USER_PROFILE_INDEX = 0;
        public static final int IMAGE_CONTROLLER_USER_AVATAR_INDEX = 1;

        public TextView userNickNameText;
        public ImageView userProfileImage;
        public ImageView userAvatarImage;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            imageControllers = new ImageViewItemController[2];
            userNickNameText = (TextView) itemView.findViewById(R.id.user_nick_name_text);
            userProfileImage = (ImageView) itemView.findViewById(R.id.user_profile_image);
            userAvatarImage = (ImageView) itemView.findViewById(R.id.user_avatar_image);
        }
    }

    public static class MoreImgViewHolder extends BaseViewHolder {
        public static final int MORE_IMG_MAX_IMAGES_SIZE = 9;

        public ImageView imageViews[];

        public MoreImgViewHolder(Context context, View itemView) {
            super(context, itemView);
            imageControllers = new ImageViewItemController[MORE_IMG_MAX_IMAGES_SIZE + 1];
            imageViews = new ImageView[MORE_IMG_MAX_IMAGES_SIZE];
            imageViews[0] = (ImageView) itemView.findViewById(R.id.more_image_image1);
            imageViews[1] = (ImageView) itemView.findViewById(R.id.more_image_image2);
            imageViews[2] = (ImageView) itemView.findViewById(R.id.more_image_image3);
            imageViews[3] = (ImageView) itemView.findViewById(R.id.more_image_image4);
            imageViews[4] = (ImageView) itemView.findViewById(R.id.more_image_image5);
            imageViews[5] = (ImageView) itemView.findViewById(R.id.more_image_image6);
            imageViews[6] = (ImageView) itemView.findViewById(R.id.more_image_image7);
            imageViews[7] = (ImageView) itemView.findViewById(R.id.more_image_image8);
            imageViews[8] = (ImageView) itemView.findViewById(R.id.more_image_image9);
        }
    }

    public static class ZeroImgViewHolder extends BaseViewHolder {

        public ZeroImgViewHolder(Context context, View itemView) {
            super(context, itemView);
            imageControllers = new ImageViewItemController[1];
        }
    }

    public static class SingleImgViewHolder extends BaseViewHolder {

        public ImageView singleImage;

        public SingleImgViewHolder(Context context, View itemView) {
            super(context, itemView);
            imageControllers = new ImageViewItemController[2];
            singleImage = (ImageView) itemView.findViewById(R.id.tweet_single_image);
        }
    }

    private static final int VIEW_TYPE_WITH_ZERO_IMAGE = 1;
    private static final int VIEW_TYPE_WITH_SINGLE_IMAGE = 2;
    private static final int VIEW_TYPE_WITH_MORE_IMAGE = 3;
    private static final int VIEW_TYPE_HEADER = 0;

    private LinkedList<TweetInfo> mTweets = new LinkedList<>();
    private UserInfo mCurrentUser;
    private LayoutInflater mInflater;
    private Context mContext;
    private int mRequiredSize[];

    public TweetsAdapter(LinearLayoutManager layoutManager, Context context, int[] requiredSize) {
        super(layoutManager);
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mRequiredSize = requiredSize;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View itemView = null;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                itemView = mInflater.inflate(R.layout.header_layout, parent, false);
                holder = new HeaderViewHolder(itemView);
                break;
            case VIEW_TYPE_WITH_ZERO_IMAGE:
                itemView = mInflater.inflate(R.layout.zero_image_tweet_item_layout, parent, false);
                holder = new ZeroImgViewHolder(mContext, itemView);
                break;
            case VIEW_TYPE_WITH_SINGLE_IMAGE:
                itemView = mInflater.inflate(R.layout.single_image_tweet_item_layout, parent, false);
                holder = new SingleImgViewHolder(mContext, itemView);
                break;
            case VIEW_TYPE_WITH_MORE_IMAGE:
                itemView = mInflater.inflate(R.layout.more_image_tweet_item_layout, parent, false);
                holder = new MoreImgViewHolder(mContext, itemView);
                break;
        }
        if (itemView != null) {
            itemView.setTag(holder);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_HEADER: {
                if (mCurrentUser != null) {
                    HeaderViewHolder hvh = (HeaderViewHolder) holder;
                    hvh.userNickNameText.setText(mCurrentUser.nick);
                    // load profile
                    ImageItem item = new ImageItem(mRequiredSize[Constants.USER_PROFILE_IMAGE_HEIGHT_INDEX],
                            mRequiredSize[Constants.USER_PROFILE_IMAGE_WIDTH_INDEX], mCurrentUser.profileImage);
                    loadImage(hvh, HeaderViewHolder.IMAGE_CONTROLLER_USER_PROFILE_INDEX, hvh.userProfileImage, item);
                    // load avatar
                    item = new ImageItem(mRequiredSize[Constants.USER_AVATAR_IMAGE_HEIGHT_INDEX],
                            mRequiredSize[Constants.USER_AVATAR_IMAGE_WIDTH_INDEX], mCurrentUser.avatar);
                    loadImage(hvh, HeaderViewHolder.IMAGE_CONTROLLER_USER_AVATAR_INDEX, hvh.userAvatarImage, item);
                }
                break;
            }
            case VIEW_TYPE_WITH_ZERO_IMAGE: {
                TweetInfo ti = mTweets.get(position - 1);
                setSender((BaseViewHolder) holder, ti);
                setComments((BaseViewHolder) holder, ti);
                break;
            }
            case VIEW_TYPE_WITH_SINGLE_IMAGE: {
                TweetInfo ti = mTweets.get(position - 1);
                setSender((BaseViewHolder) holder, ti);
                setComments((BaseViewHolder) holder, ti);
                SingleImgViewHolder sivh = (SingleImgViewHolder) holder;
                if (ti != null) {
                    ImageItem item = new ImageItem(mRequiredSize[Constants.TWEET_SENDER_SINGLE_IMAGE_WIDTH_INDEX],
                            mRequiredSize[Constants.TWEET_SENDER_SINGLE_IMAGE_HEIGHT_INDEX], ti.images.get(0));
                    loadImage(sivh, SingleImgViewHolder.IMAGE_CONTROLLER_USER_DEF_INDEX_BEGIN, sivh.singleImage, item);
                }
                break;
            }
            case VIEW_TYPE_WITH_MORE_IMAGE: {
                TweetInfo ti = mTweets.get(position - 1);
                setSender((BaseViewHolder) holder, ti);
                setComments((BaseViewHolder) holder, ti);
                int realSize = ti.images.size() > MoreImgViewHolder.MORE_IMG_MAX_IMAGES_SIZE ? MoreImgViewHolder.MORE_IMG_MAX_IMAGES_SIZE :
                        ti.images.size();
                MoreImgViewHolder mivh = (MoreImgViewHolder) holder;
                int i = 0;
                for (; i < realSize; i++) {
                    mivh.imageViews[i].setVisibility(View.VISIBLE);
                    loadImage(mivh, i + 1, mivh.imageViews[i],
                            new ImageItem(mRequiredSize[Constants.TWEET_SENDER_MORE_IMAGE_WIDTH_INDEX],
                                    mRequiredSize[Constants.TWEET_SENDER_MORE_IMAGE_HEIGHT_INDEX], ti.images.get(i)));
                }
                for (; i < MoreImgViewHolder.MORE_IMG_MAX_IMAGES_SIZE; i++) {
                    mivh.imageViews[i].setVisibility(View.GONE);
                }
                break;
            }
        }
    }

    private void setComments(BaseViewHolder holder, TweetInfo ti) {
        if (ti != null && ti.comments != null && ti.comments.size() != 0) {
            holder.commentsContainer.setVisibility(View.VISIBLE);
            holder.commentsAdapter.setComments(ti.comments);
        } else {
            holder.commentsContainer.setVisibility(View.GONE);
        }
    }

    private void setSender(BaseViewHolder holder, TweetInfo ti) {
        if (ti != null && ti.sender != null) {
            String imgUrl = ti.sender.avatar;
            ImageItem item = new ImageItem(mRequiredSize[Constants.TWEET_SENDER_AVATAR_WIDTH_INDEX],
                    mRequiredSize[Constants.TWEET_SENDER_AVATAR_HEIGHT_INDEX], imgUrl);
            loadImage(holder, BaseViewHolder.IMAGE_CONTROLLER_SENDER_AVATAR_INDEX, holder.senderAvatarImage, item);
            if (!TextUtils.isEmpty(ti.content)) {
                holder.senderContentText.setText(ti.content);
            }
            if (!TextUtils.isEmpty(ti.sender.nick)) {
                holder.senderNameText.setVisibility(View.VISIBLE);
                holder.senderNameText.setText(ti.sender.nick);
            } else {
                holder.senderNameText.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        }
        List<String> images = mTweets.get(position - 1).images;
        int size = images == null ? 0 : images.size();
        if (size > 1) {
            return VIEW_TYPE_WITH_MORE_IMAGE;
        } else {
            return size == 1 ? VIEW_TYPE_WITH_SINGLE_IMAGE : VIEW_TYPE_WITH_ZERO_IMAGE;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mTweets == null ? 1 : mTweets.size() + 1;
    }

    public void setTweets(List<TweetInfo> tweets) {
        mTweets.clear();
        if (tweets == null) {
            return;
        }
        for (TweetInfo tweet : tweets) {
            if (TextUtils.isEmpty(tweet.content) && tweet.images == null) {
                continue;
            }
            mTweets.addLast(tweet);
        }
        notifyDataSetChanged();
    }

    public void appendTweets(List<TweetInfo> tweets) {
        if (tweets == null) {
            return;
        }
        for (TweetInfo tweet : tweets) {
            if (TextUtils.isEmpty(tweet.content) && tweet.images == null) {
                continue;
            }
            mTweets.addLast(tweet);
        }
        updateItemAtPosition(0);
    }

    public void updateProfile(UserInfo user) {
        mCurrentUser = user;
        updateItemAtPosition(0);
    }
}
