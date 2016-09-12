package com.webster.gmobile.motionkit;

import android.animation.Animator;
import android.animation.Keyframe;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.android.volley.Request;
import com.webster.gmobile.util.Optional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by weby on 1/2/2016.
 */
public abstract class MirrorAnimator {

    public MirrorAnimator() {
    }

    public MirrorAnimator together(MirrorAnimator... mirrorAnimators) {
        List<MirrorAnimator> mas = mePlus(mirrorAnimators);
        return MotionKit.together(mas);
    }

    public MirrorAnimator followedBy(MirrorAnimator mirrorAnimator) {
        List<MirrorAnimator> mas = mePlus(mirrorAnimator);
        return MotionKit.sequence(mas);
    }

    public abstract Animator getAnimator();

    private List<MirrorAnimator> mePlus(MirrorAnimator... mirrorAnimators) {
        List<MirrorAnimator> result = new ArrayList<>(mirrorAnimators.length + 1);
        result.add(this);
        result.addAll(Arrays.asList(mirrorAnimators));
        return result;
    }

    public MirrorAnimator interpolator(Context context, int resId) {
        MotionKit.setInterpolator(context, getAnimator(), resId);
        return this;
    }

    public MirrorAnimator interpolator(TimeInterpolator interpolator) {
        getAnimator().setInterpolator(interpolator);
        return this;
    }

    public abstract MirrorAnimator duration(long duration);

    public abstract MirrorAnimator startDelay(long delay);

    public abstract long getDuration();

    public abstract long getStartDelay();

    public void startNoStageSetting() {
        getAnimator().start();
    }

    public void start() {
        start(new UseFirstFrameOnlyStageSetter());
    }

    public void start(StageSetter stageSetter) {
        setupStage(this, stageSetter);
        startNoStageSetting();
    }

    private void setupStage(MirrorAnimator animator, StageSetter stageSetter) {
        List<Pair<MirrorObjectAnimator, Long>> animatorStartTimes = new ArrayList<>();
        collectStartTime(animator, animatorStartTimes, 0);
        Collections.sort(animatorStartTimes, new Comparator<Pair<MirrorObjectAnimator, Long>>() {
            @Override
            public int compare(Pair<MirrorObjectAnimator, Long> lhs, Pair<MirrorObjectAnimator, Long> rhs) {
                return (int) (lhs.second - rhs.second);
            }
        });

        stageSetter.setup(animatorStartTimes);
    }

    private void collectStartTime(MirrorAnimator animator, List<Pair<MirrorObjectAnimator, Long>> output, long startTime) {
        if (animator instanceof MirrorAnimatorSet) {
            MirrorAnimatorSet set = (MirrorAnimatorSet) animator;
            long accuTimeBeforeMe = set.getStartDelay();
            for (MirrorAnimator c : set.getChildAnimations()) {
                collectStartTime(c, output, startTime + accuTimeBeforeMe);
                if (set.getOrdering() == MirrorAnimatorSet.Ordering.Sequentially) {
                    accuTimeBeforeMe += c.getStartDelay() + c.getDuration();
                }
            }
        } else if (animator instanceof MirrorObjectAnimator) {
            MirrorObjectAnimator o = (MirrorObjectAnimator) animator;
            output.add(new Pair<>(o, startTime + o.getStartDelay()));
        } else {
            throw new IllegalStateException("Unsupported animator type: " + animator);
        }
    }

    private static class UseFirstFrameOnlyStageSetter implements StageSetter {
        private static final String LOG_TAG = "FirstFrameStageSetter";
        private static final List<String> PROPS_AFFECTED_BY_LAYOUT = Arrays.asList(new String[]{"left", "right", "top", "bottom"});
        private Map<Object, Set<String>> mRegistry = new HashMap<>();

        @Override
        public void setup(List<Pair<MirrorObjectAnimator, Long>> sortedAnimatorStartTimes) {
            for (Pair<MirrorObjectAnimator, Long> pair : sortedAnimatorStartTimes) {
                setupOne(pair.first);
            }
        }

        private void setupOne(MirrorObjectAnimator objectAnimator) {
            Object target = objectAnimator.getTarget();
            Set<String> props = mRegistry.get(target);
            if (props == null) {
                props = new HashSet<>();
                mRegistry.put(target, props);
            }
            String propertyName = objectAnimator.getPropertyName();
            if (!props.contains(propertyName)) {
                if ((target instanceof View) && willBeUpdatedDuringLayout(propertyName)) {
                    callSetterOnLayoutChange((View)target, propertyName, objectAnimator.getFirstFrame());
                } else {
                    //callSetter(target, propertyName, objectAnimator.getFirstFrame());
                }
                props.add(propertyName);
            }
        }

        private void callSetterOnLayoutChange(final View target, final String propertyName, final Keyframe firstFrame) {
            target.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    //callSetter(target, propertyName, firstFrame);
                    target.removeOnLayoutChangeListener(this);
                }
            });
        }

        private boolean willBeUpdatedDuringLayout(String propertyName) {
            return PROPS_AFFECTED_BY_LAYOUT.contains(propertyName);
        }



    }

    public interface StageSetter {
        void setup(List<Pair<MirrorObjectAnimator, Long>> sortedAnimatorStartTimes);
    }

}
