package by.mtz.reminder.DAO;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by Roma on 19.10.2016.
 */

public class Picture extends SugarRecord implements Parcelable {
    String name;
    String base64;

    public Picture() {
    }

    public Picture(String name, String base64) {
        this.name = name;
        this.base64 = base64;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Picture picture1 = (Picture) o;

        if (getName() != null ? !getName().equals(picture1.getName()) : picture1.getName() != null)
            return false;
        return getBase64() != null ? getBase64().equals(picture1.getBase64()) : picture1.getBase64() == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getBase64() != null ? getBase64().hashCode() : 0);
        return result;
    }

    protected Picture(Parcel in) {
        name = in.readString();
        base64 = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(base64);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Picture> CREATOR = new Parcelable.Creator<Picture>() {
        @Override
        public Picture createFromParcel(Parcel in) {
            return new Picture(in);
        }

        @Override
        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };
}