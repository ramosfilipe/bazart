package com.boleiros.bazart.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

public class LruCacheUtil {
    private static LruCache<String, Bitmap> lru;
    private static LruCacheUtil lruCacheUtil;

    private Context mContext;

    private LruCacheUtil(Context ctx) {
        mContext = ctx;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        lru = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

    }

    public static LruCacheUtil getInstance(Context ctx) {
        if(lruCacheUtil == null) {
            lruCacheUtil = new LruCacheUtil(ctx.getApplicationContext());
        }
        return lruCacheUtil;
    }

    public Bitmap getBitmap(String key){
        return lru.get(key);
    }
    public void addBitmap(String key, Bitmap bit){
        lru.put(key,bit);
    }

}