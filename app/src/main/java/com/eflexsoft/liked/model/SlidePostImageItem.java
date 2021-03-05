package com.eflexsoft.liked.model;

import androidx.databinding.BindingAdapter;

import com.github.chrisbanes.photoview.PhotoView;

public class SlidePostImageItem {

    private String url;

    public SlidePostImageItem(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @BindingAdapter("android:loadImg")
    public static void loadImg(PhotoView photoView, String url) {



    }
}
