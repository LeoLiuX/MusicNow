package com.example.xiao.musicnow.LoginPage.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.xiao.musicnow.Controller.AppController;
import com.example.xiao.musicnow.Controller.SPController;
import com.example.xiao.musicnow.HomePage.HomeActivity;
import com.example.xiao.musicnow.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by liuxi on 2017/1/20.
 */

public class LoginFragment extends Fragment implements OnConnectionFailedListener{
    private String TAG = "LoginFragment";

    TextInputEditText Mobile, Password;
    Button signInBtn, resetBtn;
    String mobile, password;

    private View v;


    Button mFbButtonSignIn;
    LoginButton mLoginButton;
    CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton btnSignIn;
    private static final int RC_SIGN_IN = 007;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_login, container, false);

        Mobile = (TextInputEditText) v.findViewById(R.id.mobile);
        Mobile.requestFocus();
        Password = (TextInputEditText) v.findViewById(R.id.password);
        signInBtn = (Button) v.findViewById(R.id.signInBtn);
        resetBtn = (Button) v.findViewById(R.id.reset);
        mFbButtonSignIn = (Button) v.findViewById(R.id.button_fb_sign_in);
        btnSignIn = (SignInButton) v.findViewById(R.id.btn_sign_in);
        // login
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(submitForm())
                {
                    Toast.makeText(getActivity(),"Checking...",Toast.LENGTH_LONG).show();
                    checkLogin(mobile, password);
                }
                else
                {
                    checkValidation(mobile, password);
                }
            }
        });
        // reset password
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetFragment fragment = new ResetFragment();
                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.container,fragment).commit();
            }
        });
        // facebook login
        fbAndGoogle();
        return v;
    }

    private void fbAndGoogle(){
        mFbButtonSignIn = (Button) v.findViewById(R.id.button_fb_sign_in);
                /*-----------google sign in---------------*/
        btnSignIn = (SignInButton) v.findViewById(R.id.btn_sign_in);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
                /*---------------fb sign in-------------------*/
        mFbButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginButton.performClick();
            }
        });
        //mLoginButton = (LoginButton) view.findViewById(R.id.login_button);
        mLoginButton = new LoginButton(getContext());
        mLoginButton.setReadPermissions("email");
        // If using in a fragment
        mLoginButton.setFragment(this);
        // Other app specific specialization
        callbackManager = CallbackManager.Factory.create();
        // Callback registration
        mLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.e("fblogin", "success");
                final GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String id = object.getString("id");
                                    Log.e("fblogin", id);
                                    SPController.getInstance(getActivity()).setMobile(id);
                                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.i("fblogin", "fb log in error");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            // start anotger activity
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            SPController.getInstance(getActivity()).setMobile(personId);
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
        } else {
            // Signed out, show unauthenticated UI.
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void checkLogin(String mobile, String password) {
        String urlLogin = "http://rjtmobile.com/ansari//rjt_music/music_app/login.php?&user_mobile="+mobile+"&user_pass="+password;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, urlLogin, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG,response.toString());
                try {
                    JSONArray array = response.getJSONArray("msg");
                    String result = array.getString(0);
                    String mobile = array.getString(1);
                    if(result.equals("success"))
                    {
                        // go to HomeActivity
                        SPController.getInstance(getActivity()).setMobile(mobile.trim());
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        // wrong mobile or password
                        AlertDialog.Builder alertDialog=new AlertDialog.Builder(getActivity());
                        alertDialog.setTitle("Attention");
                        alertDialog.setMessage("Invalid mobile / password!");
                        alertDialog.setCancelable(true);
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                dialog.cancel();
                            }
                        });
                        AlertDialog ad=alertDialog.create();
                        ad.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(req, TAG);
    }

    private void checkValidation(String mobile, String password) {
        if(mobile.isEmpty())
        {
            Mobile.setError("Please enter a phone number");
        }
        if(password.isEmpty())
        {
            Password.setError("Please enter a password");
        }
    }

    private boolean submitForm() {
        mobile=Mobile.getText().toString().trim();
        password=Password.getText().toString().trim();
        if(mobile.isEmpty() || password.isEmpty())
            return false;
        else
            return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
