package com.dmuench.scatterhunt;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmuench.scatterhunt.formsteps.NumberOfGoalsStep;
import com.dmuench.scatterhunt.formsteps.PlayfieldStep;
import com.dmuench.scatterhunt.models.Goal;
import com.dmuench.scatterhunt.tools.DeltaLatLong;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetupFragment extends Fragment implements StepperFormListener {

    private PlayfieldStep playfieldStep;
    private NumberOfGoalsStep numberOfGoalsStep;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userId;

    private Bundle state;

    private VerticalStepperFormView verticalStepperForm;

    public SetupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        state = getArguments();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user == null? null : user.getUid();

        // Create the steps.
        playfieldStep = new PlayfieldStep("Playfield Range");
        numberOfGoalsStep = new NumberOfGoalsStep("Number of Goals");

        // Find the form view, set it up and initialize it.
        verticalStepperForm = view.findViewById(R.id.stepper_form);
        verticalStepperForm
                .setup(this, playfieldStep, numberOfGoalsStep)
                .init();
    }

    @Override
    public void onCompletedForm() {

        double playfieldRange = -1;
        int numberOfGoals = -1;

        boolean[] rangeArray = playfieldStep.getStepData();
        boolean[] goalsArray = numberOfGoalsStep.getStepData();

        final double latitude = MainActivity.location.getLatitude();
        final double longitude = MainActivity.location.getLongitude();

        Log.i("FORM LATITUDE", "Latitude: " + latitude);
        Log.i("FORM LONGITUDE", "Longitude: " + longitude);

        for (int i = 0; i < rangeArray.length; i++) {
            if (rangeArray[i]) {
                playfieldRange = Double.parseDouble(getContext().getResources().getStringArray(R.array.playfieldRange)[i]);
            }
            if (i < goalsArray.length && goalsArray[i]) {
                numberOfGoals = Integer.parseInt(getContext().getResources().getStringArray(R.array.numberOfGoals)[i]);
            }
        }

        final double radius = playfieldRange;
        final int goals = numberOfGoals;

        Log.i("FORM PLAYFIELD", Double.toString(playfieldRange));
        Log.i("FORM NUM OF GOALS", Integer.toString(numberOfGoals));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Goals").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<String> gameGoals = getGameGoals(queryDocumentSnapshots, radius, goals, latitude, longitude);

                Bundle state = new Bundle();
                state.putString("goalOne", gameGoals.get(0));
                state.putString("goalTwo", gameGoals.size() > 1 ? gameGoals.get(1) : null);
                state.putString("goalThree", gameGoals.size() > 2 ? gameGoals.get(2) : null);
                Navigation.findNavController(getView()).navigate(R.id.playFragmentAction, state);
            }

        });
    }

    public List<String> getGameGoals(QuerySnapshot queryDocumentSnapshots, final double playfieldRange, final int numberOfGoals, final double latitude, final double longitude) {
        final List<String> goalsSelected = new ArrayList<>();
        final List<DocumentSnapshot> goalsWithinPlayfield = new ArrayList<>();

                for (DocumentSnapshot document: queryDocumentSnapshots) {
                    Goal currentGoalSnapshot = document.toObject(Goal.class);
                    if (!currentGoalSnapshot.getCreatedBy().equals(userId)) {
                        if (DeltaLatLong.distance(latitude, longitude, currentGoalSnapshot.getLatitude(), currentGoalSnapshot.getLongitude(), "km") <= playfieldRange) goalsWithinPlayfield.add(document);
                    }
                }
                // TODO: If no goals exist within the playfield this crashes the app, find possible fixes
                List<Integer> numbers = getRandomNumberInRange(0, goalsWithinPlayfield.size() -1);
                for (int i = 0; i < numberOfGoals; i++) {

                    goalsSelected.add(goalsWithinPlayfield.get(numbers.get(i)).getId());
                }
        return goalsSelected;
    }

    private static ArrayList<Integer> getRandomNumberInRange(int min, int max) {
        ArrayList<Integer> randoms = new ArrayList<>();
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        while (randoms.size() != 3) {
            Random r = new Random();
            int next = r.nextInt((max - min) + 1) + min;
            if (!randoms.contains(next)) randoms.add(next);
        }
        return randoms;
    }

    @Override
    public void onCancelledForm() {
//        Navigation.findNavController(getView()).navigate(R.id.returnToMainFragment);
    }

}

