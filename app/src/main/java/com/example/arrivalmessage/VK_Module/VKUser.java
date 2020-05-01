package com.example.arrivalmessage.VK_Module;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class VKUser implements Parcelable {

    public int id ;
    public String firstname ;
    public String lastname ;
    public String photo ;
    public Boolean deactivated ;
    public Boolean isCheked;
    public Boolean hasAvatar;

    VKUser()
    {
        id = 0;
        firstname = "";
        lastname = "";
        photo = "";
        deactivated = false;
        isCheked=false;
        hasAvatar=false;

    }

    VKUser(Parcel parcel)
    {
        id = parcel.readInt();
        firstname = parcel.readString();
        lastname = parcel.readString();
        photo = parcel.readString();
        deactivated = parcel.readByte() != 0;
        isCheked=false;
        hasAvatar=false;
    }

    public static final Creator<VKUser> CREATOR = new Creator<VKUser>() {
        @Override
        public VKUser createFromParcel(Parcel source) {
            return new VKUser(source);
        }

        @Override
        public VKUser[] newArray(int size) {
            return new VKUser[size];
        }

    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(photo);

        Byte b = 0;
        if(deactivated)
            b=1;

        dest.writeByte(b);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    static public VKUser parse(JSONObject json)
    {
        VKUser user = new VKUser();
        user.id = json.optInt("id",0);
        user.firstname = json.optString("first_name", "");
        user.lastname = json.optString("last_name", "");
        user.photo = json.optString("photo_200", "");
        user.deactivated = json.optBoolean("deactivated", false);

        return user;
    }
}
