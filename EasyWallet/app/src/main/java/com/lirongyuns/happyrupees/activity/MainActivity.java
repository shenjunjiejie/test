package com.lirongyuns.happyrupees.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.facebook.LoggingBehavior;
import com.facebook.internal.Logger;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lirongyuns.happyrupees.R;
import com.lirongyuns.happyrupees.annonation.SetActivity;
import com.lirongyuns.happyrupees.annonation.SetView;

@SetActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private long mExitTime;
    @SetView(R.id.nav_view)
    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.navigation_home,R.id.navigation_certification, R.id.navigation_me)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        LoggingBehavior appEvents = LoggingBehavior.APP_EVENTS;
        Logger.log(appEvents,"Main", "main");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(getApplicationContext(),"Press again to exit the Application", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}