package com.android.studentslist.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.studentslist.BroadcastToaster;
import com.android.studentslist.R;
import com.android.studentslist.fragments.AddContactFragment;
import com.android.studentslist.fragments.ListViewFragment;
import com.android.studentslist.fragments.ContactsFragment;
import com.android.studentslist.fragments.RecycleViewFragment;

public class MainActivity extends AppCompatActivity {
    BroadcastToaster broadcastToaster = new BroadcastToaster();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main, new ListViewFragment())
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(broadcastToaster, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(broadcastToaster);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        getMenuInflater().inflate(R.menu.contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_list:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_main, new ListViewFragment())
                        .commit();
                return true;
            case R.id.menu_recycle:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_main, new RecycleViewFragment())
                        .commit();
                return true;
            case R.id.contacts_menu:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_main, new ContactsFragment())
                        .commit();
                return true;
            case R.id.contact_item:
                new AddContactFragment().show(getSupportFragmentManager(), null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

