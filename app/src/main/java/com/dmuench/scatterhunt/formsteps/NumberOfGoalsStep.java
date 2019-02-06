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
    private View rangesStepContent;

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
        rangesStepContent = inflater.inflate(R.layout.playfield_range_layout, null, false);
        setupPlayfieldRanges();

        return rangesStepContent;
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
        String[] weekRangeStrings = getContext().getResources().getStringArray(R.array.playfieldRangeExtended);
        List<String> selectedWeekRangeStrings = new ArrayList<>();
        for (int i = 0; i < weekRangeStrings.length; i++) {
            if (numberOfGoals[i]) {
                selectedWeekRangeStrings.add(weekRangeStrings[i]);
            }
        }

        return TextUtils.join(", ", selectedWeekRangeStrings);
    }

    @Override
    public void restoreStepData(boolean[] data) {
        numberOfGoals = data;
        setupPlayfieldRanges();
    }

    @Override
    protected IsDataValid isStepDataValid(boolean[] stepData) {
        boolean thereIsAtLeastOneRangeSelected = false;
        for(int i = 0; i < stepData.length && !thereIsAtLeastOneRangeSelected; i++) {
            if(stepData[i]) {
                thereIsAtLeastOneRangeSelected = true;
            }
        }

        return thereIsAtLeastOneRangeSelected
                ? new IsDataValid(true) : new IsDataValid(false, getContext().getString(R.string.playfieldRangeError));
    }

    private void setupPlayfieldRanges() {
        boolean firstSetup = numberOfGoals == null;
        numberOfGoals = firstSetup ? new boolean[7] : numberOfGoals;

        final String[] weekRanges = getContext().getResources().getStringArray(R.array.playfieldRange);
        for(int i = 0; i < weekRanges.length; i++) {
            final int index = i;
            final View dayLayout = getRangeLayout(index);

            if (firstSetup) {
                // By default, we only mark the working ranges as activated
                numberOfGoals[index] = index < 5;
            }

            updateRangeLayout(index, dayLayout, false);

            if (dayLayout != null) {
                dayLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        numberOfGoals[index] = !numberOfGoals[index];
                        updateRangeLayout(index, dayLayout, true);
                        markAsCompletedOrUncompleted(true);
                    }
                });

                final TextView dayText = dayLayout.findViewById(R.id.day);
                dayText.setText(weekRanges[index]);
            }
        }
    }

    private View getRangeLayout(int i) {
        int id = rangesStepContent.getResources().getIdentifier(
                "day_" + i, "id", getContext().getPackageName());
        return rangesStepContent.findViewById(id);
    }

    private void updateRangeLayout(int dayIndex, View dayLayout, boolean useAnimations) {
        if (numberOfGoals[dayIndex]) {
            markPlayfieldRange(dayIndex, dayLayout, useAnimations);
        } else {
            unmarkPlayfieldRange(dayIndex, dayLayout, useAnimations);
        }
    }

    private void markPlayfieldRange(int dayIndex, View dayLayout, boolean useAnimations) {
        numberOfGoals[dayIndex] = true;

        if (dayLayout != null) {
            Drawable bg = ContextCompat.getDrawable(getContext(), ernestoyaquello.com.verticalstepperform.R.drawable.circle_step_done);
            int colorPrimary = ContextCompat.getColor(getContext(), R.color.colorPrimary);
            bg.setColorFilter(new PorterDuffColorFilter(colorPrimary, PorterDuff.Mode.SRC_IN));
            dayLayout.setBackground(bg);

            TextView dayText = dayLayout.findViewById(R.id.day);
            dayText.setTextColor(Color.rgb(255, 255, 255));
        }
    }

    private void unmarkPlayfieldRange(int dayIndex, View dayLayout, boolean useAnimations) {
        numberOfGoals[dayIndex] = false;

        dayLayout.setBackgroundResource(0);

        TextView dayText = dayLayout.findViewById(R.id.day);
        int colour = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        dayText.setTextColor(colour);
    }
}
