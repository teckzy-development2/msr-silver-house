package com.teckzy.msrsilverhouse.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
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
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.Interface.PassData;
import com.teckzy.msrsilverhouse.Pojo.SubscribtionDetailsPojo;
import com.teckzy.msrsilverhouse.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class CheckoutSubscribtionAdapter extends RecyclerView.Adapter<CheckoutSubscribtionAdapter.RvViewHolder> {
    Context context;
    List<SubscribtionDetailsPojo> subscribtionDetailsPojoList;
    PassData passData;
    APIInterface apiInterface;
    JSONArray jsonArray;
    JSONObject jsonObject;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public CheckoutSubscribtionAdapter(Context context, List<SubscribtionDetailsPojo> subscribtionDetailsPojoList, PassData passData) {
        this.context = context;
        this.subscribtionDetailsPojoList = subscribtionDetailsPojoList;
        this.passData = passData;
    }

    View view;

    @Override
    public CheckoutSubscribtionAdapter.RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.layout_checkoutsubscribtion, parent, false);
        CheckoutSubscribtionAdapter.RvViewHolder rvViewHolder = new CheckoutSubscribtionAdapter.RvViewHolder(view);
        return rvViewHolder;
    }

    @Override
    public void onBindViewHolder(CheckoutSubscribtionAdapter.RvViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        SubscribtionDetailsPojo subscribtionDetailsPojo = subscribtionDetailsPojoList.get(position);

        sharedPreferences = context.getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Glide.with(holder.ivSubscribtion)
                .load(APIClient.BASE_URL + subscribtionDetailsPojo.getImage())
                .fitCenter()
                .into(holder.ivSubscribtion);

        holder.tvSubscribtionName.setText(subscribtionDetailsPojo.getSubscribtion_name());
        holder.tvProducts.setText(String.valueOf(subscribtionDetailsPojo.getProducts()));
        holder.tvWeight.setText(subscribtionDetailsPojo.getWeight() + " kg");
        holder.tvAmount.setText("₹ " + subscribtionDetailsPojo.getAmount());
    }

    @Override
    public int getItemCount() {
        return subscribtionDetailsPojoList.size();
    }

    public class RvViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubscribtionName, tvProducts, tvWeight, tvAmount;
        ImageView ivSubscribtion, deleteIcon;

        public RvViewHolder(View itemView) {
            super(itemView);
            tvSubscribtionName = itemView.findViewById(R.id.tvSubscribtionName);
            tvProducts = itemView.findViewById(R.id.tvProducts);
            tvWeight = itemView.findViewById(R.id.tvWeight);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            ivSubscribtion = itemView.findViewById(R.id.ivSubscribtion);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
        }
    }
}