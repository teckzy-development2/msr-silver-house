package com.teckzy.msrsilverhouse.Adapter;

import android.annotation.SuppressLint;
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
import com.teckzy.msrsilverhouse.Interface.PassData;
import com.teckzy.msrsilverhouse.Pojo.SubCategoryPojo;
import com.teckzy.msrsilverhouse.R;
import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.RvViewHolder>
{
    Context context;
    List<SubCategoryPojo> subCategoryPojoList;
    PassData passData;
    int selectedItem;

    public SubCategoryAdapter(Context context, List<SubCategoryPojo> subCategoryPojoList, PassData passData)
    {
        this.context = context;
        this.subCategoryPojoList = subCategoryPojoList;
        this.passData = passData;
    }

    View view;

    @Override
    public SubCategoryAdapter.RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.layout_sub_category, parent, false);
        SubCategoryAdapter.RvViewHolder rvViewHolder = new SubCategoryAdapter.RvViewHolder(view);
        return rvViewHolder;
    }

    @Override
    public void onBindViewHolder(SubCategoryAdapter.RvViewHolder holder, @SuppressLint("RecyclerView") final int position)
    {
        SubCategoryPojo subCategoryPojo = subCategoryPojoList.get(position);

        Glide.with(holder.ivSubCategory)
                .load(APIClient.BASE_URL + subCategoryPojo.getSubCatImage())
                .fitCenter()
                .into(holder.ivSubCategory);

        holder.tvSubCategory.setText(subCategoryPojo.getSubCatName());

        if (selectedItem == position && subCategoryPojo.getSelected())
        {
            holder.llSubCategory.setBackground(context.getResources().getDrawable(R.drawable.select_bg));
            subCategoryPojo.setSelected(true);
        }
        else
        {
            holder.llSubCategory.setBackground(context.getResources().getDrawable(R.drawable.app_select_bg));
            subCategoryPojo.setSelected(false);
        }

        holder.llSubCategory.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                subCategoryPojo.setSelected(true);
                selectedItem = position;
                notifyDataSetChanged();
                passData.respond(subCategoryPojo.getSubCatId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCategoryPojoList.size();
    }

    public class RvViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvSubCategory;
        ImageView ivSubCategory;
        LinearLayout llSubCategory;

        public RvViewHolder(View itemView)
        {
            super(itemView);
            tvSubCategory = itemView.findViewById(R.id.tvSubCategory);
            ivSubCategory = itemView.findViewById(R.id.ivSubCategory);
            llSubCategory = itemView.findViewById(R.id.llSubCategory);
        }
    }
}
