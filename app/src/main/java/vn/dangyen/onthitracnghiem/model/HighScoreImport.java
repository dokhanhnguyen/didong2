package vn.dangyen.onthitracnghiem.model;

import com.google.gson.annotations.SerializedName;

public class HighScoreImport {
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

    public HighScoreImport(String rawName, int examNum, long date, int sumQues, int unAns, int correct, double score) {
        this.mRawName = rawName; //mã môn
        this.mExamNum = examNum; //mã đề
        this.mDate = date; //ngày thi
        this.mSumQues = sumQues; //tổng câu hỏi
        this.mUnAns = unAns; //tổng câu hỏi chưa trả lời
        this.mCorrect = correct; //câu trả lời đúng
        this.mScore = score; //điểm
    }
}
