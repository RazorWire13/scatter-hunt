package com.dmuench.scatterhunt;


import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.dmuench.scatterhunt.models.Goal;
import com.dmuench.scatterhunt.tools.DeltaLatLong;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayFragment extends Fragment {

    private ExpandingList expandingList;
    private String[] goals;
    private Location location;
    private double[] latitudes;
    private double[] longitudes;
    private int[] goalIds;

    public PlayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play, container, false);
        expandingList = view.findViewById(R.id.expanding_list_main);

        final LocationRun locationRun = new LocationRun();


        Bundle state = getArguments();
        latitudes = new double[3];
        longitudes = new double[3];
        goalIds = new int[]{R.id.goal1, R.id.goal2, R.id.goal3};

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        goals = new String[]{state.getString("goalOne"), state.getString("goalTwo"), state.getString("goalThree")};

        for (int i = 0; i < goals.length; i++) {
            if (goals[i] != null) {
                final int finalI = i;
                db.collection("Goals").document(goals[i]).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Goal goal = documentSnapshot.toObject(Goal.class);
                        latitudes[finalI] = goal.getLatitude();
                        longitudes[finalI] = goal.getLongitude();

                        String title = goal.getTitle();
                        String clueOne = goal.getClueOne();
                        String clueTwo = goal.getClueTwo();
                        String clueThree = goal.getClueThree();

                        addItem(title, new String[]{clueOne, clueTwo, clueThree}, finalI);

                        if (finalI == goals.length - 1) new Thread(locationRun).start();
                    }
                });
            }
        }

        return view;
    }

    class LocationRun implements Runnable {
        public void run() {
            while(true) {
                location = MainActivity.location;

                // Must Have Because: Only the original thread that created a view hierarchy can touch its views.
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 3; i++) {
                            double distance = DeltaLatLong.distance(location.getLatitude(), location.getLongitude(), latitudes[i], longitudes[i], "km");

                            TextView textView = getView().findViewById(goalIds[i]);
                            DecimalFormat df = new DecimalFormat("#.##");
                            textView.setText("Distance to goal: " + df.format(distance) + " km");

                        }
                    }
                });
                System.out.println("Lat: " + Double.toString(location.getLatitude()));
                System.out.println("Long: " + Double.toString(location.getLongitude()));
                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addItem(String title, String[] subItems, int index) {
        //Let's create an item with R.layout.expanding_layout
        ExpandingItem item = expandingList.createNewItem(R.layout.expanding_layout);

        //If item creation is successful, let's configure it
        TextView textView = item.findViewById(R.id.title);
        textView.setText(title);
        textView.setId(goalIds[index]);

        //This will create 3 items
        item.createSubItems(3);
        item.setIndicatorColorRes(R.color.black);
        item.setIndicatorIconRes(R.drawable.goal_expander_icon);

//get a sub item View
        View subItemZero = item.getSubItemView(0);
        ((TextView) subItemZero.findViewById(R.id.sub_title)).setText(subItems[0]);

        View subItemOne = item.getSubItemView(1);
        ((TextView) subItemOne.findViewById(R.id.sub_title)).setText(subItems[1]);

        View subItemTwo = item.getSubItemView(2);
        ((TextView) subItemTwo.findViewById(R.id.sub_title)).setText(subItems[2]);

    }


}
