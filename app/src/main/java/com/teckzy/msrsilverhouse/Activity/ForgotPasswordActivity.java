package com.teckzy.msrsilverhouse.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends Activity {
    APIInterface apiInterface;
    TextView tvToolbar, tvPhoneNumber;
    ImageView backIcon;
    TextInputEditText tetPhone;
    Button btnResetPassword;
    JSONArray jsonArray;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        tvToolbar = findViewById(R.id.tvToolbar);
        tvToolbar.setText("Forgot Password");
        backIcon = findViewById(R.id.backIcon);

        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tetPhone = findViewById(R.id.tetPhone);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tetPhone.getText().toString().length() == 0) {
                    tetPhone.requestFocus();
                    tetPhone.setError("Field cannot be empty");
                } else {
                    apiForgotPassword();
                }
            }
        });
    }

    public void apiForgotPassword() {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.forgot_password(tetPhone.getText().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        jsonArray = new JSONArray(response.body());
                        jsonObject = jsonArray.getJSONObject(0);
                        Toast.makeText(getApplicationContext(), jsonObject.getString("otp_no"), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), OTPVerification.class);
                        intent.putExtra("action", "forgot_password");
                        intent.putExtra("otp", jsonObject.getString("otp_no"));
                        intent.putExtra("mobile_no", tetPhone.getText().toString());
                        startActivity(intent);
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