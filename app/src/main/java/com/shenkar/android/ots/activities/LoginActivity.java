package com.shenkar.android.ots.activities;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shenkar.android.ots.R;
import com.shenkar.android.ots.controllers.CreateTeamController;
import com.shenkar.android.ots.controllers.LoginListener;
import com.shenkar.android.ots.general.Constants;
import com.shenkar.android.ots.general.LoginController;
import com.shenkar.android.ots.general.TeamMember;

//Parse Imports
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class LoginActivity extends AppCompatActivity implements LoginListener {
    private TextView nameText;
    private TextView passwordText;
    private TextView phoneText;
    private Button signUpButton;
    private LoginController loginController;
    private boolean hasAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        getSupportActionBar().hide();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("admin", true);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                if (e == null) {
                    if(objects.size() > 0){
                        hasAdmin=true;
                    }
                    else{
                        ((TextView)findViewById(R.id.admin)).setText(R.string.admin_notice);
                            signUpButton.setText(R.string.sign_up);
                    }

                }
            }
        });

        loginController = new LoginController(this);

        if (loginController.isLoggedIn()) {
            Intent intent = new Intent(this, WaitingTasksActivity.class);
            startActivity(intent);
        }

        nameText = (TextView) findViewById(R.id.userName);
        passwordText = (TextView) findViewById(R.id.pass);
        signUpButton = (Button)findViewById(R.id.signIn);

        final CreateTeamController controller = new CreateTeamController(this);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasAdmin)
                    loginController.login(nameText.getText().toString(), passwordText.getText().toString());
                else{
                    controller.setMember(new TeamMember(nameText.getText().toString(),"appAdmin@gmail.com", passwordText.getText().toString()),true);
                    Intent intent = new Intent(LoginActivity.this, CreateTeamActivity.class);
                    startActivity(intent);
                }

            }
        });

        if (!isNetworkConnected()) {
            Toast.makeText(this,"There are no active networks!",Toast.LENGTH_LONG).show();
        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null) {
            return true;
        } else
            return false;
    }

    @Override
    public void onUpdate(int code) {
        if (code == Constants.USER_LOGGED_IN) {
            Intent intent = new Intent(this, WaitingTasksActivity.class);
            startActivity(intent);
        }

        if (code == Constants.USER_LOGGED_IN_FAILED) {
            Toast.makeText(this,"Login failed, try again!",Toast.LENGTH_LONG).show();
        }
    }
}
