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

public class SignupActivity extends AppCompatActivity
{
    EditText tetFirstName, tetLastName, tetPhone, tetEmail, tetPassword, tetConfirmPassword;
    Button btnContinue;
    TextView tvAlreadyaccount, tvLogin;
    String msg, status, type, usertype, otpno = "";
    APIInterface apiInterface;
    JSONArray jsonArray;
    JSONObject jsonObject;
    TextView tvToolbar;
    ImageView backIcon;
    String mobilePattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        tetFirstName = findViewById(R.id.tetFirstName);
        tetLastName = findViewById(R.id.tetLastName);
        tetPhone = findViewById(R.id.tetPhone);
        tetEmail = findViewById(R.id.tetEmail);
        tetPassword = findViewById(R.id.tetPassword);
        tetConfirmPassword = findViewById(R.id.tetConfirmPassword);
        btnContinue = findViewById(R.id.btnContinue);
        tvAlreadyaccount = findViewById(R.id.tvAlreadyaccount);
        tvLogin = findViewById(R.id.tvLogin);

        tvToolbar = findViewById(R.id.tvToolbar);
        tvToolbar.setText("Registration");
        backIcon = findViewById(R.id.backIcon);

        backIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        tetPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                if (!(tetPassword.getText().toString().equals(tetConfirmPassword.getText().toString()))) {
                    tetConfirmPassword.setError("Password does not match");
                }
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tetFirstName.getText().toString().length() == 0) {
                    tetFirstName.requestFocus();
                    tetFirstName.setError("Field cannot be Empty");
                } else if (!tetFirstName.getText().toString().matches("[a-zA-Z ]+")) {
                    tetFirstName.requestFocus();
                    tetFirstName.setError("Enter only Alphabetical Character");
                } else if (tetLastName.getText().toString().length() == 0) {
                    tetLastName.requestFocus();
                    tetLastName.setError("Field cannot be Empty");
                } else if (!tetLastName.getText().toString().matches("[a-zA-Z ]+")) {
                    tetLastName.requestFocus();
                    tetLastName.setError("Enter only Alphabetical Character");
                } else if (tetPhone.getText().toString().length() == 0) {
                    tetPhone.requestFocus();
                    tetPhone.setError("Field cannot be Empty");
                } else if (!tetPhone.getText().toString().matches(mobilePattern)) {
                    tetPhone.requestFocus();
                    tetPhone.setError("Please enter valid mobile number");
                } else if (tetEmail.getText().toString().length() == 0) {
                    tetEmail.requestFocus();
                    tetEmail.setError("Field cannot be EmptyY");
                } else if (!tetEmail.getText().toString().matches(emailPattern)) {
                    tetEmail.requestFocus();
                    tetEmail.setError("Please enter valid email address");
                } else if (tetPassword.getText().toString().length() == 0) {
                    tetPassword.requestFocus();
                    tetPassword.setError("Field cannot be EmptyY");
                } else if (tetConfirmPassword.getText().toString().length() == 0) {
                    tetConfirmPassword.requestFocus();
                    tetConfirmPassword.setError("Field cannot be EmptyY");
                } else if (!(tetPassword.getText().toString().equals(tetConfirmPassword.getText().toString()))) {
                    Toast.makeText(getApplicationContext(), "Password and Confirm Password doesn't match", Toast.LENGTH_LONG).show();
                }  else {
                    apiGenerateOtp();
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void apiGenerateOtp() {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.generate_otp(tetPhone.getText().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        jsonArray = new JSONArray(response.body());
                        jsonObject = jsonArray.getJSONObject(0);
                        msg = jsonObject.getString("msg");
                        status = jsonObject.getString("status");
                        otpno = jsonObject.getString("otp_no");
                        usertype = jsonObject.getString("user_type");

                        if (status.equals("true") && usertype.equals("new")) {
                            editor.putString("otp_no", otpno);
                            editor.commit();
                            Toast.makeText(getApplicationContext(), msg + "\t" + otpno, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), OTPVerification.class);
                            intent.putExtra("otp", otpno);
                            intent.putExtra("action", "signup");
                            intent.putExtra("first_name", tetFirstName.getText().toString());
                            intent.putExtra("last_name", tetLastName.getText().toString());
                            intent.putExtra("email", tetEmail.getText().toString());
                            intent.putExtra("mobile_no", tetPhone.getText().toString());
                            intent.putExtra("password", tetPassword.getText().toString());
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
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