package com.kirichko.kirichkorouteplanner.interfaces;

import android.location.Address;
import android.location.Location;

import java.util.ArrayList;

/**
 * Created by Киричко on 26.03.2016.
 */
public interface PresenterListener {
    void addressResolvedA(ArrayList<Address> addresses);
    void addressResolvedB(ArrayList<Address> addresses);
    void routeResolved(ArrayList<Location> nodes);
}
