package com.teckzy.msrsilverhouse.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingReviewActivity extends Activity {
    APIInterface apiInterface;
    TextView tvToolbar, tvRating, tvReview;
    ImageView ivRating1, ivRating2, ivRating3, ivRating4, ivRating5, backIcon;
    EditText etWriteReview;
    Button btnSubmit;
    JSONArray jsonArray;
    JSONObject jsonObject;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int productId;
    String review = "", rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addreview);

        tvToolbar = findViewById(R.id.tvToolbar);
        tvToolbar.setText("Add Review");
        backIcon = findViewById(R.id.backIcon);

        tvRating = findViewById(R.id.tvRating);
        tvReview = findViewById(R.id.tvReview);
        ivRating1 = findViewById(R.id.ivRating1);
        ivRating2 = findViewById(R.id.ivRating2);
        ivRating3 = findViewById(R.id.ivRating3);
        ivRating4 = findViewById(R.id.ivRating4);
        ivRating5 = findViewById(R.id.ivRating5);
        etWriteReview = findViewById(R.id.etWriteReview);
        btnSubmit = findViewById(R.id.btnSubmit);

        sharedPreferences = getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        productId = getIntent().getIntExtra("product_id", 0);
        rating = getIntent().getStringExtra("rate");
        review = getIntent().getStringExtra("review");
        etWriteReview.setText(review);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (rating.equals("1")) {
            setRating1();
        } else if (rating.equals("2")) {
            setRating2();
        } else if (rating.equals("3")) {
            setRating3();
        } else if (rating.equals("4")) {
            setRating4();
        } else if (rating.equals("5")) {
            setRating5();
        }

        ivRating1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = String.valueOf(1);
                setRating1();
            }
        });

        ivRating2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = String.valueOf(2);
                setRating2();
            }
        });

        ivRating3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = String.valueOf(3);
                setRating3();
            }
        });

        ivRating4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = String.valueOf(4);
                setRating4();
            }
        });

        ivRating5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = String.valueOf(5);
                setRating5();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rating.equals("0")) {
                    Toast.makeText(getApplicationContext(), "Please enter rating !!", Toast.LENGTH_SHORT).show();
                } else {
                    apiSendRatingReview(sharedPreferences.getInt("customer_id", 0), productId, rating);
                }
            }
        });
    }

    public void setRating1() {
        ivRating1.setImageDrawable(getResources().getDrawable(R.drawable.filled_star));
        ivRating2.setImageDrawable(getResources().getDrawable(R.drawable.outline_star));
        ivRating3.setImageDrawable(getResources().getDrawable(R.drawable.outline_star));
        ivRating4.setImageDrawable(getResources().getDrawable(R.drawable.outline_star));
        ivRating5.setImageDrawable(getResources().getDrawable(R.drawable.outline_star));
    }

    public void setRating2() {
        ivRating1.setImageDrawable(getResources().getDrawable(R.drawable.filled_star));
        ivRating2.setImageDrawable(getResources().getDrawable(R.drawable.filled_star));
        ivRating3.setImageDrawable(getResources().getDrawable(R.drawable.outline_star));
        ivRating4.setImageDrawable(getResources().getDrawable(R.drawable.outline_star));
        ivRating5.setImageDrawable(getResources().getDrawable(R.drawable.outline_star));
    }

    public void setRating3() {
        ivRating1.setImageDrawable(getResources().getDrawable(R.drawable.filled_star));
        ivRating2.setImageDrawable(getResources().getDrawable(R.drawable.filled_star));
        ivRating3.setImageDrawable(getResources().getDrawable(R.drawable.filled_star));
        ivRating4.setImageDrawable(getResources().getDrawable(R.drawable.outline_star));
        ivRating5.setImageDrawable(getResources().getDrawable(R.drawable.outline_star));
    }

    public void setRating4() {
        ivRating1.setImageDrawable(getResources().getDrawable(R.drawable.filled_star));
        ivRating2.setImageDrawable(getResources().getDrawable(R.drawable.filled_star));
        ivRating3.setImageDrawable(getResources().getDrawable(R.drawable.filled_star));
        ivRating4.setImageDrawable(getResources().getDrawable(R.drawable.filled_star));
        ivRating5.setImageDrawable(getResources().getDrawable(R.drawable.outline_star));
    }

    public void setRating5() {
        ivRating1.setImageDrawable(getResources().getDrawable(R.drawable.filled_star));
        ivRating2.setImageDrawable(getResources().getDrawable(R.drawable.filled_star));
        ivRating3.setImageDrawable(getResources().getDrawable(R.drawable.filled_star));
        ivRating4.setImageDrawable(getResources().getDrawable(R.drawable.filled_star));
        ivRating5.setImageDrawable(getResources().getDrawable(R.drawable.filled_star));
    }

    public void apiSendRatingReview(int customer_id, int productId, String rating) {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.add_reviews(customer_id, productId, rating, etWriteReview.getText().toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        jsonArray = new JSONArray(response.body());
                        jsonObject = jsonArray.getJSONObject(0);

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