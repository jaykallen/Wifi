package com.jaykallen.appwifi.views;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jaykallen.appwifi.R;
import com.jaykallen.appwifi.models.WifiModel;
import com.jaykallen.appwifi.utils.WifiHelper;

import timber.log.Timber;


public class DetailFragment extends Fragment implements View.OnClickListener {
    private TextView mSsidDetail, mBssidDetail, mLevelDetail, mFreqDetail, mTimeDetail, mCapabilityDetail;
    private EditText mUsername, mPassword;
    private Button mJoinButton;
    private String mSsid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        // I could have used Butter Knife or Kotlin Extensions (in Kotlin obviously), but it was only a few fields...
        mSsidDetail = view.findViewById(R.id.ssid_detail_textview);
        mBssidDetail = view.findViewById(R.id.bssid_detail_textview);
        mLevelDetail = view.findViewById(R.id.level_detail_textview);
        mFreqDetail = view.findViewById(R.id.freq_detail_textview);
        mTimeDetail = view.findViewById(R.id.timestamp_detail_textview);
        mCapabilityDetail = view.findViewById(R.id.capabilities_detail_textview);
        mJoinButton = (Button) view.findViewById(R.id.join_button);
        mPassword = view.findViewById(R.id.password_edit);
        mJoinButton.setOnClickListener(this);
        return view;
    }

    public void updateDetail(WifiModel wifi) {
        // Normally I would put the static text into the strings.xml and call it from there so we could switch languages.
        Timber.d("Wifi model received from interface");
        mSsidDetail.setText("SSID: " + wifi.getSsid());
        mSsid = wifi.getSsid();
        mBssidDetail.setText("BSSID: " + wifi.getBssid());
        mLevelDetail.setText("Level: " + wifi.getLevel());
        mFreqDetail.setText("Freq: " + wifi.getFrequency());
        mTimeDetail.setText("Time: " + wifi.getTimestamp());
        mCapabilityDetail.setText("Capabilities: " + wifi.getCapabilities());
    }

    @Override
    public void onClick(View v) {
        Timber.d("Join button clicked");
        WifiHelper.WifiConnect(getActivity(), mSsid, mPassword.getText().toString());
    }


}
