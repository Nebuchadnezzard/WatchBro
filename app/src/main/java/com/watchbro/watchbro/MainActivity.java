package com.watchbro.watchbro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, ActivityFragment.OnFragmentInteractionListener {

    private GoogleSignInClient signInClient;
    private FirebaseAuth firebaseAuth;
    private SignInButton signInB;
    private Button signOutB;

    private static int RC_SIGN_IN = 9001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Définie la toolbar en tant qu'action bar dans l'activité
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Ajoute le bouton du drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // L'activité est le listener du menu
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Initialise firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Options de connexion au compte google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.tokenDefaultFirebase))
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this, gso);

        signInB = findViewById(R.id.boutonConnect);
        signInB.setOnClickListener(this);
        signOutB = findViewById(R.id.boutonDeconnect);
        signOutB.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
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

    @Override
    public void onBackPressed() {
        // Ferme le drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings :
                // TODO intent activité parametres
                break;
            case R.id.nav_home :
                showFragment(new HomeFragment());
            case R.id.nav_activity :
                showFragment(new ActivityFragment());
                break;
            case R.id.nav_course :
                showFragment(new CourseFragment());
                break;
            case R.id.nav_connect :
                showFragment(new ConnectFragment());
                break;
            default:
                return false;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Remplace le fragment actuel par un autre
     * @param fragment L'autre fragment
     */
    private void showFragment(Fragment fragment) {
        FragmentManager fragManager = getSupportFragmentManager();
        fragManager.beginTransaction()
                .replace(R.id.content_main, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("Firebase", "Failed to connect");
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
                Toast toastEchec = Toast.makeText(getApplicationContext(), R.string.toastFailCo, Toast.LENGTH_SHORT);
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
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Activité ident", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), getString(R.string.toastCoHello) + " " + user.getDisplayName(), Toast.LENGTH_LONG).show();
                        } else {
                            // En cas d'erreur de connexion, affiche un message à l'utilisateur
                            Toast toastEchec = Toast.makeText(getApplicationContext(), R.string.toastFailCo, Toast.LENGTH_SHORT);
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

        Toast.makeText(getApplicationContext(), R.string.toastDeco, Toast.LENGTH_LONG).show();

        signInB.setVisibility(View.VISIBLE);
        signOutB.setVisibility(View.GONE);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
