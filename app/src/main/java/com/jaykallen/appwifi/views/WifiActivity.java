package com.jaykallen.appwifi.views;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jaykallen.appwifi.R;
import com.jaykallen.appwifi.models.WifiModel;
import com.jaykallen.appwifi.utils.EventbusTrigger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import timber.log.Timber;

// 6-5-2017: Basic test of fragments and what happens in the lifecycle during the launching / quitting.
// I guess you can't control a button from the fragment, you have to program it from the activity.
// This is NOT a bulletproof app, I never added runtime permissions for the Wifi control (not enough time)

public class WifiActivity extends FragmentActivity {
    ListFragment listFragment;
    DetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag);
        Timber.d("*********** Wifi Activity Started **********");
        if (checkLocationPermission()) {
            setupFragments();
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventbusTrigger event) {
        dialogOk(event.getMessage());
        Timber.d("Event Bus has reported that the connection is established");
    }

    private void setupFragments() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        listFragment = new ListFragment();
        detailFragment = new DetailFragment();
        transaction.add(R.id.list_fragment, listFragment);
        transaction.add(R.id.detail_fragment, detailFragment);
        transaction.commit();
        setupListener();
    }

    private void setupListener() {
        listFragment.setChangeListener(new ListFragment.ChangeListener() {
            @Override
            public void listener(WifiModel wifi) {
                detailFragment.updateDetail(wifi);
            }
        });
    }

    private boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 99);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            dialogOk("Please restart the app");
        }
    }

    private void dialogOk (String msg) {
        final Dialog dialog = new Dialog(this, R.style.DialogStyle);
        dialog.setContentView(R.layout.dialog_ok);
        dialog.setTitle("System Notice");
        TextView message = (TextView) dialog.findViewById(R.id.message);
        message.setText(msg);
        Button buttonOk = (Button) dialog.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

}
