package com.teckzy.msrsilverhouse.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.Adapter.ProductAdapter;
import com.teckzy.msrsilverhouse.Pojo.ProductsPojo;
import com.teckzy.msrsilverhouse.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAllActivity extends Activity {
    APIInterface apiInterface;
    TextView tvToolbar, tvNoProducts;
    ImageView backIcon;
    RecyclerView rvProduct;
    ProductAdapter productAdapter;
    List<ProductsPojo> productPojoList;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        tvToolbar = findViewById(R.id.tvToolbar);
        tvToolbar.setText("Products");
        backIcon = findViewById(R.id.backIcon);
        tvNoProducts = findViewById(R.id.tvNoProducts);
        rvProduct = findViewById(R.id.rvProduct);

        productPojoList = new ArrayList<>();

        progressDialog = new ProgressDialog(ProductAllActivity.this);
        progressDialog.setMessage("Wait while loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        apiGetCategorySubCategoryProducts();

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void apiGetCategorySubCategoryProducts()
    {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<List<ProductsPojo>> call = apiInterface.get_all_products();
        call.enqueue(new Callback<List<ProductsPojo>>() {
            @Override
            public void onResponse(Call<List<ProductsPojo>> call, Response<List<ProductsPojo>> response) {
                if (response.isSuccessful()) {
                    productPojoList = response.body();
                    if (productPojoList.size() == 0) {
                        tvNoProducts.setVisibility(View.VISIBLE);
                    } else {
                        tvNoProducts.setVisibility(View.GONE);
                        generateProduct(productPojoList);
                    }
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductsPojo>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void generateProduct(List<ProductsPojo> productPojoList) {
        productAdapter = new ProductAdapter(getApplicationContext(), productPojoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvProduct.setLayoutManager(mLayoutManager);
        rvProduct.setItemAnimator(new DefaultItemAnimator());
        rvProduct.setAdapter(productAdapter);
    }
}