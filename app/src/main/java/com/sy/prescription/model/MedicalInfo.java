package com.sy.prescription.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ygs on 2018/2/3.
 */

public class MedicalInfo implements Parcelable {
    public String name;
    public int num = 1;

    public MedicalInfo() {

    }

    protected MedicalInfo(Parcel in) {
        name = in.readString();
        num = in.readInt();
    }

    public static final Creator<MedicalInfo> CREATOR = new Creator<MedicalInfo>() {
        @Override
        public MedicalInfo createFromParcel(Parcel in) {
            return new MedicalInfo(in);
        }

        @Override
        public MedicalInfo[] newArray(int size) {
            return new MedicalInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(num);
    }
}
