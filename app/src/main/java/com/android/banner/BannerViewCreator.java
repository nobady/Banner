package com.android.banner;

import android.content.Context;
import android.view.View;

/**
 * Created by tengfei.lv on 2017/3/17.
 */
public interface BannerViewCreator<T> {

    View createBannerView(Context context,int position,T data);
}
