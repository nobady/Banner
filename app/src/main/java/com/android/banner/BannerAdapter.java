package com.android.banner;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tengfei.lv on 2017/3/17.
 */
public class BannerAdapter<T> extends PagerAdapter {

    private List<T> mDatas;
    private BannerViewCreator mBannerViewCreator;

    private OnPageItemClickListener mPageItemClickListener;

    private OnTouchEventListener mOnTouchEventListener;

    private SparseArray<View> views = new SparseArray<> ();

    public void setBannerViewCreator (BannerViewCreator bannerViewCreator) {
        mBannerViewCreator = bannerViewCreator;
    }

    public void setOnTouchEventListener (OnTouchEventListener onTouchEventListener) {
        mOnTouchEventListener = onTouchEventListener;
    }

    public void setPageItemClickListener (OnPageItemClickListener pageItemClickListener) {
        mPageItemClickListener = pageItemClickListener;
    }

    public void setDatas (List<T> datas) {
        mDatas = new ArrayList<> ();
        mDatas.add (0,datas.get (datas.size ()-1));
        mDatas.addAll (datas);
        mDatas.add (datas.get (0));
    }

    @Override public int getCount () {
        return mDatas == null ? 0 : mDatas.size ();
    }

    @Override public boolean isViewFromObject (View view, Object object) {
        return view == object;
    }

    @Override public Object instantiateItem (ViewGroup container, final int position) {

        View view = views.get (position);

        if (mBannerViewCreator==null){
            throw new RuntimeException ("请实现BannerViewCreator接口");
        }

        if (view==null){
            view = mBannerViewCreator.createBannerView (container.getContext (), position,
                mDatas.get (position));
            views.put (position,view);
        }
        view.setOnClickListener (new View.OnClickListener () {
            @Override public void onClick (View v) {
                if (mPageItemClickListener!=null){
                    mPageItemClickListener.onPageClick (position,mDatas.get (position));
                }
            }
        });

        view.setOnTouchListener (new View.OnTouchListener () {
            @Override public boolean onTouch (View v, MotionEvent event) {
                return mOnTouchEventListener.touchEvent (event);
            }
        });

        ViewParent parent = view.getParent ();
        if (parent!=null){
            ViewGroup vp = (ViewGroup) parent;
            vp.removeView (view);
        }

        container.addView (view);

        return view;
    }

    @Override public void destroyItem (ViewGroup container, int position, Object object) {
    }

    public interface OnPageItemClickListener<T>{
        void onPageClick(int position,T data);
    }

    interface OnTouchEventListener{
        boolean touchEvent(MotionEvent event);
    }
}
