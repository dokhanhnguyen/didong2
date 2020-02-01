package vn.dangyen.onthitracnghiem.helper;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Scanner;

import vn.dangyen.onthitracnghiem.R;
import vn.dangyen.onthitracnghiem.model.Exam;
import vn.dangyen.onthitracnghiem.model.ExamImport;
import vn.dangyen.onthitracnghiem.model.Question;
import vn.dangyen.onthitracnghiem.model.Subject;

public class IOHelper {
    private Context mContext;
    private ArrayList<Subject> mSubjects = new ArrayList<>();
    private ArrayList<Exam> mExams = new ArrayList<>();
    private ArrayList<ExamImport> mExamsImport = new ArrayList<>();
    private ArrayList<Question> mQuestions = new ArrayList<>();

    public IOHelper(Context context) {
        this.mContext = context;
    }

    private String stringFromRaw(Context context, int rawID) throws IOException {
        InputStream is = context.getResources().openRawResource(rawID);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null){
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }

    //hàm đọc từ bộ nhớ
    public String readData()
    {
        String sdcard= Environment
                .getExternalStorageDirectory()
                .getAbsolutePath()+"/subject.json";
        try {
            Scanner scan=new Scanner(new File(sdcard));
            String data="";
            while(scan.hasNext())
            {
                data+=scan.nextLine()+"\n";
            }
            scan.close();
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
    //hàm đọc tên môn học
    public ArrayList<Subject> getSubject() throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Subject>>(){}.getType();
        String json = stringFromRaw(mContext, R.raw.subject);
        String jsonData = readData();
        ArrayList<Subject> data = gson.fromJson(json, type);
        for (int i = 0; i < data.size(); i++){
            Subject subject = new Subject(
                    data.get(i).mName,
                    data.get(i).mIcon,
                    data.get(i).mSrcExam,
                    data.get(i).mInfo
            );
            mSubjects.add(subject);
        }
        return mSubjects;
    }

    //hàm đọc thông tin thi của môn học IMPORT
    public ArrayList<ExamImport> getExamImport(String rawText) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ExamImport>>(){}.getType();
        String json = rawText;
        ArrayList<ExamImport> data = gson.fromJson(json, type);
        for (int i = 0; i < data.size(); i++){
            ExamImport examImport = new ExamImport(
                    data.get(i).mSrcExam,
                    data.get(i).mSubject,
                    data.get(i).mExamNum,
                    data.get(i).mTime,
                    data.get(i).mQuestions
            );
            mExamsImport.add(examImport);
        }
        return mExamsImport;
    }


    //hàm đọc thông tin thi của môn học
    public ArrayList<Exam> getExam(int rawID) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Exam>>(){}.getType();
        String json = stringFromRaw(mContext, rawID);
        ArrayList<Exam> data = gson.fromJson(json, type);
        for (int i = 0; i < data.size(); i++){
            Exam exam = new Exam(
                    data.get(i).mSubject,
                    data.get(i).mExamNum,
                    data.get(i).mTime,
                    data.get(i).mQuestions
            );
            mExams.add(exam);
        }
        return mExams;
    }


    //hàm đọc câu hỏi kiểm tra
    public ArrayList<Question> getQuestion(int rawID, int examNum) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Exam>>(){}.getType();
        String json =stringFromRaw(mContext, rawID);
        //Toast.makeText(mContext, json, Toast.LENGTH_SHORT).show();
        ArrayList<Exam> data = gson.fromJson(json, type);
        for (int i = 0; i < data.size(); i++){
            if (data.get(i).mExamNum == examNum){
                for (int j = 0; j < data.get(i).mQuestions.size(); j++){
                    Question question = new Question(
                            data.get(i).mQuestions.get(j).mQues,
                            data.get(i).mQuestions.get(j).mImg,
                            data.get(i).mQuestions.get(j).mAnsA,
                            data.get(i).mQuestions.get(j).mAnsB,
                            data.get(i).mQuestions.get(j).mAnsC,
                            data.get(i).mQuestions.get(j).mAnsD,
                            data.get(i).mQuestions.get(j).mAnsTrue,
                            data.get(i).mQuestions.get(j).mChoice
                    );
                    mQuestions.add(question);
                }
            }
        }
        return mQuestions;
    }


    //hàm đọc câu hỏi kiểm tra IMPORT
    public ArrayList<Question> getQuestionImport(String rawText, int examNum) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ExamImport>>(){}.getType();
        String json = rawText;
        //Toast.makeText(mContext, json, Toast.LENGTH_SHORT).show();
        ArrayList<ExamImport> data = gson.fromJson(json, type);
        for (int i = 0; i < data.size(); i++){
            if (data.get(i).mExamNum == examNum){
                for (int j = 0; j < data.get(i).mQuestions.size(); j++){
                    Question question = new Question(
                            data.get(i).mQuestions.get(j).mQues,
                            data.get(i).mQuestions.get(j).mImg,
                            data.get(i).mQuestions.get(j).mAnsA,
                            data.get(i).mQuestions.get(j).mAnsB,
                            data.get(i).mQuestions.get(j).mAnsC,
                            data.get(i).mQuestions.get(j).mAnsD,
                            data.get(i).mQuestions.get(j).mAnsTrue,
                            data.get(i).mQuestions.get(j).mChoice
                    );
                    mQuestions.add(question);
                }
            }
        }
        return mQuestions;
    }
}
