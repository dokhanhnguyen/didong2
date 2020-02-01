package vn.dangyen.onthitracnghiem.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import vn.dangyen.onthitracnghiem.R;
import vn.dangyen.onthitracnghiem.activity.ExamActivity;
import vn.dangyen.onthitracnghiem.activity.ExamImportActivity;
import vn.dangyen.onthitracnghiem.activity.QuizActivity;
import vn.dangyen.onthitracnghiem.common.Common;
import vn.dangyen.onthitracnghiem.model.Exam;
import vn.dangyen.onthitracnghiem.model.HighScore;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder>{

    public static final String EXTRA_RAW_FILE_NAME = "extraRawFileName";
    public static final String EXTRA_EXAM_NUM = "extraExamNum";
    public static final String EXTRA_EXAM_TIME = "extraExamTime";

    private Context mContext;
    private ArrayList<Exam> mExams;
    private ArrayList<ArrayList<HighScore>> mArrHighScores;

    public ExamAdapter(Context context, ArrayList<Exam> exams, ArrayList<ArrayList<HighScore>> arrHighScores) {
        this.mContext = context;
        this.mExams = exams;
        this.mArrHighScores = arrHighScores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Exam exam = mExams.get(position);
        final String rawName = ExamActivity.rawName;
        final ArrayList<HighScore> mHighScores = mArrHighScores.get(position);

        holder.tvExamNumber.setText(String.format(Locale.getDefault(), "Đề số %d", position + 1));

        if (mHighScores.get(0).mRawName != null) {
            holder.tvItemExHighScore.setText(String.format(Locale.getDefault(),
                    "Điểm cao nhất: %s", mHighScores.get(mHighScores.size()-1).mScore));
            holder.tvItemExLastQuiz.setText(String.format(Locale.getDefault(), "Lần thi cuối: %s",
                    Common.unixTimeToDate(mHighScores.get(0).mDate)));
        } else {
            holder.tvItemExHighScore.setText("Điểm thi:");
            holder.tvItemExLastQuiz.setText("Chưa có dữ liệu");;
        }

        holder.layout_item_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, QuizActivity.class);
                intent.putExtra(EXTRA_RAW_FILE_NAME, rawName);
                intent.putExtra(EXTRA_EXAM_NUM, exam.mExamNum);
                intent.putExtra(EXTRA_EXAM_TIME, exam.mTime);
               // Toast.makeText(mContext, rawName+"/"+exam.mExamNum+"/"+exam.mTime, Toast.LENGTH_SHORT).show();
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExams.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvExamNumber, tvItemExHighScore, tvItemExLastQuiz;
        LinearLayout layout_item_exam;

        ViewHolder(View itemView) {
            super(itemView);
            tvExamNumber = itemView.findViewById(R.id.tvExamNumber);
            tvItemExHighScore = itemView.findViewById(R.id.tvItemExHighScore);
            tvItemExLastQuiz = itemView.findViewById(R.id.tvItemExLastQuiz);
            layout_item_exam = itemView.findViewById(R.id.layout_item_exam);
        }
    }
}
