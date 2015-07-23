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

package cn.garymb.wechatmoments.model.image;

import java.io.File;

import cn.garymb.wechatmoments.StaticApplication;
import cn.garymb.wechatmoments.common.Constants;
import cn.garymb.wechatmoments.model.data.ImageItem;

public class ImageItemInfoHelper {
    public static String sCacheDir;
    static {
        sCacheDir = StaticApplication.peekInstance().getCacheDir(Constants.CACHE_IMAGES_TYPE);
        new File(sCacheDir).mkdirs();
    }

    public static boolean isImageExist(ImageItem item) {
        File imageFile = new File(sCacheDir, item.hashCode() + "");
        if (imageFile != null && imageFile.exists()) {
            return true;
        }
        return false;
    }

    public static String getImageSavedPath(ImageItem item) {
        return sCacheDir + File.separator + item.hashCode();
    }
}
