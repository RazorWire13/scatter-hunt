package com.dmuench.scatterhunt;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetupFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setup, container, false);
        Context context = getActivity();

        Spinner pulldownMenuPlayfield = view.findViewById(R.id.pulldownMenuPlayfield);
        Spinner pulldownMenuGoals = view.findViewById(R.id.pulldownMenuGoals);

        ArrayAdapter<CharSequence> playfieldArrayAdapter = ArrayAdapter.createFromResource(context, R.array.playfieldRange, android.R.layout.simple_spinner_item);
        playfieldArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pulldownMenuPlayfield.setAdapter(playfieldArrayAdapter);

        ArrayAdapter<CharSequence> goalArrayAdapter = ArrayAdapter.createFromResource(context, R.array.numberOfGoals, android.R.layout.simple_spinner_item);
        goalArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pulldownMenuGoals.setAdapter(goalArrayAdapter);

        Button btnGoToPlayFragment = view.findViewById(R.id.btnGoToPlayFragment);



        btnGoToPlayFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.playFragmentAction);
            }
        });



        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
