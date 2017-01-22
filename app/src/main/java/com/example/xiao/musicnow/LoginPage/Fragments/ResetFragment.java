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
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.xiao.musicnow.Controller.AppController;
import com.example.xiao.musicnow.LoginPage.PasswordValidator;
import com.example.xiao.musicnow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liuxi on 2017/1/20.
 */

public class ResetFragment extends Fragment{
    private String TAG = "ResetFragment";
    PasswordValidator validator;
    TextInputEditText Mobile, OldPassword, NewPassword, ConfirmPassword;
    Button submitBtn;
    String mobile, oldP, newP, conP;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validator = new PasswordValidator();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reset, container, false);

        Mobile = (TextInputEditText) v.findViewById(R.id.mobileNumber);
        OldPassword = (TextInputEditText) v.findViewById(R.id.oldPassword);
        NewPassword = (TextInputEditText) v.findViewById(R.id.newPassword);
        ConfirmPassword = (TextInputEditText) v.findViewById(R.id.confirmPassword);
        submitBtn = (Button) v.findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = Mobile.getText().toString();
                oldP = OldPassword.getText().toString();
                newP = NewPassword.getText().toString();
                conP = ConfirmPassword.getText().toString();
                if(!mobile.isEmpty() && !oldP.isEmpty() && !newP.isEmpty() && !conP.isEmpty())
                {
                    if(validator.validate(oldP) && validator.validate(newP) && validator.validate(conP))
                    {
                        if(newP.equals(conP))
                        {
                            checkPassword(mobile, oldP, newP);
                        }
                        else
                        {
                            ConfirmPassword.setError("Inconsistent with new password!");
                        }
                    }
                    else
                    {
                        if(!validator.validate(oldP))
                            OldPassword.setError(validator.PASSWORD_DISCRIPTION);
                        if(!validator.validate(newP))
                            NewPassword.setError(validator.PASSWORD_DISCRIPTION);
                        if(!validator.validate(conP))
                            ConfirmPassword.setError(validator.PASSWORD_DISCRIPTION);
                    }
                }
                else
                {
                    if(mobile.isEmpty())
                        Mobile.setError("Please enter the phone number!");
                    if(oldP.isEmpty())
                        OldPassword.setError("Please enter the provisional password!");
                    if(newP.isEmpty())
                        NewPassword.setError("Please enter the new password!");
                    if(conP.isEmpty())
                        ConfirmPassword.setError("Please enter the confirm new password!");
                }
            }
        });

        return v;
    }

    private void checkPassword(String mobile, String oldP, String newP) {
        String urlReset = "http://rjtmobile.com/ansari/rjt_music/music_app/reset_pass.php?&user_mobile="+mobile
                +"&user_pass="+oldP+"&newpassword="+newP;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, urlReset, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    JSONArray array = response.getJSONArray("msg");
                    String result = array.getString(0);
                    if(result.equals("password reset successfully"))
                    {
                        // go back to Login Fragment
                        LoginFragment fragment = new LoginFragment();
                        FragmentManager fm=getFragmentManager();
                        FragmentTransaction ft=fm.beginTransaction();
                        ft.replace(R.id.pager,fragment).commit();
                    }
                    else if(result.equals("old password mismatch"))
                    {
                        showAlert("Old password mismatch!");
                    }
                    else if(result.equals("wrong mobile number"))
                    {
                        showAlert("Wrong mobile number!");
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

    private void showAlert(String message) {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Attention");
        alertDialog.setMessage(message);
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
