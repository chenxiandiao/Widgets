package cxd.com.programlearning.widgets.flowlayout;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Rancune@126.com on 2016/11/25.
 */

public class SimpleUser implements Serializable, Parcelable {
    private static final long serialVersionUID = 8211873736320206742L;

    public String mUid;
    public String mNickName;
    public String mAvatar;
    public String mGender;
    public String mSignature;
    public boolean mIsProfessional;

    public SimpleUser() {
        mUid = "";
        mNickName = "";
        mAvatar = "";
        mGender = "";
        mSignature = "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mUid);
        dest.writeString(this.mNickName);
        dest.writeString(this.mAvatar);
        dest.writeString(this.mGender);
        dest.writeString(this.mSignature);
        dest.writeByte(this.mIsProfessional ? (byte) 1 : (byte) 0);
    }

    protected SimpleUser(Parcel in) {
        this.mUid = in.readString();
        this.mNickName = in.readString();
        this.mAvatar = in.readString();
        this.mGender = in.readString();
        this.mSignature = in.readString();
        this.mIsProfessional = in.readByte() != 0;
    }

    public static final Creator<SimpleUser> CREATOR = new Creator<SimpleUser>() {
        @Override
        public SimpleUser createFromParcel(Parcel source) {
            return new SimpleUser(source);
        }

        @Override
        public SimpleUser[] newArray(int size) {
            return new SimpleUser[size];
        }
    };
}
