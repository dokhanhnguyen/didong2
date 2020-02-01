package vn.dangyen.onthitracnghiem.model;

import com.google.gson.annotations.SerializedName;

public class HighScore {
    @SerializedName("raw_name")
    public String mRawName;

    @SerializedName("exam_num")
    public int mExamNum;

    @SerializedName("date")
    public long mDate;

    @SerializedName("sum_ques")
    public int mSumQues;

    @SerializedName("un_ans")
    public int mUnAns;

    @SerializedName("correct")
    public int mCorrect;

    @SerializedName("score")
    public double mScore;

    public HighScore(String rawName, int examNum, long date, int sumQues, int unAns, int correct, double score) {
        this.mRawName = rawName;
        this.mExamNum = examNum;
        this.mDate = date;
        this.mSumQues = sumQues;
        this.mUnAns = unAns;
        this.mCorrect = correct;
        this.mScore = score;
    }
}
