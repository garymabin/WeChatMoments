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

import java.util.WeakHashMap;

import cn.garymb.wechatmoments.controller.IViewController;
import cn.garymb.wechatmoments.core.IExecutors;
import cn.garymb.wechatmoments.core.ITask;
import cn.garymb.wechatmoments.core.NoCacheTask;

public class DataModelHelper implements IDataOperation, ITask.TaskCallback<Message> {

    private IExecutors mExecutors;
    private WeakHashMap<Message, IViewController> mNotifyTargets;

    public DataModelHelper(IExecutors executors) {
        mExecutors = executors;
        mNotifyTargets = new WeakHashMap<Message, IViewController>();
    }


    @Override
    public void requestDataOperation(IViewController target, Message msg) {
        ITask<Message> task = null;
        switch (msg.what) {
            case IDataOperation.REQEST_TYPE_GET_USER_INFO:
            case IDataOperation.REQEST_TYPE_GET_TWEETS_INFO:
                task = new NoCacheTask(msg, this);
                break;
            default:
                break;
        }
        if (task != null) {
            task.executeOnExecutors(mExecutors);
        }
    }

    @Override
    public void cancelDataOperation(IViewController target, Message msg) {
        //TODO
    }

    @Override
    public void onTaskTimeout(Message result) {
        //TODO
    }

    @Override
    public void onTaskFinish(Message result) {
        IViewController controller = mNotifyTargets.remove(result);
        if (controller != null) {
            controller.handleMessage(result);
        }
    }
}
