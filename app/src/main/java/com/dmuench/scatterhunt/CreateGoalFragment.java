package com.dmuench.scatterhunt;


import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmuench.scatterhunt.formsteps.ClueStep;
import com.dmuench.scatterhunt.formsteps.TitleStep;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateGoalFragment extends Fragment implements StepperFormListener {

    private TitleStep titleStep;
    private ClueStep clueStepOne;
    private ClueStep clueStepTwo;
    private ClueStep clueStepThree;

    private VerticalStepperFormView verticalStepperForm;

    public CreateGoalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_goal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Create the steps.
        titleStep = new TitleStep("ScatterGoal Title");
        clueStepOne = new ClueStep("Clue", "One");
        clueStepTwo = new ClueStep("Clue", "Two");
        clueStepThree = new ClueStep("Clue", "Three");

        // Find the form view, set it up and initialize it.
        verticalStepperForm = view.findViewById(R.id.stepper_form);
        verticalStepperForm
                .setup(this, titleStep, clueStepOne, clueStepTwo, clueStepThree)
                .init();
    }

    @Override
    public void onCompletedForm() {
        Log.i("FORM TITLE", titleStep.getStepData());
        Log.i("FORM CLUE ONE", clueStepOne.getStepData());
        Log.i("FORM CLUE TWO", clueStepTwo.getStepData());
        Log.i("FORM CLUE THREE", clueStepThree.getStepData());

        // TODO: Do Some Firebase Firestore Things.
    }

    @Override
    public void onCancelledForm() {
        Activity activity = getActivity();
        activity.finish();
    }
}
