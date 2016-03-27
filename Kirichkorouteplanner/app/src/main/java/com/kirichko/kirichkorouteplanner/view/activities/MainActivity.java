package com.kirichko.kirichkorouteplanner.view.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.kirichko.kirichkorouteplanner.interfaces.OnFragmentListener;
import com.kirichko.kirichkorouteplanner.R;
import com.kirichko.kirichkorouteplanner.presenter.Presenter;
import com.kirichko.kirichkorouteplanner.util.App;
import com.kirichko.kirichkorouteplanner.view.fragments.ChooseAddressesFragment;
import com.kirichko.kirichkorouteplanner.view.fragments.HelloFragment;
import com.kirichko.kirichkorouteplanner.view.fragments.ResultRouteFragment;

/**
 * Created by Киричко on 26.03.2016.
 */
public class MainActivity extends FragmentActivity implements OnFragmentListener
{
    private Presenter presenter;

    public Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckEnableGPS();
        presenter = new Presenter();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new HelloFragment(), HelloFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void onTap() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new ChooseAddressesFragment(), ChooseAddressesFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void onFindRouteButtonPressed() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new ResultRouteFragment(), ResultRouteFragment.class.getSimpleName())
                .addToBackStack(null)
                .commit();
        presenter.findRoute();
    }

    private void CheckEnableGPS(){
        String provider = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.equals("")){
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Заметка")
                    .setMessage("Для отображения вашего расположения на картах в приложении необходимо активировать геоданные. Перейти в настройки?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }

    }
}
