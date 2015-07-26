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

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

@SuppressWarnings("serial")
public class WCMDataError extends VolleyError {
    private Object mParam;

    public WCMDataError(NetworkResponse response, Object param) {
        super(response);
        mParam = param;
    }

    @SuppressWarnings("unused")
    public WCMDataError(String message, Object param) {
        super(message);
        mParam = param;
    }

    public Object getParam() {
        return mParam;
    }
}
