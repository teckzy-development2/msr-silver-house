package com.teckzy.msrsilverhouse.BottomFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.Adapter.CategoryAdapter;
import com.teckzy.msrsilverhouse.Interface.onBackPressed;
import com.teckzy.msrsilverhouse.Pojo.CategoryPojo;
import com.teckzy.msrsilverhouse.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment implements onBackPressed
{
    APIInterface apiInterface;
    CategoryAdapter categoryAdapter;
    RecyclerView rvCategory;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        rvCategory = view.findViewById(R.id.rvCategory);

        apiGetCategory();

        return view;
    }

    private void apiGetCategory()
    {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading . . .");
        progressDialog.show();
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<List<CategoryPojo>> call = apiInterface.get_category();
        call.enqueue(new Callback<List<CategoryPojo>>() {
            @Override
            public void onResponse(Call<List<CategoryPojo>> call, Response<List<CategoryPojo>> response) {
                if (response.isSuccessful()) {
                    getCategory(response.body());
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Please try again later !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryPojo>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCategory(List<CategoryPojo> categoryPojoList)
    {
        categoryAdapter = new CategoryAdapter(getActivity(), categoryPojoList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        rvCategory.setLayoutManager(mLayoutManager);
        rvCategory.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();
    }
    @Override
    public void onBackPressed()
    {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}