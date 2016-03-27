package com.kirichko.kirichkorouteplanner.model.datastructures;

import android.location.Address;
import android.location.Location;

import java.util.ArrayList;

/**
 * Created by Киричко on 26.03.2016.
 */
public class Route {
    private Address addressA;
    private Address addressB;
    private ArrayList<Location> nodes = new ArrayList<>();

    public ArrayList<Location> getNodes() {
        return nodes;
    }

    public void addLocationToRoute(Location location)
    {
        nodes.add(location);
    }
}
