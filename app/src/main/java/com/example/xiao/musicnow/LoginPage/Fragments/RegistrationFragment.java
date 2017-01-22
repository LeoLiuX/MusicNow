package com.example.xiao.musicnow.LoginPage.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.android.volley.toolbox.StringRequest;
import com.example.xiao.musicnow.Controller.AppController;
import com.example.xiao.musicnow.LoginPage.controller.PasswordValidator;
import com.example.xiao.musicnow.R;

/**
 * Created by liuxi on 2017/1/20.
 */

public class RegistrationFragment extends Fragment{
    private String TAG = "Registration";
    TextInputEditText UserName, UserEmail, UserMobile, UserPassword;
    Button regBtn;
    String name, email, mobile, password;
    PasswordValidator validator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validator = new PasswordValidator();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registration, container,false);

        UserName = (TextInputEditText) v.findViewById(R.id.userName);
        UserEmail = (TextInputEditText) v.findViewById(R.id.userEmail);
        UserMobile = (TextInputEditText) v.findViewById(R.id.userMobile);
        UserPassword = (TextInputEditText) v.findViewById(R.id.userPassword);
        regBtn = (Button) v.findViewById(R.id.regBtn);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = UserName.getText().toString();
                email = UserEmail.getText().toString();
                mobile = UserMobile.getText().toString();
                password = UserPassword.getText().toString();
                if(!name.isEmpty() && !email.isEmpty() && !mobile.isEmpty() && !password.isEmpty())
                {
                    if(validator.validate(password))
                    {
                        register(name, email, mobile, password);
                    }
                    else
                    {
                        UserPassword.setError(validator.PASSWORD_DISCRIPTION);
                    }
                }
                else
                {
                    if(name.isEmpty())
                    {
                        UserName.setError("Please enter the user name");
                    }
                    if(email.isEmpty())
                    {
                        UserEmail.setError("Please enter the email address");
                    }
                    if(mobile.isEmpty())
                    {
                        UserMobile.setError("Please enter the phone number");
                    }
                    if(password.isEmpty())
                    {
                        UserPassword.setError("Please enter the password");
                    }
                }
            }
        });

        return v;
    }

    private void register(String name, String email, String mobile, String password) {
        String urlReg = "http://rjtmobile.com/ansari/rjt_music/music_app/registration.php?&user_name="+name
                +"&user_email="+email+"&user_mobile="+mobile+"&user_pass="+password;
        StringRequest req = new StringRequest(Request.Method.GET, urlReg, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                if(response.equals("successfully registered"))
                {
                    // go back to Login Fragment
                    LoginFragment fragment = new LoginFragment();
                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    ft.replace(R.id.pager,fragment).commit();
                }
                else
                {
                    // mobile number already exist
                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Attention");
                    alertDialog.setMessage("Mobile number already exist!");
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
}
