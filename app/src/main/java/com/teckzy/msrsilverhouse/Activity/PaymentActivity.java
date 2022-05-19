package com.teckzy.msrsilverhouse.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.R;
import com.razorpay.Checkout;

import org.json.JSONObject;

public class PaymentActivity extends Activity implements PaymentStatusListener
{
    APIInterface apiInterface;
    Checkout checkout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int address_id, total_pay;
    JSONObject jsonObject;
    String type;
    TextView tvPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tvPayment = findViewById(R.id.tvPayment);

        sharedPreferences = getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        address_id = getIntent().getIntExtra("address_id", 0);
        total_pay = getIntent().getIntExtra("total_pay", 0);
        type = getIntent().getStringExtra("type");

        //apiInitiatePayment(sharedPreferences.getInt("customer_id", 0), address_id, total_pay, type);

        makePayment(String.valueOf(total_pay),"benish1993@okhdfcbank","Benish","Testing","");
    }

    private void makePayment(String amount, String upi, String name, String desc, String transactionId)
    {
        // on below line we are calling an easy payment method and passing
        // all parameters to it such as upi id,name, description and others.
        final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                .with(this)
                // on below line we are adding upi id.
                .setPayeeVpa(upi)
                // on below line we are setting name to which we are making oayment.
                .setPayeeName(name)
                // on below line we are passing transaction id.
                .setTransactionId(transactionId)
                // on below line we are passing transaction ref id.
                .setTransactionRefId(transactionId)
                // on below line we are adding description to payment.
                .setDescription(desc)
                // on below line we are passing amount which is being paid.
                .setAmount(amount)
                // on below line we are calling a build method to build this ui.
                .build();
        // on below line we are calling a start
        // payment method to start a payment.
        easyUpiPayment.startPayment();
        // on below line we are calling a set payment
        // status listener method to call other payment methods.
        easyUpiPayment.setPaymentStatusListener(this);
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails)
    {
        // on below line we are getting details about transaction when completed.
        String transcDetails = transactionDetails.getStatus().toString() + "\n" + "Transaction ID : " + transactionDetails.getTransactionId();
        tvPayment.setVisibility(View.VISIBLE);
        // on below line we are setting details to our text view.
        tvPayment.setText(transcDetails);
    }

    @Override
    public void onTransactionSuccess() {
        // this method is called when transaction is successful and we are displaying a toast message.
        Toast.makeText(this, "Transaction successfully completed..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionSubmitted() {
        // this method is called when transaction is done
        // but it may be successful or failure.
        Log.e("TAG", "TRANSACTION SUBMIT");
    }

    @Override
    public void onTransactionFailed() {
        // this method is called when transaction is failure.
        Toast.makeText(this, "Failed to complete transaction", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionCancelled() {
        // this method is called when transaction is cancelled.
        Toast.makeText(this, "Transaction cancelled..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAppNotFound() {
        // this method is called when the users device is not having any app installed for making payment.
        Toast.makeText(this, "No app found for making transaction..", Toast.LENGTH_SHORT).show();
    }

    /*public void apiInitiatePayment(int customer_id, int addressId, int amountPayable, String type) {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.payment(customer_id, addressId, amountPayable, type);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        jsonObject = new JSONObject(response.body());

                        startPayment(jsonObject.getString("key"), jsonObject.getString("name"),
                                jsonObject.getInt("amount"), jsonObject.getString("description"),
                                jsonObject.getString("image"), jsonObject.getString("color"),
                                jsonObject.getString("email"), jsonObject.getString("contact"),
                                jsonObject.getString("order_id"));
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

    public void startPayment(String key, String name, int amount, String description, String image, String color, String email, String contact, String orderId) {
        checkout = new Checkout();
        checkout.setKeyID(key);
        checkout.setImage(R.drawable.rzp_logo);
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", name);
            options.put("description", description);
            options.put("image", APIClient.BASE_URL + image);
            options.put("order_id", orderId);
            options.put("theme.color", color);
            options.put("currency", "INR");
            options.put("amount", amount);
            options.put("prefill.email", email);
            options.put("prefill.contact", contact);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);
            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID, PaymentData paymentData) {
        apiFinishPayment(paymentData.getOrderId(), razorpayPaymentID, paymentData.getSignature());
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(getApplicationContext(), "Payment Failure !!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }

    public void apiFinishPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.payment_success(razorpayOrderId, razorpayPaymentId, razorpaySignature);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    alertOrderPlaced();
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

    public void alertOrderPlaced() {
        LayoutInflater factory = LayoutInflater.from(PaymentActivity.this);
        final View alertDialogView = factory.inflate(R.layout.layout_order_placed, null);
        Button btnDone = alertDialogView.findViewById(R.id.btnDone);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt("cart_count", 0);
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
        final AlertDialog alertDialog = new AlertDialog.Builder(PaymentActivity.this, R.style.CustomAlertDialog).create();
        alertDialog.setView(alertDialogView);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }*/
}