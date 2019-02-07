package com.dmuench.scatterhunt;


import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.dmuench.scatterhunt.models.Goal;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayFragment extends Fragment {

    private ExpandingList expandingList;
    private String[] goals;
    private List<Goal> goalList;
    private Location location;

    public PlayFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle state = getArguments();
        goalList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        goals = new String[]{state.getString("goalOne"), state.getString("goalTwo"), state.getString("goalThree")};

        for (int i = 0; i < goals.length; i++) {
            if (goals[i] != null) {
                db.collection("Goals").document(goals[i]).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        goalList.add(documentSnapshot.toObject(Goal.class));
                    }
                });
            }
        }



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

    class LocationRun implements Runnable {
        public void run() {
            location = MainActivity.location;

        }
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




}
