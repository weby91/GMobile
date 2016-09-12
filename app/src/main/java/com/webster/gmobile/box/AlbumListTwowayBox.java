package com.webster.gmobile.box;

import android.view.View;

import com.webster.gmobile.gmobile.R;
import com.webster.gmobile.mirror.MirrorSandboxBase;
import com.webster.gmobile.widget.TwoWayLayoutManager;
import com.webster.gmobile.widget.TwoWayView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by weby on 1/2/2016.
 */
public class AlbumListTwowayBox extends MirrorSandboxBase {

    @InjectView(R.id.album_list)
    TwoWayView albumList;

    public AlbumListTwowayBox(View rootView) {
        super(rootView);
        ButterKnife.inject(this, rootView);
        //SpannableGridLayoutManager lm = new SpannableGridLayoutManager(TwoWayLayoutManager.Orientation.VERTICAL, 2, 3);
        //albumList.setLayoutManager(lm);
    }

    @Override
    public void $onLayoutDone(View view) {

    }
}