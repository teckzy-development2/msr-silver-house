package com.teckzy.msrsilverhouse.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.Pojo.ChildCartPojo;
import com.teckzy.msrsilverhouse.R;

import java.text.DecimalFormat;
import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.MyViewHolder> {
    public Context mContext;
    private final List<ChildCartPojo> checkoutPojoList;
    DecimalFormat format;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvProductName, tvQuantity, tvProductAmount, tvPrice, tvQuantityName, tvTotalPrice;
        View vw;

        public MyViewHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.ivImage);
            tvProductName = view.findViewById(R.id.tvProductName);
            tvQuantity = view.findViewById(R.id.tvQuantity);
            tvProductAmount = view.findViewById(R.id.tvProductAmount);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvQuantityName = view.findViewById(R.id.tvQuantityName);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
//            vw = view.findViewById(R.id.vw);
        }
    }

    public CheckoutAdapter(Context mContext, List<ChildCartPojo> checkoutPojoList) {
        this.mContext = mContext;
        this.checkoutPojoList = checkoutPojoList;
    }

    @NonNull
    @Override
    public CheckoutAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_checkout, parent, false);

        return new CheckoutAdapter.MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final CheckoutAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final ChildCartPojo checkoutPojo = checkoutPojoList.get(position);

        format = new DecimalFormat("#,###,###");

        Glide.with(holder.ivImage)
                .load(APIClient.BASE_URL + checkoutPojo.getImage())
                .fitCenter()
                .into(holder.ivImage);

        holder.tvProductName.setText(checkoutPojo.getProduct_name());
        holder.tvProductAmount.setText(":" + " " + checkoutPojo.getPrice());
        holder.tvQuantity.setText(" " + String.valueOf(checkoutPojo.getQty()));

//        if (checkoutPojoList.size() == position + 1) {
//            holder.vw.setVisibility(View.GONE);
//        } else {
//            holder.vw.setVisibility(View.VISIBLE);
//        }

        int amount = Integer.parseInt(checkoutPojo.getPrice());
        int quantity = checkoutPojo.getQty();

        int price = amount * quantity;

        holder.tvTotalPrice.setText("₹ " + String.valueOf(price));
    }

    @Override
    public int getItemCount() {
        return checkoutPojoList.size();
    }
}