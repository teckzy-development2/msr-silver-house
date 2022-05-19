package com.teckzy.msrsilverhouse.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.Activity.OrderDetailsActivity;
import com.teckzy.msrsilverhouse.Pojo.MyOrdersPojo;
import com.teckzy.msrsilverhouse.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.RvViewHolder> {
    public Context context;
    private final List<MyOrdersPojo> myOrdersPojos;
    MyOrdersDetailAdapter orderDetailAdapter;
    APIInterface apiInterface;
    JSONArray jsonArray;
    JSONObject jsonObject;
    AlertDialog.Builder builder;

    public MyOrdersAdapter(Context context, List<MyOrdersPojo> myOrdersPojos) {
        this.context = context;
        this.myOrdersPojos = myOrdersPojos;
    }

    View view;

    @Override
    public MyOrdersAdapter.RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.layout_myorder, parent, false);
        MyOrdersAdapter.RvViewHolder rvViewHolder = new MyOrdersAdapter.RvViewHolder(view);
        return rvViewHolder;
    }

    @Override
    public void onBindViewHolder(MyOrdersAdapter.RvViewHolder holder, final int position) {
        MyOrdersPojo myOrdersPojo = myOrdersPojos.get(position);

        holder.tvOrderId.setText(myOrdersPojo.getOrder_unique_id());
        holder.tvOrderDate.setText(myOrdersPojo.getOrder_date());
        holder.tvDeliverDate.setText(myOrdersPojo.getEstimation_delivery());

        orderDetailAdapter = new MyOrdersDetailAdapter(context, myOrdersPojo.getProducts());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 1);
        holder.rvOrderDetail.setLayoutManager(mLayoutManager);
        holder.rvOrderDetail.setItemAnimator(new DefaultItemAnimator());
        holder.rvOrderDetail.setAdapter(orderDetailAdapter);

        holder.tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Do you want to cancel order?");
                builder.setTitle("Alert !!!");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        apiMyorderCancel(myOrdersPojo.getOrder_id());
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        holder.cvMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("order_id", myOrdersPojo.getOrder_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    public void apiMyorderCancel(String order_id) {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.cancel_order(order_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        jsonArray = new JSONArray(response.body());
                        jsonObject = jsonArray.getJSONObject(0);
                        Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myOrdersPojos.size();
    }

    public class RvViewHolder extends RecyclerView.ViewHolder {
        CardView cvMyOrder;
        TextView tvOrderId, colon1, tvOrderidName, tvOrderon, colon2, tvOrderDate,
                tvDeliverEstimate, tvDeliverDate, tvStatus;
        RecyclerView rvOrderDetail;

        public RvViewHolder(View itemView) {
            super(itemView);
            cvMyOrder = view.findViewById(R.id.cvMyOrder);
            tvOrderId = view.findViewById(R.id.tvOrderId);
            tvOrderidName = view.findViewById(R.id.tvOrderidName);
            colon1 = view.findViewById(R.id.colon1);
            tvOrderon = view.findViewById(R.id.tvOrderon);
            colon2 = view.findViewById(R.id.colon2);
            tvOrderDate = view.findViewById(R.id.tvOrderDate);
            tvDeliverEstimate = view.findViewById(R.id.tvDeliverEstimate);
            tvDeliverDate = view.findViewById(R.id.tvDeliverDate);
            tvStatus = view.findViewById(R.id.tvStatus);
            rvOrderDetail = view.findViewById(R.id.rvOrderDetail);
        }
    }
}