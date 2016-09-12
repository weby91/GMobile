package com.webster.gmobile.box;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.webster.gmobile.gmobile.R;
import com.webster.gmobile.mirror.MirrorSandboxBase;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by weby on 1/2/2016.
 */
public class AlbumListBox extends MirrorSandboxBase {

    @InjectView(R.id.album_list)
    RecyclerView albumList;
    public AlbumListBox(View rootView) {
        super(rootView);
        ButterKnife.inject(this, rootView);
        StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        albumList.setLayoutManager(lm);
    }

    @Override
    public void $onLayoutDone(View view) {
    }
}