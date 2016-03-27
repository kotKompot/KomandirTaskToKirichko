package com.kirichko.kirichkorouteplanner.presenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kirichko.kirichkorouteplanner.interfaces.PresenterDataProvider;
import com.kirichko.kirichkorouteplanner.interfaces.PresenterListener;
import com.kirichko.kirichkorouteplanner.interfaces.WebDataListener;
import com.kirichko.kirichkorouteplanner.model.datastructures.Route;
import com.kirichko.kirichkorouteplanner.util.App;
import com.kirichko.kirichkorouteplanner.web.WebDataProvider;

import java.util.ArrayList;

/**
 * Created by Киричко on 26.03.2016.
 */
public class Presenter implements WebDataListener {

    enum CurrentProcessingAddress { startAddress, endAddress, noAddress }

    private ArrayList<PresenterListener> listeners = new ArrayList<>();
    private PresenterDataProvider startNodeDataProvider;
    private PresenterDataProvider endNodeDataProvider;
    private PresenterDataProvider routeDataProvider;
    private WebDataProvider webDataProvider;
    private CurrentProcessingAddress currentProcessingAddress = CurrentProcessingAddress.noAddress;
    private GoogleMap addressesGoogleMap;
    private GoogleMap routesGoogleMap;
    private Location userCurrentLocation;

    public Presenter()
    {
        webDataProvider = new WebDataProvider(this);
    }

    public void findAddressForA()
    {
        webDataProvider.cancelAllRequest();
        currentProcessingAddress = CurrentProcessingAddress.startAddress;
        webDataProvider.requestAddresses(startNodeDataProvider.getUserAddressString());
    }
    public void findAddressForB()
    {
        webDataProvider.cancelAllRequest();
        currentProcessingAddress = CurrentProcessingAddress.endAddress;
        webDataProvider.requestAddresses(endNodeDataProvider.getUserAddressString());
    }

    public void setMapMarks()
    {
        LatLng start=null;
        String startDescription="";
        LatLng end=null;
        String endDescription="";
        ArrayList<MarkerOptions> markers = new ArrayList<>();
        if(startNodeDataProvider!=null && startNodeDataProvider.getAddress()!= null)
        {
            Address address = startNodeDataProvider.getAddress();
            start = new LatLng(address.getLatitude(), address.getLongitude());
            startDescription = address.getAddressLine(0);
        }
        if(endNodeDataProvider!=null && endNodeDataProvider.getAddress()!= null)
        {
            Address address = endNodeDataProvider.getAddress();
            end = new LatLng(address.getLatitude(), address.getLongitude());
            endDescription = address.getAddressLine(0);
        }

        LatLng latLng = null;

        if(addressesGoogleMap !=null)
        {
            addressesGoogleMap.clear();
            if(start!=null)
            {
                latLng = start;
                MarkerOptions marker = new MarkerOptions().position(start).title(startDescription);
                markers.add(marker);
                addressesGoogleMap.addMarker(marker);
            }
            if(end!=null)
            {
                latLng = end;
                MarkerOptions marker = new MarkerOptions().position(end).title(endDescription);
                markers.add(marker);
                addressesGoogleMap.addMarker(marker);
            }
            if(userCurrentLocation!=null) {
                LatLng userLatLng = new LatLng(userCurrentLocation.getLatitude(), userCurrentLocation.getLongitude());
                MarkerOptions user = new MarkerOptions().position(userLatLng);
                markers.add(user);
            }

            if(start!=null && end!=null) {
                latLng = new LatLng((start.latitude + end.latitude) / 2, (start.longitude + end.longitude) / 2);
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (MarkerOptions marker : markers) {
                    builder.include(marker.getPosition());
                }
                LatLngBounds bounds = builder.build();
                int padding = 100;
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                addressesGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                addressesGoogleMap.animateCamera(cu);
            }else {
                if (latLng != null) {
                    addressesGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }
        }
    }

    public void setMapRoute() {
        if(routesGoogleMap!=null) {
            ArrayList<Location> locations = routeDataProvider.getNodes();
            for(int i = 0; i<locations.size()-1; ++i)
            {
                Polyline line = routesGoogleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(locations.get(i).getLatitude(), locations.get(i).getLongitude()),
                                new LatLng(locations.get(i + 1).getLatitude(), locations.get(i + 1).getLongitude()))
                        .width(5)
                        .color(Color.BLACK));
            }
            LatLng latLng = new LatLng((locations.get(0).getLatitude() + locations.get(locations.size()-1).getLatitude()) / 2,
                    (locations.get(0).getLongitude() + locations.get(locations.size()-1).getLongitude()) / 2);
            LatLngBounds.Builder builder = new LatLngBounds.Builder()
                    ;
            ArrayList<MarkerOptions> markers = new ArrayList<>();
            MarkerOptions markerOptions1 = new MarkerOptions().position(new LatLng(locations.get(0).getLatitude(),locations.get(0).getLongitude()));
            markers.add(markerOptions1);
            MarkerOptions markerOptions2 = new MarkerOptions().position(new LatLng(locations.get(locations.size()-1).getLatitude(), locations.get(locations.size()-1).getLongitude()));
            markers.add(markerOptions2);
            if(userCurrentLocation!=null) {
                LatLng userLatLng = new LatLng(userCurrentLocation.getLatitude(), userCurrentLocation.getLongitude());
                MarkerOptions user = new MarkerOptions().position(userLatLng);
                markers.add(user);
            }

            for (MarkerOptions marker : markers) {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();
            int padding = 100;
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            routesGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            routesGoogleMap.animateCamera(cu);
        }
    }

    public void findRoute()
    {
        if(startNodeDataProvider.getAddress()!=null && endNodeDataProvider.getAddress()!=null) {
            webDataProvider.cancelAllRequest();
            webDataProvider.requestRoute(startNodeDataProvider.getAddress(), endNodeDataProvider.getAddress());
        } else
        {
            Toast toast = Toast.makeText(App.getAppContext(),
                    "Выберите точки отправления и назначения", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void setStartNodeDataProvider(PresenterDataProvider startNodeDataProvider) {
        this.startNodeDataProvider = startNodeDataProvider;
    }

    public void setEndNodeDataProvider(PresenterDataProvider endNodeDataProvider) {
        this.endNodeDataProvider = endNodeDataProvider;
    }

    public void setRouteDataProvider(PresenterDataProvider routeDataProvider) {
        this.routeDataProvider = routeDataProvider;
    }

    public void addListener(PresenterListener presenterListener)
    {
        listeners.add(presenterListener);
    }

    @Override
    public void notifyAboutReceivedAddresses(ArrayList<Address> addresses) {
        for(PresenterListener presenterListener : listeners)
        {
            if(currentProcessingAddress == CurrentProcessingAddress.startAddress) {
                presenterListener.addressResolvedA(addresses);
            }
            if(currentProcessingAddress == CurrentProcessingAddress.endAddress) {
                presenterListener.addressResolvedB(addresses);
            }
        }
        currentProcessingAddress = CurrentProcessingAddress.noAddress;
    }

    @Override
    public void notifyAboutReceivedRoute(Route route) {
        if(route!=null) {
            for (PresenterListener presenterListener : listeners) {
                presenterListener.routeResolved(route.getNodes());
            }
        } else
        {
            //TODO print error
        }
    }

    public void setAddressesGoogleMap(GoogleMap googleMap) {
        this.addressesGoogleMap = googleMap;
        if (ContextCompat.checkSelfPermission(App.getAppContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            addressesGoogleMap.setMyLocationEnabled(true);
            addressesGoogleMap.setOnMyLocationChangeListener(myLocationChangeListener);
        } else
        {
            Toast toast = Toast.makeText(App.getAppContext(),
                    "Вы запретили определять ваше местоположение", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void setRoutesGoogleMap(GoogleMap googleMap) {
        this.routesGoogleMap = googleMap;
        if (ContextCompat.checkSelfPermission(App.getAppContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            routesGoogleMap.setMyLocationEnabled(true);
            routesGoogleMap.setOnMyLocationChangeListener(myLocationChangeListener);
        } else
        {
            Toast toast = Toast.makeText(App.getAppContext(),
                    "Вы запретили определять ваше местоположение", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            userCurrentLocation = location;
        }
    };
}
