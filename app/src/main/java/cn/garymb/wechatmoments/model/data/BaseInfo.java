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

package cn.garymb.wechatmoments.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import cn.garymb.wechatmoments.common.Constants;

public abstract class BaseInfo implements Parcelable {

    public static final float BYTE_BUFFER_GROW_SCALE = 1.25f;

    public static final int ARRAY_BUFFER_INIT_SIZE = 4096;

    private static byte[] sRawArray = new byte[ARRAY_BUFFER_INIT_SIZE];

    @Override
    public int describeContents() {
        return 0;
    }

    abstract protected void readFromParcel(Parcel source);

    abstract public void fromJSONData(JSONObject obj) throws JSONException;

    abstract public ByteBuffer toByteBuffer(ByteBuffer buffer);

    @SuppressWarnings("unused")
    abstract public void fromByteBuffer(ByteBuffer buffer);

    private static ByteBuffer expandByteBuffer(ByteBuffer buffer) {
        ByteBuffer container;
        container = ByteBuffer.allocate((int) (buffer.capacity() * BYTE_BUFFER_GROW_SCALE));
        container.put(buffer.array(), 0, buffer.position());
        buffer.clear();
        return container;
    }

    private static void adjustRawArray(int size) {
        int toAdjust = size;
        if (size > sRawArray.length) {
            toAdjust = ((size / ARRAY_BUFFER_INIT_SIZE) + 1) * ARRAY_BUFFER_INIT_SIZE;
        } else if (size < sRawArray.length / 2) {
            toAdjust = sRawArray.length / 2;
        }
        sRawArray = new byte[toAdjust];
    }

    @SuppressWarnings("unused")
    protected static ByteBuffer putBaseInfo(ByteBuffer buffer, BaseInfo info) {
        if (info != null) {
            String classid = info.getClass().getName();
            int index = classid.lastIndexOf('.');
            String className = classid.substring(index + 1, classid.length());
            buffer = putString(buffer, className);
            buffer = info.toByteBuffer(buffer);
        } else {
            buffer = putString(buffer, null);
        }
        return buffer;
    }

    protected static ByteBuffer putInt(ByteBuffer buffer, int value) {
        ByteBuffer container = buffer;
        if (buffer.remaining() < 4) {
            container = expandByteBuffer(buffer);
            buffer = container;
        }
        container.putInt(value);
        return buffer;
    }

    @SuppressWarnings("unused")
    protected static ByteBuffer putLong(ByteBuffer buffer, long value) {
        ByteBuffer container = buffer;
        if (buffer.remaining() < 8) {
            container = expandByteBuffer(buffer);
            buffer = container;
        }
        container.putLong(value);
        return buffer;
    }

    @SuppressWarnings("unused")
    protected static ByteBuffer putBoolean(ByteBuffer buffer, boolean value) {
        ByteBuffer container = buffer;
        if (buffer.remaining() < 1) {
            container = expandByteBuffer(buffer);
            buffer = container;
        }
        container.put(value ? (byte) 0 : (byte) 1);
        return buffer;
    }

    protected static ByteBuffer putString(ByteBuffer buffer, String content) {
        ByteBuffer container = buffer;
        if (content != null) {
            int currentPos = container.position();
            try {
                int length = content.getBytes(Constants.DEFAULT_ENCODING).length;
                while (length + 4 > buffer.remaining()) {
                    container = expandByteBuffer(buffer);
                    buffer = container;
                }
                putInt(buffer, length);
                container.put(content.getBytes(Constants.DEFAULT_ENCODING));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                container.position(currentPos);
                container = putInt(container, 0);
            }
        } else {
            container = putInt(container, 0);
        }
        return container;
    }

    protected static String getString(ByteBuffer buffer) {
        int size = buffer.getInt();
        if (size != 0) {
            adjustRawArray(size);
            buffer.get(sRawArray, 0, size);
            try {
                return new String(sRawArray, 0, size, Constants.DEFAULT_ENCODING);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    protected static int getInt(ByteBuffer buffer) {
        return buffer.getInt();
    }

    @SuppressWarnings("unused")
    protected static boolean getBoolean(ByteBuffer buffer) {
        return buffer.get() > (byte) 0;
    }

    @SuppressWarnings("unused")
    protected static long getLong(ByteBuffer buffer) {
        return buffer.getLong();
    }
}
