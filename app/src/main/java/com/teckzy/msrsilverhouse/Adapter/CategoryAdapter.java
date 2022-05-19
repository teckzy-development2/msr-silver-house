package com.teckzy.msrsilverhouse.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.Activity.ProductActivity;
import com.teckzy.msrsilverhouse.Pojo.CategoryPojo;
import com.teckzy.msrsilverhouse.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.RvViewHolder>
{
    Context context;
    List<CategoryPojo> categoryIndianPojos;

    public CategoryAdapter(Context context, List<CategoryPojo> categoryIndianPojos) {
        this.context = context;
        this.categoryIndianPojos = categoryIndianPojos;
    }

    View view;

    @Override
    public CategoryAdapter.RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.layout_category, parent, false);
        CategoryAdapter.RvViewHolder rvViewHolder = new CategoryAdapter.RvViewHolder(view);
        return rvViewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.RvViewHolder holder, final int position) {
        CategoryPojo categoryIndianPojo = categoryIndianPojos.get(position);

        Glide.with(holder.categoryimg)
                .load(APIClient.BASE_URL + categoryIndianPojo.getCategory_image())
                .fitCenter()
                .into(holder.categoryimg);
        holder.categorytv.setText(categoryIndianPojo.getCategory_name());

        holder.linearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("category_id",categoryIndianPojo.getCategory_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryIndianPojos.size();
    }

    public class RvViewHolder extends RecyclerView.ViewHolder {
        TextView categorytv;
        ImageView categoryimg;
        LinearLayout linearLayout;

        public RvViewHolder(View itemView) {
            super(itemView);
            categorytv = itemView.findViewById(R.id.categorytv);
            categoryimg = itemView.findViewById(R.id.categoryimg);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}