package com.teckzy.msrsilverhouse.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.Adapter.HomeProductAdapter;
import com.teckzy.msrsilverhouse.Pojo.HomeProductPojo;
import com.teckzy.msrsilverhouse.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishlistActivity extends AppCompatActivity {
    RecyclerView rvWishlist;
    TextView tvNoFavourites;
    List<HomeProductPojo> wishlistPojoList;
    HomeProductAdapter wishlistAdapter;
    TextView tvToolbar;
    ImageView backIcon;
    APIInterface apiInterface;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        tvNoFavourites = findViewById(R.id.tvNoFavourites);
        rvWishlist = findViewById(R.id.rvWishlist);
        tvToolbar = findViewById(R.id.tvToolbar);
        tvToolbar.setText("Wishlist");
        backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        progressDialog = new ProgressDialog(WishlistActivity.this);
        progressDialog.setMessage("Wait while loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        wishlistPojoList = new ArrayList<>();

        apiWishlist(sharedPreferences.getInt("customer_id", 0));
    }

    private void apiWishlist(int customer_id) {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<List<HomeProductPojo>> call = apiInterface.get_wishlist(customer_id);
        call.enqueue(new Callback<List<HomeProductPojo>>() {
            @Override
            public void onResponse(Call<List<HomeProductPojo>> call, Response<List<HomeProductPojo>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() == 0) {
                        tvNoFavourites.setVisibility(View.VISIBLE);
                    } else {
                        tvNoFavourites.setVisibility(View.GONE);
                        getWishlist(response.body());
                    }
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<HomeProductPojo>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getWishlist(List<HomeProductPojo> wishlistPojoList) {
        wishlistAdapter = new HomeProductAdapter(getApplicationContext(), wishlistPojoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvWishlist.setLayoutManager(mLayoutManager);
        rvWishlist.setAdapter(wishlistAdapter);
        wishlistAdapter.notifyDataSetChanged();
    }
}