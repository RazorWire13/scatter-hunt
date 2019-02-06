package com.dmuench.scatterhunt.formsteps;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dmuench.scatterhunt.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import ernestoyaquello.com.verticalstepperform.Step;

public class NumberOfGoalsStep extends Step<boolean[]> {

    private boolean[] numberOfGoals;
    private View numberOfGoalsStepContent;

    public NumberOfGoalsStep(String title) {
        this(title, "");
    }

    public NumberOfGoalsStep(String title, String subtitle) {
        super(title, subtitle);
    }

    @NonNull
    @Override
    protected View createStepContentLayout() {

        // We create this step view by inflating an XML layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        numberOfGoalsStepContent = inflater.inflate(R.layout.number_of_goals_layout, null, false);
        setupNumberOfGoals();

        return numberOfGoalsStepContent;
    }

    @Override
    protected void onStepOpened(boolean animated) {
        // No need to do anything here
    }

    @Override
    protected void onStepClosed(boolean animated) {
        // No need to do anything here
    }

    @Override
    protected void onStepMarkedAsCompleted(boolean animated) {
        // No need to do anything here
    }

    @Override
    protected void onStepMarkedAsUncompleted(boolean animated) {
        // No need to do anything here
    }

    @Override
    public boolean[] getStepData() {
        return numberOfGoals;
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        String[] numberOfGoalsStrings = getContext().getResources().getStringArray(R.array.numberOfGoalsExtended);
        List<String> selectedNumberOfGoals = new ArrayList<>();
        for (int i = 0; i < numberOfGoalsStrings.length; i++) {
            if (numberOfGoals[i]) {
                selectedNumberOfGoals.add(numberOfGoalsStrings[i]);
            }
        }

        return TextUtils.join(", ", selectedNumberOfGoals);
    }

    @Override
    public void restoreStepData(boolean[] data) {
        numberOfGoals = data;
        setupNumberOfGoals();
    }

    @Override
    protected IsDataValid isStepDataValid(boolean[] stepData) {
        boolean thereIsAtleastOneGoalSelected = false;
        for(int i = 0; i < stepData.length && !thereIsAtleastOneGoalSelected; i++) {
            if(stepData[i]) {
                thereIsAtleastOneGoalSelected = true;
            }
        }

        return thereIsAtleastOneGoalSelected
                ? new IsDataValid(true)
                : new IsDataValid(false, getContext().getString(R.string.numberOfGoalsError));
    }

    private void setupNumberOfGoals() {
        boolean firstSetup = numberOfGoals == null;
        numberOfGoals = firstSetup ? new boolean[3] : numberOfGoals;

        final String[] numberOfGoals = getContext().getResources().getStringArray(R.array.numberOfGoals);
        for(int i = 0; i < numberOfGoals.length; i++) {
            final int index = i;
            final View numberOfGoalsLayout = getNumberOfGoalsLayout(index);

            if (firstSetup) {
                // By default, we mark the smallest number of goals activated
                this.numberOfGoals[index] = index < 1;
            }

            updateNumberOfGoalsLayout(index, numberOfGoalsLayout, false);

            if (numberOfGoalsLayout != null) {
                numberOfGoalsLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < NumberOfGoalsStep.this.numberOfGoals.length; i++) {
                            if (i == index) {
                                NumberOfGoalsStep.this.numberOfGoals[index] = true;
                                updateNumberOfGoalsLayout(index, numberOfGoalsLayout, true);
                            } else {
                                NumberOfGoalsStep.this.numberOfGoals[i] = false;
                                updateNumberOfGoalsLayout(i, getNumberOfGoalsLayout(i), true);
                            }
                        }
                        markAsCompletedOrUncompleted(true);
                    }
                });

                final TextView numberOfGoalsText = numberOfGoalsLayout.findViewById(R.id.option);
                numberOfGoalsText.setText(numberOfGoals[index]);
            }
        }
    }

    private View getNumberOfGoalsLayout(int i) {
        int id = numberOfGoalsStepContent.getResources().getIdentifier(
                "goal_" + i, "id", getContext().getPackageName());
        return numberOfGoalsStepContent.findViewById(id);
    }

    private void updateNumberOfGoalsLayout(int numberOfGoalsIndex, View numberOfGoalsLayout, boolean useAnimations) {
        if (numberOfGoals[numberOfGoalsIndex]) {
            markNumberOfGoals(numberOfGoalsIndex, numberOfGoalsLayout, useAnimations);
        } else {
            unmarkNumberOfGoals(numberOfGoalsIndex, numberOfGoalsLayout, useAnimations);
        }
    }

    private void markNumberOfGoals(int numberOfGoalsIndex, View numberOfGoalsLayout, boolean useAnimations) {
        numberOfGoals[numberOfGoalsIndex] = true;

        if (numberOfGoalsLayout != null) {
            Drawable bg = ContextCompat.getDrawable(getContext(), ernestoyaquello.com.verticalstepperform.R.drawable.circle_step_done);
            int colorPrimary = ContextCompat.getColor(getContext(), R.color.colorPrimary);
            bg.setColorFilter(new PorterDuffColorFilter(colorPrimary, PorterDuff.Mode.SRC_IN));
            numberOfGoalsLayout.setBackground(bg);

            TextView numberOfGoalsText = numberOfGoalsLayout.findViewById(R.id.option);
            numberOfGoalsText.setTextColor(Color.rgb(255, 255, 255));
        }
    }

    private void unmarkNumberOfGoals(int numberOfGoalsIndex, View numberOfGoalsLayout, boolean useAnimations) {
        numberOfGoals[numberOfGoalsIndex] = false;

        numberOfGoalsLayout.setBackgroundResource(0);

        TextView numberOfGoalsText = numberOfGoalsLayout.findViewById(R.id.option);
        int colour = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        numberOfGoalsText.setTextColor(colour);
    }
}