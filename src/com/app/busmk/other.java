// Created by Drakuwa
/**
 * 
 * @author Bojan Ilievski - Drakuwa
 *
 */
package com.app.busmk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class other extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other);
        
        ImageView linii = (ImageView)findViewById(R.id.linii);
        linii.setOnClickListener(new OnClickListener() {  
   	    public void onClick(View v2) {  
   	    		create_list();
   		     }  
   		 });

        ImageView raspored = (ImageView)findViewById(R.id.raspored);
        raspored.setOnClickListener(new OnClickListener() {  
   	    public void onClick(View v2) {  
	    	 Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://jsp.com.mk/VozenRed.aspx"));
	    	 startActivity(myIntent);  
   		     }  
   		 });
    }
    private void create_list(){
    	final CharSequence[] items = {
    			"2", 
    			"3", 
    			"5",
    			"7",
    			"8",
    			"12",
    			"15",
    			"19",
    			"22",
    			"24",
    			"41",
    			"65b"
    			};
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
   	        builder.setTitle("Избери линија:");
   	        builder.setItems(items, new DialogInterface.OnClickListener() {
   	            public void onClick(DialogInterface dialog, int item) {
   	                Intent myIntent = new Intent();
   	                myIntent.setClassName("com.app.busmk", "com.app.busmk.a"+items[item]);
   	                //Log.d("aaa", "Kliknat item:" + items[item]+";;;");
   	                startActivity(myIntent);
   	            }
   	        });
   	        AlertDialog alert = builder.create();
   	        alert.show();
    }
}
