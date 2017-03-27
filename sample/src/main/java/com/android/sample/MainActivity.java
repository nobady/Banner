package com.android.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.banner.BannerAdapter;
import com.android.banner.BannerView;
import com.android.banner.BannerViewCreator;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BannerView<Integer> mBannerView;

    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        List<Integer> datas = new ArrayList<> ();
        datas.add (R.drawable.th);
        datas.add (R.drawable.th1);
        datas.add (R.drawable.th2);
        datas.add (R.drawable.th3);
        datas.add (R.drawable.th4);
        datas.add (R.drawable.th5);

        mBannerView = (BannerView<Integer>) findViewById (R.id.bannerView);
        mBannerView.setScrollDuration (50);
        mBannerView.setPageDataView (new BannerViewCreator<Integer> () {
            @Override public View createBannerView (Context context, int position, Integer data) {
                ImageView imageView = new ImageView (context);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams (
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setLayoutParams (layoutParams);
                imageView.setImageResource (data);
                return imageView;
            }
        },datas);

        mBannerView.setOnPageItemClickListener (new BannerAdapter.OnPageItemClickListener<Integer> () {
            @Override public void onPageClick (int position, Integer data) {
                Log.e ("TAG","点击了"+position);
            }
        });
    }
}
