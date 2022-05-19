package com.teckzy.msrsilverhouse.BottomFragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.teckzy.msrsilverhouse.Activity.ChangeNewPasswordActivity;
import com.teckzy.msrsilverhouse.Activity.LoginActivity;
import com.teckzy.msrsilverhouse.Activity.MyAddressActivity;
import com.teckzy.msrsilverhouse.Activity.MyOrdersActivity;
import com.teckzy.msrsilverhouse.Activity.WishlistActivity;
import com.teckzy.msrsilverhouse.Interface.onBackPressed;
import com.teckzy.msrsilverhouse.R;

public class ProfileFragment extends Fragment implements onBackPressed
{
    TextView tvMyAddress, tvWishlist, tvMyOrders, tvChangePassword, tvSignOut;
    TextView tvName;
    CardView cvMyAddress, cvWishlist, cvMyOrders, cvChangePassword, cvSignOut;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvName = view.findViewById(R.id.tvName);
        cvMyAddress = view.findViewById(R.id.cvMyAddress);
        tvMyAddress = view.findViewById(R.id.tvMyAddress);
        cvWishlist = view.findViewById(R.id.cvWishlist);
        tvWishlist = view.findViewById(R.id.tvWishlist);
        cvMyOrders = view.findViewById(R.id.cvMyOrders);
        tvMyOrders = view.findViewById(R.id.tvMyOrders);
        cvChangePassword = view.findViewById(R.id.cvChangePassword);
        tvChangePassword = view.findViewById(R.id.tvChangePassword);
        cvSignOut = view.findViewById(R.id.cvSignOut);
        tvSignOut = view.findViewById(R.id.tvSignOut);

        sharedPreferences = getContext().getSharedPreferences("Fateh fruits", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        tvName.setText("Hello," + " " + sharedPreferences.getString("name", ""));

        if (sharedPreferences.getInt("customer_id", 0) == 0) {
            tvSignOut.setText("Sign In");
            cvChangePassword.setVisibility(View.GONE);
            tvName.setText("Hello, Guest");
        }

        cvMyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getInt("customer_id", 0) == 0) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity().getApplicationContext(), MyAddressActivity.class);
                    startActivity(intent);
                }
            }
        });

        cvWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getInt("customer_id", 0) == 0) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity().getApplicationContext(), WishlistActivity.class);
                    startActivity(intent);
                }
            }
        });

        cvMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getInt("customer_id", 0) == 0) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity().getApplicationContext(), MyOrdersActivity.class);
                    startActivity(intent);
                }
            }
        });

        cvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getInt("customer_id", 0) == 0) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity().getApplicationContext(), ChangeNewPasswordActivity.class);
                    intent.putExtra("click","change_password");
                    startActivity(intent);
                }
            }
        });

        cvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getInt("customer_id", 0) == 0) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setMessage("Are you sure you want to Signout?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editor.putInt("customer_id", 0);
                            editor.putString("name", "");
                            editor.putInt("cart_count", 0);
                            editor.putString("mobile_no", "");
                            editor.apply();
                            editor.clear();
                            editor.commit();
                            Toast.makeText(getActivity(), "Logout Successfull !!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
        return view;
    }

    @Override
    public void onBackPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}