package com.teckzy.msrsilverhouse.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.Adapter.CartAdapter;
import com.teckzy.msrsilverhouse.Adapter.SubscribtionCartAdapter;
import com.teckzy.msrsilverhouse.Interface.PassData;
import com.teckzy.msrsilverhouse.Pojo.CartPojo;
import com.teckzy.msrsilverhouse.Pojo.ChildCartPojo;
import com.teckzy.msrsilverhouse.Pojo.SubscribtionCartPojo;
import com.teckzy.msrsilverhouse.Pojo.SubscribtionDetailsPojo;
import com.teckzy.msrsilverhouse.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends Activity {
    APIInterface apiInterface;
    TextView tvTotal, tvTotalPrice, tvEmptyCart;
    RecyclerView rvCart, rvSubscribtion;
    Button btnProceedToCheckout;
    CartAdapter cartAdapter;
    List<CartPojo> cartPojoList;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    LinearLayout linearLayout;
    PassData passData;
    int total_pay;
    String cartTotal = "0", subscribtionTotal = "0";
    List<SubscribtionCartPojo> subscribtionCartPojoList;
    SubscribtionCartAdapter subscribtionCartAdapter;
    NestedScrollView nestedScrollView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cart);
        rvCart = findViewById(R.id.rvCart);
        rvSubscribtion = findViewById(R.id.rvSubscribtion);
        tvTotal = findViewById(R.id.tvTotal);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        btnProceedToCheckout = findViewById(R.id.btnProceedToCheckout);
        linearLayout = findViewById(R.id.linearLayout);
        nestedScrollView = findViewById(R.id.nestedScrollView);

        cartPojoList = new ArrayList<>();
        subscribtionCartPojoList = new ArrayList<>();

        sharedPreferences = getApplicationContext().getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Wait while loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        apiGetCart();

        btnProceedToCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CheckOutActivity.class);
                intent.putExtra("type", "cart");
                intent.putExtra("total_pay", total_pay);
                startActivity(intent);
            }
        });

        passData = new PassData() {
            @Override
            public void respond(int id) {
                apiGetCart();
            }
        };
    }

    public void apiGetCart()
    {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<List<CartPojo>> call = apiInterface.get_cart(sharedPreferences.getInt("customer_id", 0));
        call.enqueue(new Callback<List<CartPojo>>()
        {
            @Override
            public void onResponse(Call<List<CartPojo>> call, Response<List<CartPojo>> response)
            {
                if (response.isSuccessful())
                {
                    cartPojoList = response.body();

                    if (cartPojoList.size() != 0)
                    {
                        generateCartItems(cartPojoList.get(0).getItems());
                        tvEmptyCart.setVisibility(View.GONE);
                    }
                    else
                    {
                        tvEmptyCart.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<CartPojo>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sum(String cartTotal, String subscribtionTotal) {
        int x = Integer.parseInt(cartTotal);
        int y = Integer.parseInt(subscribtionTotal);

        int z = x + y;

        tvTotalPrice.setText(String.valueOf(z));
    }

    public void generateCartItems(List<ChildCartPojo> cartItemsPojoList) {
        cartAdapter = new CartAdapter(getApplicationContext(), cartItemsPojoList, passData);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        rvCart.setLayoutManager(mLayoutManager);
        rvCart.setItemAnimator(new DefaultItemAnimator());
        rvCart.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
    }

    public void generateSubscribtionItems(List<SubscribtionDetailsPojo> subscribtionDetailsPojoList) {
        subscribtionCartAdapter = new SubscribtionCartAdapter(getApplicationContext(), subscribtionDetailsPojoList, passData);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        rvSubscribtion.setLayoutManager(mLayoutManager);
        rvSubscribtion.setItemAnimator(new DefaultItemAnimator());
        rvSubscribtion.setAdapter(subscribtionCartAdapter);
        subscribtionCartAdapter.notifyDataSetChanged();
    }
}