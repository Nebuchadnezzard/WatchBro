package com.watchbro.watchbro;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#//newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    private GoogleSignInClient signInClient;
    private FirebaseAuth firebaseAuth;
    private SignInButton signInB;
    private Button signOutB;
    private static int RC_SIGN_IN = 9001;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Initialise firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Options de connexion au compte google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.tokenDefaultFirebase))
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this.getContext(), gso);

        signInB = (SignInButton) getView().findViewById(R.id.boutonConnect);
        signInB.setOnClickListener(this);
        signOutB = getView().findViewById(R.id.boutonDeconnect);
        signOutB.setOnClickListener(this);


    }

    @Override
    public void onStart() {
        super.onStart();

        // Vérifie si l'utilisateur est déjà connecté
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // Change l'affichage des bouton signIn et signOut
        if(currentUser != null) {
            signInB.setVisibility(View.GONE);
            signOutB.setVisibility(View.VISIBLE);
        }
        else {
            signInB.setVisibility(View.VISIBLE);
            signOutB.setVisibility(View.GONE);
        }
    }

    //resultat de l'envoi de l'intent signin google
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // réussite google signin
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.e("Firebase", "fail");
                Toast toastEchec = Toast.makeText(getActivity().getApplicationContext(), R.string.toastFailCo, Toast.LENGTH_SHORT);
                toastEchec.show();

                signInB.setVisibility(View.VISIBLE);
                signOutB.setVisibility(View.GONE);
            }
        }
    }


    // Authentification Firebase avec compte google
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Activité ident", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Activité ident", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.toastCoHello) + " " + user.getDisplayName(), Toast.LENGTH_LONG).show();
                        } else {
                            // En cas d'erreur de connexion, affiche un message à l'utilisateur
                            Toast toastEchec = Toast.makeText(getActivity().getApplicationContext(), R.string.toastFailCo, Toast.LENGTH_SHORT);
                            toastEchec.show();
                            signInB.setVisibility(View.VISIBLE);
                            signOutB.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public void signIn() {
        Intent intentSignInGoogle = signInClient.getSignInIntent();
        startActivityForResult(intentSignInGoogle, RC_SIGN_IN);

        signInB.setVisibility(View.GONE);
        signOutB.setVisibility(View.VISIBLE);
    }

    public void signOut() {

        // Déconnexion de firebase
        firebaseAuth.signOut();

        // Déconnexion du compte google
        signInClient.signOut();

        Toast.makeText(getActivity().getApplicationContext(), R.string.toastDeco, Toast.LENGTH_LONG).show();

        signInB.setVisibility(View.VISIBLE);
        signOutB.setVisibility(View.GONE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("Firebase", "Failed to connect");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.boutonConnect :
                signIn();
                break;
            case R.id.boutonDeconnect :
                signOut();
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
