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

public final class Constants {
    public static final String DEFAULT_ENCODING = "UTF-8";

    public static final int CACHE_IMAGES_TYPE = 0;

    public static final String CACHE_IMAGES_DIR = "images";

    public static final String JSON_KEY_USER_PROFILE_NAME = "profile-image";
    public static final String JSON_KEY_USER_AVATAR = "avatar";
    public static final String JSON_KEY_USER_NICK = "John Smith";
    public static final String JSON_KEY_USER_USERNAME = "jsmith";

    public static final String JSON_KEY_TWEET_SENDER = "sender";
    public static final String JSON_KEY_TWEET_CONTENT = "content";
    public static final String JSON_KEY_TWEET_IMAGES = "images";

    public static final String BASE_URL = "http://thoughtworks-ios.herokuapp.com/";

    public static final String USER_URL = BASE_URL + "user/jsmith";
    public static final String TWEETS_URL = BASE_URL + "user/jsmith/tweets";
}
