// Created by Drakuwa
/**
 * 
 * @author Bojan Ilievski - Drakuwa
 *
 */
package com.app.busmk;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.util.constants.OpenStreetMapConstants;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.MyLocationOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlayItem;
import org.andnav.osm.views.util.HttpUserAgentHelper;
import org.andnav.osm.views.util.OpenStreetMapRendererInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle; //import android.util.Log;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class baraj_mapa extends Activity implements OpenStreetMapConstants {

	private LocationManager lm;
	private static final int MENU_MY_LOCATION = Menu.FIRST;
	private static final int MENU_MAP_MODE = MENU_MY_LOCATION + 1;
	private static final int MENU_CLEAR_MAP = MENU_MAP_MODE + 1;
	private static final int MENU_ABOUT = MENU_CLEAR_MAP + 1;

	private static final int DIALOG_ABOUT_ID = 1;

	private SharedPreferences mPrefs;
	private OpenStreetMapView mOsmv;
	private MyLocationOverlay mLocationOverlay;

	ProgressDialog myProgressDialog = null;

	private OpenStreetMapViewItemizedOverlay<OpenStreetMapViewOverlayItem> mMyLocationOverlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		boolean exists = (new File("/data/data/com.app.busmk/notfirst"))
				.exists();
		if (!exists) {
			mOsmv.getController().setZoom(15);
			mOsmv.scrollTo(41995912, 21431454);
			try {
				new File("/data/data/com.app.busmk/notfirst").createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d("xxxx", "First RUN!!! :D");
		}

		DataBaseHelper myDb = new DataBaseHelper(null);
		myDb = new DataBaseHelper(this);

		try {
			myDb.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}

		try {
			myDb.openDataBase();
		} catch (SQLException sqle) {
			throw sqle;
		}

		{
			myDb.getReadableDatabase();
		}

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			createGpsDisabledAlert();
		}

		mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

		final RelativeLayout rl = new RelativeLayout(this);

		this.mOsmv = new OpenStreetMapView(this, OpenStreetMapRendererInfo
				.values()[mPrefs.getInt(PREFS_RENDERER,
				OpenStreetMapRendererInfo.MAPNIK.ordinal())]);
		this.mLocationOverlay = new MyLocationOverlay(this.getBaseContext(),
				this.mOsmv);
		this.mOsmv.setBuiltInZoomControls(true);
		this.mOsmv.getOverlays().add(this.mLocationOverlay);
		rl.addView(this.mOsmv, new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		this.setContentView(rl);

		mOsmv.getController().setZoom(mPrefs.getInt(PREFS_ZOOM_LEVEL, 1));
		mOsmv.scrollTo(mPrefs.getInt(PREFS_SCROLL_X, 0), mPrefs.getInt(
				PREFS_SCROLL_Y, 0));

		/* Itemized Overlay */
		{
			final ArrayList<OpenStreetMapViewOverlayItem> items = new ArrayList<OpenStreetMapViewOverlayItem>();

			// pristapuvanje na baza
			{
				Cursor c = myDb.getStanici();
				if (c.moveToFirst()) {
					do {
						// String id = c.getString(0);
						String ime = c.getString(1);

						double flon = c.getDouble(2);
						double flat = c.getDouble(3);

						GeoPoint stanica = new GeoPoint(flon, flat);
						items.add(new OpenStreetMapViewOverlayItem(" ", ime,
								stanica));

						// Log.d("xxx", "ID: " + id + "  ime=" + ime +
						// " lat/lon=" + flon + " / " + flat);

					} while (c.moveToNext());
				}
				myDb.close();
			}

			this.mMyLocationOverlay = new OpenStreetMapViewItemizedOverlay<OpenStreetMapViewOverlayItem>(
					this,
					items,
					new OpenStreetMapViewItemizedOverlay.OnItemTapListener<OpenStreetMapViewOverlayItem>() {
						public boolean onItemTap(int index,
								OpenStreetMapViewOverlayItem item) {

							create_dialog(item);

							return true;
						}
					});
			this.mOsmv.getOverlays().add(this.mMyLocationOverlay);
		}
	}

	@Override
	protected void onPause() {
		SharedPreferences.Editor edit = mPrefs.edit();
		edit.putInt(PREFS_RENDERER, mOsmv.getRenderer().ordinal());
		edit.putInt(PREFS_SCROLL_X, mOsmv.getScrollX());
		edit.putInt(PREFS_SCROLL_Y, mOsmv.getScrollY());
		edit.putInt(PREFS_ZOOM_LEVEL, mOsmv.getZoomLevel());
		edit.putBoolean(PREFS_SHOW_LOCATION, mLocationOverlay
				.isMyLocationEnabled());
		edit.putBoolean(PREFS_FOLLOW_LOCATION, mLocationOverlay
				.isLocationFollowEnabled());
		edit.commit();

		this.mLocationOverlay.disableMyLocation();

		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mOsmv.setRenderer(OpenStreetMapRendererInfo.values()[mPrefs.getInt(
				PREFS_RENDERER, OpenStreetMapRendererInfo.MAPNIK.ordinal())]);
		if (mPrefs.getBoolean(PREFS_SHOW_LOCATION, false))
			this.mLocationOverlay.enableMyLocation();
		this.mLocationOverlay.followLocation(mPrefs.getBoolean(
				PREFS_FOLLOW_LOCATION, true));
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu pMenu) {
		pMenu.add(0, MENU_MY_LOCATION, Menu.NONE, R.string.my_location)
				.setIcon(android.R.drawable.ic_menu_mylocation);

		{
			int id = 1000;
			final SubMenu mapMenu = pMenu.addSubMenu(0, MENU_MAP_MODE,
					Menu.NONE, R.string.map_mode).setIcon(
					android.R.drawable.ic_menu_mapmode);

			for (OpenStreetMapRendererInfo renderer : OpenStreetMapRendererInfo
					.values()) {
				mapMenu.add(MENU_MAP_MODE, id++, Menu.NONE,
						getString(renderer.NAME));
			}
			mapMenu.setGroupCheckable(MENU_MAP_MODE, true, true);
		}
		pMenu.add(0, MENU_CLEAR_MAP, Menu.NONE, R.string.clear_map).setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		pMenu.add(0, MENU_ABOUT, Menu.NONE, R.string.about).setIcon(
				android.R.drawable.ic_menu_info_details);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		int id = mOsmv.getRenderer().ordinal();
		menu.findItem(1000 + id).setChecked(true);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case MENU_MY_LOCATION:
			this.mLocationOverlay.followLocation(true);
			this.mLocationOverlay.enableMyLocation();
			Location lastFix = this.mLocationOverlay.getLastFix();
			if (lastFix != null)
				this.mOsmv.setMapCenter(new GeoPoint(lastFix));
			return true;

		case MENU_MAP_MODE:
			this.mOsmv.invalidate();
			return true;

		case MENU_CLEAR_MAP:

			for (Iterator<OpenStreetMapViewOverlay> iter = mOsmv.getOverlays()
					.iterator(); iter.hasNext();) {
				Object o = iter.next();
				if (MyOverLay.class.getName().equals(o.getClass().getName())) {
					iter.remove();
				}
			}
			return true;

		case MENU_ABOUT:
			showDialog(DIALOG_ABOUT_ID);
			return true;

		default: // Map mode submenu items
			this.mOsmv.setRenderer(OpenStreetMapRendererInfo.values()[item
					.getItemId() - 1000]);
		}
		return false;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;

		switch (id) {
		case DIALOG_ABOUT_ID:
			return new AlertDialog.Builder(baraj_mapa.this).setIcon(
					R.drawable.icon).setTitle(R.string.app_name).setMessage(
					R.string.about_message).setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					}).create();

		default:
			dialog = null;
			break;
		}
		return dialog;
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		return this.mOsmv.onTrackballEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE)
			this.mLocationOverlay.followLocation(false);

		return super.onTouchEvent(event);
	}

	private void createGpsDisabledAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder
				.setMessage(
						"Вашиот GPS е оневозможен! Потребно е да биде активиран, за нормално функционирање на апликацијата (пронаоѓање на Вашата локација). Дали би сакале да го овозможите?")
				.setIcon(R.drawable.icon).setTitle(R.string.app_name)
				.setCancelable(false).setPositiveButton("Овозможи GPS",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								showGpsOptions();
							}
						});
		builder.setNegativeButton("Откажи",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void showGpsOptions() {
		Intent gpsOptionsIntent = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(gpsOptionsIntent);
	}

	private void final_dialog(final String txt) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(txt).setIcon(R.drawable.marker_default).setTitle(
				R.string.app_name).setCancelable(false).setPositiveButton(
				"OK..", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void draw(GeoPoint srcGeoPoint, GeoPoint destGeoPoint,
			GeoPoint final_geo) {
		DrawPath(srcGeoPoint, destGeoPoint, Color.BLUE, mOsmv);
		mOsmv.invalidate();
		mOsmv.getOverlays().add(new MyOverLay(destGeoPoint));
		mOsmv.getOverlays().add(new MyOverLay(final_geo, Color.GREEN));
	}

	private void create_dialog(final OpenStreetMapViewOverlayItem item) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Кликнавте на: " + item.mDescription
						+ ", Дали сакате да стигнете до таму?").setIcon(
				R.drawable.icon).setTitle(R.string.app_name).setCancelable(
				false).setPositiveButton("Да!",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						Location current = mLocationOverlay.getLastFix();
						if (current != null) {

							String itemdesc = item.mDescription;

							int lon = item.mGeoPoint.getLongitudeE6();
							int lat = item.mGeoPoint.getLatitudeE6();
							String lons = Integer.toString(lon);
							String lats = Integer.toString(lat);

							new calc_stanica().execute(itemdesc, lons, lats);

						} else {
							final_dialog("Потребно е да ја најдеме Вашата моментална локација, Ве молиме почекајте...");
						}
					}
				});

		builder.setNegativeButton("Откажи",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}

	private void DrawPath(GeoPoint src, GeoPoint dest, int color,
			OpenStreetMapView mMapView01) {

		URL url = null;
		try {
			url = new URL("http://www.yournavigation.org/gosmore.php?"
					+ "flat=" + (src.getLatitudeE6() / 1000000.0) + "&"
					+ "flon=" + (src.getLongitudeE6() / 1000000.0) + "&"
					+ "tlat=" + (dest.getLatitudeE6() / 1000000.0) + "&"
					+ "tlon=" + (dest.getLongitudeE6() / 1000000.0) + "&"
					+ "v=" + "motorcar" + "&" + "fast=1&layer=mapnik");
		} catch (MalformedURLException e) {
			//
		}
		try {
			URLConnection conn = url.openConnection();
			String userAgent = HttpUserAgentHelper
					.getUserAgent(getBaseContext());
			if (userAgent != null)
				conn.setRequestProperty("User-Agent", userAgent);
			conn.setReadTimeout(30000);
			BufferedReader in = new BufferedReader(new InputStreamReader(conn
					.getInputStream()), 8192);
			StringBuilder kmlBuilder = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				kmlBuilder.append(line + "\n");
			}
			in.close();
			String kml = kmlBuilder.toString();

			String coords = null;

			if (kml.indexOf("<coordinates>") != -1) {
				coords = kml.substring(kml.indexOf("<coordinates>") + 14, kml
						.indexOf("</coordinates>"));

				// String test = coords.substring(0, coords.indexOf(','));
				// String test2 = coords.substring(coords.indexOf(',') + 1,
				// coords.indexOf('\n'));

				// Log.d("xxx", "TEST-d=" + coords + " tralala =" + test +
				// " NAJBITNO= " + test2);

				float lonRegularfirst = Float.parseFloat(coords.substring(0,
						coords.indexOf(',')));
				float latRegularfirst = Float.parseFloat(coords.substring(
						coords.indexOf(',') + 1, coords.indexOf('\n')).trim());

				// Log.d("xxx", "lonfirst=" + lonRegularfirst + " latfirst=" +
				// latRegularfirst);

				GeoPoint startGP = new GeoPoint(
						(int) (latRegularfirst * 1000000),
						(int) (lonRegularfirst * 1000000));
				mMapView01.getOverlays().add(new MyOverLay(src, startGP));
				mMapView01.getOverlays().add(new MyOverLay(startGP, startGP));
				GeoPoint gp1;
				GeoPoint gp2 = startGP;

				StringTokenizer tokenizer = new StringTokenizer(coords, "\n");

				while (tokenizer.hasMoreTokens()) {
					String coord = tokenizer.nextToken();
					gp1 = gp2;
					if ((coord != null) && (coord.indexOf(',') != -1)) {
						// yes, the data returned from the server is long, lat
						float lonRegular = Float.parseFloat(coord.substring(0,
								coord.indexOf(',')));
						float latRegular = Float.parseFloat(coord
								.substring(coord.indexOf(',') + 1));
						// Log.d("xxx", "Demek koordinati=" + coord);
						gp2 = new GeoPoint((int) (latRegular * 1000000),
								(int) (lonRegular * 1000000));
						mMapView01.getOverlays().add(new MyOverLay(gp1, gp2));
					}

				}
				mMapView01.getOverlays().add(new MyOverLay(gp2, dest));
				mMapView01.getOverlays().add(new MyOverLay(dest, dest));

			} else {
				throw new IOException();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public class calc_stanica extends
			AsyncTask<String, Void, ArrayList<String>> {
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(baraj_mapa.this);
			dialog.setTitle("Пресметуваме...");
			dialog.setMessage("Ве молиме почекајте...");
			dialog.setIndeterminate(true);
			dialog.show();
		}

		protected ArrayList<String> doInBackground(String... vlezni) {

			String itemdesc = vlezni[0];
			String final_lon = vlezni[1];
			String final_lat = vlezni[2];

			Location current = mLocationOverlay.getLastFix();

			DataBaseHelper db = new DataBaseHelper(null);
			db = new DataBaseHelper(getApplicationContext());

			try {

				db.createDataBase();

			} catch (IOException ioe) {

				throw new Error("Unable to create database");

			}

			try {

				db.openDataBase();

			} catch (SQLException sqle) {

				throw sqle;

			}

			db.getReadableDatabase();

			int min = 2000;
			GeoPoint g = null;
			ArrayList<String> korisni_linii = new ArrayList<String>();
			double glon = 0;
			double glat = 0;
			boolean b = false;
			boolean d = false;

			Cursor c = db.getStanica(itemdesc);
			if (c.moveToFirst()) {
				String _id = c.getString(0); // id na kliknata stanica...

				// Log.d("xxx", "ID na Kliknata Stanica: " + _id);

				Cursor c2 = db.getUsefulLinii(_id);
				if (c2.moveToFirst()) {
					do {
						String id_korisna_linija = c2.getString(0);
						// Log.d("xxx", "ID na Korisni Linie: " +
						// id_korisna_linija);
						// int newmin=3000;

						Cursor br = db.getRbr(_id, id_korisna_linija);
						int rbr_finaldest = br.getInt(0);

						Cursor c3 = db.getUsefulStanici(id_korisna_linija);
						if (c3.moveToFirst()) {
							do {
								String id_korisna_stanica = c3.getString(0);

								Cursor br_2 = db.getRbr(id_korisna_stanica,
										id_korisna_linija);
								int rbr_currentdest = br_2.getInt(0);

								Cursor nas_2 = db.getNasoka(id_korisna_stanica,
										id_korisna_linija);
								String nasoka_currentdest = nas_2.getString(0);

								// Log.d("xxx", "ID na Korisni Stanice: " +
								// id_korisna_stanica);

								if (current == null) {
									break;
								}
								GeoPoint currentG = new GeoPoint(current);

								Cursor c4 = db
										.getLocationStanici(id_korisna_stanica);
								// c4 ima samo eden rezultat
								if (c4.moveToFirst()) {
									glon = c4.getDouble(0);
									glat = c4.getDouble(1);
									GeoPoint stanica = new GeoPoint(glon, glat);
									int rastojanie = currentG
											.distanceTo(stanica);

									if (rbr_finaldest > rbr_currentdest
											&& nasoka_currentdest
													.equalsIgnoreCase("A")) {
										if (min > rastojanie) {
											min = rastojanie;
											g = new GeoPoint(glon, glat);
											d = true;
											// Log.d("xxx"," KOORDINATI NA G: "
											// + glon +" / "+ glat);
										}
									}

									else if (rbr_finaldest < rbr_currentdest
											&& nasoka_currentdest
													.equalsIgnoreCase("B")) {
										if (min > rastojanie) {
											min = rastojanie;
											g = new GeoPoint(glon, glat);
											d = true;
											// Log.d("xxx"," KOORDINATI NA G: "
											// + glon +" / "+ glat);
										}
									}

									if (min == rastojanie) {// najdena e linija
										// shto pominuva na
										// ista stanica
										b = true;
										// Log.d("xxx",
										// " VNATRE VO B rastojanie: "
										// +rastojanie+ "  Konachan minimum = "
										// + min);
									}
									// Log.d("xxx",
									// " RASTOJANIE do sekoja stanica: "
									// +rastojanie+ "  Nov minimum:  " + newmin
									// + "  Konachan minimum = " + min);
								}
							} while (c3.moveToNext());
						}

						if (d) {
							// Log.d("xxx",
							// "VLEZENO E VO D!!! - za najden pomal minimum...");
							korisni_linii.clear();
							Cursor cX = db.getLinija(id_korisna_linija);
							String ime_linija = cX.getString(0);
							korisni_linii.add(ime_linija);
							d = false;
							b = false;
						} else if (b) {
							// Log.d("xxx",
							// "VLEZENO E VO B!!! - za najden ist minimum...");
							Cursor cX = db.getLinija(id_korisna_linija);
							String ime_linija = cX.getString(0);
							korisni_linii.add(ime_linija);
							b = false;
						}
					} while (c2.moveToNext());
				}
			}
			db.close();

			ArrayList<String> izlezni = new ArrayList<String>();
			String korlin = korisni_linii.toString();
			String minim = Integer.toString(min);

			int lon = 0;
			int lat = 0;
			if (min < 2000) {
				lon = g.getLongitudeE6();
				lat = g.getLatitudeE6();
			}
			String closest_lon = Integer.toString(lon);
			String closest_lat = Integer.toString(lat);

			izlezni.add(minim);

			izlezni.add(closest_lon);
			izlezni.add(closest_lat);

			izlezni.add(final_lon);
			izlezni.add(final_lat);

			izlezni.add(korlin);

			return izlezni;
		}

		@Override
		public void onPostExecute(ArrayList<String> izlezni) {
			dialog.dismiss();

			String minim = izlezni.get(0);
			int min = Integer.parseInt(minim);

			String closest_lon = izlezni.get(1);
			String closest_lat = izlezni.get(2);

			int c_lon = Integer.parseInt(closest_lon);
			int c_lat = Integer.parseInt(closest_lat);

			GeoPoint g = new GeoPoint(c_lat, c_lon);

			String final_lon = izlezni.get(3);
			String final_lat = izlezni.get(4);

			int f_lon = Integer.parseInt(final_lon);
			int f_lat = Integer.parseInt(final_lat);

			GeoPoint final_geo = new GeoPoint(f_lat, f_lon);

			String korisni_linii = izlezni.get(5);

			if (min < 2000) {
				Location current = mLocationOverlay.getLastFix();
				GeoPoint srcGeoPoint = new GeoPoint(current);
				GeoPoint destGeoPoint = g;

				// Log.d("DDD",
				// "Geopointi shto se isprakjav: "+srcGeoPoint.toString() +" "+
				// destGeoPoint.toString()+" "+final_geo.toString());
				draw(srcGeoPoint, destGeoPoint, final_geo);
				mOsmv.setMapCenter(new GeoPoint(current));

				final_dialog("Појдете до означената постојка со црвено и почекајте автобус со број: "
						+ korisni_linii);
			} else
				final_dialog("За жал нема постојка во Ваша близина што поминува од овде, побарајте нова станица...");
		}
	}
}
