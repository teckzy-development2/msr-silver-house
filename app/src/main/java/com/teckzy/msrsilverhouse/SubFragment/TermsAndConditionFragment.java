package com.teckzy.msrsilverhouse.SubFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.teckzy.msrsilverhouse.R;

public class TermsAndConditionFragment extends Fragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_termsandcondition, container, false);
        return view;
    }
}