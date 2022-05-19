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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.BottomFragment.CartFragment;
import com.teckzy.msrsilverhouse.Interface.PassData;
import com.teckzy.msrsilverhouse.Pojo.ChildCartPojo;
import com.teckzy.msrsilverhouse.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.RvViewHolder> {
    Context context;
    List<ChildCartPojo> childCartPojoList;
    PassData passData;
    APIInterface apiInterface;
    JSONArray jsonArray;
    JSONObject jsonObject;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public CartAdapter(Context context, List<ChildCartPojo> childCartPojoList, PassData passData) {
        this.context = context;
        this.childCartPojoList = childCartPojoList;
        this.passData = passData;
    }

    View view;

    @Override
    public CartAdapter.RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.layout_cart, parent, false);
        CartAdapter.RvViewHolder rvViewHolder = new CartAdapter.RvViewHolder(view);
        return rvViewHolder;
    }

    @Override
    public void onBindViewHolder(CartAdapter.RvViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ChildCartPojo childCartPojo = childCartPojoList.get(position);

        sharedPreferences = context.getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Glide.with(holder.ivCart)
                .load(APIClient.BASE_URL + childCartPojo.getImage())
                .fitCenter()
                .into(holder.ivCart);

        holder.tvCartProductName.setText(childCartPojo.getProduct_name());
        holder.tvAmount.setText("₹ " + childCartPojo.getPrice());
        holder.tvQuantity.setText(String.valueOf(childCartPojo.getQty()));

        holder.ivIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(String.valueOf(holder.tvQuantity.getText()));
                count++;
                holder.tvQuantity.setText("" + count);
                apiAdjustQuantity(childCartPojo.getCart_id(), count);
                childCartPojo.setQty(count);
                notifyDataSetChanged();
            }
        });

        holder.ivDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(String.valueOf(holder.tvQuantity.getText()));

                if (count == 1) {
                    holder.tvQuantity.setText("1");
                    apiAdjustQuantity(childCartPojo.getCart_id(), count);
                    childCartPojo.setQty(count);
                    notifyDataSetChanged();
                } else {
                    count -= 1;
                    holder.tvQuantity.setText("" + count);
                    apiAdjustQuantity(childCartPojo.getCart_id(), count);
                    childCartPojo.setQty(count);
                    notifyDataSetChanged();
                }
            }
        });

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiDeleteCart(childCartPojo.getCart_id());
                removeAt(position);
                notifyDataSetChanged();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new CartFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, myFragment).addToBackStack(null).commit();
            }
        });

        int amount = Integer.parseInt(childCartPojo.getPrice());
        int quantity = childCartPojo.getQty();

        int price = amount * quantity;

        holder.tvPrice.setText("₹ " + String.valueOf(price));
    }

    public void removeAt(int position) {
        childCartPojoList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, childCartPojoList.size());
        passData.respond(1);
    }

    public void apiAdjustQuantity(int cartId, int qty) {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.update_qty(sharedPreferences.getInt("customer_id", 0),cartId, qty);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    passData.respond(1);
                } else {
                    Toast.makeText(context.getApplicationContext(), "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void apiDeleteCart(int cartId) {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.delete_cart(cartId, sharedPreferences.getInt("customer_id", 0));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        jsonArray = new JSONArray(response.body());
                        jsonObject = jsonArray.getJSONObject(0);
                        editor.putInt("cart_count", jsonObject.getInt("cart_count"));
                        editor.apply();
                        Toast.makeText(context.getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context.getApplicationContext(), "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return childCartPojoList.size();
    }

    public class RvViewHolder extends RecyclerView.ViewHolder {
        TextView tvCartProductName, tvAmount, tvQuantity, tvTotal, tvPrice;
        ImageView ivCart, deleteIcon, ivDecrease, ivIncrease;

        public RvViewHolder(View itemView) {
            super(itemView);
            tvCartProductName = itemView.findViewById(R.id.tvCartProductName);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            ivCart = itemView.findViewById(R.id.ivCart);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
            ivDecrease = itemView.findViewById(R.id.ivDecrease);
            ivIncrease = itemView.findViewById(R.id.ivIncrease);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}