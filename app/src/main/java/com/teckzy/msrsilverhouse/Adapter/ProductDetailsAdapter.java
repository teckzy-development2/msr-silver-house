package com.teckzy.msrsilverhouse.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.Pojo.ChildReviewsPojo;
import com.teckzy.msrsilverhouse.R;

import java.util.List;

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.RvViewHolder> {
    Context context;
    List<ChildReviewsPojo> childReviewsPojoList;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public ProductDetailsAdapter(Context context, List<ChildReviewsPojo> childReviewsPojoList) {
        this.context = context;
        this.childReviewsPojoList = childReviewsPojoList;
    }

    View view;

    @Override
    public ProductDetailsAdapter.RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.layout_productdetails, parent, false);
        ProductDetailsAdapter.RvViewHolder rvViewHolder = new ProductDetailsAdapter.RvViewHolder(view);
        return rvViewHolder;
    }

    @Override
    public void onBindViewHolder(ProductDetailsAdapter.RvViewHolder holder, final int position) {
        ChildReviewsPojo childReviewsPojo = childReviewsPojoList.get(position);

        sharedPreferences = context.getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Glide.with(holder.ivImage)
                .load(APIClient.BASE_URL + childReviewsPojo.getImage())
                .fitCenter()
                .into(holder.ivImage);

        holder.tvName.setText(childReviewsPojo.getName());

        if (childReviewsPojo.getRate().equals("0")) {
//            holder.tvRating.setText("No ratings yet");
//            holder.tvRating.setText("");
            holder.ivRating1.setVisibility(View.GONE);
            holder.ivRating2.setVisibility(View.GONE);
            holder.ivRating3.setVisibility(View.GONE);
            holder.ivRating4.setVisibility(View.GONE);
            holder.ivRating5.setVisibility(View.GONE);
        } else {
//            holder.tvRating.setText(childReviewsPojo.getRate());

            if (Double.parseDouble(childReviewsPojo.getRate()) < 1) {
                holder.ivRating1.setImageDrawable(context.getResources().getDrawable(R.drawable.half_filled_star));
                holder.ivRating2.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_star));
                holder.ivRating3.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_star));
                holder.ivRating4.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_star));
                holder.ivRating5.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_star));
            } else if (Double.parseDouble(childReviewsPojo.getRate()) == 1) {
                holder.ivRating1.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating2.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_star));
                holder.ivRating3.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_star));
                holder.ivRating4.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_star));
                holder.ivRating5.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_star));
            } else if ((Double.parseDouble(childReviewsPojo.getRate()) > 1)
                    && (Double.parseDouble(childReviewsPojo.getRate()) < 2)) {
                holder.ivRating1.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating2.setImageDrawable(context.getResources().getDrawable(R.drawable.half_filled_star));
                holder.ivRating3.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_star));
                holder.ivRating4.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_star));
                holder.ivRating5.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_star));
            } else if (Double.parseDouble(childReviewsPojo.getRate()) == 2) {
                holder.ivRating1.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating2.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating3.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_star));
                holder.ivRating4.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_star));
                holder.ivRating5.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_star));
            } else if ((Double.parseDouble(childReviewsPojo.getRate()) > 2)
                    && (Double.parseDouble(childReviewsPojo.getRate()) < 3)) {
                holder.ivRating1.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating2.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating3.setImageDrawable(context.getResources().getDrawable(R.drawable.half_filled_star));
                holder.ivRating4.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_star));
                holder.ivRating5.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_star));
            } else if (Double.parseDouble(childReviewsPojo.getRate()) == 3) {
                holder.ivRating1.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating2.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating3.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating4.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_star));
                holder.ivRating5.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_star));
            } else if ((Double.parseDouble(childReviewsPojo.getRate()) > 3)
                    && (Double.parseDouble(childReviewsPojo.getRate()) < 4)) {
                holder.ivRating1.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating2.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating3.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating4.setImageDrawable(context.getResources().getDrawable(R.drawable.half_filled_star));
                holder.ivRating5.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_star));
            } else if (Double.parseDouble(childReviewsPojo.getRate()) == 4) {
                holder.ivRating1.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating2.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating3.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating4.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating5.setImageDrawable(context.getResources().getDrawable(R.drawable.outline_star));
            } else if ((Double.parseDouble(childReviewsPojo.getRate()) > 4)
                    && (Double.parseDouble(childReviewsPojo.getRate()) < 5)) {
                holder.ivRating1.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating2.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating3.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating4.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating5.setImageDrawable(context.getResources().getDrawable(R.drawable.half_filled_star));
            } else if (Double.parseDouble(childReviewsPojo.getRate()) == 5) {
                holder.ivRating1.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating2.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating3.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating4.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
                holder.ivRating5.setImageDrawable(context.getResources().getDrawable(R.drawable.filled_star));
            }
        }

        holder.tvDate.setText(childReviewsPojo.getDate());

        if (childReviewsPojo.getMessage().equals("")) {
            holder.tvReview.setVisibility(View.GONE);
        } else {
            holder.tvReview.setVisibility(View.VISIBLE);
            holder.tvReview.setText(childReviewsPojo.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return childReviewsPojoList.size();
    }

    public class RvViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDate,tvReview;
        ImageView ivImage;
        ImageView ivRating1,ivRating2,ivRating3,ivRating4,ivRating5;

        public RvViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
//            tvRating = itemView.findViewById(R.id.tvRating);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvReview = itemView.findViewById(R.id.tvReview);

            ivImage = itemView.findViewById(R.id.ivImage);
            ivRating1 = itemView.findViewById(R.id.ivRating1);
            ivRating2 = itemView.findViewById(R.id.ivRating2);
            ivRating3 = itemView.findViewById(R.id.ivRating3);
            ivRating4 = itemView.findViewById(R.id.ivRating4);
            ivRating5 = itemView.findViewById(R.id.ivRating5);
        }
    }
}