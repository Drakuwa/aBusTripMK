// Created by Drakuwa
/**
 * 
 * @author Bojan Ilievski - Drakuwa
 *
 */
package com.app.busmk;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class main extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, main_menu.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("main").setIndicator("Главно Мени",
                res.getDrawable(R.drawable.ic_tab_main))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, other.class);
        spec = tabHost.newTabSpec("other").setIndicator("Останато...",
                res.getDrawable(R.drawable.ic_tab_other))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, about.class);
        spec = tabHost.newTabSpec("about").setIndicator("За Апликацијата...",
                res.getDrawable(R.drawable.ic_tab_about))
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);

        
    }
}