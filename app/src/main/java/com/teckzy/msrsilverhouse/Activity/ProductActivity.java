package com.teckzy.msrsilverhouse.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
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
import com.teckzy.msrsilverhouse.Adapter.HomeProductAdapter;
import com.teckzy.msrsilverhouse.Adapter.SubCategoryAdapter;
import com.teckzy.msrsilverhouse.Interface.PassData;
import com.teckzy.msrsilverhouse.Pojo.HomeProductPojo;
import com.teckzy.msrsilverhouse.Pojo.SubCategoryPojo;
import com.teckzy.msrsilverhouse.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends Activity
{
    APIInterface apiInterface;
    TextView tvToolbar, tvNoProducts;
    ImageView backIcon;
    RecyclerView rvSubCategory,rvProduct;
    HomeProductAdapter productAdapter;
    List<HomeProductPojo> productPojoList;
    int categoryId;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    SubCategoryAdapter subCategoryAdapter;
    PassData passData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        tvToolbar = findViewById(R.id.tvToolbar);
        tvToolbar.setText("Products");
        backIcon = findViewById(R.id.backIcon);
        tvNoProducts = findViewById(R.id.tvNoProducts);
        rvSubCategory = findViewById(R.id.rvSubCategory);
        rvProduct = findViewById(R.id.rvProduct);
        productPojoList = new ArrayList<>();

        sharedPreferences = getApplicationContext().getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        categoryId = getIntent().getIntExtra("category_id",0);

        apiGetSubCategory(categoryId);

        passData = new PassData()
        {
            @Override
            public void respond(int id)
            {
                apiGetProducts(sharedPreferences.getInt("customer_id", 0), categoryId,id);
            }
        };

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void apiGetSubCategory(int categoryId)
    {
        progressDialog = new ProgressDialog(ProductActivity.this);
        progressDialog.setMessage("Wait while loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<List<SubCategoryPojo>> call = apiInterface.getSubCategory(categoryId);
        call.enqueue(new Callback<List<SubCategoryPojo>>()
        {
            @Override
            public void onResponse(Call<List<SubCategoryPojo>> call, Response<List<SubCategoryPojo>> response)
            {
                if (response.isSuccessful())
                {
                    generateSubCategory(response.body());
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<SubCategoryPojo>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void apiGetProducts(int customerId,int catId,int subCatId)
    {
        progressDialog = new ProgressDialog(ProductActivity.this);
        progressDialog.setMessage("Wait while loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<List<HomeProductPojo>> call = apiInterface.get_product(customerId, catId, subCatId);
        call.enqueue(new Callback<List<HomeProductPojo>>()
        {
            @Override
            public void onResponse(Call<List<HomeProductPojo>> call, Response<List<HomeProductPojo>> response)
            {
                if (response.isSuccessful())
                {
                    productPojoList = response.body();

                    if (productPojoList.size() == 0)
                    {
                        tvNoProducts.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        tvNoProducts.setVisibility(View.GONE);
                        generateProduct(productPojoList);
                    }
                    progressDialog.dismiss();
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<HomeProductPojo>> call, Throwable t)
            {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void generateSubCategory(List<SubCategoryPojo> subCategoryPojoList)
    {
        subCategoryAdapter = new SubCategoryAdapter(getApplicationContext(), subCategoryPojoList, passData);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, false);
        rvSubCategory.setLayoutManager(manager);
        rvSubCategory.setAdapter(subCategoryAdapter);
    }

    public void generateProduct(List<HomeProductPojo> productPojoList)
    {
        productAdapter = new HomeProductAdapter(getApplicationContext(), productPojoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvProduct.setLayoutManager(mLayoutManager);
        rvProduct.setItemAnimator(new DefaultItemAnimator());
        rvProduct.setAdapter(productAdapter);
    }
}