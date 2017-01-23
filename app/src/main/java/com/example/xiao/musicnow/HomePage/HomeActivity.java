package com.example.xiao.musicnow.HomePage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import android.view.Window;
import android.widget.Toast;


import com.example.xiao.musicnow.Controller.SPController;
import com.example.xiao.musicnow.HomePage.Fragments.HomePageFragment;
import com.example.xiao.musicnow.HomePage.Fragments.MusicFragment;
import com.example.xiao.musicnow.HomePage.Fragments.MyZoneFragment;
import com.example.xiao.musicnow.HomePage.Fragments.OfflineFragment;
import com.example.xiao.musicnow.HomePage.Fragments.PictureFragment;
import com.example.xiao.musicnow.HomePage.Fragments.VideoFragment;
import com.example.xiao.musicnow.LoginPage.LoginActivity;
import com.example.xiao.musicnow.R;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

    private static ProgressDialog pDialog;
    private static ProgressDialog mDownloadingDialog;
    private static AlertDialog.Builder loginAlert;
    GoogleApiClient mGoogleApiClient;
    boolean mSignInClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_home);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        mDownloadingDialog = new ProgressDialog(this);
        mDownloadingDialog.setMessage("Loading");
        mDownloadingDialog.setIndeterminate(true);
        mDownloadingDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDownloadingDialog.setCancelable(false);


        loginAlert = new AlertDialog.Builder(HomeActivity.this).setTitle("Please Login First").setIcon(
                android.R.drawable.ic_dialog_info).setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        }).setNegativeButton("Cancel", null);

        initDrawer();
        initContainer();

    }

    private void initDrawer(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void initContainer(){
        if(findViewById(R.id.home_fragment_container) != null) {
            HomePageFragment homeFragment = new HomePageFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, homeFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_search){
            Toast.makeText(this, "Search function is selected", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Settings function is selected", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment homeFragment = null;
        String title = "";
        if (id == R.id.nav_home) {
            homeFragment = new HomePageFragment();
            title = "Home";
        } else if (id == R.id.nav_music) {
            homeFragment = new MusicFragment();
            title = "Music";
        } else if (id == R.id.nav_video) {
            homeFragment = new VideoFragment();
            title = "Video";
        } else if (id == R.id.nav_pic) {
            homeFragment = new PictureFragment();
            title = "Picture";
        } else if (id == R.id.nav_offline) {
            homeFragment = new OfflineFragment();
            title = "Download";
        } else if (id == R.id.nav_my_zone) {
            homeFragment = new MyZoneFragment();
            title = "My Zone";
        } else if (id == R.id.nav_logout) {
            Toast.makeText(this, "User: " + SPController.getInstance(this).getMobile() + " Log out", Toast.LENGTH_SHORT).show();
            LoginManager.getInstance().logOut();
            if (mGoogleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            SPController.getInstance(this).clearSharedPreference();
        }
        if(homeFragment != null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, homeFragment).commit();
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public static void showPDialog(){
        if (!pDialog.isShowing()){
            pDialog.show();
            pDialog.setContentView(R.layout.progress_loading);
        }
    }
    public static void disPDialog(){
        if (pDialog.isShowing()){
            pDialog.dismiss();
        }
    }

    public static void showmDownloadingDialog(){
        if (!mDownloadingDialog.isShowing()){
            mDownloadingDialog.show();
        }
    }
    public static void dismDownloadingDialog(){
        if (mDownloadingDialog.isShowing()){
            mDownloadingDialog.dismiss();
        }
    }

    public static ProgressDialog getmDownloadingDialog(){
        return mDownloadingDialog;
    }

    public static void showLoginAlert(){
        loginAlert.show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mSignInClicked = false;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("LOGOUT", "onConnectionFailed:" + connectionResult);

    }
}
