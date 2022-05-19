package com.teckzy.msrsilverhouse.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.Adapter.MyAddressAdapter;
import com.teckzy.msrsilverhouse.Interface.PassData;
import com.teckzy.msrsilverhouse.Pojo.AddressPojo;
import com.teckzy.msrsilverhouse.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAddressActivity extends AppCompatActivity {
    Button btnAddNewAddress;
    RecyclerView rvAddress;
    TextView tvNoAddressFound;
    TextView tvToolbar;
    ImageView backIcon;
    APIInterface apiInterface;
    List<AddressPojo> addressPojoList;
    MyAddressAdapter addressAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    PassData passData;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaddress);
        tvNoAddressFound = findViewById(R.id.tvNoAddressFound);
        rvAddress = findViewById(R.id.rvAddress);
        btnAddNewAddress = findViewById(R.id.btnAddNewAddress);
        tvToolbar = findViewById(R.id.tvToolbar);
        tvToolbar.setText("My Address");
        backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        addressPojoList = new ArrayList<>();

        type = getIntent().getStringExtra("type");

        btnAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNewAddressActivity.class);
                intent.putExtra("action", "add");
                startActivityForResult(intent, 1);
            }
        });

        apiGetAddress();

        passData = new PassData() {
            @Override
            public void respond(int id) {
                if (id == 0) {
                    tvNoAddressFound.setVisibility(View.VISIBLE);
                } else {
                    tvNoAddressFound.setVisibility(View.GONE);
                }
            }
        };
    }

    public void apiGetAddress() {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<List<AddressPojo>> call = apiInterface.my_address(sharedPreferences.getInt("customer_id", 0));
        call.enqueue(new Callback<List<AddressPojo>>() {
            @Override
            public void onResponse(Call<List<AddressPojo>> call, Response<List<AddressPojo>> response) {
                if (response.isSuccessful()) {
                    addressPojoList = response.body();

                    if (addressPojoList.size() == 0) {
                        tvNoAddressFound.setVisibility(View.VISIBLE);
                        rvAddress.setVisibility(View.GONE);
                    } else {
                        tvNoAddressFound.setVisibility(View.GONE);
                        rvAddress.setVisibility(View.VISIBLE);
                        generateAddress(addressPojoList);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AddressPojo>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void generateAddress(List<AddressPojo> addressPojoList) {
        addressAdapter = new MyAddressAdapter(MyAddressActivity.this, addressPojoList, passData);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        rvAddress.setLayoutManager(mLayoutManager);
        rvAddress.setItemAnimator(new DefaultItemAnimator());
        rvAddress.setAdapter(addressAdapter);
    }

    public void startActivityFromAdapter(int address_id, String name, String mobile, String doorNo, String street, String city, String district, String pincode, String state, String country, String landmark, String address_type) {
        Intent intent = new Intent(getApplicationContext(), AddNewAddressActivity.class);
        intent.putExtra("action", "edit");
        intent.putExtra("address_id", address_id);
        intent.putExtra("name", name);
        intent.putExtra("mobile", mobile);
        intent.putExtra("door_no", doorNo);
        intent.putExtra("street", street);
        intent.putExtra("country", country);
        intent.putExtra("state", state);
        intent.putExtra("district", district);
        intent.putExtra("landmark", landmark);
        intent.putExtra("city", city);
        intent.putExtra("pincode", pincode);
        intent.putExtra("address_type", address_type);

        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 || requestCode == 2) {
            apiGetAddress();
        }
    }
}