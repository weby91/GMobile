package com.webster.gmobile.mirror;

import android.view.View;

/**
 * Created by weby on 1/2/2016.
 */
public interface MirrorSandbox {
    /**
     * Mirror calls this method from the UI thread right after the sandbox object is created, BEFORE
     * the layout pass is done. This method can be used for sandbox-only initialization code.
     *
     * Methods with the prefix '$' are considered as "design mode" methods used for quick experiments
     * or populating views with mock data. Do not call from production code. However, it is encouraged
     * to call other methods in the sandbox class from production code to reuse the code written in
     * sandbox mode.
     *
     * @param rootView the root view that can be used to find child views in the layout
     */
    void $onCreate(View rootView);

    /**
     * Mirror calls this method from the UI thread after the layout pass for the entire screen finishes.
     * Methods such as View#getMeasuredWidth() will return proper values.
     *
     * Methods with the prefix '$' are considered as "design mode" methods used for quick experiments
     * or populating views with mock data. Do not call from production code. However, it is encouraged
     * to call other methods in the sandbox class from production code to reuse the code written in
     * sandbox mode.
     *
     * @param rootView the root view that can be used to find child views in the layout
     */
    void $onLayoutDone(View rootView);

    /**
     * Mirror calls this method from the UI thread in the Activity#onDestroy() call back.
     * This method can be used to release things that are not supposed to be persist across refreshes.
     *
     * Methods with the prefix '$' are considered as "design mode" methods used for quick experiments
     * or populating views with mock data. Do not call from production code. However, it is encouraged
     * to call other methods in the sandbox class from production code to reuse the code written in
     * sandbox mode.
     */
    void $onDestroy();
}
