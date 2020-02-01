package vn.dangyen.onthitracnghiem.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Exam {
    @SerializedName("src_exam")
    public String mSrcExam;

    @SerializedName("subject")
    public String mSubject;

    @SerializedName("exam_num")
    public int mExamNum;

    @SerializedName("time")
    public int mTime;

    @SerializedName("questions")
    public ArrayList<vn.dangyen.onthitracnghiem.model.Question> mQuestions;

    public Exam( String subject, int examNum, int time, ArrayList<vn.dangyen.onthitracnghiem.model.Question> questions) {
        this.mSubject = subject;
        this.mExamNum = examNum;
        this.mTime = time;
        this.mQuestions = questions;
    }
}
