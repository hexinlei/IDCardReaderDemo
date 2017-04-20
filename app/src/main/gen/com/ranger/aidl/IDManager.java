/*___Generated_by_IDEA___*/

/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/hexinlei/Documents/develop/IDCardReaderDemo/IDCardReaderDemo/app/src/main/java/com/ranger/aidl/IDManager.aidl
 */
package com.ranger.aidl;
// Declare any non-default types here with import statements

public interface IDManager extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.ranger.aidl.IDManager
{
private static final java.lang.String DESCRIPTOR = "com.ranger.aidl.IDManager";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.ranger.aidl.IDManager interface,
 * generating a proxy if needed.
 */
public static com.ranger.aidl.IDManager asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.ranger.aidl.IDManager))) {
return ((com.ranger.aidl.IDManager)iin);
}
return new com.ranger.aidl.IDManager.Stub.Proxy(obj);
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
case TRANSACTION_getCheckResult:
{
data.enforceInterface(DESCRIPTOR);
com.ranger.aidl.IDCardInfoData _arg0;
if ((0!=data.readInt())) {
_arg0 = com.ranger.aidl.IDCardInfoData.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
java.lang.String _result = this.getCheckResult(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.ranger.aidl.IDManager
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
@Override public java.lang.String getCheckResult(com.ranger.aidl.IDCardInfoData info) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((info!=null)) {
_data.writeInt(1);
info.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_getCheckResult, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getCheckResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public java.lang.String getCheckResult(com.ranger.aidl.IDCardInfoData info) throws android.os.RemoteException;
}
