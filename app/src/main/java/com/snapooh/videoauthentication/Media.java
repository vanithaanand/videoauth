package com.snapooh.videoauthentication;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by snapooh on 29/9/15.
 */
public class Media implements Parcelable {
    private int type;
    private String tpath;
    private String  uri;
    public Media(int type,String thumnail,String uri){
        this.type=type;
        this.tpath=thumnail;
        this.uri=uri;
    }

    public int getType() {
        return type;
    }

    public String getTpath() {
        return tpath;

    }

    public String getUri() {
        return uri;
    }

    private Media(Parcel in){
        this.type=in.readInt();
        this.tpath=in.readString();
        this.uri=in.readString();
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel in) {
         //   Log.i(TAG, "Create From Parcel");
            return new Media(in);
        }

        @Override
        public Media[] newArray(int size) {
            //Log.i(TAG,"new Array");
            return new Media[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(type);
        dest.writeString(tpath);
        dest.writeString(uri);

    }
}
