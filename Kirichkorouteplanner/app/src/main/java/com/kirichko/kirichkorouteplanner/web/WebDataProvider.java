package com.kirichko.kirichkorouteplanner.web;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.kirichko.kirichkorouteplanner.interfaces.WebDataListener;
import com.kirichko.kirichkorouteplanner.model.datastructures.Route;
import com.kirichko.kirichkorouteplanner.util.App;
import com.kirichko.kirichkorouteplanner.util.DataConverter;
import com.kirichko.kirichkorouteplanner.util.URLRequestStringBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Киричко on 26.03.2016.
 * подключил библиотеку com.loopj.android:android-async-http:1.4.9 думал понадобиться для запроса geocoder,
 * но как оказалось у android есть специальный класс для этого.
 * Использую AsyncTask так как активности
 * и фрагменты он сам не обновляет, поэтому пойдет.
 * Чтобы при каждом вводе новового символа не запускать новый geocoder, так как он лезет в интернет и
 * это все может быть долго и затратно, я добавил ожидание в 1 секунду, чтобы geocoder начинал работать только
 * через секунду после последнего изменнеия текста
 */
public class WebDataProvider {
    GetAddressesTask currentGetAddressesTask;

    private AsyncHttpClient client;
    public WebDataProvider(WebDataListener webDataListener)
    {
        this.client = new AsyncHttpClient();
        listeners.add(webDataListener);
    }

    private ArrayList<WebDataListener> listeners = new ArrayList<>();

    public void requestAddresses(String userAddressString)
    {
        if(currentGetAddressesTask!=null) {
            currentGetAddressesTask.cancel(true);
        }
        currentGetAddressesTask = new GetAddressesTask(listeners.get(0));
        currentGetAddressesTask.execute(userAddressString);
    }

    public void cancelAllRequest()
    {
        if(client!= null) client.cancelAllRequests(true);
    }

    class GetAddressesTask extends AsyncTask<String, Void, List<Address>> {
        WebDataListener webDataListener;

        public GetAddressesTask(WebDataListener webDataListener)
        {
            this.webDataListener = webDataListener;
        }
        @Override
        protected List<Address> doInBackground(String... params) {
            long startTime  = System.currentTimeMillis();
            while(System.currentTimeMillis()- startTime < 1000 && !isCancelled());
            if(isCancelled() || this!=currentGetAddressesTask) return null;

            List<Address> addresses = new ArrayList<>();
            Geocoder geocoder;
            try {
                geocoder = new Geocoder(App.getAppContext());
                addresses = geocoder.getFromLocationName(params[0], 7);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> result) {
            super.onPostExecute(result);
            if(result != null) {
                webDataListener.notifyAboutReceivedAddresses(new ArrayList<>(result));
            }
        }
    }

    public void requestRoute(Address addressA, Address addressB)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URLRequestStringBuilder.getURLForDirections(addressA,addressB), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
            listeners.get(0).notifyAboutReceivedRoute(DataConverter.parseJsonToRoute(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                listeners.get(0).notifyAboutReceivedRoute(null);
            }
        });
    }
}
