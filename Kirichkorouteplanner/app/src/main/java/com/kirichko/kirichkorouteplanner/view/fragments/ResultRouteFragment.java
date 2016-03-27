package com.kirichko.kirichkorouteplanner.view.fragments;

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.kirichko.kirichkorouteplanner.interfaces.PresenterDataProvider;
import com.kirichko.kirichkorouteplanner.interfaces.PresenterListener;
import com.kirichko.kirichkorouteplanner.R;
import com.kirichko.kirichkorouteplanner.view.activities.MainActivity;

import java.util.ArrayList;

/**
 * Created by Киричко on 26.03.2016.
 */
public class ResultRouteFragment extends Fragment implements PresenterListener, OnMapReadyCallback, PresenterDataProvider {

    private ArrayList<Location> nodes;
    private TextView editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result_route,
                container, false);
        ((MainActivity)getActivity()).getPresenter().addListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ((MainActivity)getActivity()).getPresenter().setRouteDataProvider(this);

        editText = (TextView) rootView.findViewById(R.id.findornot);

        return rootView;
    }

    @Override
    public void addressResolvedA(ArrayList<Address> addresses) {
    }

    @Override
    public void addressResolvedB(ArrayList<Address> addresses) {
    }

    @Override
    public void routeResolved(ArrayList<Location> nodes) {
        this.nodes = nodes;
        if(getActivity()!=null && nodes !=null && nodes.size()>0) {
            ((MainActivity) getActivity()).getPresenter().setMapRoute();
            editText.setText("Found");
        } else
        {
            if(editText!=null)
            {
                editText.setText("Unknown");
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        ((MainActivity)getActivity()).getPresenter().setRoutesGoogleMap(googleMap);
    }

    @Override
    public String getUserAddressString() {
        return null;
    }

    @Override
    public Address getAddress() {
        return null;
    }

    @Override
    public ArrayList<Location> getNodes() {
        return nodes;
    }
}