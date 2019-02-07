package com.dmuench.scatterhunt;


import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmuench.scatterhunt.formsteps.NumberOfGoalsStep;
import com.dmuench.scatterhunt.formsteps.PlayfieldStep;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetupFragment extends Fragment implements StepperFormListener {

    private PlayfieldStep playfieldStep;
    private NumberOfGoalsStep numberOfGoalsStep;

    private Bundle state;

    private VerticalStepperFormView verticalStepperForm;

    private GeofencingClient mGeofencingClient;

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

        int playfieldRange = -1;
        int numberOfGoals = -1;

        boolean[] rangeArray = playfieldStep.getStepData();
        boolean[] goalsArray = numberOfGoalsStep.getStepData();

        double latitude = MainActivity.location.getLatitude();
        double longitude = MainActivity.location.getLongitude();

        Log.i("FORM LATITUDE", "Latitude: " + latitude);
        Log.i("FORM LONGITUDE", "Longitude: " + longitude);

        for (int i = 0; i < rangeArray.length; i++) {
            if (rangeArray[i]) {
                playfieldRange = Integer.parseInt(getContext().getResources().getStringArray(R.array.playfieldRange)[i]);
            }
            if (i < goalsArray.length && goalsArray[i]) {
                numberOfGoals = Integer.parseInt(getContext().getResources().getStringArray(R.array.numberOfGoals)[i]);
            }
        }

        Log.i("FORM PLAYFIELD", Integer.toString(playfieldRange));
        Log.i("FORM NUM OF GOALS", Integer.toString(numberOfGoals));


    }


    @Override
    public void onCancelledForm() {
//        Navigation.findNavController(getView()).navigate(R.id.returnToMainFragment);
    }

}

