package com.teckzy.msrsilverhouse.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.Activity.LoginActivity;
import com.teckzy.msrsilverhouse.Activity.ProductDetailsActivity;
import com.teckzy.msrsilverhouse.Pojo.HomeProductPojo;
import com.teckzy.msrsilverhouse.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.RvViewHolder> {
    Context context;
    List<HomeProductPojo> homeProductPojos;
    APIInterface apiInterface;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String status = "kg";

    public HomeProductAdapter(Context context, List<HomeProductPojo> homeProductPojos) {
        this.context = context;
        this.homeProductPojos = homeProductPojos;
    }

    View view;

    @Override
    public HomeProductAdapter.RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.layout_home_products, parent, false);
        HomeProductAdapter.RvViewHolder rvViewHolder = new HomeProductAdapter.RvViewHolder(view);
        return rvViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(HomeProductAdapter.RvViewHolder holder, final int position)
    {
        HomeProductPojo homeProductPojo = homeProductPojos.get(position);

        sharedPreferences = context.getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (homeProductPojo.getWishlist().equals("true")) {
            holder.ivFavourite.setImageResource(R.drawable.favoritefilled_new);
        } else {
            holder.ivFavourite.setImageResource(R.drawable.favoriteoutline_new);
        }

        Glide.with(holder.fruitsimg)
                .load(APIClient.BASE_URL + homeProductPojo.getImage())
                .fitCenter()
                .into(holder.fruitsimg);

        holder.productnametv.setText(homeProductPojo.getProduct_name());
        holder.pricetv.setText("??? " + homeProductPojo.getPrice());
        holder.cvCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("productId", homeProductPojo.getProduct_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.cartimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getInt("customer_id", 0) == 0) {
                    Toast.makeText(context, "Please Login !!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    apiAddToCart(sharedPreferences.getInt("customer_id", 0), homeProductPojo.getProduct_id(), 0, "cart", homeProductPojo.getPrice());
                }
            }
        });

        holder.ivFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getInt("customer_id", 0) == 0) {
                    Toast.makeText(context, "Please Login !!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    if (homeProductPojo.getWishlist().equals("true")) {
                        holder.ivFavourite.setImageResource(R.drawable.favoriteoutline_new);
                        homeProductPojo.setWishlist("false");
                    } else {
                        holder.ivFavourite.setImageResource(R.drawable.favoritefilled_new);
                        homeProductPojo.setWishlist("true");
                    }
                    apiAddRemoveFavourites(sharedPreferences.getInt("customer_id", 0), homeProductPojo.getProduct_id());
                    notifyDataSetChanged();
                }
            }
        });
    }

    public void apiAddToCart(int customer_id, int productId, int quantity, String type, String price)
    {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.add_to_cart(customer_id, productId, quantity, type, price);
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

    public void apiAddRemoveFavourites(int customer_id, int productId) {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.add_wishlist(customer_id, productId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        jsonArray = new JSONArray(response.body());
                        jsonObject = jsonArray.getJSONObject(0);
                        Toast.makeText(context.getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
        return homeProductPojos.size();
    }

    public class RvViewHolder extends RecyclerView.ViewHolder {
        TextView productnametv, pricetv;
        ImageView fruitsimg, cartimg, ivFavourite;
        CardView cvCart;

        public RvViewHolder(View itemView) {
            super(itemView);
            productnametv = itemView.findViewById(R.id.productnametv);
            pricetv = itemView.findViewById(R.id.pricetv);
            fruitsimg = itemView.findViewById(R.id.fruitsimg);
            cartimg = itemView.findViewById(R.id.cartimg);
            cvCart = itemView.findViewById(R.id.cvCart);
            ivFavourite = itemView.findViewById(R.id.ivFavourite);
        }
    }
}