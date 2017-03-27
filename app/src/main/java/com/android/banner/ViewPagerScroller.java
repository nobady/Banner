package com.android.banner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by tengfei.lv on 2017/3/20.
 */
public class ViewPagerScroller extends Scroller {
    private int mScrollDuration = 0;

    public void setScrollDuration (int scrollDuration) {
        mScrollDuration = scrollDuration;
    }

    public ViewPagerScroller (Context context) {
        super (context);
    }

    public ViewPagerScroller (Context context, Interpolator interpolator) {
        super (context, interpolator);
    }

    public ViewPagerScroller (Context context, Interpolator interpolator, boolean flywheel) {
        super (context, interpolator, flywheel);
    }

    @Override public void startScroll (int startX, int startY, int dx, int dy) {
        super.startScroll (startX, startY, dx, dy,mScrollDuration);
    }

    @Override public void startScroll (int startX, int startY, int dx, int dy, int duration) {
        super.startScroll (startX, startY, dx, dy, mScrollDuration);
    }
}
