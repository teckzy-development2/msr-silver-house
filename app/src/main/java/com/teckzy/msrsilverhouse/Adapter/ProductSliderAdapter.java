package com.teckzy.msrsilverhouse.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.Pojo.SliderPojo;
import com.teckzy.msrsilverhouse.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class ProductSliderAdapter extends SliderViewAdapter<ProductSliderAdapter.SliderAdapterViewHolder> {
    private final List<SliderPojo> sliderPojoList;
    Context context;

    public ProductSliderAdapter(Context context, List<SliderPojo> sliderPojoList) {
        this.context = context;
        this.sliderPojoList = sliderPojoList;
    }

    @Override
    public ProductSliderAdapter.SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sliderview, null);
        return new ProductSliderAdapter.SliderAdapterViewHolder(inflate);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(ProductSliderAdapter.SliderAdapterViewHolder viewHolder, final int position) {

        final SliderPojo sliderPojo = sliderPojoList.get(position);

        Glide.with(viewHolder.ivSlider)
                .load(APIClient.BASE_URL + sliderPojo.getImage())
                .fitCenter()
                .into(viewHolder.ivSlider);
    }

    @Override
    public int getCount() {
        return sliderPojoList.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        ImageView ivSlider;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            ivSlider = itemView.findViewById(R.id.ivSlider);
        }
    }
}