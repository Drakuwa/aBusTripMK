// Created by Drakuwa
/**
 * 
 * @author Bojan Ilievski - Drakuwa
 *
 */
package com.app.busmk;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class about extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        
        ImageView twitter = (ImageView)findViewById(R.id.twitter);
        twitter.setOnClickListener(new OnClickListener() {  
        	     public void onClick(View v2) {  
        	    	 
        	    	 Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/aBusTripMK"));
        	    	 startActivity(myIntent);  
        		     }  
        		 });
    }
}