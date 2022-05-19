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
import com.teckzy.msrsilverhouse.Adapter.MyOrdersAdapter;
import com.teckzy.msrsilverhouse.Pojo.MyOrdersPojo;
import com.teckzy.msrsilverhouse.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrdersActivity extends AppCompatActivity {
    RecyclerView rvMyOrder;
    List<MyOrdersPojo> myOrdersPojoList;
    MyOrdersAdapter myOrdersAdapter;
    TextView tvToolbar, tvMyorderFound;
    ImageView backIcon;
    APIInterface apiInterface;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);
        rvMyOrder = findViewById(R.id.rvMyOrder);
        tvMyorderFound = findViewById(R.id.tvMyorderFound);

        tvToolbar = findViewById(R.id.tvToolbar);
        tvToolbar.setText("My Order");
        backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        myOrdersPojoList = new ArrayList<>();

        sharedPreferences = getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        progressDialog = new ProgressDialog(MyOrdersActivity.this);
        progressDialog.setMessage("Wait while loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        apiMyOrders(sharedPreferences.getInt("customer_id", 0));
    }

    public void apiMyOrders(int customer_id) {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<List<MyOrdersPojo>> call = apiInterface.my_orders(customer_id);
        call.enqueue(new Callback<List<MyOrdersPojo>>() {
            @Override
            public void onResponse(Call<List<MyOrdersPojo>> call, Response<List<MyOrdersPojo>> response) {
                if (response.isSuccessful()) {
                    myOrdersPojoList = response.body();
                    if (myOrdersPojoList.get(0).getProducts().size()==0) {
                        tvMyorderFound.setVisibility(View.VISIBLE);
                        rvMyOrder.setVisibility(View.GONE);
                    } else {
                        tvMyorderFound.setVisibility(View.GONE);
                        rvMyOrder.setVisibility(View.VISIBLE);
                        getMyorder(myOrdersPojoList);
                    }
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MyOrdersPojo>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getMyorder(List<MyOrdersPojo> myOrdersPojoList)
    {
        myOrdersAdapter = new MyOrdersAdapter(getApplicationContext(), myOrdersPojoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvMyOrder.setLayoutManager(mLayoutManager);
        rvMyOrder.setAdapter(myOrdersAdapter);
        myOrdersAdapter.notifyDataSetChanged();
    }
}