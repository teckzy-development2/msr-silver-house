package com.teckzy.msrsilverhouse.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText mobilenumberet, passwordet;
    Button loginbtn;
    TextView dontaccounttv, signtv, tvSkip, tvForgotPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    JSONArray jsonArray, customerJsonArray;
    JSONObject jsonObject, customerJsonObject;
    APIInterface apiInterface;
    String msg, status, customer;
    Boolean isPasswordVisible = false;
    ProgressDialog progressDialog;
    private long pressedTime;
    String mobilePattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mobilenumberet = findViewById(R.id.mobilenumberet);
        passwordet = findViewById(R.id.passwordet);
        loginbtn = findViewById(R.id.loginbtn);
        dontaccounttv = findViewById(R.id.dontaccounttv);
        signtv = findViewById(R.id.signtv);
        tvSkip = findViewById(R.id.tvSkip);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        logo = findViewById(R.id.logo);

        Glide.with(logo)
                .load(R.drawable.msr)
                .fitCenter()
                .into(logo);

        sharedPreferences = this.getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        passwordet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (passwordet.getRight() - passwordet.getCompoundDrawables()[RIGHT].getBounds().width())) {
                        int selection = passwordet.getSelectionEnd();
                        if (isPasswordVisible) {
                            passwordet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.invisible_eye, 0);
                            passwordet.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            isPasswordVisible = false;
                        } else {
                            passwordet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visible_eye, 0);
                            passwordet.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisible = true;
                        }
                        passwordet.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        signtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobilenumberet.getText().toString().length() == 0) {
                    mobilenumberet.requestFocus();
                    mobilenumberet.setError("Field cannot be empty");
                } else if (!(mobilenumberet.getText().toString().matches(mobilePattern))) {
                    mobilenumberet.requestFocus();
                    mobilenumberet.setError("Please enter valid Mobile Number");
                } else if (passwordet.getText().toString().length() == 0)
                {
                    passwordet.requestFocus();
                    passwordet.setError("Field cannot be empty");
                }
                else
                {
                    check_login(mobilenumberet.getText().toString(), passwordet.getText().toString());

                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Wait while loading...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                }
            }
        });

        if (sharedPreferences.getBoolean("logged", false)) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void check_login(String mobile_no, String mpin)
    {
        {
            apiInterface = APIClient.getAPIClient().create(APIInterface.class);
            Call<String> call = apiInterface.check_login(mobile_no, mpin);
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

                            if (status.equals("true"))
                            {
                                customerJsonArray = new JSONArray(customer);
                                customerJsonObject = customerJsonArray.getJSONObject(0);

                                editor.putInt("customer_id", customerJsonObject.getInt("customer_id"));
                                editor.putString("name", customerJsonObject.getString("name"));
                                editor.putInt("cart_count", customerJsonObject.getInt("cart_count"));
                                editor.putString("mobile_no", customerJsonObject.getString("mobile_no"));
                                editor.putBoolean("logged", true);
                                editor.commit();
                                Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Please try again later !!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_LONG).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}