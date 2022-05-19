package com.teckzy.msrsilverhouse.Activity;

import android.app.ProgressDialog;
import android.icu.math.BigDecimal;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.Adapter.OrderDetailAdapter;
import com.teckzy.msrsilverhouse.Pojo.MyordersSubPojo;
import com.teckzy.msrsilverhouse.Pojo.OrderDetailPojo;
import com.teckzy.msrsilverhouse.R;

import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity {
    APIInterface apiInterface;
    OrderDetailAdapter myOrderDetailAdapter;
    TextView tvToolbar, tvOrderId, tvOrderDate, tvDeliverEstimate, tvDeliverDate, tvName, tvDoorNo,
            tvStreetName, tvCity, tvPin, tvState, tvLandmark, tvPhoneNumber,
            tvSubTotal, tvOrderIdName, colon1, tvOrderDateList, colon2, tvStatus, tvOrderStatus, tvDeliverAddress,
            colon3, colon4, colon5, colon6, tvContact, tvPriceDetails, tvTotal;
    ImageView backIcon;
    String orderId;
    RecyclerView rvOrderDetail;
    Format format;
    List<MyordersSubPojo> orderDetailPojoList;
    ProgressDialog progressDialog;
    MyordersSubPojo myordersSubPojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);

        tvToolbar = findViewById(R.id.tvToolbar);
        tvToolbar.setText("Order Detail");
        backIcon = findViewById(R.id.backIcon);

        tvOrderIdName = findViewById(R.id.tvOrderIdName);
        colon1 = findViewById(R.id.colon1);
        tvOrderDateList = findViewById(R.id.tvOrderDateList);
        colon2 = findViewById(R.id.colon2);
        tvStatus = findViewById(R.id.tvStatus);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvDeliverAddress = findViewById(R.id.tvDeliverAddress);
        colon3 = findViewById(R.id.colon3);
        colon4 = findViewById(R.id.colon4);
        colon5 = findViewById(R.id.colon5);
        colon6 = findViewById(R.id.colon6);
        tvContact = findViewById(R.id.tvContact);
        tvPriceDetails = findViewById(R.id.tvPriceDetails);
        tvTotal = findViewById(R.id.tvTotal);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvDeliverEstimate = findViewById(R.id.tvDeliverEstimate);
        tvDeliverDate = findViewById(R.id.tvDeliverDate);
        tvName = findViewById(R.id.tvName);
        tvDoorNo = findViewById(R.id.tvDoorNo);
        tvStreetName = findViewById(R.id.tvStreetName);
        tvCity = findViewById(R.id.tvCity);
        tvPin = findViewById(R.id.tvPin);
        tvState = findViewById(R.id.tvState);
        tvLandmark = findViewById(R.id.tvLandmark);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        rvOrderDetail = findViewById(R.id.rvOrderDetail);
        tvSubTotal = findViewById(R.id.tvSubTotal);

        orderDetailPojoList = new ArrayList<>();

        orderId = getIntent().getStringExtra("order_id");

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        progressDialog = new ProgressDialog(OrderDetailsActivity.this);
        progressDialog.setMessage("Wait while loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        apiGetOrderDetail(orderId);
    }

    public void apiGetOrderDetail(String orderId) {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<List<OrderDetailPojo>> call = apiInterface.my_order_details(orderId);
        call.enqueue(new Callback<List<OrderDetailPojo>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<OrderDetailPojo>> call, Response<List<OrderDetailPojo>> response) {
                if (response.isSuccessful()) {

                        tvOrderId.setText(response.body().get(0).getUnique_id());
                        tvOrderDate.setText(response.body().get(0).getDate_time());
                        tvDeliverDate.setText(response.body().get(0).getEstimated_delivery());
                        tvOrderStatus.setText(response.body().get(0).getOrder_status());

                        if (response.body().get(0).getOrder_status().equals("Delivered")) {

                        } else if ((response.body().get(0).getOrder_status().equals("Placed") || response.body().get(0).getOrder_status().equals("Shipped"))) {

                        }

                        tvName.setText(response.body().get(0).getName());
                        tvDoorNo.setText(response.body().get(0).getDoor_no());
                        tvStreetName.setText(response.body().get(0).getStreet());
                        tvCity.setText(response.body().get(0).getCity());
                        tvPin.setText(response.body().get(0).getPincode());
                        tvState.setText(response.body().get(0).getState());
                        tvLandmark.setText(response.body().get(0).getLandmark());
                        tvPhoneNumber.setText(response.body().get(0).getMobile());
                        format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
                        tvSubTotal.setText(format.format(new BigDecimal(response.body().get(0).getTotal_payable())));

                        orderDetailPojoList = response.body().get(0).getProducts();

//                        myordersSubPojo = new MyordersSubPojo();
//
//                        for (int i = 0;i<orderDetailPojoList.size();i++)
//                        {
//                            myordersSubPojo.setOrderStatus(response.body().get(i).getOrder_status());
//                            orderDetailPojoList.add(myordersSubPojo);
//                        }

                        generateOrderDetail(orderDetailPojoList);
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderDetailPojo>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void generateOrderDetail(List<MyordersSubPojo> orderDetailPojoList) {
        myOrderDetailAdapter = new OrderDetailAdapter(getApplicationContext(), orderDetailPojoList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        rvOrderDetail.setLayoutManager(mLayoutManager);
        rvOrderDetail.setItemAnimator(new DefaultItemAnimator());
        rvOrderDetail.setAdapter(myOrderDetailAdapter);
    }
}