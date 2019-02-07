package com.dmuench.scatterhunt;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EndGameFragment extends Fragment {


    public EndGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_end_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get all the text views
        TextView totalTimeView = view.findViewById(R.id.time_game_length);
        TextView goalOneTimeView = view.findViewById(R.id.time_goal_one);
        TextView goalTwoTimeView = view.findViewById(R.id.time_goal_two);
        TextView goalThreeTimeView = view.findViewById(R.id.time_goal_three);
        TextView goalOneNameView = view.findViewById(R.id.label_goal_one);
        TextView goalTwoNameView = view.findViewById(R.id.label_goal_two);
        TextView goalThreeNameView = view.findViewById(R.id.label_goal_three);
        view.findViewById(R.id.btn_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_endGameFragment_to_mainFragment);

            }
        });
        view.findViewById(R.id.btn_play_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_endGameFragment_to_setupFragment);
            }
        });

        // Get arguments for state bundle
        Bundle state = getArguments();

        // Get times from state
        Long startTime = state.getString("startTime") == null? null : Long.parseLong(state.getString("startTime"));
        Long endTime = state.getString("endTime") == null? null : Long.parseLong(state.getString("endTime"));
        Long goalOneTime = state.getString("goalOneTime") == null? null : Long.parseLong(state.getString("goalOneTime"));
        Long goalTwoTime = state.getString("goalTwoTime") == null? null : Long.parseLong(state.getString("goalTwoTime"));
        Long goalThreeTime = state.getString("goalThreeTime") == null? null : Long.parseLong(state.getString("goalThreeTime"));

        // Get names from state
        String goalOneName = state.getString("goalOneName");
        String goalTwoName = state.getString("goalTwoName");
        String goalThreeName = state.getString("goalThreeName");

        // Set total game duration
        totalTimeView.setText(getDeltaTimeString(endTime, startTime));

        // If Goal One exists set name and time, otherwise set visibility to gone
        if (goalOneName != null) {
            goalOneNameView.setText("Found " + goalOneName + " In:");
            goalOneTimeView.setText(getDeltaTimeString(goalOneTime, startTime));
        } else {
            goalOneNameView.setVisibility(View.GONE);
            goalOneTimeView.setVisibility(View.GONE);
        }

        // If Goal Two exists set name and time, otherwise set visibility to gone
        if (goalTwoName != null) {
            goalTwoNameView.setText("Found " + goalTwoName + " In:");
            goalTwoTimeView.setText(getDeltaTimeString(goalTwoTime, startTime));
        } else {
            goalTwoNameView.setVisibility(View.GONE);
            goalTwoTimeView.setVisibility(View.GONE);
        }

        // If Goal Three exists set name and time, otherwise set visibility to gone
        if (goalThreeName != null) {
            goalThreeNameView.setText("Found " + goalThreeName + " In:");
            goalThreeTimeView.setText(getDeltaTimeString(goalThreeTime, startTime));
        } else {
            goalThreeNameView.setVisibility(View.GONE);
            goalThreeTimeView.setVisibility(View.GONE);
        }

    }

    // Gets Total Time in Hours:Minutes:Seconds between two moments
    public String getDeltaTimeString (long end, long start) {
        String deltaTime = "";

        long millis = end - start;
        int seconds = (int)(millis/1000);
        int minutes = seconds/60;
        int hours = minutes/60;

        millis = millis%1000;
        seconds = seconds%60;
        minutes = minutes%60;
        hours = hours%24;

        deltaTime = String.format("%02d Hours %02d Minutes %02d Seconds", hours, minutes, seconds);
        return deltaTime;

    }
}
