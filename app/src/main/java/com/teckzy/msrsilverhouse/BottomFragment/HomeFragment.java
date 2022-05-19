package com.teckzy.msrsilverhouse.BottomFragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.Activity.ProductActivity;
import com.teckzy.msrsilverhouse.Activity.ProductDetailsActivity;
import com.teckzy.msrsilverhouse.Adapter.HomeProductAdapter;
import com.teckzy.msrsilverhouse.Adapter.SliderAdapter;
import com.teckzy.msrsilverhouse.Adapter.SuggestionAdapter;
import com.teckzy.msrsilverhouse.Interface.onBackPressed;
import com.teckzy.msrsilverhouse.Pojo.HomeProductPojo;
import com.teckzy.msrsilverhouse.Pojo.SliderPojo;
import com.teckzy.msrsilverhouse.Pojo.SuggestionPojo;
import com.teckzy.msrsilverhouse.R;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements onBackPressed {
    TextView viewalltv;
    SearchView searchProduct;
    RecyclerView searchrv, productsrv;
    SliderView imageSlider;
    HomeProductAdapter homeProductAdapter;
    List<HomeProductPojo> homeProductPojoList;
    SliderAdapter SliderAdapter;
    APIInterface apiInterface;
    SuggestionAdapter suggestionAdapter;
    JSONArray jsonArray;
    JSONObject jsonObject;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    Fragment fragment;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchProduct = view.findViewById(R.id.searchProduct);
        searchrv = view.findViewById(R.id.searchrv);
        imageSlider = view.findViewById(R.id.imageSlider);
        viewalltv = view.findViewById(R.id.viewalltv);
        productsrv = view.findViewById(R.id.productsrv);

        sharedPreferences = getContext().getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Wait while loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        searchProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchProduct.setIconified(false);
            }
        });

        searchProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getSearchProducts(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    searchrv.setVisibility(View.GONE);
                } else {
                    searchrv.setVisibility(View.VISIBLE);
                }
                apiGetSuggestion(newText);
                return false;
            }
        });

        viewalltv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                fragment = new CategoryFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();
            }
        });

        homeProductPojoList = new ArrayList<>();

        apiFreshProducts();
        apiGetImageSlider();

        return view;
    }

    public void getSearchProducts(String query)
    {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.search_product(query);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        jsonArray = new JSONArray(response.body());
                        jsonObject = jsonArray.getJSONObject(0);
                        if (jsonObject.getString("status").equals("Product details")) {
                            Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
                            intent.putExtra("productId", jsonObject.getInt("product_id"));
                            startActivity(intent);
                        } else if (jsonObject.getString("status").equals("Product List")) {
                            Intent intent = new Intent(getContext(), ProductActivity.class);
                            intent.putExtra("category_id", jsonObject.getString("category_id"));
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void apiGetSuggestion(String searchText) {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<List<SuggestionPojo>> call = apiInterface.search_product_list(searchText);
        call.enqueue(new Callback<List<SuggestionPojo>>() {
            @Override
            public void onResponse(Call<List<SuggestionPojo>> call, Response<List<SuggestionPojo>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() != 0) {
                        generateSuggestions(response.body());
                    } else {
                        Toast.makeText(getActivity(), "No Products Available", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SuggestionPojo>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void apiGetImageSlider() {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<List<SliderPojo>> call = apiInterface.list_banner();
        call.enqueue(new Callback<List<SliderPojo>>() {
            @Override
            public void onResponse(Call<List<SliderPojo>> call, Response<List<SliderPojo>> response) {
                if (response.isSuccessful()) {
                    generateSlider(response.body());
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SliderPojo>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void apiFreshProducts()
    {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<List<HomeProductPojo>> call = apiInterface.list_product(sharedPreferences.getInt("customer_id", 0));
        call.enqueue(new Callback<List<HomeProductPojo>>()
        {
            @Override
            public void onResponse(Call<List<HomeProductPojo>> call, Response<List<HomeProductPojo>> response)
            {
                if (response.isSuccessful())
                {
                    getFreshProducts(response.body());
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<HomeProductPojo>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getFreshProducts(List<HomeProductPojo> homeProductPojoList) {
        homeProductAdapter = new HomeProductAdapter(getActivity(), homeProductPojoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        productsrv.setLayoutManager(mLayoutManager);
        productsrv.setAdapter(homeProductAdapter);
        homeProductAdapter.notifyDataSetChanged();
    }

    public void generateSuggestions(List<SuggestionPojo> suggestionPojoList) {
        suggestionAdapter = new SuggestionAdapter(getActivity(), suggestionPojoList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        searchrv.setLayoutManager(mLayoutManager);
        searchrv.setItemAnimator(new DefaultItemAnimator());
        searchrv.setAdapter(suggestionAdapter);
    }

    public void generateSlider(List<SliderPojo> sliderPojosList) {
        SliderAdapter = new SliderAdapter(getContext(), sliderPojosList);
        imageSlider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        imageSlider.setSliderAdapter(SliderAdapter);
        imageSlider.setScrollTimeInSec(3);
        imageSlider.setAutoCycle(true);
        imageSlider.startAutoCycle();
    }

    @Override
    public void onBackPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}