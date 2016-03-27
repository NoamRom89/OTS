package com.example.android.ots.general;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.ots.activities.LoginActivity;
import com.example.android.ots.R;
import com.example.android.ots.activities.AboutActivity;
import com.example.android.ots.activities.ManageTeamActivity;
import com.example.android.ots.activities.WaitingTasksActivity;


public abstract class Toolbar extends AppCompatActivity {
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(!LoginController.isAdmin())
            menu.findItem(R.id.manageMembers).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch(id){
            case R.id.showTasks:
                intent = new Intent(this,WaitingTasksActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                LoginController.logout();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.manageMembers:
                intent = new Intent(this, ManageTeamActivity.class);
                startActivity(intent);
                break;
            case R.id.aboutSection:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
