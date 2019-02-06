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

public class PlayfieldStep extends Step<boolean[]> {

    private boolean[] playfieldRanges;
    private View playfieldRangesStepContent;

    public PlayfieldStep(String title) {
        this(title, "");
    }

    public PlayfieldStep(String title, String subtitle) {
        super(title, subtitle);
    }

    @NonNull
    @Override
    protected View createStepContentLayout() {

        // We create this step view by inflating an XML layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        playfieldRangesStepContent = inflater.inflate(R.layout.playfield_range_layout, null, false);
        setupPlayfieldRanges();

        return playfieldRangesStepContent;
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
        return playfieldRanges;
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        String[] playfieldRangesStrings = getContext().getResources().getStringArray(R.array.playfieldRangeExtended);
        List<String> selectedPlayfieldRanges = new ArrayList<>();
        for (int i = 0; i < playfieldRangesStrings.length; i++) {
            if (playfieldRanges[i]) {
                selectedPlayfieldRanges.add(playfieldRangesStrings[i]);
            }
        }

        return TextUtils.join(", ", selectedPlayfieldRanges);
    }

    @Override
    public void restoreStepData(boolean[] data) {
        playfieldRanges = data;
        setupPlayfieldRanges();
    }

    @Override
    protected IsDataValid isStepDataValid(boolean[] stepData) {
        boolean thereIsAtLeastOneDaySelected = false;
        for(int i = 0; i < stepData.length && !thereIsAtLeastOneDaySelected; i++) {
            if(stepData[i]) {
                thereIsAtLeastOneDaySelected = true;
            }
        }

        return thereIsAtLeastOneDaySelected
                ? new IsDataValid(true)
                : new IsDataValid(false, getContext().getString(R.string.playfieldRangeError));
    }

    private void setupPlayfieldRanges() {
        boolean firstSetup = playfieldRanges == null;
        playfieldRanges = firstSetup ? new boolean[4] : playfieldRanges;

        final String[] playfieldRanges = getContext().getResources().getStringArray(R.array.playfieldRange);
        for(int i = 0; i < playfieldRanges.length; i++) {
            final int index = i;
            final View rangeLayout = getRangeLayout(index);

            if (firstSetup) {
                // By default, we mark the smallest playfield activated
                this.playfieldRanges[index] = index < 1;
            }

            updateRangeLayout(index, rangeLayout, false);

            if (rangeLayout != null) {
                rangeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < PlayfieldStep.this.playfieldRanges.length; i++) {
                            if (i == index) {
                                PlayfieldStep.this.playfieldRanges[index] = true;
                                updateRangeLayout(index, rangeLayout, true);
                            } else {
                                PlayfieldStep.this.playfieldRanges[i] = false;
                                updateRangeLayout(i, getRangeLayout(i), true);
                            }
                        }
                        markAsCompletedOrUncompleted(true);
                    }
                });

                final TextView rangeText = rangeLayout.findViewById(R.id.option);
                rangeText.setText(playfieldRanges[index]);
            }
        }
    }

    private View getRangeLayout(int i) {
        int id = playfieldRangesStepContent.getResources().getIdentifier(
                "range_" + i, "id", getContext().getPackageName());
        return playfieldRangesStepContent.findViewById(id);
    }

    private void updateRangeLayout(int rangeIndex, View rangeLayout, boolean useAnimations) {
        if (playfieldRanges[rangeIndex]) {
            markPlayfieldRange(rangeIndex, rangeLayout, useAnimations);
        } else {
            unmarkPlayfieldRange(rangeIndex, rangeLayout, useAnimations);
        }
    }

    private void markPlayfieldRange(int rangeIndex, View rangeLayout, boolean useAnimations) {
        playfieldRanges[rangeIndex] = true;

        if (rangeLayout != null) {
            Drawable bg = ContextCompat.getDrawable(getContext(), ernestoyaquello.com.verticalstepperform.R.drawable.circle_step_done);
            int colorPrimary = ContextCompat.getColor(getContext(), R.color.colorPrimary);
            bg.setColorFilter(new PorterDuffColorFilter(colorPrimary, PorterDuff.Mode.SRC_IN));
            rangeLayout.setBackground(bg);

            TextView rangeText = rangeLayout.findViewById(R.id.option);
            rangeText.setTextColor(Color.rgb(255, 255, 255));
        }
    }

    private void unmarkPlayfieldRange(int rangeIndex, View rangeLayout, boolean useAnimations) {
        playfieldRanges[rangeIndex] = false;

        rangeLayout.setBackgroundResource(0);

        TextView rangeText = rangeLayout.findViewById(R.id.option);
        int colour = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        rangeText.setTextColor(colour);
    }
}