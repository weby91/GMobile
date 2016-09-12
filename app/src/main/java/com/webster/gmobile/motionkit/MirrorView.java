package com.webster.gmobile.motionkit;

import android.view.View;

/**
 * Created by weby on 1/2/2016.
 */
public class MirrorView<T extends View> extends MirrorWrap {
    private final T mView;

    public MirrorView(T view) {
        super(view.getContext(), view);
        mView = view;
    }

    public MirrorAnimator scale(float... values) {
        return MotionKit.together(scaleX(values), scaleY(values));
    }

    public MirrorAnimator scaleY(float... values) {
        return animator("scaleY", values);
    }

    public MirrorAnimator scaleX(float... values) {
        return animator("scaleX", values);
    }

    public MirrorAnimator bottom(int... values) {
        return animator("bottom", values);
    }

    public MirrorAnimator alpha(float... values) {
        return animator("alpha", values);
    }

    public int getHeight() {
        return mView.getMeasuredHeight();
    }

    public int getTop() {
        return mView.getTop();
    }

    public T getView() {
        return mView;
    }
}
