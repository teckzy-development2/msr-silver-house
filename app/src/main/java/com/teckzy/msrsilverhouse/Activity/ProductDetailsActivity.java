package com.teckzy.msrsilverhouse.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.Adapter.ProductDetailsAdapter;
import com.teckzy.msrsilverhouse.Adapter.ProductSliderAdapter;
import com.teckzy.msrsilverhouse.Pojo.ChildReviewsPojo;
import com.teckzy.msrsilverhouse.Pojo.ProductsPojo;
import com.teckzy.msrsilverhouse.Pojo.SliderPojo;
import com.teckzy.msrsilverhouse.R;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends Activity
{
    TextView tvProductName, tvProductPrice, tvProductDetails, tvReview, tvQuantity;
    ImageView ivIncrease, ivDecrease, ivCart, ivFavourites;
    RecyclerView rvReview;
    SliderView imageSlider;
    ProductSliderAdapter SliderAdapter;
    APIInterface apiInterface;
    ProductDetailsAdapter productDetailsAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button btnCheckout;
    List<ProductsPojo> productsPojoList;
    TextView tvToolbar;
    ImageView backIcon;
    int count, productid;
    DecimalFormat decimalFormat;
    JSONArray jsonArray;
    JSONObject jsonObject;
    String status;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetails);

        imageSlider = findViewById(R.id.imageSlider);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        ivIncrease = findViewById(R.id.ivIncrease);
        tvQuantity = findViewById(R.id.tvQuantity);
        ivDecrease = findViewById(R.id.ivDecrease);
        tvProductDetails = findViewById(R.id.tvProductDetails);
        tvReview = findViewById(R.id.tvReview);
        rvReview = findViewById(R.id.rvReview);
        ivCart = findViewById(R.id.ivCart);
        btnCheckout = findViewById(R.id.btnCheckout);

        tvToolbar = findViewById(R.id.tvToolbar);
        tvToolbar.setText("Product Details");
        backIcon = findViewById(R.id.backIcon);
        ivFavourites = findViewById(R.id.ivFavourites);
        decimalFormat = new DecimalFormat("#,###,###");

        sharedPreferences = getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        progressDialog = new ProgressDialog(ProductDetailsActivity.this);
        progressDialog.setMessage("Wait while loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        productsPojoList = new ArrayList<>();

        productid = getIntent().getIntExtra("productId", 0);

        ivCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (sharedPreferences.getInt("customer_id", 0) == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please Login !!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else if (tvQuantity.getText().length() == 0 || tvQuantity.getText().toString().equals("0"))
                {
                    Toast.makeText(getApplicationContext(), "Please enter valid Quantity", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    apiAddToCart("cart");
                }
            }
        });

        apiProductDetails(productid);

        btnCheckout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (sharedPreferences.getInt("customer_id", 0) == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please Login !!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else if (tvQuantity.getText().length() == 0 || tvQuantity.getText().toString().equals("0"))
                {
                    Toast.makeText(getApplicationContext(), "Please enter valid Quantity", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    apiAddToCart("checkout");
                    Intent intent = new Intent(getApplicationContext(), CheckOutActivity.class);
                    intent.putExtra("type", "checkout");
                    startActivity(intent);
                }
            }
        });

        ivFavourites.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (sharedPreferences.getInt("customer_id", 0) == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please Login !!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else
                {
                    if (productsPojoList.get(0).getWishlist().equals("true"))
                    {
                        ivFavourites.setImageResource(R.drawable.favoriteoutline_new);
                        productsPojoList.get(0).setWishlist("false");
                    }
                    else
                    {
                        ivFavourites.setImageResource(R.drawable.favoritefilled_new);
                        productsPojoList.get(0).setWishlist("true");
                    }
                    apiAddRemoveFavourites(sharedPreferences.getInt("customer_id", 0), productid);
                }
            }
        });

        ivDecrease.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (tvQuantity.getText().length() != 0)
                {
                    count = Integer.parseInt(String.valueOf(tvQuantity.getText()));
                    if (count == 1)
                    {
                        tvQuantity.setText("1");
                    }
                    else
                    {
                        count -= 1;
                        tvQuantity.setText("" + count);
                    }
                }
            }
        });

        ivIncrease.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (tvQuantity.getText().length() == 0)
                {
                    tvQuantity.setText("1");
                }
                else
                {
                    count = Integer.parseInt(String.valueOf(tvQuantity.getText()));
                    count++;
                    tvQuantity.setText("" + count);
                }
            }
        });
    }

    public void apiAddToCart(String type)
    {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.add_to_cart(sharedPreferences.getInt("customer_id", 0), productid, Integer.parseInt(tvQuantity.getText().toString()), type, tvProductPrice.getText().toString());
        call.enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(Call<String> call, Response<String> response)
            {
                if (response.isSuccessful())
                {
                    try
                    {
                        jsonArray = new JSONArray(response.body());
                        jsonObject = jsonArray.getJSONObject(0);
                        editor.putInt("cart_count", jsonObject.getInt("cart_count"));
                        editor.apply();
                        Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
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
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void apiAddRemoveFavourites(int customerId, int productId)
    {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.add_wishlist(customerId, productId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        jsonArray = new JSONArray(response.body());
                        jsonObject = jsonArray.getJSONObject(0);
                        Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void apiProductDetails(int productid)
    {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<List<ProductsPojo>> call = apiInterface.get_product_details(sharedPreferences.getInt("customer_id", 0), productid);
        call.enqueue(new Callback<List<ProductsPojo>>()
        {
            @Override
            public void onResponse(Call<List<ProductsPojo>> call, Response<List<ProductsPojo>> response)
            {
                if (response.isSuccessful())
                {
                    productsPojoList = response.body();

                    tvProductName.setText(productsPojoList.get(0).getProduct_name());
                    tvProductPrice.setText("₹ " + productsPojoList.get(0).getPrice());

                    if (productsPojoList.get(0).getWishlist().equals("true"))
                    {
                        ivFavourites.setImageResource(R.drawable.favoritefilled_new);
                    }
                    else
                    {
                        ivFavourites.setImageResource(R.drawable.favoriteoutline_new);
                    }
                    tvProductDetails.setText(productsPojoList.get(0).getDescription());

                    if (productsPojoList.get(0).getReviews().size() != 0)
                    {
                        tvReview.setVisibility(View.VISIBLE);
                        getFreshProducts(productsPojoList.get(0).getReviews());
                    }
                    else
                    {
                        tvReview.setVisibility(View.GONE);
                    }
                    generateProductImages(productsPojoList.get(0).getSlider());
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

    public void getFreshProducts(List<ChildReviewsPojo> childReviewsPojoList) {
        productDetailsAdapter = new ProductDetailsAdapter(getApplicationContext(), childReviewsPojoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvReview.setLayoutManager(mLayoutManager);
        rvReview.setAdapter(productDetailsAdapter);
        productDetailsAdapter.notifyDataSetChanged();
    }

    public void generateProductImages(List<SliderPojo> sliderPojoList) {
        SliderAdapter = new ProductSliderAdapter(getApplicationContext(), sliderPojoList);
        imageSlider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        imageSlider.setSliderAdapter(SliderAdapter);
    }
}