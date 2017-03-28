package com.android.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by tengfei.lv on 2017/3/17.
 */
public class BannerView<T> extends FrameLayout {

    private static final int REMOVE_TASK = 1;
    private static final int START_TASK = 2;
    private Context mContext;
    private ViewPager mViewPager;
    private ViewPagerScroller mScroll;
    private BannerAdapter.OnPageItemClickListener<T> mOnPageItemClickListener;
    private BannerAdapter<T> mAdapter;

    private int currentItem = 0;
    /**
     * 轮播的默认时间
     */
    private int DELAYTIEM = 1000;
    private Handler mHandler = new MyHandler (this);
    private Runnable updateItemTask = new UpdateItemTask (this);
    /**
     * 指示器距底部的边距
     */
    private float mMarginBottom = 5;
    /**
     * 指示器的每个圆点的水平间距
     */
    private float mHorizontalSpace = 3;
    /**
     * 选中的背景
     */
    private int mSelectBackgroundId = R.drawable.icon_point_select;
    /**
     * 未选中的背景
     */
    private int mUnSelectBackgroundId = R.drawable.icon_point_un_select;
    private LinearLayout mIndicatorLayout;
    /**
     * 指示器的位置
     */
    private int mGravity = Gravity.CENTER | Gravity.BOTTOM;
    /**
     * 是否支持自动轮播
     */
    private boolean mIsBanner;

    /*handler和runnable都使用静态内部类，目的是防止内存泄漏,在activity销毁时，调用onDestory()方法*/

    private static class MyHandler extends Handler {
        private WeakReference<BannerView> mWeakReference;

        public MyHandler (BannerView context) {
            mWeakReference = new WeakReference<> (context);
        }

        @Override public void handleMessage (Message msg) {
            super.handleMessage (msg);
            BannerView bannerView = mWeakReference.get ();
            if (bannerView != null) {
                switch (msg.what) {
                    case REMOVE_TASK:
                        bannerView.removeTask ();
                        break;
                    case START_TASK:
                        bannerView.startTask ();
                        break;
                }
            }
        }
    }

    private static class UpdateItemTask implements Runnable {
        private WeakReference<BannerView> mWeakReference;

        public UpdateItemTask (BannerView context) {
            mWeakReference = new WeakReference<> (context);
        }

        @Override public void run () {
            BannerView bannerView = mWeakReference.get ();
            if (bannerView != null) {
                bannerView.updateItem ();
            }
        }
    }

    /**
     * 在手指按下时调用，目的让viewpager停止自动循环
     */
    private void removeTask () {
        if (mIsBanner) {
            mHandler.removeCallbacks (updateItemTask);
        }
    }

    /**
     * 在手指抬起之后调用，目的是让viewpager可以继续自动循环
     */
    private void startTask () {
        if (mIsBanner) {
            mHandler.postDelayed (updateItemTask, DELAYTIEM * 3);
        }
    }

    public void setMarginBottom (float marginBottom) {
        mMarginBottom = marginBottom;
    }

    public void setHorizontalSpace (float horizontalSpace) {
        mHorizontalSpace = horizontalSpace;
    }

    public void setSelectBackgroundId (@DrawableRes int selectBackgroundId) {
        mSelectBackgroundId = selectBackgroundId;
    }

    public void setUnSelectBackgroundId (@DrawableRes int unSelectBackgroundId) {
        mUnSelectBackgroundId = unSelectBackgroundId;
    }

    public void setGravity (int gravity) {
        mGravity = gravity;
    }

    public void setBanner (boolean banner) {
        mIsBanner = banner;
    }

    /**
     * 切换view
     */
    private void updateItem () {
        currentItem = mViewPager.getCurrentItem ();
        if (currentItem == mAdapter.getCount () - 1) {
            currentItem = -1;
        }
        mViewPager.setCurrentItem (++currentItem);
        mHandler.postDelayed (updateItemTask, 2000);
    }

    @Override public void onWindowFocusChanged (boolean hasWindowFocus) {
        super.onWindowFocusChanged (hasWindowFocus);
        if (hasWindowFocus) {
            mHandler.postDelayed (updateItemTask, DELAYTIEM);
        } else {
            mHandler.removeCallbacks (updateItemTask);
        }
    }

    /**
     * 设置item的点击事件
     */
    public void setOnPageItemClickListener (
        BannerAdapter.OnPageItemClickListener<T> onPageItemClickListener) {
        mOnPageItemClickListener = onPageItemClickListener;
        if (mAdapter != null) {
            mAdapter.setPageItemClickListener (onPageItemClickListener);
        }
    }

    /**
     * 设置viewpager滑动速度
     */
    public void setScrollDuration (int scrollDuration) {
        mScroll.setScrollDuration (scrollDuration);
    }

    public BannerView (@NonNull Context context) {
        super (context);
        mContext = context;
        initViewpager ();
        initIndicator ();
    }

    public BannerView (@NonNull Context context, @Nullable AttributeSet attrs) {
        super (context, attrs);
        mContext = context;
        /*获取自定义的属性值*/
        TypedArray typedArray = context.obtainStyledAttributes (attrs, R.styleable.BannerView);
        mMarginBottom = typedArray.getDimension (R.styleable.BannerView_margin_bottom, 5);
        mHorizontalSpace = typedArray.getDimension (R.styleable.BannerView_horizontal_space, 3);
        mSelectBackgroundId = typedArray.getResourceId (R.styleable.BannerView_select_background,
            R.drawable.icon_point_select);
        mUnSelectBackgroundId =
            typedArray.getResourceId (R.styleable.BannerView_un_select_background,
                R.drawable.icon_point_un_select);
        mGravity = typedArray.getInt (R.styleable.BannerView_gravity,
            Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);

        mIsBanner = typedArray.getBoolean (R.styleable.BannerView_isBanner, true);
        typedArray.recycle ();

        initViewpager ();
        initIndicator ();
    }

    public BannerView (@NonNull Context context, @Nullable AttributeSet attrs,
        @AttrRes int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        mContext = context;
        initViewpager ();
        initIndicator ();
    }

    /**
     * 创建指示器的父布局，即创建一个线性布局
     */
    private void initIndicator () {
        mIndicatorLayout = new LinearLayout (mContext);
        mIndicatorLayout.setOrientation (LinearLayout.HORIZONTAL);
        FrameLayout.LayoutParams layoutParams =
            new LayoutParams (ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = mGravity;
        layoutParams.bottomMargin = (int) mMarginBottom;
        mIndicatorLayout.setLayoutParams (layoutParams);
        addView (mIndicatorLayout);
    }

    /**
     * 初始化viewpager
     */
    private void initViewpager () {
        mViewPager = new ViewPager (mContext);
        initViewPagerScroller ();
        addView (mViewPager);

        /*监听手指抬起的操作。本来在adapter中监听每一个item的按下和抬起的操作，
        但是在滑动的时候，抬起的手势没有被执行，所以就在这里监听viewpager的抬起手势*/
        mViewPager.setOnTouchListener (new OnTouchListener () {
            @Override public boolean onTouch (View v, MotionEvent event) {
                if (event.getAction () == MotionEvent.ACTION_UP) {
                    mHandler.sendEmptyMessage (START_TASK);
                }
                return false;
            }
        });
        /*监听viewpager的页面改变*/
        mViewPager.addOnPageChangeListener (new ViewPager.SimpleOnPageChangeListener () {
            @Override public void onPageSelected (int position) {
                super.onPageSelected (position);
                /*因为在adapter中，其实是在集合的首尾各添加了一个对象，目的是在最后一个
                位置往右滑或者第一个位置往左滑的时候看起来没有那么生硬，然后在这里就需要根据position来动态的设置viewpager的item*/
                if (mAdapter.getCount () > 1) {
                    if (position < 1) {
                        mViewPager.setCurrentItem (mAdapter.getCount () - 2, false);
                    } else if (position > mAdapter.getCount () - 2) {
                        mViewPager.setCurrentItem (1, false);
                    }
                }
                /*改变指示器的UI*/
                int newPosition = mViewPager.getCurrentItem () - 1;
                int oldPosition = newPosition - 1;
                if (newPosition == 0) {
                    oldPosition = mAdapter.getCount () - 2 - 1;
                }
                changedIndicator (oldPosition, newPosition);
            }
        });
    }

    /**
     * 设置viewpager滑动的速度
     */
    private void initViewPagerScroller () {
        try {
            Field scroller = ViewPager.class.getDeclaredField ("mScroller");
            scroller.setAccessible (true);
            mScroll = new ViewPagerScroller (mContext);
            scroller.set (mViewPager, mScroll);
        } catch (NoSuchFieldException e) {
            e.printStackTrace ();
        } catch (IllegalAccessException e) {
            e.printStackTrace ();
        }
    }

    /**
     * 设置view创造器和数据
     */
    public void setPageDataView (BannerViewCreator<T> creator, List<T> datas) {
        mAdapter = new BannerAdapter<> ();
        mAdapter.setDatas (datas);
        mAdapter.setBannerViewCreator (creator);
        if (mOnPageItemClickListener != null) {
            mAdapter.setPageItemClickListener (mOnPageItemClickListener);
        }
        mViewPager.setAdapter (mAdapter);
        mViewPager.setCurrentItem (1);

        createIndicator (datas);

        /*设置监听手指按下或者抬起的操作，目的是在手指按下时，停止自动滑动，手指抬起的时候开始自动滑动*/
        mAdapter.setOnTouchEventListener (new BannerAdapter.OnTouchEventListener () {
            @Override public boolean touchEvent (MotionEvent event) {
                switch (event.getAction ()) {
                    case MotionEvent.ACTION_DOWN:
                        mHandler.sendEmptyMessage (REMOVE_TASK);
                        return false;
                    case MotionEvent.ACTION_UP:
                        mHandler.sendEmptyMessage (START_TASK);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 根据集合的大小创建指示器的点
     */
    private void createIndicator (List<T> datas) {
        Log.e ("TAG", "size = " + datas.size ());
        for (int i = 0, size = datas.size (); i < size; i++) {
            int item = mViewPager.getCurrentItem () - 1;
            ImageView imageView = new ImageView (mContext);
            imageView.setPadding (0, 0, (int) mHorizontalSpace, 0);
            if (item == i) {
                imageView.setImageResource (mSelectBackgroundId);
            } else {
                imageView.setImageResource (mUnSelectBackgroundId);
            }
            mIndicatorLayout.addView (imageView);
        }
    }

    /**
     * 在item改变的时候。指示器的UI也要改变
     *
     * @param oldPosition
     *     上一个item的位置
     * @param newPosition
     *     正在显示的item的位置
     */
    private void changedIndicator (int oldPosition, int newPosition) {
        View childAt = mIndicatorLayout.getChildAt (oldPosition);
        View childAt1 = mIndicatorLayout.getChildAt (newPosition);
        if (childAt != null && childAt1 != null) {
            ((ImageView) childAt).setImageResource (mUnSelectBackgroundId);
            ((ImageView) childAt1).setImageResource (mSelectBackgroundId);
        }
    }

    /**
     * 释放资源，在页面销毁的时候调用
     */
    public void onDestory () {
        mHandler.removeCallbacksAndMessages (null);
        mHandler = null;
        updateItemTask = null;
    }
}
