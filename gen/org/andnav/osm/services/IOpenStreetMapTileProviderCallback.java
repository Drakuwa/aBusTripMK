/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /root/Documents/workspace/aBusTripMK/src/org/andnav/osm/services/IOpenStreetMapTileProviderCallback.aidl
 */
package org.andnav.osm.services;
import java.lang.String;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;
import android.os.Parcel;
public interface IOpenStreetMapTileProviderCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.andnav.osm.services.IOpenStreetMapTileProviderCallback
{
private static final java.lang.String DESCRIPTOR = "org.andnav.osm.services.IOpenStreetMapTileProviderCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an IOpenStreetMapTileProviderCallback interface,
 * generating a proxy if needed.
 */
public static org.andnav.osm.services.IOpenStreetMapTileProviderCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.andnav.osm.services.IOpenStreetMapTileProviderCallback))) {
return ((org.andnav.osm.services.IOpenStreetMapTileProviderCallback)iin);
}
return new org.andnav.osm.services.IOpenStreetMapTileProviderCallback.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_mapTileRequestCompleted:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
java.lang.String _arg4;
_arg4 = data.readString();
this.mapTileRequestCompleted(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.andnav.osm.services.IOpenStreetMapTileProviderCallback
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/**
 * The map tile request has completed.
 * @param rendererID
 * @param zoomLevel
 * @param tileX
 * @param tileY
 * @param aTilePath the path of the requested tile, or null if request has completed without returning a tile path 
 */
public void mapTileRequestCompleted(int rendererID, int zoomLevel, int tileX, int tileY, java.lang.String aTilePath) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(rendererID);
_data.writeInt(zoomLevel);
_data.writeInt(tileX);
_data.writeInt(tileY);
_data.writeString(aTilePath);
mRemote.transact(Stub.TRANSACTION_mapTileRequestCompleted, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_mapTileRequestCompleted = (IBinder.FIRST_CALL_TRANSACTION + 0);
}
/**
 * The map tile request has completed.
 * @param rendererID
 * @param zoomLevel
 * @param tileX
 * @param tileY
 * @param aTilePath the path of the requested tile, or null if request has completed without returning a tile path 
 */
public void mapTileRequestCompleted(int rendererID, int zoomLevel, int tileX, int tileY, java.lang.String aTilePath) throws android.os.RemoteException;
}
