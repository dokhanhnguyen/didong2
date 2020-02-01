package vn.dangyen.onthitracnghiem.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import vn.dangyen.onthitracnghiem.R;
import vn.dangyen.onthitracnghiem.adapter.ExamImportAdapter;
import vn.dangyen.onthitracnghiem.fragment.SelectFragment;
import vn.dangyen.onthitracnghiem.helper.IOHelper;
import vn.dangyen.onthitracnghiem.model.ExamImport;
import vn.dangyen.onthitracnghiem.model.HighScoreImport;

public class ExamImportActivity extends AppCompatActivity {

    public static String rawName="Con mèo";
    public static String rawCheck="";
    public static String rawText="";

    private ArrayList<ExamImport> mExamsImport = new ArrayList<>();
    private ArrayList<ArrayList<HighScoreImport>> mArrHighScoresImport = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        Toolbar toolbar = findViewById(R.id.exam_toolbar);
        setSupportActionBar(toolbar);


        IOHelper helper = new IOHelper(this);
        Intent getIntent = getIntent();

        try {
            rawCheck = getIntent.getStringExtra(SelectFragment.EXTRA_RAW_CHECK);
            rawText = getIntent.getStringExtra(SelectFragment.EXTRA_RAW_TEXT);
        }
        catch (Exception e)
        {
            Toast.makeText(ExamImportActivity.this, "Lỗi "+ e.toString(), Toast.LENGTH_LONG).show();
        }

        RecyclerView recyclerExam = findViewById(R.id.recycler_exam);

        if( rawCheck.equals("true"))
        {
            try {
                mExamsImport = helper.getExamImport(rawText);
                //rawText="";
                rawCheck="";
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        TextView tvName = findViewById(R.id.tvExamName);
        tvName.setText(String.format("Môn: %s", mExamsImport.get(0).mSubject));
        rawName = mExamsImport.get(0).mSrcExam;
        //Toast.makeText(this, rawName, Toast.LENGTH_SHORT).show();
        //tvName.setText(tvName.getText().toString()+rawName);
        loadHighScore();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerExam.setLayoutManager(layoutManager);

        ExamImportAdapter adapter = new ExamImportAdapter(this, mExamsImport, mArrHighScoresImport);
        recyclerExam.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_exam, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_ex_home:
                loadHomePage();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        loadHomePage();
    }

    private void loadHighScore(){
        SharedPreferences pref = getSharedPreferences("shared_prefs_import", Context.MODE_PRIVATE);
        for (int i = 0; i < mExamsImport.size(); i++){
            String key_prefs = rawName + "_" + mExamsImport.get(i).mExamNum;
            Gson gson = new Gson();
            String json = pref.getString(key_prefs, null);
            Type type = new TypeToken<ArrayList<HighScoreImport>>(){}.getType();
            ArrayList<HighScoreImport> mHighScores = gson.fromJson(json, type);
            if (mHighScores == null){
                HighScoreImport highScore = new HighScoreImport(null, 0, 0, 0, 0, 0, 0);
                mHighScores = new ArrayList<>();
                mHighScores.add(highScore);
            }
            mArrHighScoresImport.add(mHighScores);
        }
    }

    private void loadHomePage(){
        Intent intent = new Intent(this, vn.dangyen.onthitracnghiem.activity.MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

