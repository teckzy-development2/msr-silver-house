package com.teckzy.msrsilverhouse.SubFragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.teckzy.msrsilverhouse.R;

public class NeedHelpFragment extends Fragment {
    ImageView whatsapp1;
    TextView whatsappnum1;
    String url;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_needhelp, container, false);
        whatsapp1 = view.findViewById(R.id.whatsapp1);
        whatsappnum1 = view.findViewById(R.id.whatsappnum1);

        whatsapp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //whatsapp1();
            }
        });

        whatsappnum1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //whatsapp1();
            }
        });

        return view;
    }

    public void whatsapp1() {
        try {
            url = "https://wa.me/message/RVAUAIP4MLEOB1";
            PackageManager pm = getActivity().getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}