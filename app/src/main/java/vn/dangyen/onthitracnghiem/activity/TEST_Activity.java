package vn.dangyen.onthitracnghiem.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import vn.dangyen.onthitracnghiem.R;
import vn.dangyen.onthitracnghiem.adapter.ExamImportAdapter;

public class TEST_Activity extends AppCompatActivity {
    private String rawName="Name";
    private String rawText="Text";
    private int numExam=0;
    private long currentTime;
    private int time =0;
    EditText txtrawName;
    EditText txtnumExam;
    EditText txtmTime;
    EditText txtrawText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();
        rawName = intent.getStringExtra(ExamImportAdapter.EXTRA_TEST);
        //Toast.makeText(this, intent.getStringExtra(ExamImportAdapter.EXTRA_RAW_FILE_NAME123), Toast.LENGTH_SHORT).show();
        rawText = intent.getStringExtra(ExamImportAdapter.EXTRA_RAW_FILE_TEXT);
        numExam = intent.getIntExtra(ExamImportAdapter.EXTRA_EXAM_NUM, 0);
        int time = intent.getIntExtra(ExamImportAdapter.EXTRA_EXAM_TIME, 0);

        txtrawName = (EditText) findViewById(R.id.txtrawName);
        txtnumExam = (EditText) findViewById(R.id.txtnumExam);
        txtmTime = (EditText) findViewById(R.id.txtmTime);
        txtrawText = (EditText) findViewById(R.id.txtrawText);

        txtrawName.setText(rawName);
        txtnumExam.setText(numExam+"");
        txtmTime.setText(time+"");
        txtrawText.setText(rawText);

    }
}
