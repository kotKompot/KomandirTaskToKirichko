package com.kirichko.kirichkorouteplanner.interfaces;

import android.location.Address;

import com.kirichko.kirichkorouteplanner.model.datastructures.Route;

import java.util.ArrayList;

/**
 * Created by Киричко on 26.03.2016.
 */
public interface WebDataListener {
    void notifyAboutReceivedAddresses(ArrayList<Address> addresses);
    void notifyAboutReceivedRoute(Route route);
}
