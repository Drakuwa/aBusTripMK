// Created by Drakuwa
/**
 * 
 * @author Bojan Ilievski - Drakuwa
 *
 */
package com.app.busmk;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

		boolean exists = (new File("/data/data/com.app.busmk/notwelcomefirst"))
				.exists();

		if (!exists) {
			// Welcome note...
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"Добредојдовте во aBusTripMK2, "
							+ "за повеќе информации и начин на користење, "
							+ "погледнете во 'За Апликацијата' делот ").setIcon(
					R.drawable.icon).setTitle(R.string.app_name).setCancelable(
					false).setPositiveButton("OK..",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
			try {
				new File("/data/data/com.app.busmk/notwelcomefirst")
						.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		ImageView mapa = (ImageView) findViewById(R.id.mapaButton);
		mapa.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk",
						"com.app.busmk.baraj_mapa");
				startActivity(myIntent);
			}
		});

		TextView txtmapa = (TextView) findViewById(R.id.mapa_text);
		txtmapa.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk",
						"com.app.busmk.baraj_mapa");
				startActivity(myIntent);
			}
		});

		ImageView lista = (ImageView) findViewById(R.id.listaButton);
		lista.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk",
						"com.app.busmk.baraj_lista");
				startActivity(myIntent);
			}
		});

		TextView txtlista = (TextView) findViewById(R.id.lista_text);
		txtlista.setOnClickListener(new OnClickListener() {
			public void onClick(View v2) {
				Intent myIntent = new Intent();
				myIntent.setClassName("com.app.busmk",
						"com.app.busmk.baraj_lista");
				startActivity(myIntent);
			}
		});

	}
}