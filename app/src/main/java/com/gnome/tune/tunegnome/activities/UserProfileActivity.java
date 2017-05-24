package com.gnome.tune.tunegnome.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gnome.tune.tunegnome.R;
import com.gnome.tune.tunegnome.storage.ProfilesDatabaseHelper;
import com.gnome.tune.tunegnome.utils.UserProfile;

import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private List<UserProfile> profiles;
    private ListView listView;
    private ArrayAdapter<UserProfile> profilesListAdapter;

    private ProfilesDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        dbHelper = new ProfilesDatabaseHelper(this);
        listView = (ListView) findViewById(R.id.profilesList);

//        dbHelper.onUpgrade(dbHelper.getReadableDatabase(), 2,3);
//        dbHelper.onCreate(dbHelper.getReadableDatabase());
        setProfilesFromDb();
        addItemOnclickListener();
    }

    private void addItemOnclickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                UserProfile profileToDelete = (UserProfile) adapter.getItemAtPosition(position);
                dbHelper.deleteProfile(profileToDelete.getId());

                setProfilesFromDb();
            }
        });
    }

    private void setProfilesFromDb() {
        profiles = dbHelper.getAllProfiles();
        profilesListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, profiles);
        listView.setAdapter(profilesListAdapter);
    }


    public void addNewProfile(View view) {
        startActivity(new Intent(this, AddProfileActivity.class));
    }

    public void backToMain(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
