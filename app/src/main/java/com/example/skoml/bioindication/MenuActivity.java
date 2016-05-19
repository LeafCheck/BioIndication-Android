package com.example.skoml.bioindication;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.ecometr.app.R;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    Location location = null;
    private ResideMenu resideMenu;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemMap;
    private ResideMenuItem itemHistory;
    private ResideMenuItem itemSettings;
    private ResideMenuItem itemAbout;
    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            // Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            // Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);


        setUpMenu();

        if (savedInstanceState == null)
            changeFragment(new HomeFragment(), HomeFragment.class.getName());


    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.bg_screen);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome = new ResideMenuItem(this, R.drawable.ic_camera_black_48px, "Camera");
        itemMap = new ResideMenuItem(this, R.drawable.ic_map_black_48px, "Map");
        itemHistory = new ResideMenuItem(this, R.drawable.ic_event_note_black_48px, "History");
        itemSettings = new ResideMenuItem(this, R.drawable.ic_settings_black_48px, "Settings");
        itemAbout = new ResideMenuItem(this, R.drawable.ic_plus_one_black_48px, "About us");

        itemHome.setOnClickListener(this);
        itemMap.setOnClickListener(this);
        itemHistory.setOnClickListener(this);
        itemSettings.setOnClickListener(this);
        itemAbout.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemMap, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemHistory, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemAbout, ResideMenu.DIRECTION_RIGHT);

        // You can disable a direction by setting ->
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        if (view == itemHome) {
            changeFragment(new HomeFragment(), HomeFragment.class.getName());
        } else if (view == itemMap) {
            changeFragment(new MapFragment(), MapFragment.class.getName());
        } else if (view == itemHistory) {
            changeFragment(new HistoryFragment(), HistoryFragment.class.getName());
        } else if (view == itemSettings) {
            changeFragment(new SettingsFragment(),  SettingsFragment.class.getName());
        } else if (view == itemAbout) {
            changeFragment(new AboutFragment(), AboutFragment.class.getName());
        }
        resideMenu.closeMenu();
    }

    protected void changeFragment(Fragment targetFragment, String tag) {
        resideMenu.clearIgnoredViewList();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, tag)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu() {
        return resideMenu;
    }

    @Override
    protected void onStart() {
        super.onStart();
        startLocation();
    }

    @Override
    protected void onStop() {
        stopLocation();
        super.onStop();
    }

    private void startLocation() {
        new SmartLocation.Builder(this).build().location().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                MenuActivity.this.location = location;
            }
        });

    }

    private void stopLocation() {
        SmartLocation.with(this).location().stop();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getFragmentManager().findFragmentByTag(HomeFragment.class.getName());
        if(fragment!=null)
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
