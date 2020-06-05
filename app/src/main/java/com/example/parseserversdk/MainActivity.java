package com.example.parseserversdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , View.OnKeyListener {

    TextView loginTextView;
    Boolean signupModeActive = true;
    EditText passwordEditText;
    EditText usernameEditText;

    public void gotoUserList(){
        Intent intent = new Intent(getApplicationContext(),userListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent event) {
        if (i == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN ){
            signUpClicked(view);
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.loginTextView) {
            Button signUpButton = findViewById(R.id.signUpButton);
            if (signupModeActive) {
                signupModeActive = false;
                signUpButton.setText("Login");
                loginTextView.setText("Sign Up");
            } else {
                signupModeActive = true;
                signUpButton.setText("Sign Up");
                loginTextView.setText("Login");
            }
        }else if (view.getId() == R.id.logoImageView || view.getId() == R.id.loginPageLayout){
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    public void signUpClicked (View view){


        if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){
            Toast.makeText(this, "Enter Username and Password",Toast.LENGTH_SHORT).show();
        }else{
            if (signupModeActive) {
                ParseUser user = new ParseUser();
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("SignUp", "Successful");
                            gotoUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                //Login script
                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user != null) {
                            Log.i("Login", "Done done!!!");
                            gotoUserList();
                        }else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("NotInstagram");

        loginTextView = findViewById(R.id.loginTextView);
        loginTextView.setOnClickListener(this);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        ImageView logoImageView = findViewById(R.id.logoImageView);
        ConstraintLayout loginPageLayout = findViewById(R.id.loginPageLayout);
        logoImageView.setOnClickListener(this);
        loginPageLayout.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null){
            gotoUserList();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}
