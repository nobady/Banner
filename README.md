# Banner
支持任意view的轮播框架

**使用方法**

    compile 'com.android:bannerview:1.0.0'


## 在布局文件中使用 ##

      `<com.android.banner.BannerView
        android:id="@+id/bannerView"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        app:horizontal_space="7dp"
        app:isBanner="true"
        app:margin_bottom="15dp"
        app:select_background="@drawable/select_shape"
        app:un_select_background="@drawable/shape_un_select"
        app:gravity="END|BOTTOM"
        />`
   


- horizontal_space：表示指示器item间距
- isBanner：是否支持自动轮播
- margin_bottom：指示器和底部的距离
- select_background：指示器item选中的资源id
- un_select_background：指示器item未选中的资源id
- gravity：指示器显示的位置

  然后在布局中获取实例，设置数据和itemview
    
	`mBannerView = (BannerView<Integer>) findViewById (R.id.bannerView);
        mBannerView.setScrollDuration (50);
        mBannerView.setPageDataView (new BannerViewCreator<Integer> () {
            @Override public View createBannerView (Context context, int position, Integer data) {
                //返回每个item要显示的view
                ImageView imageView = new ImageView (context);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams (
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setLayoutParams (layoutParams);
                imageView.setImageResource (data);
                return imageView;
            }
        },datas);`

## 在代码中创建 ##

	` BannerView bannerView = new BannerView (this);
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
        ll.addView (bannerView);`
    
**注意：**必须要在设置属性完成之后调用setPageDataView方法，否则第一次显示的时候，会出现默认的属性值的情况
[博客链接-http://blog.csdn.net/hello_word_1024/article/details/67636891](http://blog.csdn.net/hello_word_1024/article/details/67636891)