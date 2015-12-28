package com.wmtw.clinic;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;


public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener {
    private static final String ITEM_ID = "item_id";
    MapView mapView;
    GoogleMap googleMap;
    public Marker marker;
    MarkerOptions minMarkerOptions = null;
    int zoomLevel = 15;

    private int mItemId;
    private List<Clinic.ClinicEntity> mClinics;

    public ViewPager mViewPager;
    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            if (marker != null) {
                marker.remove();
            }

            MarkerOptions m = (MarkerOptions) Common.markAry.get(arg0);
            marker = googleMap.addMarker(m);
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_benefit_current));
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(m.getPosition()), 500, null);
        }
    };
    private PagerAdapter mMapPagerAdapter;


    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(int itemId) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putInt(ITEM_ID, itemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mItemId = getArguments().getInt(ITEM_ID);
        }

        try {
            Common.markAry = new ArrayList<Object>();
            Common.clinicArray = new ArrayList<Clinic.ClinicEntity>();
            mMapPagerAdapter = new MapPagerAdapter(this, Common.clinicArray);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setOnPageChangeListener(changeListener);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) rootView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        googleMap = mapView.getMap();
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());

        // Updates the location and zoom of the MapView
        //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(22.66, 120.35), 10);
        //googleMap.animateCamera(cameraUpdate);

        googleMap.setOnMarkerClickListener(this);

        Common.setUserLocation(getContext());

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        fakeRequestSendSMS();
        super.onResume();
    }

    private void fakeRequestSendSMS() {
        String jsonData = null;
        try {
            jsonData = ServerApis.parseResource(getActivity(), R.raw.clinic);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ServerApis.asyncPostFake(jsonData, Clinic.class);
    }

    @DebugLog
    public void onEventMainThread(Clinic clinic) {
        mClinics = clinic.getClinic();

        loadStoreInfo();
        loadStroeMap();
    }

    private void loadStoreInfo() {
        MarkerOptions m = null;

        if (Common.isNetwork(getContext())) {

            double minDistance = 0;
            for (Clinic.ClinicEntity store : mClinics) {
                double lat = Double.parseDouble(store.getLat());
                double lng = Double.parseDouble(store.getLng());


                LatLng localation = new LatLng(lat, lng);


                int imgID = 0;
                imgID = R.drawable.pin;

                double distance = 0;
                if (Common.loc[0] != 0 && Common.loc[1] != 0) {
                    distance = Math.sqrt((Common.loc[0] - lat)
                            * (Common.loc[0] - lat) + (Common.loc[1] - lng)
                            * (Common.loc[1] - lng)) * 111.1;
                    if (minDistance == 0) {
                        minDistance = distance;
                    }

                    if (distance < minDistance) {
                        minDistance = distance;
                    }
                }

                Common.clinicArray.add(store);
                m = new MarkerOptions().position(localation)
                        .title(store.getCompany()).snippet(store.getAddress())
                        .icon(BitmapDescriptorFactory.fromResource(imgID));
                //設定最近距離pin
                if (distance <= minDistance) {
                    minMarkerOptions = m;
                }
                Common.markAry.add(m);
            }

            if (minDistance > 0 && minDistance < 0.7) {
                zoomLevel = 14;
            } else if (minDistance >= 0.7 && minDistance <= 3) {
                zoomLevel = 13;
            } else if (minDistance > 3 && minDistance <= 6.3) {
                zoomLevel = 12;
            } else if (minDistance > 6.3 && minDistance <= 7) {
                zoomLevel = 11;
            } else {
                zoomLevel = 7;
            }


        }

    }

    private void loadStroeMap() {
        if (!Common.isNetwork(getContext())) {
            Common.alert(getContext(), R.string.no_network_connection, R.string.remind_you);
        } else {
            mViewPager.setAdapter(mMapPagerAdapter);

            if (mClinics.size() == 0) {
                Common.alert(getContext(), R.string.nostoreMsg2, R.string.nostoreMsg1);
            } else {
                for (int i = 0; i < Common.markAry.size(); i++) {
                    MarkerOptions m = (MarkerOptions) Common.markAry.get(i);
                    if (m != null)
                        googleMap.addMarker(m);
                }

                if (minMarkerOptions != null) {
                    marker = googleMap.addMarker(minMarkerOptions);
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_benefit_current));
                }
            }


            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(
                    Common.loc[0], Common.loc[1])));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Common.loc[0], Common.loc[1])
                    , (float) zoomLevel));

        }

    }

    public void showLocation(LatLng localation) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + Common.loc[0] + "," + Common.loc[1] + "&daddr=" + localation.latitude + "," + localation.longitude));
        startActivity(intent);
    }


    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for (int i = 0; i < Common.markAry.size(); i++) {
            MarkerOptions m = (MarkerOptions) Common.markAry.get(i);
            if (m.getTitle().equals(marker.getTitle())) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        return true;
    }
}
