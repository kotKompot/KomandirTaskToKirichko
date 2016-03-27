package com.kirichko.kirichkorouteplanner.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kirichko.kirichkorouteplanner.interfaces.OnFragmentListener;
import com.kirichko.kirichkorouteplanner.R;


/**
 * Created by Киричко on 26.03.2016.
 */
public class HelloFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hello,
                container, false);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OnFragmentListener)getActivity()).onTap();
            }
        });
        return rootView;
    }
}