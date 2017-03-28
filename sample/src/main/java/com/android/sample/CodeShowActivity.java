package com.android.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.banner.BannerView;
import com.android.banner.BannerViewCreator;
import java.util.ArrayList;
import java.util.List;

/**
 * 在代码中使用
 * Created by tengfei.lv on 2017/3/28.
 */
public class CodeShowActivity extends AppCompatActivity {

    @Override protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_code);

        List<Integer> datas = new ArrayList<> ();
        datas.add (R.drawable.th);
        datas.add (R.drawable.th1);
        datas.add (R.drawable.th2);
        datas.add (R.drawable.th3);
        datas.add (R.drawable.th4);
        datas.add (R.drawable.th5);
        /*需要先将属性设置完成，才能设置数据*/
        BannerView bannerView = new BannerView (this);
        bannerView.setGravity (Gravity.CENTER);
        bannerView.setScrollDuration (60);
        bannerView.setHorizontalSpace (6);
        bannerView.setMarginBottom (2);
        bannerView.setSelectBackgroundId (R.drawable.select_shape);
        bannerView.setUnSelectBackgroundId (R.drawable.shape_un_select);
        bannerView.setPageDataView (new BannerViewCreator<Integer> () {
            @Override public View createBannerView (Context context, int position, Integer data) {
                ImageView imageView = new ImageView (context);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams (
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setLayoutParams (layoutParams);
                imageView.setImageResource (data);
                return imageView;
            }
        },datas);

        LinearLayout ll = (LinearLayout) findViewById (R.id.ll);
        ll.addView (bannerView);
    }
}
