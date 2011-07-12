// Created by Drakuwa
/**
 * 
 * @author Bojan Ilievski - Drakuwa
 *
 */
package com.app.busmk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splash extends Activity {
	
	protected boolean _active = true;
	protected int _splashTime = 3000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        
        new Handler().postDelayed(new Runnable(){
        	public void run() {
        	Intent novIntent = new Intent(splash.this,main.class);
        	startActivity(novIntent);
        	finish();

        	}},3000);    
    }
    
}