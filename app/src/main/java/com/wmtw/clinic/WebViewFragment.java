package com.wmtw.clinic;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class WebViewFragment extends Fragment {
    private static final String ITEM_ID = "item_id";
    private int mItemId;

    public WebViewFragment() {
        // Required empty public constructor
    }

    public static WebViewFragment newInstance(int itemId) {
        WebViewFragment fragment = new WebViewFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_view_web, container, false);
        TextView content = (TextView) rootview.findViewById(R.id.content);

        switch (mItemId) {
            case R.id.nav_results:
                content.setText("My results");
                break;
            case R.id.nav_about:
                content.setText("About");
                break;
            case R.id.nav_faq:
                content.setText("FAQ");
                break;
            case R.id.nav_contact:
                content.setText("Contact");
                break;
        }

        return rootview;
    }


}
