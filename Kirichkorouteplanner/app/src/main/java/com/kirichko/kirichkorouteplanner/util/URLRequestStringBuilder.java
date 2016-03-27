package com.kirichko.kirichkorouteplanner.util;

import android.app.Application;
import android.location.Address;

import com.kirichko.kirichkorouteplanner.R;

/**
 * Created by Киричко on 27.03.2016.
 * https://maps.googleapis.com/maps/api/directions/json?origin=55.929336,37.517369&destination=55.923600,37.535549
 */
public class URLRequestStringBuilder {
    private static final String GOOGLE_MAPS_DIRECTIONS_API_URL = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String PARAM_ORIGIN = "origin=";
    private static final String PARAM_DESTINATION = "destination=";
    public static String getURLForDirections(Address addressA, Address addressB)
    {
        String latitudeA = String.valueOf(addressA.getLatitude());
        String latitudeB = String.valueOf(addressB.getLatitude());
        String longitudeA = String.valueOf(addressA.getLongitude());
        String longitudeB = String.valueOf(addressB.getLongitude());
        String s = GOOGLE_MAPS_DIRECTIONS_API_URL + PARAM_ORIGIN + latitudeA+"," +longitudeA + "&"+
                   PARAM_DESTINATION + latitudeB+"," +longitudeB;
        return s;
    }
}
