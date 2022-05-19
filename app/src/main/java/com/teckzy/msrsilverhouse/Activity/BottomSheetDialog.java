package com.teckzy.msrsilverhouse.Activity;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.Adapter.SubsribtionWeightAdapter;
import com.teckzy.msrsilverhouse.Interface.BottomSheetData;
import com.teckzy.msrsilverhouse.Pojo.ChildPricePojo;
import com.teckzy.msrsilverhouse.Pojo.SubscribtionDetailsPojo;
import com.teckzy.msrsilverhouse.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    TextView tvGetFreshFruits, tvEveryWeekAtYourDoorstep, tvPremium, tvPremiumData;
    RecyclerView rvWeight;
    Button btnCancel, btnSubscribe;
    APIInterface apiInterface;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String weights = "", subscriptionid, amounts = "";
    SubsribtionWeightAdapter subsribtionWeightAdapter;
    JSONArray jsonArray;
    JSONObject jsonObject;
    List<SubscribtionDetailsPojo> subscribtionDetailsPojoList;
    BottomSheetData bottomSheetData;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_subscribtion, container, false);

        tvGetFreshFruits = view.findViewById(R.id.tvGetFreshFruits);
        tvEveryWeekAtYourDoorstep = view.findViewById(R.id.tvEveryWeekAtYourDoorstep);
        tvPremium = view.findViewById(R.id.tvPremium);
        tvPremiumData = view.findViewById(R.id.tvPremiumData);
        rvWeight = view.findViewById(R.id.rvWeight);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnSubscribe = view.findViewById(R.id.btnSubscribe);

        sharedPreferences = getActivity().getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        subscribtionDetailsPojoList = new ArrayList<>();

        Bundle bundle = getArguments();
        subscriptionid = bundle.getString("subscribtion_id");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        apigetsubscribtiondetails(subscriptionid);

        bottomSheetData = new BottomSheetData() {
            @Override
            public void response(String weight, String amount) {
                weights = weight;
                amounts = amount;
            }
        };

        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weights.equals("") && amounts.equals("")) {
                    Toast.makeText(getActivity(), "Please choose kg and weight", Toast.LENGTH_SHORT).show();
                } else {
                    apiInterface = APIClient.getAPIClient().create(APIInterface.class);
                    Call<String> call = apiInterface.add_subscribers(sharedPreferences.getInt("customer_id", 0), subscriptionid, weights, amounts);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                try {
                                    jsonArray = new JSONArray(response.body());
                                    jsonObject = jsonArray.getJSONObject(0);
                                    Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Please try again later !!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        return view;
    }

    public void apigetsubscribtiondetails(String subscribtion_id) {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<List<SubscribtionDetailsPojo>> call = apiInterface.get_subscribtion_details(subscribtion_id);
        call.enqueue(new Callback<List<SubscribtionDetailsPojo>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<SubscribtionDetailsPojo>> call, Response<List<SubscribtionDetailsPojo>> response) {
                if (response.isSuccessful()) {
                    if (response.isSuccessful()) {
                        subscribtionDetailsPojoList = response.body();
                        tvPremium.setText(subscribtionDetailsPojoList.get(0).getSubscribtion_name());
                        tvPremiumData.setText(subscribtionDetailsPojoList.get(0).getProducts());
                        getSubscribtionWeight(subscribtionDetailsPojoList.get(0).getWeight_amount());
                    } else {
                        Toast.makeText(getActivity(), "Please try again later !!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SubscribtionDetailsPojo>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getSubscribtionWeight(List<ChildPricePojo> childPricePojos) {
        subsribtionWeightAdapter = new SubsribtionWeightAdapter(getActivity(), childPricePojos, bottomSheetData);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvWeight.setLayoutManager(mLayoutManager);
        rvWeight.setAdapter(subsribtionWeightAdapter);
        subsribtionWeightAdapter.notifyDataSetChanged();
    }
}