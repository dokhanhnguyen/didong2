package vn.dangyen.onthitracnghiem.model;

import com.google.gson.annotations.SerializedName;

public class Subject {
    @SerializedName("name")
    public String mName;

    @SerializedName("icon")
    public String mIcon;

    @SerializedName("src_exam")
    public String mSrcExam;
    @SerializedName("info")
    public String mInfo;

    public Subject(String name, String icon, String srcExam, String info) {
        this.mName = name;
        this.mIcon = icon;
        this.mSrcExam = srcExam;
        this.mInfo = info;
    }
}
