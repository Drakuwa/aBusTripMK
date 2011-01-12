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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class main_menu extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        
        ImageView mapa = (ImageView)findViewById(R.id.mapaButton);
        mapa.setOnClickListener(new OnClickListener() {  
   	    public void onClick(View v2) {  
    	   	 Intent myIntent = new Intent();
           	 myIntent.setClassName("com.app.busmk", "com.app.busmk.baraj_mapa");
           	 startActivity(myIntent);
   		     }  
   		 });
        
        TextView txtmapa = (TextView)findViewById(R.id.mapa_text);
        txtmapa.setOnClickListener(new OnClickListener() {  
   	    public void onClick(View v2) {  
    	   	 Intent myIntent = new Intent();
           	 myIntent.setClassName("com.app.busmk", "com.app.busmk.baraj_mapa");
           	 startActivity(myIntent);
   		     }  
   		 });
        
        ImageView lista = (ImageView)findViewById(R.id.listaButton);
        lista.setOnClickListener(new OnClickListener() {  
   	    public void onClick(View v2) {  
    	   	 Intent myIntent = new Intent();
           	 myIntent.setClassName("com.app.busmk", "com.app.busmk.baraj_lista");
           	 startActivity(myIntent);
   		     }  
   		 });
        
        TextView txtlista = (TextView)findViewById(R.id.lista_text);
        txtlista.setOnClickListener(new OnClickListener() {  
   	    public void onClick(View v2) {  
    	   	 Intent myIntent = new Intent();
           	 myIntent.setClassName("com.app.busmk", "com.app.busmk.baraj_lista");
           	 startActivity(myIntent);
   		     }  
   		 });
        
        
    }
}