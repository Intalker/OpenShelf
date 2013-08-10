package com.intalker.openshelf.ui.control;

import com.intalker.openshelf.R;
import com.intalker.openshelf.animation.Rotate3dAnimation;

import android.app.Dialog;
import android.content.Context;
import android.widget.FrameLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

public class FlipPanel extends Dialog {

    private FrameLayout mContent = null;
    private View mViewA = null;
    private View mViewB = null;

    private long mDuration = 300;
    private float mDepth = 500.0f;
    
    private float mPrecision = 4.0f;
    private float mMidPos = 90.0f - mPrecision;
    
    public FlipPanel(Context context) {
        super(context, R.style.Theme_TransparentDialog);
        createUI(context);
    }
    
    @Override
    public void show() {
        // TODO Auto-generated method stub
        super.show();
        mViewA.setVisibility(View.VISIBLE);
        mViewB.setVisibility(View.INVISIBLE);
        mViewA.requestFocus();

//        AnimationSet animSet = new AnimationSet(true);
//        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
//        alphaAnimation.setFillAfter(true);
        
//        ScaleAnimation scaleAnimation = new ScaleAnimation(3.5f, 1.0f, 3.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        scaleAnimation.setFillAfter(true);
        
//        animSet.addAnimation(alphaAnimation);
//        animSet.addAnimation(scaleAnimation);
//        animSet.setInterpolator(new DecelerateInterpolator());
//        animSet.setDuration(2000);
//        
//        mContent.startAnimation(animSet);
    }

    private void createUI(Context context) {
        mContent = new FrameLayout(context);
        this.setContentView(mContent);
        mContent.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
    }

    public void flip(boolean forward)
    {
        if (forward)
        {
            applyRotation(-1, 0, -mMidPos);
        }
        else
        {
            applyRotation(1, 0, mMidPos);
        }
    }

    public void setBackgroundImage(int resId)
    {
        mContent.setBackgroundResource(resId);
    }
    
    public void setViewA(View v)
    {
        mViewA = v;
        mContent.addView(mViewA);
    }
    
    public void setViewB(View v)
    {
        mViewB = v;
        mViewB.setVisibility(View.GONE);
        mContent.addView(mViewB);
    }
    
    /**
     * Setup a new 3D rotation on the container view.
     *
     * @param position the item that was clicked to show a picture, or -1 to show the list
     * @param start the start angle at which the rotation must begin
     * @param end the end angle of the rotation
     */
    private void applyRotation(int position, float start, float end) {
        // Find the center of the container
        final float centerX = mContent.getWidth() / 2.0f;
        final float centerY = mContent.getHeight() / 2.0f;

        final Rotate3dAnimation rotation =
                new Rotate3dAnimation(start, end, centerX, centerY, mDepth, true);
        rotation.setDuration(mDuration);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(position));

        mContent.startAnimation(rotation);
    }

    /**
     * This class listens for the end of the first half of the animation.
     * It then posts a new action that effectively swaps the views when the container
     * is rotated 90 degrees and thus invisible.
     */
    private final class DisplayNextView implements Animation.AnimationListener {
        private final int mPosition;

        private DisplayNextView(int position) {
            mPosition = position;
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            mContent.post(new SwapViews(mPosition));
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    /**
     * This class is responsible for swapping the views and start the second
     * half of the animation.
     */
    private final class SwapViews implements Runnable {
        private final int mPosition;

        public SwapViews(int position) {
            mPosition = position;
        }

        public void run() {
            final float centerX = mContent.getWidth() / 2.0f;
            final float centerY = mContent.getHeight() / 2.0f;
            Rotate3dAnimation rotation;
            
            if (mPosition > -1) {
                mViewB.setVisibility(View.INVISIBLE);
                mViewA.setVisibility(View.VISIBLE);
                mViewA.requestFocus();
                rotation = new Rotate3dAnimation(-mMidPos, 0, centerX, centerY, mDepth, false);
            } else {
                mViewA.setVisibility(View.INVISIBLE);
                mViewB.setVisibility(View.VISIBLE);
                mViewB.requestFocus();
                rotation = new Rotate3dAnimation(mMidPos, 0, centerX, centerY, mDepth, false);
            }

            rotation.setDuration(mDuration);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());

            mContent.startAnimation(rotation);
        }
    }

}
