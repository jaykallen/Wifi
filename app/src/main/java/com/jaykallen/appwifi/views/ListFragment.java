package com.jaykallen.appwifi.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class ListFragment extends Fragment {
    private RecyclerAdapter mRecyclerAdapter;
    private ArrayList<WifiModel> mWifis;
    private WifiModel mWifi;
    public ChangeListener changeListener;

    public interface ChangeListener {
        void listener(WifiModel wifi);
    }

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    private void updateDetail() {
        // Update the detail fragment through the interface.
        changeListener.listener(mWifi);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mWifi = WifiModel.getInstance();
        mWifis = WifiHelper.getWifi(getActivity());
        setupRecycler();
    }

    private void setupRecycler() {
        RecyclerView mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerAdapter = new RecyclerAdapter(mWifis);
        mRecyclerView.setAdapter(mRecyclerAdapter);
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

    private class RecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            mWifi = WifiModel.getInstance();
            mWifi = mWifis.get(position);
            updateDetail();
            mRecyclerAdapter.notifyDataSetChanged();
        }
    }




}
