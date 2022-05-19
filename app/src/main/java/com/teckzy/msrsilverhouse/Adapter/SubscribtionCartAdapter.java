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
import com.teckzy.msrsilverhouse.Pojo.SubscribtionDetailsPojo;
import com.teckzy.msrsilverhouse.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscribtionCartAdapter extends RecyclerView.Adapter<SubscribtionCartAdapter.RvViewHolder> {
    Context context;
    List<SubscribtionDetailsPojo> subscribtionDetailsPojoList;
    PassData passData;
    APIInterface apiInterface;
    JSONArray jsonArray;
    JSONObject jsonObject;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SubscribtionCartAdapter(Context context, List<SubscribtionDetailsPojo> subscribtionDetailsPojoList, PassData passData) {
        this.context = context;
        this.subscribtionDetailsPojoList = subscribtionDetailsPojoList;
        this.passData = passData;
    }

    View view;

    @Override
    public SubscribtionCartAdapter.RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.layout_subscribtioncart, parent, false);
        SubscribtionCartAdapter.RvViewHolder rvViewHolder = new SubscribtionCartAdapter.RvViewHolder(view);
        return rvViewHolder;
    }

    @Override
    public void onBindViewHolder(SubscribtionCartAdapter.RvViewHolder holder, @SuppressLint("RecyclerView") final int position) {
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

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiDeleteSubscribtion(subscribtionDetailsPojo.getSubscribtion_id());
                removeAt(position);
                notifyDataSetChanged();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new CartFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, myFragment).addToBackStack(null).commit();
            }
        });
    }

    public void removeAt(int position) {
        subscribtionDetailsPojoList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, subscribtionDetailsPojoList.size());
        passData.respond(1);
    }

    public void apiDeleteSubscribtion(int subscribtionId) {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.delete_subscribtion(sharedPreferences.getInt("customer_id", 0), subscribtionId);
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