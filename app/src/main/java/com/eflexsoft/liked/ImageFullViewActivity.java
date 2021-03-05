package com.eflexsoft.liked;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.eflexsoft.liked.databinding.ActivityImageFullViewBinding;

public class ImageFullViewActivity extends AppCompatActivity {

    ActivityImageFullViewBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_image_full_view);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_image_full_view);

        String url = getIntent().getStringExtra("url");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.no_p);
        requestOptions.placeholder(R.drawable.no_p);

        Glide.with(this).load(url)
                .apply(requestOptions)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;

                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(binding.img);

    }

    public void goBack(View view) {
        onBackPressed();
    }
}