// Created by Drakuwa
/**
 * 
 * @author Bojan Ilievski - Drakuwa
 *
 */
package com.app.busmk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
//import android.widget.Toast;

public class baraj_lista extends ExpandableListActivity {
	
	
	
	static final String naselbi[] = {
		  "Ѓ.Петров",
		  "Карпош",
		  "Центар",
		  "Чаир",
		  "Гази Баба",
		  "Аеродром",
		  "К.Вода",
		  "Бутел",
		  "Ш.Оризари"
		};
	
	static final String stanici=new String();
	
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.baraj_lista);
        
        ArrayList<String> gjorce = new ArrayList<String>();
        ArrayList<String> karpos = new ArrayList<String>();
        ArrayList<String> centar = new ArrayList<String>();
        ArrayList<String> cair = new ArrayList<String>();
        ArrayList<String> gazibaba = new ArrayList<String>();
        ArrayList<String> aerodrom = new ArrayList<String>();
        ArrayList<String> kvoda = new ArrayList<String>();
        ArrayList<String> butel = new ArrayList<String>();
        ArrayList<String> shutka = new ArrayList<String>();
        
        DataBaseHelper myDb = new DataBaseHelper(null);
        myDb = new DataBaseHelper(this);
 
        try {
 
        		myDb.createDataBase();
 
        } catch (IOException ioe) {
 
 			throw new Error("Unable to create database");
 
 		}
 
 		try {
 
 			myDb.openDataBase();
 
 		}catch(SQLException sqle){
 
 			throw sqle;
 
 		}

 		myDb.getReadableDatabase();

 			Cursor c = myDb.getStanici();
 			if (c.moveToFirst())
 			{
 				do {
 					String ime = c.getString(1);
 					String naselba = c.getString(4);
 					if(naselba.equalsIgnoreCase("Ѓ.Петров")&&ime.endsWith("А")){gjorce.add(ime);}
 					if(naselba.equalsIgnoreCase("Карпош")&&ime.endsWith("А")){karpos.add(ime);}
 					if(naselba.equalsIgnoreCase("Центар")&&ime.endsWith("А")){centar.add(ime);}
 					if(naselba.equalsIgnoreCase("Чаир")&&ime.endsWith("А")){cair.add(ime);}
 					if(naselba.equalsIgnoreCase("Гази Баба")&&ime.endsWith("А")){gazibaba.add(ime);}
 					if(naselba.equalsIgnoreCase("Аеродром")&&ime.endsWith("А")){aerodrom.add(ime);}
 					if(naselba.equalsIgnoreCase("К.Вода")&&ime.endsWith("А")){kvoda.add(ime);}
 					if(naselba.equalsIgnoreCase("Бутел")&&ime.endsWith("А")){butel.add(ime);}
 					if(naselba.equalsIgnoreCase("Ш.Оризари")&&ime.endsWith("А")){shutka.add(ime);}    			
 				} while (c.moveToNext());	
 			}
 		
 		myDb.close();
 		
 		String gjorcetest [] = (String []) gjorce.toArray (new String [gjorce.size ()]);
 		String karpostest [] = (String []) karpos.toArray (new String [karpos.size ()]);
 		String centartest [] = (String []) centar.toArray (new String [centar.size ()]);
 		String cairtest [] = (String []) cair.toArray (new String [cair.size ()]);
 		String gazibabatest [] = (String []) gazibaba.toArray (new String [gazibaba.size ()]);
 		String aerodromtest [] = (String []) aerodrom.toArray (new String [aerodrom.size ()]);
 		String kvodatest [] = (String []) kvoda.toArray (new String [kvoda.size ()]);
 		String buteltest [] = (String []) butel.toArray (new String [butel.size ()]);
 		String shutkatest [] = (String []) shutka.toArray (new String [shutka.size ()]);
 		
 		final String stanici[][] = {
 				gjorcetest, 
 				karpostest, 
 				centartest,
 				cairtest,
 				gazibabatest,
 				aerodromtest,
 				kvodatest,
 				buteltest,
 				shutkatest
 				};
        
        final SimpleExpandableListAdapter expListAdapter =
			new SimpleExpandableListAdapter(
				this,
				createGroupList(),	// groupData describes the first-level entries
				R.layout.child_row,	// Layout for the first-level entries
				new String[] { "Naselba" },	// Key in the groupData maps to display
				new int[] { R.id.stanica },		// Data under "colorName" key goes into this TextView
				createChildList(stanici),	// childData describes second-level entries
				R.layout.child_row,	// Layout for second-level entries
				new String[] { "Stanica" },	// Keys in childData maps to display
				new int[] { R.id.stanica }	// Data under the keys above go into these TextViews
			);
		setListAdapter( expListAdapter );        
		
		ExpandableListView lv = getExpandableListView();
        lv.setTextFilterEnabled(true);
        
        lv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Object o = (Object)expListAdapter.getChild(groupPosition, childPosition);
                // perform work on child object here
                
                String text = o.toString().trim();
                String txt = text.substring(text.indexOf('=') + 1, text.indexOf('}'));
                //Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
                
                createDialog(txt);
                
                return true;
            }
        });      
    }
        
    private List createGroupList() {
  	  ArrayList result = new ArrayList();
  	  for( int i = 0 ; i < naselbi.length; ++i ) {
  		HashMap m = new HashMap();
  	    m.put( "Naselba",naselbi[i] );
  		result.add( m );
  	  }
  	  return (List)result;
      }
      
    private List createChildList(String stanici[][]) {
    	ArrayList result = new ArrayList();
    	for( int i = 0 ; i < stanici.length ; ++i ) {
    		ArrayList secList = new ArrayList();
    		for( int n = 0 ; n < stanici[i].length ; n += 1 ) {
    	    HashMap child = new HashMap();
    		child.put( "Stanica", stanici[i][n]);
    		secList.add( child );
    		}
    	  result.add( secList );
    	}
    	return result;
      }
      	 
      	 
         private void createDialog(final String txt){  
           	 AlertDialog.Builder builder = new AlertDialog.Builder(this);  
           	 builder.setMessage("Кликнавте на: " + txt + ", Дали сакате да ги погледнете линиите кои што поминуваат таму?")
           	 	  .setIcon(R.drawable.icon)
                  .setTitle(R.string.app_name)
           	      .setCancelable(false)  
           	      .setPositiveButton("Да..",  
           	           new DialogInterface.OnClickListener(){  
           	           public void onClick(DialogInterface dialog, int id){
           	        	   
           	        	ArrayList<String> korisni_linii = new ArrayList<String>();
           	        	   
           	        	DataBaseHelper Db = new DataBaseHelper(null);
           	        	Db = new DataBaseHelper(getApplicationContext());
           	        	Db.openDataBase();
           	        	
           	        	Cursor c = Db.getStanica(txt);
           	        	if (c.moveToFirst())
                    	{
                    		String _id = c.getString(0);	//id na kliknata stanica...
                    		Cursor c2 = Db.getUsefulLinii(_id);
                    		if (c2.moveToFirst())
                        	{
                        		do {
                        			String id_korisna_linija = c2.getString(0);
                        			Cursor c3 = Db.getLinija(id_korisna_linija);
                        			String ime_linija = c3.getString(0);
                        			korisni_linii.add(ime_linija);
                        		} while(c2.moveToNext());
                        	}
                    	}
           	        	
           	        	String linii = "Следниве линии поминуваат на кликнатата станица: "+ korisni_linii;
           	        	
           	        	final_dialog(linii);
           	        	
           	        	Db.close();
           	           }
 
           	      });  
           	      builder.setNegativeButton("Откажи",  
           	           new DialogInterface.OnClickListener(){  
           	           public void onClick(DialogInterface dialog, int id){  
           	                dialog.cancel();  
           	           }  
           	      });  
           	 AlertDialog alert = builder.create();  
           	 alert.show();  
           	 }
         
         private void final_dialog(final String txt){  
           	 AlertDialog.Builder builder = new AlertDialog.Builder(this);  
           	 builder.setMessage(txt)
           	 	  .setIcon(R.drawable.marker_default)
                  .setTitle(R.string.app_name)
           	      .setCancelable(false)  
           	      .setPositiveButton("OK..",  
           	           new DialogInterface.OnClickListener(){  
           	           public void onClick(DialogInterface dialog, int id){
           	        }
           	      });  
           	 AlertDialog alert = builder.create();  
           	 alert.show();  
           	 }
    
}