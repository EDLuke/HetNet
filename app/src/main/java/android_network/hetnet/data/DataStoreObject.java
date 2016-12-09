package android_network.hetnet.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lanking on 06/12/2016.
 */

public class DataStoreObject implements Parcelable {
    protected DataStoreObject(Parcel in) {
    }

    public static final Creator<DataStoreObject> CREATOR = new Creator<DataStoreObject>() {
        @Override
        public DataStoreObject createFromParcel(Parcel in) {
            return new DataStoreObject(in);
        }

        @Override
        public DataStoreObject[] newArray(int size) {
            return new DataStoreObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
