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

public interface IPoolObject {
    /**
     * Initialization method. Called when an object is retrieved from the object
     * pool or has just been created.
     */
    public void initializePoolObject();

    /**
     * Finalization method. Called when an object is stored in the object pool
     * to mark it as free.
     */
    public void finalizePoolObject();
}
