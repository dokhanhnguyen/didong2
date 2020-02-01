package vn.dangyen.onthitracnghiem.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import vn.dangyen.onthitracnghiem.R;
import vn.dangyen.onthitracnghiem.activity.QuizActivity;
import vn.dangyen.onthitracnghiem.common.Common;
import vn.dangyen.onthitracnghiem.model.Question;

public class ViewQuestionAdapter extends PagerAdapter {
    private ArrayList<Question> mQuestions;
    private LayoutInflater inflater;
    private Context mContext;

    public ViewQuestionAdapter(ArrayList<Question> questions, Context context) {
        this.mQuestions = questions;
        this.mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mQuestions.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        View view = inflater.inflate(R.layout.view_question, container, false);

        container.addView(view);

        final TextView tvQuestionCount = view.findViewById(R.id.tvQuestionCount);
        tvQuestionCount.setText(String.format(Locale.getDefault(), "Câu %d/%d", position + 1, mQuestions.size()));

        final TextView tvQuestionStatus = view.findViewById(R.id.tvQuestionStatus);

        final LinearLayout layoutImg = view.findViewById(R.id.layoutImg);
        final ImageView imgQues = view.findViewById(R.id.imgQues);

        if (mQuestions.get(position).mImg == null){
            layoutImg.setVisibility(View.GONE);
        } else {
            layoutImg.setVisibility(View.VISIBLE);
            imgQues.setImageBitmap(Common.getBitmapFromAssets(mContext, mQuestions.get(position).mImg));
            imgQues.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.showImageDialog(mContext, mQuestions.get(position).mImg);
                }
            });
        }

        final TextView tvQuestion = view.findViewById(R.id.tvQuestion);
        tvQuestion.setText(mQuestions.get(position).mQues);

        final TextView tvAnsA, tvAnsB, tvAnsC, tvAnsD;

        tvAnsA = view.findViewById(R.id.tvAnsA);
        tvAnsB = view.findViewById(R.id.tvAnsB);
        tvAnsC = view.findViewById(R.id.tvAnsC);
        tvAnsD = view.findViewById(R.id.tvAnsD);

        tvAnsA.setText(mQuestions.get(position).mAnsA);
        tvAnsB.setText(mQuestions.get(position).mAnsB);
        tvAnsC.setText(mQuestions.get(position).mAnsC);
        tvAnsD.setText(mQuestions.get(position).mAnsD);

        if (mQuestions.get(position).mChoice == 1)
            tvAnsA.setBackgroundResource(R.drawable.answer_choice);
        else if (mQuestions.get(position).mChoice == 2)
            tvAnsB.setBackgroundResource(R.drawable.answer_choice);
        else if (mQuestions.get(position).mChoice == 3)
            tvAnsC.setBackgroundResource(R.drawable.answer_choice);
        else if (mQuestions.get(position).mChoice == 4)
            tvAnsD.setBackgroundResource(R.drawable.answer_choice);
        else {
            tvAnsA.setBackgroundResource(R.drawable.answer_normal);
            tvAnsB.setBackgroundResource(R.drawable.answer_normal);
            tvAnsC.setBackgroundResource(R.drawable.answer_normal);
            tvAnsD.setBackgroundResource(R.drawable.answer_normal);
        }

        if (!QuizActivity.checkResult) {
            tvAnsA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mQuestions.get(position).mChoice = 1;
                    tvAnsA.setBackgroundResource(R.drawable.answer_choice);
                    tvAnsB.setBackgroundResource(R.drawable.answer_normal);
                    tvAnsC.setBackgroundResource(R.drawable.answer_normal);
                    tvAnsD.setBackgroundResource(R.drawable.answer_normal);
                }
            });

            tvAnsB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mQuestions.get(position).mChoice = 2;
                    tvAnsB.setBackgroundResource(R.drawable.answer_choice);
                    tvAnsA.setBackgroundResource(R.drawable.answer_normal);
                    tvAnsC.setBackgroundResource(R.drawable.answer_normal);
                    tvAnsD.setBackgroundResource(R.drawable.answer_normal);
                }
            });

            tvAnsC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mQuestions.get(position).mChoice = 3;
                    tvAnsC.setBackgroundResource(R.drawable.answer_choice);
                    tvAnsA.setBackgroundResource(R.drawable.answer_normal);
                    tvAnsB.setBackgroundResource(R.drawable.answer_normal);
                    tvAnsD.setBackgroundResource(R.drawable.answer_normal);
                }
            });


            tvAnsD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mQuestions.get(position).mChoice = 4;
                    tvAnsD.setBackgroundResource(R.drawable.answer_choice);
                    tvAnsA.setBackgroundResource(R.drawable.answer_normal);
                    tvAnsB.setBackgroundResource(R.drawable.answer_normal);
                    tvAnsC.setBackgroundResource(R.drawable.answer_normal);
                }
            });
        } else {
            tvQuestionStatus.setVisibility(View.VISIBLE);

            if (mQuestions.get(position).mChoice == 0){
                tvQuestionStatus.setText("Chưa trả lời");
            } else if (mQuestions.get(position).mChoice == mQuestions.get(position).mAnsTrue){
                tvQuestionStatus.setText("Đúng");
            } else {
                tvQuestionStatus.setText("Sai");
            }

            if (mQuestions.get(position).mAnsTrue == 1)
                tvAnsA.setBackgroundResource(R.drawable.answer_true);
            else if (mQuestions.get(position).mAnsTrue == 2)
                tvAnsB.setBackgroundResource(R.drawable.answer_true);
            else if (mQuestions.get(position).mAnsTrue == 3)
                tvAnsC.setBackgroundResource(R.drawable.answer_true);
            else if (mQuestions.get(position).mAnsTrue == 4)
                tvAnsD.setBackgroundResource(R.drawable.answer_true);
        }

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }

}
