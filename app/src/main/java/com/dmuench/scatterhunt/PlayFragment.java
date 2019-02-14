package com.dmuench.scatterhunt;


import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.TypedValue;
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
    private Bundle state;
    private ExpandingList expandingList;
    private String[] goals;
    private Goal[] goalObjects;
    private Long[] completeGoals;
    private Location location;
    private double[] latitudes;
    private double[] longitudes;
    private int[] goalIds;
    private int numberOfGoals;

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
        state = getArguments();
        numberOfGoals = Integer.parseInt(state.getString("numberOfGoals"));
        completeGoals = new Long[numberOfGoals];
        goalObjects = new Goal[numberOfGoals];
        latitudes = new double[3];
        longitudes = new double[3];
        goalIds = new int[]{R.id.goal1, R.id.goal2, R.id.goal3};
        Log.i("TIME", "Start At: " + state.getString("startTime"));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // why is goals an instance variable? You don't use it outside of this method.
        goals = new String[]{state.getString("goalOne"), state.getString("goalTwo"), state.getString("goalThree")};

        for (int i = 0; i < numberOfGoals; i++) {
            final int finalI = i;
            db.collection("Goals").document(goals[i]).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Goal goal = documentSnapshot.toObject(Goal.class);
                    goalObjects[finalI] = goal;
                    latitudes[finalI] = goal.getLatitude();
                    longitudes[finalI] = goal.getLongitude();

                    String title = goal.getTitle();
                    String clueOne = goal.getClueOne();
                    String clueTwo = goal.getClueTwo();
                    String clueThree = goal.getClueThree();

                    addItem(title, new String[]{clueOne, clueTwo, clueThree}, finalI);

                    // noooooooo! The nature of async programming is that the last requested item might not be the last to complete.
                    // So this will still potentially start the thread before all the locations are received.
                    // Instead, consider adding a single OnSuccessListener that counts the number of responses received,
                    // and starts the thread if the number of responses received is equal to the number of requests sent.
                    if (finalI == numberOfGoals - 1) {
                        new Thread(locationRun).start();
                    }
                }
            });
        }
        return view;
    }

    class LocationRun implements Runnable {
        public void run() {
            while(true) {
                location = MainActivity.location;

                // Must Have Because: Only the original thread that created a view hierarchy can touch its views.
                if (getActivity() != null) {
                    if (goalObjects.length == numberOfGoals) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < goalObjects.length; i++) {
                                    if (completeGoals[i] == null) {
                                        double distance = DeltaLatLong.distance(location.getLatitude(), location.getLongitude(), latitudes[i], longitudes[i], "km");
                                        DecimalFormat df = new DecimalFormat("#.##");
                                        if (distance >= 1) {
                                            Log.i("DISTANCE", "kilometers to " + goalObjects[i].getTitle() + ": " + distance);
                                            if (getView() != null) {
                                                TextView textView = getView().findViewById(goalIds[i]);
                                                // hardcoded, non-translatable string
                                                textView.setText("Distance to goal: " + df.format(distance) + " km");
                                            }
                                        } else if (distance < 1 && distance * 1000 > 5) {
                                            Log.i("DISTANCE", "meters to " + goalObjects[i].getTitle() + ": " + distance * 1000);
                                            if (getView() != null) {
                                                TextView textView = getView().findViewById(goalIds[i]);
                                                textView.setText("Distance to goal: " + df.format(distance * 1000) + " m");
                                            }
                                        } else {
                                            if (getView() != null) {
                                                Log.i("TIME", "Goal " + i + " At: " + System.currentTimeMillis());
                                                Goal goal = goalObjects[i];
                                                TextView textView = getView().findViewById(goalIds[i]);
                                                textView.setText(goal.getTitle() + "\nCompleted");
                                                completeGoals[i] = System.currentTimeMillis();
                                            }
                                        }
                                    }
                                }
                            }
                        });

                        // Create gameEnd check boolean
                        boolean gameEnd = true;

                        // Verify gameEnd by checking against completed goals
                        for (int i = 0; i < completeGoals.length; i++) {
                            if (completeGoals[i] == null) {
                                gameEnd = false;
                            }
                        }

                        // If gameEnd still True then end game
                        if (gameEnd) {
                            // Add times for goals to state
                            // You could use an array for these instead of having so many different keys!
                            // The simplest implementation would be two arrays, one for times and one for names;
                            // the better implementation would make your Goal class implement Parcelable, and
                            // set an array of Goal instances as a single key/value pair in the state.
                            state.putString("goalOneTime", completeGoals.length >= 1 ? Long.toString(completeGoals[0]) : null);
                            state.putString("goalTwoTime", completeGoals.length >= 2 ? Long.toString(completeGoals[1]) : null);
                            state.putString("goalThreeTime", completeGoals.length >= 3 ? Long.toString(completeGoals[2]) : null);

                            // Add names for goals to state
                            state.putString("goalOneName", goalObjects.length >= 1 ? goalObjects[0].getTitle() : null);
                            state.putString("goalTwoName", goalObjects.length >= 2 ? goalObjects[0].getTitle() : null);
                            state.putString("goalThreeName", goalObjects.length >= 3 ? goalObjects[0].getTitle() : null);


                            // Calculate end time
                            // this could be simple getting-max code
                            // long latestTime = completeGoals[0];
                            // for (int i = 1; i < completeGoals.length; i++) {
                            //     latestTime = Math.max(latestTime, completeGoals[i];
                            // }
                            long latestTime =

                                    // First check if there wasn't a 3rd goal
                                    completeGoals.length < 3 ?

                                            // Second check if there wasn't a 2nd goal
                                            completeGoals.length < 2 ?

                                                    // This occurs if only one goal existed
                                                    completeGoals[0] :

                                                    // This occurs if only two goals existed
                                                    Math.max(completeGoals[0], completeGoals[1]) :

                                            // This occurs if three goals existed
                                            Math.max(completeGoals[0], Math.max(completeGoals[1], completeGoals[2]));

                            state.putString("endTime", Long.toString(latestTime));
                            if (getView() != null)
                                // goto fail! oh no!!!
                                Navigation.findNavController(getView()).navigate(R.id.gameEndAction, state);
                        }

                        try {
                            Thread.sleep(1000);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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
        item.setIndicatorColorRes(R.color.ivory);
        item.setIndicatorIconRes(R.drawable.goal_expander_icon);

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);

//get a sub item View
        View subItemZero = item.getSubItemView(0);
        ((TextView) subItemZero.findViewById(R.id.sub_title)).setText(subItems[0]);
        ((TextView) subItemZero.findViewById(R.id.sub_title)).setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

        View subItemOne = item.getSubItemView(1);
        ((TextView) subItemOne.findViewById(R.id.sub_title)).setText(subItems[1]);
        ((TextView) subItemOne.findViewById(R.id.sub_title)).setTextSize(TypedValue.COMPLEX_UNIT_SP,20);


        View subItemTwo = item.getSubItemView(2);
        ((TextView) subItemTwo.findViewById(R.id.sub_title)).setText(subItems[2]);
        ((TextView) subItemTwo.findViewById(R.id.sub_title)).setTextSize(TypedValue.COMPLEX_UNIT_SP,20);


    }


}
