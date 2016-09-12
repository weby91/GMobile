package com.webster.gmobile.motionkit;

import android.content.Context;

/**
 * Created by weby on 1/2/2016.
 */
public class MirrorWrap {
    private final Object mWrapped;
    private final Context mContext;

    public MirrorWrap(Context context, Object obj) {
        mContext = context;
        mWrapped = obj;
    }
    public MirrorAnimator animator(String property, int... values) {
        return MotionKit.animator(mWrapped, property, values);
    }

    public MirrorAnimator animator(String property, float... values) {
        return MotionKit.animator(mWrapped, property, values);
    }


}
