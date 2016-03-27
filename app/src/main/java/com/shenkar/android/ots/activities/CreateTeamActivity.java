package com.shenkar.android.ots.activities;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.shenkar.android.ots.R;
import com.shenkar.android.ots.general.LoginController;
import com.shenkar.android.ots.controllers.CreateTeamController;
import com.shenkar.android.ots.controllers.ICreateTeamController;
import com.shenkar.android.ots.general.TeamMember;
import com.shenkar.android.ots.general.Toolbar;
import java.util.ArrayList;

public class CreateTeamActivity extends Toolbar {
    private EditText name;
    private EditText mail;
    private EditText phone;
    private String teamName;
    private Button sendButton;
    private Button addButton;
    private ArrayList<TeamMember> teamMemberArray;
    private ICreateTeamController controller;
    private int memberNumberCounter;
    private TextView memberNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_a_team_activity);
        controller = new CreateTeamController(this);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
           if(extras.getBoolean("add")){
               ((EditText)findViewById(R.id.teamNameVal)).setVisibility(View.GONE);
           }
            ((TextView)findViewById(R.id.makeTeam)).setText(R.string.add_members);
        }
        name = (EditText)findViewById(R.id.memberNameVal);
        mail = (EditText)findViewById(R.id.member_mail_input);
        phone = (EditText)findViewById(R.id.member_phone_input);
        teamMemberArray = new ArrayList<TeamMember>();
        addButton = (Button)findViewById(R.id.plus_button);
        sendButton = (Button)findViewById(R.id.send_button);
        memberNumberCounter=1;
        memberNumber = (TextView)findViewById(R.id.member_num);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //this is called when admin press on plus button
            public void onClick(View v) {
                if (addMember()) {
                    memberNumber.setText(String.valueOf(++memberNumberCounter));
                    name.setText("");      // reset inputs
                    mail.setText("");      //
                    phone.setText("");     //
                }
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //this is called when admin press on send button
            public void onClick(View v) {
                if (addMember()){
                    LoginController.logout();
                    Intent intent = new Intent(CreateTeamActivity.this, LoginActivity.class);
                    startActivity(intent);
                    sendMails();
                }

//                Intent intent = new Intent(CreateTeamActivity.this, ManageTeamActivity.class);
//                startActivity(intent);
            }
        });
    }
    //sends invitations mails to the team , with link to the app on google play
    public void sendMails(){
        String mails="";
        for (TeamMember t:teamMemberArray) {
            controller.setMember(t,false);
            mails+="\"";
            mails+=t.getEmail();
            mails+="\",";
        }
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{mails});
        i.putExtra(Intent.EXTRA_SUBJECT, "Invitation to Join OTS team");
        i.putExtra(Intent.EXTRA_TEXT, "Hi, You have been invited to be a team member in an OTS Team created by me.\n" +
                "Use this link to download and install the App from Google Play: (don't forget to use your phone number as password)\n\n" +
        "https://play.google.com/store/apps/details?id=com.shenkar.android.ots");
        try {
            startActivity(Intent.createChooser(i, "Send mail"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CreateTeamActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    //Verification that email is valid
    public boolean verify(TeamMember member){
        String mail = member.getEmail();
        if (mail.isEmpty()||!(android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches())) {
                new AlertDialog.Builder(this).setTitle("invalid mail").setMessage("please enter a valid email address").setNeutralButton("Close", null).show();
                return false;
        }
        String phone = member.getPhone();
        if (phone.contains("[a-zA-Z]+") || phone.length() < 10) {
            new AlertDialog.Builder(this).setTitle("invalid phone").setMessage("please enter a valid phone").setNeutralButton("Close", null).show();
            return false;
        }
        return true;
    }
    //creates a team member, verifies the inputs, and creates the member on the parse DB.
    public boolean addMember(){
       // teamName = ((EditText)findViewById(R.id.teamNameVal)).getText().toString();
        TeamMember teamMember = new TeamMember(name.getText().toString(),mail.getText().toString(),phone.getText().toString());
        if (verify(teamMember)) {
            teamMemberArray.add(teamMember);
            return true;
        }
        return false;
    }


}
