package vn.dangyen.onthitracnghiem.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import vn.dangyen.onthitracnghiem.R;
import vn.dangyen.onthitracnghiem.activity.ExamActivity;
import vn.dangyen.onthitracnghiem.adapter.ExamAdapter;
import vn.dangyen.onthitracnghiem.adapter.ListQuestionAdapter;
import vn.dangyen.onthitracnghiem.adapter.ViewQuestionAdapter;
import vn.dangyen.onthitracnghiem.adapter.ViewQuestionMathAdapter;
import vn.dangyen.onthitracnghiem.common.Common;
import vn.dangyen.onthitracnghiem.helper.IOHelper;
import vn.dangyen.onthitracnghiem.model.HighScore;
import vn.dangyen.onthitracnghiem.model.Question;

import static vn.dangyen.onthitracnghiem.fragment.HomeFragment.EXTRA_RAW_NAME;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    public static boolean checkResult;
    private String rawName;
    private int numExam;
    private long currentTime;

    private ArrayList<Question> mQuestions = new ArrayList<>();
    private ViewPager viewQuestion;
    private Button btnEndQuiz;
    private TextView tvCountdown;
    private Dialog dialog;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private int sumUnAnswered;
    private int sumCorrect;
    private double scoreInScaleTen;

    private long backPressTime;

    private boolean isDialogNotification = false;
    private boolean isDialogConfirm = false;
    private boolean isDialogListQuestion = false;
    private boolean isDialogTimeUp = false;

    //Lưu dữ liệu khi xoay màn hình
    private static final String KEY_QUESTION_LIST = "keyQuestionList";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_CHECK_RESULT = "keyCheckResult";
    private static final String KEY_IS_DIALOG_NOTIFICATION = "keyDialogNotification";
    private static final String KEY_IS_DIALOG_CONFIRM = "keyDialogConfirm";
    private static final String KEY_IS_DIALOG_LIST_QUESTION = "keyDialogListQuestion";
    private static final String KEY_IS_DIALOG_TIME_UP = "keyDialogTimeUp";
    private static final String KEY_UN_ANSWERED = "keyUnAnswered";
    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_SCORE_IN_SCALE_TEN = "keyScoreInScaleTen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        currentTime = System.currentTimeMillis();
        checkResult = false;
        initWidget();

        Intent intent = getIntent();
        rawName = intent.getStringExtra(ExamAdapter.EXTRA_RAW_FILE_NAME);
        numExam = intent.getIntExtra(ExamAdapter.EXTRA_EXAM_NUM, 0);
        int time = intent.getIntExtra(ExamAdapter.EXTRA_EXAM_TIME, 0);
        timeLeftInMillis = time * 60 * 1000;

        if (savedInstanceState == null) {
            IOHelper helper = new IOHelper(this);
            try {
                mQuestions = helper.getQuestion(Common.getId(rawName, R.raw.class), numExam);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //Lấy dữ liệu trước khi xoay màn hình
            mQuestions = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            checkResult = savedInstanceState.getBoolean(KEY_CHECK_RESULT);
            sumUnAnswered = savedInstanceState.getInt(KEY_UN_ANSWERED);
            sumCorrect = savedInstanceState.getInt(KEY_SCORE);
            scoreInScaleTen = savedInstanceState.getDouble(KEY_SCORE_IN_SCALE_TEN);
            isDialogNotification = savedInstanceState.getBoolean(KEY_IS_DIALOG_NOTIFICATION);
            isDialogConfirm = savedInstanceState.getBoolean(KEY_IS_DIALOG_CONFIRM);
            isDialogListQuestion = savedInstanceState.getBoolean(KEY_IS_DIALOG_LIST_QUESTION);
            isDialogTimeUp = savedInstanceState.getBoolean(KEY_IS_DIALOG_TIME_UP);
        }
        showViewQuestion();
        if (!checkResult){
            startCountdown();
        } else {
            updateCountdownText();
        }

        if (isDialogNotification)
            showDialogNotification();
        else if (isDialogConfirm)
            showDialogConfirm();
        else if (isDialogListQuestion)
            showDialogListQuestion();
        else if (isDialogTimeUp)
            showDialogTimeUp();
        else
            dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEndQuiz:
                if (!checkResult)
                    showDialogNotification();
                else
                    finishQuiz();
                break;
            case R.id.btnListQuestion:
                showDialogListQuestion();
                break;

            // Dialog Notification:
            case R.id.btnDlNotifyEnd:
                dialog.dismiss();
                removeCheckDialog();
                showDialogConfirm();
                break;
            case R.id.btnDlNotifyReturn:
                dialog.dismiss();
                removeCheckDialog();
                break;

            //DialogConfirm
            case R.id.btnDlConfirmOK:
                dialog.dismiss();
                countDownTimer.cancel();
                removeCheckDialog();
                showResultUI();
                break;
            case R.id.btnDlConfirmCancel:
                dialog.dismiss();
                removeCheckDialog();
                break;

            //Dialog ListQuestion
            case R.id.btnDlListQ_EndQuiz:
                dialog.dismiss();
                removeCheckDialog();
                showDialogNotification();
                break;
            case R.id.btnDlListQ_Return:
                dialog.dismiss();
                removeCheckDialog();
                break;

            //Dialog TimeUp
            case R.id.btnDlTimeUpOK:
                dialog.dismiss();
                removeCheckDialog();
                showResultUI();
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, mQuestions);
        outState.putBoolean(KEY_CHECK_RESULT, checkResult);
        outState.putInt(KEY_UN_ANSWERED, sumUnAnswered);
        outState.putInt(KEY_SCORE, sumCorrect);
        outState.putDouble(KEY_SCORE_IN_SCALE_TEN, scoreInScaleTen);
        outState.putBoolean(KEY_IS_DIALOG_NOTIFICATION, isDialogNotification);
        outState.putBoolean(KEY_IS_DIALOG_CONFIRM, isDialogConfirm);
        outState.putBoolean(KEY_IS_DIALOG_LIST_QUESTION, isDialogListQuestion);
        outState.putBoolean(KEY_IS_DIALOG_TIME_UP, isDialogTimeUp);
    }

    @Override
    public void onBackPressed() {
        if (backPressTime + 2000 > System.currentTimeMillis()){
            finishQuiz();
        } else {
            Toast.makeText(this, "Nhấn  " + '"' + "TRỞ VỀ" + '"' + "  lần nữa để thoát", Toast.LENGTH_SHORT).show();
        }
        backPressTime = System.currentTimeMillis();
    }

    //hàm hiện thị câu hỏi
    private void showViewQuestion(){
        ViewQuestionAdapter viewQuestionAdapter = new ViewQuestionAdapter(mQuestions, this);
        ViewQuestionMathAdapter viewQuestionMathAdapter = new ViewQuestionMathAdapter(this, mQuestions);

        if (rawName.equals("ex_math") || rawName.equals("ex_chemical") || rawName.equals("ex_physical")) {
            viewQuestion.setAdapter(viewQuestionMathAdapter);
            Toast.makeText(this, "Đến view Math", Toast.LENGTH_SHORT).show();
        } else {
            viewQuestion.setAdapter(viewQuestionAdapter);
        }
        if (checkResult)
            btnEndQuiz.setText(R.string.close);
    }

    private void startCountdown(){
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountdownText();
                showDialogTimeUp();
            }
        }.start();
    }

    private void updateCountdownText() {
        int minutes = (int) ((timeLeftInMillis / 1000) / 60);
        int seconds = (int) ((timeLeftInMillis / 1000) % 60);
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tvCountdown.setText(timeFormatted);
        if (timeLeftInMillis <= 5 * 60 * 1000) {
            tvCountdown.setTextColor(Color.RED);
        }
    }

    private void initWidget(){
        viewQuestion = findViewById(R.id.viewQuestion);
        ImageButton btnListQuestion = findViewById(R.id.btnListQuestion);
        btnListQuestion.setOnClickListener(this);
        btnEndQuiz = findViewById(R.id.btnEndQuiz);
        btnEndQuiz.setOnClickListener(this);
        tvCountdown = findViewById(R.id.tvCountdown);

        dialog = new Dialog(this);
        if (dialog.getWindow() != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
        }
    }

    private void showDialogNotification(){
        removeCheckDialog();
        dialog.setContentView(R.layout.dialog_notification);
        TextView tvDlNotifyMessage = dialog.findViewById(R.id.tvDlNotifyMessage);
        Button btnDlNotifyReturn = dialog.findViewById(R.id.btnDlNotifyReturn);
        btnDlNotifyReturn.setOnClickListener(this);
        Button btnDlNotifyEnd = dialog.findViewById(R.id.btnDlNotifyEnd);
        btnDlNotifyEnd.setOnClickListener(this);

        int unAns = 0;
        for (int i = 0; i < mQuestions.size(); i++){
            if (mQuestions.get(i).mChoice == 0)
                unAns ++;
        }

        if (unAns > 0){
            tvDlNotifyMessage.setText(String.format(Locale.getDefault(), "Vẫn còn %d/%d câu chưa được trả lời." +
                    "\nBạn có muốn kết thúc bài thi?", unAns, mQuestions.size()));
            dialog.show();
            isDialogNotification = true;
        } else
            showDialogConfirm();
    }

    private void showDialogConfirm(){
        removeCheckDialog();
        dialog.setContentView(R.layout.dialog_confirm);
        Button btnDlConfirmOK = dialog.findViewById(R.id.btnDlConfirmOK);
        btnDlConfirmOK.setOnClickListener(this);
        Button btnDlConfirmCancel = dialog.findViewById(R.id.btnDlConfirmCancel);
        btnDlConfirmCancel.setOnClickListener(this);
        dialog.show();
        isDialogConfirm = true;
    }

    private void showDialogListQuestion(){
        removeCheckDialog();
        dialog.setContentView(R.layout.dialog_list_question);
        TextView dlListQ_Title = dialog.findViewById(R.id.dlListQ_Title);
        GridView dlListQ_GridItem = dialog.findViewById(R.id.dlListQ_GridItem);
        Button btnDlListQ_EndQuiz = dialog.findViewById(R.id.btnDlListQ_EndQuiz);
        btnDlListQ_EndQuiz.setOnClickListener(this);
        Button btnDlListQ_Return = dialog.findViewById(R.id.btnDlListQ_Return);
        btnDlListQ_Return.setOnClickListener(this);

        LinearLayout layoutResult = dialog.findViewById(R.id.layoutResult);
        TextView resultTrue = dialog.findViewById(R.id.resultTrue);
        TextView resultFalse = dialog.findViewById(R.id.resultFalse);
        TextView resultUnChoice = dialog.findViewById(R.id.resultUnChoice);
        TextView resultScore = dialog.findViewById(R.id.resultScore);

        ListQuestionAdapter listQuestionAdapter = new ListQuestionAdapter(this, mQuestions);
        dlListQ_GridItem.setAdapter(listQuestionAdapter); //hiện thị list gridItem câu trả lời - đáp an

        if (!checkResult) {
            dlListQ_Title.setText(R.string.title_quiz_ques);
        }
        else {
            dlListQ_Title.setText(R.string.title_quiz_result);
            btnDlListQ_EndQuiz.setVisibility(View.GONE); //nút
            layoutResult.setVisibility(View.VISIBLE); //layout info
            resultTrue.setText(String.valueOf(sumCorrect)); //đúng
            resultFalse.setText(String.valueOf(mQuestions.size() - sumUnAnswered - sumCorrect)); //sai
            resultUnChoice.setText(String.valueOf(sumUnAnswered)); //chưa trả lời
            resultScore.setText(String.valueOf(scoreInScaleTen));  //điểm
        }

        dialog.show();
        isDialogListQuestion = true;

        //sự kiện click thì load ra câu hỏi đó
        dlListQ_GridItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                removeCheckDialog();
                viewQuestion.setCurrentItem(position);
            }
        });
    }

    private void showDialogTimeUp(){
        removeCheckDialog();
        dialog.setContentView(R.layout.dialog_time_up);
        Button btnDlTimeUpOK = dialog.findViewById(R.id.btnDlTimeUpOK);
        btnDlTimeUpOK.setOnClickListener(this);
        dialog.show();
        isDialogTimeUp = true;
    }

    private void parseResult(){
        //Tính điểm
        for (int i = 0; i < mQuestions.size(); i++) {
            int choice = mQuestions.get(i).mChoice;
            if (choice == 0)
                sumUnAnswered++;
            if (mQuestions.get(i).mChoice == mQuestions.get(i).mAnsTrue)
                sumCorrect++;
        }
        scoreInScaleTen = ((double) sumCorrect / (double) mQuestions.size()) * 10;
    }

    private void showResultUI(){
        checkResult = true;
        parseResult();
        showDialogListQuestion();
        showViewQuestion();
        saveScore();
    }

    private void saveScore(){
        //Load điểm cũ trong SharedPrefs
        ArrayList<HighScore> mHighScores;
        SharedPreferences pref = getSharedPreferences("shared_prefs", MODE_PRIVATE);

        Gson gson = new Gson();
        String key_prefs = rawName + "_" + numExam; //Tên String lưu trong SharePrefs
        String json = pref.getString(key_prefs, null);
        Type type = new TypeToken<ArrayList<HighScore>>(){}.getType();
        mHighScores = gson.fromJson(json, type);
        if (mHighScores == null){
            mHighScores = new ArrayList<>();
        }

        //Add điểm lần này vào SharedPrefs
        HighScore highScore = new HighScore(
                rawName, numExam, currentTime, mQuestions.size(), sumUnAnswered, sumCorrect, scoreInScaleTen);
        mHighScores.add(highScore);

        //Sort dữ liệu theo điểm (từ cao đến thấp)
        Collections.sort(mHighScores, new Comparator<HighScore>() {
            @Override
            public int compare(HighScore o1, HighScore o2) {
                //Toast.makeText(QuizActivity.this, "Sort dữ liệu theo điểm (từ cao đến thấp): "+Double.compare(o2.mScore, o1.mScore), Toast.LENGTH_SHORT).show();
                return Double.compare(o2.mScore, o1.mScore);

            }
        });

        HighScore highScoresMax = mHighScores.get(0);

        //Sort dư liệu theo ngày làm bài thi
        Collections.sort(mHighScores, new Comparator<HighScore>() {
            @Override
            public int compare(HighScore o1, HighScore o2) {
                return Double.compare(o2.mDate, o1.mDate);
            }
        });

        //Nếu size ArrayList<HightScore> > 6 -->
        // xóa điểm thứ 5(chỉ lưu giữ 5 lần thi gần nhất)
        if (mHighScores.size() > 6){
            mHighScores.remove(mHighScores.size()-2);
        }

        //Add điểm cao nhất vào SharesPrefs
        if (mHighScores.size() > 1){
            mHighScores.remove(mHighScores.size()-1);
        }
        mHighScores.add(highScoresMax);

        //Lưu giữ liệu vào SharedPrefs
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("check", "checked");
        String jsonNew = gson.toJson(mHighScores);
        editor.putString(key_prefs, jsonNew);
        editor.apply();
    }

    private void removeCheckDialog(){
        isDialogNotification = false;
        isDialogConfirm = false;
        isDialogListQuestion = false;
        isDialogTimeUp = false;
    }

    private void finishQuiz(){
        countDownTimer.cancel();
        removeCheckDialog();
        Intent intentResult = new Intent(this, ExamActivity.class);
        intentResult.putExtra(EXTRA_RAW_NAME, rawName);
        intentResult.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentResult);
    }

}
