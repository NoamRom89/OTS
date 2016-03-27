package com.example.android.ots.controllers;
import android.content.Context;
import android.util.Log;
import com.example.android.ots.general.TeamMember;
import com.example.android.ots.dao.DAO;
import com.example.android.ots.dao.IDataAccess;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class CreateTeamController implements ICreateTeamController {
    private IDataAccess dao;
    private Context context;
    public CreateTeamController(Context context) {
        this.context = context;
        dao = DAO.getInstance(context.getApplicationContext());
    }

    @Override
    public void setMember(TeamMember member, boolean admin) {
        ParseUser user = new ParseUser();
        user.setUsername(member.getName());
        user.setPassword(member.getPhone());
        user.setEmail(member.getEmail());
// other fields can be set just like with ParseObject
        if(admin)
            user.put("admin", true);
        else
            user.put("admin", false);
        Log.e("in", "in");
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.e("yep", "yep");// Hooray! Let them use the app now.
                } else {
                    Log.e("no","no");
                    Log.e("tg",e.getMessage());
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }
}
