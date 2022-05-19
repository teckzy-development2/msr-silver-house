package com.teckzy.msrsilverhouse.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class OTPVerification extends AppCompatActivity {
    EditText otpet1, otpet2, otpet3, otpet4, otpet5, otpet6;
    Button verifybtn;
    TextView mobilenumber, resendtv;
    TextView tvToolbar;
    ImageView backIcon;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    JSONArray jsonArray, customerJsonArray;
    JSONObject jsonObject, customerJsonObject;
    APIInterface apiInterface;
    String msg, status, customer, otp, usertype, firstName, lastName, email, mobileNo, password, action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otpet1 = findViewById(R.id.otpet1);
        otpet2 = findViewById(R.id.otpet2);
        otpet3 = findViewById(R.id.otpet3);
        otpet4 = findViewById(R.id.otpet4);
        otpet5 = findViewById(R.id.otpet5);
        otpet6 = findViewById(R.id.otpet6);
        verifybtn = findViewById(R.id.verifybtn);
        mobilenumber = findViewById(R.id.mobilenumber);
        resendtv = findViewById(R.id.resendtv);
        tvToolbar = findViewById(R.id.tvToolbar);
        tvToolbar.setText("OTP Verification");
        backIcon = findViewById(R.id.backIcon);

        sharedPreferences = getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        firstName = getIntent().getStringExtra("first_name");
        lastName = getIntent().getStringExtra("last_name");
        email = getIntent().getStringExtra("email");
        mobileNo = getIntent().getStringExtra("mobile_no");
        password = getIntent().getStringExtra("password");
        action = getIntent().getStringExtra("action");
        otp = getIntent().getStringExtra("otp");
        mobilenumber.setText(mobileNo);

        otpet1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otpet1.getText().toString().length() == 1)     //size as per your requirement
                {
                    otpet2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                if (otpet2.length() == 0)
//                    otpet1.requestFocus();
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

        });

        otpet2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otpet2.getText().toString().length() == 1)     //size as per your requirement
                {
                    otpet3.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable editable) {
//                if (otpet3.length() == 0)
//                    otpet2.requestFocus();
            }
        });

        otpet3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otpet3.getText().toString().length() == 1)     //size as per your requirement
                {
                    otpet4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                if (otpet4.length() == 0)
//                    otpet3.requestFocus();
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

        });

        otpet4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otpet4.getText().toString().length() == 1)     //size as per your requirement
                {
                    otpet5.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable editable) {
//                if (otpet5.length() == 0)
//                    otpet4.requestFocus();
            }
        });

        otpet5.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otpet5.getText().toString().length() == 1)     //size as per your requirement
                {
                    otpet6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                if (otpet6.length() == 0)
//                    otpet5.requestFocus();
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

        });

        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otpet1.getText().toString().length() == 0) {
                    otpet1.requestFocus();
                    otpet1.setError("Field cannot be empty");
                } else if (otpet2.getText().toString().length() == 0) {
                    otpet2.requestFocus();
                    otpet2.setError("Field cannot be empty");
                } else if (otpet3.getText().toString().length() == 0) {
                    otpet3.requestFocus();
                    otpet3.setError("Field cannot be empty");
                } else if (otpet4.getText().toString().length() == 0) {
                    otpet4.requestFocus();
                    otpet4.setError("Field cannot be empty");
                } else if (otpet5.getText().toString().length() == 0) {
                    otpet5.requestFocus();
                    otpet5.setError("Field cannot be empty");
                } else if (otpet6.getText().toString().length() == 0) {
                    otpet6.requestFocus();
                    otpet6.setError("Field cannot be empty");
                } else if (otp.equals(otpet1.getText().toString() + otpet2.getText().toString() + otpet3.getText().toString() + otpet4.getText().toString() + otpet5.getText().toString() + otpet6.getText().toString())) {
                    Toast.makeText(getApplicationContext(),"OTP Verified !!", Toast.LENGTH_LONG).show();

                    if (action.equals("signup"))
                    {
                        apiRegistration();
                    }
                    else if (action.equals("forgot_password"))
                    {
                        Intent intent = new Intent(getApplicationContext(), ChangeForgotPasswordActivity.class);
                        intent.putExtra("mobile_no", mobileNo);
                        intent.putExtra("click","forgot_password");
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "OTP Incorrect", Toast.LENGTH_LONG).show();
                }

            }
        });

        resendtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (action.equals("forgot_password")) {
                    apiForgotPassword();
                } else if (action.equals("signup")) {
                    apiInterface = APIClient.getAPIClient().create(APIInterface.class);
                    Call<String> call = apiInterface.generate_otp(mobileNo);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                try {
                                    jsonArray = new JSONArray(response.body());
                                    jsonObject = jsonArray.getJSONObject(0);
                                    msg = jsonObject.getString("msg");
                                    status = jsonObject.getString("status");
                                    usertype = jsonObject.getString("user_type");

                                    if (status.equals("true") && usertype.equals("new")) {
                                        Toast.makeText(getApplicationContext(), msg + "\t" + otp, Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Please try again later !!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    public void apiForgotPassword() {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.forgot_password(mobileNo);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body().equals("Please Enter Registered Mobile Number")) {
                        Toast.makeText(getApplicationContext(), response.body(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body(), Toast.LENGTH_LONG).show();
                        otp = response.body();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please try again later !!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void apiRegistration()
    {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.register(firstName, lastName, email, mobileNo, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        jsonArray = new JSONArray(response.body());
                        jsonObject = jsonArray.getJSONObject(0);
                        msg = jsonObject.getString("msg");
                        status = jsonObject.getString("status");
                        customer = jsonObject.getString("customer");

                        if (status.equals("true")) {
                            customerJsonArray = new JSONArray(customer);
                            customerJsonObject = customerJsonArray.getJSONObject(0);

                            editor.putInt("customer_id", customerJsonObject.getInt("customer_id"));
                            editor.putString("name", customerJsonObject.getString("name"));
                            editor.putString("mobile_no", customerJsonObject.getString("mobile_no"));
                            editor.putString("email", customerJsonObject.getString("email"));
                            editor.putString("customer_type", customerJsonObject.getString("customer_type"));
                            editor.commit();

                            Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please try again later !!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}