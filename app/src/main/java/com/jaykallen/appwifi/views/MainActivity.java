package com.jaykallen.appwifi.views;
// Created by Jay Kallen on 11/11/2017. 

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaykallen.appwifi.R;
import com.jaykallen.appwifi.models.WifiModel;
import com.jaykallen.appwifi.utils.WifiHelper;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

    // 2017-11-11: First I'm going to build this in a regular activity and then transfer the logic over to Fragments.

public class MainActivity extends AppCompatActivity {
    private RecyclerAdapter mRecyclerAdapter;
    private ArrayList<WifiModel> mWifis = new ArrayList<>();
    private WifiModel mWifi;
    private TextView mSsidDetail, mBssidDetail, mLevelDetail, mFreqDetail, mTimeDetail, mCapabilityDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // I could have used Butter Knife or Kotlin Extensions (in Kotlin obviously), but it was only a few fields...
        mSsidDetail = findViewById(R.id.ssid_detail_textview);
        mBssidDetail = findViewById(R.id.bssid_detail_textview);
        mLevelDetail = findViewById(R.id.level_detail_textview);
        mFreqDetail = findViewById(R.id.freq_detail_textview);
        mTimeDetail = findViewById(R.id.timestamp_detail_textview);
        mCapabilityDetail = findViewById(R.id.capabilities_detail_textview);
        Timber.d("**************** Main Activity *****************");
        // This sets up the Singleton through the static instance.  Or it can be done with Dagger 2 without dependencies.
        mWifi = WifiModel.getInstance();
        // I'm surprised that I didn't need RxJava or a Callback to update the list of WifiModels.
        mWifis = WifiHelper.getWifi(this);
        if (mWifis.size() > 0) {
            updateDetail(mWifis.get(0));
        }
        setupRecyclerView();
    }

    private void updateDetail(WifiModel wifi) {
        // Normally I would put the static text into the strings.xml and call it from there so we could switch languages.
        mSsidDetail.setText("SSID: " + wifi.getSsid());
        mBssidDetail.setText("BSSID: " + wifi.getBssid());
        mLevelDetail.setText("Level: " + wifi.getLevel());
        mFreqDetail.setText("Freq: " + wifi.getFrequency());
        mTimeDetail.setText("Time: " + wifi.getTimestamp());
        mCapabilityDetail.setText("Capabilities: " + wifi.getCapabilities());
    }

    private void setupRecyclerView() {
        if (mWifis.size() > 0) {
            RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerAdapter = new RecyclerAdapter(mWifis);
            mRecyclerView.setAdapter(mRecyclerAdapter);
        } else {
            Timber.d("Nothing found in the list for the recycler view");
        }
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerHolder> {
        private List<WifiModel> wifis;
        public RecyclerAdapter(List<WifiModel> list) {
            wifis = list;
        }
        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_select_wifi, parent, false);
            return new RecyclerHolder(view);
        }
        @Override
        public void onBindViewHolder(RecyclerHolder recyclerholder, int position) {
            // Some say logic like this should be moved to the ViewModel to be a true MVVM
            WifiModel item = wifis.get(position);
            if (item.isSelected()) {
                recyclerholder.itemView.setBackgroundColor(getResources().getColor(R.color.colorYellow));
            } else {
                recyclerholder.itemView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            }
            recyclerholder.bindRecyclerData(item);
        }
        @Override
        public int getItemCount(){
            return wifis.size();
        }
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView ssidTextView, bssidTextView, levelTextView, freqTextView;
        private WifiModel wifi;

        public RecyclerHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            // I could have used Butter Knife or Kotlin Extensions (in Kotlin obviously), but it was only a few fields...
            ssidTextView = (TextView) itemView.findViewById(R.id.ssid_textview);
            bssidTextView = (TextView) itemView.findViewById(R.id.bssid_textview);
            levelTextView = (TextView) itemView.findViewById(R.id.level_textview);
            freqTextView = (TextView) itemView.findViewById(R.id.freq_textview);
        }
        public void bindRecyclerData(WifiModel listItem) {
            wifi = listItem;
            ssidTextView.setText(wifi.getSsid());
            bssidTextView.setText(wifi.getBssid());
            // Normally I would put the static text into the strings.xml and call it from there so we could switch languages.
            levelTextView.setText("Level: " + wifi.getLevel());
            freqTextView.setText("Freq: " + wifi.getFrequency());
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            // Set all the other items to back to white
            for (int i=0; i<mWifis.size(); i++) {
                mWifis.get(i).setSelected(false);
            }
            mWifis.get(position).setSelected(true);
            updateDetail(mWifis.get(position));
            mRecyclerAdapter.notifyDataSetChanged();
        }
    }




}
