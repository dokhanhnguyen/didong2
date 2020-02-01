package vn.dangyen.onthitracnghiem.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import vn.dangyen.onthitracnghiem.adapter.ScoreAdapter;
import vn.dangyen.onthitracnghiem.R;
import vn.dangyen.onthitracnghiem.adapter.SpinnerSubjectAdapter;
import vn.dangyen.onthitracnghiem.common.Common;
import vn.dangyen.onthitracnghiem.helper.IOHelper;
import vn.dangyen.onthitracnghiem.model.Exam;
import vn.dangyen.onthitracnghiem.model.HighScore;
import vn.dangyen.onthitracnghiem.model.Subject;

public class ScoreboardFragment extends Fragment {

    private LinearLayout layout_data_highscore, layout_hs_max, layout_hs_last;
    private Spinner spinnerSubject, spinnerExam;
    private TextView tvHSMax, tvHSDate, tvHSScoreLast, tvHSLastDate, tvHSStatus;
    private RecyclerView recycler_score;

    private ArrayList<Subject> mSubject = new ArrayList<>();
    private ArrayList<Exam> mExams = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scoreboard, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            layout_data_highscore = getActivity().findViewById(R.id.layout_data_highscore);
            layout_hs_max = getActivity().findViewById(R.id.layout_hs_max);
            layout_hs_last = getActivity().findViewById(R.id.layout_hs_last);
            tvHSStatus = getActivity().findViewById(R.id.tvHSStatus);
            spinnerSubject = getActivity().findViewById(R.id.spinnerSubject);
            tvHSMax = getActivity().findViewById(R.id.tvHSMax);
            tvHSDate = getActivity().findViewById(R.id.tvHSDate);
            tvHSScoreLast = getActivity().findViewById(R.id.tvHSLast);
            tvHSLastDate = getActivity().findViewById(R.id.tvHSLastDate);
            recycler_score = getActivity().findViewById(R.id.recycler_score);
            spinnerExam = getActivity().findViewById(R.id.spinnerExam);
        }

        setDataSpinnerSubject();
        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Subject currentSubject = (Subject) parent.getItemAtPosition(position);
                setDataHighScore(currentSubject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setDataSpinnerSubject() {
        IOHelper helper = new IOHelper(getActivity());
        try {
            mSubject = helper.getSubject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SpinnerSubjectAdapter adapter = new SpinnerSubjectAdapter(getActivity(), mSubject);
        spinnerSubject.setAdapter(adapter);
    }

    private void setDataHighScore(Subject subject) {
        if (subject.mSrcExam.equals("")) {
            setDataNull();
        } else {
            tvHSStatus.setVisibility(View.GONE);
            layout_data_highscore.setVisibility(View.VISIBLE);
            String rawName = subject.mSrcExam;
            SharedPreferences pref = getActivity().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            IOHelper helper = new IOHelper(getActivity());
            try {
                mExams = helper.getExam(Common.getId(rawName, R.raw.class));
            } catch (IOException e) {
                e.printStackTrace();
            }

            final ArrayList<ArrayList<HighScore>> mArrHighScores = new ArrayList<>();
            final ArrayList<HighScore> arrHighScoresMax = new ArrayList<>();
            final ArrayList<HighScore> arrHighScoresLast = new ArrayList<>();
            final String[] exams = new String[mExams.size()];
            boolean checkData = false;
            for (int i = 0; i < mExams.size(); i++) {
                exams[i] = "Đề số " + mExams.get(i).mExamNum;

                String key_prefs = rawName + "_" + mExams.get(i).mExamNum;
                Gson gson = new Gson();
                String json = pref.getString(key_prefs, null);
                Type type = new TypeToken<ArrayList<HighScore>>(){}.getType();
                ArrayList<HighScore> mHighScores = gson.fromJson(json, type);
                if (mHighScores == null) {
                    HighScore highScore = new HighScore(null, 0, 0, 0, 0, 0, 0);
                    mHighScores = new ArrayList<>();
                    mHighScores.add(highScore);
                } else {
                    checkData = true;
                }
                mArrHighScores.add(mHighScores);
                arrHighScoresMax.add(mHighScores.get(mHighScores.size()-1));
                arrHighScoresLast.add(mHighScores.get(0));
            }

            if (!checkData){
                setDataNull();
            } else {
                //Set dữ liệu điểm cao nhất của môn học
                Collections.sort(arrHighScoresMax, new Comparator<HighScore>() {
                    @Override
                    public int compare(HighScore o1, HighScore o2) {
                        return Double.compare(o2.mDate, o1.mDate);
                    }
                });

                Collections.sort(arrHighScoresMax, new Comparator<HighScore>() {
                    @Override
                    public int compare(HighScore o1, HighScore o2) {
                        return Double.compare(o2.mScore, o1.mScore);
                    }
                });
                tvHSMax.setText(String.valueOf(arrHighScoresMax.get(0).mScore));
                tvHSDate.setText(String.format("%s\n%s", Common.unixTimeToDate(arrHighScoresMax.get(0).mDate),
                        Common.unixTimeToTime(arrHighScoresMax.get(0).mDate)));
                layout_hs_max.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.showDialogHighScore(getActivity(), arrHighScoresMax, 0);
                    }
                });

                //Set dữ liệu lần thi gần nhất
                Collections.sort(arrHighScoresLast, new Comparator<HighScore>() {
                    @Override
                    public int compare(HighScore o1, HighScore o2) {
                        return Double.compare(o2.mDate, o1.mDate);
                    }
                });
                tvHSScoreLast.setText(String.valueOf(arrHighScoresLast.get(0).mScore));
                tvHSLastDate.setText(String.format("%s\n%s", Common.unixTimeToDate(arrHighScoresLast.get(0).mDate),
                        Common.unixTimeToTime(arrHighScoresLast.get(0).mDate)));
                layout_hs_last.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.showDialogHighScore(getActivity(), arrHighScoresLast, 0);
                    }
                });
            }

            ArrayAdapter<String> adapterExam = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, exams);
            adapterExam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerExam.setAdapter(adapterExam);
            spinnerExam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ArrayList<HighScore> arrHighScores = mArrHighScores.get(position);
                    ArrayList<HighScore> arrHighScoresExam = new ArrayList<>();
                    if (arrHighScores.size() > 1) {
                        for (int i = 0; i < arrHighScores.size() - 1; i++) {
                            arrHighScoresExam.add(arrHighScores.get(i));
                        }
                    } else {
                        arrHighScoresExam.addAll(arrHighScores);
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recycler_score.setLayoutManager(layoutManager);

                    ScoreAdapter adapter = new ScoreAdapter(getActivity(), arrHighScoresExam);
                    recycler_score.setAdapter(adapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void setDataNull(){
        tvHSStatus.setVisibility(View.VISIBLE);
        layout_data_highscore.setVisibility(View.GONE);
        tvHSStatus.setText("Chưa có dữ liệu");
    }
}
