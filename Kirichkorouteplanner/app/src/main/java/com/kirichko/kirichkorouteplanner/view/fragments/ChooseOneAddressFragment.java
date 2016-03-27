package com.kirichko.kirichkorouteplanner.view.fragments;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.kirichko.kirichkorouteplanner.interfaces.PresenterDataProvider;
import com.kirichko.kirichkorouteplanner.interfaces.PresenterListener;
import com.kirichko.kirichkorouteplanner.R;
import com.kirichko.kirichkorouteplanner.presenter.Presenter;
import com.kirichko.kirichkorouteplanner.util.DataConverter;
import com.kirichko.kirichkorouteplanner.view.activities.MainActivity;

import java.util.ArrayList;

/**
 * Created by Киричко on 26.03.2016.
 */
public class ChooseOneAddressFragment extends ListFragment implements PresenterListener, PresenterDataProvider {

    public static final String RIGHT_OR_LEFT_FRAGMENT = "fromTabFragment";

    //Если true, то это фрагмент выбора начальной точки маршрута, если false, то конечной.
    private boolean fromTabFragment;
    private Address chosenAddress;
    private String userAddressToResolve;
    private Presenter presenter;
    private ArrayList<Address> currentAddresses;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fromTabFragment = getArguments().getBoolean(RIGHT_OR_LEFT_FRAGMENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chooseoneaddress,
                container, false);

            presenter = ((MainActivity)getActivity()).getPresenter();

        if(fromTabFragment)
        {
            presenter.setStartNodeDataProvider(this);
        } else
        {
            presenter.setEndNodeDataProvider(this);
        }
        presenter.addListener(this);

        ((EditText)rootView.findViewById(R.id.editText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                userAddressToResolve = editable.toString();
                if (fromTabFragment) {
                    presenter.findAddressForA();
                } else {
                    presenter.findAddressForB();
                }
            }
        });

        return rootView;
    }

    private void setAddressesLisrView(String data[])
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.adapter_addresses_row, data);
        setListAdapter(adapter);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        chosenAddress = currentAddresses.get(position);
        presenter.setMapMarks();

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void addressResolvedA(ArrayList<Address> addresses) {
        if(addresses!=null && addresses.size()>0) {
            if (fromTabFragment) {
                currentAddresses = addresses;
                setAddressesLisrView(DataConverter.addressesToStrings(addresses));
            }
        } else
        {

        }
    }

    @Override
    public void addressResolvedB(ArrayList<Address> addresses) {
        if(addresses!=null && addresses.size()>0) {
            if (!fromTabFragment) {
                currentAddresses = addresses;
                setAddressesLisrView(DataConverter.addressesToStrings(addresses));
            }
        } else
        {

        }
    }

    @Override
    public void routeResolved(ArrayList<Location> nodes) {
    }

    @Override
    public String getUserAddressString() {
        return userAddressToResolve;
    }

    @Override
    public Address getAddress() {
        return chosenAddress;
    }

    @Override
    public ArrayList<Location> getNodes() {
        return null;
    }
}