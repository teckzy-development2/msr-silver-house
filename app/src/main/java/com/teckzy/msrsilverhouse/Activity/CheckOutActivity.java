package com.teckzy.msrsilverhouse.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.Adapter.CheckoutAdapter;
import com.teckzy.msrsilverhouse.Adapter.CheckoutSubscribtionAdapter;
import com.teckzy.msrsilverhouse.Interface.PassData;
import com.teckzy.msrsilverhouse.Pojo.CheckoutPojo;
import com.teckzy.msrsilverhouse.Pojo.ChildCartPojo;
import com.teckzy.msrsilverhouse.Pojo.SubscribtionDetailsPojo;
import com.teckzy.msrsilverhouse.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutActivity extends AppCompatActivity {
    Button btnChangeAddress, btnPay;
    CardView cvAddress, cardview;
    TextView tvShipping, tvName, tvPrice, tvCartValue, tvCartValuePrice, tvDiscount, tvApplyCoupon,
            tvTaxes, tvTaxesPrice, tvDeliveryCharges, tvDeliveryChargesPrice, tvTotalAmount, tvTotalAmountPrice;
    TextView tvDoorNo, tvStreetName, tvCity, tvDistrict, tvPin, tvState, tvCountry;
    RecyclerView rvCheckout, rvSubscribtion;
    CheckoutAdapter checkoutAdapter;
    CheckoutSubscribtionAdapter subscribtionCartAdapter;
    List<ChildCartPojo> cartPojoList;
    TextView tvToolbar;
    ImageView backIcon;
    APIInterface apiInterface;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    List<CheckoutPojo> checkoutPojos;
    List<SubscribtionDetailsPojo> subscribtionDetailsPojoList;
    String type;
    int customerAddressId = 0;
    PassData passData;
    RelativeLayout relativeLayout;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        tvShipping = findViewById(R.id.tvShipping);
        tvName = findViewById(R.id.tvName);
        tvDoorNo = findViewById(R.id.tvDoorNo);
        tvStreetName = findViewById(R.id.tvStreetName);
        tvCity = findViewById(R.id.tvCity);
        tvDistrict = findViewById(R.id.tvDistrict);
        tvPin = findViewById(R.id.tvPin);
        tvState = findViewById(R.id.tvState);
        tvCountry = findViewById(R.id.tvCountry);
        relativeLayout = findViewById(R.id.relativeLayout);
        cardview = findViewById(R.id.cardview);

        btnChangeAddress = findViewById(R.id.btnChangeAddress);
        cvAddress = findViewById(R.id.cvAddress);
        rvCheckout = findViewById(R.id.rvCheckout);
        rvSubscribtion = findViewById(R.id.rvSubscribtion);
        tvPrice = findViewById(R.id.tvPrice);
        tvCartValue = findViewById(R.id.tvCartValue);
        tvCartValuePrice = findViewById(R.id.tvCartValuePrice);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvApplyCoupon = findViewById(R.id.tvApplyCoupon);
        tvTaxes = findViewById(R.id.tvTaxes);
        tvTaxesPrice = findViewById(R.id.tvTaxesPrice);
        tvDeliveryCharges = findViewById(R.id.tvDeliveryCharges);
        tvDeliveryChargesPrice = findViewById(R.id.tvDeliveryChargesPrice);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvTotalAmountPrice = findViewById(R.id.tvTotalAmountPrice);
        btnPay = findViewById(R.id.btnPay);

        tvToolbar = findViewById(R.id.tvToolbar);
        tvToolbar.setText("Check Out");
        backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        progressDialog = new ProgressDialog(CheckOutActivity.this);
        progressDialog.setMessage("Wait while loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        type = getIntent().getStringExtra("type");

        cartPojoList = new ArrayList<>();
        checkoutPojos = new ArrayList<>();
        subscribtionDetailsPojoList = new ArrayList<>();

        if (type.equals("cart")) {
            apiCheckout(sharedPreferences.getInt("customer_id", 0), 0);
        } else if (type.equals("checkout")) {
            apiCheckout(sharedPreferences.getInt("customer_id", 0), 0);
        }

        btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyAddressActivity.class);
                intent.putExtra("type", "checkout");
                startActivityForResult(intent, 3);
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customerAddressId == 0) {
                    Toast.makeText(getApplicationContext(), "Please add address !!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                    intent.putExtra("address_id", customerAddressId);
                    intent.putExtra("total_pay", Integer.valueOf(tvTotalAmountPrice.getText().toString()));
                    intent.putExtra("type", type);
                    startActivity(intent);
                }
            }
        });

        passData = new PassData() {
            @Override
            public void respond(int id) {

            }
        };
    }

    public void apiCheckout(int customer_id, int addressId) {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<List<CheckoutPojo>> call = apiInterface.checkout(customer_id, addressId, type);
        call.enqueue(new Callback<List<CheckoutPojo>>() {
            @Override
            public void onResponse(Call<List<CheckoutPojo>> call, Response<List<CheckoutPojo>> response) {
                if (response.isSuccessful()) {
                    checkoutPojos = response.body();

                    if (checkoutPojos.get(0).getAddress().size() == 0) {
                        cvAddress.setVisibility(View.GONE);
                        getCheckout(checkoutPojos.get(0).getItems());
                        getSubscribtion(checkoutPojos.get(0).getSubscribtion());
                        tvCartValuePrice.setText(checkoutPojos.get(0).getCart_value());
                        tvApplyCoupon.setText(checkoutPojos.get(0).getDiscount());
                        tvTaxesPrice.setText(checkoutPojos.get(0).getTaxes());
                        tvDeliveryChargesPrice.setText(checkoutPojos.get(0).getDelivery_charges());
                        tvTotalAmountPrice.setText(checkoutPojos.get(0).getTotal_pay());
                    } else {
                        cvAddress.setVisibility(View.VISIBLE);
                        customerAddressId = checkoutPojos.get(0).getAddress().get(0).getAddress_id();
                        tvName.setText(checkoutPojos.get(0).getAddress().get(0).getName());
                        tvDoorNo.setText(checkoutPojos.get(0).getAddress().get(0).getDoor_no());
                        tvStreetName.setText(checkoutPojos.get(0).getAddress().get(0).getStreet());
                        tvCity.setText(checkoutPojos.get(0).getAddress().get(0).getCity());
                        tvDistrict.setText(checkoutPojos.get(0).getAddress().get(0).getDistrict());
                        tvPin.setText(checkoutPojos.get(0).getAddress().get(0).getPincode());
                        tvState.setText(checkoutPojos.get(0).getAddress().get(0).getState());
                        tvCountry.setText(checkoutPojos.get(0).getAddress().get(0).getCountry());

                        customerAddressId = checkoutPojos.get(0).getAddress().get(0).getAddress_id();
                        tvName.setText(checkoutPojos.get(0).getAddress().get(0).getName());
                        tvDoorNo.setText(checkoutPojos.get(0).getAddress().get(0).getDoor_no());
                        tvStreetName.setText(checkoutPojos.get(0).getAddress().get(0).getStreet());
                        tvCity.setText(checkoutPojos.get(0).getAddress().get(0).getCity());
                        tvDistrict.setText(checkoutPojos.get(0).getAddress().get(0).getDistrict());
                        tvPin.setText(checkoutPojos.get(0).getAddress().get(0).getPincode());
                        tvState.setText(checkoutPojos.get(0).getAddress().get(0).getState());
                        tvCountry.setText(checkoutPojos.get(0).getAddress().get(0).getCountry());

                        getCheckout(checkoutPojos.get(0).getItems());
                        getSubscribtion(checkoutPojos.get(0).getSubscribtion());

                        tvCartValuePrice.setText(checkoutPojos.get(0).getCart_value());
                        tvApplyCoupon.setText(checkoutPojos.get(0).getDiscount());
                        tvTaxesPrice.setText(checkoutPojos.get(0).getTaxes());
                        tvDeliveryChargesPrice.setText(checkoutPojos.get(0).getDelivery_charges());
                        tvTotalAmountPrice.setText(checkoutPojos.get(0).getTotal_pay());
                    }
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CheckoutPojo>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCheckout(List<ChildCartPojo> childCartPojoList) {
        checkoutAdapter = new CheckoutAdapter(getApplicationContext(), childCartPojoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvCheckout.setLayoutManager(mLayoutManager);
        rvCheckout.setAdapter(checkoutAdapter);
        checkoutAdapter.notifyDataSetChanged();
    }

    public void getSubscribtion(List<SubscribtionDetailsPojo> subscribtionDetailsPojoList) {
        subscribtionCartAdapter = new CheckoutSubscribtionAdapter(getApplicationContext(), subscribtionDetailsPojoList, passData);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvSubscribtion.setLayoutManager(mLayoutManager);
        rvSubscribtion.setAdapter(subscribtionCartAdapter);
        subscribtionCartAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            data = new Intent();
        }

        if (requestCode == 3) {
            customerAddressId = data.getIntExtra("address_id", 0);
            apiCheckout(sharedPreferences.getInt("customer_id", 0), data.getIntExtra("address_id", 0));
        }
    }
}