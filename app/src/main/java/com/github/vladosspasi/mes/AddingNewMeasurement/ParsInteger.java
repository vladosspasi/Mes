package com.github.vladosspasi.mes.AddingNewMeasurement;

import android.os.Parcel;
import android.os.Parcelable;

//Класс для передачи данных инт между фрагментами
//Было необходимо это сделать изза особенностей передачи данных

public class ParsInteger implements Parcelable {

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    protected ParsInteger(Parcel in) {
    }

    protected ParsInteger(int in) {
        value = in;
    }

    public static final Creator<ParsInteger> CREATOR = new Creator<ParsInteger>() {
        @Override
        public ParsInteger createFromParcel(Parcel in) {
            return new ParsInteger(in);
        }

        @Override
        public ParsInteger[] newArray(int size) {
            return new ParsInteger[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
