package vn.dangyen.onthitracnghiem.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Scanner;

import vn.dangyen.onthitracnghiem.R;
import vn.dangyen.onthitracnghiem.activity.ExamImportActivity;
import vn.dangyen.onthitracnghiem.model.Exam;
import vn.dangyen.onthitracnghiem.model.ExamImport;

import static android.app.Activity.RESULT_OK;

public class SelectFragment extends Fragment {

    public static final String EXTRA_RAW_CHECK = "false";
    public static final String EXTRA_RAW_TEXT = "";
    private ArrayList<ExamImport> mExams = new ArrayList<>();
    String data="";
    Button btnOpenfile;
    EditText txtLink;
    public String duongdan="ĐƯỜNG DẪN";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == RESULT_OK) {
            Uri selectedfile = data.getData();

            duongdan = selectedfile.toString();
            readData();
            readDStest();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select, container, false);
        btnOpenfile = (Button) view.findViewById(R.id.btnOpenfile);
        txtLink = (EditText) view.findViewById(R.id.editdata);
        btnOpenfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent().setType("json/json").setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
            }
        });
        return view;
    }

    public void readData()
    {
        String[] output = duongdan.split("///");
        String link = output[1];
        //Toast.makeText(getActivity(), sdcard, Toast.LENGTH_LONG).show();
        //Toast.makeText(getActivity(), link, Toast.LENGTH_LONG).show();
        try {
            Scanner scan=new Scanner(new File(link));
            while(scan.hasNext())
            {
                data+=scan.nextLine()+"\n";
            }
            scan.close();
            txtLink.setText(duongdan+"\n"+data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public  void readDStest()
    {
        Intent intent = new Intent(getActivity(), ExamImportActivity.class);
        intent.putExtra(EXTRA_RAW_CHECK,"true");
        intent.putExtra(EXTRA_RAW_TEXT,data);
        startActivity(intent);
    }

    public ArrayList<ExamImport> getExamImport() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ExamImport>>(){}.getType();
        String json = data;
        ArrayList<ExamImport> data = gson.fromJson(json, type);
        for (int i = 0; i < data.size(); i++){
            ExamImport exam = new ExamImport(
                    data.get(i).mSrcExam,
                    data.get(i).mSubject,
                    data.get(i).mExamNum,
                    data.get(i).mTime,
                    data.get(i).mQuestions
            );
            mExams.add(exam);
        }
        return mExams;
    }
}
