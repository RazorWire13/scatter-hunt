package com.dmuench.scatterhunt;


import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.dmuench.scatterhunt.formsteps.ClueStep;
import com.dmuench.scatterhunt.formsteps.NumberOfGoalsStep;
import com.dmuench.scatterhunt.formsteps.PlayfieldStep;
import com.dmuench.scatterhunt.formsteps.TitleStep;
import com.dmuench.scatterhunt.models.Goal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetupFragment extends Fragment implements StepperFormListener {

    private PlayfieldStep playfieldStep;
    private NumberOfGoalsStep numberOfGoalsStep;

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
        Log.i("FORM TITLE", playfieldStep.getStepData().toString());
        Log.i("FORM CLUE ONE", numberOfGoalsStep.getStepData().toString());
//
//        Log.i("FORM LATITUDE", state.getString("latitude"));
//        Log.i("FORM LONGITUDE", state.getString("longitude"));


//        String latitude = state.getString("latitude");
//        String longitude = state.getString("longitude");

//        if (latitude == null || longitude == null)
//            Toast.makeText(getContext(), "Location Data Not Available - Please Ensure Location Is Enabled", Toast.LENGTH_SHORT).show();
//        else {

// todo firebase stuff


//        }
    }

    @Override
    public void onCancelledForm() {
//        Navigation.findNavController(getView()).navigate(R.id.returnToMainFragment);
    }

}
