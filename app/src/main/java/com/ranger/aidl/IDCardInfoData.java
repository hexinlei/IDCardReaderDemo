package com.ranger.aidl;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by hexinlei on 2017/4/13.
 */
public class IDCardInfoData implements Parcelable {

    private int photolength;
    private byte[] content;
    private byte[] photo;
    private String name;
    private String sex;
    private String nation;
    private String birth;
    private String address;
    private String id;
    private String depart;
    private String validityTime;

    protected IDCardInfoData(Parcel in) {
        photolength = in.readInt();
        content = in.createByteArray();
        photo = in.createByteArray();
        name = in.readString();
        sex = in.readString();
        nation = in.readString();
        birth = in.readString();
        address = in.readString();
        id = in.readString();
        depart = in.readString();
        validityTime = in.readString();
    }

    public IDCardInfoData(int photolength, byte[] photo, String name, String id) {
        this.photolength = photolength;
        this.photo = photo;
        this.name = name;
        this.id = id;
    }

    public static final Creator<IDCardInfoData> CREATOR = new Creator<IDCardInfoData>() {
        @Override
        public IDCardInfoData createFromParcel(Parcel in) {
            return new IDCardInfoData(in);
        }

        @Override
        public IDCardInfoData[] newArray(int size) {
            return new IDCardInfoData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(photolength);
        dest.writeByteArray(content);
        dest.writeByteArray(photo);
        dest.writeString(name);
        dest.writeString(sex);
        dest.writeString(nation);
        dest.writeString(birth);
        dest.writeString(address);
        dest.writeString(id);
        dest.writeString(depart);
        dest.writeString(validityTime);
    }


    public int getPhotolength() {
        return photolength;
    }

    public void setPhotolength(int photolength) {
        this.photolength = photolength;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getValidityTime() {
        return validityTime;
    }

    public void setValidityTime(String validityTime) {
        this.validityTime = validityTime;
    }
}
