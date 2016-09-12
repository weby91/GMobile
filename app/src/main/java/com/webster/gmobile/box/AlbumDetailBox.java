package com.webster.gmobile.box;

import android.animation.Animator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;

import com.webster.gmobile.gmobile.R;
import com.webster.gmobile.mirror.MirrorSandboxBase;
import com.webster.gmobile.motionkit.MirrorAnimator;
import com.webster.gmobile.motionkit.MirrorView;
import com.webster.gmobile.motionkit.MotionKit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by weby on 1/2/2016.
 */
public class AlbumDetailBox extends MirrorSandboxBase {
    @InjectView(R.id.fab)
    View fab;
    @InjectView(R.id.title_container)
    View cyanPanel;
    @InjectView(R.id.info_container)
    View whitePanel;
    @InjectView(R.id.album_art)
    ImageView albumArt;

    public AlbumDetailBox(View root) {
        super(root);
        ButterKnife.inject(this, root);
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        Animator anim = ViewAnimationUtils.createCircularReveal(albumArt, 100, 100, 1000, 000);
        anim.start();
    }

    @Override
    public void $onLayoutDone(View rootView) {
        enterAnimation().start();
//        exitAnimation().start();
//        ValueAnimator a = ObjectAnimator.ofFloat(fab, "scaleX", 0, 1).setDuration(1000);
//        a.start();
    }

    public MirrorAnimator exitAnimation() {
        return MotionKit.sequence(wrap(fab).scale(1, 0).duration(200),
                fold(whitePanel).duration(100),
                fold(cyanPanel).duration(100)
        );
    }

    public MirrorAnimator enterAnimation() {
        return MotionKit.sequence(unfold(cyanPanel),
                unfold(whitePanel),
                wrap(fab).scale(0, 1).duration(200)
        );
    }

    private MirrorView wrap(View view) {
        return new MirrorView<>(view);
    }

    private MirrorAnimator unfold(View v) {
        int bottomFrom = v == cyanPanel ? 1200 : 1465;
        int bottomTo = v == cyanPanel ? 1465 : 1677;
        return wrap(v).bottom(bottomFrom, bottomTo).duration(200);
    }

    private MirrorAnimator fold(View v) {
        return wrap(v).bottom(v.getTop() + v.getHeight(), v.getTop()).duration(200);
    }

}
