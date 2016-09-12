package com.webster.gmobile.mirror;

import android.view.View;

/**
 * Created by weby on 1/2/2016.
 */
public abstract class MirrorSandboxBase implements MirrorSandbox {
    protected final View mRootView;

    public MirrorSandboxBase(View rootView) {
        mRootView = rootView;
    }

    @Override
    public void $onCreate(View rootView) {
        // do nothing by default
    }

    @Override
    public void $onDestroy() {
        // do nothing by default
    }
}
