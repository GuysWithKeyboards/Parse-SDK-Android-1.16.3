/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    private Boolean exit = false;

    TextView changeMode;

    EditText username;
    EditText password;
    EditText confirmPassword;

    Button button;

    ImageView logo;
    ImageView background;

    Boolean signUpModeActive = false;



    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.changeMode){

            if(signUpModeActive == false){

                password.setText("");
                confirmPassword.setText("");

                signUpModeActive = true;

                confirmPassword.setVisibility(View.VISIBLE);

                //confirmPassword.setEnabled(true);
                //confirmPassword.setFocusableInTouchMode(true);       //GG
                //confirmPassword.setFocusable(true);

                changeMode.animate().translationY(175f).setDuration(300).setInterpolator(new
                        AccelerateDecelerateInterpolator());
                button.animate().translationY(175f).setDuration(300).setInterpolator(new
                        AccelerateDecelerateInterpolator());

                confirmPassword.animate().alpha(1).setDuration(300);

                //username.setHint("Enter your username");
                //password.setHint("Enter your password");
                button.setText("Sign Up");
                changeMode.setText("Or, Login");


            }else {

                password.setText("");
                confirmPassword.setText("");

                signUpModeActive = false;

                confirmPassword.animate().alpha(0).setDuration(300);

                confirmPassword.setVisibility(View.GONE);

                //confirmPassword.setEnabled(false);
                //confirmPassword.setFocusableInTouchMode(false);     // GG
                //confirmPassword.setFocusable(false);

                changeMode.animate().translationYBy(-175f).setDuration(300).setInterpolator(new
                        AccelerateDecelerateInterpolator());
                button.animate().translationYBy(-175f).setDuration(300).setInterpolator(new
                        AccelerateDecelerateInterpolator());



                //username.setHint("Username");
                //password.setHint("Password");
                button.setText("Login");
                changeMode.setText("New to GwK? Sign up");


            }


        }else if (v.getId() == R.id.background || v.getId() == R.id.logo) {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService
                    (INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == 66 && event.getAction() == 0) {
            if ((v.getId() == R.id.confirmPassword && signUpModeActive) || (v.getId() == R.id.password && !signUpModeActive)){

                login(v);

            }



        }


        return false;
    }

    public void login(View view) {

        if (username.getText().toString().matches("") || password.getText().toString().matches
                ("")) {

            Toast.makeText(this, "A Username and Password are required", Toast.LENGTH_SHORT).show();

        } else if (signUpModeActive) {

            ParseUser user = new ParseUser();

            user.setUsername(username.getText().toString());
            if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                Toast.makeText(this, "Passwords doesn't match. Try again", Toast.LENGTH_SHORT)
                        .show();

            } else {
                user.setPassword(password.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {
                           Toast.makeText(MainActivity.this, "Signed up as " + username.getText()
                                 .toString(), Toast
                                 .LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    }
                });
            }
        }


        else {
            ParseUser.logInInBackground(username.getText().toString(), password.getText()
                    .toString(), new LogInCallback() {


                @Override
                public void done(ParseUser user, ParseException e) {

                    if (user != null) {

                        Toast.makeText(MainActivity.this, "Logged In successfully as " +
                                username.getText().toString(), Toast
                                .LENGTH_SHORT).show();


                    } else {

                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }
    }


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username);

        password = (EditText) findViewById(R.id.password);

        confirmPassword = (EditText) findViewById(R.id.confirmPassword);

        //confirmPassword.setFocusableInTouchMode(false);
        //.setFocusable(false);
        //confirmPassword.setEnabled(false);

        button = (Button) findViewById(R.id.button);

        logo = (ImageView) findViewById(R.id.logo);

        background = (ImageView) findViewById(R.id.background);

        changeMode = (TextView) findViewById(R.id.changeMode);


        logo.setOnClickListener(this);
        background.setOnClickListener(this);
        changeMode.setOnClickListener(this);

        password.setOnKeyListener(this);
        confirmPassword.setOnKeyListener(this);




        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }


}