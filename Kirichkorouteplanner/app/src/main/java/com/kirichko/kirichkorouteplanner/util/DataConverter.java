package com.kirichko.kirichkorouteplanner.util;

import android.location.Address;
import android.location.Location;
import android.location.LocationProvider;

import com.kirichko.kirichkorouteplanner.model.datastructures.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Киричко on 27.03.2016.
 */
public class DataConverter {
    public static String[] addressesToStrings(ArrayList<Address> addresses)
    {
        String[] addressesString = new String[addresses.size()];
        int i = 0;
        for(Address address : addresses)
        {
            String s ="";
            for(int j = 0; j< address.getMaxAddressLineIndex(); j++)
            {
                s = s+" " + address.getAddressLine(j);
            }
            addressesString[i] = s;
            i++;
        }
        return addressesString;
    }

    public static Route parseJsonToRoute(JSONObject jsonObject)
    {
        Route route = new Route();
        try {
            JSONArray jsonArrayRoutes = jsonObject.getJSONArray("routes");
            JSONObject jsonObject1 = jsonArrayRoutes.getJSONObject(0);
            JSONArray jsonArrayLegs = jsonObject1.getJSONArray("legs");
            JSONArray jsonArraySteps = jsonArrayLegs.getJSONObject(0).getJSONArray("steps");
            for(int i = 0; i<jsonArraySteps.length(); ++i)
            {
                Location location = new Location("");
                location.setLatitude(Double.valueOf(jsonArraySteps.getJSONObject(i).getJSONObject("start_location").getString("lat")));
                location.setLongitude(Double.valueOf(jsonArraySteps.getJSONObject(i).getJSONObject("start_location").getString("lng")));
                route.addLocationToRoute(location);

                Location location2 = new Location("");
                location2.setLatitude(Double.valueOf(jsonArraySteps.getJSONObject(i).getJSONObject("end_location").getString("lat")));
                location2.setLongitude(Double.valueOf(jsonArraySteps.getJSONObject(i).getJSONObject("end_location").getString("lng")));
                route.addLocationToRoute(location2);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return route;
    }
}
