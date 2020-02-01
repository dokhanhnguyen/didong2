package vn.dangyen.onthitracnghiem.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ExamImport {
    @SerializedName("src_exam")
    public String mSrcExam;

    @SerializedName("subject")
    public String mSubject;

    @SerializedName("exam_num")
    public int mExamNum;

    @SerializedName("time")
    public int mTime;

    @SerializedName("questions")
    public ArrayList<Question> mQuestions;

    public ExamImport( String srcExam, String subject, int examNum, int time, ArrayList<vn.dangyen.onthitracnghiem.model.Question> questions) {
        this.mSrcExam = srcExam;
        this.mSubject = subject;
        this.mExamNum = examNum;
        this.mTime = time;
        this.mQuestions = questions;
    }
}

