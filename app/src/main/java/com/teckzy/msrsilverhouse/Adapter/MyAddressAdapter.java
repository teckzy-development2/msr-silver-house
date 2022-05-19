package com.teckzy.msrsilverhouse.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.teckzy.msrsilverhouse.API.APIClient;
import com.teckzy.msrsilverhouse.API.APIInterface;
import com.teckzy.msrsilverhouse.Activity.MyAddressActivity;
import com.teckzy.msrsilverhouse.Interface.PassData;
import com.teckzy.msrsilverhouse.Pojo.AddressPojo;
import com.teckzy.msrsilverhouse.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAddressAdapter extends RecyclerView.Adapter<MyAddressAdapter.MyViewHolder> {
    APIInterface apiInterface;
    public Activity activity;
    private final List<AddressPojo> addressPojoList;
    PassData passData;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    JSONArray jsonArray;
    JSONObject jsonObject;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cvAddress;
        TextView tvName, tvDoorno, tvStreet, tvCity, tvDistrict, tvPincode, tvLandmarkTitle, tvLandmark,
                tvContactList, tvPhoneNumber, tvAddressType, tvState, tvCountry;
        ImageView ivEdit, ivDelete;

        public MyViewHolder(View view) {
            super(view);
            cvAddress = view.findViewById(R.id.cvAddress);
            tvName = view.findViewById(R.id.tvName);
            tvDoorno = view.findViewById(R.id.tvDoorno);
            tvStreet = view.findViewById(R.id.tvStreet);
            tvCity = view.findViewById(R.id.tvCity);
            tvDistrict = view.findViewById(R.id.tvDistrict);
            tvPincode = view.findViewById(R.id.tvPincode);
            tvLandmarkTitle = view.findViewById(R.id.tvLandmarkTitle);
            tvLandmark = view.findViewById(R.id.tvLandmark);
            tvContactList = view.findViewById(R.id.tvContactList);
            tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
            tvAddressType = view.findViewById(R.id.tvAddressType);
            tvState = view.findViewById(R.id.tvState);
            tvCountry = view.findViewById(R.id.tvCountry);
            ivEdit = view.findViewById(R.id.ivEdit);
            ivDelete = view.findViewById(R.id.ivDelete);
        }
    }

    public MyAddressAdapter(Activity activity, List<AddressPojo> addressPojoList, PassData passData) {
        this.activity = activity;
        this.addressPojoList = addressPojoList;
        this.passData = passData;
    }

    @NonNull
    @Override
    public MyAddressAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_myaddress, parent, false);

        return new MyAddressAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAddressAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final AddressPojo addressPojo = addressPojoList.get(position);

        sharedPreferences = activity.getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        holder.tvName.setText(addressPojo.getName());
        holder.tvDoorno.setText(addressPojo.getDoor_no());
        holder.tvStreet.setText(addressPojo.getStreet());
        holder.tvCity.setText(addressPojo.getCity());
        holder.tvDistrict.setText(addressPojo.getDistrict());
        holder.tvPincode.setText(addressPojo.getPincode());
        holder.tvState.setText(addressPojo.getState());
        holder.tvCountry.setText(addressPojo.getCountry());
        holder.tvLandmark.setText(addressPojo.getLandmark());
        holder.tvPhoneNumber.setText(addressPojo.getMobile());
        holder.tvAddressType.setText(addressPojo.getAddress_type());

        holder.cvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("address_id", addressPojo.getAddress_id());
                activity.setResult(3, intent);
                activity.finish();
            }
        });

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyAddressActivity) activity).startActivityFromAdapter(addressPojo.getAddress_id(),
                        addressPojo.getName(), addressPojo.getMobile(), addressPojo.getDoor_no(), addressPojo.getStreet(),
                        addressPojo.getCity(), addressPojo.getDistrict(), addressPojo.getPincode(),
                        addressPojo.getState(), addressPojo.getCountry(),
                        addressPojo.getLandmark(), addressPojo.getAddress_type());
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAddressAlert(position, addressPojo.getAddress_id());
            }
        });
    }

    public void deleteAddressAlert(int position, int addressId) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setMessage("Are you sure you want to delete?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        apiDeleteAddress(addressId, position);
                    }
                }).setNegativeButton("Cancel", null);

        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    public void removeAt(int position) {
        addressPojoList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, addressPojoList.size());
        passData.respond(addressPojoList.size());
    }

    public void apiDeleteAddress(int address_id, int position)
    {
        apiInterface = APIClient.getAPIClient().create(APIInterface.class);
        Call<String> call = apiInterface.delete_address(address_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    jsonArray = new JSONArray(response.body());
                    jsonObject = jsonArray.getJSONObject(0);
                    Toast.makeText(activity.getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                removeAt(position);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressPojoList.size();
    }
}