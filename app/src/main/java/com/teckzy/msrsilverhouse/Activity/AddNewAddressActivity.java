package com.teckzy.msrsilverhouse.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewAddressActivity extends AppCompatActivity {
    Button btnSubmit;
    TextView tvToolbar;
    ImageView backIcon;
    EditText tetFullName, tetMobileNumber, tetPincode, tetState, tetAddressDoorno, tetAddressStreet, tetLandmark, tetCity, tetDistrict, tetCountry;
    TextView tvTypesOfAddress;
    RadioButton radioHome, radioOffice;
    RadioGroup radioGroup;
    APIInterface apiInterface;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String action, addresstype;
    int addressId;
    JSONArray jsonArray;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewaddress);
        tetFullName = findViewById(R.id.tetFullName);
        tetMobileNumber = findViewById(R.id.tetMobileNumber);
        tetAddressDoorno = findViewById(R.id.tetAddressDoorno);
        tetAddressStreet = findViewById(R.id.tetAddressStreet);
        tetCity = findViewById(R.id.tetCity);
        tetDistrict = findViewById(R.id.tetDistrict);
        tetPincode = findViewById(R.id.tetPincode);
        tetState = findViewById(R.id.tetState);
        tetCountry = findViewById(R.id.tetCountry);
        tetLandmark = findViewById(R.id.tetLandmark);

        tvTypesOfAddress = findViewById(R.id.tvTypesOfAddress);
        radioHome = findViewById(R.id.radioHome);
        radioOffice = findViewById(R.id.radioOffice);
        radioGroup = findViewById(R.id.radioGroup);
        // checkboxAddress = findViewById(R.id.checkboxAddress);
        btnSubmit = findViewById(R.id.btnSubmit);

        tvToolbar = findViewById(R.id.tvToolbar);
        tvToolbar.setText("Add New Address");
        backIcon = findViewById(R.id.backIcon);

        sharedPreferences = getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        radioHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addresstype = "Home";
            }
        });

        radioOffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addresstype = "Office";
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tetFullName.getText().length() == 0) {
                    tetFullName.requestFocus();
                    tetFullName.setError("Field cannot be empty");
                } else if (tetMobileNumber.getText().length() == 0) {
                    tetMobileNumber.requestFocus();
                    tetMobileNumber.setError("Field cannot be empty");
                } else if (tetAddressDoorno.getText().length() == 0) {
                    tetAddressDoorno.requestFocus();
                    tetAddressDoorno.setError("Field cannot be empty");
                } else if (tetAddressStreet.getText().length() == 0) {
                    tetAddressStreet.requestFocus();
                    tetAddressStreet.setError("Field cannot be empty");
                } else if (tetCity.getText().length() == 0) {
                    tetCity.requestFocus();
                    tetCity.setError("Field cannot be empty");
                } else if (tetDistrict.getText().length() == 0) {
                    tetDistrict.requestFocus();
                    tetDistrict.setError("Field cannot be empty");
                } else if (tetPincode.getText().length() == 0) {
                    tetPincode.requestFocus();
                    tetPincode.setError("Field cannot be empty");
                } else if (tetState.getText().length() == 0) {
                    tetState.requestFocus();
                    tetState.setError("Field cannot be empty");
                } else if (tetCountry.getText().length() == 0) {
                    tetCountry.requestFocus();
                    tetCountry.setError("Field cannot be empty");
                }
//                else if (tetLandmark.getText().length() == 0) {
//                    tetLandmark.requestFocus();
//                    tetLandmark.setError("Field cannot be empty"); }
                else if (!radioHome.isChecked() && !radioOffice.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Please select address type !!!", Toast.LENGTH_SHORT).show();
                } else {
                    if (action.equals("add")) {
                        apiAddAddress();
                    } else if (action.equals("edit")) {
                        apiEditAddress();
                    }
                }
            }
        });

        action = getIntent().getStringExtra("action");

        if (action.equals("add")) {
            tvToolbar.setText("Add Address");
            btnSubmit.setText("Add Address");
        } else if (action.equals("edit")) {
            tvToolbar.setText("Edit Address");
            btnSubmit.setText("Submit");
            addressId = getIntent().getIntExtra("address_id", 0);
            tetFullName.setText(getIntent().getStringExtra("name"));
            tetMobileNumber.setText(getIntent().getStringExtra("mobile"));
            tetAddressDoorno.setText(getIntent().getStringExtra("door_no"));
            tetAddressStreet.setText(getIntent().getStringExtra("street"));
            tetCountry.setText(getIntent().getStringExtra("country"));
            tetState.setText(getIntent().getStringExtra("state"));
            tetDistrict.setText(getIntent().getStringExtra("district"));
            tetLandmark.setText(getIntent().getStringExtra("landmark"));
            tetCity.setText(getIntent().getStringExtra("city"));
            tetPincode.setText(getIntent().getStringExtra("pincode"));

            if (getIntent().getStringExtra("address_type").equals("Home")) {
                radioHome.setChecked(true);
            } else if (getIntent().getStringExtra("address_type").equals("Office")) {
                radioOffice.setChecked(true);
            }
        }
    }

    public void apiAddAddress() {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.add_address(sharedPreferences.getInt("customer_id", 0),
                tetFullName.getText().toString(), tetMobileNumber.getText().toString(), tetAddressDoorno.getText().toString(),
                tetAddressStreet.getText().toString(), tetLandmark.getText().toString(), addresstype, tetPincode.getText().toString(), tetCity.getText().toString(), tetDistrict.getText().toString(),
                tetState.getText().toString(), tetCountry.getText().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent();
                    setResult(1, intent);
                    finish();
                    Toast.makeText(getApplicationContext(), response.body(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void apiEditAddress() {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.edit_address(addressId, tetFullName.getText().toString(), tetMobileNumber.getText().toString(), tetAddressDoorno.getText().toString(),
                tetAddressStreet.getText().toString(), tetLandmark.getText().toString(), addresstype, tetPincode.getText().toString(), tetCity.getText().toString(),
                tetDistrict.getText().toString(), tetState.getText().toString(), tetCountry.getText().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Intent intent = new Intent();
                setResult(2, intent);
                finish();
                try {
                    jsonArray = new JSONArray(response.body());
                    jsonObject = jsonArray.getJSONObject(0);
                    Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}