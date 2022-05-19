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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeForgotPasswordActivity extends AppCompatActivity {
    Button btnSumbit, btnCancel;
    EditText tetOldPassword, tetNewPassword, tetConfirmPassword;
    TextView tvToolbar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView backIcon;
    APIInterface apiInterface;
    JSONArray jsonArray;
    JSONObject jsonObject;
    String click = "";
    TextInputLayout tilOld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        tetOldPassword = findViewById(R.id.tetOldPassword);
        tetNewPassword = findViewById(R.id.tetNewPassword);
        tetConfirmPassword = findViewById(R.id.tetConfirmPassword);
        btnSumbit = findViewById(R.id.btnSumbit);
        btnCancel = findViewById(R.id.btnCancel);
        tilOld = findViewById(R.id.tilOld);
        tvToolbar = findViewById(R.id.tvToolbar);
        backIcon = findViewById(R.id.backIcon);

        sharedPreferences = getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        click = getIntent().getStringExtra("click");

        if (click.equals("forgot_password")) {
            tilOld.setVisibility(View.GONE);
            tvToolbar.setText("Forgot Password");
        }

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tetConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!(tetNewPassword.getText().toString().equals(tetConfirmPassword.getText().toString()))) {
                    tetConfirmPassword.setError("Password does not match");
                }
            }
        });

        btnSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click.equals("forgot_password")) {
                    if (tetNewPassword.getText().length() == 0) {
                        tetNewPassword.requestFocus();
                        tetNewPassword.setError("Field cannot be empty");
                    } else if (tetConfirmPassword.getText().length() == 0) {
                        tetConfirmPassword.requestFocus();
                        tetConfirmPassword.setError("Field cannot be empty");
                    } else if (!(tetNewPassword.getText().toString().equals(tetConfirmPassword.getText().toString()))) {
                        tetConfirmPassword.setError("Password does not match");
                    } else {
                        apiChangeForgotPassword(sharedPreferences.getString("mobile_no", ""),tetNewPassword.getText().toString() );
                    }
                }
            }
        });
    }

    public void apiChangeForgotPassword(String mobile_no, String Password) {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.change_forgot_password(mobile_no, Password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        jsonArray = new JSONArray(response.body());
                        jsonObject = jsonArray.getJSONObject(0);

                        if (click.equals("forgot_password")) {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }

                        Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
