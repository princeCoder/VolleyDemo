package princecoder.volleydemo;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.io.File;

import princecoder.volleydemo.utils.L;
import princecoder.volleydemo.utils.LruBitmapCache;

/**
 * Created by prinzlyngotoum on 2/23/15.
 */
public class VolleyLauncher extends Application {

    /**
     * Log or request TAG
     */
    private final String TAG =getClass().getSimpleName();

    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    /**
     *  image loader
     */
    private ImageLoader mImageLoader;

    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static VolleyLauncher sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the singleton
        sInstance = this;
    }

    /**
     * @return VolleyLauncher singleton instance
     */
    public static synchronized VolleyLauncher getInstance() {
        if(sInstance == null) {
            sInstance = new VolleyLauncher();
        }
        return sInstance;
    }


    /**
     * Disk cache implementation
     * @param context
     * @return
     */
    public RequestQueue newRequestQueue(Context context) {
        // define cache folder

        // Default maximum disk usage in bytes
        final int DEFAULT_DISK_USAGE_BYTES = 25 * 1024 * 1024;

        // Default cache folder name
        final String DEFAULT_CACHE_DIR = "photos";

        File rootCache = context.getExternalCacheDir();
        if (rootCache == null) {
            L.m("Can't find External Cache Dir, "
                    + "switching to application specific cache directory");
            rootCache = context.getCacheDir();
        }

        File cacheDir = new File(rootCache, DEFAULT_CACHE_DIR);
        cacheDir.mkdirs();

        HttpStack stack = new HurlStack();
        Network network = new BasicNetwork(stack);
        DiskBasedCache diskBasedCache = new DiskBasedCache(cacheDir, DEFAULT_DISK_USAGE_BYTES);

        if (mRequestQueue == null) {
            mRequestQueue =  new RequestQueue(diskBasedCache, network);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */

    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */

    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache(LruBitmapCache.getCacheSize(getApplicationContext())));
        }
        return this.mImageLoader;
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void cancelAll(String tag){
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
