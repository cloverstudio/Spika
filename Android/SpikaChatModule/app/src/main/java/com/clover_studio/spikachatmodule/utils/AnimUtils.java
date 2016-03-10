package com.clover_studio.spikachatmodule.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by ubuntu_ivo on 22.07.15..
 */
public class AnimUtils {

    /**
     * translate along the x axis
     *
     * @param view
     * @param from
     * @param to
     * @param duration
     * @param listener animator listener adapter, can be null
     * @return ObjectAnimator
     */
    public static ObjectAnimator translateX(View view, float from, float to, int duration, AnimatorListenerAdapter listener) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", from, to);
        translationX.setDuration(duration);

        if (listener != null) {
            translationX.addListener(listener);
        }

        translationX.start();

        return translationX;

    }

    /**
     * translate along the y axis
     *
     * @param view
     * @param from
     * @param to
     * @param duration
     * @param listener animator listener adapter, can be null
     * @return ObjectAnimator
     */
    public static ObjectAnimator translateY(View view, float from, float to, int duration, AnimatorListenerAdapter listener) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", from, to);
        translationY.setDuration(duration);

        if (listener != null) {
            translationY.addListener(listener);
        }

        translationY.start();

        return translationY;

    }

    /**
     * apply alpha animation to given view
     * @param view
     * @param from
     * @param to
     * @param duration
     * @param listener animator listener adapter, can be null
     * @return ObjectAnimator
     */
    public static ObjectAnimator fade(View view, float from, float to, int duration, AnimatorListenerAdapter listener) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", from, to);
        alpha.setDuration(duration);

        if (listener != null) {
            alpha.addListener(listener);
        }

        alpha.start();

        return alpha;

    }

    /**
     * apply alpha animation to given view and set visibility to gone or visible
     * @param view
     * @param from have to be 1 or 0
     * @param to have to be 1 or 0
     * @param duration
     * @return ObjectAnimator
     */
    public static ObjectAnimator fadeThenGoneOrVisible(final View view, float from, final float to, int duration) {

        if(from == 0){
            view.setVisibility(View.VISIBLE);
        }

        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", from, to);
        alpha.setDuration(duration);

        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(to == 0){
                    view.setVisibility(View.GONE);
                }
            }
        });

        alpha.start();

        return alpha;

    }

    /**
     * rotate view
     *
     * @param view
     * @param from
     * @param to
     * @param duration
     * @param listener listener animator listener adapter, can be null
     * @return ObjectAnimator
     */
    public static ObjectAnimator rotateX(View view, float from, float to, int duration, AnimatorListenerAdapter listener) {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", from, to);
        rotate.setDuration(duration);

        if (listener != null) {
            rotate.addListener(listener);
        }

        rotate.start();

        return rotate;

    }

    /**
     * scale view
     * @param view
     * @param from
     * @param to
     * @param pivotX
     * @param pivotY
     * @param duration
     * @param listener listener animator listener adapter, can be null
     */
    public static void scale(View view, float from, float to, float pivotX, float pivotY, int duration, AnimatorListenerAdapter listener) {
        view.setPivotX(pivotX);
        view.setPivotY(pivotY);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", from, to);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", from, to);

        scaleX.setDuration(duration);
        scaleY.setDuration(duration);


        if (listener != null) {
            scaleX.addListener(listener);
        }

        scaleX.start();
        scaleY.start();

    }

    /**
     * scale view
     * @param view
     * @param from
     * @param to
     * @param duration
     * @param listener listener animator listener adapter, can be null
     */
    public static void scale(View view, float from, float to, int duration, AnimatorListenerAdapter listener) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", from, to);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", from, to);

        scaleX.setDuration(duration);
        scaleY.setDuration(duration);


        if (listener != null) {
            scaleX.addListener(listener);
        }

        scaleX.start();
        scaleY.start();

    }

    /**
     * infinite fade animation
     *
     * @param view
     * @param singleDuration duration of single alpha animation
     * @return AnimatorSet
     */
    public static AnimatorSet fadingInfinite(View view, long singleDuration){
        ObjectAnimator firstFade = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f).setDuration(singleDuration);
        ObjectAnimator secondFade = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f).setDuration(singleDuration);

        final AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet.start();
            }
        });

        animatorSet.play(firstFade).before(secondFade);
        animatorSet.start();

        return animatorSet;
    }

}
