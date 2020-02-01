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
import vn.dangyen.onthitracnghiem.activity.ExamImportActivity;
import vn.dangyen.onthitracnghiem.activity.QuizImportActivity;
import vn.dangyen.onthitracnghiem.common.Common;
import vn.dangyen.onthitracnghiem.model.ExamImport;
import vn.dangyen.onthitracnghiem.model.HighScoreImport;

public class ExamImportAdapter extends RecyclerView.Adapter<ExamImportAdapter.ViewHolder> {
    //public static final String EXTRA_RAW_FILE_NAME123 = "extraRawFileName";
    public static final String EXTRA_RAW_FILE_TEXT = "extraRawFileName";
    public static final String EXTRA_EXAM_NUM = "extraExamNum";
    public static final String EXTRA_EXAM_TIME = "extraExamTime";
    public static final String EXTRA_TEST = "Con mèo"; //đây là mã môn học nha raw_name


    private Context mContext;
    private ArrayList<ExamImport> mExams;
    private ArrayList<ArrayList<HighScoreImport>> mArrHighScoresImport;

    public ExamImportAdapter(Context context, ArrayList<ExamImport> exams, ArrayList<ArrayList<HighScoreImport>> arrHighScores) {
        this.mContext = context;
        this.mExams = exams;
        this.mArrHighScoresImport = arrHighScores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamImportAdapter.ViewHolder holder, int position) {
        final ExamImport exam = mExams.get(position);
        final String rawName = ExamImportActivity.rawName;
        final String rawText = ExamImportActivity.rawText;
        final ArrayList<HighScoreImport> mHighScores = mArrHighScoresImport.get(position);

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
                Intent intent = new Intent(mContext, QuizImportActivity.class);
                //intent.putExtra(EXTRA_RAW_FILE_NAME123, "Con mèo óc bò");
                //Toast.makeText(mContext, rawName, Toast.LENGTH_SHORT).show();
                intent.putExtra(EXTRA_EXAM_NUM, exam.mExamNum);
                intent.putExtra(EXTRA_EXAM_TIME, exam.mTime);
                intent.putExtra(EXTRA_RAW_FILE_TEXT, rawText);
                intent.putExtra(EXTRA_TEST, rawName);

                //Toast.makeText(mContext,"Lựa chọn ở Adapter: /"+ rawName+"/"+exam.mExamNum+"/"+exam.mTime, Toast.LENGTH_SHORT).show();
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
