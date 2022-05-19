package com.teckzy.msrsilverhouse.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.Activity.MyOrdersActivity;
import com.teckzy.msrsilverhouse.Activity.RatingReviewActivity;
import com.teckzy.msrsilverhouse.Pojo.MyordersSubPojo;
import com.teckzy.msrsilverhouse.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {
    APIInterface apiInterface;
    public Context mContext;
    private final List<MyordersSubPojo> orderDetailPojoList;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    JSONArray jsonArray;
    JSONObject jsonObject;
    String cancelReturnStatus;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llRating;
        ImageView ivImage;
        TextView tvProductName, tvQuantity, colon1, tvProductCount, tvWriteAReview, tvOrderPrice, tvCancel, tvCancelled;
        ImageView ivRating1, ivRating2, ivRating3, ivRating4, ivRating5;

        public MyViewHolder(View view) {
            super(view);
            llRating = view.findViewById(R.id.llRating);
            ivImage = view.findViewById(R.id.ivImage);
            tvProductName = view.findViewById(R.id.tvProductName);
            tvQuantity = view.findViewById(R.id.tvQuantity);
            colon1 = view.findViewById(R.id.colon1);
            tvProductCount = view.findViewById(R.id.tvProductCount);
            ivRating1 = view.findViewById(R.id.ivRating1);
            ivRating2 = view.findViewById(R.id.ivRating2);
            ivRating3 = view.findViewById(R.id.ivRating3);
            ivRating4 = view.findViewById(R.id.ivRating4);
            ivRating5 = view.findViewById(R.id.ivRating5);
            tvWriteAReview = view.findViewById(R.id.tvWrite);
            tvOrderPrice = view.findViewById(R.id.tvOrderPrice);
            tvCancel = view.findViewById(R.id.tvCancel);
            tvCancelled = view.findViewById(R.id.tvCancelled);
        }
    }

    public OrderDetailAdapter(Context mContext, List<MyordersSubPojo> orderDetailPojoList) {
        this.mContext = mContext;
        this.orderDetailPojoList = orderDetailPojoList;
    }

    @NonNull
    @Override
    public OrderDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_orderdetails, parent, false);

        return new OrderDetailAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderDetailAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final MyordersSubPojo orderDetailPojo = orderDetailPojoList.get(position);

        sharedPreferences = mContext.getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Glide.with(holder.ivImage)
                .load(APIClient.BASE_URL + orderDetailPojo.getImage())
                .fitCenter()
                .into(holder.ivImage);

        holder.tvProductName.setText(orderDetailPojo.getProduct_name());
        holder.tvProductCount.setText(orderDetailPojo.getQty());
        holder.tvOrderPrice.setText("₹" + " " + orderDetailPojo.getPrice());

        if (orderDetailPojo.getRating().equals("0")) {
            setRating0(holder);
        } else if (orderDetailPojo.getRating().equals("1")) {
            setRating1(holder);
        } else if (orderDetailPojo.getRating().equals("2")) {
            setRating2(holder);
        } else if (orderDetailPojo.getRating().equals("3")) {
            setRating3(holder);
        } else if (orderDetailPojo.getRating().equals("4")) {
            setRating4(holder);
        } else if (orderDetailPojo.getRating().equals("5")) {
            setRating5(holder);
        }

        if (orderDetailPojo.getReview().equals("")) {
            holder.tvWriteAReview.setText("Write Review");
        } else {
            holder.tvWriteAReview.setText("Edit Review");
        }

        if ((orderDetailPojo.getOrderStatus().equals("Placed") || orderDetailPojo.getOrderStatus().equals("Shipped")) && (orderDetailPojo.getStatus().equals("Cancelled")))
        {
            holder.tvCancelled.setText(orderDetailPojo.getStatus());
            holder.tvCancelled.setVisibility(View.VISIBLE);
            holder.tvCancel.setVisibility(View.GONE);
        } else if ((orderDetailPojo.getOrderStatus().equals("Delivered")) && (orderDetailPojo.getStatus().equals("Returned")))
        {
            holder.tvCancelled.setText(orderDetailPojo.getStatus());
            holder.tvCancelled.setVisibility(View.VISIBLE);
            holder.tvCancel.setVisibility(View.GONE);
        }
        else if((orderDetailPojo.getOrderStatus().equals("Placed") || orderDetailPojo.getOrderStatus().equals("Shipped")))
        {
            holder.tvCancel.setText("Cancel");
            holder.tvCancel.setVisibility(View.VISIBLE);
            holder.tvCancelled.setVisibility(View.GONE);
        }
        else if(orderDetailPojo.getOrderStatus().equals("Delivered"))
        {
            holder.tvCancel.setText("Return");
            holder.tvCancel.setVisibility(View.VISIBLE);
            holder.tvCancelled.setVisibility(View.GONE);
        }
        else if(orderDetailPojo.getOrderStatus().equals("Cancelled"))
        {
            holder.tvCancelled.setText(orderDetailPojo.getStatus());
            holder.tvCancelled.setVisibility(View.VISIBLE);
            holder.tvCancel.setVisibility(View.GONE);
        }


        holder.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertCancelReturn(view, orderDetailPojo.getCart_id(), holder.tvCancel.getText().toString());
            }
        });

        holder.ivRating1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRating1(holder);
                apiSendRating(sharedPreferences.getInt("customer_id", 0), orderDetailPojo.getProduct_id(), String.valueOf(1));
                orderDetailPojo.setRating(String.valueOf(1));
                notifyItemChanged(position);
            }
        });

        holder.ivRating2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRating2(holder);
                apiSendRating(sharedPreferences.getInt("customer_id", 0), orderDetailPojo.getProduct_id(), String.valueOf(2));
                orderDetailPojo.setRating(String.valueOf(2));
                notifyItemChanged(position);
            }
        });

        holder.ivRating3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRating3(holder);
                apiSendRating(sharedPreferences.getInt("customer_id", 0), orderDetailPojo.getProduct_id(), String.valueOf(3));
                orderDetailPojo.setRating(String.valueOf(3));
                notifyItemChanged(position);
            }
        });

        holder.ivRating4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRating4(holder);
                apiSendRating(sharedPreferences.getInt("customer_id", 0), orderDetailPojo.getProduct_id(), String.valueOf(4));
                orderDetailPojo.setRating(String.valueOf(4));
                notifyItemChanged(position);
            }
        });

        holder.ivRating5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRating5(holder);
                apiSendRating(sharedPreferences.getInt("customer_id", 0), orderDetailPojo.getProduct_id(), String.valueOf(5));
                orderDetailPojo.setRating(String.valueOf(5));
                notifyItemChanged(position);
            }
        });

        holder.tvWriteAReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RatingReviewActivity.class);
                intent.putExtra("product_id", orderDetailPojo.getProduct_id());
                intent.putExtra("rate", orderDetailPojo.getRating());
                intent.putExtra("review", orderDetailPojo.getReview());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    public void setRating0(@NonNull final OrderDetailAdapter.MyViewHolder holder) {
        holder.ivRating1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.outline_star));
        holder.ivRating2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.outline_star));
        holder.ivRating3.setImageDrawable(mContext.getResources().getDrawable(R.drawable.outline_star));
        holder.ivRating4.setImageDrawable(mContext.getResources().getDrawable(R.drawable.outline_star));
        holder.ivRating5.setImageDrawable(mContext.getResources().getDrawable(R.drawable.outline_star));
    }

    public void setRating1(@NonNull final OrderDetailAdapter.MyViewHolder holder) {
        holder.ivRating1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.filled_star));
        holder.ivRating2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.outline_star));
        holder.ivRating3.setImageDrawable(mContext.getResources().getDrawable(R.drawable.outline_star));
        holder.ivRating4.setImageDrawable(mContext.getResources().getDrawable(R.drawable.outline_star));
        holder.ivRating5.setImageDrawable(mContext.getResources().getDrawable(R.drawable.outline_star));
    }

    public void setRating2(@NonNull final OrderDetailAdapter.MyViewHolder holder) {
        holder.ivRating1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.filled_star));
        holder.ivRating2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.filled_star));
        holder.ivRating3.setImageDrawable(mContext.getResources().getDrawable(R.drawable.outline_star));
        holder.ivRating4.setImageDrawable(mContext.getResources().getDrawable(R.drawable.outline_star));
        holder.ivRating5.setImageDrawable(mContext.getResources().getDrawable(R.drawable.outline_star));
    }

    public void setRating3(@NonNull final OrderDetailAdapter.MyViewHolder holder) {
        holder.ivRating1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.filled_star));
        holder.ivRating2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.filled_star));
        holder.ivRating3.setImageDrawable(mContext.getResources().getDrawable(R.drawable.filled_star));
        holder.ivRating4.setImageDrawable(mContext.getResources().getDrawable(R.drawable.outline_star));
        holder.ivRating5.setImageDrawable(mContext.getResources().getDrawable(R.drawable.outline_star));
    }

    public void setRating4(@NonNull final OrderDetailAdapter.MyViewHolder holder) {
        holder.ivRating1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.filled_star));
        holder.ivRating2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.filled_star));
        holder.ivRating3.setImageDrawable(mContext.getResources().getDrawable(R.drawable.filled_star));
        holder.ivRating4.setImageDrawable(mContext.getResources().getDrawable(R.drawable.filled_star));
        holder.ivRating5.setImageDrawable(mContext.getResources().getDrawable(R.drawable.outline_star));
    }

    public void setRating5(@NonNull final OrderDetailAdapter.MyViewHolder holder) {
        holder.ivRating1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.filled_star));
        holder.ivRating2.setImageDrawable(mContext.getResources().getDrawable(R.drawable.filled_star));
        holder.ivRating3.setImageDrawable(mContext.getResources().getDrawable(R.drawable.filled_star));
        holder.ivRating4.setImageDrawable(mContext.getResources().getDrawable(R.drawable.filled_star));
        holder.ivRating5.setImageDrawable(mContext.getResources().getDrawable(R.drawable.filled_star));
    }

    public void apiSendRating(int customer_id, int product_id, String rating) {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.add_reviews(customer_id, product_id, rating, "");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        jsonArray = new JSONArray(response.body());
                        jsonObject = jsonArray.getJSONObject(0);
                        Toast.makeText(mContext.getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void alertCancelReturn(View view, int cartId, String cancelReturn) {
        LayoutInflater factory = LayoutInflater.from(view.getContext());
        final View alertDialogView = factory.inflate(R.layout.layout_cancel_return, null);
        ImageView ivClose = alertDialogView.findViewById(R.id.ivClose);
        TextView title = alertDialogView.findViewById(R.id.title);
        TextView description = alertDialogView.findViewById(R.id.description);
        Button btnDone = alertDialogView.findViewById(R.id.btnDone);
        if (cancelReturn.equals("Cancel")) {
            title.setText("Cancel Product");
            description.setText("Are you sure you want to cancel the product?");
            btnDone.setText("Yes, Cancel");
            cancelReturnStatus = "Cancelled";
        } else if (cancelReturn.equals("Return")) {
            title.setText("Return Product");
            description.setText("Are you sure you want to return the product?");
            btnDone.setText("Yes, Return");
            cancelReturnStatus = "Returned";
        }
        final android.app.AlertDialog alertDialog = new AlertDialog.Builder(view.getContext(), R.style.CustomAlertDialog).create();

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiCancelReturnOrder(cartId, cancelReturnStatus);
            }
        });

        alertDialog.setView(alertDialogView);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void apiCancelReturnOrder(int cartId, String cancelReturn) {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.change_order_status(cartId, cancelReturn);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        jsonArray = new JSONArray(response.body());
                        jsonObject = jsonArray.getJSONObject(0);
                        Toast.makeText(mContext.getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(mContext, MyOrdersActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderDetailPojoList.size();
    }
}