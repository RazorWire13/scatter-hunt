package com.dmuench.scatterhunt;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import androidx.navigation.Navigation;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private static final int RC_SIGN_IN = 3742;

    private Bundle state;

    public MainFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        final TextView welcomeView = view.findViewById(R.id.loggedInUserTextView);
        final Button signInButton = view.findViewById(R.id.btnGoToSignInView);
        final Button createGoalButton = view.findViewById(R.id.btnGoToCreateGoalFragment);
        final FirebaseAuth auth = FirebaseAuth.getInstance();



        if (auth.getCurrentUser() == null) {
            welcomeView.setText("NOT LOGGED IN");
            signInButton.setText("Sign Up/Sign In");
            createGoalButton.setVisibility(View.GONE);
        } else {
            welcomeView.setText(auth.getCurrentUser().getDisplayName());
            signInButton.setText("Logout");
            createGoalButton.setVisibility(View.VISIBLE);
        }






        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Choose authentication providers
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build());

                if (auth.getCurrentUser() != null) {
                    AuthUI.getInstance()
                            .signOut(getContext())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                                    welcomeView.setText("NOT LOGGED IN");
                                    signInButton.setText("Sign Up/Sign In");
                                    createGoalButton.setVisibility(View.GONE);
                                }
                            });

                } else {

// Create and launch sign-in intent
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .setTheme(R.style.AppTheme)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        state = savedInstanceState != null? savedInstanceState : new Bundle();

        super.onViewCreated(view, savedInstanceState);
        Button btnGoToSetupFragment = view.findViewById(R.id.btnGoToSetupFragment);
        Button btnGoToCreateGoalFragment = view.findViewById(R.id.btnGoToCreateGoalFragment);

//                                                      changed to setup button to testaction for test
        btnGoToSetupFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.setupFragmentAction);
            }
        });

        btnGoToCreateGoalFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.createGoalAction, state);
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                TextView welcomeView = getView().findViewById(R.id.loggedInUserTextView);
                welcomeView.setText(user.getDisplayName());

                Button signInButton = getView().findViewById(R.id.btnGoToSignInView);
                signInButton.setText("Logout");
                Button addHuntsButton = getView().findViewById(R.id.btnGoToCreateGoalFragment);
                addHuntsButton.setVisibility(View.VISIBLE);


                // ...
            } else {
                // You still have no error handling for if sign in fails!
                // You need error handling!
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }


}