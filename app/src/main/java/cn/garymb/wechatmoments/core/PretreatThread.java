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

import java.util.concurrent.LinkedBlockingDeque;

import cn.garymb.wechatmoments.model.IDataOperation;

public class PretreatThread extends Thread {

    public interface PretreatCallback {
        void onPretreatComplete(ITask<?> task, int result);
    }

    private LinkedBlockingDeque<ITask<?>> mPretreatQueue;

    private volatile boolean isTerminated = false;

    private PretreatCallback mCallback;

    public PretreatThread(LinkedBlockingDeque<ITask<?>> queue,
                          PretreatCallback callback) {
        mPretreatQueue = queue;
        mCallback = callback;
    }

    public void run() {
        while (!isTerminated) {
            int result = IDataOperation.REQUEST_RESULT_FAILED;
            ITask<?> task;
            try {
                task = mPretreatQueue.take();
            } catch (InterruptedException e) {
                continue;
            }
            if (task != null) {
                if (task.perfromPretreat()) {
                    result = IDataOperation.REQUEST_RESULT_SUCCESS;
                }
                if (mCallback != null) {
                    mCallback.onPretreatComplete(task, result);
                }
            }
        }
    }

    @SuppressWarnings("unused")
    public void terminate() {
        isTerminated = true;
        interrupt();
    }
}
