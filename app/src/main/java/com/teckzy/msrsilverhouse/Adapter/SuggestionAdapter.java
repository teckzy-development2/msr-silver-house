package com.teckzy.msrsilverhouse.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teckzy.msrsilverhouse.Activity.ProductDetailsActivity;
import com.teckzy.msrsilverhouse.Pojo.SuggestionPojo;
import com.teckzy.msrsilverhouse.R;

import java.util.List;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.MyViewHolder> {
    public Context context;
    private final List<SuggestionPojo> suggestionPojoList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSuggestionName;

        public MyViewHolder(View view) {
            super(view);
            tvSuggestionName = view.findViewById(R.id.tvSuggestionName);
        }
    }

    public SuggestionAdapter(Context context, List<SuggestionPojo> suggestionPojoList) {
        this.context = context;
        this.suggestionPojoList = suggestionPojoList;
    }

    @NonNull
    @Override
    public SuggestionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_suggestion, parent, false);

        return new SuggestionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SuggestionAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final SuggestionPojo suggestionPojo = suggestionPojoList.get(position);

        holder.tvSuggestionName.setText(suggestionPojo.getProduct_name());

        holder.tvSuggestionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("productId", suggestionPojo.getProduct_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return suggestionPojoList.size();
    }
}