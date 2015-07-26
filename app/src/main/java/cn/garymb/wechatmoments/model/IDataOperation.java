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

package cn.garymb.wechatmoments.model;

import android.os.Message;

import cn.garymb.wechatmoments.controller.IViewController;

public interface IDataOperation {

    //request code
    int REQUEST_RESULT_SUCCESS = 0;
    int REQUEST_RESULT_FAILED = -1;
    @SuppressWarnings("unused")
    int REQUEST_RESULT_NO_DATA = -2;
    @SuppressWarnings("unused")
    int REQUEST_RESULT_TIMEOUT = -3;
    int REQUEST_RESULT_CANCEL = -4;

    //request parameter key
    String BUNDLE_KEY_PARAM_IMAGE_REQUEST_ITEM = "cgw.image.req.item";
    String BUNDLE_KEY_RESULT_MESSAGE = "cgw.result.msg";

    //request result key
    String BUNDLE_KEY_RESULT_USER = "cgw.result.user";
    String BUNDLE_KEY_RESULT_TWEETS = "cgw.result.tweets";

    //request type
    int REQUEST_TYPE_GET_USER_INFO = 0x1001;
    int REQUEST_TYPE_GET_TWEETS_INFO = 0x1002;
    int REQUEST_TYPE_GET_IMAGE_BITMAP = 0x10FE;
    int REQUEST_TYPE_DECODE_IMAGE_BITMAP = 0x10FF;

    void requestDataOperation(IViewController target, Message msg);
    void cancelDataOperation(IViewController target, Message msg);
}
