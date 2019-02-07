package com.dmuench.scatterhunt;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayFragment extends Fragment {

    private ExpandingList expandingList;

    public PlayFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play, container, false);

//        // this is receiving the bundle from the previous fragment
//        String[] goalIds = getArguments().getStringArray("goalIds");

        expandingList = view.findViewById(R.id.expanding_list_main);

//        ExpandingItem item = expandingList.createNewItem(R.layout.expanding_layout);

/*ExpandingItem extends from View, so you can call
findViewById to get any View inside the layout*/


        addItem("Goal", new String[]{"Hint1", "Hint2", "Hint3"}, "3");
        addItem("Goal2", new String[]{"Hint1", "Hint2", "Hint3"}, "5");






        return view;

    }

    private void addItem(String title, String[] subItems, String distance) {
        //Let's create an item with R.layout.expanding_layout
        ExpandingItem item = expandingList.createNewItem(R.layout.expanding_layout);

        //If item creation is successful, let's configure it
        TextView textView = item.findViewById(R.id.title);
        textView.setText(title);

        //This will create 3 items
        item.createSubItems(4);
        item.setIndicatorColorRes(R.color.black);
        item.setIndicatorIconRes(R.drawable.goal_expander_icon);

//get a sub item View
        View subItemZero = item.getSubItemView(0);
        ((TextView) subItemZero.findViewById(R.id.sub_title)).setText(subItems[0]);

        View subItemOne = item.getSubItemView(1);
        ((TextView) subItemOne.findViewById(R.id.sub_title)).setText(subItems[1]);

        View subItemTwo = item.getSubItemView(2);
        ((TextView) subItemTwo.findViewById(R.id.sub_title)).setText(subItems[2]);

        View subItemThree = item.getSubItemView(3);
        ((TextView) subItemThree.findViewById(R.id.sub_title)).setText(distance);

    }





    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit == "K") {
                dist = dist * 1.609344;
            }
            return (dist);
        }
    }



}
