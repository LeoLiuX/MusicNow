package com.example.xiao.musicnow.LoginPage.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.xiao.musicnow.HomePage.HomeActivity;
import com.example.xiao.musicnow.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by liuxi on 2017/1/20.
 */

public class LoginFragment extends Fragment {
    private String TAG = "LoginFragment";

    TextInputEditText Mobile, Password;
    Button signInBtn, resetBtn;
    String mobile, password;

    LoginButton loginButton;
    CallbackManager callbackManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        Mobile = (TextInputEditText) v.findViewById(R.id.mobile);
        Mobile.requestFocus();
        Password = (TextInputEditText) v.findViewById(R.id.password);
        signInBtn = (Button) v.findViewById(R.id.signInBtn);
        resetBtn = (Button) v.findViewById(R.id.reset);
        loginButton = (LoginButton) v.findViewById(R.id.login_button);
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
        loginButton.setReadPermissions(Arrays.asList("public_profile"));
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("facebook login",loginResult.toString());
                // go to HomeActivity
                Intent homeIntent=new Intent(getActivity(),HomeActivity.class);
                startActivity(homeIntent);
                getActivity().finish();
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "Facebook login cancel");
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
                Log.e(TAG, error.toString());
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
                    if(result.equals("success"))
                    {
                        // go to HomeActivity
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
}
