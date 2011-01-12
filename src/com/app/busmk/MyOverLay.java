// Created by Drakuwa
/**
 * 
 * @author Bojan Ilievski - Drakuwa
 *
 */
package com.app.busmk;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlay;
import org.andnav.osm.views.OpenStreetMapView.OpenStreetMapViewProjection;

public class MyOverLay extends OpenStreetMapViewOverlay
{
	
private GeoPoint gp1;
private GeoPoint gp2;
private int mRadius=5;
int color;

private String text="";
private Bitmap img = null;

public MyOverLay(GeoPoint gp1,GeoPoint gp2)
{
this.gp1 = gp1;
this.gp2 = gp2;
}

public MyOverLay(GeoPoint gp1)
{
this.gp1 = gp1;
}

public MyOverLay(GeoPoint gp1, int color) {
	this.gp1 = gp1;
	this.color = Color.GREEN;
}

public void setText(String t)
{
this.text = t;
}
public void setBitmap(Bitmap bitmap)
{
this.img = bitmap;
}

@Override
public void onDraw(Canvas canvas, OpenStreetMapView mapView)
{
	
if(gp2!=null)	
{	
final OpenStreetMapViewProjection projection = mapView.getProjection();

Paint paint = new Paint();
paint.setAntiAlias(true);
Point point = new Point();
projection.toMapPixels(gp1, point);

paint.setColor(Color.BLUE);
paint.setAlpha(120);

RectF oval=new RectF(point.x - mRadius, point.y - mRadius,
point.x + mRadius, point.y + mRadius);
// start point

canvas.drawOval(oval, paint);

Point point2 = new Point();
projection.toMapPixels(gp2, point2);
paint.setStrokeWidth(7);
paint.setAlpha(120);
canvas.drawLine(point.x, point.y, point2.x,point2.y, paint);

}
if(gp2==null){
	final OpenStreetMapViewProjection projection = mapView.getProjection();
	int radius = 20;
	Paint paint = new Paint();
	paint.setAntiAlias(true);
	Point point = new Point();
	projection.toMapPixels(gp1, point);
	if(color==Color.GREEN){
		paint.setColor(color);
	}
	else paint.setColor(Color.RED);
	
	paint.setAlpha(120);
	RectF oval=new RectF(point.x - radius, point.y - radius,
	point.x + radius, point.y + radius);
	canvas.drawOval(oval, paint);
}

}
@Override
protected void onDrawFinished(Canvas c, OpenStreetMapView osmv) {
	// TODO Auto-generated method stub
	
}

}

