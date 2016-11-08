package by.mtz.reminder.DAO;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by Roma on 04.10.2016.
 */

public class Action extends SugarRecord implements Parcelable {
    String name;//database url https://activities-54cd5.firebaseio.com/
    long date;
    String description;
    int actionId;
    boolean repeating;

    public boolean isRepeating() {
        return repeating;
    }

    public void setRepeating(boolean repeating) {
        this.repeating = repeating;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Action() {}

    public Action(String name, long date, String description, int actionId, boolean repeating) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.actionId = actionId;
        this.repeating = repeating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Action action = (Action) o;

        if (getDate() != action.getDate()) return false;
        if (getActionId() != action.getActionId()) return false;
        if (isRepeating() != action.isRepeating()) return false;
        if (getName() != null ? !getName().equals(action.getName()) : action.getName() != null)
            return false;
        return getDescription() != null ? getDescription().equals(action.getDescription()) : action.getDescription() == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (int) (getDate() ^ (getDate() >>> 32));
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + getActionId();
        result = 31 * result + (isRepeating() ? 1 : 0);
        return result;
    }

    protected Action(Parcel in) {
        name = in.readString();
        date = in.readLong();
        description = in.readString();
        actionId = in.readInt();
        repeating = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(date);
        dest.writeString(description);
        dest.writeInt(actionId);
        dest.writeByte((byte) (repeating ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Action> CREATOR = new Parcelable.Creator<Action>() {
        @Override
        public Action createFromParcel(Parcel in) {
            return new Action(in);
        }

        @Override
        public Action[] newArray(int size) {
            return new Action[size];
        }
    };
}