package com.kirichko.kirichkorouteplanner.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.kirichko.kirichkorouteplanner.R;
import com.kirichko.kirichkorouteplanner.view.activities.MainActivity;


/**
 * Created by Киричко on 26.03.2016.
 */
public class ChooseAddressesFragment extends Fragment implements OnMapReadyCallback {

    private FragmentTabHost mTabHost;
    private GoogleMap googleMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chooseaddress,
                container, false);

        Bundle bundleA = new Bundle();
        Bundle bundleB = new Bundle();
        bundleA.putBoolean(ChooseOneAddressFragment.RIGHT_OR_LEFT_FRAGMENT, true);
        bundleB.putBoolean(ChooseOneAddressFragment.RIGHT_OR_LEFT_FRAGMENT, false);

        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("fragmenta").setIndicator("Откуда"),
                ChooseOneAddressFragment.class, bundleA);
        mTabHost.addTab(mTabHost.newTabSpec("fragmentb").setIndicator("Куда"),
                ChooseOneAddressFragment.class, bundleB);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if(googleMap==null) {
            mapFragment.getMapAsync(this);
        }else
        {
            ((MainActivity)getActivity()).getPresenter().setAddressesGoogleMap(googleMap);
        }


        rootView.findViewById(R.id.find_route_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).onFindRouteButtonPressed();
            }
        });

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        ((MainActivity)getActivity()).getPresenter().setAddressesGoogleMap(googleMap);
    }
}