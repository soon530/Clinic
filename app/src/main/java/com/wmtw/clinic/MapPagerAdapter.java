package com.wmtw.clinic;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class MapPagerAdapter extends PagerAdapter {
    private ArrayList<Clinic.ClinicEntity> items;
    private LayoutInflater inflater;
    private MapFragment mapFragment;

    Button.OnClickListener storeTelListener = new ImageButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            ArrayList<?> arraylist = (ArrayList<?>) items;
            Clinic.ClinicEntity store = (Clinic.ClinicEntity) arraylist.get(v.getId());
            Common.showCallPhoneAlert(mapFragment.getContext(), store.getCompany(), store.getPhone());
        }
    };

    Button.OnClickListener storeMapListener = new ImageButton.OnClickListener() {

        @Override
        public void onClick(View v) {
            ArrayList<?> arraylist = (ArrayList<?>) items;
            Clinic.ClinicEntity store = (Clinic.ClinicEntity) arraylist.get(v.getId());

            double savedLat = Double.parseDouble(store.getLat());
            double savedLng = Double.parseDouble(store.getLng());
            LatLng cameraLatLng = new LatLng(savedLat, savedLng);

            mapFragment.showLocation(cameraLatLng);
        }
    };

    public MapPagerAdapter(MapFragment mapFragment, ArrayList<Clinic.ClinicEntity> items) {
        super();
        this.items = items;
        this.inflater = LayoutInflater.from(mapFragment.getContext());
        this.mapFragment = mapFragment;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        ViewPager pager = (ViewPager) container;

        View view = null;
        view = this.inflater.inflate(R.layout.item_mappager, null);
        Clinic.ClinicEntity store = (Clinic.ClinicEntity) items.get(position);
        view = this.inflater.inflate(R.layout.item_mappager, null);
        TextView titleTextView = (TextView) view.findViewById(R.id.textView_title);
        TextView addTextView = (TextView) view.findViewById(R.id.textView_addr);
        TextView kmTextView = (TextView) view.findViewById(R.id.textView_km);
        TextView telTextView = (TextView) view.findViewById(R.id.textView_tel);
        RelativeLayout telButton = (RelativeLayout) view.findViewById(R.id.button_tel);
        RelativeLayout gpsButton = (RelativeLayout) view.findViewById(R.id.button_gps);
        telButton.setId(position);
        gpsButton.setId(position);
        telButton.setOnClickListener(storeTelListener);
        gpsButton.setOnClickListener(storeMapListener);

        titleTextView.setText(store.getCompany());
        addTextView.setText(store.getAddress());
        telTextView.setText(store.getPhone());
        //String showDistance = Common.showDistance(store.getDistance());
        kmTextView.setText("");

        pager.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view == ((View) object);
    }
}
