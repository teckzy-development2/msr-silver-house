package com.teckzy.msrsilverhouse.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.recyclerview.widget.RecyclerView;

import com.teckzy.msrsilverhouse.Interface.BottomSheetData;
import com.teckzy.msrsilverhouse.Pojo.ChildPricePojo;
import com.teckzy.msrsilverhouse.R;

import java.util.List;

public class SubsribtionWeightAdapter extends RecyclerView.Adapter<SubsribtionWeightAdapter.RvViewHolder> {
    Activity activity;
    List<ChildPricePojo> childPricePojos;
    BottomSheetData bottomSheetData;
    private int selectedItem;

    public SubsribtionWeightAdapter(Activity activity, List<ChildPricePojo> childPricePojos, BottomSheetData bottomSheetData) {
        this.activity = activity;
        this.childPricePojos = childPricePojos;
        this.bottomSheetData = bottomSheetData;
    }

    View view;

    @Override
    public SubsribtionWeightAdapter.RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.bottomsheet_layout_weight, parent, false);
        SubsribtionWeightAdapter.RvViewHolder rvViewHolder = new SubsribtionWeightAdapter.RvViewHolder(view);
        return rvViewHolder;
    }

    @Override
    public void onBindViewHolder(SubsribtionWeightAdapter.RvViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ChildPricePojo childPricePojo = childPricePojos.get(position);

        holder.radioWeight.setText(childPricePojo.getWeights() + " KG" + "/" + "₹ " + childPricePojo.getAmounts());

        if (selectedItem == position && childPricePojo.getSelected()) {
            holder.radioWeight.setChecked(true);
            childPricePojo.setSelected(true);
        } else {
            holder.radioWeight.setChecked(false);
            childPricePojo.setSelected(false);
        }

        holder.radioWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childPricePojo.setSelected(true);
                selectedItem = position;
                notifyDataSetChanged();
                bottomSheetData.response(childPricePojo.getWeights(), childPricePojo.getAmounts());
            }
        });
    }

    @Override
    public int getItemCount() {
        return childPricePojos.size();
    }

    public class RvViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioWeight;

        public RvViewHolder(View itemView) {
            super(itemView);
            radioWeight = itemView.findViewById(R.id.radioWeight);
        }
    }
}