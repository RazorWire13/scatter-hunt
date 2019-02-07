package com.dmuench.scatterhunt;


import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
    private List<Goal> goalObjects;
    private boolean[] completeGoals;
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

        goalObjects = new ArrayList<>();
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
                        goalObjects.add(goal);
                        latitudes[finalI] = goal.getLatitude();
                        longitudes[finalI] = goal.getLongitude();

                        String title = goal.getTitle();
                        String clueOne = goal.getClueOne();
                        String clueTwo = goal.getClueTwo();
                        String clueThree = goal.getClueThree();

                        addItem(title, new String[]{clueOne, clueTwo, clueThree}, finalI);

                        if (finalI == goals.length - 1) {
                            completeGoals = new boolean[goalObjects.size()];
                            new Thread(locationRun).start();
                        }
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
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < goalObjects.size(); i++) {
                                if (!completeGoals[i]) {
                                    double distance = DeltaLatLong.distance(location.getLatitude(), location.getLongitude(), latitudes[i], longitudes[i], "km");
                                    DecimalFormat df = new DecimalFormat("#.##");
                                    if (distance >= 1) {
                                        Log.i("DISTANCE", "kilometers to " + goalObjects.get(i).getTitle() + ": " + distance);
                                        if (getView() != null) {
                                            TextView textView = getView().findViewById(goalIds[i]);
                                            textView.setText("Distance to goal: " + df.format(distance) + " km");
                                        }
                                    } else if (distance < 1 && distance * 1000 > 5) {
                                        Log.i("DISTANCE", "meters to " + goalObjects.get(i).getTitle() + ": " + distance * 1000);
                                        if (getView() != null) {
                                            TextView textView = getView().findViewById(goalIds[i]);
                                            textView.setText("Distance to goal: " + df.format(distance * 1000) + " m");
                                        }
                                    } else {
                                        if (getView() != null) {
                                            Goal goal = goalObjects.get(i);
                                            TextView textView = getView().findViewById(goalIds[i]);
                                            textView.setText(goal.getTitle() + " - Completed");
                                            completeGoals[i] = !completeGoals[i];
                                        }
                                    }
                                }
                            }
                        }
                    });
                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    break;
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
