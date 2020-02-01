package vn.dangyen.onthitracnghiem.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Question implements Parcelable {

    @SerializedName("ques")
    public String mQues;

    @SerializedName("img")
    public String mImg;

    @SerializedName("ans_a")
    public String mAnsA;

    @SerializedName("ans_b")
    public String mAnsB;

    @SerializedName("ans_c")
    public String mAnsC;

    @SerializedName("ans_d")
    public String mAnsD;

    @SerializedName("ans_true")
    public int mAnsTrue;

    public int mChoice;

    public Question(String ques, String img, String ansA, String ansB, String ansC, String ansD, int ansTrue, int choice) {
        this.mQues = ques;
        this.mImg = img;
        this.mAnsA = ansA;
        this.mAnsB = ansB;
        this.mAnsC = ansC;
        this.mAnsD = ansD;
        this.mAnsTrue = ansTrue;
        this.mChoice = choice;
    }

    private Question(Parcel in){
        mQues = in.readString();
        mImg = in.readString();
        mAnsA = in.readString();
        mAnsB = in.readString();
        mAnsC = in.readString();
        mAnsD = in.readString();
        mAnsTrue = in.readInt();
        mChoice = in.readInt();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mQues);
        dest.writeString(mImg);
        dest.writeString(mAnsA);
        dest.writeString(mAnsB);
        dest.writeString(mAnsC);
        dest.writeString(mAnsD);
        dest.writeInt(mAnsTrue);
        dest.writeInt(mChoice);
    }
}
