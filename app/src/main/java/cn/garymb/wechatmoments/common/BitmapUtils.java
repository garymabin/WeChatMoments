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

import android.content.Context;
import android.graphics.Matrix;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtils {
    /**
     * Mix two Bitmap as one.
     *
     * @param first
     *            the first bitmap to mix
     * @param second
     *            the second bitmap to mix
     * @param fromPoint
     *            where the second bitmap is painted.
     * @return
     */
    public static Drawable mixtureBitmap(Context context, Bitmap first,
                                         Bitmap second, PointF fromPoint) {
        if (first == null || second == null || fromPoint == null) {
            return null;
        }
        Bitmap newBitmap = Bitmap.createBitmap(first.getWidth(),
                first.getHeight(), Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(first, 0, 0, null);
        cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();

        BitmapDrawable bitmapDrawable = new BitmapDrawable(
                context.getResources(), newBitmap);
        Drawable drawable = (Drawable) bitmapDrawable;
        return drawable;
    }

    public static Bitmap createNewBitmapWithResource(Resources res, int resID,
                                                     int wh[], boolean forceResize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inDither = false;

        BitmapFactory.decodeResource(res, resID, options);
        int requestWidth = wh[0];
        int bmpWidth = options.outWidth;
        float inSampleSize = (float) bmpWidth / (float) requestWidth;
        if (inSampleSize > 1.0 && inSampleSize < 2.0) {
            options.inSampleSize = 2;
        } else if (inSampleSize >= 2.0) {
            options.inSampleSize = (int) inSampleSize;
        }
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(res, resID, options);

        return decodeWithFixDimension(requestWidth, wh[1], bitmap, options, forceResize);
    }

    public static Bitmap createNewBitmapFromStream(InputStream is,
                                                   int wh[], boolean forceResize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = false;

        BitmapFactory.decodeStream(is, null, options);

        return decodeWithFixDimension(wh[0], wh[1], is, options, forceResize);
    }

    public static Bitmap createNewBitmapAndCompressByFile(String filePath,
                                                          int wh[], boolean forceResize) {
        int offset = 100;
        File file = new File(filePath);
        long fileSize = 0;
        if (file.exists()) {
            fileSize = file.length();
            if (200 * 1024 < fileSize && fileSize <= 1024 * 1024)
                offset = 90;
            else if (1024 * 1024 < fileSize)
                offset = 85;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inDither = false;

        BitmapFactory.decodeFile(filePath, options);
        Bitmap bitmap = decodeWithFixDimension(wh[0], wh[1], file, options,
                forceResize);
        if (bitmap != null) {
            bitmap = compressBitmapFile(offset, fileSize, bitmap);
        }
        return bitmap;
    }

    public static Bitmap createNewBitmapWithoutCompress(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        return bitmap;
    }

    public static Bitmap createNewBitmapWithoutCompress(InputStream is) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inDither = false;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
        return bitmap;
    }

    public static Bitmap createNewBitmapAndCompressByByteArray(byte[] array,
                                                               int wh[], boolean forceResize) {
        int offset = 100;
        long fileSize = 0;
        fileSize = array.length;
        if (200 * 1024 < fileSize && fileSize <= 1024 * 1024)
            offset = 90;
        else if (1024 * 1024 < fileSize)
            offset = 85;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inDither = false;
        BitmapFactory.decodeByteArray(array, 0, array.length, options);
        Bitmap bitmap = decodeByteArrayWithFixDimension(wh[0], wh[1], array,
                options, forceResize);
        if (bitmap != null) {
            bitmap = compressBitmapFile(offset, fileSize, bitmap);
        }
        return bitmap;
    }

    public static Bitmap createNewBitmapAndCompressByFile(String filePath,
                                                          int width, boolean forceResize) {
        return createNewBitmapAndCompressByFile(filePath,
                new int[] { width, -1 }, forceResize);
    }

    public static Bitmap createNewBitmapFromStream(InputStream is,
                                                   int width, boolean forceResize) {
        return createNewBitmapFromStream(is,
                new int[] { width, -1 }, forceResize);
    }

    private static Bitmap compressBitmapFile(int offset, long fileSize,
                                             Bitmap bitmap) {
        if (offset == 100)
            return bitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, offset, baos);
        byte[] buffer = baos.toByteArray();
        if (buffer.length >= fileSize)
            return bitmap;
        return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
    }

    private static Bitmap decodeWithFixDimension(int width, int height,
                                                 Bitmap bmp, BitmapFactory.Options options, boolean forceResize) {
        int requestWidth = width;
        int requestHeight = height;
        int bmpWidth = options.outWidth;
        float inSampleSize = (float) bmpWidth / (float) requestWidth;
        if (inSampleSize > 1.0 && inSampleSize < 2.0) {
            options.inSampleSize = 2;
        } else if (inSampleSize >= 2.0) {
            options.inSampleSize = (int) inSampleSize;
        } else {
            options.inSampleSize = 1;
        }
        options.inJustDecodeBounds = false;

        Bitmap bitmap = bmp;
        try {
            if (bitmap != null) {
                float scale = ((float) requestWidth / options.outWidth);
                Matrix matrix = new Matrix();
                matrix.setScale(scale, scale);
                if (scale < 1.0 || forceResize
                        || (inSampleSize > 1.0 && inSampleSize < 2.0)) {
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            options.outWidth, options.outHeight, matrix, true);
                }
                if (requestHeight != -1 && bitmap.getHeight() > requestHeight) {
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), requestHeight);
                }
            }
        } catch (OutOfMemoryError e) {
            System.gc();
            bitmap = null;
        }
        return bitmap;
    }

    private static Bitmap decodeWithFixDimension(int width, int height,
                                                 InputStream is, BitmapFactory.Options options, boolean forceResize) {
        int requestWidth = width;
        int requestHeight = height;
        int bmpWidth = options.outWidth;
        float inSampleSize = (float) bmpWidth / (float) requestWidth;
        if (inSampleSize > 1.0 && inSampleSize < 2.0) {
            options.inSampleSize = 2;// set scale factor
        } else if (inSampleSize >= 2.0) {
            options.inSampleSize = (int) inSampleSize;
        } else {
            options.inSampleSize = 1;
        }
        options.inJustDecodeBounds = false;

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(is, null, options);
            if (bitmap != null) {
                float scale = ((float) requestWidth / options.outWidth);
                Matrix matrix = new Matrix();
                matrix.setScale(scale, scale);
                // there are three situations to execute force scale；
                // 1.requested size is smaller.
                // 2.explicit force scale.
                // 3.To reduce image quality, we scale the image lower than the requested size, now we need to scale it back.
                if (scale < 1.0 || forceResize
                        || (inSampleSize > 1.0 && inSampleSize < 2.0)) {
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            options.outWidth, options.outHeight, matrix, true);
                }
                if (requestHeight != -1 && bitmap.getHeight() > requestHeight) {
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), requestHeight);
                }
            }
        } catch (OutOfMemoryError e) {
            System.gc();
            bitmap = null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return bitmap;
    }

    private static Bitmap decodeWithFixDimension(int width, int height,
                                                 File file, BitmapFactory.Options options, boolean forceResize) {
        try {
            InputStream is = new FileInputStream(file);
            return decodeWithFixDimension(width, height, is, options,
                    forceResize);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private static Bitmap decodeByteArrayWithFixDimension(int width,
                                                          int height, byte[] array, BitmapFactory.Options options,
                                                          boolean forceResize) {
        int requestWidth = width;
        int requestHeight = height;
        int bmpWidth = options.outWidth;
        float inSampleSize = (float) bmpWidth / (float) requestWidth;
        if (inSampleSize > 1.0 && inSampleSize < 2.0) {
            options.inSampleSize = 2;// set scale factor
        } else if (inSampleSize >= 2.0) {
            options.inSampleSize = (int) inSampleSize;
        }
        options.inJustDecodeBounds = false;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeByteArray(array, 0, array.length,
                    options);
            if (bitmap != null) {
                float scale = ((float) requestWidth / options.outWidth);
                Matrix matrix = new Matrix();
                matrix.setScale(scale, scale);
                // there are three situations to execute force scale；
                // 1.requested size is smaller.
                // 2.explicit force scale.
                // 3.To reduce image quality, we scale the image lower than the requested size, now we need to scale it back.
                if (scale < 1.0 || forceResize
                        || (inSampleSize > 1.0 && inSampleSize < 2.0)) {
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            options.outWidth, options.outHeight, matrix, true);
                }
                if (requestHeight != -1 && bitmap.getHeight() > requestHeight) {
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), requestHeight);
                }
            }
        } catch (OutOfMemoryError e) {
            System.gc();
            bitmap = null;
        }
        return bitmap;
    }
}
