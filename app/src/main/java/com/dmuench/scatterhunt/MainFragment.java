package com.dmuench.scatterhunt;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


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


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private static final int RC_SIGN_IN = 3742;

    public MainFragment() {

    }

    public Boolean signInStatus = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        Button signInButton = view.findViewById(R.id.signInButton);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Choose authentication providers
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build());


                if (signInStatus) {
                    AuthUI.getInstance()
                            .signOut(getContext())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                                    TextView welcomeView = getView().findViewById(R.id.loggedInUserTextView);
                                    welcomeView.setText("NOT LOGGED IN");
                                    Button signInButton = getView().findViewById(R.id.signInButton);
                                    signInButton.setText("Sign Up");
                                    Button addHuntsButton = getView().findViewById(R.id.goToAddHuntButton);
                                    addHuntsButton.setVisibility(View.GONE);
                                }
                            });
                }
                if (!signInStatus) {


// Create and launch sign-in intent
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        });

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button goToPlayGameButton = view.findViewById(R.id.goToPlayGameButton);
        Button goToAddHuntButton = view.findViewById(R.id.goToAddHuntButton);


        goToPlayGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.toSetupFragment);
            }
        });

        goToAddHuntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.toAddGoal);
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

                Button signInButton = getView().findViewById(R.id.signInButton);
                signInButton.setText("Logout");
                signInStatus = true;
                Button addHuntsButton = getView().findViewById(R.id.goToAddHuntButton);
                addHuntsButton.setVisibility(View.VISIBLE);


                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }


}