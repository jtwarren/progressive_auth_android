/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Volumes/android/ProgressiveAuthentication/ProgressiveAuthentication/src/com/android/progressiveauthentication/IAuthService.aidl
 */
package com.android.progressiveauthentication;
public interface IAuthService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.android.progressiveauthentication.IAuthService
{
private static final java.lang.String DESCRIPTOR = "com.android.progressiveauthentication.IAuthService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.android.progressiveauthentication.IAuthService interface,
 * generating a proxy if needed.
 */
public static com.android.progressiveauthentication.IAuthService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.android.progressiveauthentication.IAuthService))) {
return ((com.android.progressiveauthentication.IAuthService)iin);
}
return new com.android.progressiveauthentication.IAuthService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
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
case TRANSACTION_authorized:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.authorized(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_setAuthLevel:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setAuthLevel(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.android.progressiveauthentication.IAuthService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/*
	* public interface
	*/
@Override public boolean authorized(java.lang.String packageName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
mRemote.transact(Stub.TRANSACTION_authorized, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/*
	* private interface
	*/
@Override public void setAuthLevel(int level) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(level);
mRemote.transact(Stub.TRANSACTION_setAuthLevel, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_authorized = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_setAuthLevel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
/*
	* public interface
	*/
public boolean authorized(java.lang.String packageName) throws android.os.RemoteException;
/*
	* private interface
	*/
public void setAuthLevel(int level) throws android.os.RemoteException;
}
